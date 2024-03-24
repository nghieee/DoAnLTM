package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class UserClassListActivity extends AppCompatActivity {
    database.dbHelper dbHelper;
    ImageButton btn_userHomeOption;
    String teacherUsername;

    private long backpressTime;
    /** @noinspection deprecation*/
    @Override
    public void onBackPressed() {
        if (backpressTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Nhấn back lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
        backpressTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_class_list_activity);

        dbHelper = new database.dbHelper(this);

        Intent intent = getIntent();
        if (intent != null) {
            teacherUsername = intent.getStringExtra("loginUsername");
        }

        btn_userHomeOption = findViewById(R.id.btn_userHomeOption);
        btn_userHomeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(UserClassListActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.user_home_option, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_view_account) {
                            Intent intent = new Intent(UserClassListActivity.this, UserTeacherDetailActivity.class);
                            intent.putExtra("teacherUsername", teacherUsername);
                            startActivity(intent);
                            return true;
                        } else if (id == R.id.menu_logout) {
                            Intent intent1 = new Intent(UserClassListActivity.this, LoginActivity.class);
                            startActivity(intent1);
                            finish();
                            return true;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }


}