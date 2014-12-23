package ru.ifmo.mobdev.colloquium3;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import ru.ifmo.mobdev.colloquium3.db.CurrencyDatabase;
import ru.ifmo.mobdev.colloquium3.db.MyContentProvider;

/**
 * @author sugakandrey
 */
public class AccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        CurrencyAdapter adapter = new CurrencyAdapter(getItems(), this);
        ListView lv = (ListView) findViewById(R.id.account_list);
        lv.setAdapter(adapter);
    }

    public ArrayList<Currency> getItems() {
        Cursor cursor = getContentResolver().query(MyContentProvider.CONTENT_URI_CURRENCIES, new String[]{CurrencyDatabase.CURRENCY_AMOUNT,
                CurrencyDatabase.CURRENCY_NAME, CurrencyDatabase.CURRENCY_RATE}, null, null, null);
        ArrayList<Currency> items = new ArrayList<Currency>();
        cursor.moveToFirst();
        int total = 0;
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(CurrencyDatabase.CURRENCY_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(CurrencyDatabase.CURRENCY_AMOUNT));
            double rate = cursor.getDouble(cursor.getColumnIndex(CurrencyDatabase.CURRENCY_RATE));
            total += amount * rate;
            items.add(new Currency(name, amount));
            cursor.moveToNext();
        }
        items.add(new Currency("Total:", total));
        return items;
    }
}
