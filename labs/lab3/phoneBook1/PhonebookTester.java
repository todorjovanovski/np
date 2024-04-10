package lab3.phoneBook1;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidNumberException extends Exception{}

class MaximumSizeExceddedException extends Exception{}

class InvalidFormatException extends Exception{}

class InvalidNameException extends Exception{
    String name;

    public InvalidNameException(String name) {
        this.name = name;
    }
}

class Contact implements Comparable<Contact> {
    String name;
    String [] phoneNumbers;
    int size;

    public Contact(String name, String... phoneNumbers) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        if(!checkName(name)) throw new InvalidNameException(name);
        if(phoneNumbers.length > 5) throw new MaximumSizeExceddedException();
        for (String number : phoneNumbers) {
            if(!checkPhoneNumber(number)) throw new InvalidNumberException();
        }
        this.name = name;
        this.phoneNumbers = Arrays.copyOf(phoneNumbers, 5);
        this.size = phoneNumbers.length;
    }

    public static boolean checkName(String name) {
         if(name.length() <= 4 || name.length() > 10)
             return false;
         for(Character c : name.toCharArray()) {
             if(!Character.isDigit(c) && !Character.isAlphabetic(c))
                 return false;
         }
         return true;
    }

    public static boolean checkPhoneNumber(String s) {
        if (s.length() != 9) return false;
        return s.charAt(0) == '0' && s.charAt(1) == '7'
                && Character.isDigit(s.charAt(2))
                && (s.charAt(2) != '3' && s.charAt(2) != '4' && s.charAt(2) != '9');
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        String[] copy = Arrays.copyOf(phoneNumbers, size);
        Arrays.sort(copy);
        return copy;
    }

    public void addNumber(String phoneNumber) throws MaximumSizeExceddedException, InvalidNumberException {
        if (size == 5) throw new MaximumSizeExceddedException();
        if(!checkPhoneNumber(phoneNumber)) throw new InvalidNumberException();
        phoneNumbers[size++] = phoneNumber;
    }

    @Override
    public String toString() {
        String result = String.format("%s\n%d", name, size);
        for(String number : getNumbers()) {
            result += String.format("%s\n", number);
        }
        return result;
    }

    public static Contact valueOf(String s) throws InvalidFormatException, InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        String[] parts = s.split("\\s+");
        String name = parts[0];

        List<String> listString = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
        String[] numbers = new String[listString.size()];
        for(int i=0; i<listString.size(); i++) {
            numbers[i] = listString.get(i);
        }
        if(!checkName(name)) throw new InvalidFormatException();
        for(String num : numbers) {
            if(!checkPhoneNumber(num)) throw new InvalidFormatException();
        }
        return new Contact(name, numbers);
    }

    @Override
    public int compareTo(Contact o) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.name, o.name);
    }
}

class PhoneBook{
    Contact[] contacts;
    int size;

    public PhoneBook() {
        this.contacts = new Contact[250];
        int size = 0;
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if(size == 250) throw new MaximumSizeExceddedException();
        for(Contact c : contacts) {
            if(c.name.equals(contact.name))
                throw new InvalidNameException(c.name);
        }
        contacts[size++] = contact;
    }

    public int numberOfContacts() {
        return size;
    }

    public List<Contact> getContacts() {
        List<Contact> copy = new ArrayList<>(List.of(contacts));
        return copy;
    }

    public boolean removeContact(String name) {
        if(contacts[size].name.equals(name)){
            size--;
            return true;
        }
        for(int i=0; i<size-1; i++) {
            if(contacts[i].name.equals(name)) {
                for(int j=i; j<size-1; j++) {
                    contacts[i] = contacts[i+i];
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
        for(Contact contact : contacts) {
            result += contact + "\n";
        }
        return result;
//        return Arrays.stream(contacts).toString();
    }

    public static boolean saveAsTextFile(PhoneBook phonebook, String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(phonebook.toString());
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static PhoneBook loadFromTextFile(String path) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        List<Contact> contactList  = br.lines().map(line -> {
            try {
                return Contact.valueOf(line);
            } catch (InvalidFormatException | InvalidNameException | InvalidNumberException | MaximumSizeExceddedException e) {
                throw new RuntimeException();
            }
        }).toList();
        PhoneBook phonebook = new PhoneBook();
        for(Contact contact : contactList) {
            try {
                phonebook.addContact(contact);
            } catch (MaximumSizeExceddedException | InvalidNameException e) {
                throw new RuntimeException(e);
            }
        }
        return phonebook;
    }

    public Contact[] getContactsForNumber(String numberPrefix) {
        List<Contact> contactList = new ArrayList<>();
        for(Contact contact : contacts) {
            for(String number : contact.phoneNumbers) {
                if(number.startsWith(numberPrefix)) {
                    contactList.add(contact);
                    break;
                }
            }
        }
        return contactList.toArray(new Contact[0]);
    }

    public Contact getContactForName(String name) {
        for (Contact contact : contacts) {
            if(contact.name.equals(name))
                return contact;
        }
        return null;
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
                    System.out.println((phonebook.getContacts()));
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
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
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
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
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
