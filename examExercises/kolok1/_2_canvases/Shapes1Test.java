package kolok1._2_canvases;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Canvas implements Comparable<Canvas>{
    String id;
    List<Integer> squares;

    public Canvas(String id, List<Integer> squares) {
        this.id = id;
        this.squares = squares;
    }

    public static Canvas createCanvas(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> squares = new ArrayList<>();
        for(int i=1; i<parts.length; i++) {
            squares.add(Integer.parseInt(parts[i]));
        }
        return new Canvas(id, squares);
    }

    public int getSize() {
        return squares.size();
    }

    public int getPerimeter() {
        int perimeter = 0;
        for(Integer square : squares) {
            perimeter += square * 4;
        }
        return perimeter;
    }


    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(this.getPerimeter(), o.getPerimeter());
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", id, getSize(), getPerimeter());
    }
}

class ShapesApplication {

    List<Canvas> canvases;

    public int getTotalSize() {
        int counter = 0;
        for(Canvas canvas : canvases) {
            counter += canvas.getSize();
        }
        return counter;
    }
    public int readCanvases(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        canvases = br.lines()
                .map(Canvas::createCanvas)
                .collect(Collectors.toList());
        br.close();
        return getTotalSize();
    }

    public void printLargestCanvasTo(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        Canvas max = canvases.stream()
                .max(Canvas::compareTo)
                .orElse(null);
        pw.flush();
        System.out.println(max);
    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        try {
            System.out.println(shapesApplication.readCanvases(System.in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}