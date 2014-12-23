package ru.ifmo.colloquium3;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;

import java.util.Random;

import ru.ifmo.colloquium3.db.DatabaseHelper;
import ru.ifmo.colloquium3.db.MyContentProvider;

/**
 * @author Zakhar Voit (zakharvoit@gmail.com)
 */
public class CourseServer extends IntentService {
    public CourseServer() {
        super("ru.ifmo.colloquium3.Courser");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Random random = new Random();
        while (true) { // TODO: Make it better
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {

            }
            String[] projection = {
                    DatabaseHelper.ID_KEY,
                    DatabaseHelper.WALLET_NAME_KEY,
                    DatabaseHelper.WALLET_VALUE_KEY
            };
            Cursor cursor = getContentResolver().query(MyContentProvider.WALLETS_URI,
                    projection, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    ContentValues contentValues = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
                    double newValue = contentValues.getAsDouble(DatabaseHelper.WALLET_VALUE_KEY);
                    int id = contentValues.getAsInteger(DatabaseHelper.ID_KEY);
                    double delta = random.nextDouble() / 10;
                    if (random.nextBoolean()) {
                        delta *= -1;
                    }
                    newValue += delta;
                    contentValues.put(DatabaseHelper.WALLET_VALUE_KEY, newValue);
                    String selection = DatabaseHelper.ID_KEY + " = ?";
                    String[] args = { id + "" };
                    getContentResolver().update(MyContentProvider.WALLETS_URI, contentValues, selection, args);
                } while (cursor.moveToNext());
            }
        }
    }
}
