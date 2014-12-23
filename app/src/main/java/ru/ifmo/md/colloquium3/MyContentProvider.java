package ru.ifmo.md.colloquium3;

/**
 * Created by 107476 on 23.12.2014.
 */
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    final String LOG_TAG = "myLogs";

    // // Константы для БД
    // БД
    static final String DB_NAME = "mydb";
    static final int DB_VERSION = 1;

    // Таблица
    static final String CURR_TABLE = "currency";

    // Поля
    static final String CURR_ID = "_id";
    static final String CURR_NAME = "name";
    static final String CURR_RATE = "rate";
    static final String CURR_VALUE = "value";

    // Скрипт создания таблицы
    static final String DB_CREATE = "create table " + CURR_TABLE + "("
            + CURR_ID + " integer primary key autoincrement, "
            + CURR_NAME + " text, " + CURR_RATE + " text," + CURR_VALUE + " text" + ");";


    static final String AUTHORITY = "ru.ifmo.md.colloquium3.provider";

    static final String CURR_PATH = "currency";

    public static final Uri CURR_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + CURR_PATH);

    static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CURR_PATH;

    static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + CURR_PATH;

    static final int URI_CURR = 1;

    static final int URI_CURR_ID = 2;


    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CURR_PATH, URI_CURR);
        uriMatcher.addURI(AUTHORITY, CURR_PATH + "/#", URI_CURR_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    // чтение
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_CURR:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = CURR_NAME + " ASC";
                }
                break;
            case URI_CURR_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = CURR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CURR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CURR_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                CURR_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_CURR)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(CURR_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CURR_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CURR:
                Log.d(LOG_TAG, "URI_CONTACTS");
                break;
            case URI_CURR_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = CURR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CURR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(CURR_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CURR:
                Log.d(LOG_TAG, "URI_CONTACTS");

                break;
            case URI_CURR_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = CURR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CURR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(CURR_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CURR:
                return CONTACT_CONTENT_TYPE;
            case URI_CURR_ID:
                return CONTACT_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            ContentValues cv = new ContentValues();
            for (int i = 1; i <= 3; i++) {
                cv.put(CURR_NAME, "name " + i);
                cv.put(CURR_RATE, "email " + i);
                cv.put(CURR_VALUE, "value" + i);
                db.insert(CURR_TABLE, null, cv);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
