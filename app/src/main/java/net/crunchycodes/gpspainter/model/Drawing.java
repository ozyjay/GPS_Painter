package net.crunchycodes.gpspainter.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Drawing {
    List<Point> points;
    int currentCount;

    public Drawing() {
        points = Collections.synchronizedList(new ArrayList<Point>());
        currentCount = 0;
    }

    public synchronized void addPoint(Coordinate newCoordinate, int color) {
        // prevent the same color from being added to the same point
        if (currentCount > 0) {
            Point mostRecentPoint = points.get(currentCount - 1);

            if (mostRecentPoint.coordinate.equals(newCoordinate) && color == points.get(currentCount - 1).color)
                return;
        }

        if (currentCount < points.size()) {
            // remove beyond current
            points = points.subList(0, currentCount);
        }
        points.add(new Point(newCoordinate, color));
        ++currentCount;
    }

    public synchronized boolean redoPoint() {
        if (currentCount < points.size()) {
            ++currentCount;
            return true;
        }
        return false;
    }

    public synchronized boolean undoPoint() {
        if (currentCount > 0) {
            --currentCount;
            return true;
        }
        return false;
    }

    public synchronized Point[] getPoints() {
        Point[] results = new Point[currentCount];

        for (int i = 0; i < currentCount; ++i) {
            results[i] = points.get(i);
        }

        return results;
    }

    public synchronized void clear() {
        points.clear();
        currentCount = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Point point : points) {
            builder.append(point);
        }

        return builder.toString();
    }
}
