package kolok1._1_F1trka;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class Lap {
    int mm;
    int ss;
    int nnn;

    public Lap(int mm, int ss, int nnn) {
        this.mm = mm;
        this.ss = ss;
        this.nnn = nnn;
    }

    public static Lap createLap(String lap) {
        String[] parts = lap.split(":");
        int mm = Integer.parseInt(parts[0]);
        int ss = Integer.parseInt(parts[1]);
        int nnn = Integer.parseInt(parts[2]);
        return new Lap(mm, ss, nnn);
    }

    public int lapTime() {
        return mm * 100000 + ss * 1000 + nnn;
    }

    @Override
    public String toString() {
        return String.format("%d:%02d:%03d", mm, ss, nnn);
    }
}

class CarPilot implements Comparable<CarPilot> {
    String driverName;
    List<String> laps;

    public CarPilot(String driverName, List<String> laps) {
        this.driverName = driverName;
        this.laps = new ArrayList<>(laps);
    }

    public static CarPilot createCarPilot(String line) {
        String[] parts = line.split("\\s+");
        String name = parts[0];
        List<String> laps = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            laps.add(parts[i]);
        }
        return new CarPilot(name, laps);
    }

    public Lap bestTime() {
        return laps.stream()
                .map(Lap::createLap)
                .min(Comparator.comparing(Lap::lapTime))
                .get();
    }

    @Override
    public String toString() {
        return String.format("%-10s %9s", driverName, bestTime().toString());
    }


    @Override
    public int compareTo(CarPilot o) {
        return Integer.compare(this.bestTime().lapTime(), o.bestTime().lapTime());
    }
}

class F1Race {
    List<CarPilot> pilots;

    public void readResults(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        pilots = reader.lines()
                .map(CarPilot::createCarPilot)
                .collect(Collectors.toList());
        reader.close();
    }

    public void printSorted(PrintStream out) {
        PrintWriter writer = new PrintWriter(out);
        AtomicInteger i = new AtomicInteger();
        pilots.stream()
                .sorted()
                .forEach(pilot -> writer.println(i.incrementAndGet() + ". " + pilot));
        writer.flush();
    }
}