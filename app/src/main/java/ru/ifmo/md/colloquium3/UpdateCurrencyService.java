package ru.ifmo.md.colloquium3;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

public class UpdateCurrencyService extends IntentService {
    private Random rand;
    public static final String BIG_PERIOD = "big_period";

    public UpdateCurrencyService() {
        super("UpdateCurrencyService");
        rand = new Random();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean bigPeriod = intent.getBooleanExtra(BIG_PERIOD, false);

        Cursor c = getContentResolver().query(CurrencyContentProvider.CONTENT_URI_CURRENCIES, null, null, null, null);
        int cur = 0;
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndexOrThrow("_id"));
            cur = c.getInt(c.getColumnIndexOrThrow(CurrencyContentProvider.CURRENT_PRICE));
            if (bigPeriod) {
                cur += rand.nextBoolean() ? -1 : 1;
            } else {
                cur += rand.nextInt(200 + 1) - 100;
            }
            ContentValues values = new ContentValues();
            Uri uri = Uri.withAppendedPath(CurrencyContentProvider.CONTENT_URI_CURRENCIES, "" + id);
            values.put(CurrencyContentProvider.CURRENT_PRICE, cur);
            getContentResolver().update(uri, values, null, null);
        }

        Intent i = new Intent(ExchangeActivity.RECEIVER_ACTION);
        i.putExtra(CurrencyContentProvider.CURRENT_PRICE, cur);
        Log.i("", "sendBroadcast");
        sendBroadcast(i);

        c.close();
    }
}
