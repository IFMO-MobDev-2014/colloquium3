package ru.ifmo.md.colloquium3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by MSviridenkov on 23.12.2014.
 */
public class MyContentProvider extends ContentProvider {
    private static String AUTHORITY = "ru.ifmo.md.colloquium3.MyContentProvider";

    public static final Uri COURSE_CONTENT_URL = Uri.parse("content://" + AUTHORITY + "/courses");

    private DBHelper mDbHelper;

    private String getTableName(Uri uri) {

        return getTableName(uri.getLastPathSegment());
    }

    private String getTableName(String type) {
        String tableName;
        if(type.equals("courses")) {
            tableName = mDbHelper.TABLE_COURSE;
        } else {
            throw new UnsupportedOperationException("Invalid data type");
        }
        return tableName;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(getTableName(uri), selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tableName = getTableName(uri);
        long id = db.insert(tableName, null, values);
        return Uri.parse("content://" + AUTHORITY + "/" + tableName + "/" + Long.toString(id));
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        return db.query(getTableName(uri), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(getTableName(uri), values, selection, selectionArgs);
    }
}
