package ru.ifmo.md.colloquium3;

        import android.content.ContentProvider;
        import android.content.ContentValues;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.net.Uri;

public class MoneyProvider extends ContentProvider {
    private static String AUTHORITY = "ru.ifmo.md.colloqium3.moneyProvider";

    public static final Uri MONEY1_URI = Uri.parse("content://" + AUTHORITY + "/money1");

    private DBMoney dbWeather;

    @Override
    public boolean onCreate() {
        dbWeather = new DBMoney(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbWeather.getReadableDatabase();
        return db.query(uri.getLastPathSegment(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbWeather.getWritableDatabase();
        String tableName = uri.getLastPathSegment();
        long id = db.insert(tableName, null, values);
        return Uri.parse("content://" + AUTHORITY + "/" + tableName + "/" + Long.toString(id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbWeather.getWritableDatabase();
        return db.delete(uri.getLastPathSegment(), selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbWeather.getWritableDatabase();
        int cnt = db.update(uri.getLastPathSegment(), values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(MONEY1_URI, null);
        return cnt;
    }
}
