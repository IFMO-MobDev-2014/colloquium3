package ru.ifmo.md.colloquium3;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private CurrencyCursorAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new CurrencyCursorAdapter(this, null);
        listView  = (ListView) findViewById(R.id.currency_list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Cursor c = adapter.getCursor();
                c.moveToPosition(pos);
                String currencyName = c.getString(c.getColumnIndexOrThrow(CurrencyContentProvider.CURRENCY_NAME));
                int currentPrice = c.getInt(c.getColumnIndexOrThrow(CurrencyContentProvider.CURRENT_PRICE));

                Intent intent = new Intent(getBaseContext(), ExchangeActivity.class);
                intent.putExtra("_id", id);
                intent.putExtra(CurrencyContentProvider.CURRENCY_NAME, currencyName);
                intent.putExtra(CurrencyContentProvider.CURRENT_PRICE, currentPrice);

                startActivity(intent);
            }
        });

        PendingIntent pi = PendingIntent.getService(this, 0, new Intent(this, UpdateCurrencyService.class).putExtra(UpdateCurrencyService.BIG_PERIOD, false), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 1000, pi);

        PendingIntent pi2 = PendingIntent.getService(this, 1, new Intent(this, UpdateCurrencyService.class).putExtra(UpdateCurrencyService.BIG_PERIOD, true), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am2 = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am2.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 1000*10, pi2);

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {"_id", "name", "current_price"};
        Uri uri = CurrencyContentProvider.CONTENT_URI_CURRENCIES;
        return new CursorLoader(this, uri, projection, "_id > 0", null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }
}
