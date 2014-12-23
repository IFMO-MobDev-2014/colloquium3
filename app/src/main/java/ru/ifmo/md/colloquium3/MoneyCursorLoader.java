package ru.ifmo.md.colloquium3;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

/**
 * Created by sultan on 23.12.14.
 */
public class MoneyCursorLoader extends CursorLoader {
    public MoneyCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return getContext().getContentResolver().query(MoneyProvider.MONEY1_URI, null, DBMoney.CURRENCY1 + " <> ?", new String[]{"RUB"}, null);
    }
}
