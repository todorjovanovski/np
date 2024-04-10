package lab2.contacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.text.DecimalFormat;
import java.util.Scanner;

abstract class Contact {
    private String date;

    public Contact(String date) {
        this.date = date;
    }

    private double toDouble(String str) {
        String result = "";
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i) != '-') {
                result += str.charAt(i);
            }
        }
        return Double.parseDouble(result);
    }

    public boolean isNewerThan(Contact c) {
        return (toDouble(date) > toDouble(c.date));
    }

    public abstract String getType();
}

class EmailContact extends Contact {
    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String toString() {
        return "\"" + email + "\"";
    }
}

class PhoneContact extends Contact {
    private String phone;
    private enum Operator { VIP, ONE, TMOBILE}

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        String str = "";
        str += phone.charAt(2);
        int op = Integer.parseInt(str);
        if (op >= 0 && op <= 2) return Operator.TMOBILE;
        else if (op == 5 || op == 6) return Operator.ONE;
        else if(op == 7 || op == 8) return Operator.VIP;
        return null;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        return "\"" + phone + "\"";
    }

}

class Student {
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
/*    private int size;*/
    private ArrayList<Contact> contacts;


    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
/*        this.size = 0;*/
        contacts = new ArrayList<>();
    }

    public void addEmailContact(String date, String email) {
        Contact emailContact = new EmailContact(date, email);
        contacts.add(emailContact);
    }

    public void addPhoneContact(String date, String phone) {
        Contact phoneContact = new PhoneContact(date, phone);
        contacts.add(phoneContact);
    }

    public Contact[] getEmailContacts() {
        int size = 0;
        for (Contact value : contacts) {
            if (value.getType().equals("Email"))
                size++;
        }
        Contact[] emailContacts = new Contact[size];
        int counter = 0;
        for (Contact contact : contacts) {
            if (contact.getType().equals("Email"))
                emailContacts[counter++] = contact;
        }
        return emailContacts;
    }

    public Contact[] getPhoneContacts() {
        int size = 0;
        for (Contact value : contacts) {
            if (value.getType().equals("Phone"))
                size++;
        }
        Contact[] phoneContacts = new Contact[size];
        int counter = 0;
        for (Contact contact : contacts) {
            if (contact.getType().equals("Phone"))
                phoneContacts[counter++] = contact;
        }
        return phoneContacts;
    }

    public int getNumberOfContacts() {
        return contacts.size();
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        Contact latestContact = contacts.get(0);
        for(Contact contact : contacts) {
            if(contact.isNewerThan(latestContact))
                latestContact = contact;
        }
        return latestContact;
    }


    @Override
    public String toString() {
        return "{\"" +
                "ime\":\"" + firstName + "\"" +
                ", \"prezime\":\"" + lastName + "\"" +
                ", \"vozrast\":" + age  +
                ", \"grad\":\"" + city + "\"" +
                ", \"indeks\":" + index +
                ", \"telefonskiKontakti\":" + Arrays.toString(getPhoneContacts()) +
                ", \"emailKontakti\":" + Arrays.toString(getEmailContacts()) +
                '}';
    }
}

class Faculty {
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName) {
        int counter = 0;
        for(Student student : students) {
            if(student.getCity().equals(cityName))
                counter++;
        }
        return counter;
    }

    public Student getStudent(long index) {
        for(Student student : students) {
            if (student.getIndex() == index)
                return student;
        }
        return null;
    }

    public double getAverageNumberOfContacts() {
        double sum = 0;
        for(Student student : students) {
            sum += student.getNumberOfContacts();
        }
        return sum/students.length;
    }

    public Student getStudentWithMostContacts() {
        Student topG = students[0];
        for(Student student : students) {
            if(student.getNumberOfContacts() > topG.getNumberOfContacts())
                topG = student;
        }
        for(Student student : students) {
            if(topG.getNumberOfContacts() == student.getNumberOfContacts() && topG.getIndex() < student.getIndex())
                topG = student;
        }
        return topG;
    }

    @Override
    public String toString() {
        return  "{" +
                "\"fakultet\":\"" + name + "\"" +
                ", \"studenti\":" + Arrays.toString(students) +
                '}';
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
