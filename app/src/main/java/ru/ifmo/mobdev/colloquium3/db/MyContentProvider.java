package ru.ifmo.mobdev.colloquium3.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.ArrayList;

import ru.ifmo.mobdev.colloquium3.Currency;

/**
 * @author sugakandrey
 */
public class MyContentProvider extends ContentProvider {
    private static final String CURRENCIES_TABLE = "Currencies";

    private DBHelper helper;

    private static final int CURRENCIES = 1;

    private static final String AUTHORITY = "ru.ifmo.mobdev.colloquium3";
    private static final String PATH_CURRENCIES = "Currencies";

    public static final Uri CONTENT_URI_CURRENCIES =
            Uri.parse("content://" + AUTHORITY + "/" + PATH_CURRENCIES);

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, PATH_CURRENCIES, CURRENCIES);
//        uriMatcher.addURI(AUTHORITY, PATH_ENTRIES, ENTRIES);
//        uriMatcher.addURI(AUTHORITY, PATH_ENTRIES + "/#", ENTRY);
//        uriMatcher.addURI(AUTHORITY, PATH_FEEDS + "/#", SINGLE_FEED);
    }


    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = helper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case CURRENCIES:
                queryBuilder.setTables(CURRENCIES_TABLE);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case CURRENCIES:
                id = db.insert(CurrencyDatabase.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int removed;
        switch (uriMatcher.match(uri)) {
            case CURRENCIES:
                removed = db.delete(CurrencyDatabase.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return removed;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int updated;
        switch (uriMatcher.match(uri)) {
            case CURRENCIES:
                updated = db.update(CurrencyDatabase.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updated;
    }
}