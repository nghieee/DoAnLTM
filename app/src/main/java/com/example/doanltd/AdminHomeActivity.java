package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {
    ImageButton mibtnClass, mibtnTeacher, mibtnStudent, mibntMajor, mibtnSubject, mibtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        mibtnClass = (ImageButton) findViewById(R.id.ibtnClass);
        mibtnTeacher = (ImageButton) findViewById(R.id.ibtnTeacher);
        mibtnStudent = (ImageButton) findViewById(R.id.ibtnStudent);
        mibntMajor = (ImageButton) findViewById(R.id.ibtnMajor);
        mibtnSubject = (ImageButton) findViewById(R.id.ibtnSubject);

        //Xử lý sự kiện
        mibtnClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdminClassListActivity(v);
            }
        });

        mibtnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdminTeacherListActivity(v);
            }
        });

        mibtnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdminStudentListActivity(v);
            }
        });

        mibntMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdminMajorListActivity(v);
            }
        });

        mibtnSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdminSubjectListActivity(v);
            }
        });
    }

    //Chuyển đến danh sách lớp học
    public void goToAdminClassListActivity(View view) {
        Intent intent = new Intent(this, AdminClassListActivity.class);
        startActivity(intent);
    }

    //Chuyển đến danh sách giảng viên
    public void goToAdminTeacherListActivity(View view) {
        Intent intent = new Intent(this, AdminTeacherListActivity.class);
        startActivity(intent);
    }

    //Chuyển đến danh sách sinh viên
    public void goToAdminStudentListActivity(View view) {
        Intent intent = new Intent(this, AdminStudentListActivity.class);
        startActivity(intent);
    }

    //Chuyển đến danh sách khoa
    public void goToAdminMajorListActivity(View view) {
        Intent intent = new Intent(this, AdminMajorListActivity.class);
        startActivity(intent);
    }

    //Chuyển đến danh sách môn học
    public void goToAdminSubjectListActivity(View view) {
        Intent intent = new Intent(this, AdminSubjectListActivity.class);
        startActivity(intent);
    }
}