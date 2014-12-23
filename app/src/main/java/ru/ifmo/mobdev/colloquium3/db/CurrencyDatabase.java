package ru.ifmo.mobdev.colloquium3.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * @author sugakandrey
 */
public class CurrencyDatabase implements BaseColumns {
    public static final String TABLE_NAME = "Currencies";

    public static final String CURRENCY_NAME = "name";
    public static final String CURRENCY_RATE = "course";
    public static final String CURRENCY_AMOUNT = "amount";

    private static String DB_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    BaseColumns._ID + " integer PRIMARY KEY autoincrement, " +
                    CURRENCY_NAME + " text not null, " +
                    CURRENCY_AMOUNT + " integer, " +
                    CURRENCY_RATE + " real );";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}