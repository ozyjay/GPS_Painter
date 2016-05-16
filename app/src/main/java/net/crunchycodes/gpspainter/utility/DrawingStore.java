package net.crunchycodes.gpspainter.utility;

import android.content.Context;
import android.util.Log;

import net.crunchycodes.gpspainter.model.Coordinate;
import net.crunchycodes.gpspainter.model.Drawing;
import net.crunchycodes.gpspainter.model.Point;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class DrawingStore {

    private final static String CURRENT_DRAWING = "current.txt";
    private Context context;

    public DrawingStore(Context context) {
        this.context = context;
    }

    // format is x,y,color
    public void saveFrom(Drawing drawing) {
        try {
            FileOutputStream output = context.openFileOutput(CURRENT_DRAWING, Context.MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));

            for (Point point : drawing.getPoints()) {
                writer.println(point.coordinate + "," + point.color);
            }
            writer.println();
            writer.flush();

            output.close();
        } catch (IOException e) {
            Log.e("DrawingStore", "error saving drawing: " + e.getMessage());
        }
    }

    public void loadInto(Drawing drawing) {
        try {
            FileInputStream input = context.openFileInput(CURRENT_DRAWING);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            drawing.clear();
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                int x = Integer.parseInt(data[0]);
                int y = Integer.parseInt(data[1]);
                int color = Integer.parseInt(data[2]);
                drawing.addPoint(new Coordinate(x, y), color);
                Log.i("DrawingStore",
                        String.format("loaded point %d %d %d", x, y, color));
            }

            input.close();
        } catch (Exception e) {
            Log.e("DrawingStore", "error loading drawing: " + e.getMessage());
        }
    }
}
