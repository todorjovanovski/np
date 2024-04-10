package lab5.resizableArray;

import java.util.ArrayList;
import java.util.LinkedList;

public class pr {
    public static void main(String[] args) {
/*        int [] a = {1, 2, 3, 4, 5, 2, 4, 6};
        ArrayList<Integer> copy = new ArrayList<>();
        copy.add(1);
        for (int i = 1; i < 8; i++) {
            int flag = 0;
            for (int j = 0; j < copy.size(); j++) {
                if (a[i] == copy.get(j)) {
                    flag = 1;
                    j = copy.size();
                }
            }
            if (flag == 0) {
                copy.add(a[i]);
            }
        }
        System.out.println(copy);*/

        LinkedList<String> strings = new LinkedList<>();
        strings.add("A");
        strings.add("n");
        strings.add("d");
        strings.add("r");
        strings.add("ej`");
        strings.add("Gaj");
        strings.add("du");
        strings.add("K");
        String s = "aGajduK";
        /*int count = s.length();
        int k = 0;
        for (String string : strings) {
            for (int j = 0; j < string.length(); j++) {
                if (string.charAt(j) == s.charAt(k++)) {
                    count--;
                    if(count == 0) {
                        System.out.println("CONTAINS");
                        break;
                    }
                }
                else {
                    count = s.length();
                    k = 0;
                }
            }
            if(count == 0) {
                System.out.println("CONTAINS");
                break;
            }
        }
        if(count != 0) {
            System.out.println("NOT CONTAINS");
        }*/
        System.out.println(strings);
        int size = strings.size();
        LinkedList<String> copy = new LinkedList<>();
        while(size > 0) {
            strings.add(strings.remove(--size));
        }

        System.out.println(strings);
    }
}
