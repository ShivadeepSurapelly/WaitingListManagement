package com.example.waitinglistmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerViewStudents;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        dbHelper = new DatabaseHelper(this);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));

        List<Student> studentList = dbHelper.getAllStudents();
        StudentSorter.sortByPriority(studentList);
        adapter = new StudentAdapter(studentList);
        recyclerViewStudents.setAdapter(adapter);

        adapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                editStudent(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteStudent(position);
            }
        });
    }

    private void editStudent(int position) {
        long id = adapter.getStudentList().get(position).getId();
        Intent intent = new Intent(this, EditStudentActivity.class);
        intent.putExtra("student_id", id);
        startActivity(intent);
    }

    private void deleteStudent(int position) {
        long id = adapter.getStudentList().get(position).getId();
        int deletedRows = dbHelper.deleteStudent(id);
        if (deletedRows > 0) {
            Toast.makeText(this, "Student deleted", Toast.LENGTH_SHORT).show();
            adapter.removeStudent(position);
        } else {
            Toast.makeText(this, "Failed to delete student", Toast.LENGTH_SHORT).show();
        }
    }
}
