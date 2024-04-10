package kolok1._19_fileSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


class FileNameExistsException extends Exception {
    public FileNameExistsException(String message) {
        super(message);
    }
}

interface IFile extends Comparable<IFile>{
    String getFileName();
    long getFileSize();
    String getFileInfo();
    void sortBySize();
    IFile findLargestFile();
}

class File implements IFile {
    String name;
    long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo() {
        return String.format("\tFile name:\t%10s size:\t%10d\n", name, size);
    }

    @Override
    public void sortBySize() {}

    @Override
    public IFile findLargestFile() {
        return null;
    }

    @Override
    public int compareTo(IFile o) {
        return Long.compare(this.getFileSize(), o.getFileSize());
    }

    @Override
    public String toString() {
        return getFileInfo();
    }
}

class Folder implements IFile {
    String name;
    long size;
    List<IFile> iFiles;

    public Folder(String name) {
        this.name = name;
        this.size = 0;
        this.iFiles = new ArrayList<>();
    }

    void addFile(IFile file) throws FileNameExistsException {
        for(IFile iFile : iFiles) {
            if(iFile.getFileName().equals(file.getFileName()))
                throw new FileNameExistsException("There is already a file named " + file.getFileName() + " in the folder");
        }
        size += file.getFileSize();
        iFiles.add(file);
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo() {
        StringBuilder result = new StringBuilder(String.format("Folder name:\t%10s size:\t%10d\n", name, size));
        for (IFile iFile : iFiles) {
            result.append(iFile.getFileInfo());
        }
        return result.toString();
    }

    @Override
    public void sortBySize() {
        this.iFiles = iFiles.stream().sorted(Comparator.comparing(this::compareTo).reversed()).collect(Collectors.toList());
    }

    @Override
    public IFile findLargestFile() {
        return iFiles.stream()
                .max(Comparator.comparing(IFile::getFileSize)).orElse(null);
    }

    @Override
    public int compareTo(IFile o) {
        return Long.compare(this.getFileSize(), o.getFileSize());
    }

    @Override
    public String toString() {
        return getFileInfo();
    }
}

class FileSystem {
    Folder rootDirectory;

    public FileSystem() {
        rootDirectory = new Folder("root");
    }

    public void addFile(IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }

    public void sortBySize() {
        rootDirectory.sortBySize();
    }

    public IFile findLargestFile() {
        return rootDirectory.findLargestFile();
    }

    @Override
    public String toString() {
        return rootDirectory.getFileInfo();
    }
}

public class FileSystemTest {
    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());
    }
}
