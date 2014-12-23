package ru.ifmo.colloquium3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 7;
    private static final String DB_NAME = "colloquium3db";
    public static final String ID_KEY = "_id";
    public static final String ID_KEY_TYPE = "integer primary key";
    public static final String WALLETS_TABLE = "wallets";
    public static final String MONEY_TABLE = "money";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createFeedsTable(sqLiteDatabase);
        createSubscriptionsTable(sqLiteDatabase);
        populateDatabases(sqLiteDatabase);
    }

    private void populateDatabases(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dropFeedsTable(sqLiteDatabase);
        dropSubscriptionsTable(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    private void createFeedsTable(SQLiteDatabase db) {
        String createFeedTable = "create table " + WALLETS_TABLE + " ("
                + ID_KEY + " " + ID_KEY_TYPE
                + ")";
        db.execSQL(createFeedTable);
    }

    private void createSubscriptionsTable(SQLiteDatabase db) {
        String createTable = "create table " + MONEY_TABLE + " ("
                + ID_KEY + " " + ID_KEY_TYPE
                + ")";
        db.execSQL(createTable);
    }

    private void dropFeedsTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("drop table if exists " + WALLETS_TABLE);
    }

    private void dropSubscriptionsTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("drop table if exists " + MONEY_TABLE);
    }
}