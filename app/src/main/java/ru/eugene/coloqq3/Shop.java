package ru.eugene.coloqq3;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ru.eugene.coloqq3.db.BlackProvider;
import ru.eugene.coloqq3.db.CountDataSource;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Shop extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    Cursor cursor;
    String cur_name;
    public static final String interesting_name = "RUB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        cur_name = getIntent().getStringExtra(CountDataSource.COLUMN_NAME);
        ((TextView) findViewById(R.id.name)).setText(cur_name + ": ");

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BlackProvider.CONTENT_URI_COUNT, CountDataSource.getProjection(), null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        Log.i("LOG", cur_name);
        Log.i("LOG", interesting_name);
        if (cursor.moveToFirst()) {
            do {
                String t = cursor.getString(cursor.getColumnIndex(CountDataSource.COLUMN_NAME));
                Log.i("LOG", t);
                String cur_count = cursor.getString(cursor.getColumnIndex(CountDataSource.COLUMN_COUNT));
                if (t.equals(cur_name)) {
                    ((TextView) findViewById(R.id.count)).setText(cur_count);
                } else if (t.equals(interesting_name)) {
                    ((TextView) findViewById(R.id.count_rub)).setText(cur_count);
                }
            } while (cursor.moveToNext());
        }
        Log.i("LOG", "finish Shop");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursor = null;
    }
}
