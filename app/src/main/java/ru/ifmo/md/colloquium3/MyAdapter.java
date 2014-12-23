package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Галина on 23.12.2014.
 */
public class MyAdapter extends ArrayAdapter<CurrencyType> {
    Context context;
    ArrayList<CurrencyType> currencyTypeArrayList;
    double costs;
    public MyAdapter(Context context, ArrayList<CurrencyType> currencyTypeArrayList) {
        super(context,R.layout.list_item,currencyTypeArrayList);
        this.context = context;
        this.currencyTypeArrayList = currencyTypeArrayList;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View elementView = inflater.inflate(R.layout.list_item, parent,false);
        TextView currency_cost = (TextView) elementView.findViewById(R.id.currency_cost);
        TextView currency_name = (TextView) elementView.findViewById(R.id.currency_name);
        costs=(double) currencyTypeArrayList.get(position).cost/100;
        currency_cost.setText(Double.toString(costs));
        currency_name.setText(currencyTypeArrayList.get(position).name);
        return elementView;
    }
}