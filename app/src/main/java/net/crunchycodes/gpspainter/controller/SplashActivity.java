package net.crunchycodes.gpspainter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // pause on splash for ~2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.w("SplashActivity", e.getMessage());
        }

        Intent intent = new Intent(this, GPSPainterActivity.class);
        startActivity(intent);
        finish();
    }
}
