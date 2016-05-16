package net.crunchycodes.gpspainter.model;

public class Coordinate {
    public int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return x * 10 + y;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Coordinate) {
            Coordinate coordinate = (Coordinate) object;
            return x == coordinate.x && y == coordinate.y;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s,%s", x, y);
    }
}
