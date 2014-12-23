package ru.ifmo.md.colloquium3;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class UpdateWalletService extends IntentService {
    public static final String INTENT_CURRENCY_ID = "currency_id";
    public static final String INTENT_FOREIGN_CHANGE = "foreign_change";
    public static final String INTENT_RUB_CHANGE = "rub_change";

    public UpdateWalletService() {
        super("UpdateWalletService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int currencyId = intent.getIntExtra(INTENT_CURRENCY_ID, 0);
        int foreignChange = intent.getIntExtra(INTENT_FOREIGN_CHANGE, 0);
        int rubChange = intent.getIntExtra(INTENT_RUB_CHANGE, 0);

        changeAmount(currencyId, foreignChange);
        changeAmount(0, rubChange);
    }

    private void changeAmount(int currencyId, int change) {
        Uri uri = Uri.withAppendedPath(CurrencyContentProvider.CONTENT_URI_CURRENCIES, "" + currencyId);
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        int cur = c.getInt(c.getColumnIndexOrThrow(CurrencyContentProvider.CURRENT_PRICE));
        cur += change;
        ContentValues values = new ContentValues();
        values.put(CurrencyContentProvider.AMOUNT, cur);
        getContentResolver().update(uri, values, null, null);
        c.close();
    }
}
