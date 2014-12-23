package ru.ifmo.md.colloquium3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    public static final String DATABASE_NAME =  "walletase.db";
    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CURRENT_COURCE = "channel_id";
    public static final String COLUMN_COURCE_NAME = "title";
    public static final String COLUMN_COUNT = "summary";
    public static final int DATABASE_VERSION = 1;
    public static final String AUTHORITY = "ru.ifmo.md.colloquium3";


    public static final String CREATE_TABLE_COURCES = "CREATE TABLE "
            + TABLE_COURSES + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_COURCE_NAME + " TEXT NOT NULL, "
            + COLUMN_CURRENT_COURCE + " REAL, "
            + COLUMN_COUNT + " INTEGER "
            +");";


    public static final Uri TABLE_COURCES_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TABLE_COURSES);

    static final int COURCES = 1;
    static final int COURCES_ID = 3;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, TABLE_COURSES + "/#", COURCES_ID);
        uriMatcher.addURI(AUTHORITY, TABLE_COURSES, COURCES);
    }

    MySQLiteHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new MySQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        SQLiteQueryBuilder sqB = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case COURCES:
                sqB.setTables(TABLE_COURSES);
                break;
            case COURCES_ID:
                sqB.setTables(TABLE_COURSES);
                sqB.appendWhere(COLUMN_CURRENT_COURCE +"="+uri.getLastPathSegment());
                break;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = sqB.query(db, strings, s, strings2, null, null, s2);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return "";
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase sqlQB = dbHelper.getWritableDatabase();
        long insertedID=1;
        switch (uriMatcher.match(uri)) {
            case COURCES:
                insertedID = sqlQB.insert(TABLE_COURSES, null, contentValues);
                break;
            case COURCES_ID:
                contentValues.put(COLUMN_CURRENT_COURCE, uri.getLastPathSegment());
                insertedID = sqlQB.insert(TABLE_COURSES, null, contentValues);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, Long.toString(insertedID));
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase sqlQB = dbHelper.getWritableDatabase();
        int result = 0;
        switch (uriMatcher.match(uri)) {
            case COURCES_ID:
                String ending = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    result = sqlQB.delete(TABLE_COURSES, COLUMN_ID + " = "+ ending, null);
                }
                else {
                    result = sqlQB.delete(TABLE_COURSES, COLUMN_ID + " = "+ ending + " and " + s, strings);
                }
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase sqlQB = dbHelper.getWritableDatabase();
        int result = 0;
        String ending;
        switch (uriMatcher.match(uri)) {
            case COURCES_ID:
                ending = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    result = sqlQB.update(TABLE_COURSES, contentValues, COLUMN_ID + " = "+ ending, null);
                }
                else {
                    result = sqlQB.update(TABLE_COURSES, contentValues, COLUMN_ID + " = "+ ending + " and " + s, strings);
                }
                break;
            case COURCES:
                if (TextUtils.isEmpty(s)) {
                    result = sqlQB.update(TABLE_COURSES, contentValues, null, null);
                }
                else {
                    result = sqlQB.update(TABLE_COURSES, contentValues, s, strings);
                }
                break;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    public class MySQLiteHelper extends SQLiteOpenHelper {

        public MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(CREATE_TABLE_COURCES);
            Log.d("DATABASE", "added");
            database.execSQL("insert into " + TABLE_COURSES +" ("+ COLUMN_COURCE_NAME +", " + COLUMN_CURRENT_COURCE + ") values (?, ?)", new String[]{"USD", "54.53"});
            database.execSQL("insert into " + TABLE_COURSES +" ("+ COLUMN_COURCE_NAME +", " + COLUMN_CURRENT_COURCE + ") values (?, ?)", new String[]{"EUR", "65"});
            database.execSQL("insert into " + TABLE_COURSES +" ("+ COLUMN_COURCE_NAME +", " + COLUMN_CURRENT_COURCE + ") values (?, ?)", new String[]{"GBP", "75"});
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MySQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
            onCreate(db);
        }
    }

}