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
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by german on 29.11.14.
 */
public class MoneyContentProvider extends ContentProvider {
    /**
     * Database constants
     */
    private SQLiteDatabase db;

    private static final String DB_NAME = "MoneyDB";
    private static final int DB_VERSION = 1;

    // Valutas
    public static final String VALUTAS_TABLE = "valutas";

    public static final String VALUTA_ID = "_id";
    public static final String VALUTA_NAME = "name";
    public static final String VALUTA_VALUE = "value";
    public static final String VALUTA_BALANCE = "balance";

    private static final String VALUTAS_TABLE_CREATE = "CREATE TABLE " + VALUTAS_TABLE + " ("
            + VALUTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VALUTA_NAME + " TEXT, "
            + VALUTA_VALUE + " DOUBLE, "
            + VALUTA_BALANCE + " DOUBLE"
            + ");";

    /**
     * Provider constants
     */
    public static final String AUTHORITY = "ru.ifmo.md.colloquium3";

    public static final Uri DATA_CONTENT = Uri.parse("content://" + AUTHORITY);
    public static final Uri VALUTAS_CONTENT = Uri.parse("content://" + AUTHORITY +
            "/" + VALUTAS_TABLE);


    // constants for uri matching (u = uri)
    private static final int uVALUTAS = 1;
    private static final int uVALUTA_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, VALUTAS_TABLE, uVALUTAS);
        uriMatcher.addURI(AUTHORITY, VALUTAS_TABLE + "/#", uVALUTA_ID);
    }

    private static HashMap<String, String> PROJECTION_MAP;

    /**
     * Help class
     */
    private static class DataBaseHelper extends SQLiteOpenHelper {
        DataBaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(VALUTAS_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + VALUTAS_TABLE);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        db = dbHelper.getWritableDatabase();
        MoneyManager.addValuta(db, "USD", 54.00);
        MoneyManager.addValuta(db, "EUR", 65.00);
        MoneyManager.addValuta(db, "KZT", 0.33);
        MoneyManager.addValuta(db, "RUB", 1.00, 10000.00);
        return (db != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case uVALUTAS:
                qb.setTables(VALUTAS_TABLE);
                qb.setProjectionMap(PROJECTION_MAP);
                break;
            case uVALUTA_ID:
                qb.setTables(VALUTAS_TABLE);
                qb.appendWhere(VALUTA_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case uVALUTAS:
                long rowID = db.insert(VALUTAS_TABLE, null, values);
                if (rowID > 0) {
                    Uri _uri = ContentUris.withAppendedId(VALUTAS_CONTENT, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                throw new SQLException("City inserting error: Failed insert values to " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case uVALUTAS:
                count = db.delete(VALUTAS_TABLE, selection, selectionArgs);
                break;
            case uVALUTA_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(VALUTAS_TABLE, VALUTA_ID + " = " + id
                                + (TextUtils.isEmpty(selection) ? "" : " AND ( " + selection + ")"),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case uVALUTAS:
                count = db.update(VALUTAS_TABLE, values, selection, selectionArgs);
                break;
            case uVALUTA_ID:
                String id = uri.getPathSegments().get(1);
                count = db.update(VALUTAS_TABLE, values, VALUTA_ID + " = " + id
                                + (TextUtils.isEmpty(selection) ? "" : " AND ( " + selection + ")"),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}