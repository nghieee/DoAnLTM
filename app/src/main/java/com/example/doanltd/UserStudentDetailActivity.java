package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class UserStudentDetailActivity extends AppCompatActivity {
    ImageView mbtnBackToClassDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_student_detail_activity);
        mbtnBackToClassDetail = findViewById(R.id.btnBackToStudentList);

        mbtnBackToClassDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToClassDetail(v);
            }
        });
    }

    public void backToClassDetail(View view) {
        finish();
    }
}