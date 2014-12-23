package ru.ifmo.md.colloquium3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Евгения on 23.12.2014.
 */
public class DBAdapter {

    private static DBAdapter mInstance = null;

    public static DBAdapter getOpenedInstance(Context context){
        if (mInstance == null)
            try {
                mInstance = new DBAdapter(context.getApplicationContext()).open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return mInstance;
    }

    private static final String LOG_TAG = "CurrencyDBAdapter";
    //Common keys
    public static final String KEY_ID = "_id";
    public static final String KEY_TIME = "time";

    //Currency
    public static final String TABLE_NAME_CURRENCY = "currency";
    public static final String KEY_CURRENCY_GBP = "pound";
    public static final String KEY_CURRENCY_EUR = "euro";
    public static final String KEY_CURRENCY_USD = "dollar";
    public static final String KEY_CURRENCY_CAPACITY_GBP = "capacity_pound";
    public static final String KEY_CURRENCY_CAPACITY_EUR = "capacity_euro";
    public static final String KEY_CURRENCY_CAPACITY_USD = "capacity_dollar";

    public static final String CREATE_TABLE_VALUTA = "CREATE TABLE " + TABLE_NAME_CURRENCY + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TIME + " INTEGER NOT NULL, "
            + KEY_CURRENCY_GBP + " REAL NOT NULL, "
            + KEY_CURRENCY_EUR + " REAL NOT NULL, "
            + KEY_CURRENCY_USD + " REAL NOT NULL, "
            + KEY_CURRENCY_CAPACITY_GBP + " REAL NOT NULL, "
            + KEY_CURRENCY_CAPACITY_EUR + " REAL NOT NULL, "
            + KEY_CURRENCY_CAPACITY_USD + " REAL NOT NULL"
            + ")";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME    = "valuta.db";
    private static final int    DATABASE_VERSION = 13;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        @Override
        public void onOpen(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");
        }

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_VALUTA);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CURRENCY);
            onCreate(db);
        }
    }

    public DBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        if (getSnapshots().getCount() == 0)
            initCurrency();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void initCurrency() {
        ContentValues initialValues = new ContentValues();
        saveSnapshot(60.3, 500, 50.43, 400, 70.3, 300);
    }



    public long saveSnapshot(double gpbCourse, double gbpCapacity,
                             double usdCourse, double usdCapacity,
                             double eurCourse, double eurCapacity) {
        ContentValues values = makeContentValues(gpbCourse, gbpCapacity, usdCourse, usdCapacity, eurCourse, eurCapacity);
        return saveSnapshot(values);
    }

    public static ContentValues makeContentValues(double gpbCourse, double gbpCapacity, double usdCourse, double usdCapacity, double eurCourse, double eurCapacity) {
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, System.currentTimeMillis() / 1000);
        values.put(KEY_CURRENCY_GBP, gpbCourse);
        values.put(KEY_CURRENCY_CAPACITY_GBP, gbpCapacity);
        values.put(KEY_CURRENCY_USD, usdCourse);
        values.put(KEY_CURRENCY_CAPACITY_USD, usdCapacity);
        values.put(KEY_CURRENCY_EUR, eurCourse);
        values.put(KEY_CURRENCY_CAPACITY_EUR, eurCapacity);
        return values;
    }

    public long saveSnapshot(ContentValues values) {
        return mDb.insert(TABLE_NAME_CURRENCY, null, values);
    }

    public Cursor getSnapshots() {
        return mDb.query(
                TABLE_NAME_CURRENCY
                , new String[]{KEY_ID,
                        KEY_CURRENCY_GBP, KEY_CURRENCY_CAPACITY_GBP,
                        KEY_CURRENCY_USD, KEY_CURRENCY_CAPACITY_USD,
                        KEY_CURRENCY_EUR, KEY_CURRENCY_CAPACITY_EUR}
                , null, null, null, null, KEY_TIME + " DESC", "100");
    }

    /*public Cursor fetchCandidateById(long id) {
        return mDb.query(
                CANDIDATES_TABLE_NAME
                , new String[]{KEY_ID, KEY_CANDIDATE, KEY_VOTES}
                , KEY_ID +"="+id, null, null, null, null);
    }

    public boolean updateCandidate(long id, String newName) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_CANDIDATE, newName);
        return mDb.update(CANDIDATES_TABLE_NAME, newValues, KEY_ID + "=" + id, null) > 0;
    }*/

    public void clearSnapshots() {
        mDb.delete(TABLE_NAME_CURRENCY, null, null);
    }
}
