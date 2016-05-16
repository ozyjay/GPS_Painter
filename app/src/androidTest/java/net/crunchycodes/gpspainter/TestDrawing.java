package net.crunchycodes.gpspainter;

import net.crunchycodes.gpspainter.model.Coordinate;
import net.crunchycodes.gpspainter.model.Drawing;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TestDrawing {
    @Test
    public void testAddUndoRedo() {
        Drawing drawing = new Drawing();

        drawing.addPoint(new Coordinate(0, 0), 0);
        Assert.assertEquals(1, drawing.getPoints().length);

        drawing.addPoint(new Coordinate(1, 1), 1);
        drawing.addPoint(new Coordinate(2, 2), 2);
        Assert.assertEquals(3, drawing.getPoints().length);

        System.out.println(Arrays.toString(drawing.getPoints()));

        drawing.undoPoint();
        drawing.undoPoint();
        Assert.assertEquals(1, drawing.getPoints().length);

        drawing.addPoint(new Coordinate(3, 3), 3);
        Assert.assertEquals(2, drawing.getPoints().length);

        System.out.println(Arrays.toString(drawing.getPoints()));

        drawing.redoPoint();
        Assert.assertEquals(2, drawing.getPoints().length);

        System.out.println(Arrays.toString(drawing.getPoints()));

        drawing.undoPoint();
        Assert.assertEquals(1, drawing.getPoints().length);

        System.out.println(Arrays.toString(drawing.getPoints()));

        drawing.addPoint(new Coordinate(4, 4), 4);
        drawing.addPoint(new Coordinate(5, 5), 5);
        drawing.addPoint(new Coordinate(6, 6), 6);
        Assert.assertEquals(4, drawing.getPoints().length);

        System.out.println(Arrays.toString(drawing.getPoints()));

        drawing.undoPoint();
        drawing.undoPoint();
        drawing.redoPoint();
        drawing.addPoint(new Coordinate(7, 7), 7);
        Assert.assertEquals(4, drawing.getPoints().length);

        System.out.println(Arrays.toString(drawing.getPoints()));
    }
}
