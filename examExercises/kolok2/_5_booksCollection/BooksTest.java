package kolok2._5_booksCollection;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner, BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде

class Book implements Comparable<Book> {
    private String title;
    private String category;
    private float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public int compareTo(Book o) {
        Comparator<Book> comparator = Comparator.comparing(Book::getTitle).thenComparing(Book::getPrice);
        return comparator.compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }
}

class BookCollection {
    TreeMap<String, List<Book>> booksByCategory;

    public BookCollection() {
        this.booksByCategory = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public void addBook(Book book) {
        String category = book.getCategory();
        booksByCategory.putIfAbsent(category, new ArrayList<>());
        booksByCategory.get(category).add(book);
    }

    public List<Book> getCheapestN(int n) {
        AtomicInteger counter = new AtomicInteger();
        Set<Book> books = new HashSet<>();
        booksByCategory.values().forEach(books::addAll);
        return books.stream().sorted(Comparator.comparing(Book::getPrice))
                .filter(book -> counter.incrementAndGet() <= n)
                .collect(Collectors.toList());
    }

    public void printByCategory(String category) {
        if (booksByCategory.containsKey(category.toUpperCase())) {
            booksByCategory.get(category).stream().sorted().forEach(System.out::println);
        }
    }

}