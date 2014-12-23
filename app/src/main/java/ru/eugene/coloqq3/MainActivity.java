package ru.eugene.coloqq3;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.eugene.coloqq3.db.BlackProvider;
import ru.eugene.coloqq3.db.CountDataSource;
import ru.eugene.coloqq3.db.MoneyDataSource;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] from = new String[] {MoneyDataSource.COLUMN_NAME, MoneyDataSource.COLUMN_COURSE};
        int[] to = new int[] {android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, R.layout.item_list_view, null, from, to, 0);
        getListView().setAdapter(adapter);

        startService(new Intent(this, CourseService.class));
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent shop = new Intent(this, Shop.class);
        String name = ((TextView) v.findViewById(android.R.id.text1)).getText().toString();
        shop.putExtra(MoneyDataSource.COLUMN_NAME, name);
        startActivity(shop);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BlackProvider.CONTENT_URI_MONEY, MoneyDataSource.getProjection(), null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
//        Log.i("LOG", "first: " + data.getString(data.getColumnIndex(MoneyDataSource.COLUMN_NAME)) + "");
        if (data.getCount() == 0) {
            insert_money("USD", 54);
            insert_money("EUR", 65);
            insert_money("GBP", 75);

            insert_count("USD", 0);
            insert_count("EUR", 0);
            insert_count("GBP", 0);
            insert_count("RUB", 10000);
        }
    }

    private void insert_count(String name, int count) {
        ContentValues values = new ContentValues();
        values.put(CountDataSource.COLUMN_NAME, name);
        values.put(CountDataSource.COLUMN_COUNT, count);
        getContentResolver().insert(BlackProvider.CONTENT_URI_COUNT, values);
    }

    private void insert_money(String name, int course) {
        ContentValues values = new ContentValues();
        values.put(MoneyDataSource.COLUMN_NAME, name);
        values.put(MoneyDataSource.COLUMN_COURSE, course);
        getContentResolver().insert(BlackProvider.CONTENT_URI_MONEY, values);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
