package net.crunchycodes.gpspainter.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import net.crunchycodes.gpspainter.R;
import net.crunchycodes.gpspainter.utility.PercentagesUtility;

public class SettingsChoices extends AppCompatDialogFragment {

    private PercentagesUtility canvasSizeUtility, locationAccuracyUtility, brushScaleUtility;
    private int brushScale, canvasSize, locationAccuracy;
    private SettingsChoicesListener listener;

    public void setListener(SettingsChoicesListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        canvasSizeUtility = new PercentagesUtility(5, 31, 2);
        locationAccuracyUtility = new PercentagesUtility(1, 20, 1);
        brushScaleUtility = new PercentagesUtility(4, 8, 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();

        final SharedPreferences prefs = dialog.getContext().getSharedPreferences("GPSPainter", Activity.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.settings_choices, container, false);

        canvasSize = prefs.getInt("canvas size", 11);
        locationAccuracy = prefs.getInt("location accuracy", 10);
        brushScale = prefs.getInt("brush scale", 5);

        SeekBar canvasSizeSelect = (SeekBar) view.findViewById(R.id.canvas_size_select);
        canvasSizeSelect.setProgress(canvasSizeUtility.percentage(canvasSize));

        SeekBar locationAccuracySelect = (SeekBar) view.findViewById(R.id.location_accuracy_select);
        locationAccuracySelect.setProgress(locationAccuracyUtility.percentage(locationAccuracy));

        SeekBar brushScaleSelect = (SeekBar) view.findViewById(R.id.brush_scale_select);
        brushScaleSelect.setProgress(brushScaleUtility.percentage(brushScale));

        final TextView canvasSizeText = (TextView) view.findViewById(R.id.canvas_size);
        canvasSizeText.setText(String.format("%d", canvasSize));

        final TextView locationAccuracyText = (TextView) view.findViewById(R.id.location_accuracy);
        locationAccuracyText.setText(String.format("%d", locationAccuracy));

        final TextView brushScaleText = (TextView) view.findViewById(R.id.brush_scale);
        brushScaleText.setText(String.format("%d", brushScale));

        canvasSizeSelect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                canvasSize = canvasSizeUtility.value(progress);
                canvasSizeText.setText(String.format("%d", canvasSize));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                prefs.edit().putInt("canvas size", canvasSize).apply();

                if (listener == null) return;
                listener.canvasSizeChanged(canvasSize);
            }
        });

        locationAccuracySelect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                locationAccuracy = locationAccuracyUtility.value(progress);
                locationAccuracyText.setText(String.format("%d", locationAccuracy));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                prefs.edit().putInt("location accuracy", locationAccuracy).apply();

                if (listener == null) return;
                listener.locationAccuracyChanged(locationAccuracy);

            }
        });

        brushScaleSelect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                brushScale = brushScaleUtility.value(progress);
                brushScaleText.setText(String.format("%d", brushScale));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                prefs.edit().putInt("brush scale", brushScale).apply();

                if (listener == null) return;
                listener.brushScaleChanged(brushScale);
            }
        });

        dialog.setTitle(getTag());
        return view;
    }
}
