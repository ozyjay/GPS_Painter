package net.crunchycodes.gpspainter.model;

public class Point {
    public Coordinate coordinate;
    public int color;

    public Point(Coordinate coordinate, int color) {
        this.coordinate = coordinate;
        this.color = color;
    }

    @Override
    public String toString() {
        return coordinate + "," + color;
    }
}
