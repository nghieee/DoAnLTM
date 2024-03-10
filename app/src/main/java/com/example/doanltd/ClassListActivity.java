package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ClassListActivity extends AppCompatActivity {
    String listClass[] = {"Lớp học a", "Lớp học b", "Lớp học c", "Lớp học d", "Lớp học e"};
    ArrayAdapter<String> listClassAdapter;
    ListView mlistClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        mlistClass = (ListView) findViewById(R.id.listClass);
        listClassAdapter = new ArrayAdapter<>(ClassListActivity.this, android.R.layout.simple_list_item_1, listClass);
        mlistClass.setAdapter(listClassAdapter);

        //Bắt xự kiện OnClick
        mlistClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToClassDetail(view);
            }
        });

    }

    public void goToClassDetail(View view) {
        Intent intent = new Intent(this, StudentListActivity.class);
        startActivity(intent);
    }
}