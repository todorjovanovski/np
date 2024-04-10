package kolok1._7_naslovnaStranica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}

class Category {
    String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        else {
            Category other = (Category) obj;
            return this.name.equals(other.name);
        }
    }
}

abstract class NewsItem {
    String title;
    Date releaseDate;
    Category category;

    public NewsItem(String title, Date releaseDate, Category category) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.category = category;
    }

    public abstract String getTeaser();
}

class TextNewsItem extends NewsItem{
    String newsText;
    public TextNewsItem(String title, Date releaseDate, Category category, String newsText) {
        super(title, releaseDate, category);
        this.newsText = newsText;
    }

    @Override
    public String getTeaser() {
        int size = Math.min(newsText.length(), 80);
        String text = "";
        for(int i=0; i<size; i++) {
            text += newsText.charAt(i);
        }
        int now = LocalDateTime.now().getMinute();
        int releaseDate = this.releaseDate.getMinutes();
        if(now > releaseDate)
            return String.format("%s\n%d\n%s\n", title, now - releaseDate, text);
        else
            return String.format("%s\n%d\n%s\n", title, now + releaseDate, text);
    }
}

class MediaNewsItem extends NewsItem {
    String url;
    int views;
    public MediaNewsItem(String title, Date releaseDate, Category category, String url, int views) {
        super(title, releaseDate, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        int now = LocalDateTime.now().getMinute();
        int releaseDate = this.releaseDate.getMinutes();
        if(now > releaseDate)
            return String.format("%s\n%d\n%s\n%d\n", title, now - releaseDate, url, views);
        else
            return String.format("%s\n%d\n%s\n%d\n", title, now + releaseDate, url, views);
    }
}

class FrontPage{
    List<NewsItem> news;
    Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        this.news = new ArrayList<>();
    }

    public void addNewsItem(NewsItem newsItem) {
        news.add(newsItem);
    }

    public List<NewsItem> listByCategory (Category category) {
        List<NewsItem> listByCategory = new ArrayList<>();
        for(NewsItem newsItem : news) {
            if(newsItem.category.equals(category))
                listByCategory.add(newsItem);
        }
        return listByCategory;
    }

    public List<NewsItem> listByCategoryName (String category) throws CategoryNotFoundException {
        int flag = 0;
        for(Category c : categories) {
            if(c.name.equals(category)) {
                flag = 1;
                break;
            }
        }
        if(flag == 0) throw new CategoryNotFoundException("Category " + category + " was not found");
        List<NewsItem> listByCategoryName = new ArrayList<>();
        for(NewsItem item : news) {
            if(item.category.name.equals(category))
                listByCategoryName.add(item);
        }
        return listByCategoryName;
    }

    @Override
    public String toString() {
        String result = "";
        for (NewsItem newsItem : news) {
            result += newsItem.getTeaser();
        }
        return result;
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde