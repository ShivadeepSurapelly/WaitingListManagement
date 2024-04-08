package com.example.waitinglistmanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextCourse;
    private Spinner spinnerPriority;
    private Button buttonSave, buttonShowList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        editTextName = findViewById(R.id.editTextName);
        editTextCourse = findViewById(R.id.editTextCourse);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        buttonSave = findViewById(R.id.buttonSave);
        buttonShowList = findViewById(R.id.buttonShowList);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentToWaitingList();
            }
        });

        buttonShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StudentListActivity.class));
            }
        });
    }

    private void addStudentToWaitingList() {
        String studentName = editTextName.getText().toString().trim();
        String courseName = editTextCourse.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        if (studentName.isEmpty() || courseName.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_NAME, studentName);
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_COURSE, courseName);
        values.put(WaitlistContract.WaitlistEntry.COLUMN_NAME_PRIORITY, priority);

        long newRowId = db.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, values);
        if (newRowId != -1) {
            Toast.makeText(MainActivity.this, "Student added to waiting list", Toast.LENGTH_SHORT).show();
            editTextName.getText().clear();
            editTextCourse.getText().clear();
            spinnerPriority.setSelection(0);
        } else {
            Toast.makeText(MainActivity.this, "Failed to add student", Toast.LENGTH_SHORT).show();
        }
    }
}
