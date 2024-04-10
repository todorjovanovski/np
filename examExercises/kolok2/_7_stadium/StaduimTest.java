package kolok2._7_stadium;

import java.util.*;

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

class SeatNotAllowedException extends Exception {}

class SeatTakenException extends Exception {}

class Sector implements Comparable<Sector>{
    private String code;
    private int seats;
    private HashSet<Integer> takenSeats;

    private int type;

    public Sector(String code, int seats) {
        this.code = code;
        this.seats = seats;
        this.takenSeats = new HashSet<>();
        this.type = 0;
    }

    public String getCode() {
        return code;
    }

    public int getAvailableSeats() {
        return seats - takenSeats.size();
    }

    public HashSet<Integer> getTakenSeats() {
        return takenSeats;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(Sector o) {
        Comparator<Sector> comparator =
                Comparator.comparing(Sector::getAvailableSeats).reversed()
                        .thenComparing(Sector::getCode);
        return comparator.compare(this, o);
    }

    @Override
    public String toString() {
        String seat = String.format("%d/%d", getAvailableSeats(), seats);
        return String.format("%s\t%s\t%.1f%%", code, seat, (float)takenSeats.size()/seats*100);
    }
}

class Stadium {
    private String name;
    HashMap<String, Sector> sectorsByName;

    public Stadium(String name) {
        this.name = name;
        this.sectorsByName = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        for(int i=0; i<sectorNames.length; i++) {
            sectorsByName.put(sectorNames[i], new Sector(sectorNames[i], sectorSizes[i]));
        }
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Sector sector = sectorsByName.get(sectorName);
        if(sector.getTakenSeats().contains(seat))
            throw new SeatTakenException();
        if(sector.getType() == 1 && type == 2 || sector.getType() == 2 && type == 1)
            throw new SeatNotAllowedException();
        if(type != 0)
            sector.setType(type);
        sector.getTakenSeats().add(seat);

    }

    public void showSectors() {
        sectorsByName.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(stringSectorEntry -> System.out.println(stringSectorEntry.getValue()));
    }
}
