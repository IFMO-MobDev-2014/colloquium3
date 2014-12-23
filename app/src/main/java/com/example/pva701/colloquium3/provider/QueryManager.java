package com.example.pva701.colloquium3.provider;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by pva701 on 23.12.14.
 */
public class QueryManager {
    public static QueryManager get(Context c) {
        if (instance == null)
            instance = new QueryManager(c);
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
}
