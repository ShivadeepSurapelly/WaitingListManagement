package com.example.waitinglistmanagement;

import android.provider.BaseColumns;

public final class WaitlistContract {
    private WaitlistContract() {}

    public static class WaitlistEntry implements BaseColumns {
        public static final String TABLE_NAME = "students";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COURSE = "course";
        public static final String COLUMN_NAME_PRIORITY = "priority";
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + WaitlistEntry.TABLE_NAME + " (" +
                    WaitlistEntry._ID + " INTEGER PRIMARY KEY," +
                    WaitlistEntry.COLUMN_NAME_NAME + " TEXT," +
                    WaitlistEntry.COLUMN_NAME_COURSE + " TEXT," +
                    WaitlistEntry.COLUMN_NAME_PRIORITY + " TEXT)";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + WaitlistEntry.TABLE_NAME;
}
