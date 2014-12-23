package ru.ifmo.mobdev.colloquium3;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.ifmo.mobdev.colloquium3.db.CurrencyDatabase;
import ru.ifmo.mobdev.colloquium3.db.MyContentProvider;

/**
 * @author sugakandrey
 */
public class ExchangeActivity extends Activity {

    private TextView exchangeTo;
    private TextView exchangeToAmount;
    private TextView currentRate;
    private String currency;
    private TextView balance;
    private double rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_activity);
        exchangeTo = (TextView) findViewById(R.id.exchange_to);
        currency = getIntent().getStringExtra(MainActivity.CURRENCY);
        exchangeTo.setText(currency);
        exchangeToAmount = (TextView) findViewById(R.id.exchange_to_amount);
        currentRate = (TextView) findViewById(R.id.current_rate);
        balance = (TextView) findViewById(R.id.exchange_from_amount);
        setViews();
    }

    public void setViews() {
        Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI_CURRENCIES, new String[]{CurrencyDatabase.CURRENCY_AMOUNT,
                CurrencyDatabase.CURRENCY_NAME, CurrencyDatabase.CURRENCY_RATE}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(CurrencyDatabase.CURRENCY_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(CurrencyDatabase.CURRENCY_AMOUNT));
            if (name.equals(currency)) {
                rate = cursor.getDouble(cursor.getColumnIndex(CurrencyDatabase.CURRENCY_RATE));
                currentRate.setText(String.valueOf(rate));
                exchangeTo.setText(name);
                exchangeToAmount.setText(String.valueOf(amount));
            } else if (name.equals("RUB")) {
                balance.setText(String.valueOf(amount));
            }
            cursor.moveToNext();
        }
    }

    public void onBuyClicked(View view) {
        int balance = Integer.parseInt(this.balance.getText().toString());
        int exchanged = Integer.parseInt(exchangeToAmount.getText().toString());
        if (balance < rate)
            Toast.makeText(this, "Not enough currency", Toast.LENGTH_LONG).show();
        else {
            ContentValues cv = new ContentValues();
            cv.put(CurrencyDatabase.CURRENCY_AMOUNT, exchanged + 1);
            getContentResolver().update(MyContentProvider.CONTENT_URI_CURRENCIES, cv, CurrencyDatabase.CURRENCY_NAME + " = ?", new String[] {currency});
            cv.clear();
            cv.put(CurrencyDatabase.CURRENCY_AMOUNT, balance - rate);
            getContentResolver().update(MyContentProvider.CONTENT_URI_CURRENCIES, cv, CurrencyDatabase.CURRENCY_NAME + " = ?", new String[]{"RUB"});
        }
        setViews();
    }

    public void onSellClicked(View view) {
        int balance = Integer.parseInt(this.balance.getText().toString());
        int exchanged = Integer.parseInt(exchangeToAmount.getText().toString());
        if (exchanged < 1)
            Toast.makeText(this, "Not enough currency", Toast.LENGTH_LONG).show();
        else {
            ContentValues cv = new ContentValues();
            cv.put(CurrencyDatabase.CURRENCY_AMOUNT, exchanged - 1);
            getContentResolver().update(MyContentProvider.CONTENT_URI_CURRENCIES, cv, CurrencyDatabase.CURRENCY_NAME + " = ?", new String[] {currency});
            cv.clear();
            cv.put(CurrencyDatabase.CURRENCY_AMOUNT, balance + rate);
            getContentResolver().update(MyContentProvider.CONTENT_URI_CURRENCIES, cv, CurrencyDatabase.CURRENCY_NAME + " = ?", new String[]{"RUB"});
        }
        setViews();
    }


}
