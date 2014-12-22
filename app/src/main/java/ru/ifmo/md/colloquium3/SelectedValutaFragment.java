package ru.ifmo.md.colloquium3;

import android.app.Fragment;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by german on 22.12.14.
 */
public class SelectedValutaFragment extends Fragment {
    public static final String BUNDLE_KEY = "selected_valuta_fragment";

    private Button buyButton;
    private Button sellButton;

    private String valutaName;
    private double valutaValue;
    private double valutaBalance;
    private double rubBalance;

    public SelectedValutaFragment() {
        super();
    }

    public SelectedValutaFragment(String valutaName) {
        this.valutaName = valutaName;
    }

    private void updateBalanceAndValue(View view) {
        this.valutaValue = MoneyManager.getValue(getActivity().getContentResolver(), valutaName);
        this.valutaBalance = MoneyManager.getBalance(getActivity().getContentResolver(), valutaName);
        this.rubBalance = MoneyManager.getBalance(getActivity().getContentResolver(), "RUB");

        valutaBalance = ((double)((long)(valutaBalance * 100.0))) / 100.0;
        rubBalance = ((double)((long)(rubBalance * 100.0))) / 100.0;


        ((TextView)view.findViewById(R.id.selected_valuta_balance)).setText(Double.toString(valutaBalance));
        ((TextView)view.findViewById(R.id.selected_valuta_value)).setText(Double.toString(valutaValue));
        ((TextView)view.findViewById(R.id.rub_balance)).setText(Double.toString(rubBalance));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.selected_valuta_fragment, null);
        ((TextView)view.findViewById(R.id.selected_valuta_name)).setText(valutaName);

        updateBalanceAndValue(view);

        buyButton = (Button)view.findViewById(R.id.selected_valuta_buy);
        sellButton = (Button)view.findViewById(R.id.selected_valuta_sell);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = getActivity().getContentResolver();
                if (MoneyManager.decreaseBalance(resolver, "RUB", valutaValue)) {
                    MoneyManager.increaseBalance(resolver, valutaName, 1);
                    updateBalanceAndValue(view);
                }
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = getActivity().getContentResolver();
                if (MoneyManager.decreaseBalance(resolver, valutaName, 1)) {
                    MoneyManager.increaseBalance(resolver, "RUB", valutaValue);
                    updateBalanceAndValue(view);
                }
            }
        });

        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(valutaName + " trade");
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }
}
