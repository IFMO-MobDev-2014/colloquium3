package ru.ifmo.md.colloquium3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Амир on 23.12.2014.
 */
public class DBContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.ifmo.md.colloquium3.DBContentProvider";

    public static final Uri CURRENCY = Uri.parse("content://" + AUTHORITY + "/CURRENCY");

    DBHelper dataBaseHelper;

    @Override
    public boolean onCreate() {
        dataBaseHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortingOrder) {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor result = db.query(uri.getLastPathSegment(), projection, selection, selectionArgs, null, null, sortingOrder, null);
        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        long id = db.insert(uri.getLastPathSegment(), null, contentValues);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int result = db.delete(uri.getLastPathSegment(), selection, selectionArgs);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int result = db.update(uri.getLastPathSegment(), contentValues, selection, selectionArgs);
        return result;
    }
}