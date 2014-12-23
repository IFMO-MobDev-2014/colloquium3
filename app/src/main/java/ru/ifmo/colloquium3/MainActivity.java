package ru.ifmo.colloquium3;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import ru.ifmo.colloquium3.adapter.WalletsCursorAdapter;
import ru.ifmo.colloquium3.db.DatabaseHelper;
import ru.ifmo.colloquium3.db.MyContentProvider;


public class MainActivity extends ActionBarActivity {
    private ListView walletsList;
    private WalletsCursorAdapter walletsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        walletsList = (ListView) findViewById(R.id.wallets_list);

        String[] projection = {
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
