package ru.ifmo.md.colloquium3;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.ifmo.md.colloquium3.database.CurrencyContract;


public class BuySellActivity extends ActionBarActivity {

    public static final String EXTRA_CURRENCY_ID = "cid";

    private double cnt;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell);

        String id = Long.toString(getIntent().getLongExtra(EXTRA_CURRENCY_ID, 1));

        Cursor cursor = getContentResolver().query(CurrencyContract.Currency.buildCurrencyUri(id), null, null, null, null);
        cursor.moveToFirst();

        name = cursor.getString(cursor.getColumnIndex(CurrencyContract.Currency.CURRENCY_NAME));
        cnt = cursor.getDouble(cursor.getColumnIndex(CurrencyContract.Currency.CURRENCY_CNT));
        final double rub = CurrencyService.getRoubles(this);

        ((TextView)findViewById(R.id.currency)).setText(name + ": " + String.format("%.2f", cnt));
        ((TextView)findViewById(R.id.yourMoney)).setText("You have " + String.format("%.2f", rub) + " rub");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buy_sell, menu);
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

    public void buy(View view) {
        double rub = CurrencyService.getRoubles(this);
        if (rub < cnt) {
            Toast.makeText(this, "Not enough money to buy", Toast.LENGTH_SHORT).show();
        } else {
            rub -= cnt;
        }
        CurrencyService.setRoubles(this, (float)rub);
        ((TextView)findViewById(R.id.yourMoney)).setText("You have " + String.format("%.2f", rub) + " rub");
    }

    public void sell(View view) {
        double rub = CurrencyService.getRoubles(this);
        rub += cnt;
        CurrencyService.setRoubles(this, (float)rub);
        ((TextView)findViewById(R.id.yourMoney)).setText("You have " + String.format("%.2f", rub) + " rub");
    }
}
