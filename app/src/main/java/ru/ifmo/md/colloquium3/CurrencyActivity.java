package ru.ifmo.md.colloquium3;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class CurrencyActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter cursorAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(android.R.id.list);

        String[] columns = new String[]{"curr", "rate"};
        int[] elements = new int[]{R.id.curr_currency_list_elem, R.id.rate_currency_list_elem};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.currency_list_elem, null, columns, elements, 0);
        listView.setAdapter(cursorAdapter);
        Log.i("title2", "azaza");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(CurrencyActivity.this, StoreActivity.class);
                String curr = ((Cursor) listView.getAdapter().getItem(i)).getString(1);
                intent.putExtra("curr", curr);
                startActivity(intent);
            }
        });

        cursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                return false;
            }
        });

        getLoaderManager().initLoader(0, null, this);

        startService(new Intent(this, RateService.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i("title3", "azaza");

        String[] columns = new String[]{"_id", "curr", "rate"};
        return new CursorLoader(this, RateContentProvider.RATES_CONTENT_URI, columns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }
}
