package ru.eugene.coloqq3;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import java.util.Random;

import ru.eugene.coloqq3.db.BlackProvider;
import ru.eugene.coloqq3.db.MoneyDataSource;

/**
 * Created by eugene on 12/23/14.
 */
public class CourseService extends IntentService {
    private static final long TIME1 = 1000;
    public static final String NOTIFICATION = "ayxayyayay";
    private int countTime = 0;
    private double[] count;
    private int[] id;
    private int cntEntities = 0;
    private Random rand = new Random();

    public CourseService() {
        super("CourseService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor cursor = getContentResolver().query(BlackProvider.CONTENT_URI_MONEY,
                MoneyDataSource.getProjection(), null, null, null);
        cntEntities = cursor.getCount();
        count = new double[cntEntities];
        id = new int[cntEntities];
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                id[i] = cursor.getInt(cursor.getColumnIndex(MoneyDataSource.COLUMN_ID));
                count[i] = cursor.getDouble(cursor.getColumnIndex(MoneyDataSource.COLUMN_COURSE));
                i++;
            } while (cursor.moveToNext());
        }

        try {
            while (true) {
                Thread.sleep(TIME1);
                countTime++;
                if (countTime == 10) {
                    for (int i = 0; i < cntEntities; i++) {
                        if (rand.nextBoolean()) {
                            count[i]++;
                        } else {
                            count[i]--;
                        }
                    }
                    countTime = 0;
                } else {
                    for (int i = 0; i < cntEntities; i++) {
                        count[i] += rand.nextDouble() * 2 - 1;
                    }
                }
                updCourse();
                Intent intent2 = new Intent(NOTIFICATION);
                sendBroadcast(intent2);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updCourse() {
        for (int i = 0; i < cntEntities; i++) {
            ContentValues values = new ContentValues();
            values.put(MoneyDataSource.COLUMN_COURSE, count[i]);
            getContentResolver().update(BlackProvider.CONTENT_URI_MONEY, values,
                    MoneyDataSource.COLUMN_ID + "=" + id[i], null);
        }
    }
}
