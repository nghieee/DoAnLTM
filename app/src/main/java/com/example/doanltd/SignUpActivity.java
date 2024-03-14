package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import database.dbHelper;

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
        medtName = findViewById(R.id.edt_User_Name);
        medtUsername = findViewById(R.id.edt_User_Username);
        medtPassword = findViewById(R.id.edt_User_Password);
        medtEmail = findViewById(R.id.edt_User_Email);
        medtPhone = findViewById(R.id.edt_User_Phone);
        mbtnSignup = (Button) findViewById(R.id.btnSignup);
        mtvLogin = (TextView) findViewById(R.id.tvLogin);

        dbHelper = new dbHelper(this);

        mbtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy thông tin từ các trường nhập liệu
                String name = medtName.getText().toString();
                String username = medtUsername.getText().toString();
                String password = medtPassword.getText().toString();
                String email = medtEmail.getText().toString();
                String phone = medtPhone.getText().toString();

                //Thực hiện thêm User vào db
                addUserToDatabase(name, username, password, email, phone);

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

    //Thêm User vào database
    private void addUserToDatabase(String userName, String userUsername, String userPassword, String userEmail, String userPhone) {
        //Khai báo db để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(dbHelper.TB_User_HoTen, userName);
        values.put(dbHelper.TB_User_Username, userUsername);
        values.put(dbHelper.TB_User_MatKhau, userPassword);
        values.put(dbHelper.TB_User_Email, userEmail);
        values.put(dbHelper.TB_User_SDT, userPhone);


        //Thực hiện thêm User vào database
        long newRowUser = db.insert(dbHelper.TB_USER, null, values);

        //Kiểm tra SignUp thành công hay không
        if (newRowUser != -1) {
            //Đăng ký thành công
            Toast.makeText(this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
            //Cập nhật lại danh sách
        } else {
            //Đăng ký thất bại
            Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu sau khi thêm xong
        db.close();
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