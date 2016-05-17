package net.crunchycodes.gpspainter.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import net.crunchycodes.gpspainter.R;
import net.crunchycodes.gpspainter.model.Brush;
import net.crunchycodes.gpspainter.model.Coordinate;
import net.crunchycodes.gpspainter.model.Drawing;
import net.crunchycodes.gpspainter.model.Point;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DrawingView extends View {

    Handler handler;
    private float cellWidth, cellHeight;
    private int rowCount, columnCount;
    private int[] cursor;
    private Drawing drawing;
    private Paint cellOutline, cursorOutline, pointFill, background;
    private Brush brush;
    private Context context;
    private boolean cursorVisible, running;
    Runnable updater = new Runnable() {
        @Override
        public void run() {
            if (running) {
                updateCursor();
                invalidate();
                handler.postDelayed(updater, 1000);
            }
        }
    };

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        handler = new Handler();
        rowCount = columnCount = 5;
        cursor = new int[]{columnCount / 2, rowCount / 2};
        setupPaints();
        setBackgroundColor(background.getColor());
    }

    public void assignDatasources(Drawing drawing, Brush brush) {
        this.drawing = drawing;
        this.brush = brush;
    }

    public void enable(boolean value) {
        running = value;
        if (running) {
            handler.post(updater);
        }
    }

    public void setSize(int size) {
        rowCount = columnCount = size;
        cellWidth = 1.0f * (getWidth() - 2 * getPaddingLeft()) / rowCount;
        cellHeight = 1.0f * (getHeight() - 2 * getPaddingTop()) / columnCount;
    }

    public void addImageToGallery() {
        Bitmap image = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        drawPoints(canvas);

        SimpleDateFormat formatter = new SimpleDateFormat("yMdkms", Locale.getDefault());
        Date now = new Date();
        String title = String.format("GPS Painter Image (%s)", formatter.format(now));
        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                image, title, "Created by GPS Painter!");
    }

    private void setupPaints() {
        cellOutline = new Paint();
        cellOutline.setColor(Color.BLACK);
        cellOutline.setStrokeWidth(1);
        cellOutline.setStyle(Paint.Style.STROKE);

        cursorOutline = new Paint();
        cursorOutline.setColor(Color.BLACK);
        cursorOutline.setStrokeWidth(6);
        cursorOutline.setStyle(Paint.Style.STROKE);

        pointFill = new Paint();
        pointFill.setColor(Color.BLACK);
        pointFill.setStyle(Paint.Style.FILL);

        background = new Paint();
        background.setColor(ContextCompat.getColor(context, R.color.paper));
        background.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        cellWidth = 1.0f * (width - 2 * getPaddingLeft()) / rowCount;
        cellHeight = 1.0f * (height - 2 * getPaddingTop()) / columnCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        drawCells(canvas);
        drawPoints(canvas);
        if (cursorVisible) drawCursor(canvas);

        canvas.restore();
    }

    private void drawCells(Canvas canvas) {
        float cellTop = getPaddingTop();
        for (int row = 0; row < rowCount; ++row) {
            float cellLeft = getPaddingLeft();
            for (int column = 0; column < columnCount; ++column) {
                float cellRight = cellLeft + cellWidth;
                float cellBottom = cellTop + cellHeight;
                canvas.drawRect(cellLeft, cellTop, cellRight, cellBottom, cellOutline);
                cellLeft += cellWidth;
            }
            cellTop += cellHeight;
        }
    }

    private void drawPoints(Canvas canvas) {
        if (drawing == null) return;

        // points are offset by center cell (the origin)
        for (Point point : drawing.getPoints()) {
            pointFill.setColor(point.color);
            float left = (point.coordinate.x + columnCount / 2) * cellWidth + getPaddingLeft();
            float top = (point.coordinate.y + rowCount / 2) * cellHeight + getPaddingTop();
            canvas.drawRect(left, top, left + cellWidth, top + cellHeight, pointFill);
        }
    }

    private void drawCursor(Canvas canvas) {
        float cursorLeft = cursor[0] * cellWidth + getPaddingLeft();
        float cursorTop = cursor[1] * cellHeight + getPaddingTop();
        float cursorRight = cursorLeft + cellWidth;
        float cursorBottom = cursorTop + cellHeight;
        canvas.drawRect(cursorLeft, cursorTop, cursorRight, cursorBottom, cursorOutline);
    }

    private void updateCursor() {
        // the cursor is offset by the center cell (the origin)
        Coordinate coordinate = brush.getCursor();
        this.cursor[0] = coordinate.x + columnCount / 2;
        this.cursor[1] = coordinate.y + rowCount / 2;

        // run the countdown state machine
        cursorVisible = !cursorVisible;
    }
}
