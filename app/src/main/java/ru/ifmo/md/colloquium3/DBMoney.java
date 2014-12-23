package ru.ifmo.md.colloquium3;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBMoney extends SQLiteOpenHelper {
    private static final String DB_NAME = "money.db";
    private static final int VERSION = 4;

    public static final String TABLE_MONEY1 = "money1";
    public static final String ID1 = "_id";
    public static final String CURRENCY1 = "currency";
    public static final String RATE1 = "rate";
    public static final String SUM1 = "sum";
    public static final String MONEY_TYPE1 = "moneytype";

    private SQLiteDatabase database;

    private static final String INIT_MONEY1_TABLE =
            "CREATE TABLE " + TABLE_MONEY1 + " (" +
                    ID1 + " INTEGER " + "PRIMARY KEY AUTOINCREMENT, " +
                    CURRENCY1 + " TEXT, " +
                    RATE1 + " REAL, " +
                    SUM1 + " REAL, " +
                    MONEY_TYPE1 + " TEXT ) ;";

    private static final CurrencyItem[] defaultItems = new CurrencyItem[]{
        new CurrencyItem("RUB", 10.0, 10000),
        new CurrencyItem("USD", 54d, 0),
        new CurrencyItem("EUR", 75d, 0),
        new CurrencyItem("GBP", 85d, 0)
    };


    public DBMoney(Context context) {
        super(context, DB_NAME, null, VERSION);
        database = getWritableDatabase();
        addAll();
    }

    private void addItem(CurrencyItem item) {
        ContentValues cv = new ContentValues();
        cv.put(DBMoney.CURRENCY1, item.getCurrency());
        cv.put(DBMoney.RATE1, item.getRate());
        cv.put(DBMoney.SUM1, item.getSum());
        database.insert(DBMoney.TABLE_MONEY1, null, cv);
    }

    private void addAll() {
        Cursor cursor = database.rawQuery("SELECT COUNT (*) FROM " + TABLE_MONEY1, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                for (CurrencyItem item : defaultItems) {
                    addItem(item);
                }
            }
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INIT_MONEY1_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONEY1);
        onCreate(db);
    }
}
