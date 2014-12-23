package ru.eugene.coloqq3.db;

/**
 * Created by eugene on 12/23/14.
 */
public class MoneyDataSource {
    public static final String TABLE = "money";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COURSE = "cur_course";

    public static final String CREATE_TABLE = "create table " + TABLE +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
            " ," + COLUMN_NAME + " TEXT NOT NULL" +
            " ," + COLUMN_COURSE + " REAL NOT NULL);";

    public static String[] getProjection() {
        return new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_COURSE};
    }
}
