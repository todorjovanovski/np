package kolok2._2_mernaStanica;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde
class Measurement implements Comparable<Measurement> {
    double temperature;
    double speed;
    double humidity;
    double visibility;
    Date date;

    public Measurement(double temperature, double speed, double humidity, double visibility, Date date) {
        this.temperature = temperature;
        this.speed = speed;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public boolean dayIsOlder(Measurement other, int days) {
        return ((double) ((this.date.getTime() - other.date.getTime()) / 86400000) >= (double) days);
    }

    public boolean minuteGapIsSmall(Measurement other) {
        return ((double) Math.abs((this.date.getTime() - other.date.getTime()) / 60000) <= 2.5);
    }

    @Override
    public int compareTo(Measurement o) {
        return Long.compare(this.date.getTime(), o.date.getTime());
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km ", temperature, speed, humidity, visibility) + date;
    }
}

class WeatherStation {

    List<Measurement> measurements;
    int numberOfDays;

    public WeatherStation(int days) {
        this.measurements = new ArrayList<>();
        this.numberOfDays = days;
    }

    public void addMeasurement(float temp, float wind, float hum, float vis, Date date) {
        Measurement current = new Measurement(temp, wind, hum, vis, date);
        for (Measurement measurement : measurements) {
            if (current.minuteGapIsSmall(measurement)) return;
        }
        measurements.add(current);
        measurements.removeIf(measurement -> current.dayIsOlder(measurement, numberOfDays));
    }

    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) throws RuntimeException {
        List<Measurement> interval = new ArrayList<>();
        for (Measurement measurement : measurements) {
            if (measurement.date.getTime() >= from.getTime() && measurement.date.getTime() <= to.getTime())
                interval.add(measurement);
        }
        if (interval.size() == 0) throw new RuntimeException();
        interval.stream()
                .sorted()
                .forEach(System.out::println);

        double avg = interval.stream()
                .mapToDouble(t -> t.temperature)
                .average()
                .orElse(0);
        System.out.printf("Average temperature: %.2f", avg);
    }
}