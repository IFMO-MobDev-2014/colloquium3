package ru.ifmo.colloquium3.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ru.ifmo.colloquium3.R;
import ru.ifmo.colloquium3.db.DatabaseHelper;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
public class WalletsCursorAdapter extends CursorAdapter {
    public WalletsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.wallet_row, null);
        bindView(view, context, cursor);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.WALLET_NAME_KEY));
        double value = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.WALLET_VALUE_KEY));
        TextView textName = (TextView) view.findViewById(R.id.wallet_name);
        TextView textValue = (TextView) view.findViewById(R.id.wallet_value);
        textName.setText(name);
        textValue.setText(String.format("%.2f", value));
    }
}
