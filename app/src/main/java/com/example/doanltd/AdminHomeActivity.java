package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AdminHomeActivity extends AppCompatActivity {
    ImageButton mibtnClass, mibtnTeacher, mibtnStudent, mibntFaculty, mibtnSubject, mibtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_activity);
        mibtnClass = (ImageButton) findViewById(R.id.ibtnClass);
        mibtnTeacher = (ImageButton) findViewById(R.id.ibtnTeacher);
        mibtnStudent = (ImageButton) findViewById(R.id.ibtnStudent);
        mibntFaculty = (ImageButton) findViewById(R.id.ibtnMajor);
        mibtnSubject = (ImageButton) findViewById(R.id.ibtnSubject);
        mibtnLogout = (ImageButton) findViewById(R.id.ibtnLogout);

        //Xử lý sự kiện
        mibtnClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminClassListActivity.class);
                startActivity(intent);
            }
        });

        mibtnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminTeacherListActivity.class);
                startActivity(intent);
            }
        });

        mibtnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminStudentListActivity.class);
                startActivity(intent);
            }
        });

        mibntFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminFacultyListActivity.class);
                startActivity(intent);
            }
        });

        mibtnSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminSubjectListActivity.class);
                startActivity(intent);
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
}