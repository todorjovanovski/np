package kolok1._21_mojDDV2;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        try {
            mojDDV.readRecords(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(String message) {
        super(message);
    }
}

abstract class Item {
    double itemPrice;
    String type;

    public Item(double itemPrice, String type) {
        this.itemPrice = itemPrice;
        this.type = type;
    }

    abstract double getTax();

    double taxReturn() {
        return getTax() * 0.15;
    }
}

class ItemA extends Item {
    static double A_TAX = 0.18;

    public ItemA(double itemPrice, String type) {
        super(itemPrice, type);
    }

    @Override
    double getTax() {
        return itemPrice * A_TAX;
    }
}

class ItemB extends Item {
    static double B_TAX = 0.05;

    public ItemB(double itemPrice, String type) {
        super(itemPrice, type);
    }

    @Override
    double getTax() {
        return itemPrice * B_TAX;
    }
}

class ItemV extends Item {
    static double V_TAX = 0;

    public ItemV(double itemPrice, String type) {
        super(itemPrice, type);
    }

    @Override
    double getTax() {
        return itemPrice * V_TAX;
    }
}

class Bill {
    String id;
    List<Item> items;

    public Bill(String id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public static Bill createBill(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Item> items = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 2) {
            double price = Double.parseDouble(parts[i]);
            String type = parts[i + 1];
            Item item = switch (type) {
                case "A" -> new ItemA(price, type);
                case "B" -> new ItemB(price, type);
                case "V" -> new ItemV(price, type);
                default -> null;
            };
            items.add(item);
        }
        Bill bill = new Bill(id, items);
        if (bill.totalPrice() > 30000)
            throw new AmountNotAllowedException("Receipt with amount " + (int) bill.totalPrice() + " is not allowed to be scanned");
        return bill;
    }

    double totalPrice() {
        return items.stream().mapToDouble(item -> item.itemPrice).sum();
    }

    double totalTaxReturn() {
        return items.stream().mapToDouble(Item::taxReturn).sum();
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10.0f\t%10.5f", id, totalPrice(), totalTaxReturn());
    }
}

class MojDDV {
    List<Bill> bills;

    public void readRecords(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        bills = br.lines()
                .map(line -> {
                    try {
                        return Bill.createBill(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        br.close();
    }

    public void printTaxReturns(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        bills.forEach(pw::println);
        pw.flush();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void printStatistics(PrintStream out) {
        DoubleSummaryStatistics dss = bills.stream().mapToDouble(Bill::totalTaxReturn).summaryStatistics();
        PrintWriter pw = new PrintWriter(out);
        String result = String.format("min:\t%5.3f\nmax:\t%5.3f\nsum:\t%5.3f\ncount:\t%-5d\navg:\t%5.3f",
                dss.getMin(),
                dss.getMax(),
                dss.getSum(),
                dss.getCount(),
                dss.getAverage());
        pw.println(result);
        pw.flush();
    }
}