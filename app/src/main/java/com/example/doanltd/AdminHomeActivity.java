package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {
    int imageOption[] = {R.drawable.admin_option_students, R.drawable.admin_option_teachers,
                        R.drawable.admin_option_classes, R.drawable.admin_option_subjects,
                        R.drawable.admin_option_majors};
    String nameOption[] = {"Sinh Viên", "Giảng viên", "Lớp học phần", "Môn học", "Khoa"};
    GridView gv_option;
    ArrayList<AdminHomeOption> optionList;
    AdminHomeOptionAdapter adminAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        //Tạo mảng chính và add 2 mảng con vào
        optionList = new ArrayList<>();
        for (int i = 0; i < nameOption.length; i++) {
            optionList.add(new AdminHomeOption(imageOption[i], nameOption[i]));
        }
        adminAdapter = new AdminHomeOptionAdapter(AdminHomeActivity.this, R.layout.admin_option_item, optionList);

        gv_option = findViewById(R.id.gv_option);
        gv_option.setAdapter(adminAdapter);
    }
}