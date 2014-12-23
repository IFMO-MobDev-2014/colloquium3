package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBase {
    public static DataBase dataBase = null;
    private Context context;
    public SQLiteDatabase sqLiteDatabase;

    private DataBase(Context context) {
        this.context = context;
    }

    public static DataBase getDataBase(Context context) {
        if (dataBase == null)
            dataBase = new DataBase(context.getApplicationContext()).open();
        return dataBase;
    }

    private DataBase open() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public String getUrlByChannelId(long channelId) {
        Cursor c = sqLiteDatabase.query("channel", new String[]{"url"}, "_id=" + channelId, null, null, null, null);
        if (c.getCount() != 0) {
            c.moveToFirst();
            String result = c.getString(c.getColumnIndex("url"));
            c.close();
            return result;
        } else {
            c.close();
            return null;

        }
    }

}
