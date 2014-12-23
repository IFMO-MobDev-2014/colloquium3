package ru.ifmo.colloquium3;

import android.app.ActivityManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import ru.ifmo.colloquium3.adapter.WalletsCursorAdapter;
import ru.ifmo.colloquium3.db.DatabaseHelper;
import ru.ifmo.colloquium3.db.MyContentProvider;


public class MainActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView walletsList;
    private WalletsCursorAdapter walletsAdapter;
    private String[] projection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isMyServiceRunning(CourseServer.class)) {
            Intent intent = new Intent(this, CourseServer.class);
            startService(intent);
        }

        getLoaderManager().initLoader(0, null, this);

        walletsList = (ListView) findViewById(R.id.wallets_list);

        projection = new String[] {
                DatabaseHelper.ID_KEY,
                DatabaseHelper.WALLET_NAME_KEY,
                DatabaseHelper.WALLET_VALUE_KEY
        };
        Cursor cursor = getContentResolver().query(
                MyContentProvider.WALLETS_URI,
                projection, null, null, null);
        walletsAdapter = new WalletsCursorAdapter(this, cursor);
        walletsList.setAdapter(walletsAdapter);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MyContentProvider.WALLETS_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("LoadFinished", "");
        walletsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        walletsAdapter.swapCursor(null);
    }
}
