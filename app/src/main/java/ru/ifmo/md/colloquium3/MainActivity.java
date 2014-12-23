package ru.ifmo.md.colloquium3;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private static final int LOADER_ID = 1;
    private Intent intent;
    private ListView listView;
    private SimpleCursorAdapter simpleCursorAdapter;
    private static final String[] from_feed = new String[]{DBMoney.CURRENCY1, DBMoney.RATE1};
    private static final int[] to_feed = new int[]{R.id.currency_name, R.id.currency_rate};
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        intent = new Intent(this, ExchangeActivity.class);

        listView = (ListView) findViewById(R.id.list_currency);


        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.currency_item_row, null, from_feed, to_feed, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listView.setAdapter(simpleCursorAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(MoneyUpdateIntent.ACTION_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReceiver, intentFilter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor = simpleCursorAdapter.getCursor();
                cursor.moveToPosition(i);

                intent.putExtra(ExchangeActivity.INTENT_CURRENCY_ID, cursor.getLong(cursor.getColumnIndexOrThrow(DBMoney.ID1)));
                startActivity(intent);
            }
        });
        AlarmManagerHelper.stopAlarm(getApplicationContext());
        AlarmManagerHelper.startAlarm(getApplicationContext(), 1 * 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    private void display()
    {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("debug1", "onRecive!");
            display();
        }
    }


    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new MoneyCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
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
}
