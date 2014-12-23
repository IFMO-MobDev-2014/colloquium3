package ru.ifmo.md.colloquium3;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ru.ifmo.md.colloquium3.database.CurrencyContract;


public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_CURRENCIES = 1;

    private CursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCursorAdapter = new CursorAdapter(this, null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, viewGroup, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                final String name = cursor.getString(cursor.getColumnIndex(CurrencyContract.Currency.CURRENCY_NAME));
                ((TextView) view.findViewById(android.R.id.text1)).setText(name);
                final double cnt = cursor.getDouble(cursor.getColumnIndex(CurrencyContract.Currency.CURRENCY_CNT));
                ((TextView) view.findViewById(android.R.id.text2)).setText(String.format("%.2f", cnt));
            }
        };

        setListAdapter(mCursorAdapter);

        getLoaderManager().initLoader(LOADER_CURRENCIES, Bundle.EMPTY, this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case LOADER_CURRENCIES:
                return new CursorLoader(this, CurrencyContract.Currency.CONTENT_URI, null, null, null, null);
            default:
                throw new UnsupportedOperationException("No such loader");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
