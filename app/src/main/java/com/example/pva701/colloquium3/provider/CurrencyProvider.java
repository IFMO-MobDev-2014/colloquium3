package com.example.pva701.colloquium3.provider;

/**
 * Created by pva701 on 23.12.14.
 */
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class CurrencyProvider extends ContentProvider {
    public static final String LOG_TAG = "CurrencyProvider";
    public static final String AUTHORITY = "com.example.pva701.colloquium3.provider.CurrencyProvider";
    public static final String COURSE_PATH = CurrencyDbHelper.TABLE_COURSE;
    public static final String TOTAL_PATH = CurrencyDbHelper.TABLE_TOTAL;
    public static final String ADD_COURSE_PATH = "add_course";

    public static final Uri COURSE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_PATH);
    public static final Uri TOTAL_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TOTAL_PATH);
    public static final Uri ADD_COURSE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ADD_COURSE_PATH);

    public static final int URI_COURSE_ID = 1;
    public static final int URI_TOTAL_ID = 2;
    public static final int ADD_URI_COURSE_ID = 3;

    static final String CITY_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + COURSE_PATH;
    static final String FORECAST_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + COURSE_PATH;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH, URI_COURSE_ID);
        uriMatcher.addURI(AUTHORITY, TOTAL_PATH, URI_TOTAL_ID);
        uriMatcher.addURI(AUTHORITY, ADD_COURSE_PATH, ADD_URI_COURSE_ID);
    }
    private CurrencyDbHelper dbHelper;

    public CurrencyProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cnt;
        if (uriMatcher.match(uri) == URI_COURSE_ID)
            cnt = dbHelper.getWritableDatabase().delete(CurrencyDbHelper.TABLE_COURSE, selection, selectionArgs);
        else
            cnt = dbHelper.getWritableDatabase().delete(CurrencyDbHelper.TABLE_TOTAL, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public String getType(Uri uri) {
        //Log.i("RSSContentProvider", "getType");
        if (uriMatcher.match(uri) == URI_COURSE_ID)
            return CITY_CONTENT_TYPE;
        else if (uriMatcher.match(uri) == URI_TOTAL_ID)
            return FORECAST_CONTENT_TYPE;
        throw new RuntimeException("incorrect getType");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //Log.d(LOG_TAG, "insert " + uri.toString());
        int m = uriMatcher.match(uri);
        Uri resultUri;
        if (m == URI_COURSE_ID) {
            long rowID = dbHelper.getWritableDatabase().insert(CurrencyDbHelper.TABLE_COURSE, null, values);
            resultUri = ContentUris.withAppendedId(COURSE_CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(resultUri, null);
        } else if (m == URI_TOTAL_ID) {
            long rowID = dbHelper.getWritableDatabase().insert(CurrencyDbHelper.TABLE_TOTAL, null, values);
            resultUri = ContentUris.withAppendedId(TOTAL_CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(resultUri, null);
        } else
            throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        return resultUri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new CurrencyDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //Log.i(LOG_TAG, "query, " + uri.toString());
        int m = uriMatcher.match(uri);
        Cursor cursor;
        if (m == URI_COURSE_ID) {
            cursor = dbHelper.getReadableDatabase().query(CurrencyDbHelper.TABLE_COURSE, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), COURSE_CONTENT_URI);
        } else if (m == URI_TOTAL_ID) {
            cursor = dbHelper.getReadableDatabase().query(CurrencyDbHelper.TABLE_TOTAL, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), TOTAL_CONTENT_URI);
        } else
            throw new IllegalArgumentException("Wrong URI: " + uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int cnt = 1;
        if (uriMatcher.match(uri) == URI_COURSE_ID)
            cnt = dbHelper.getWritableDatabase().update(CurrencyDbHelper.TABLE_COURSE, values, selection, selectionArgs);
        else if (uriMatcher.match(uri) == ADD_URI_COURSE_ID) {
            dbHelper.getWritableDatabase().execSQL("UPDATE " + CurrencyDbHelper.TABLE_COURSE +
                    " SET " + CurrencyDbHelper.COURSE_VAL + " + " + values.get("add") + " WHERE " + selection);
        } else
            cnt = dbHelper.getWritableDatabase().update(CurrencyDbHelper.TABLE_TOTAL, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
