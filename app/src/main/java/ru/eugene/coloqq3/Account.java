package ru.eugene.coloqq3;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.HashMap;

import ru.eugene.coloqq3.db.BlackProvider;
import ru.eugene.coloqq3.db.CountDataSource;
import ru.eugene.coloqq3.db.MoneyDataSource;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Account extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter adapter;
    TextView totalView;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Cursor count = adapter.getCursor();
            Cursor course = getContentResolver().query(BlackProvider.CONTENT_URI_MONEY, MoneyDataSource.getProjection(),
                    null, null, null);
            double total = 0;
            HashMap<String, Double> hash = new HashMap<>();
            hash.put(Shop.interesting_name, 1.0);
            if (course.moveToFirst()) {
                do {
                   hash.put(course.getString(course.getColumnIndex(MoneyDataSource.COLUMN_NAME)),
                           course.getDouble(course.getColumnIndex(MoneyDataSource.COLUMN_COURSE)));
                } while (course.moveToNext());
            }
            if (count.moveToFirst()) {
                do {
                    total += count.getDouble(count.getColumnIndex(CountDataSource.COLUMN_COUNT)) *
                            hash.get(count.getString(count.getColumnIndex(CountDataSource.COLUMN_NAME)));
                } while (count.moveToNext());
                total = ((int) (total * 100)) / 100.0;
                totalView.setText(Double.toString(total));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        totalView = (TextView) findViewById(R.id.total);

        String[] from = new String[]{CountDataSource.COLUMN_NAME, CountDataSource.COLUMN_COUNT};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, R.layout.item_list_view, null, from, to, 0);

        getListView().setAdapter(adapter);

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BlackProvider.CONTENT_URI_COUNT, CountDataSource.getProjection(), null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(CourseService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
