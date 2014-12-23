package ru.ifmo.md.colloquium3;


import android.net.Uri;
import android.provider.BaseColumns;

public class Currency {
    public static final String AUTHORITY =
            "ru.ifmo.md.colloquium3.provider.currency";

    public static final class SimpleCurrency implements BaseColumns {
        public static final int ID_COLUMN = 0;
        public static final int CUR_COLUMN = 1;
        public static final int RATE_COLUMN = 2;

        private SimpleCurrency() {}

        public static final Uri CURRENCY_URI = Uri.parse("content://" +
                AUTHORITY + "/" + SimpleCurrency.CURRENCY_NAME);

        public static final Uri CONTENT_URI = CURRENCY_URI;

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.currency.data";

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.currency.data";

        public static final String CURRENCY_NAME = "currency";

        public static final String CUR_NAME = "cur";

        public static final String RATE_NAME = "rate";
    }
}
