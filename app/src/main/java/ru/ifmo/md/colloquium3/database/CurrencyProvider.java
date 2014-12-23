package ru.ifmo.md.colloquium3.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import static ru.ifmo.md.colloquium3.database.CurrencyContract.Currency;
import static ru.ifmo.md.colloquium3.database.CurrencyDatabase.Tables;

public class CurrencyProvider extends ContentProvider {

    private CurrencyDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildMatcher();

    private static final int CURRENCIES = 100;
    private static final int CURRENCY_ID = 101;

    private static UriMatcher buildMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CurrencyContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "currency", CURRENCIES);
        matcher.addURI(authority, "currency/#", CURRENCY_ID);

        return matcher;
    }

    public CurrencyProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final long id;

        switch (match) {
            case CURRENCIES:
                id = db.insert(Tables.CURRENCIES, null, values);
                notifyChange(uri);
                return CurrencyContract.Currency.buildCurrencyUri(Long.toString(id));
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CurrencyDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (match) {
            case CURRENCIES:
                builder.setTables(Tables.CURRENCIES);
                break;
            case CURRENCY_ID:
                builder.setTables(Tables.CURRENCIES);
                builder.appendWhere(Currency._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int rowsUpdated;

        switch (match) {
            case CURRENCY_ID:
                rowsUpdated = db.update(Tables.CURRENCIES, values, Currency._ID + " = " + uri.getLastPathSegment(), selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        notifyChange(Currency.CONTENT_URI);
        return rowsUpdated;
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }
}
