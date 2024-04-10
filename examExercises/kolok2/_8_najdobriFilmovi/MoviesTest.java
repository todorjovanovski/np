package kolok2._8_najdobriFilmovi;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoefficient();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

class Movie {
    private final String title;
    private final List<Integer> ratings;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = new ArrayList<>();
        for (int rating : ratings) {
            this.ratings.add(rating);
        }
    }

    public String getTitle() {
        return title;
    }

    public double averageRating() {
        return ratings.stream().mapToDouble(rating -> rating).average().orElse(0);
    }

    public double ratingCoefficient() {
        int maxRating = ratings.stream().mapToInt(rating -> rating).max().orElse(0);
        return averageRating() * ratings.size() / maxRating;
    }

    @Override
    public String toString() {
        //Love on the Run (1979) (7.33) of 6 ratings
        return String.format("%s (%.2f) of %d ratings", title, averageRating(), ratings.size());
    }
}

class MoviesList {
    private final List<Movie> movies;

    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        AtomicInteger atomicInteger = new AtomicInteger();
        return movies
                .stream()
                .sorted(Comparator.comparing(Movie::averageRating).reversed().thenComparing(Movie::getTitle))
                .filter(movie -> atomicInteger.incrementAndGet() <= 10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoefficient () {
        AtomicInteger atomicInteger = new AtomicInteger();
        return movies
                .stream()
                .sorted(Comparator.comparing(Movie::ratingCoefficient).reversed().thenComparing(Movie::getTitle))
                .filter(movie -> atomicInteger.incrementAndGet() <= 10)
                .collect(Collectors.toList());
    }
}
