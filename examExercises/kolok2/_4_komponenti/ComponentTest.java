package kolok2._4_komponenti;

import java.security.cert.CertPath;
import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.switchComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде

class InvalidPositionException extends Exception {
    public InvalidPositionException(String message) {
        super(message);
    }
}
class Component {
    private String color;
    private int weight;
    private final TreeSet<Component> components;


    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.components = new TreeSet<>(Comparator.comparing(Component::getWeight).thenComparing(Component::getColor));
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TreeSet<Component> getComponents() {
        return components;
    }

    public void printRecursive(int level) {
        for(int i=0; i<level; i++) {
            System.out.print("---");
        }
        System.out.printf("%d:%s\n", weight, color);
        for (Component component : components) {
            component.printRecursive(level + 1);
        }
    }

    @Override
    public String toString() {
/*        String result = String.format("%d:%s\n", weight, color);*/
/*

        for (Component component : components) {

            result += component.toString();

        }*/
        /*String result = printRecursive("", 0);
        return result;*/
        printRecursive(0);
        return "";
    }

    public void changeColors(int weight, String color) {
        if(this.weight < weight) this.color = color;
        for (Component component : components) {
            component.changeColors(weight, color);
        }
    }
}

class Window {
    private String name;
    private HashMap<Integer, Component> componentsByPosition;

    public Window(String name) {
        this.name = name;
        this.componentsByPosition = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        if(componentsByPosition.containsKey(position))
            throw new InvalidPositionException("Invalid position " + position + ", alredy taken!");
        componentsByPosition.put(position, component);
    }

    public void changeColor(int weight, String color) {
        componentsByPosition.values().forEach(component -> component.changeColors(weight, color));
    }



    public void switchComponents(int pos1, int pos2) {
        Component componentPos1 = componentsByPosition.get(pos1);
        Component componentPos2 = componentsByPosition.get(pos2);
        componentsByPosition.put(pos1, componentPos2);
        componentsByPosition.put(pos2, componentPos1);
    }

    @Override
    public String toString() {
/*        String result = String.format("WINDOW %s\n", name);
        for(Integer key : componentsByPosition.keySet()) {
            result += key + ":" + componentsByPosition.get(key);
        }
        return result;*/
        System.out.printf("WINDOW %s\n", name);
        for(Integer key : componentsByPosition.keySet()) {
            System.out.print(key + ":");
            System.out.print(componentsByPosition.get(key));
        }
        return "";
    }
}
