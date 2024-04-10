package kolok1._6_canvas2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String message) {
        super(message);
    }
}

abstract class Shape {
    String type;
    int size;

    public Shape(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public abstract double getArea();
}

class Square extends Shape {
    public Square(String type, int size) {
        super(type, size);
    }

    @Override
    public double getArea() {
        return size * size;
    }
}

class Circle extends Shape {
    public Circle(String type, int size) {
        super(type, size);
    }

    @Override
    public double getArea() {
        return Math.PI * size * size;
    }
}

class Canvas implements Comparable<Canvas> {
    String id;
    List<Shape> shapes;

    public Canvas(String id, List<Shape> shapes) {
        this.id = id;
        this.shapes = shapes;
    }

    public static Canvas createCanvas(String line, double maxArea) throws IrregularCanvasException {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Shape> shapes = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 2) {
            String type = parts[i];
            int size = Integer.parseInt(parts[i + 1]);
            if (type.equals("S")) {
                Square square = new Square(type, size);
                if (square.getArea() >= maxArea)
                    throw new IrregularCanvasException(String.format("Canvas %s has a shape with area larger than %.2f", id, maxArea));
                else {
                    shapes.add(square);
                }
            } else {
                Circle circle = new Circle(type, size);
                if (circle.getArea() >= maxArea)
                    throw new IrregularCanvasException(String.format("Canvas %s has a shape with area larger than %.2f", id, maxArea));
                else {
                    shapes.add(circle);
                }
            }
        }
        return new Canvas(id, shapes);
    }

    public int getCircles() {
        int count = 0;
        for (Shape shape : shapes) {
            if (shape instanceof Circle) count++;
        }
        return count;
    }

    public int getSquares() {
        int count = 0;
        for (Shape shape : shapes) {
            if (shape instanceof Square) count++;
        }
        return count;
    }


    public double getTotalArea() {
        double totalArea = 0;
        for (Shape shape : shapes) {
            totalArea += shape.getArea();
        }
        return totalArea;
    }


    @Override
    public int compareTo(Canvas o) {
        return Double.compare(this.getTotalArea(), o.getTotalArea());
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics dss = shapes.stream()
                .mapToDouble(Shape::getArea)
                .summaryStatistics();
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                id,
                shapes.size(),
                getCircles(),
                getSquares(),
                dss.getMin(),
                dss.getMax(),
                dss.getAverage());
    }
}

class ShapesApplication {

    List<Canvas> canvases;
    double maxArea;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
    }

    public void readCanvases(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        canvases = br.lines()
                .map(line -> {
                    try {
                        return Canvas.createCanvas(line, maxArea);
                    } catch (IrregularCanvasException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        br.close();
    }

    public void printCanvases(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        canvases.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(pw::println);
        pw.flush();
    }
}

public class Shapes2Test {

    public static void main(String[] args) throws IOException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}