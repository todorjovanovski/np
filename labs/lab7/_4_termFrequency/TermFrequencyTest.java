package lab7._4_termFrequency;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in, stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
class TermFrequency {
    Set<String> wordsToIgnore;
    List<String> totalWords;
    Map<String, Integer> uniqueWords;

    public TermFrequency(InputStream in, String[] stop) {
        this.wordsToIgnore = new HashSet<>();
        this.wordsToIgnore.addAll(Arrays.asList(stop));
        this.uniqueWords = new TreeMap<>();
        this.totalWords = new ArrayList<>();
        readText(in);
    }

    public void readText(InputStream in) {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext()) {
            String word = scanner.next();
            word = word.toLowerCase();
            word = word.replaceAll("\\.", "").replaceAll(",", "");
            word = word.trim();
            if(word.equals("")){
                continue;
            }
            if(!wordsToIgnore.contains(word)) {
                totalWords.add(word);
                uniqueWords.putIfAbsent(word, 0);
                uniqueWords.put(word, uniqueWords.get(word) + 1);
            }
        }
    }
    public int countTotal() {
        return totalWords.size();
    }

    public int countDistinct() {
        return uniqueWords.size();
    }

    public List<String> mostOften(int i) {
        ArrayList<String> mostFrequent = new ArrayList<>();

        uniqueWords.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> mostFrequent.add(entry.getKey()));

        return mostFrequent.subList(0, i);

    }
}