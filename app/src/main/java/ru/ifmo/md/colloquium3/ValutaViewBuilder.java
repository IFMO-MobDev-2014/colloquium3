package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by german on 22.12.14.
 */
public class ValutaViewBuilder {
    private final int layoutId = R.layout.valuta_item;

    private String valutaName;
    private double valutaValue;
    private View.OnClickListener listener;
    private Context context;

    ValutaViewBuilder(Context context) {
        this.context = context;
    }

    public ValutaViewBuilder withValutaName(String valutaName) {
        this.valutaName = valutaName;
        return this;
    }

    public ValutaViewBuilder withValutaValue(double valutaValue) {
        this.valutaValue = valutaValue;
        return this;
    }

    public ValutaViewBuilder withClickListener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public View create(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        if (valutaName != null) {
            ((TextView)view.findViewById(R.id.valuta_name)).setText(valutaName);
        } else {
            ((TextView)view.findViewById(R.id.valuta_name)).setText(R.string.fake_value);
        }
        ((TextView)view.findViewById(R.id.valuta_value)).setText(Double.toString(valutaValue));
        if (listener != null) {
            view.setOnClickListener(listener);
            view.setEnabled(true);
            view.setClickable(true);
        }

        return view;
    }
}
