package kolok2._16_facultyTest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}

class Student implements Comparable<Student> {
    private final String id;
    private final int yearsOfStudies;
    private int coursesPassed;
    private final Map<Integer, Map<String, Integer>> gradesByCoursesAndTerm;
    private final List<Integer> allGrades;
    private final TreeSet<String> coursesAttended;

    public Student(String id, int yearsOfStudies) {
        this.id = id;
        this.yearsOfStudies = yearsOfStudies;
        coursesPassed = 0;
        gradesByCoursesAndTerm = new HashMap<>();
        IntStream.range(1, yearsOfStudies * 2 + 1).forEach(i -> gradesByCoursesAndTerm.put(i, new HashMap<>()));
        allGrades = new ArrayList<>();
        coursesAttended = new TreeSet<>();
    }

    public int getCoursesPassed() {
        return coursesPassed;
    }

    public void addGrade(int term, String courseName, int grade) throws OperationNotAllowedException {
        if (term > yearsOfStudies * 2)
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s", term, id));
        if (gradesByCoursesAndTerm.get(term).size() == 3)
            throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d", id, term));
        gradesByCoursesAndTerm.get(term).put(courseName, grade);
        allGrades.add(grade);
        coursesAttended.add(courseName);

        Course.COURSES.putIfAbsent(courseName, new Course(courseName));
        Course.COURSES.get(courseName).addStudent(this);
        Course.COURSES.get(courseName).addGrade(grade);

        if (grade > 5) coursesPassed++;
    }

    public boolean graduated() {
        return coursesPassed == yearsOfStudies * 6;
    }

    public double avgGrade() {
        return allGrades.stream().mapToInt(value -> value).average().orElse(5);
    }

    private double avgGradeForTerm(int term) {
        return gradesByCoursesAndTerm.get(term).values().stream().mapToInt(value -> value).average().orElse(5);
    }

    public String getLog() {
        return String.format("Student with ID %s graduated with average grade %.2f in %d years.", id, avgGrade(), yearsOfStudies);
    }

    public String getId() {
        return id;
    }

    private String coursesAttended() {
        return String.join(",", coursesAttended);
    }

    public String detailedReport() {
        List<String> terms = new ArrayList<>();
        String term = "Student: " + id;
        terms.add(term);
        for (Integer integer : gradesByCoursesAndTerm.keySet()) {
            int courses = gradesByCoursesAndTerm.get(integer).size();
            term = String.format("Term %d\nCourses: %d\nAverage grade for term: %.2f",
                    integer,
                    courses,
                    avgGradeForTerm(integer));
            terms.add(term);
        }
        term = String.format("Average grade: %.2f\nCourses attended: ", avgGrade()) + coursesAttended();
        terms.add(term);
        return String.join("\n", terms);
    }

    public String shortReport() {
        return String.format("Student: %s Courses passed: %d Average grade: %.2f", id, coursesPassed, avgGrade());
    }

    @Override
    public int compareTo(Student o) {
        Comparator<Student> comparator = Comparator.comparing(Student::getCoursesPassed)
                .thenComparing(Student::avgGrade)
                .thenComparing(Student::getId);
        return comparator.reversed().compare(this, o);
    }
}

class Course implements Comparable<Course> {
    static TreeMap<String, Course> COURSES = new TreeMap<>();
    List<Integer> grades;
    String courseName;
    List<Student> students;

    public Course(String courseName) {
        grades = new ArrayList<>();
        this.courseName = courseName;
        students = new ArrayList<>();
    }

    public int countStudents() {
        return students.size();
    }

    public String getCourseName() {
        return courseName;
    }

    public double averageGrade() {
        return grades.stream().mapToInt(grade -> grade).average().orElse(0);
    }

    public void addStudent(Student s) {
        students.add(s);
    }

    public void addGrade(int grade) {
        grades.add(grade);
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f", courseName, countStudents(), averageGrade());
    }

    @Override
    public int compareTo(Course o) {
        Comparator<Course> comparator = Comparator.comparing(Course::countStudents)
                .thenComparing(Course::averageGrade)
                .thenComparing(Course::getCourseName);
        return comparator.compare(this, o);
    }
}

class Faculty {
    private final Map<String, Student> studentsById;
    private final List<String> logs;

    public Faculty() {
        studentsById = new HashMap<>();
        logs = new ArrayList<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        studentsById.put(id, new Student(id, yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        Student student = studentsById.get(studentId);
        student.addGrade(term, courseName, grade);
        if (student.graduated()) {
            logs.add(student.getLog());
            studentsById.remove(studentId);
        }
    }

    String getFacultyLogs() {
        return String.join("\n", logs);
    }

    String getDetailedReportForStudent(String id) {
        return studentsById.get(id).detailedReport();
    }

    void printFirstNStudents(int n) {
        AtomicInteger i = new AtomicInteger();
        studentsById.values()
                .stream()
                .sorted()
                .filter(value -> i.incrementAndGet() <= n)
                .forEach(student -> System.out.println(student.shortReport()));
    }

    void printCourses() {
        Course.COURSES.values().stream().sorted().forEach(System.out::println);
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase == 10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase == 11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i = 11; i < 15; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
