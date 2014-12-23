package ru.ifmo.md.colloquium3;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ListActivity {
    SharedPreferences prefs = null;
    ArrayAdapter<Value> adapter;
    ArrayList<Value> values;
    DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        values = new ArrayList<Value>();
        adapter = new MyArrayAdapter(getApplicationContext(), values);

        mDBHelper = new DBHelper(this);

        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        boolean flag = settings.getBoolean("firstrun", true);

        if (flag) {
            Log.i("SMTH", "!!!!!!");
            ContentValues USDValues = new ContentValues();
            ContentValues EURValues = new ContentValues();
            ContentValues GBPValues = new ContentValues();

            USDValues.put(DBHelper.COLUMN_NAME_NAME, "USD");
            USDValues.put(DBHelper.COLUMN_NAME_COUNT, 54.0);
            values.add(new Value("USD", 54.0));

            EURValues.put(DBHelper.COLUMN_NAME_NAME, "EUR");
            EURValues.put(DBHelper.COLUMN_NAME_COUNT, 65.0);
            values.add(new Value("EUR", 65.0));

            GBPValues.put(DBHelper.COLUMN_NAME_NAME, "GBP");
            GBPValues.put(DBHelper.COLUMN_NAME_COUNT, 75.0);
            values.add(new Value("GBP", 75.0));

            adapter.notifyDataSetChanged();

            getContentResolver().insert(MyContentProvider.COURSE_CONTENT_URL, USDValues);
            getContentResolver().insert(MyContentProvider.COURSE_CONTENT_URL, EURValues);
            getContentResolver().insert(MyContentProvider.COURSE_CONTENT_URL, GBPValues);

            //Service
            ScheduledExecutorService scheduleTaskExecutor1 = Executors.newScheduledThreadPool(5);

            scheduleTaskExecutor1.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    double rand = Math.random() * 2 - 1;
                    double usd = 0;
                    double eur = 0;
                    double gbp = 0;

                    Cursor c = db.query("cources", null, null, null, null, null, null);
                    if (c.moveToFirst()) {
                        int idColIndex = c.getColumnIndex("id");
                        int nameColIndex = c.getColumnIndex("name");
                        int countColIndex = c.getColumnIndex("count");

                        usd = Double.parseDouble(c.getString(countColIndex));
                        c.moveToNext();
                        eur = Double.parseDouble(c.getString(countColIndex));
                        c.moveToNext();
                        gbp = Double.parseDouble(c.getString(countColIndex));
                        c.moveToNext();
                    }
                    c.close();

                    ContentValues USDValues = new ContentValues();
                    USDValues.put(DBHelper.COLUMN_NAME_COUNT, String.valueOf(usd + rand));
                    getContentResolver().update(MyContentProvider.COURSE_CONTENT_URL, USDValues, "name = ?", new String[] { "USD" });
                    values.set(0, new Value("USD", usd + rand));

                    ContentValues EURValues = new ContentValues();
                    USDValues.put(DBHelper.COLUMN_NAME_COUNT, String.valueOf(eur + rand));
                    getContentResolver().update(MyContentProvider.COURSE_CONTENT_URL, USDValues, "name = ?", new String[] { "EUR" });
                    values.set(1, new Value("EUR", eur + rand));

                    ContentValues GBPValues = new ContentValues();
                    USDValues.put(DBHelper.COLUMN_NAME_COUNT, String.valueOf(gbp + rand));
                    getContentResolver().update(MyContentProvider.COURSE_CONTENT_URL, USDValues, "name = ?", new String[] { "GBP" });
                    values.set(2, new Value("GBP", gbp + rand));

                    adapter.notifyDataSetChanged();
                }
            }, 0, 1,TimeUnit.SECONDS);

            ScheduledExecutorService scheduleTaskExecutor10 = Executors.newScheduledThreadPool(5);

            scheduleTaskExecutor10.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    double rand = 0;
                    if (Math.random() > 0.5) {
                        rand = 1;
                    } else {
                        rand = -1;
                    }
                    double usd = 0;
                    double eur = 0;
                    double gbp = 0;

                    Cursor c = db.query("cources", null, null, null, null, null, null);
                    if (c.moveToFirst()) {
                        int idColIndex = c.getColumnIndex("id");
                        int nameColIndex = c.getColumnIndex("name");
                        int countColIndex = c.getColumnIndex("count");

                        usd = Double.parseDouble(c.getString(countColIndex));
                        c.moveToNext();
                        eur = Double.parseDouble(c.getString(countColIndex));
                        c.moveToNext();
                        gbp = Double.parseDouble(c.getString(countColIndex));
                        c.moveToNext();
                    }
                    c.close();

                    ContentValues USDValues = new ContentValues();
                    USDValues.put(DBHelper.COLUMN_NAME_COUNT, String.valueOf(usd + rand));
                    getContentResolver().update(MyContentProvider.COURSE_CONTENT_URL, USDValues, "name = ?", new String[] { "USD" });
                    values.set(0, new Value("USD", usd + rand));

                    ContentValues EURValues = new ContentValues();
                    USDValues.put(DBHelper.COLUMN_NAME_COUNT, String.valueOf(eur + rand));
                    getContentResolver().update(MyContentProvider.COURSE_CONTENT_URL, USDValues, "name = ?", new String[] { "EUR" });
                    values.set(1, new Value("EUR", eur + rand));

                    ContentValues GBPValues = new ContentValues();
                    USDValues.put(DBHelper.COLUMN_NAME_COUNT, String.valueOf(gbp + rand));
                    getContentResolver().update(MyContentProvider.COURSE_CONTENT_URL, USDValues, "name = ?", new String[] { "GBP" });
                    values.set(2, new Value("GBP", gbp + rand));
                }
            }, 0, 10, TimeUnit.SECONDS);
            //settings.edit().putBoolean("firstrun", false).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
