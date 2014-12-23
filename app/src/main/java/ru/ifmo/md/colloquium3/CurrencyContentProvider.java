package ru.ifmo.md.colloquium3;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Евгения on 23.12.2014.
 */
public class CurrencyContentProvider extends ContentProvider {
    // used for the UriMacher
    private static final int CURRENCY = 10;

    public static final String AUTHORITY = "ru.ifmo.md.colloquium3.currency";

    private static final String BASE_CURRENCY = "currency";

    public static final Uri CURRENCY_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_CURRENCY);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/feeds";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/valuta";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_CURRENCY, CURRENCY);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);
        if (uriType == CURRENCY) {
            Cursor result = DBAdapter.getOpenedInstance(getContext()).getSnapshots();
            if (result != null)
                result.setNotificationUri(getContext().getContentResolver(), uri);
            return result;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        if (uriType == CURRENCY) {
            DBAdapter.getOpenedInstance(getContext()).saveSnapshot(values);
            getContext().getContentResolver().notifyChange(uri, null);
            return uri;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        if (uriType == CURRENCY) {
            DBAdapter.getOpenedInstance(getContext()).clearSnapshots();
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


}
