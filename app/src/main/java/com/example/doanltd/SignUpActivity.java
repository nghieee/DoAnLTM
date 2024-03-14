package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {
    Button mbtnSignup;
    EditText medtName, medtUsername, medtPassword, medtEmail, medtPhone;
    database.dbHelper dbHelper;
    TextView mtvLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Ánh xạ các trường nhập liệu từ layout
        medtName = findViewById(R.id.);
        medtUsername = findViewById(R.id.editTextText2);
        medtPassword = findViewById(R.id.editTextTextPassword);
        medtEmail = findViewById(R.id.editTextTextEmailAddress);
        medtPhone = findViewById(R.id.editTextPhone);
        mbtnSignup = (Button) findViewById(R.id.btnSignup);
        mtvLogin = (TextView) findViewById(R.id.tvLogin);

        mbtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpSuccess(v);
            }
        });

        mtvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity(v);
            }
        });
    }

    public void SignUpSuccess(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}