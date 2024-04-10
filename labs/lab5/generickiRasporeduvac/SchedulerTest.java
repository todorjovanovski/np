package lab5.generickiRasporeduvac;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Comparator;



public class SchedulerTest {

    static final LocalDateTime TIME = LocalDateTime.of(2016, 10, 25, 10, 15);

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Timestamp with String
            TimeStamp<String> t = new TimeStamp<>(TIME, jin.next());
            System.out.println(t);
            System.out.println(t.getTime());
            System.out.println(t.getElement());
        }
        if (k == 1) { //test Timestamp with ints
            TimeStamp<Integer> t1 = new TimeStamp<>(TIME, jin.nextInt());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            TimeStamp<Integer> t2 = new TimeStamp<>(TIME.plusDays(10), jin.nextInt());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 2) {//test Timestamp with String, complex
            TimeStamp<String> t1 = new TimeStamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t1);
            System.out.println(t1.getTime());
            System.out.println(t1.getElement());
            TimeStamp<String> t2 = new TimeStamp<>(ofEpochMS(jin.nextLong()), jin.next());
            System.out.println(t2);
            System.out.println(t2.getTime());
            System.out.println(t2.getElement());
            System.out.println(t1.compareTo(t2));
            System.out.println(t2.compareTo(t1));
            System.out.println(t1.equals(t2));
            System.out.println(t2.equals(t1));
        }
        if (k == 3) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<>();
            LocalDateTime now = LocalDateTime.now();
            scheduler.add(new TimeStamp<>(now.minusHours(2), jin.next()));
            scheduler.add(new TimeStamp<>(now.minusHours(1), jin.next()));
            scheduler.add(new TimeStamp<>(now.minusHours(4), jin.next()));
            scheduler.add(new TimeStamp<>(now.plusHours(2), jin.next()));
            scheduler.add(new TimeStamp<>(now.plusHours(4), jin.next()));
            scheduler.add(new TimeStamp<>(now.plusHours(1), jin.next()));
            scheduler.add(new TimeStamp<>(now.plusHours(5), jin.next()));
            System.out.println(scheduler.next().getElement());
            System.out.println(scheduler.last().getElement());
            List<TimeStamp<String>> result = scheduler.getAll(now.minusHours(3), now.plusHours(4).plusMinutes(15));
            String out = result.stream()
                    .sorted()
                    .map(TimeStamp::getElement)
                    .collect(Collectors.joining(", "));
            System.out.println(out);
        }
        if (k == 4) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<>();
            int counter = 0;
            ArrayList<TimeStamp<Integer>> forRemoval = new ArrayList<>();
            while (jin.hasNextLong()) {
                TimeStamp<Integer> ti = new TimeStamp<>(ofEpochMS(jin.nextLong()), jin.nextInt());
                if ((counter & 7) == 0) {
                    forRemoval.add(ti);
                }
                scheduler.add(ti);
                ++counter;
            }
            jin.next();

            while (jin.hasNextLong()) {
                LocalDateTime left = ofEpochMS(jin.nextLong());
                LocalDateTime right = ofEpochMS(jin.nextLong());
                List<TimeStamp<Integer>> res = scheduler.getAll(left, right);
                Collections.sort(res);
                System.out.println(left + " <: " + print(res) + " >: " + right);
            }
            System.out.println("test");
            List<TimeStamp<Integer>> res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            forRemoval.forEach(scheduler::remove);
            res = scheduler.getAll(ofEpochMS(0), ofEpochMS(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static LocalDateTime ofEpochMS(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault());
    }

    private static <T> String print(List<TimeStamp<T>> res) {
        if (res == null || res.size() == 0) return "NONE";
        return res.stream()
                .map(each -> each.getElement().toString())
                .collect(Collectors.joining(", "));
    }

}

// vashiot kod ovde
class TimeStamp<T> implements Comparable<TimeStamp<T>>{
    private final T element;
    private final LocalDateTime time;

    public TimeStamp(LocalDateTime time, T element) {
        this.element = element;
        this.time = time;
    }

    public T getElement() {
        return element;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public int compareTo(TimeStamp o) {
        return this.time.compareTo(o.time);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(this.getClass() != obj.getClass())
            return false;
        TimeStamp<T> casted = (TimeStamp<T>) obj;
        return this.time.equals(casted.time);
    }

    @Override
    public String toString() {
        return time + " " + element;
    }
}

class Scheduler <T> {
    private ArrayList<TimeStamp<T>> timeStamps;

    public Scheduler() {
        this.timeStamps = new ArrayList<>();
    }

    public void add(TimeStamp<T> t) {
        timeStamps.add(t);
    }

    public boolean remove(TimeStamp<T> t) {
        for(int i=0; i<timeStamps.size(); i++) {
            if(timeStamps.get(i).equals(t)) {
                timeStamps.remove(i);
                return true;
            }
        }
        return false;
    }

    public TimeStamp<T> next() {
        LocalDateTime present = LocalDateTime.now();
        TimeStamp<T> result = null;
        int first = 0;
        for (TimeStamp<T> timeStamp : timeStamps) {
            if(timeStamp.getTime().compareTo(present) > 0 && first == 0) {
                result = timeStamp;
                first = 1;
            }
            else if (first == 1 && timeStamp.getTime().compareTo(present) > 0 && timeStamp.getTime().compareTo(result.getTime()) < 0) {
                result = timeStamp;
            }
        }
        return result;
    }

    public TimeStamp<T> last() {
        LocalDateTime present = LocalDateTime.now();
        TimeStamp<T> result = null;
        int first = 0;
        for(TimeStamp<T> timeStamp : timeStamps) {
            if(timeStamp.getTime().compareTo(present) < 0 && first == 0) {
                result = timeStamp;
                first = 1;
            }
            else if(first == 1 && timeStamp.getTime().compareTo(present) < 0 && timeStamp.getTime().compareTo(result.getTime()) > 0) {
                result = timeStamp;
            }
        }
        return result;
    }

    public ArrayList<TimeStamp<T>> getAll(LocalDateTime begin, LocalDateTime end) {
        ArrayList<TimeStamp<T>> result = new ArrayList<>();
        for (TimeStamp<T> timeStamp : timeStamps) {
            if (timeStamp.getTime().compareTo(begin) > 0 && timeStamp.getTime().compareTo(end) < 0) {
                result.add(timeStamp);
            }
        }
        return result;
    }
}




