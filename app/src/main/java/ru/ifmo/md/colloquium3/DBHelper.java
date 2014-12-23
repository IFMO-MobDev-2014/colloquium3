package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Амир on 23.12.2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "data.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CHANNELS_TABLE_NAME = "CURRENCY";
    public static final String CHANNELS_COLUMN_ID = "ID";
    public static final String CHANNELS_COLUMN_NAME = "NAME";
    public static final String CHANNELS_COLUMN_COST = "COST";
    public static final String CHANNELS_ON_CREATE = "create table if not exists " + CHANNELS_TABLE_NAME + "("
            + CHANNELS_COLUMN_ID + " integer primary key autoincrement, " + CHANNELS_COLUMN_NAME + " text, " + CHANNELS_COLUMN_COST + " float);";
    public static final String CHANNELS_ON_DESTROY = "drop table if exists " + CHANNELS_TABLE_NAME;



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CHANNELS_ON_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(CHANNELS_ON_DESTROY);
        onCreate(sqLiteDatabase);
    }
}
