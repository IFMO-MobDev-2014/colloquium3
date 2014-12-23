package ru.ifmo.md.colloquium3;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Амир on 23.12.2014.
 */
public class Receiver extends BroadcastReceiver {

    public static final String BROADCAST = "Load";

    public ArrayList<Item> changeCurrency(Context context) {
        Cursor cursor = context.getContentResolver().query(DBContentProvider.CURRENCY, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<Item> items = new ArrayList<Item>();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.CHANNELS_COLUMN_NAME));
            double cost = cursor.getDouble(cursor.getColumnIndex(DBHelper.CHANNELS_COLUMN_COST));
            cursor.moveToNext();
            items.add(new Item(name, cost));
        }
        cursor.close();
        Random r = new Random();
        context.getContentResolver().delete(DBContentProvider.CURRENCY, null, null);
        for (Item i : items) {
            if (r.nextBoolean()) {
                i.cost += 0.1;
            } else {
                i.cost -= 0.1;
            }
            ContentValues k = new ContentValues();
            k.put(DBHelper.CHANNELS_COLUMN_NAME, i.name);
            k.put(DBHelper.CHANNELS_COLUMN_COST, i.cost);
            context.getContentResolver().insert(DBContentProvider.CURRENCY, k);
        }
        return items;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Item> items = changeCurrency(context);
        Log.d("asd", "asd");
        Intent intentResponse = new Intent();
        intentResponse.setAction(BROADCAST);
        context.sendBroadcast(intentResponse);
    }
}