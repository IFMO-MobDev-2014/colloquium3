package ru.ifmo.mobdev.colloquium3;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.*;

import ru.ifmo.mobdev.colloquium3.db.CurrencyDatabase;
import ru.ifmo.mobdev.colloquium3.db.MyContentProvider;


public class MainActivity extends Activity {

    public static final String CURRENCY = "ru.ifmo.mobdev.colloquium3.currencies";
    private ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cr = getContentResolver();
        ListView list = (ListView) findViewById(R.id.currencies_list);
        ArrayList<Currency> items = getItems();
        final CurrencyAdapter adapter = new CurrencyAdapter(items, this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ExchangeActivity.class);
                intent.putExtra(CURRENCY, ((Currency) adapter.getItem(position)).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, AccountActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Currency> getItems() {
        Cursor cursor = cr.query(MyContentProvider.CONTENT_URI_CURRENCIES, new String[]{CurrencyDatabase.CURRENCY_AMOUNT,
                CurrencyDatabase.CURRENCY_NAME, CurrencyDatabase.CURRENCY_RATE}, null, null, null);
        ArrayList<Currency> items = new ArrayList<Currency>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(CurrencyDatabase.CURRENCY_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(CurrencyDatabase.CURRENCY_AMOUNT));
            double rate = cursor.getDouble(cursor.getColumnIndex(CurrencyDatabase.CURRENCY_RATE));
            if (!name.equals("RUB"))
                items.add(new Currency(rate, name));
            cursor.moveToNext();
        }
        return items;
    }
}
