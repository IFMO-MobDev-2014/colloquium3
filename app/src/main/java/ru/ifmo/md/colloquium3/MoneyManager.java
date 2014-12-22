package ru.ifmo.md.colloquium3;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by german on 22.12.14.
 */
public class MoneyManager {
    public static void addValuta(SQLiteDatabase db, String name, double value) {
        if (db.query(
                MoneyContentProvider.VALUTAS_TABLE,
                null, MoneyContentProvider.VALUTA_NAME + " = ? ",
                new String[] {
                        name
                }, null, null, null).getCount() != 0) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(MoneyContentProvider.VALUTA_NAME, name);
        cv.put(MoneyContentProvider.VALUTA_VALUE, value);
        cv.put(MoneyContentProvider.VALUTA_BALANCE, 0);
        db.insert(MoneyContentProvider.VALUTAS_TABLE, null, cv);
    }

    public static void addValuta(SQLiteDatabase db, String name, double value, double balance) {
        if (db.query(
                MoneyContentProvider.VALUTAS_TABLE,
                null, MoneyContentProvider.VALUTA_NAME + " = ? ",
                new String[] {
                        name
                }, null, null, null).getCount() != 0) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(MoneyContentProvider.VALUTA_NAME, name);
        cv.put(MoneyContentProvider.VALUTA_VALUE, value);
        cv.put(MoneyContentProvider.VALUTA_BALANCE, balance);
        db.insert(MoneyContentProvider.VALUTAS_TABLE, null, cv);
    }

    public static Cursor getAllData(ContentResolver resolver) {
        return resolver.query(MoneyContentProvider.VALUTAS_CONTENT, null, null, null, null);
    }

    public static double getValue(ContentResolver resolver, String name) {
        Cursor cursor = resolver.query(
                MoneyContentProvider.VALUTAS_CONTENT,
                new String[] {
                        MoneyContentProvider.VALUTA_VALUE
                },
                MoneyContentProvider.VALUTA_NAME + " = ? ",
                new String[] {
                        name
                },
                null
        );
        cursor.moveToFirst();
        double value = cursor.getDouble(cursor.getColumnIndexOrThrow(MoneyContentProvider.VALUTA_VALUE));
        cursor.close();
        return value;
    }

    public static double getBalance(ContentResolver resolver, String name) {
        Cursor cursor = resolver.query(
                MoneyContentProvider.VALUTAS_CONTENT,
                new String[] {
                        MoneyContentProvider.VALUTA_BALANCE
                },
                MoneyContentProvider.VALUTA_NAME + " = ? ",
                new String[] {
                        name
                },
                null
        );
        cursor.moveToFirst();
        double value = cursor.getDouble(cursor.getColumnIndexOrThrow(MoneyContentProvider.VALUTA_BALANCE));
        cursor.close();
        return value;
    }

    public static boolean decreaseBalance(ContentResolver resolver, String name, double dec) {
        Cursor cursor = resolver.query(
                MoneyContentProvider.VALUTAS_CONTENT,
                new String[] {
                        MoneyContentProvider.VALUTA_BALANCE
                },
                MoneyContentProvider.VALUTA_NAME + " = ? ",
                new String[] {
                        name
                },
                null
        );
        cursor.moveToFirst();
        double curBalance = cursor.getDouble(cursor.getColumnIndexOrThrow(MoneyContentProvider.VALUTA_BALANCE));
        cursor.close();
        if (curBalance - dec < 0) {

            return false;
        }
        curBalance -= dec;
        ContentValues cv = new ContentValues();
        cv.put(MoneyContentProvider.VALUTA_NAME, name);
        cv.put(MoneyContentProvider.VALUTA_BALANCE, curBalance);
        cv.put(MoneyContentProvider.VALUTA_VALUE, getValue(resolver, name));
        resolver.update(
                MoneyContentProvider.VALUTAS_CONTENT,
                cv,
                MoneyContentProvider.VALUTA_NAME + " = ? ",
                new String[] {
                        name
                }
        );
        return true;
    }

    public static void increaseBalance(ContentResolver resolver, String name, double inc) {
        Cursor cursor = resolver.query(
                MoneyContentProvider.VALUTAS_CONTENT,
                new String[] {
                        MoneyContentProvider.VALUTA_BALANCE
                },
                MoneyContentProvider.VALUTA_NAME + " = ? ",
                new String[] {
                        name
                },
                null
        );
        cursor.moveToFirst();
        double curBalance = cursor.getDouble(cursor.getColumnIndexOrThrow(MoneyContentProvider.VALUTA_BALANCE));
        cursor.close();
        curBalance += inc;
        ContentValues cv = new ContentValues();
        cv.put(MoneyContentProvider.VALUTA_NAME, name);
        cv.put(MoneyContentProvider.VALUTA_BALANCE, curBalance);
        cv.put(MoneyContentProvider.VALUTA_VALUE, getValue(resolver, name));
        resolver.update(
                MoneyContentProvider.VALUTAS_CONTENT,
                cv,
                MoneyContentProvider.VALUTA_NAME + " = ? ",
                new String[] {
                        name
                }
        );
    }
}
