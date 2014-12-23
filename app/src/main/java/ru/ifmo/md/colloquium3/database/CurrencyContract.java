package ru.ifmo.md.colloquium3.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Nikita Yaschenko on 23.12.14.
 */
public class CurrencyContract {

    interface CurrencyColumns {
        String CURRENCY_NAME = "currency_name";
        String CURRENCY_CNT = "currency_cnt";
    }

    public static final String CONTENT_AUTHORITY = "ru.ifmo.md.colloquium3";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_CURRENCY = "currency";

    public static class Currency implements BaseColumns, CurrencyColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_CURRENCY).build();

        public static final String[] CNT_COLUMNS = {
                BaseColumns._ID,
                CURRENCY_CNT
        };

        public static Uri buildCurrencyUri(String currencyId) {
            return CONTENT_URI.buildUpon().appendPath(currencyId).build();
        }
    }

}
