package net.crunchycodes.gpspainter.utility;

// this utility class allows you to define a mapping between percentage values (0..100)
// and a specified set of values with a know min value, max value, and step size
public class PercentagesUtility {
    private int min, step;
    private double scale;
    private int sizeOfValues;

    public PercentagesUtility(int min, int max, int step) {
        this.min = min;
        this.step = step;
        sizeOfValues = ((max - min) / step) + 1;
        scale = 101.0 / sizeOfValues;
        if (max != value(100)) {
            String message = String.format("incorrect max value for range %d -> %d steps %d",
                    min, max, step);
            throw new PercentagesUtilityException(message);
        }
    }

    public int value(int percentage) {
        return min + step * (int) (percentage / scale);
    }

    public int percentage(int value) {
        double position = Math.floor((value - min) / step) + 1;
        return (int) (position / sizeOfValues * 100);
    }

    public static class PercentagesUtilityException extends RuntimeException {
        public PercentagesUtilityException(String msg) {
            super(msg);
        }
    }
}
