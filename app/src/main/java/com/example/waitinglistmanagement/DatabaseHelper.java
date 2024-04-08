package com.example.waitinglistmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "waitlist.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WaitlistContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(WaitlistContract.SQL_DELETE_TABLE);
        onCreate(db);
    }

    public long addStudent(String name, String course, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_NAME, name);
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_COURSE, course);
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_PRIORITY, priority);
        long newRowId = db.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WaitlistContract.WaitlistEntry.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID));
                String name = cursor.getString(cursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_NAME_NAME));
                String course = cursor.getString(cursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_NAME_COURSE));
                String priority = cursor.getString(cursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_NAME_PRIORITY));
                studentList.add(new Student(id, name, course, priority));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return studentList;
    }

    public int updateStudent(long id, String name, String course, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_NAME, name);
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_COURSE, course);
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_PRIORITY, priority);
        int rowsAffected = db.update(WaitlistContract.WaitlistEntry.TABLE_NAME, values,
                WaitlistContract.WaitlistEntry._ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public int deleteStudent(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(WaitlistContract.WaitlistEntry.TABLE_NAME,
                WaitlistContract.WaitlistEntry._ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return deletedRows;
    }
}
