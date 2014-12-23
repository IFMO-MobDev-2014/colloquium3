package ru.ifmo.md.colloquium3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    ListView listView;
    AlarmManager manager;
    PendingIntent pendingIntent;
    public static final String APP_PREFERENCES = "my settings";
    public static final String APP_PREFERENCES_FIRST_LAUNCH = "first launch";

    public BroadcastReceiver load = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showCurrency();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mSettings;
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (!mSettings.contains(APP_PREFERENCES_FIRST_LAUNCH)) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_FIRST_LAUNCH, 1);
            editor.apply();
            String[] name = {"Dollar", "Euro", "Pound"};
            double[] cost = {60, 90, 120};
            for (int i = 0; i < 3; i++) {
                ContentValues k = new ContentValues();
                k.put(DBHelper.CHANNELS_COLUMN_NAME, name[i]);
                k.put(DBHelper.CHANNELS_COLUMN_COST, cost[i]);
                getContentResolver().insert(DBContentProvider.CURRENCY, k);
            }

        }
        listView = (ListView) findViewById(R.id.listView);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Receiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 100, pendingIntent);
    }

    public void showCurrency() {
        Cursor cursor = getContentResolver().query(DBContentProvider.CURRENCY, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<Item> items = new ArrayList<Item>();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.CHANNELS_COLUMN_NAME));
            double cost = cursor.getDouble(cursor.getColumnIndex(DBHelper.CHANNELS_COLUMN_COST));
            cursor.moveToNext();
            items.add(new Item(name, cost));
        }
        cursor.close();
        ItemAdapter adapter = new ItemAdapter(items, this);
        listView.setAdapter(adapter);
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
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Receiver.BROADCAST);
        this.registerReceiver(load, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(load);
        manager.cancel(pendingIntent);
    }

}
