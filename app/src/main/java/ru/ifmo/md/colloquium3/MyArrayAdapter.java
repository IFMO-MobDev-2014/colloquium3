package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MSviridenkov on 23.12.2014.
 */
public class MyArrayAdapter extends ArrayAdapter<Value> {
    public MyArrayAdapter(Context context, ArrayList<Value> values) {
        super(context, R.layout.list_item, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Value value = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, null);
        }
        ((TextView) convertView.findViewById(R.id.name))
                .setText(value.getName());
        ((TextView) convertView.findViewById(R.id.count))
                .setText(value.getCount());

        return convertView;
    }
}
