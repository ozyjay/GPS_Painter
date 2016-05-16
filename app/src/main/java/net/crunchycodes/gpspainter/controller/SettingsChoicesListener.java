package net.crunchycodes.gpspainter.controller;

public interface SettingsChoicesListener {
    void canvasSizeChanged(int value);

    void locationAccuracyChanged(int value);

    void brushScaleChanged(int value);
}
