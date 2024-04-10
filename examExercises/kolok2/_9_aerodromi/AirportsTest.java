package kolok2._9_aerodromi;

import java.util.*;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// Вашиот код овде

class Airport {
    private final String name;
    private final String country;
    private final String code;
    private final int passengers;

    private final TreeSet<Flight> flightsFromHere;
    final TreeSet<Flight> flightsToHere;

    public Airport(String name, String country, String code, int passengers) {
        this.flightsFromHere = new TreeSet<>(Comparator.comparing(Flight::getDestinationCode)
                .thenComparing(Flight::getTime)
                .thenComparing(Flight::getDuration));
        this.flightsToHere = new TreeSet<>(Comparator.comparing(Flight::getDestinationCode)
                .thenComparing(Flight::getTime)
                .thenComparing(Flight::getDuration));
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    public TreeSet<Flight> getFlightsFromHere() {
        return flightsFromHere;
    }

    public TreeSet<Flight> getFlightsToHere() {
        return flightsToHere;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }
}

class Flight{
    private final String source;
    private final String  destination;
    private final int time;
    private final int duration;

    public Flight(String source, String destination, int time, int duration) {
        this.source = source;
        this.destination = destination;
        this.time = time;
        this.duration = duration;
    }

    public String getDestinationCode() {
        return destination;
    }

    public String getSourceCode() {
        return source;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    private String getTakeoffTime() {
        int hours = time / 60;
        int minutes = time % 60;
        int hoursDest = (time + duration) / 60 % 24;
        int minutesDest = (time + duration) % 60;
        return String.format("%02d:%02d-%02d:%02d", hours, minutes, hoursDest, minutesDest);
    }

    private String getDurationTime() {
        Duration d = Duration.ofMinutes(duration);
        long days = Duration.ofMinutes(duration + time).toDays();
        long hours = d.toHours() % 24;
        long minutes = d.toMinutes() % 60;
        if(days > 0) return String.format("+%dd %dh%02dm", days, hours, minutes);
        else return String.format("%dh%02dm", hours, minutes);
    }

    @Override
    public String toString() {
        return String.format("%s-%s %s %s", getSourceCode(), getDestinationCode(), getTakeoffTime(), getDurationTime());
    }
}

class Airports {

    private final HashMap<String, Airport> airportsByCode;

    public Airports() {
        airportsByCode = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airportsByCode.putIfAbsent(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);
        airportsByCode.get(from).getFlightsFromHere().add(flight);
        airportsByCode.get(to).getFlightsToHere().add(flight);
    }

    public void showFlightsFromAirport(String code) {
        Airport airport = airportsByCode.get(code);
        System.out.println(airport);
        AtomicInteger i = new AtomicInteger();
        airport.getFlightsFromHere().forEach(flight -> System.out.println(i.incrementAndGet() + ". " + flight));
    }

    public void showDirectFlightsFromTo(String from, String to) {
        ArrayList<Flight> flights = new ArrayList<>();
        airportsByCode.get(from).getFlightsFromHere()
                .stream()
                .filter(flight -> flight.getDestinationCode().equals(to))
                .forEach(flights::add);
        if(flights.size() == 0) System.out.println("No flights from " + from + " to " + to);
        else flights.forEach(System.out::println);
    }

    public void showDirectFlightsTo(String to) {
        airportsByCode.get(to).getFlightsToHere().forEach(System.out::println);
    }
}