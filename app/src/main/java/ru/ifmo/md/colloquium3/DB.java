package ru.ifmo.md.colloquium3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Галина on 23.12.2014.
 */
public class DB {
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Currency";
    private static final int DATABASE_VERSION = 1;
    private static final String CURRENCIES_TABLE = "currencies";
    private static final String LAST_TABLE = "last";

    public static final String KEY_ID = "_id";
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_LAST = "last";

    private static final String INIT_CURRENCY =
            "create table if not exists " + CURRENCIES_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_CURRENCY + " text not null)";

    private static final String INIT_LAST =
            "create table if not exists " + LAST_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_LAST + " integer not null)";

    private static final String REMOVE_CURRENCY =
            "drop table if exists " + CURRENCIES_TABLE;

    private static final String REMOVE_LAST =
            "drop table if exists " + LAST_TABLE;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(INIT_CURRENCY);
            db.execSQL(INIT_LAST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(REMOVE_CURRENCY);
            db.execSQL(REMOVE_LAST);
            onCreate(db);
        }
    }

    public DB(Context ctx) {
        this.mCtx = ctx;
    }

    private void init() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LAST, -1);
        mDb.insert(LAST_TABLE, null, initialValues);
    }

    public DB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        init();
        return this;
    }

    public void close() {
        mDb.close();
        mDbHelper.close();
    }

    public long addCurrency(String currency) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CURRENCY, currency);
        return mDb.insert(CURRENCIES_TABLE, null, initialValues);
    }

    public boolean updateCurrency(long rowID, String currency) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_CURRENCY, currency);
        return mDb.update(CURRENCIES_TABLE, newValues, KEY_ID + "=" + rowID, null) > 0;
    }

    public boolean deleteCurrency(long rowID) {
        return mDb.delete(CURRENCIES_TABLE, KEY_ID + "=" + rowID, null) > 0;
    }

    public boolean setLast(int currencyID) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_LAST, currencyID);
        return mDb.update(LAST_TABLE, newValues, null, null) > 0;
    }

    public Cursor getLast() {
        return mDb.query(LAST_TABLE, new String[] {KEY_LAST}, null, null, null, null, null);
    }
}
