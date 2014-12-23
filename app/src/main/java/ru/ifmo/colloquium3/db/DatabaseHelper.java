package ru.ifmo.colloquium3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 3;
    private static final String DB_NAME = "colloquium3db";
    public static final String ID_KEY = "_id";
    public static final String ID_KEY_TYPE = "integer primary key autoincrement";
    public static final String WALLETS_TABLE = "wallets";
    public static final String MONEY_TABLE = "money";

    public static final String WALLET_NAME_KEY = "walletName";
    public static final String WALLET_NAME_TYPE = "text";
    public static final String WALLET_VALUE_KEY = "walletValue";
    public static final String WALLET_VALUE_TYPE = "real";

    public static final String WALLET_ID_KEY = "walletId";
    public static final String WALLET_ID_TYPE = "text";
    public static final String WALLET_AMOUNT_KEY = "walletAmount";
    public static final String WALLET_AMOUNT_TYPE = "real";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createWalletsTable(sqLiteDatabase);
        createMoneyTable(sqLiteDatabase);
        populateDatabases(sqLiteDatabase);
    }

    private void populateDatabases(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WALLET_NAME_KEY, "USD");
        contentValues.put(WALLET_VALUE_KEY, 54.0);
        db.insert(WALLETS_TABLE, null, contentValues);
        contentValues.clear();
        contentValues.put(WALLET_NAME_KEY, "EUR");
        contentValues.put(WALLET_VALUE_KEY, 65.0);
        db.insert(WALLETS_TABLE, null, contentValues);

        contentValues.clear();
        contentValues.put(WALLET_NAME_KEY, "GBP");
        contentValues.put(WALLET_VALUE_KEY, 75.0);
        db.insert(WALLETS_TABLE, null, contentValues);

        contentValues.clear();
        contentValues.put(WALLET_ID_KEY, "USD");
        contentValues.put(WALLET_AMOUNT_KEY, 0);
        db.insert(MONEY_TABLE, null, contentValues);

        contentValues.clear();
        contentValues.put(WALLET_ID_KEY, "EUR");
        contentValues.put(WALLET_AMOUNT_KEY, 0);
        db.insert(MONEY_TABLE, null, contentValues);

        contentValues.clear();
        contentValues.put(WALLET_ID_KEY, "GBP");
        contentValues.put(WALLET_AMOUNT_KEY, 0);
        db.insert(MONEY_TABLE, null, contentValues);

        contentValues.clear();
        contentValues.put(WALLET_ID_KEY, "RUB");
        contentValues.put(WALLET_AMOUNT_KEY, 10000);
        db.insert(MONEY_TABLE, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dropWalletsTable(sqLiteDatabase);
        dropMoneyTable(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    private void createWalletsTable(SQLiteDatabase db) {
        String query = "create table " + WALLETS_TABLE + " ("
                + ID_KEY + " " + ID_KEY_TYPE + ", "
                + WALLET_NAME_KEY + " " + WALLET_NAME_TYPE + ", "
                + WALLET_VALUE_KEY + " " + WALLET_VALUE_TYPE
                + ")";
        db.execSQL(query);
    }

    private void createMoneyTable(SQLiteDatabase db) {
        String query = "create table " + MONEY_TABLE + " ("
                + ID_KEY + " " + ID_KEY_TYPE + ", "
                + WALLET_ID_KEY + " " + WALLET_ID_TYPE + ", "
                + WALLET_AMOUNT_KEY + " " + WALLET_AMOUNT_TYPE
                + ")";
        db.execSQL(query);
    }

    private void dropWalletsTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("drop table if exists " + WALLETS_TABLE);
    }

    private void dropMoneyTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("drop table if exists " + MONEY_TABLE);
    }
}