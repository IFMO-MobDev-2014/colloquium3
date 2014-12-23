package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    Context context;

    public DataBaseHelper(Context context) {
        super(context, "database", null, 1);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE channel (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, url TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE news (_id INTEGER PRIMARY KEY AUTOINCREMENT, channel_id INTEGER NOT NULL, title TEXT, description TEXT, url TEXT, time INTEGER NOT NULL, FOREIGN KEY (channel_id ) REFERENCES channel (_id) ON DELETE CASCADE, UNIQUE (url) ON CONFLICT IGNORE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}