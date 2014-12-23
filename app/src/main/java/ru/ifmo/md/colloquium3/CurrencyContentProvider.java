package ru.ifmo.md.colloquium3;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Алексей on 23.12.2014.
 */
public class CurrencyContentProvider extends ContentProvider {
    public static final String AUTHORITY = CurrencyContentProvider.class.getName();

    public static final Uri CURRENCY_CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/" + CurrencyDatabaseHelper.CURRENCY_TABLE_NAME);
    public static final Uri ACCOUNT_CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/" + CurrencyDatabaseHelper.ACCOUNT_TABLE_NAME);
    static final String CURRENCY_CONTENT_TYPE =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CurrencyDatabaseHelper.CURRENCY_TABLE_NAME;
    static final String ACCOUNT_CONTENT_TYPE =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CurrencyDatabaseHelper.ACCOUNT_TABLE_NAME;
    public static final int URI_CURRENCY_ID = 0;
    public static final int URI_ACCOUNT_ID = 1;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CurrencyDatabaseHelper.CURRENCY_TABLE_NAME, URI_CURRENCY_ID);
        uriMatcher.addURI(AUTHORITY, CurrencyDatabaseHelper.ACCOUNT_TABLE_NAME, URI_ACCOUNT_ID);
    }

    private CurrencyDatabaseHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new CurrencyDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Log.d("query", "start");
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                Cursor cursor = dbHelper.getReadableDatabase().query(CurrencyDatabaseHelper.CURRENCY_TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), CURRENCY_CONTENT_URI);
                return cursor;
            case URI_ACCOUNT_ID:
                cursor = dbHelper.getReadableDatabase().query(CurrencyDatabaseHelper.ACCOUNT_TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), ACCOUNT_CONTENT_URI);
                return cursor;
            default:
                throw new IllegalArgumentException("wrong URI");
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                return CURRENCY_CONTENT_TYPE;
            case URI_ACCOUNT_ID:
                return ACCOUNT_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("wrong URI");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                long id = dbHelper.getWritableDatabase().insert(CurrencyDatabaseHelper.CURRENCY_TABLE_NAME, null, contentValues);
                Uri result = ContentUris.withAppendedId(CURRENCY_CONTENT_URI, id);
                getContext().getContentResolver().notifyChange(result, null);
                return result;
            case URI_ACCOUNT_ID:
                id = dbHelper.getWritableDatabase().insert(CurrencyDatabaseHelper.ACCOUNT_TABLE_NAME, null, contentValues);
                result = ContentUris.withAppendedId(ACCOUNT_CONTENT_URI, id);
                getContext().getContentResolver().notifyChange(result, null);
                return result;
            default:
                throw new IllegalArgumentException("wrong URI");
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                int count = dbHelper.getWritableDatabase().delete(CurrencyDatabaseHelper.CURRENCY_TABLE_NAME, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            case URI_ACCOUNT_ID:
                count = dbHelper.getWritableDatabase().delete(CurrencyDatabaseHelper.ACCOUNT_TABLE_NAME, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("wrong URI");
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        switch (uriMatcher.match(uri)) {
            case URI_CURRENCY_ID:
                int count = dbHelper.getWritableDatabase().update(CurrencyDatabaseHelper.CURRENCY_TABLE_NAME, contentValues, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            case URI_ACCOUNT_ID:
                count = dbHelper.getWritableDatabase().update(CurrencyDatabaseHelper.ACCOUNT_TABLE_NAME, contentValues, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("wrong URI");
        }
    }
}
