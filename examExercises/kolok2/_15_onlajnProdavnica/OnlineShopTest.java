package kolok2._15_onlajnProdavnica;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    private final String category;
    private final String id;
    private final String name;
    private final LocalDateTime dateCreated;
    private final double price;
    private int quantity;

    public Product(String category, String id, String name, LocalDateTime dateCreated, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.price = price;
        quantity = 0;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public double getPrice() {
        return price;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public int getSold() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + dateCreated +
                ", price=" + price +
                ", quantitySold=" + getSold() +
                '}';
    }
}

class OnlineShop {
    private final HashMap<String, Product> productsByIdAndCategory;

    OnlineShop() {
        productsByIdAndCategory = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        productsByIdAndCategory.putIfAbsent(id, new Product(category, id, name, createdAt, price));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {
        if (!productsByIdAndCategory.containsKey(id))
            throw new ProductNotFoundException("Product with id " + id + " does not exist in the online shop!");
        productsByIdAndCategory.get(id).addQuantity(quantity);
        return productsByIdAndCategory.get(id).getPrice() * quantity;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        List<Product> productsByCategory;
        Comparator<Product> comparator = null;

        if (comparatorType.equals(COMPARATOR_TYPE.NEWEST_FIRST))
            comparator = Comparator.comparing(Product::getDateCreated).reversed();
        else if (comparatorType.equals(COMPARATOR_TYPE.OLDEST_FIRST))
            comparator = Comparator.comparing(Product::getDateCreated);
        else if (comparatorType.equals(COMPARATOR_TYPE.LOWEST_PRICE_FIRST))
            comparator = Comparator.comparing(Product::getPrice);
        else if (comparatorType.equals(COMPARATOR_TYPE.HIGHEST_PRICE_FIRST))
            comparator = Comparator.comparing(Product::getPrice).reversed();
        else if (comparatorType.equals(COMPARATOR_TYPE.LEAST_SOLD_FIRST))
            comparator = Comparator.comparing(Product::getSold);
        else if (comparatorType.equals(COMPARATOR_TYPE.MOST_SOLD_FIRST))
            comparator = Comparator.comparing(Product::getSold).reversed();

        if (category == null)
            productsByCategory = productsByIdAndCategory.values()
                    .stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        else
            productsByCategory = productsByIdAndCategory.values()
                    .stream()
                    .filter(product -> product.getCategory().equals(category))
                    .sorted(comparator)
                    .collect(Collectors.toList());

        for (int i = 0; i < productsByCategory.size(); i++) {
            List<Product> list = new ArrayList<>();
            for (int j = 0; j < pageSize; j++) {
                if (i >= productsByCategory.size()) {
                    break;
                }
                list.add(productsByCategory.get(i));
                i++;
            }
            i--;
            result.add(list);
        }
        return result;
    }
}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addProduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyProduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

