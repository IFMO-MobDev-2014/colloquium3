package ru.eugene.coloqq3.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by eugene on 12/23/14.
 */
public class BlackProvider extends ContentProvider {
    DbHelper dbHelper;
    SQLiteDatabase db;

    public static final String AUTHORITY = "ru.eugene.coloqq3.db";
    public static final String PATH_MONEY = "path_money";
    public static final String PATH_COUNT = "path_count";
    public static final Uri CONTENT_URI_MONEY = Uri.parse("content://" + AUTHORITY + "/" + PATH_MONEY);
    public static final Uri CONTENT_URI_COUNT = Uri.parse("content://" + AUTHORITY + "/" + PATH_COUNT);

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int MONEY = 10;
    public static final int COUNT = 20;

    static {
        sUriMatcher.addURI(AUTHORITY, PATH_MONEY, MONEY);
        sUriMatcher.addURI(AUTHORITY, PATH_COUNT, COUNT);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = dbHelper.getReadableDatabase();
        Cursor result = db.query(getTable(uri), projection, selection, selectionArgs, null, null, sortOrder);
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long resultId = database.insert(getTable(uri), null, values);
        if (resultId > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            uri = ContentUris.withAppendedId(uri, resultId);
        }
//        database.close();
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int resultId = database.delete(getTable(uri), selection, selectionArgs);
        if (resultId > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
//        database.close();
        return resultId;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int resultId = database.update(getTable(uri), values, selection, selectionArgs);
        if (resultId > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
//        database.close();
        return resultId;
    }

    private String getTable(Uri uri) {
        if (sUriMatcher.match(uri) == MONEY) {
            return MoneyDataSource.TABLE;
        } else {
            return CountDataSource.TABLE;
        }
    }
}
