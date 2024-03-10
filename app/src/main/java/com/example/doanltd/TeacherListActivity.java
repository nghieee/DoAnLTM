package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TeacherListActivity extends AppCompatActivity {
    String listTeacher[] = {"Giảng viên a", "Giảng viên b", "Giảng viên c", "Giảng viên d", "Giảng viên e"};
    ArrayAdapter<String> listTeacherAdapter;
    ListView mlistTeacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        mlistTeacher = (ListView) findViewById(R.id.listTeacher);
        listTeacherAdapter = new ArrayAdapter<>(TeacherListActivity.this, android.R.layout.simple_list_item_1, listTeacher);
        mlistTeacher.setAdapter(listTeacherAdapter);

        //Bắt xự kiện OnClick
        mlistTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToTeacherDetail(view);
            }
        });
    }

    public void goToTeacherDetail(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}