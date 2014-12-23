package com.example.pva701.colloquium3.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pva701.colloquium3.Course;

/**
 * Created by pva701 on 17.10.14.
 */
public class CurrencyDbHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DB_NAME = "weather";

    public static final String TABLE_COURSE = "course";
    public static final String COURSE_ID = "_id";
    public static final String COURSE_NAME = "name";
    public static final String COURSE_VAL = "value";

    public static final String TABLE_TOTAL = "total";


    public static class CourseCursor extends CursorWrapper {
        private Cursor cursor;
        public CourseCursor(Cursor cur) {
            super(cur);
            cursor = cur;
        }

        public Course getCourse() {
            return getCourse(cursor);

        }


        public static Course getCourse(Cursor cur) {
            return new Course(
                    cur.getInt(0),
                    cur.getString(1),
                    cur.getDouble(2));

        }
    }

    public CurrencyDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table course (_id integer primary key autoincrement, " +
                "name varchar(64), " +
                "value double)");

        /*db.execSQL("create table forecast (" + FORECAST_ID + " integer primary key autoincrement, "
                + FORECAST_TEMP_MIN + " integer, " + FORECAST_TEMP_MAX + " integer, " +
                FORECAST_TEMP + " integer, " + FORECAST_WIND_SPEED + " integer, " +
                FORECAST_HUMIDITY + " integer, " +
                FORECAST_WEATHER_MAIN + " varchar(30), " + FORECAST_WEATHER_DESCRIPTION + " varchar(100), " +
                FORECAST_ICON + " varchar(5), " + FORECAST_DATE + " integer, " + FORECAST_CONDITION_CODE + " integer, " +
                FORECAST_CITY_ID + " integer)");*/

        ContentValues cv = new ContentValues();
        cv.put(COURSE_NAME, "USD");
        cv.put(COURSE_VAL, 55);
        db.insert(TABLE_COURSE, null, cv);

        ContentValues cv1 = new ContentValues();
        cv1.put(COURSE_NAME, "EUR");
        cv1.put(COURSE_VAL, 65);
        db.insert(TABLE_COURSE, null, cv1);


        ContentValues cv2 = new ContentValues();
        cv2.put(COURSE_NAME, "GBP");
        cv2.put(COURSE_VAL, 75);
        db.insert(TABLE_COURSE, null, cv2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //none
    }
}