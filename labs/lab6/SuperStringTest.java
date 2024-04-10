package lab6;

import java.util.Scanner;
import java.util.LinkedList;

class SuperString {

    private LinkedList<String> strings;
    private String checker;

    public SuperString() {
        this.strings = new LinkedList<>();
        checker = "";
    }

    public void append(String s) {
        strings.addLast(s);
        checker += '1';
    }

    public void insert(String s) {
        strings.addFirst(s);
        checker += '0';
    }

    public boolean contains(String s) {
        int count = s.length();
        int k = 0;
        for (String string : strings) {
            for (int j = 0; j < string.length(); j++) {
                if (string.charAt(j) == s.charAt(k++)) {
                    count--;
                    if(count == 0) return true;
                }
                else {
                    count = s.length();
                    k = 0;
                }
            }
            if(count == 0) return true;
        }
        return false;
    }

    public void reverse() {
        for (int i=0; i<strings.size(); i++) {
            String newStr = "";
            char c;
            for(int j=0; j<strings.get(i).length(); j++) {
                c = strings.get(i).charAt(j);
                newStr = c + newStr;
            }
            strings.set(i, newStr);
        }
        int size = strings.size();

        while (size > 0) {
            strings.add(strings.remove(--size));
        }
    }

    @Override
    public String toString() {
        String result = "";
        for(String string : strings) {
            result += string;
        }
        return result;
    }

    public void removeLast(int k) {
        int size = checker.length();
        while(k > 0) {
            if(checker.charAt(--size) == '0')
                strings.removeFirst();
            else
                strings.removeLast();
            k--;
        }
    }
}



public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
