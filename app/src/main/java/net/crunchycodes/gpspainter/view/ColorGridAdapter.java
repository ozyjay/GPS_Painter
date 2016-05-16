package net.crunchycodes.gpspainter.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ColorGridAdapter extends BaseAdapter {
    private Context context;
    private int[] colorIDs;
    private int columnCount;

    public ColorGridAdapter(Context context, int[] colors, int columnCount) {
        this.context = context;
        colorIDs = colors;
        this.columnCount = columnCount;
    }

    @Override
    public int getCount() {
        return colorIDs.length;
    }

    @Override
    public Object getItem(int position) {
        return colorIDs[position];
    }

    @Override
    public long getItemId(int position) {
        return position / columnCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColorView itemView;
        if (convertView == null) {
            itemView = new ColorView(context);
        } else {
            itemView = (ColorView) convertView;
        }

        itemView.setBackgroundColor(colorIDs[position]);
        return itemView;
    }
}
