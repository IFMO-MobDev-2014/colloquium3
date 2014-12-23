package ru.ifmo.md.colloquium3.database;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Nikita Yaschenko on 23.12.14.
 */
public class CurrencyDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "currency.db";

    private static final int VERSION = 1;

    interface Tables {
        String CURRENCIES = "currencies";
    }

    public CurrencyDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.CURRENCIES + " ("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CurrencyContract.CurrencyColumns.CURRENCY_NAME + " TEXT NOT NULL,"
                    + CurrencyContract.Currency.CURRENCY_CNT+ " REAL NOT NULL"
                    + ");"
        );

        db.execSQL(createEntry("Dollar", 50.0));
        db.execSQL(createEntry("Euro", 69.0));
        db.execSQL(createEntry("Pound", 102.0));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }

    private static String createEntry(String name, double cnt) {
        return "INSERT INTO " + Tables.CURRENCIES + "(" +
                    CurrencyContract.CurrencyColumns.CURRENCY_NAME + ", " +
                    CurrencyContract.CurrencyColumns.CURRENCY_CNT + ")" +
                " VALUES (" +
                    DatabaseUtils.sqlEscapeString(name) + ", " +
                    cnt +
                ");";
    }

}
