package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Амир on 23.12.2014.
 */



public class ItemAdapter extends BaseAdapter {

    ArrayList<Item> items;
    Context context;

    public ItemAdapter(ArrayList<Item> items, Context context) {
        super();
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_view, null);
        }
        Item t = items.get(i);

        TextView title = (TextView) view.findViewById(R.id.textView);
        TextView cost = (TextView) view.findViewById(R.id.textView2);

        title.setText(t.name);
        cost.setText(String.format("%.2f", t.cost));

        return view;
    }
}

