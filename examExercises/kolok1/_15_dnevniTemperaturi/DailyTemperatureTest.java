package kolok1._15_dnevniTemperaturi;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        try {
            dailyTemperatures.readTemperatures(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde

class Measurement implements Comparable<Measurement> {
    int date;
    List<Double> temperatures;
    String type;

    public Measurement(int date, List<Double> temperatures, String type) {
        this.date = date;
        this.temperatures = temperatures;
        this.type = type;
    }

    public static Measurement createMeasurement(String line) {
        String[] parts = line.split("\\s+");
        int date = Integer.parseInt(parts[0]);
        List<Double> temperatures = new ArrayList<>();
        String type = String.valueOf(parts[1].charAt(parts[1].length() - 1));
        for(int i=1; i<parts.length; i++) {
            temperatures.add(Double.parseDouble(parts[i].substring(0, parts[i].length()-1)));
        }
        return new Measurement(date, temperatures, type);
    }

    public Measurement convertToCelsius(Measurement measurement, char scale) {
        if(measurement.type.equals("F"))
            measurement.temperatures.replaceAll(aDouble -> ((aDouble - 32) * 5) / 9);
        type = String.valueOf(scale);
        return measurement;
    }


    public Measurement convertToFahrenheit(Measurement measurement, char scale) {
        if(measurement.type.equals("C"))
            measurement.temperatures.replaceAll(aDouble -> (aDouble * 9) / 5 + 32);
        type = String.valueOf(scale);
        return measurement;
    }


    @Override
    public int compareTo(Measurement o) {
        return Double.compare(this.date, o.date);
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics dss = temperatures.stream().mapToDouble(temp -> temp).summaryStatistics();
        return String.format("%6d: Count: %6d Min: %6.2f%s Max: %6.2f%s Avg: %6.2f%s",
                date,
                dss.getCount(),
                dss.getMin(), type,
                dss.getMax(), type,
                dss.getAverage(), type);
    }
}
class DailyTemperatures {

    List<Measurement> dailyTemperatures;
    public void readTemperatures(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        dailyTemperatures = br.lines().map(Measurement::createMeasurement).collect(Collectors.toList());
        br.close();
    }

    public void writeDailyStats(PrintStream out, char scale) {
        PrintWriter pw = new PrintWriter(out);
        if(scale == 'C') {
            dailyTemperatures.stream()
                    .map(measurement -> measurement.convertToCelsius(measurement, scale))
                    .sorted()
                    .forEach(pw::println);
        } else {
            dailyTemperatures.stream()
                    .map(measurement -> measurement.convertToFahrenheit(measurement, scale))
                    .sorted()
                    .forEach(pw::println);
        }
        pw.flush();
    }
}
