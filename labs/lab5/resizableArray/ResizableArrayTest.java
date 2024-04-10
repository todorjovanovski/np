package lab5.resizableArray;

import java.util.Arrays;
import java.util.Scanner;
import java.util.LinkedList;

class ResizableArray<T> {
    private T[] array;
    private int maxCapacity;
    private int size;

    public ResizableArray() {
        this.size = 0;
        this.maxCapacity = 1;
        this.array = (T[]) new Object[maxCapacity];
    }

    public void addElement(T element) {
        if (size == maxCapacity) {
/*            T[] copy = (T[]) new Object[maxCapacity*2];
            for(int i=0; i<size; i++) {
                copy[i] = array[i];
            }
            array = copy;*/
                this.array = Arrays.copyOf(array, maxCapacity*2);
                maxCapacity *= 2;
        }
        array[size++] = element;
    }

    public boolean removeElement(T element) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                for (int j = i; j < size - 1; j++) {
                    array[j] = array[j + 1];
                }
                size--;
                if (size < maxCapacity / 2) maxCapacity /=2;
                return true;
            }
        }
        return false;
    }

    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element))
                return true;
        }
        return false;
    }

    public Object[] toArray() {
        return array;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int count() {
        return size;
    }

    public T elementAt(int index) {
        if (index < 0 || index > count()) throw new ArrayIndexOutOfBoundsException();
        return array[index];
    }

    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
/*        ResizableArray<Object> copy = new ResizableArray<>();
        for(int i =0; i<dest.count(); i++) {
            copy.addElement(dest.elementAt(i));
        }
        for(int i=0; i<src.count(); i++) {
            copy.addElement(src.elementAt(i));
        }
        dest = copy;*/
        for (int i = 0; i < src.count(); i++) {
            dest.addElement(src.elementAt(i));
        }
    }
}

class IntegerArray extends ResizableArray<Integer> {
    public double sum() {
        double sum = 0;
        for (int i = 0; i < count(); i++) {
            sum += (Integer) toArray()[i];
        }
        return sum;
    }

    public double mean() {
        return sum() / count();
    }

    public int countNonZero() {
        int counter = 0;
        for (int i = 0; i < count(); i++) {
            if ((Integer) toArray()[i] != 0)
                counter++;
        }
        return counter;
    }

    public IntegerArray distinct() {
        IntegerArray copy = new IntegerArray();
        copy.addElement((Integer) toArray()[0]);
        for (int i = 1; i < count(); i++) {
            int flag = 0;
            for (int j = 0; j < copy.count(); j++) {
                if (toArray()[i] == copy.toArray()[j]) {
                    flag = 1;
                    j = copy.count();
                }
            }
            if (flag == 0) {
                copy.addElement((Integer) toArray()[i]);
            }
        }
        return copy;
    }

    public IntegerArray increment(int offset) {
        IntegerArray copy = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            copy.addElement((Integer) toArray()[i] + offset);
        }
        return copy;
    }
}


public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}

