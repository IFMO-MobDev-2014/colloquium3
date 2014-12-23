package ru.ifmo.md.colloquium3;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;


public class ExchangeActivity extends ActionBarActivity {

    private double value;
    private String name;
    private DecimalFormat df;
    private Account accountRub;
    private Account accountNotRub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        name = getIntent().getStringExtra(MainActivity.CURRENCY_NAME);
        Log.d("ONCREATE", name);
        value = getIntent().getDoubleExtra(MainActivity.CURRENCY_VALUE, 11);

        ((TextView) findViewById(R.id.exchange_currency_name)).setText(name);

        ((TextView) findViewById(R.id.course)).setText(df.format(value));
        updateAccount();
        updateButton();
        ((Button) findViewById(R.id.buy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountRub.setValue(accountRub.getValue() - value);
                accountNotRub.setValue(accountNotRub.getValue() + 1);
                insertAccount();
                updateAccount();
            }
        });
        ((Button) findViewById(R.id.sell)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountRub.setValue(accountRub.getValue() + value);
                accountNotRub.setValue(accountNotRub.getValue() - 1);
                insertAccount();
                updateAccount();
            }
        });

    }

    void updateAccount() {
        Cursor cursor = getContentResolver().query(CurrencyContentProvider.ACCOUNT_CONTENT_URI, null,
                CurrencyDatabaseHelper.ACCOUNT_NAME + " = 'RUB'", null, null);
        cursor.moveToNext();
        accountRub = CurrencyDatabaseHelper.AccountCursor.getAccount(cursor);
        cursor = getContentResolver().query(CurrencyContentProvider.ACCOUNT_CONTENT_URI, null,
                CurrencyDatabaseHelper.ACCOUNT_NAME + " = '"+ name + "'", null, null);
        cursor.moveToNext();
        accountNotRub = CurrencyDatabaseHelper.AccountCursor.getAccount(cursor);
        ((TextView) findViewById(R.id.exchange_rouble_value)).setText(df.format(accountRub.getValue()));
        ((TextView) findViewById(R.id.exchange_currency_value)).setText(df.format(accountNotRub.getValue()));
    }

    void updateButton() {
        ((Button) findViewById(R.id.buy)).setEnabled(accountRub.getValue() >= value);
        ((Button) findViewById(R.id.sell)).setEnabled(accountNotRub.getValue() >= 1);
    }

    void insertAccount() {
        getContentResolver().delete(CurrencyContentProvider.ACCOUNT_CONTENT_URI, CurrencyDatabaseHelper.ACCOUNT_NAME + " = '" + accountRub.name + "'", null);
        ContentValues cv = new ContentValues();
        cv.put(CurrencyDatabaseHelper.ACCOUNT_NAME, accountRub.getName());
        cv.put(CurrencyDatabaseHelper.ACCOUNT_COUNT, accountRub.getValue());
        getContentResolver().insert(CurrencyContentProvider.ACCOUNT_CONTENT_URI, cv);

        getContentResolver().delete(CurrencyContentProvider.ACCOUNT_CONTENT_URI, CurrencyDatabaseHelper.ACCOUNT_NAME + " = '" + accountNotRub.name + "'", null);
        cv = new ContentValues();
        cv.put(CurrencyDatabaseHelper.ACCOUNT_NAME, accountNotRub.getName());
        cv.put(CurrencyDatabaseHelper.ACCOUNT_COUNT, accountNotRub.getValue());
        getContentResolver().insert(CurrencyContentProvider.ACCOUNT_CONTENT_URI, cv);
    }


}
