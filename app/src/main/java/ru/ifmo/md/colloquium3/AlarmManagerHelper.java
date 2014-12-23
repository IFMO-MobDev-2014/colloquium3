package ru.ifmo.md.colloquium3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sultan on 23.12.14.
 */
public class AlarmManagerHelper {
    public static void startAlarm(Context context, int interval) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MoneyUpdateIntent.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);
    }

    public static void stopAlarm(Context context) {
        Intent intent = new Intent(context, MoneyUpdateIntent.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent2 = PendingIntent.getService(context, 0, intent, 0);
            alarmManager.cancel(pendingIntent2);
            pendingIntent2.cancel();
        }
    }
}
