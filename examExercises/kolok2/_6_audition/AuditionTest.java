package kolok2._6_audition;

import java.util.*;

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}

class Participant implements Comparable<Participant> {
    private final String name;
    private final int age;
    private final String code;

    public Participant(String name, int age, String code) {
        this.name = name;
        this.age = age;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    @Override
    public int compareTo(Participant o) {
        Comparator<Participant> comparator =
                Comparator.comparing(Participant::getName).
                thenComparing(Participant::getAge);
        return comparator.compare(this, o);
    }
}

class Audition {
    HashMap<String, HashMap<String, Participant>> participantsByCodeAndCity;

    public Audition() {
        this.participantsByCodeAndCity = new HashMap<>();
    }

    public void addParticipant(String city, String code, String name, int age) {
        participantsByCodeAndCity.putIfAbsent(city, new HashMap<>());
        participantsByCodeAndCity.get(city).putIfAbsent(code, new Participant(name, age, code));
    }

    public void listByCity(String city) {
        participantsByCodeAndCity.get(city)
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(stringParticipantEntry -> System.out.println(stringParticipantEntry.getValue()));
    }
}