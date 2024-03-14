package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;

import adapter.AdminFacultyListAdapter;

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
        mibtnLogout = (ImageButton) findViewById(R.id.ibtnLogout);

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
                goToAdminFacultyListActivity(v);
            }
        });

        mibtnSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdminSubjectListActivity(v);
            }
        });

        mibtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chuyển về LoginActivity
                Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                //Kết thúc hoạt động hiện tại tránh trường hợp người dùng back lại trang Admin
                finish();
            }
        });
    }

    //Chuyển đến danh sách lớp học
    private void goToAdminClassListActivity(View view) {
        Intent intent = new Intent(this, AdminClassListActivity.class);
        startActivity(intent);
    }

    //Chuyển đến danh sách giảng viên
    private void goToAdminTeacherListActivity(View view) {
        Intent intent = new Intent(this, AdminTeacherListActivity.class);
        startActivity(intent);
    }

    //Chuyển đến danh sách sinh viên
    private void goToAdminStudentListActivity(View view) {
        Intent intent = new Intent(this, AdminStudentListActivity.class);
        startActivity(intent);
    }

    //Chuyển đến danh sách khoa
    private void goToAdminFacultyListActivity(View view) {
        Intent intent = new Intent(this, AdminFacultyListActivity.class);
        startActivity(intent);
    }

    //Chuyển đến danh sách môn học
    private void goToAdminSubjectListActivity(View view) {
        Intent intent = new Intent(this, AdminSubjectListActivity.class);
        startActivity(intent);
    }
}