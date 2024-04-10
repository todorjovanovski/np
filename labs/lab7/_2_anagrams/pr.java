/*
package lab7._2_grupaAnagrami;

import java.io.InputStream;
import java.util.*;

class Anagram implements Comparable<Anagram> {
    char[] letters;

    public Anagram(String word) {
        letters = word.toCharArray();
    }

    char[] sortedLetters() {
        char[] copy = Arrays.copyOf(letters, letters.length);
        Arrays.sort(copy);
        return copy;
    }

    public String getWord() {
        StringBuilder word = new StringBuilder();
        for(char c : letters) {
            word.append(c);
        }
        return word.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(this.getClass() != obj.getClass())
            return false;
        Anagram casted = (Anagram) obj;
        return (Arrays.equals(this.sortedLetters(), casted.sortedLetters()));
    }

    @Override
    public String toString() {
        return getWord() + " ";
    }

    @Override
    public int compareTo(Anagram o) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.getWord(), o.getWord());
    }
}

public class pr {
    public static void main(String[] args) {
        Anagram anagram = new Anagram("acres");
        Anagram anagram1 = new Anagram("races");
        System.out.println(anagram.equals(anagram1));
    }

}*/
