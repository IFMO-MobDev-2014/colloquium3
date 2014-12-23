package ru.ifmo.md.colloquium3;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Random;

public class RSSIntentService extends IntentService {

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent != null) {
            String actionUpdate = "ru.ifmo.md.colloquium3.action.updateChannel";
            String actionUpdateAll = "ru.ifmo.md.colloquium3.action.updateAllChannels";
            if (actionUpdate.equals(intent.getAction())) {
                if (DataBase.getDataBase(this).getUrlByChannelId(intent.getLongExtra("ru.ifmo.md.colloquium3.extra.channel_id", -1)) != null) {

                }
            } else if (actionUpdateAll.equals(intent.getAction())) {
                final Cursor cursor = DataBase.getDataBase(this).sqLiteDatabase.query("channel", new String[]{"_id", "name", "url"}, null, null, null, null, null);
                if (!cursor.moveToFirst()) return;
                do {
                    handleActionUpdateChannel(cursor.getLong(cursor.getColumnIndex("_id")));
                    String[] st = new String[] {"USD", "EUR", "GBP"};
                    int i = 0;
                    if (DataBase.getDataBase(this).getUrlByChannelId(cursor.getLong(cursor.getColumnIndex("_id"))) != null) {
                        ContentValues contentValues = new ContentValues();
                        Random random = new Random();
                        int k = random.nextInt() % 100;
                        if (k < 0) k = -k;
                        contentValues.put("name", k);
                        contentValues.put("url", st[i]);
                        i++;

                        DataBase.getDataBase(this).sqLiteDatabase.update("channel", contentValues, "" + cursor.getLong(cursor.getColumnIndex("_id")), null);
                        getContentResolver().notifyChange(Uri.parse(Uri.parse("content://ru.ifmo.md.colloquium3.feeds/channels") + "/" + contentValues.getAsLong("channel_id")), null);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
            AlarmManager manager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            manager1.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 1000, pi);
            AlarmManager manager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pi1 = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            manager1.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + 100, 100, pi1);
        }
    }

    public static void start(Context context, long rssNumber) {
        Intent intent = new Intent(context, RSSIntentService.class);
        intent.setAction("ru.ifmo.md.colloquium3.action.updateAllChannels");
        intent.putExtra("ru.ifmo.md.colloquium3.extra.channel_id", rssNumber);
        context.startService(intent);
    }

    public RSSIntentService() {
        super("RSSIntentService");
    }

    private void handleActionUpdateChannel(final long rssNumber) {
        if (DataBase.getDataBase(this).getUrlByChannelId(rssNumber) != null) {

        }
    }


}
