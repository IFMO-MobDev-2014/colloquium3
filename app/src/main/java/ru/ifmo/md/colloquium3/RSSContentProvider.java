package ru.ifmo.md.colloquium3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class RSSContentProvider extends ContentProvider {

    private DataBase database;

    private static final int newsCount;
    private static final int channelCount;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        newsCount = 20;
        channelCount = 10;
        uriMatcher.addURI("ru.ifmo.md.colloquium3.feeds", "channels", channelCount);
        uriMatcher.addURI("ru.ifmo.md.colloquium3.feeds", "news/#", newsCount);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == channelCount) {
            if (database.sqLiteDatabase.delete("channel", "_id=" + (Long.parseLong(selection)), null) == 1) {
                getContext().getContentResolver().notifyChange(uri, null);
                return 1;
            } else {
                getContext().getContentResolver().notifyChange(uri, null);
                return 0;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long number = -1;
        Cursor cursor = database.sqLiteDatabase.query("channel", new String[]{"_id", "name", "url"}, null, null, null, null, null);
        cursor.moveToLast();
        if (cursor.getCount() == 3)
            return Uri.parse(uri.toString() + "/" + 3);

        if (uriMatcher.match(uri) == newsCount) {
            number = database.sqLiteDatabase.insert("news", null, contentValues);
            getContext().getContentResolver().notifyChange(Uri.parse(Uri.parse("content://ru.ifmo.md.colloquium3.feeds/channels") + "/" + contentValues.getAsLong("channel_id")), null);
        } else if (uriMatcher.match(uri) == channelCount) {
            number = database.sqLiteDatabase.insert("channel", null, contentValues);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(uri.toString() + "/" + number);
    }

    @Override
    public boolean onCreate() {
        database = DataBase.getDataBase(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        if (uriMatcher.match(uri) == newsCount) {
            cursor = database.sqLiteDatabase.query("news", new String[]{"_id", "url", "description", "title"}, "channel_id=" + Long.parseLong(uri.getLastPathSegment()), null, null, null, "time DESC");
        } else if (uriMatcher.match(uri) == channelCount) {
            cursor = database.sqLiteDatabase.query("channel", new String[]{"_id", "name", "url"}, null, null, null, null, null);
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) != channelCount)
            throw new UnsupportedOperationException();
        if (database.sqLiteDatabase.update("channel", contentValues, "_id=" + contentValues.getAsLong("_id"), null) == 1) {
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        } else {
            getContext().getContentResolver().notifyChange(uri, null);
            return 0;
        }

    }
}
