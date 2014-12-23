package com.example.pva701.colloquium3.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by pva701 on 23.12.14.
 */
public class QueryManager {
    public static QueryManager get(Context c) {
        if (instance == null)
            instance = new QueryManager(c.getApplicationContext());
        return instance;
    }

    private static QueryManager instance;

    private Context context;
    private QueryManager(Context c) {
        context = c;
    }

    public Cursor getAllCourses() {
        return context.getContentResolver().query(CurrencyProvider.COURSE_CONTENT_URI, null, null, null, null);
    }

    public int sizeCourse() {
        int size = 0;
        Cursor cursor = context.getContentResolver().query(CurrencyProvider.COURSE_CONTENT_URI, null, null, null, null);
        while (!cursor.isAfterLast()) {
            ++size;
            cursor.moveToNext();
        }
        return size;
    }

    public void addCourses(double[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            ContentValues cv = new ContentValues();
            cv.put("add", arr[i]);
            context.getContentResolver().update(CurrencyProvider.ADD_COURSE_CONTENT_URI,
                    cv, CurrencyDbHelper.COURSE_ID + " = " + (i + 1), null);
        }
    }
}
