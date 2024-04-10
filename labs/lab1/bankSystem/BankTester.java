package lab1.bankSystem;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

class Account{
    private String name;
    private String balance;
    private long id;

    public Account(String name, String balance) {
        this.name = name;
        this.balance = balance;
        this.id = new Random().nextLong();
    }

    public String getName() {
        return name;
    }

    public String getBalance() {
        return balance;
    }

    public long getId() {
        return id;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nBalance: "+balance+"\n";
    }

    public boolean equals(Account obj) {
        if(obj == null) return false;
        return this.name.equals(obj.name) && this.balance.equals(obj.balance) && this.id == obj.id;
    }
}

class Transaction {
    private long fromId;
    private long toId;
    private String description;
    private String amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public boolean equals(Transaction t) {
        return this.fromId == t.fromId && this.toId == t.toId
                && Objects.equals(this.amount, t.amount) && Objects.equals(this.description, t.description);
    }
}

class FlatAmountProvisionTransaction extends Transaction {

    String flatProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = flatProvision;

    }

    public String getFlatProvision() {
        return flatProvision;
    }


    public boolean equals(FlatAmountProvisionTransaction obj) {
        if(obj == null) return false;
        return this.getFromId() == obj.getFromId() && this.getAmount().equals(obj.getAmount()) &&
                this.flatProvision.equals(obj.flatProvision);
    }
}

class FlatPercentProvisionTransaction extends Transaction {

    int centsPerDollar;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int centsPerDollar) {
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDollar = centsPerDollar;
    }

    public int getCentsPerDollar() {
        return centsPerDollar;
    }

    public boolean equals(FlatPercentProvisionTransaction obj) {
        if (obj == null) return false;
        return this.getFromId() == obj.getFromId() && this.getToId() == obj.getToId()
                && this.getAmount().equals(obj.getAmount()) && this.centsPerDollar == obj.centsPerDollar;
    }
}

class Bank {
    private String name;
    private Account[] accounts;

    private double totalTransfers;
    private double totalProvisions;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = Arrays.copyOf(accounts, accounts.length);
        this.totalProvisions = 0;
        this.totalTransfers = 0;
    }

    public double parseDouble(String str) {
        StringBuilder newStr = new StringBuilder();
        for(int i=0; i<str.length() - 1; i++) {
            newStr.append(str.charAt(i));
        }
        return Double.parseDouble(newStr.toString());

    }

    public boolean makeTransaction(Transaction t) {
        int flag = 0;
        Account sender = null;
        Account receiver = null;
        for(int i=0; i<accounts.length; i++) {
            if(t.getFromId() == accounts[i].getId()){
                sender = accounts[i];
                flag++;
            }
            if(t.getToId() == accounts[i].getId()){
                receiver = accounts[i];
                flag++;
            }
        }
        if(flag == 2) {
            double newSenderBalance = parseDouble(sender.getBalance());
            double newReceiverBalance = parseDouble(receiver.getBalance());
            double fullAmount;
            if(t instanceof FlatAmountProvisionTransaction) {
                fullAmount = parseDouble(t.getAmount()) + parseDouble(((FlatAmountProvisionTransaction) t).getFlatProvision());
                if(fullAmount <= parseDouble(sender.getBalance())) {
                    newSenderBalance -= fullAmount;
                    totalProvisions += parseDouble(((FlatAmountProvisionTransaction) t).getFlatProvision());
                } else return false;
            }
            else if(t instanceof FlatPercentProvisionTransaction) {
                fullAmount = parseDouble(t.getAmount()) +
                        (double)((FlatPercentProvisionTransaction) t).getCentsPerDollar()/100.0 * parseDouble(t.getAmount());
                if(fullAmount <= parseDouble(sender.getBalance())) {
                    newSenderBalance -= fullAmount;
                    totalProvisions += (double)((FlatPercentProvisionTransaction) t).getCentsPerDollar()/100.0 * parseDouble(t.getAmount());
                } else return false;
            }
            newReceiverBalance += parseDouble(t.getAmount());
            totalTransfers += parseDouble(t.getAmount());

            String nsb = String.format("%.2f$", newSenderBalance);
            sender.setBalance(nsb);
            String nrb = String.format("%.2f$", newReceiverBalance);
            receiver.setBalance(nrb);
            return true;
        }
        return false;
    }

    public String totalTransfers() {
        return String.format("%.2f$", totalTransfers);
    }

    public String totalProvision() {
        return String.format("%.2f$", totalProvisions);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Name: " + name + "\n\n");
        for(int i=0; i<accounts.length; i++) {
            str.append(accounts[i].toString());
        }
        return str.toString();
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public boolean equals(Bank bank) {
        if(bank != null) {
            if(!this.name.equals(bank.name)) return false;
            return Arrays.equals(this.accounts, bank.accounts)
                    && this.totalProvisions == bank.totalProvisions && this.totalTransfers == bank.totalTransfers;

        }
        return false;
    }
}

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1)&&!a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts [] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}
