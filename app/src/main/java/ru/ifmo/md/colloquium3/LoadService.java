package ru.ifmo.md.colloquium3;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Created by Амир on 23.12.2014.
 */
public class LoadService extends IntentService {

    public static final String ON_LOAD_FINISHED_BROADCAST = "Load finished";
    public static final String ON_LOAD_FAILED_BROADCAST = "Load failed";

    public LoadService() {
        super("LoadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor cursor = getContentResolver().query(DBContentProvider.CURRENCY, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<Item> items = new ArrayList<Item>();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.CHANNELS_COLUMN_NAME));
            int cost = cursor.getInt(cursor.getColumnIndex(DBHelper.CHANNELS_COLUMN_COST));
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.CHANNELS_COLUMN_ID));
            cursor.moveToNext();
            items.add(new Item(name, cost));
        }
        cursor.close();
        Intent intentResponse = new Intent();
    }
}
