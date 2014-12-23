package ru.ifmo.colloquium3;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.impl.client.DefaultProxyAuthenticationHandler;

import ru.ifmo.colloquium3.db.DatabaseHelper;
import ru.ifmo.colloquium3.db.MyContentProvider;


public class WalletExchangeActivity extends ActionBarActivity {
    public static final String WALLET_NAME_EXTRA = "ru.ifmo.WALLET_NAME_EXTRA";

    private String walletName;
    private TextView walletAmount;
    private TextView walletValue;
    private TextView rublesAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_exchange);

        walletAmount = (TextView) findViewById(R.id.current_wallet_amount);
        walletValue = (TextView) findViewById(R.id.current_wallet_course);
        rublesAmount = (TextView) findViewById(R.id.rubles_amount);

        walletName = getIntent().getStringExtra(WALLET_NAME_EXTRA);
        getLoaderManager().initLoader(1, null, new WalletCursorLoader(this, walletName) {
            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data.moveToFirst()) {
                    double value = data.getDouble(data.getColumnIndex(DatabaseHelper.WALLET_VALUE_KEY));
                    walletValue.setText(String.format("%.2f", value));
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                walletValue.setText("Fetching");
            }
        });

        ((TextView) findViewById(R.id.current_wallet_name)).setText(walletName);

        getLoaderManager().initLoader(2, null, new MoneyCursorLoader(this) {
            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Log.d("FetchValue", data.getCount() + "");
                if (data.moveToFirst()) {
                    do {
                        double currentWalletAmount = data.getDouble(data.getColumnIndex(DatabaseHelper.WALLET_AMOUNT_KEY));
                        String name = data.getString(data.getColumnIndex(DatabaseHelper.WALLET_ID_KEY));
                        Log.d("FetchValue", name + " " + currentWalletAmount);
                        if (name.equals("RUB")) {
                            setRubles(currentWalletAmount);
                        } else if (name.equals(walletName)) {
                            setCurrent(currentWalletAmount);
                        }
                    } while (data.moveToNext());
                }
            }

            private void setCurrent(double value) {
                walletAmount.setText(String.format("%.2f", value));
            }

            private void setRubles(double value) {
                rublesAmount.setText(String.format("%.2f", value));
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                walletAmount.setText("Fetching");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wallet_exchange, menu);
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

    public void onBuyClick(View view) {
        changeAmount(1);
    }

    private void changeAmount(int delta) {
        String[] projection = {
                DatabaseHelper.ID_KEY,
                DatabaseHelper.WALLET_ID_KEY,
                DatabaseHelper.WALLET_AMOUNT_KEY
        };
        String selection = DatabaseHelper.WALLET_ID_KEY + " = ?";
        String[] selectionArgsCurrent = {
            walletName
        };
        String[] selectionArgsRubles = {
            "RUB"
        };
        String[] projectionCost = { DatabaseHelper.WALLET_VALUE_KEY };
        Cursor cost = getContentResolver().query(MyContentProvider.WALLETS_URI, projectionCost, null, null, null);
        cost.moveToFirst();
        double currentCost = cost.getDouble(cost.getColumnIndex(DatabaseHelper.WALLET_VALUE_KEY));
        Cursor cursorCurrent = getContentResolver().query(MyContentProvider.MONEY_URI, projection, selection, selectionArgsCurrent, null);
        Cursor cursorRubles = getContentResolver().query(MyContentProvider.MONEY_URI, projection, selection, selectionArgsRubles, null);
        cursorCurrent.moveToFirst();
        cursorRubles.moveToFirst();

        ContentValues values = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursorCurrent, values);
        values.put(DatabaseHelper.WALLET_AMOUNT_KEY,
                values.getAsDouble(DatabaseHelper.WALLET_AMOUNT_KEY) + delta);
        getContentResolver().update(MyContentProvider.MONEY_URI, values, DatabaseHelper.ID_KEY + " = ?",
                new String[] { values.getAsString(DatabaseHelper.ID_KEY)});
        values.clear();
        DatabaseUtils.cursorRowToContentValues(cursorRubles, values);
        values.put(DatabaseHelper.WALLET_AMOUNT_KEY,
                values.getAsDouble(DatabaseHelper.WALLET_AMOUNT_KEY) - delta * currentCost);
        getContentResolver().update(MyContentProvider.MONEY_URI, values, DatabaseHelper.ID_KEY + " = ?",
                new String[] { values.getAsString(DatabaseHelper.ID_KEY)});
    }

    public void onSellClick(View view) {
        changeAmount(-1);
    }

    private static abstract class WalletCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {
        private final Context context;
        private final String walletName;

        protected WalletCursorLoader(Context context, String walletName) {
            this.context = context;
            this.walletName = walletName;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] projection = {
                    DatabaseHelper.WALLET_NAME_KEY,
                    DatabaseHelper.WALLET_VALUE_KEY
            };
            String selection = DatabaseHelper.WALLET_NAME_KEY + " = ?";
            String[] selectionArgs = {
                walletName
            };
            return new CursorLoader(context, MyContentProvider.WALLETS_URI, projection,
                    selection, selectionArgs, null);
        }
    }

    private static abstract class MoneyCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {
        private final Context context;

        protected MoneyCursorLoader(Context context) {
            this.context = context;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] projection = {
                    DatabaseHelper.WALLET_ID_KEY,
                    DatabaseHelper.WALLET_AMOUNT_KEY
            };
            return new CursorLoader(context, MyContentProvider.MONEY_URI, projection,
                    null, null, null);
        }
    }
}
