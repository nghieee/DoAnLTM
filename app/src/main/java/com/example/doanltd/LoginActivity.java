package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    Button mbtnLogin, btnAdmin;
    TextView mtvSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Ánh xạ
        mbtnLogin = (Button) findViewById(R.id.btnLogin);
        mtvSignup = (TextView) findViewById(R.id.tvSignup);
        btnAdmin = (Button) findViewById(R.id.btnAdmin);

        //Xử lý sự kiện OnClick
        mtvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gọi phương thức chuyển đến Đăng Ký
                goToSignUpActivity(v);
            }
        });

        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSuccess(v);
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Admin(v);
            }
        });
    }

    //Chuyển đến Đăng Ký
    public void goToSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    //Chuyển đến trang chủ GV
    public void LoginSuccess(View view) {
        Intent intent = new Intent(this, ClassListActivity.class);
        startActivity(intent);
    }

    public void Admin(View view) {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }
}