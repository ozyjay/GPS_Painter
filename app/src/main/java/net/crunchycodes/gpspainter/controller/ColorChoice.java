package net.crunchycodes.gpspainter.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import net.crunchycodes.gpspainter.R;
import net.crunchycodes.gpspainter.view.ColorGridAdapter;
import net.crunchycodes.gpspainter.view.ColorView;

public class ColorChoice extends AppCompatDialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();

        final SharedPreferences sharedPreferences = dialog.getContext().getSharedPreferences("GPSPainter", Activity.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.color_choice, container, false);

        ColorView currentColor = (ColorView) view.findViewById(R.id.current_color);
        currentColor.setBackgroundColor(sharedPreferences.getInt("color", Color.BLACK));

        final int[] colors = view.getResources().getIntArray(R.array.colors);
        GridView colorGrid = (GridView) view.findViewById(R.id.color_grid);
        colorGrid.setAdapter(new ColorGridAdapter(view.getContext(), colors, colorGrid.getNumColumns()));

        colorGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ColorView colorView = (ColorView) view;
                view.animate().setDuration(2).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        colorView.setTouched(true);
                        Log.i("ColorChoice", "animation started");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dismiss();
                    }
                }).start();

                sharedPreferences.edit()
                        .putInt("color", colors[position])
                        .apply();
            }
        });

        dialog.setTitle(getTag());

        return view;
    }
}
