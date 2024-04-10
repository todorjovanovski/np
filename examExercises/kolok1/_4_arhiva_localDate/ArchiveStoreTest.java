package kolok1._4_arhiva_localDate;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NonExistingItemException extends Exception {
    public NonExistingItemException(String message) {
        super(message);
    }
}

abstract class Archive {
    int id;
    LocalDate date;

    public Archive(int id) {
        this.id = id;
    }

    public abstract void setDate(LocalDate date);
}

class LockedArchive extends Archive {

    LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }
}

class SpecialArchive extends Archive {

    int maxOpen;
    int opened;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        this.opened = maxOpen;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setOpened(int opened) {
        this.opened = opened;
    }
}

class ArchiveStore {

    List<Archive> archives;
    String log;

    public ArchiveStore() {
        this.archives = new ArrayList<>();
        this.log = "";
    }

    public void archiveItem(Archive item, LocalDate date) {
        archives.add(item);
        log += "Item " + item.id + " archived at " + date + "\n";
    }

    public void openItem(int id, LocalDate date) throws NonExistingItemException {
        Archive item = null;
        for (Archive archive : archives) {
            if (archive.id == id) {
                item = archive;
                break;
            }
        }
        if (item == null) throw new NonExistingItemException("Item with id " + id + " doesn't exist");
        if (item instanceof LockedArchive) {
            if (date.isBefore(((LockedArchive) item).dateToOpen)) {
                log += "Item " + id + " cannot be opened before " + ((LockedArchive) item).dateToOpen + "\n";
            } else {
                log += "Item " + id + " opened at " + date + "\n";
            }
        } else if (item instanceof SpecialArchive) {
            if (((SpecialArchive) item).opened == 0) {
                log += "Item " + id + " cannot be opened more than " + ((SpecialArchive) item).maxOpen + " times" + "\n";
            } else {
                log += "Item " + id + " opened at " + date + "\n";
                ((SpecialArchive) item).setOpened(((SpecialArchive) item).opened - 1);
            }
        }
    }

    public String getLog() {
        return log;
    }
}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}