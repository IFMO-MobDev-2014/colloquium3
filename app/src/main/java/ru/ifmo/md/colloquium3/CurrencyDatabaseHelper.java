package ru.ifmo.md.colloquium3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Алексей on 23.12.2014.
 */
public class CurrencyDatabaseHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final String DB_NAME = "currency.db";
    public static final int DB_VERSION = 3;

    public static final String CURRENCY_TABLE_NAME = "currency";
    public static final String CURRENCY_ID = _ID;
    public static final String CURRENCY_NAME = "name";
    public static final String CURRENCY_VALUE = "value";

    public static final String CREATE_CURRENCY_TABLE = "create table "
            + CURRENCY_TABLE_NAME + " ("
            + CURRENCY_ID + " integer primary key autoincrement, "
            + CURRENCY_NAME + " text, "
            + CURRENCY_VALUE + " real)";

    public static final String ACCOUNT_TABLE_NAME = "account";
    public static final String ACCOUNT_ID = _ID;
    public static final String ACCOUNT_NAME = "name";
    public static final String ACCOUNT_COUNT = "count";

    public static final String CREATE_ACCOUNT_TABLE = "create table "
            + ACCOUNT_TABLE_NAME + " ("
            + ACCOUNT_ID + " integer primary key autoincrement, "
            + ACCOUNT_NAME + " text, "
            + ACCOUNT_COUNT + " real)";


    public CurrencyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d("base", "start");
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CURRENCY_TABLE);
        sqLiteDatabase.execSQL(CREATE_ACCOUNT_TABLE);

        ContentValues cv = new ContentValues();
        cv.put(CURRENCY_NAME, "EUR");
        cv.put(CURRENCY_VALUE, 65D);
        sqLiteDatabase.insert(CURRENCY_TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(ACCOUNT_NAME, "EUR");
        cv.put(ACCOUNT_COUNT, 0D);
        sqLiteDatabase.insert(ACCOUNT_TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(CURRENCY_NAME, "USD");
        cv.put(CURRENCY_VALUE, 50D);
        sqLiteDatabase.insert(CURRENCY_TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(ACCOUNT_NAME, "USD");
        cv.put(ACCOUNT_COUNT, 0D);
        sqLiteDatabase.insert(ACCOUNT_TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(CURRENCY_NAME, "GBP");
        cv.put(CURRENCY_VALUE, 45D);
        sqLiteDatabase.insert(CURRENCY_TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(ACCOUNT_NAME, "GBP");
        cv.put(ACCOUNT_COUNT, 0D);
        sqLiteDatabase.insert(ACCOUNT_TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(ACCOUNT_NAME, "RUB");
        cv.put(ACCOUNT_COUNT, 10000D);
        sqLiteDatabase.insert(ACCOUNT_TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("drop table " + CURRENCY_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table " + ACCOUNT_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static class CurrencyCursor extends CursorWrapper{
        private Cursor cursor;

        public CurrencyCursor(Cursor cursor) {
            super(cursor);
            this.cursor = cursor;
        }

        public static Currency getCurrency(Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex(CURRENCY_NAME));
            double value = cursor.getDouble(cursor.getColumnIndex(CURRENCY_VALUE));

            return new Currency(name, value);
        }

        public Currency getCurrency() {
            return getCurrency(cursor);
        }
    }

    public static class AccountCursor extends CursorWrapper{
        private Cursor cursor;

        public AccountCursor(Cursor cursor) {
            super(cursor);
            this.cursor = cursor;
        }

        public static Account getAccount(Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex(ACCOUNT_NAME));
            double value = cursor.getDouble(cursor.getColumnIndex(ACCOUNT_COUNT));

            return new Account(name, value);
        }

        public Account getAccount() {
            return getAccount(cursor);
        }
    }
}
