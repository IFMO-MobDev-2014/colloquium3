package ru.eugene.coloqq3;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.eugene.coloqq3.db.BlackProvider;
import ru.eugene.coloqq3.db.CountDataSource;
import ru.eugene.coloqq3.db.MoneyDataSource;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Shop extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    Cursor cursor;
    String currentName;
    public static final String interesting_name = "RUB";
    Context context;
    TextView curCourse;
    TextView count;
    TextView countRub;
    Button buy;
    Button sell;
    double currentCourse;
    double currentCount;
    double currentCountRub;

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            Cursor cursor = context.getContentResolver().query(BlackProvider.CONTENT_URI_MONEY, MoneyDataSource.getProjection(),
                    MoneyDataSource.COLUMN_NAME + "=?", new String[]{currentName}, null);

            cursor.moveToFirst();
            currentCourse = cursor.getDouble(cursor.getColumnIndex(MoneyDataSource.COLUMN_COURSE));
            currentCourse = ((int) (currentCourse * 100)) / 100.0;
            curCourse.setText(Double.toString(currentCourse));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        currentName = getIntent().getStringExtra(CountDataSource.COLUMN_NAME);
        ((TextView) findViewById(R.id.name)).setText(currentName + ": ");
        context = this;

        curCourse = ((TextView) findViewById(R.id.cur_course));
        count = ((TextView) findViewById(R.id.count));
        countRub = ((TextView) findViewById(R.id.count_rub));

        buy = (Button) findViewById(R.id.buy);
        sell = (Button) findViewById(R.id.sell);

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCount = currentCount - 1.0 / currentCourse;
                if (currentCount < 0) {
                    Toast.makeText(context, "too few money =(", Toast.LENGTH_SHORT).show();
                    currentCount = currentCount + 1.0 / currentCourse;
                    return;
                }
                ContentValues values = new ContentValues();
                values.put(CountDataSource.COLUMN_COUNT, currentCount);
                getContentResolver().update(BlackProvider.CONTENT_URI_COUNT, values, CountDataSource.COLUMN_NAME + "=?",
                        new String[] {currentName});

                values.clear();
                currentCountRub++;
                values.put(CountDataSource.COLUMN_COUNT, currentCountRub);
                getContentResolver().update(BlackProvider.CONTENT_URI_COUNT, values, CountDataSource.COLUMN_NAME + "=?",
                        new String[] {interesting_name});

                values.clear();

            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCountRub = currentCountRub - currentCourse;
                if (currentCountRub < 0) {
                    Toast.makeText(context, "too few money =(", Toast.LENGTH_SHORT).show();
                    currentCountRub = currentCountRub + currentCourse;
                    return;
                }
                ContentValues values = new ContentValues();
                values.put(CountDataSource.COLUMN_COUNT, currentCountRub);
                getContentResolver().update(BlackProvider.CONTENT_URI_COUNT, values, CountDataSource.COLUMN_NAME + "=?",
                        new String[] {interesting_name});

                values.clear();
                currentCount++;
                values.put(CountDataSource.COLUMN_COUNT, currentCount);
                getContentResolver().update(BlackProvider.CONTENT_URI_COUNT, values, CountDataSource.COLUMN_NAME + "=?",
                        new String[] {currentName});

            }
        });

        getLoaderManager().initLoader(1, null, this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BlackProvider.CONTENT_URI_COUNT, CountDataSource.getProjection(), null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        if (cursor.moveToFirst()) {
            do {
                String t = cursor.getString(cursor.getColumnIndex(CountDataSource.COLUMN_NAME));
                double cur_count = cursor.getDouble(cursor.getColumnIndex(CountDataSource.COLUMN_COUNT));
                cur_count = ((int) (cur_count * 100)) / 100.0;
                if (t.equals(currentName)) {
                    currentCount = cur_count;
                    count.setText(Double.toString(cur_count));
                } else if (t.equals(interesting_name)) {
                    currentCountRub = cur_count;
                    countRub.setText(Double.toString(cur_count));
                }
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursor = null;
    }
}
