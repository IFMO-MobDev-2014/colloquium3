package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CurrencyCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public CurrencyCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.currency_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

        int current_price = cursor.getInt(cursor.getColumnIndexOrThrow("current_price"));
        TextView text = (TextView) view.findViewById(R.id.currency_item_text);
        text.setText(name + " " + current_price / 100 + "." + current_price % 100);
    }
}
