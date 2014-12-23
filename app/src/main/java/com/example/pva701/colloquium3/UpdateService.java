package com.example.pva701.colloquium3;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.pva701.colloquium3.provider.QueryManager;

import java.util.Random;

/**
 * Created by pva701 on 23.12.14.
 */
public class UpdateService extends IntentService {
    public static String TAG = "UpdateService";
    private static Handler handler;

    public static int CURRENCY_UPDATED = 0;
    public static int cnt = 0;
    public static Random rnd = new Random(System.currentTimeMillis());

    public UpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ++cnt;
        if (cnt % 10 == 0) {
            double array[] = new double[QueryManager.get(getApplicationContext()).sizeCourse()];
            for (int i = 0; i < array.length; ++i)
                array[i] = (rnd.nextBoolean() ? -0.5 : 0.5);
            QueryManager.get(getApplicationContext()).addCourses(array);
        } else {
            double array[] = new double[QueryManager.get(getApplicationContext()).sizeCourse()];
            for (int i = 0; i < array.length; ++i)
                array[i] = rnd.nextDouble() - 0.5;
            QueryManager.get(getApplicationContext()).addCourses(array);
        }

        if (handler != null)
            handler.obtainMessage(UpdateService.CURRENCY_UPDATED).sendToTarget();
    }

    public static void setHandler(Handler h) {
        handler = h;
    }


    public static void setServiceAlarm(Context context, boolean isOn) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            cnt = 0;
            Intent intent = new Intent(context, UpdateService.class);
            PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 1000, pi);
        } else if (isServiceAlarmOn(context)) {
            Intent intent = new Intent(context, UpdateService.class);
            PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = new Intent(context, UpdateService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
}
