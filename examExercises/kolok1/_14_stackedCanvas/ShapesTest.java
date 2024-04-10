package kolok1._14_stackedCanvas;

import java.util.*;
import java.util.stream.Collectors;


public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("\nORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("\nAFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}
abstract class Shape implements Scalable, Stackable, Comparable<Shape> {
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    @Override
    public abstract String toString();

    @Override
    public int compareTo(Shape o) {
        if(this.weight() > o.weight())
            return 1;
        else
            return -1;
    }
}

class Circle extends Shape {
    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.PI * radius * radius);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f", id, color, weight());
    }
}

class Rectangle extends Shape  {

    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        (height) *= scaleFactor;
        width *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f", id, color, weight());
    }
}

class Canvas {
    List<Shape> shapes;

    public Canvas() {
        this.shapes = new ArrayList<>();
    }

    public void add(String id, Color color, float radius) {
        Shape insert = new Circle(id, color, radius);
        addShape(insert, id);
    }

    public void add(String id, Color color, float width, float height) {
        Shape insert = new Rectangle(id, color, width, height);
        addShape(insert, id);
    }

    private void addShape(Shape insert, String id) {
        for(int i=0; i<shapes.size(); i++) {
            if(shapes.get(i).id.equals(id)) {
                shapes.remove(i);
                break;
            }
        }
        int index = Collections.binarySearch(shapes, insert, Collections.reverseOrder());
        if (index < 0) {
            index = -index - 1;
        }
        shapes.add(index, insert);
    }

    public void scale(String id, float scaleFactor) {
        for(Shape shape : shapes) {
            if(shape.id.equals(id)) {
                shape.scale(scaleFactor);
                addShape(shape, id);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return shapes.stream()
                .map(Shape::toString)
                .collect(Collectors.joining("\n"));
    }
}