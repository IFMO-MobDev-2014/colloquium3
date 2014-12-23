package ru.ifmo.md.colloquium3;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Random;

import ru.ifmo.md.colloquium3.database.CurrencyContract;


public class CurrencyService extends IntentService {
    private static final String ACTION_UPDATE_CURRENCIES = "ru.ifmo.md.colloquium3.action.UPDATE_CURRENCIES";

    private static final String EXTRA_PARAM1 = "ru.ifmo.md.colloquium3.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "ru.ifmo.md.colloquium3.extra.PARAM2";


    public static void startActionUpdateCurrencies(Context context) {
        Intent intent = new Intent(context, CurrencyService.class);
        intent.setAction(ACTION_UPDATE_CURRENCIES);
        context.startService(intent);
    }

    public CurrencyService() {
        super("CurrencyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_CURRENCIES.equals(action)) {
                handleActionUpdateCurrencies();
            }
        }
    }

    private void handleActionUpdateCurrencies() {
        Cursor cursor = getContentResolver().query(
                CurrencyContract.Currency.CONTENT_URI,
                CurrencyContract.Currency.CNT_COLUMNS, null, null, null);
        cursor.moveToFirst();
        Random random = new Random();
        while (!cursor.isBeforeFirst() && !cursor.isAfterLast()) {
            final long id = cursor.getLong(cursor.getColumnIndex(CurrencyContract.Currency._ID));
            double cnt = cursor.getLong(cursor.getColumnIndex(CurrencyContract.Currency.CURRENCY_CNT));
            double delta = random.nextDouble() * 0.1;
            if (random.nextBoolean()) {
                delta *= -1;
            }
            Log.d("TAG", "delta: " + delta);
            cnt += delta;
            Log.d("TAG", "id: " + id + ", cnt: " + cnt);
            ContentValues values = new ContentValues();
            values.put(CurrencyContract.Currency.CURRENCY_CNT, cnt);
            getContentResolver().update(CurrencyContract.Currency.buildCurrencyUri(Long.toString(id)), values, null, null);
            cursor.moveToNext();
        }
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = new Intent(context, CurrencyService.class);
        intent.setAction(ACTION_UPDATE_CURRENCIES);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = new Intent(context, CurrencyService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public static float getRoubles(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat("roubles", 1000);
    }

    public static void setRoubles(Context context, float roubles) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putFloat("roubles", roubles)
                .commit();
    }

}
