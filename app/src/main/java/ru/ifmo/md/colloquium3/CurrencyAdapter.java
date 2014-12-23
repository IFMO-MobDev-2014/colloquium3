package ru.ifmo.md.colloquium3;

import android.app.LoaderManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Алексей on 23.12.2014.
 */
public class CurrencyAdapter extends BaseAdapter {

    private ArrayList<Currency> data;

    CurrencyAdapter() {
        data = new ArrayList<>();
    }

    public void add(Currency x) {
        data.add(x);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Currency getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v;
        if (view == null) {
            v = android.view.LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.currency_layout, viewGroup, false);
        } else {
            v = view;
        }
        Currency current = getItem(position);
        ((TextView) v.findViewById(R.id.currency_name)).setText(current.getName());
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        ((TextView) v.findViewById(R.id.currency_value)).setText(df.format(current.getValue()));
        return v;
    }
}
