package ru.ifmo.mobdev.colloquium3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author sugakandrey
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Currencies";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        ContentValues cv = new ContentValues();
        cv.put(CurrencyDatabase.CURRENCY_NAME, "USD");
        cv.put(CurrencyDatabase.CURRENCY_RATE, 54);
        cv.put(CurrencyDatabase.CURRENCY_AMOUNT, 0);
        db.insert(DATABASE_NAME, null, cv);
        cv.clear();
        cv.put(CurrencyDatabase.CURRENCY_NAME, "EUR");
        cv.put(CurrencyDatabase.CURRENCY_RATE, 65);
        cv.put(CurrencyDatabase.CURRENCY_AMOUNT, 0);
        db.insert(DATABASE_NAME,null, cv);
        cv.clear();
        cv.put(CurrencyDatabase.CURRENCY_NAME, "GBP");
        cv.put(CurrencyDatabase.CURRENCY_RATE, 80);
        cv.put(CurrencyDatabase.CURRENCY_AMOUNT, 0);
        db.insert(DATABASE_NAME,null, cv);
        cv.clear();
        cv.put(CurrencyDatabase.CURRENCY_NAME, "RUB");
        cv.put(CurrencyDatabase.CURRENCY_RATE, 1);
        cv.put(CurrencyDatabase.CURRENCY_AMOUNT, 10000);
        db.insert(DATABASE_NAME,null, cv);
        CurrencyDatabase.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CurrencyDatabase.onUpgrade(db, oldVersion, newVersion);
    }
}