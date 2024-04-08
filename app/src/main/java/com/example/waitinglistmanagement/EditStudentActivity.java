package com.example.waitinglistmanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditStudentActivity extends AppCompatActivity {

    private EditText editTextName, editTextCourse;
    private Spinner spinnerPriority;
    private DatabaseHelper dbHelper;
    private long studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        dbHelper = new DatabaseHelper(this);

        editTextName = findViewById(R.id.editTextName);
        editTextCourse = findViewById(R.id.editTextCourse);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        Button buttonSave = findViewById(R.id.buttonSave);

        // Get the student ID passed from the intent
        studentId = getIntent().getLongExtra("student_id", -1);

        if (studentId == -1) {
            // Invalid student ID, finish the activity
            Toast.makeText(this, "Invalid student ID", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            loadStudentData();
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });
    }

    private void loadStudentData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                WaitlistContract.WaitlistEntry.COLUMN_NAME_NAME,
                WaitlistContract.WaitlistEntry.COLUMN_NAME_COURSE,
                WaitlistContract.WaitlistEntry.COLUMN_NAME_PRIORITY
        };
        String selection = WaitlistContract.WaitlistEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentId)};
        Cursor cursor = db.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            editTextName.setText(cursor.getString(cursor.getColumnIndexOrThrow(WaitlistContract.WaitlistEntry.COLUMN_NAME_NAME)));
            editTextCourse.setText(cursor.getString(cursor.getColumnIndexOrThrow(WaitlistContract.WaitlistEntry.COLUMN_NAME_COURSE)));
            String priority = cursor.getString(cursor.getColumnIndexOrThrow(WaitlistContract.WaitlistEntry.COLUMN_NAME_PRIORITY));
            setSpinnerSelection(spinnerPriority, priority);
        }
        cursor.close();
    }

    private void updateStudent() {
        String name = editTextName.getText().toString().trim();
        String course = editTextCourse.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        if (name.isEmpty() || course.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_NAME, name);
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_COURSE, course);
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_PRIORITY, priority);

        String selection = WaitlistContract.WaitlistEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentId)};

        int rowsUpdated = db.update(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update student", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, StudentListActivity.class);
        startActivity(intent);
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
