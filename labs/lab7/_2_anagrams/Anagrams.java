package lab7._2_anagrams;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

class Anagram implements Comparable<Anagram> {
    char[] letters;

    public Anagram(String word) {
        letters = word.toCharArray();
    }

    String sortedLetters() {
        char[] copy = Arrays.copyOf(letters, letters.length);
        Arrays.sort(copy);
        return Arrays.toString(copy);
    }

    public String getWord() {
        StringBuilder word = new StringBuilder();
        for (char c : letters) {
            word.append(c);
        }
        return word.toString();
    }

    @Override
    public int compareTo(Anagram object) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.sortedLetters(), object.sortedLetters());
    }
}

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        TreeMap<Anagram, ArrayList<Anagram>> anagrams = new TreeMap<>();

        while (scanner.hasNextLine()) {
            String word = scanner.nextLine();
            Anagram anagram = new Anagram(word);
            anagrams.putIfAbsent(anagram, new ArrayList<>());
            anagrams.get(anagram).add(anagram);
        }
        scanner.close();

        List<Anagram> list = anagrams.keySet().stream()
                .sorted(Comparator.comparing(Anagram::getWord))
                .filter(key -> anagrams.get(key).size() > 1).toList();

        for (Anagram anagram : list) {
            String str = anagrams.get(anagram).stream().map(Anagram::getWord).collect(Collectors.joining(" "));
            System.out.println(str);
        }
    }
}
