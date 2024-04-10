package lab5.integerList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class IntegerList {
    private ArrayList<Integer> numbers;

    public IntegerList() {
        this.numbers = new ArrayList<>();
    }

    public IntegerList(Integer[] numbers) {
        this.numbers = new ArrayList<>();
        this.numbers.addAll(Arrays.asList(numbers));
    }

    public void add(int element, int index) throws ArrayIndexOutOfBoundsException{
        if(index < 0) throw new ArrayIndexOutOfBoundsException();
        if(index < numbers.size()) {
            numbers.add(index, element);
        }
        else if(index > numbers.size()) {
            for(int i=numbers.size(); i<index; i++) {
                numbers.add(0);
            }
            numbers.add(element);
        }
    }

    public int remove(int index) throws ArrayIndexOutOfBoundsException {
        if(index < 0 || index > numbers.size()-1) throw new ArrayIndexOutOfBoundsException();
        return numbers.remove(index);
    }

    public void set(int element, int index) throws ArrayIndexOutOfBoundsException {
        if(index < 0 || index > numbers.size()-1) throw new ArrayIndexOutOfBoundsException();
        numbers.set(index, element);
    }

    public int get(int index) throws ArrayIndexOutOfBoundsException {
        if(index < 0 || index > numbers.size()-1) throw new ArrayIndexOutOfBoundsException();
        return numbers.get(index);
    }

    public int size() {
        return numbers.size();
    }

    public int count(int element) {
        int count = 0;
        for (Integer number : numbers) {
            if (number == element)
                count++;
        }
        return count;
    }

    public void removeDuplicates() {
        for(int i=0; i<size()-1; i++) {
            int curr = numbers.get(i);
            int count = count(curr);
            int check = 0;
            for(int j=i; j<size(); j++) {
                if(count > 1 && numbers.get(j).equals(curr)) {
                    numbers.remove(j);
                    j--;
                    count--;
                    check = 1;
                } else {
                    j = size();
                }
            }
            if (check == 1)
                i--;
        }
    }

    public int sumFirst(int k) throws ArrayIndexOutOfBoundsException {
        if(k < 0) throw new ArrayIndexOutOfBoundsException();
        if(k > size()) k = size();
        int sum = 0;
        for(int i=0; i<k; i++) {
            sum += numbers.get(i);
        }
        return sum;
    }

    public int sumLast(int k) throws ArrayIndexOutOfBoundsException {
        if(k < 0 || k > size()) throw new ArrayIndexOutOfBoundsException();
        int sum = 0;
        for(int i=size()-k; i<size(); i++) {
            sum += numbers.get(i);
        }
        return sum;
    }

    public void shiftRight(int index, int k) throws ArrayIndexOutOfBoundsException{
        numbers.add((index+k)%size(), numbers.remove(index));
    }

    public void shiftLeft(int index, int k) throws ArrayIndexOutOfBoundsException {
        if(k <= index)
            numbers.add(Math.abs((index-k)%size()), numbers.remove(index));
        else
            numbers.add(size() - Math.abs((index-k)%size()), numbers.remove(index));
    }

    //0 1 2 3 4 5 6 7 8 9
    //1 2 3 4 5 6 7 8 9 10
    //i:4 shift: 2
    public IntegerList addValue(int value) {
        Integer[] copy = new Integer[size()];
        for(int i=0; i<size(); i++) {
            copy[i] = numbers.get(i) + value;
        }
        return new IntegerList(copy);
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}