package ru.ifmo.md.colloquium3;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    public static final String CURRENCY_NAME = "name";
    public static final String CURRENCY_VALUE = "value";

    private CurrencyAdapter adapter;
    private Button button;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.account_button);

        button.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.currency_list);
        adapter = new CurrencyAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getLoaderManager().restartLoader(2222, null, this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Currency currency = adapter.getItem(position);
        Intent intent = new Intent(this, ExchangeActivity.class);
        intent.putExtra(CURRENCY_NAME, currency.getName());
        intent.putExtra(CURRENCY_VALUE, currency.getValue());
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CurrencyContentProvider.CURRENCY_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            adapter.clear();
            while (data.moveToNext()) {
                Currency currency = CurrencyDatabaseHelper.CurrencyCursor.getCurrency(data);
                adapter.add(currency);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
}
