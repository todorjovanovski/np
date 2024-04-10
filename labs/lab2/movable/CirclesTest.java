package lab2.movable;
/*import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;*/
import java.util.*;

class MovableObjectNotFittableException extends Throwable {
    private String message;

    public MovableObjectNotFittableException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

class ObjectCanNotBeMovedException extends Throwable {
    private int x, y;

    public ObjectCanNotBeMovedException(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getMessage() {
        return "Point (" + x + "," + y + ") is out of bounds";
    }
}

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();
}

class MovingPoint implements Movable {

    private int x, y;
    private int xSpeed, ySpeed;

    public MovingPoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (y + ySpeed > MovablesCollection.getMaxY())
            throw new ObjectCanNotBeMovedException(x, y+ySpeed);
        else
            y += ySpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - xSpeed < 0)
            throw new ObjectCanNotBeMovedException(x-xSpeed, y);
        else
            x -= xSpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y - ySpeed < 0)
            throw new ObjectCanNotBeMovedException(x, y-ySpeed);
        else
            y -= ySpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (x + xSpeed > MovablesCollection.getMaxX())
            throw new ObjectCanNotBeMovedException(x+xSpeed, y);
        else
            x += xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y + ")";
    }
}

class MovingCircle implements Movable {
    private int radius;
    private MovingPoint center;

    public MovingCircle(int radius, MovingPoint center) {
        this.radius = radius;
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates " +
                "(" + center.getCurrentXPosition() + "," + center.getCurrentYPosition() + ") and radius " + radius;
    }
}

class MovablesCollection {
    private Movable[] movables;
    private static int MAX_X = 0;
    private static int MAX_Y = 0;

    int maxCapacity;
    int size;

    public MovablesCollection(int xMax, int yMax) {
        MAX_X = xMax;
        MAX_Y = yMax;
        this.maxCapacity = 1;
        this.size = 0;
        this.movables = new Movable[maxCapacity];
    }

    public static void setxMax(int maxX) {
        MAX_X = maxX;
    }

    public static void setyMax(int maxY) {
        MAX_Y = maxY;
    }

    public static int getMaxX() {
        return MAX_X;
    }

    public static int getMaxY() {
        return MAX_Y;
    }

    public void addMovableObject(Movable movable) throws MovableObjectNotFittableException {
        if (movable instanceof MovingPoint) {
            if (movable.getCurrentXPosition() < 0 || movable.getCurrentXPosition() > MAX_X
                    && movable.getCurrentYPosition() < 0 || movable.getCurrentYPosition() > MAX_Y)
                throw new MovableObjectNotFittableException(movable + " can not be fitted into the collection");
        } else if (movable instanceof MovingCircle) {
            if (movable.getCurrentXPosition() + ((MovingCircle) movable).getRadius() > MAX_X
                    || movable.getCurrentXPosition() - ((MovingCircle) movable).getRadius() < 0
                    || movable.getCurrentYPosition() + ((MovingCircle) movable).getRadius() > MAX_Y
                    || movable.getCurrentYPosition() - ((MovingCircle) movable).getRadius() < 0)
                throw new MovableObjectNotFittableException("Movable circle with center (" +
                        movable.getCurrentXPosition() + "," + movable.getCurrentYPosition() + ") and radius " +
                         ((MovingCircle) movable).getRadius() + " can not be fitted into the collection");
        }
        if (size == maxCapacity) {
            maxCapacity *= 2;
            Movable[] newMovables = new Movable[maxCapacity];
            System.arraycopy(movables, 0, newMovables, 0, movables.length);
            movables = newMovables;
        }
        movables[size++] = movable;
    }


    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException {
        for (Movable movable : movables) {
            if (movable instanceof MovingCircle && type == TYPE.CIRCLE) {
                if (direction == DIRECTION.UP) movable.moveUp();
                else if (direction == DIRECTION.DOWN) movable.moveDown();
                else if (direction == DIRECTION.LEFT) movable.moveLeft();
                else if (direction == DIRECTION.RIGHT) movable.moveRight();
            } else if (movable instanceof MovingPoint && type == TYPE.POINT) {
                if (direction == DIRECTION.UP) movable.moveUp();
                else if (direction == DIRECTION.DOWN) movable.moveDown();
                else if (direction == DIRECTION.LEFT) movable.moveLeft();
                else if (direction == DIRECTION.RIGHT) movable.moveRight();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Collection of movable objects with size " + size + ":\n");

        for(Movable movable : movables) {
            if(movable != null)
                str.append(movable.toString()).append("\n");
        }
        return str.toString();
    }

}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovingPoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovingCircle(radius, new MovingPoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        } catch (ObjectCanNotBeMovedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        } catch (ObjectCanNotBeMovedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        } catch (ObjectCanNotBeMovedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        } catch (ObjectCanNotBeMovedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());


    }
}
