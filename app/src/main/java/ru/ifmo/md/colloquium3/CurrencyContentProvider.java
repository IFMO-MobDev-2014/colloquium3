package ru.ifmo.md.colloquium3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class CurrencyContentProvider extends ContentProvider {
    private static final int CURRENCIES = 0;
    private static final int CITY_ID = 1;
    //private static final int PRICES = 2;
    //private static final int WEATHER_ID = 3;

    private static final String AUTHORITY = "ru.ifmo.md.colloquium3";
    private static final String PATH_CURRENCIES = "currencies";
    public static final Uri CONTENT_URI_CURRENCIES = Uri.parse("content://" + AUTHORITY + "/" + PATH_CURRENCIES);
    //private static final String PATH_PRICES = "weathers";
    //public static final Uri CONTENT_URI_PRICES = Uri.parse("content://" + AUTHORITY + "/" + PATH_PRICES);
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CURRENT_PRICE = "current_price";
    public static final String CURRENCY_NAME = "name";
    public static final String AMOUNT = "amount";

    static {
        URI_MATCHER.addURI(AUTHORITY, PATH_CURRENCIES, CURRENCIES);
        URI_MATCHER.addURI(AUTHORITY, PATH_CURRENCIES + "/#", CITY_ID);
        //URI_MATCHER.addURI(AUTHORITY, PATH_PRICES, PRICES);
        //URI_MATCHER.addURI(AUTHORITY, PATH_PRICES + "/#", WEATHER_ID);
    }

    static String DB_NAME = "currencies.db";
    static int DB_VERSION = 1;
    private static String CURRENCIES_TABLE = "currencies";
    //private static String PRICES_TABLE = "weathers";
    private FeedDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new FeedDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case CURRENCIES:
                queryBuilder.setTables(CURRENCIES_TABLE);
                break;
            case CITY_ID:
                queryBuilder.setTables(CURRENCIES_TABLE);
                queryBuilder.appendWhere("_id=" + uri.getLastPathSegment());
                break;
            /*
            case PRICES:
                queryBuilder.setTables(PRICES_TABLE);
                break;
            case WEATHER_ID:
                queryBuilder.setTables(PRICES_TABLE);
                queryBuilder.appendWhere("currency_id=" + uri.getLastPathSegment());
                break;
                */
            default:
                throw new IllegalArgumentException("Bad URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        long id;
        switch (uriType) {
            case CURRENCIES:
                id = sqlDB.insert(CURRENCIES_TABLE, null, values);
                break;
            /*
            case PRICES:
                id = sqlDB.insert(PRICES_TABLE, null, values);
                break;
            case WEATHER_ID:
                values.put("currency_id", uri.getLastPathSegment());
                id = sqlDB.insert(PRICES_TABLE, null, values);
                break;
                */
            default:
                throw new IllegalArgumentException("Bad URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, "" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case CURRENCIES:
                rowsDeleted = sqlDB.delete(CURRENCIES_TABLE, selection, selectionArgs);
                break;
            case CITY_ID:
                String fid = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(CURRENCIES_TABLE, "_id=" + fid, null);
                } else {
                    rowsDeleted = sqlDB.delete(CURRENCIES_TABLE, "_id=" + fid + " and " + selection, selectionArgs);
                }
                break;
            /*
            case PRICES:
                rowsDeleted = sqlDB.delete(PRICES_TABLE, selection, selectionArgs);
                break;
            case WEATHER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PRICES_TABLE, "currency_id=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(PRICES_TABLE, "currency_id=" + id + " and " + selection, selectionArgs);
                }
                break;
                */
            default:
                throw new IllegalArgumentException("Bad URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated;
        switch (uriType) {
            case CURRENCIES:
                rowsUpdated = db.update(CURRENCIES_TABLE, values, selection, selectionArgs);
                break;
            case CITY_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(CURRENCIES_TABLE, values, "_id=" + id, null);
                } else {
                    rowsUpdated = db.update(CURRENCIES_TABLE, values, "_id=" + id + " and " + selection, selectionArgs);
                }
                break;
            /*
            case PRICES:
                rowsUpdated = db.update(PRICES_TABLE, values, selection, selectionArgs);
                break;
            case WEATHER_ID:
                String currencyId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(PRICES_TABLE, values, "_id=" + currencyId, null);
                } else {
                    rowsUpdated = db.update(PRICES_TABLE, values, "_id=" + currencyId + " and " + selection, selectionArgs);
                }
                break;
                */
            default:
                throw new IllegalArgumentException("Bad URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private final class FeedDatabaseHelper extends SQLiteOpenHelper {
        public FeedDatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL("CREATE TABLE " + CURRENCIES_TABLE +
                            " (_id INTEGER PRIMARY KEY AUTOINCREMENT" +
                            ", name TEXT NOT NULL" +
                            ", current_price INTEGER NOT NULL" +
                            ", amount INTEGER NOT NULL);"
            );
            //db.execSQL("CREATE TABLE " + PRICES_TABLE +
            //                " (_id INTEGER PRIMARY KEY AUTOINCREMENT" +
            //                ", time INTEGER NOT NULL" +
            //                ", price TEXT NOT NULL" +
            //                ", currency_id INTEGER REFERENCES currencies(_id) ON DELETE CASCADE);"
            //);
            db.execSQL("INSERT INTO " + CURRENCIES_TABLE + "(_id,name,current_price,amount) VALUES(0,'RUB', 1, 10000);");
            db.execSQL("INSERT INTO " + CURRENCIES_TABLE + "(name,current_price,amount) VALUES('USD', 5400, 0);");
            db.execSQL("INSERT INTO " + CURRENCIES_TABLE + "(name,current_price,amount) VALUES('EUR', 6500, 0);");
            db.execSQL("INSERT INTO " + CURRENCIES_TABLE + "(name,current_price,amount) VALUES('GBP', 7500, 0);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i2) {
            db.execSQL("DROP TABLE IF EXISTS " + CURRENCIES_TABLE);
            //db.execSQL("DROP TABLE IF EXISTS " + PRICES_TABLE);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int i, int i2) {
            db.execSQL("DROP TABLE IF EXISTS " + CURRENCIES_TABLE);
            //db.execSQL("DROP TABLE IF EXISTS " + PRICES_TABLE);
            onCreate(db);
        }
    }
}
