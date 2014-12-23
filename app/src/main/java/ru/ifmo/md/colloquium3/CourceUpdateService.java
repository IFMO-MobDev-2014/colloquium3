package ru.ifmo.md.colloquium3;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Шолохов on 23.12.2014.
 */
public class CourceUpdateService extends IntentService {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public CourceUpdateService() {
        super("CourceIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            Cursor c = null;
            try {
                TimeUnit.SECONDS.sleep(1);
                c = getContentResolver().query(MyContentProvider.TABLE_COURCES_URI, null, null, null, null);
//                Log.d("WORKED", Integer.toString(c.getCount()));
                c.moveToFirst();
                do {
                    String name = c.getString(1);
                    double rate = c.getDouble(2);

                    double delta = rate + Math.random()*2-1;

                    ContentValues cv = new ContentValues();
                    cv.put(MyContentProvider.COLUMN_COURCE_NAME, name);
                    cv.put(MyContentProvider.COLUMN_CURRENT_COURCE, delta);
                    int r = getContentResolver().update(MyContentProvider.TABLE_COURCES_URI, cv, MyContentProvider.COLUMN_COURCE_NAME + " = '" + name + "'", null);
                    Log.d("ddf35", Integer.toString(r));
                } while (c.moveToNext());
                c.close();
            } catch (Exception e) {
                c.close();
                e.printStackTrace();
            }

        }
    }
}
