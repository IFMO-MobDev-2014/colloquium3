package ru.ifmo.md.colloquium3;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

/**
 * Created by sultan on 23.12.14.
 */
public class MoneyUpdateIntent extends IntentService {

    public static final String ACTION_RESPONSE = "ru.ifmo.md.colloqium3.moneyUpdateIntent.RESPONSE";

    public MoneyUpdateIntent() {
        super("MoneyUpdateIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor cursor = getContentResolver().query(MoneyProvider.MONEY1_URI, null, DBMoney.CURRENCY1 + " <> ?", new String[]{"RUB"}, null);
        cursor.moveToFirst();
//        Log.d("INTENT", "AAAAA");
        while (!cursor.isAfterLast()) {
            ContentValues cv = new ContentValues();
            long moneyId = cursor.getLong(cursor.getColumnIndexOrThrow(DBMoney.ID1));
            double moneyRate = cursor.getLong(cursor.getColumnIndexOrThrow(DBMoney.ID1));
            moneyRate += Math.random() * 0.2 - 0.1;
            cv.put(DBMoney.RATE1, moneyRate);
            getContentResolver().update(MoneyProvider.MONEY1_URI, cv, "_id = ? ", new String[]{String.valueOf(moneyId)});
            cursor.moveToNext();
        }

        Intent response = new Intent();
        response.setAction(ACTION_RESPONSE);
        response.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(response);
    }
}
