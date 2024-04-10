package lab3.phoneBook;

import java.io.*;
import java.util.*;

class InvalidNameException extends Exception {
    String name;

    public InvalidNameException(String name) {
        this.name = name;
    }

    public String e() {
        return name;
    }
}

class InvalidNumberException extends Exception {
}

class MaximumSizeExceededException extends Exception {
}

class InvalidFormatException extends Exception {}

class Contact {
    private String name;
    private String[] phoneNumber;

    private int size;


    public Contact(String name) throws InvalidNameException {
        checkName(name);
    }

    public Contact(String name, String... phoneNumber) throws InvalidNameException, InvalidNumberException, MaximumSizeExceededException {
        checkName(name);
        if (phoneNumber.length > 5) throw new MaximumSizeExceededException();
        for (String s : phoneNumber) {
            checkNumber(s);
        }
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.size = phoneNumber.length;
    }

    private void checkName(String name) throws InvalidNameException {
        if (name.length() <= 4 || name.length() > 10) throw new InvalidNameException(name);
        for (int i = 0; i < name.length(); i++) {
            if (Character.isAlphabetic(name.charAt(i)) || Character.isDigit(name.charAt(i)))
                throw new InvalidNameException(name);
        }
    }

    private void checkNumber(String s) throws InvalidNumberException {
        if (s.length() != 9) throw new InvalidNumberException();
        if (s.charAt(0) != '0' || s.charAt(1) != '7'
                || !Character.isDigit(s.charAt(2))
                || (s.charAt(2) == '3' || s.charAt(2) == '4' || s.charAt(2) == '9'))
            throw new InvalidNumberException();

    }

    public String getName() {
        return name;
    }

    public String[] getPhoneNumber() {
        String[] copy = Arrays.copyOf(phoneNumber, phoneNumber.length);
        Arrays.sort(copy);
        return copy;
    }

    public void addNumber(String number) throws MaximumSizeExceededException, InvalidNumberException {
        if (size == 5) throw new MaximumSizeExceededException();
        checkNumber(number);
        phoneNumber[size++] = number;
    }

    @Override
    public String toString() {
        String result = name + "\n" + phoneNumber.length + "\n";
        for (int i = 0; i < phoneNumber.length; i++) {
            result += getPhoneNumber()[i] + "\n";
        }
        return result;
    }

    public static Contact valueOf(String s)  {
        String[] split = s.split("\n");
        String[] numbers = new String[split.length-3];
        int size = 0;
        for(int i=2; i<split.length-1; i++) {
            numbers[size++] = split[i];
        }
        try {
            Contact result = new Contact(split[0], numbers);
            return result;
        }
        catch (MaximumSizeExceededException | InvalidNameException | InvalidNumberException e) {
            throw new RuntimeException(e);
        }
    }
}

class PhoneBook {
    private Contact[] contacts;
    private int size;

    public PhoneBook() {
        this.contacts = new Contact[250];
        size = 0;
    }

    public void addContact(Contact contact) throws MaximumSizeExceededException, InvalidNameException {
        if (size == 250) throw new MaximumSizeExceededException();
        for (Contact value : contacts) {
            if (value.getName().equals(contact.getName()))
                throw new InvalidNameException(contact.getName());
        }
        contacts[size++] = contact;
    }

    public Contact getContactForName(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equals(name))
                return contact;
        }
        return null;
    }

    public int numberOfContacts() {
        return size;
    }

    public Contact[] getContacts() {
        Contact[] copy = Arrays.copyOf(contacts, size);
        Arrays.sort(copy);
        return copy;
    }

    public boolean removeContact(String name) {
        for(int i=0; i<size; i++) {
            if(contacts[i].getName().equals(name))
            {
                for(int j=i; j<size-1; j++) {
                    contacts[i] = contacts[i+1];
                }
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String result = "";
        for(int i=0; i<size; i++) {
            result += getContacts()[i] + " ";
        }
        return result;
    }

    public static boolean saveAsTextFile(PhoneBook phoneBook, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(phoneBook.toString());
        writer.close();
        return true;
    }

    public static PhoneBook loadFromTextFile(String path) throws IOException, MaximumSizeExceededException, InvalidNameException {
        String string = "";
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            string += line;
        }


        String[] strings = string.split("\s");
        PhoneBook phoneBook = new PhoneBook();
        for(int i=0; i<strings.length; i++) {
            phoneBook.addContact(Contact.valueOf(strings[i]));
        }

        return phoneBook;
    }


    public Contact[] getContactsForNumber(String numberPrefix) {
        ArrayList<Contact> temp = new ArrayList<>();

        for(int i=0; i<size; i++) {
            for(int j=0; j<contacts[i].getPhoneNumber().length; j++) {
                if (contacts[i].getPhoneNumber()[j].startsWith(numberPrefix))
                    temp.add(contacts[i]);
            }
        }

        Contact[] prefix = new Contact[temp.size()];
        for(int i=0; i<temp.size(); i++) {
            prefix[i] = temp.get(i);
        }

        return prefix;
    }

}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");

    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceededException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");


    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
/*        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }*/
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceededException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        /*Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getPhoneNumber()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getPhoneNumber()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getPhoneNumber()));
        System.out.println(contact.toString());*/
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}
