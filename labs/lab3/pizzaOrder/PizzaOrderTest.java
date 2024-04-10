package lab3.pizzaOrder;

import java.util.Scanner;
import java.util.ArrayList;

class InvalidExtraTypeException extends Exception {}

class InvalidPizzaTypeException extends Exception {}

class ItemOutOfStockException extends Exception {
    private Item item;

    public ItemOutOfStockException(Item item) {
        this.item = item;
    }
}

class ArrayIndexOutOfBoundsException extends Exception {
    private int index;
    public ArrayIndexOutOfBoundsException(int index) {
        this.index = index;
    }
}
class EmptyOrder extends Exception {}

class OrderLockedException extends Exception {}


interface Item {
    int getPrice();
    String getType();
}

class ExtraItem implements Item {

    private String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(!type.equals("Ketchup") && !type.equals("Coke"))
            throw new InvalidExtraTypeException();
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public int getPrice() {
        if(type.equals("Ketchup")) return 3;
        if(type.equals("Coke")) return 5;
        return -1;
    }
}

class PizzaItem implements Item{
    private String type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(!type.equals("Standard") && !type.equals("Pepperoni") && !type.equals("Vegetarian"))
            throw new InvalidPizzaTypeException();
        this.type = type;
    }

    public String getType() {
        return type;
    }
    @Override
    public int getPrice() {
        if(type.equals("Standard")) return 10;
        if(type.equals("Pepperoni")) return 12;
        if(type.equals("Vegetarian")) return 8;
        return -1;
    }
}

class Order {
    private ArrayList<Item> items;
    private ArrayList<Integer> counter;
    private int price;
    private boolean locked;

    public Order() {
        this.price = 0;
        this.items = new ArrayList<>();
        this.counter = new ArrayList<>();
        this.locked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if(locked) throw new OrderLockedException();
        if(count > 10) throw new ItemOutOfStockException(item);
        for(int i=0; i<items.size(); i++) {
            if(items.get(i).getType().equals(item.getType())) {
                price -= items.get(i).getPrice() * counter.get(i);
                items.set(i, item);
                counter.set(i, count);
                price += item.getPrice() * count;
                return;
            }
        }
        items.add(item);
        counter.add(count);
        price += item.getPrice() * count;
    }

    public int getPrice() {
        return price;
    }

    public void displayOrder() {
        for(int i=0; i<items.size(); i++) {
            System.out.printf("%3d.%-15sx%2d%5d$\n",
                    i+1, items.get(i).getType(), counter.get(i), items.get(i).getPrice() * counter.get(i));
        }
        System.out.printf("%-22s%5d$\n", "Total: ", this.getPrice());
    }

    public void removeItem(int index) throws ArrayIndexOutOfBoundsException, OrderLockedException {
        if(locked) throw new OrderLockedException();
        if(index < 0 || index >= items.size()) throw new ArrayIndexOutOfBoundsException(index);
        price -= items.get(index).getPrice() * counter.get(index);
        items.remove(index);
        counter.remove(index);
    }

    public void lock() throws EmptyOrder {
        if(items.size() == 0) throw new EmptyOrder();
        else locked = true;
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
