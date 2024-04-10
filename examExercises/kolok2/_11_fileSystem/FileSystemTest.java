package kolok2._11_fileSystem;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

record File(String name, int size, LocalDateTime dateCreated) implements Comparable<File> {

    public boolean isHidden() {
        return name.charAt(0) == '.';
    }

    @Override
    public int compareTo(File other) {
        Comparator<File> comparator = Comparator.comparing(File::dateCreated)
                .thenComparing(File::name)
                .thenComparing(File::name);
        return comparator.compare(this, other);
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB ", name, size) + dateCreated;
    }
}

class Folder {
    private final char name;
    private final HashMap<String, File> filesByName;

    public Folder(char name) {
        this.name = name;
        filesByName = new HashMap<>();
    }

    public char getName() {
        return name;
    }

    public HashMap<String, File> getFilesByName() {
        return filesByName;
    }

    public int getTotalSize() {
        return filesByName.values().stream().mapToInt(File::size).sum();
    }
}

class FileSystem {
    private final HashMap<Character, Folder> foldersByName;
    private final HashMap<Integer, Set<File>> filesByYear;
    private final HashMap<String, Long> fileSizeByMonth;

    public FileSystem() {
        foldersByName = new HashMap<>();
        filesByYear = new HashMap<>();
        fileSizeByMonth = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        foldersByName.putIfAbsent(folder, new Folder(folder));
        foldersByName.get(folder).getFilesByName().putIfAbsent(name, new File(name, size, createdAt));

        filesByYear.putIfAbsent(createdAt.getYear(), new HashSet<>());
        filesByYear.get(createdAt.getYear()).add(new File(name, size, createdAt));

        String monthAndDay = monthAndDay(createdAt);
        fileSizeByMonth.putIfAbsent(monthAndDay, 0L);
        fileSizeByMonth.put(monthAndDay, fileSizeByMonth.get(monthAndDay) + size);

    }

    private String monthAndDay(LocalDateTime date) {
        String month = String.valueOf(date.getMonth());
        int day = date.getDayOfMonth();
        return String.format("%s-%d", month, day);
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        List<File> files = new ArrayList<>();
        foldersByName.values().forEach(map -> map.getFilesByName().values()
                .stream()
                .filter(file -> file.size() < size && file.isHidden())
                .sorted()
                .forEach(files::add));
        return files;
    }

    public int totalSizeOfFilesFromFolders(List<Character> folders) {
        return folders.stream().map(foldersByName::get).mapToInt(Folder::getTotalSize).sum();
    }

    public Map<Integer, Set<File>> byYear() {
        return filesByYear;
    }

    public Map<String, Long> sizeByMonthAndDay() {
        return fileSizeByMonth;
    }
}

