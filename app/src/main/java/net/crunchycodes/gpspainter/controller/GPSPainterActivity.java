package net.crunchycodes.gpspainter.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.crunchycodes.gpspainter.R;
import net.crunchycodes.gpspainter.model.Brush;
import net.crunchycodes.gpspainter.model.Drawing;
import net.crunchycodes.gpspainter.utility.DrawingStore;
import net.crunchycodes.gpspainter.utility.LocationUpdateListener;
import net.crunchycodes.gpspainter.utility.LocationUtility;
import net.crunchycodes.gpspainter.view.DrawingView;

public class GPSPainterActivity extends AppCompatActivity {

    private LocationUtility locationUtility;
    private Brush brush;
    private Drawing drawing;
    private DrawingView drawingView;
    private DrawingStore drawingStore;
    private GestureDetectorCompat gestureDetector;
    private SharedPreferences prefs;
    private ColorChoice colorChoice;
    private SettingsChoices settingsChoices;
    private HelpDialog helpDialog;

    private Animation blinkSlow, shake, shakeBig;

    public GPSPainterActivity() {
        colorChoice = new ColorChoice();
        settingsChoices = new SettingsChoices();
        helpDialog = new HelpDialog();
        brush = new Brush();
        drawing = new Drawing();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup
        blinkSlow = AnimationUtils.loadAnimation(this, R.anim.blink_slow);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        shakeBig = AnimationUtils.loadAnimation(this, R.anim.shake_big);

        prefs = getSharedPreferences("GPSPainter", MODE_PRIVATE);

        locationUtility = new LocationUtility(this);
        locationUtility.setAccuracy(prefs.getInt("location accuracy", 10));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        assert myToolbar != null;
        setSupportActionBar(myToolbar);

        drawingStore = new DrawingStore(this);

        drawingView = (DrawingView) findViewById(R.id.drawing);
        assert drawingView != null;

        brush.setScale(prefs.getInt("brush scale", 5));
        drawingView.assignDatasources(drawing, brush);
        drawingView.setSize(prefs.getInt("canvas size", 11));

        // callbacks
        settingsChoices.setListener(new SettingsChoicesListener() {
            @Override
            public void canvasSizeChanged(int value) {
                drawingView.setSize(value);
                drawingView.invalidate();
            }

            @Override
            public void locationAccuracyChanged(int value) {
                locationUtility.setAccuracy(value);
            }

            @Override
            public void brushScaleChanged(int value) {
                brush.setScale(value);
            }
        });

        locationUtility.setLocationUpdate(new LocationUpdateListener() {
            @Override
            public void update(double latitude, double longitude) {
                brush.moveTo(latitude, longitude);
                drawingView.invalidate();
            }
        });

        setupGestures();
    }

    private void setupGestures() {
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent startEvent, MotionEvent endEvent, float velocityX, float velocityY) {
                boolean result = false;
                if (Math.abs(velocityY) > 2000 && startEvent.getY() < endEvent.getY()) {
                    result = true;
                    if (drawing.redoPoint()) {
                        drawingView.invalidate();
                        drawingView.startAnimation(shake);
                    }

                } else if (Math.abs(velocityY) > 2000 && startEvent.getY() > endEvent.getY()) {
                    result = true;
                    if (drawing.undoPoint()) {
                        drawingView.invalidate();
                        drawingView.startAnimation(shake);
                    }
                }
                return result;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                drawing.addPoint(brush.getCursor(), prefs.getInt("color", R.color.white));
                drawingView.invalidate();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                brush.recalibrate();
                drawingView.invalidate();
                drawingView.startAnimation(blinkSlow);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        drawingStore.saveFrom(drawing);
        locationUtility.stop();
        drawingView.enable(false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        System.gc();
        super.onResume();
        locationUtility.start();
        drawingStore.loadInto(drawing);
        drawingView.enable(true);
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
        Log.i("GPSPainterActivity", "destroyed");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w("GPSPainterActivity", "low memory warning!!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                settingsChoices.show(getSupportFragmentManager(), "Change drawing settings");
                break;

            case R.id.help:
                helpDialog.show(getSupportFragmentManager(), "Help using GPS Painter");
                break;

            case R.id.color:
                colorChoice.show(getSupportFragmentManager(), "Pick a color");
                break;

            case R.id.clear:
                drawingView.startAnimation(shakeBig);
                drawing.clear();
                break;

            case R.id.save:
                drawingView.addImageToGallery();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
