package kolok2._10_iminjaUnikatni;

import java.util.*;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// code

class Name {
    private final String letters;
    private int appearances;

    public Name(String letters) {
        this.letters = letters;
        this.appearances = 1;
    }

    public void addAppearance() {
        appearances++;
    }

    public int getAppearances() {
        return appearances;
    }

    public String getLetters() {
        return letters;
    }

    public int uniqueLetters() {
        HashSet<Character> uniqueLetters = new HashSet<>();
        for (Character character : letters.toCharArray()) {
            uniqueLetters.add(Character.toLowerCase(character));
        }
        return uniqueLetters.size();
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d", letters, getAppearances(), uniqueLetters());
    }

}

class Names {
    TreeMap<String, Name> names;

    public Names() {
        this.names = new TreeMap<>();
    }

    public void addName(String name) {
        if (!names.containsKey(name)) names.put(name, new Name(name));
        else names.get(name).addAppearance();
    }

    public void printN(int n) {
        names.values()
                .stream()
                .filter(name -> name.getAppearances() >= n)
                .forEach(System.out::println);
    }

    public String findName(int len, int index) {
        ArrayList<String> namesSmallerThanLen = (ArrayList<String>) names.keySet()
                .stream()
                .filter(key -> names.get(key).getLetters().length() < len)
                .toList();
        int position = (index) % namesSmallerThanLen.size();
        return namesSmallerThanLen.get(position);
    }
}