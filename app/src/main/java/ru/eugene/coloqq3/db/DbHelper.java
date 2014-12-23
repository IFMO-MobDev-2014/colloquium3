package ru.eugene.coloqq3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eugene on 12/23/14.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final int VERSION = 8;
    public static final String NAME = "money.db";

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MoneyDataSource.CREATE_TABLE);
        db.execSQL(CountDataSource.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoneyDataSource.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CountDataSource.TABLE);
        onCreate(db);
    }
}
