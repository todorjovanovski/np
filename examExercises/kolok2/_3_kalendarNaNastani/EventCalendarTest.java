package kolok2._3_kalendarNaNastani;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde

class Event {
    private String name;
    private String location;
    private Date eventDate;

    public Event(String name, String location, Date eventDate) {
        this.name = name;
        this.location = location;
        this.eventDate = eventDate;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public int getEventHours() {
        return eventDate.getHours();
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("dd MMM, YYY HH:mm").format(eventDate) + String.format(" at %s, %s", location, name);
    }
}

class WrongDateException extends Exception {
    public WrongDateException(String message) {
        super(message);
    }
}
class EventCalendar {
    private final TreeMap<Date, List<Event>> eventsByDay;
    private final int year;

    public EventCalendar(int year) {
        this.eventsByDay = new TreeMap<>(Comparator.comparing(Date::getMonth).thenComparing(Date::getDate));
        this.year = year;
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        if((date.getYear() + 1900) != year)
            throw new WrongDateException("Wrong date: " + date);
        Event event = new Event(name, location, date);
        eventsByDay.putIfAbsent(date, new ArrayList<Event>());
        eventsByDay.get(date).add(event);
    }

    public void listEvents(Date date) {
        if (eventsByDay.containsKey(date))
            eventsByDay.get(date).stream()
                .sorted(Comparator.comparing(Event::getEventDate).thenComparing(Event::getName))
                .forEach(System.out::println);
        else System.out.println("No events on this day!");

    }

    public void listByMonth() {
        TreeMap<Integer, Integer> months = new TreeMap<>();
        IntStream.range(1, 13).boxed().forEach(integer -> months.put(integer, 0));
        for(Date date : eventsByDay.keySet()) {
            int month = date.getMonth()+1;
            if(months.containsKey(month)) {
                months.put(month, months.get(month) + eventsByDay.get(date).size());
            }
        }
        months.forEach((key, value) -> System.out.println(key + " : " + value));
    }
}