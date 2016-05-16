package net.crunchycodes.gpspainter.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;

public class ColorView extends View {

    private Paint outlinePaint;
    private boolean touched;

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }


    public ColorView(Context context) {
        super(context);
        setup();
    }

    private void setup() {
        setMinimumHeight(100);
        setMinimumWidth(50);

        outlinePaint = new Paint();
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(5);
        outlinePaint.setColor(Color.BLACK);
    }

    public void setTouched(boolean touched) {
        this.touched = touched;

        // a useful side-effect is to adjust the outline color before redrawing!
        ColorDrawable colorDrawable = (ColorDrawable) getBackground();
        int color = isColorLight(colorDrawable.getColor()) ? Color.BLACK : Color.WHITE;
        outlinePaint.setColor(color);
        invalidate();
    }

    private boolean isColorLight(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        double lightness = (0.299 * red + 0.587 * green + 0.114 * blue) / 255;
        return lightness >= 0.5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (touched) {
            canvas.drawRect(10, 10, getWidth() - 10, getHeight() - 10, outlinePaint);
        }
    }
}
