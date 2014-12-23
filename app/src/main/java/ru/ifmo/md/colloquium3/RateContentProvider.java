package ru.ifmo.md.colloquium3;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.Telephony;
import android.text.TextUtils;

public class RateContentProvider extends ContentProvider {
    //private static final String LOG_TAG = "myLogs";

    public static final String SIMPLE_CUR = "simple_cur";
    public static final String CURS_TABLE_NAME = "feeds";

    private static final int CURS = 1;
    private static final int CURS_ID = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Currency.AUTHORITY, Currency.SimpleCurrency.CURRENCY_NAME, CURS);
        uriMatcher.addURI(Currency.AUTHORITY, Currency.SimpleCurrency.CURRENCY_NAME + "/#", CURS_ID);
    }

    private static class CursDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = SIMPLE_CUR + ".db";
        private static int DATABASE_VERSION = 1;

        private CursDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            createTables(sqLiteDatabase);
        }

        private void createTables(SQLiteDatabase sqLiteDatabase) {
            String qs = "CREATE TABLE " + CURS_TABLE_NAME + " ("
                    + Currency.SimpleCurrency._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Currency.SimpleCurrency.CUR_NAME + " TEXT, "
                    + Currency.SimpleCurrency.RATE_NAME + " INTEGER" + ");";
            sqLiteDatabase.execSQL(qs);
            insertFeeds(sqLiteDatabase);
        }

        private void insertFeeds(SQLiteDatabase sqLiteDatabase) {
            ContentValues cv = new ContentValues();
            cv.put(Currency.SimpleCurrency.CUR_NAME, "usd");
            cv.put(Currency.SimpleCurrency.RATE_NAME, 5500);
            sqLiteDatabase.insert(CURS_TABLE_NAME, null, cv);
            cv.clear();
            cv.put(Currency.SimpleCurrency.CUR_NAME, "eur");
            cv.put(Currency.SimpleCurrency.RATE_NAME, 6500);
            sqLiteDatabase.insert(CURS_TABLE_NAME, null, cv);
            cv.clear();
            cv.put(Currency.SimpleCurrency.CUR_NAME, "gpb");
            cv.put(Currency.SimpleCurrency.RATE_NAME, 7500);
            sqLiteDatabase.insert(CURS_TABLE_NAME, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CURS_TABLE_NAME + ";");
            createTables(sqLiteDatabase);
        }
    }

    public RateContentProvider() {}

    private CursDbHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Log.d(LOG_TAG, "delete, " + uri.toString());

        int match = uriMatcher.match(uri);
        int affected;

        switch (match) {
            case CURS:
                affected = getDb().delete(CURS_TABLE_NAME,
                        (!TextUtils.isEmpty(selection) ?
                                " (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case CURS_ID:
                long feedId = ContentUris.parseId(uri);
                affected = getDb().delete(CURS_TABLE_NAME,
                        Currency.SimpleCurrency._ID + "=" + feedId
                                + (!TextUtils.isEmpty(selection) ?
                                " (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unknown feed element: " +
                        uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    @Override
    public String getType(Uri uri) {
        //Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case CURS:
                return Currency.SimpleCurrency.CONTENT_TYPE;
            case CURS_ID:
                return Currency.SimpleCurrency.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown type: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        //Log.d(LOG_TAG, "insert, " + uri.toString());
        int u = uriMatcher.match(uri);
        if (u != CURS) {
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = initialValues;
        } else {
            values = new ContentValues();
        }

        db = getDb();

        if (u == CURS) {
            long rowID = db.insert(CURS_TABLE_NAME, null, values);
            if (rowID > 0) {
                Uri resultUri = ContentUris.withAppendedId(Currency.SimpleCurrency.CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            }
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        //Log.d(LOG_TAG, "onCreate");
        dbHelper = new CursDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //Log.d(LOG_TAG, "query, " + uri.toString());

        String TABLE_NAME;
        Uri CONTENT_URI;
        switch (uriMatcher.match(uri)) {
            case CURS:
                //Log.d(LOG_TAG, "URI_FEEDS");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Currency.SimpleCurrency.CUR_NAME + " ASC";
                }
                TABLE_NAME = CURS_TABLE_NAME;
                CONTENT_URI = Currency.SimpleCurrency.CONTENT_URI;
                break;
            case CURS_ID: {
                String id = uri.getLastPathSegment();
                //Log.d(LOG_TAG, "URI_FEEDS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = Currency.SimpleCurrency._ID + " = " + id;
                } else {
                    selection = selection + " AND " + Currency.SimpleCurrency._ID + " = " + id;
                }
                TABLE_NAME = CURS_TABLE_NAME;
                CONTENT_URI = Currency.SimpleCurrency.CONTENT_URI;
            }
            break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = getDb();
        Cursor cursor = db.query(TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int affected;

        switch (uriMatcher.match(uri)) {
            case CURS:
                affected = getDb().update(CURS_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case CURS_ID:
                String feedId = uri.getPathSegments().get(1);
                affected = getDb().update(CURS_TABLE_NAME, values,
                        Currency.SimpleCurrency._ID + "=" + feedId
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return affected;
    }

    private SQLiteDatabase getDb() { return dbHelper.getWritableDatabase(); }
}
