package ru.ifmo.md.colloquium3;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class RateService extends IntentService {
    public RateService() {
        super("RateService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            Cursor cursor = null;
            int ticks = 0;

            try {
                TimeUnit.SECONDS.sleep(1);
                ticks++;

                cursor = getContentResolver().query(RateContentProvider.RATES_CONTENT_URI, null, null, null, null);
                cursor.moveToFirst();

                do {
                    String curr = cursor.getString(1);
                    double rate = cursor.getDouble(2);

                    double newRate = rate + Math.random() * 2 - 1;

                    if (ticks % 10 == 0) {
                        newRate += Math.random() < 0.5 ? -1 : +1;
                    }

                    ContentValues values = new ContentValues();
                    values.put("curr", curr);
                    values.put("rate", newRate);
                    getContentResolver().update(RateContentProvider.RATES_CONTENT_URI, values, "curr = '" + curr + "'", null);
                } while (cursor.moveToNext());
            } catch (InterruptedException ignore) {
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }
}
