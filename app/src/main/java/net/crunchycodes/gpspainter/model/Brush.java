package net.crunchycodes.gpspainter.model;

import android.util.Log;

public class Brush {
    private double[] flag;
    private double[] current;
    private double scale;

    public Brush() {
        flag = new double[]{0, 0};
        current = new double[]{0, 0};
        scale = 5;
    }

    public void setScale(int amount) {
        this.scale = Math.pow(10, amount);
        Log.i("Brush", "scale is now: " + scale);
    }

    public void recalibrate() {
        flag[0] = current[0];
        flag[1] = current[1];
    }

    public void moveTo(double latitude, double longitude) {
        current[0] = latitude;
        current[1] = longitude;
    }

    public Coordinate getCursor() {
        int x = ((int) (scale * (current[0] - flag[0])));
        int y = (int) (scale * (current[1] - flag[1]));
        return new Coordinate(x, y);
    }
}
