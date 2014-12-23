package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MSviridenkov on 23.12.2014.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "money";

    public static final String TABLE_COURSE = "courses";

    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_COUNT = "count";

    private static final String CREATE_TABLE_COURSE = "CREATE TABLE "
            + TABLE_COURSE + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
            COLUMN_NAME_NAME + " TEXT," +
            COLUMN_NAME_COUNT + " TEXT" + " );";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COURSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);

        onCreate(db);
    }
}
