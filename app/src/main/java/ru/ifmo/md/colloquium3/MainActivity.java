package ru.ifmo.md.colloquium3;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private TextView tvGbp;
    private TextView tvUsd;
    private TextView tvEur;

    private Button btnWallet;

    private static final int LOADER_ID_CURRENCY = 1;

    private double gbp = 0;
    private double usd = 0;
    private double eur = 0;

    private int gbpCapacity = 0;
    private int usdCapacity = 0;
    private int eurCapacity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvGbp = (TextView) findViewById(R.id.tvGbp);
        tvUsd = (TextView) findViewById(R.id.tvUsd);
        tvEur = (TextView) findViewById(R.id.tvEur);

        btnWallet = (Button) findViewById(R.id.btnWallet);
        btnWallet.setOnClickListener(this);

        getLoaderManager().initLoader(LOADER_ID_CURRENCY, null, this);
        new Timer("tmrCurrencyUpdate", true).scheduleAtFixedRate(new TimerTask() {
            private int ticks = 10;
            Random rng = new Random();

            @Override
            public void run() {
                ticks--;
                double deltaGbp = (rng.nextDouble() - 0.5) * 0.1;
                double deltaUsd = (rng.nextDouble() - 0.5) * 0.1;
                double deltaEur = (rng.nextDouble() - 0.5) * 0.1;
                if (ticks == 0) {
                    ticks = 10;
                    deltaGbp = rng.nextDouble() > 0.5 ? 1 : -1;
                    deltaUsd = rng.nextDouble() > 0.5 ? 1 : -1;
                    deltaEur = rng.nextDouble() > 0.5 ? 1 : -1;
                }
                if (usd != 0) {
                    gbp += deltaGbp;
                    usd += deltaUsd;
                    eur += deltaEur;

                    getContentResolver().insert(CurrencyContentProvider.CURRENCY_URI,
                            DBAdapter.makeContentValues(gbp, gbpCapacity, usd, usdCapacity, eur, eurCapacity));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayCurrency();
                        }
                    });
                }
            }
        }, 1000, 1000);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CurrencyContentProvider.CURRENCY_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            gbp = data.getDouble(data.getColumnIndex(DBAdapter.KEY_CURRENCY_GBP));
            gbpCapacity = data.getInt(data.getColumnIndex(DBAdapter.KEY_CURRENCY_CAPACITY_GBP));
            usd = data.getDouble(data.getColumnIndex(DBAdapter.KEY_CURRENCY_USD));
            usdCapacity = data.getInt(data.getColumnIndex(DBAdapter.KEY_CURRENCY_CAPACITY_USD));
            eur = data.getDouble(data.getColumnIndex(DBAdapter.KEY_CURRENCY_EUR));
            eurCapacity = data.getInt(data.getColumnIndex(DBAdapter.KEY_CURRENCY_CAPACITY_EUR));
            displayCurrency();
            data.close();
        }
    }

    private void displayCurrency() {
        tvGbp.setText(String.format(getString(R.string.tvGbp_template),
                gbp,
                gbpCapacity));
        tvUsd.setText(String.format(getString(R.string.tvUsd_template),
                usd,
                usdCapacity));
        tvEur.setText(String.format(getString(R.string.tvEur_template),
                eur,
                eurCapacity));
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
    }
}
