package ru.ifmo.md.colloquium3;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ExchangeActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private long currencyId;
    private String currencyName;
    private int currencyPrice;
    private TextView rubTextView;
    private TextView foreignTextView;
    public TextView currentPriceTextView;
    private BroadcastReceiver receiver;
    private int foreignAmount;
    private int rubAmount;
    public static final String RECEIVER_ACTION = "exchange_receiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        currentPriceTextView = (TextView) findViewById(R.id.current_price_text);

        currencyId = getIntent().getLongExtra("_id", 0);
        currencyName = getIntent().getStringExtra(CurrencyContentProvider.CURRENCY_NAME);
        currencyPrice = getIntent().getIntExtra(CurrencyContentProvider.CURRENT_PRICE, 0);
        int currencyAmount = getIntent().getIntExtra(CurrencyContentProvider.AMOUNT, 0);

        rubTextView = (TextView) findViewById(R.id.text_ammount_rub);
        foreignTextView = (TextView) findViewById(R.id.text_ammount_foreign);

        currentPriceTextView.setText(currencyPrice / 100 + "." + currencyPrice%100);
        foreignTextView.setText(currencyName + ": " + currencyAmount);

        IntentFilter filter = new IntentFilter();
        filter.addAction(RECEIVER_ACTION);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                currencyPrice = intent.getIntExtra(CurrencyContentProvider.CURRENT_PRICE, 0);
                currentPriceTextView.setText(currencyPrice / 100 + "." + currencyPrice%100);
            }
        };
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {"_id", "name", "current_price"};
        Uri uri = CurrencyContentProvider.CONTENT_URI_CURRENCIES;
        return new CursorLoader(this, uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursor.moveToFirst();
        rubTextView.setText("RUB: " + cursor.getInt(cursor.getColumnIndexOrThrow(CurrencyContentProvider.AMOUNT)));
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public void onBuylick(View view) {
        int foreignChange = 1;
        int rubChange = -currencyPrice;

        Intent intent = new Intent(this, UpdateWalletService.class);
        intent.putExtra(UpdateWalletService.INTENT_CURRENCY_ID, currencyId);
        intent.putExtra(UpdateWalletService.INTENT_RUB_CHANGE, rubChange);
        intent.putExtra(UpdateWalletService.INTENT_FOREIGN_CHANGE, foreignChange);
        startService(intent);
    }
    public void onSellClick(View view) {
        int foreignChange = -1;
        int rubChange = currencyPrice;

        Intent intent = new Intent(this, UpdateWalletService.class);
        intent.putExtra(UpdateWalletService.INTENT_CURRENCY_ID, currencyId);
        intent.putExtra(UpdateWalletService.INTENT_RUB_CHANGE, rubChange);
        intent.putExtra(UpdateWalletService.INTENT_FOREIGN_CHANGE, foreignChange);
        startService(intent);
    }
}
