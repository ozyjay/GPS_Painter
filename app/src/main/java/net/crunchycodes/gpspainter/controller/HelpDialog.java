package net.crunchycodes.gpspainter.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.crunchycodes.gpspainter.R;

public class HelpDialog extends AppCompatDialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Dialog dialog = getDialog();
        View view = inflater.inflate(R.layout.help, container, false);

        dialog.setTitle(getTag());

        return view;
    }
}
