package ru.ifmo.colloquium3.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
public class MyContentProvider extends ContentProvider {
    public static final int URI_WALLETS = 1;
    public static final int URI_WALLET_ID = 2;
    public static final int URI_ALL_MONEY = 3;
    public static final int URI_MONEY_ID = 4;
    public static final String AUTHORITY = "ru.ifmo.colloquium3.contentprovider";
    public static final String WALLETS_PATH = "wallets";
    public static final String MONEY_PATH = "money";
    public static final Uri WALLETS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + WALLETS_PATH);
    public static final Uri MONEY_URI = Uri.parse("content://" + AUTHORITY
            + "/" + MONEY_PATH);
    public static final String WALLETS_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + WALLETS_PATH;
    public static final String WALLET_CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + WALLETS_PATH;
    public static final String ALL_MONEY_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + MONEY_PATH;
    public static final String MONEY_CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + MONEY_PATH;
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, WALLETS_PATH, URI_WALLETS);
        uriMatcher.addURI(AUTHORITY, WALLETS_PATH + "/#", URI_WALLET_ID);
        uriMatcher.addURI(AUTHORITY, MONEY_PATH, URI_ALL_MONEY);
        uriMatcher.addURI(AUTHORITY, MONEY_PATH + "/#", URI_MONEY_ID);
    }

    private DatabaseHelper dbHandler;

    @Override
    public boolean onCreate() {
        dbHandler = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case URI_WALLETS:
                cursor = db.query(DatabaseHelper.WALLETS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), WALLETS_URI);
                break;
            case URI_WALLET_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = "";
                } else {
                    selection += " and ";
                }
                selection += DatabaseHelper.ID_KEY + " = " + uri.getLastPathSegment();
                cursor = db.query(DatabaseHelper.WALLETS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), WALLETS_URI);
                break;
            case URI_ALL_MONEY:
                cursor = db.query(DatabaseHelper.MONEY_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), MONEY_URI);
                break;
            case URI_MONEY_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = "";
                } else {
                    selection += " and ";
                }
                selection += DatabaseHelper.ID_KEY + " = " + uri.getLastPathSegment();
                cursor = db.query(DatabaseHelper.MONEY_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), MONEY_URI);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_WALLETS:
                return WALLETS_CONTENT_TYPE;
            case URI_WALLET_ID:
                return WALLET_CONTENT_TYPE;
            case URI_ALL_MONEY:
                return ALL_MONEY_CONTENT_TYPE;
            case URI_MONEY_ID:
                return MONEY_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        try {
            long id;
            switch (uriType) {
                case URI_WALLETS:
                    id = db.insert(DatabaseHelper.WALLETS_TABLE, null, values);
                    return Uri.parse(WALLETS_PATH + "/" + id);
                case URI_ALL_MONEY:
                    id = db.insert(DatabaseHelper.MONEY_TABLE, null, values);
                    return Uri.parse(MONEY_PATH + "/" + id);
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        } finally {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case URI_WALLETS:
                rowsDeleted = db.delete(DatabaseHelper.WALLETS_TABLE, selection,
                        selectionArgs);
                break;
            case URI_WALLET_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = "";
                } else {
                    selection += " and ";
                }
                selection += DatabaseHelper.ID_KEY + "=" + uri.getLastPathSegment();
                rowsDeleted = db.delete(DatabaseHelper.WALLETS_TABLE,
                        selection,
                        null);
                break;
            case URI_MONEY_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = "";
                } else {
                    selection += " and ";
                }
                selection += DatabaseHelper.ID_KEY + "=" + uri.getLastPathSegment();
                rowsDeleted = db.delete(DatabaseHelper.MONEY_TABLE,
                        selection,
                        null);
                break;
            case URI_ALL_MONEY:
                rowsDeleted = db.delete(DatabaseHelper.MONEY_TABLE, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        int rowsUpdated;
        switch (uriType) {
            case URI_WALLETS:
                rowsUpdated = db.update(DatabaseHelper.WALLETS_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case URI_WALLET_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = "";
                } else {
                    selection += " and ";
                }
                selection += DatabaseHelper.ID_KEY + "=" + uri.getLastPathSegment();
                rowsUpdated = db.update(DatabaseHelper.WALLETS_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case URI_ALL_MONEY:
                rowsUpdated = db.update(DatabaseHelper.MONEY_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}