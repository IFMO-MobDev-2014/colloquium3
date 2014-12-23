package ru.ifmo.md.colloquium3;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.util.Random;

public class CurIntentService extends IntentService {

    public CurIntentService() {
        super("RSSPullService");
    }

    static final String[] SUMMARY_PROJECTION = new String[] {
            Currency.SimpleCurrency._ID,
            Currency.SimpleCurrency.CUR_NAME,
            Currency.SimpleCurrency.RATE_NAME
    };

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Random r = new Random();
            int va = 0;
            while (true) {
                va = (va + 1) % 10;
                Thread.sleep(1000);
                ContentValues cv = new ContentValues();
                cv.clear();

                cv.put(Currency.SimpleCurrency.CUR_NAME, "usd");
                Cursor cursor = getContentResolver().query(Currency.SimpleCurrency.CONTENT_URI,
                        SUMMARY_PROJECTION, Currency.SimpleCurrency.CUR_NAME + " = \"usd\" ", null, null);
                cursor.moveToFirst();
                int rate =  cursor.getInt(cursor.getColumnIndex(Currency.SimpleCurrency.RATE_NAME));
                cv.put(Currency.SimpleCurrency.RATE_NAME, rate + r.nextInt() % 10);
                getContentResolver().update(Currency.SimpleCurrency.CONTENT_URI, cv,
                        Currency.SimpleCurrency.CUR_NAME + " = \"usd\" ", null);

                cv.clear();
                cv.put(Currency.SimpleCurrency.CUR_NAME, "eur");
                cursor = getContentResolver().query(Currency.SimpleCurrency.CONTENT_URI,
                        SUMMARY_PROJECTION, Currency.SimpleCurrency.CUR_NAME + " = \"eur\" ", null, null);
                cursor.moveToFirst();
                rate =  cursor.getInt(cursor.getColumnIndex(Currency.SimpleCurrency.RATE_NAME));
                cv.put(Currency.SimpleCurrency.RATE_NAME, rate + r.nextInt() % 10);
                getContentResolver().update(Currency.SimpleCurrency.CONTENT_URI, cv,
                        Currency.SimpleCurrency.CUR_NAME + " = \"eur\" ", null);

                cv.clear();
                cv.put(Currency.SimpleCurrency.CUR_NAME, "gpb");
                cursor = getContentResolver().query(Currency.SimpleCurrency.CONTENT_URI,
                        SUMMARY_PROJECTION, Currency.SimpleCurrency.CUR_NAME + " = \"gpb\" ", null, null);
                cursor.moveToFirst();
                rate =  cursor.getInt(cursor.getColumnIndex(Currency.SimpleCurrency.RATE_NAME));
                cv.put(Currency.SimpleCurrency.RATE_NAME, rate + r.nextInt() % 10);
                getContentResolver().update(Currency.SimpleCurrency.CONTENT_URI, cv,
                        Currency.SimpleCurrency.CUR_NAME + " = \"gpb\" ", null);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
