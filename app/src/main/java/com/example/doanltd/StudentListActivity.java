package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

public class StudentListActivity extends AppCompatActivity {
    ImageView mbtnBackToClassList;
    String listStudent[] = {"Sinh viên a", "Sinh viên b", "Sinh viên c", "Sinh viên d", "Sinh viên e"};
    ArrayAdapter<String> listStudentAdapter;
    ListView mlistStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        mlistStudent = (ListView) findViewById(R.id.listTeacher);
        listStudentAdapter = new ArrayAdapter<>(StudentListActivity.this, android.R.layout.simple_list_item_1, listStudent);
        mlistStudent.setAdapter(listStudentAdapter);

        //Bắt sự kiện OnClick
        mlistStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToStudentDetailActivity(view);
            }
        });

        mbtnBackToClassList = (ImageView) findViewById(R.id.btnBackToClassList);
        mbtnBackToClassList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToClassListActivity(v);
            }
        });
    }

    public void backToClassListActivity(View view) {
        finish();
    }

    public void goToStudentDetailActivity(View view) {
        Intent intent = new Intent(this, StudentDetailActivity.class);
        startActivity(intent);
    }
}