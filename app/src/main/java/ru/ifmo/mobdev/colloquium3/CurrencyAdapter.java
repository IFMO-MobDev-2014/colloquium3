package ru.ifmo.mobdev.colloquium3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author sugakandrey
 */
public class CurrencyAdapter extends BaseAdapter {
    private ArrayList<Currency> items;
    private Context context;

    public CurrencyAdapter(ArrayList<Currency> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.currency_view, null);
        } else
            view = convertView;
        TextView currencyName = (TextView) view.findViewById(R.id.currency_name);
        currencyName.setText(items.get(position).getName());
        TextView currencyRate = (TextView) view.findViewById(R.id.currency_rate);
        Integer amount = items.get(position).getAmount();
        if (amount != null) {
            currencyRate.setText(amount.toString());
        } else
            currencyRate.setText(String.valueOf(items.get(position).getRate()));
        return view;
    }
}
