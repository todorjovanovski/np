package kolok1._12_timeTable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class InvalidTimeException extends Exception {
    public InvalidTimeException(String message) {
        super(message);
    }
}

public class TimesTest {


    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class Time implements Comparable<Time> {
    int hours;
    int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public static Time createTime(String time) throws UnsupportedFormatException, InvalidTimeException {
        for (int i = 0; i < time.length(); i++) {
            if (!Character.isDigit(time.charAt(i)) && time.charAt(i) != ':' && time.charAt(i) != '.')
                throw new UnsupportedFormatException("UnsupportedFormatException: " + time);
        }
        String[] parts = time.split("[:.]");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        if (minutes < 0 || minutes > 59 || hours < 0 || hours > 23)
            throw new InvalidTimeException("InvalidTimeException: " + time);
        return new Time(hours, minutes);
    }


    public String toString(TimeFormat format) {
        if (format == TimeFormat.FORMAT_24)
            return String.format("%2d:%02d", hours, minutes);
        else {
            String suffix = "";

            int hours = this.hours;
            if (this.hours <= 11) {
                if (this.hours == 0) hours = 12;
                suffix = "AM";
            } else {
                if (this.hours >= 13 && this.hours <= 23) hours -= 12;
                suffix = "PM";
            }

            return String.format("%2d:%02d %s", hours, minutes, suffix);
        }
    }

    public int getTime() {
        return hours * 100 + minutes;
    }

    @Override
    public int compareTo(Time o) {
        return Integer.compare(this.getTime(), o.getTime());
    }
}

class TimeTable {
    List<Time> times;

    public TimeTable() {
        this.times = new ArrayList<>();
    }

    public void line(String line) throws UnsupportedFormatException, InvalidTimeException {
        String[] parts = line.split("\\s+");
        for (String part : parts) {
            times.add(Time.createTime(part));
        }
    }

    public void readTimes(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        br.lines()
                .forEach(line -> {
                    try {
                        line(line);
                    } catch (UnsupportedFormatException | InvalidTimeException e) {
                        System.out.println(e.getMessage());
                    }
                });
        br.close();
    }

    public void writeTimes(PrintStream out, TimeFormat format) {
        PrintWriter pw = new PrintWriter(out);
        times.stream()
                .sorted()
                .forEach(time -> pw.println(time.toString(format)));
        pw.flush();
    }
}