package lab7._4_termFrequency;

import java.util.Scanner;

public class pr {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = "";
        while (scanner.hasNext()) {
            text += scanner.next() + " ";
        }
        System.out.println(text);
    }
}
