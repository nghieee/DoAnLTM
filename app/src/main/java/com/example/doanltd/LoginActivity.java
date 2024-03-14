package com.example.doanltd;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import database.dbHelper;

public class LoginActivity extends AppCompatActivity {
    Button mbtnLogin, btnAdmin;
    EditText medt_LogIn_Password, medt_LogIn_Username;
    TextView mtvSignup;
    database.dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Ánh xạ
        medt_LogIn_Username = (EditText) findViewById(R.id.edt_LogIn_Username);
        medt_LogIn_Password = (EditText) findViewById(R.id.edt_LogIn_Password);
        mbtnLogin = (Button) findViewById(R.id.btnLogin);
        mtvSignup = (TextView) findViewById(R.id.tvSignup);
        btnAdmin = (Button) findViewById(R.id.btnAdmin);

        //Khởi tạo dbHelper
        dbHelper = new dbHelper(this);

        //Xử lý sự kiện Đăng ký
        mtvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gọi phương thức chuyển đến Đăng Ký
                goToSignUpActivity(v);
            }
        });

        //Xử lý sự kiện nút Đăng Nhập
        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Lấy thông tin từ trường nhập liệu
                String Login_Username = medt_LogIn_Username.getText().toString();
                String Login_Password = medt_LogIn_Password.getText().toString();

                //Check tk mk
                if (Login_Username.isEmpty() || Login_Password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản và mật khẩu của bạn", Toast.LENGTH_SHORT).show();
                } else if (validateLogin(Login_Username, Login_Password)) {
                    //Đăng nhập thành công, chuyển sang trang GV
                    Intent intent = new Intent(LoginActivity.this, ClassListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //Đăng nhập thất bại
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không chính xác.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Admin(v);
            }
        });
    }

    //Truy vấn kiểm tra tài khoản Đăng Nhập
    private boolean validateLogin(String checkLogin_username, String checkLogin_password) {
        //Mở cơ sở dữ liệu để đọc
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Thiết lập câu lệnh truy vấn
        String queryLogin = "SELECT * FROM " + dbHelper.TB_USER + " WHERE " +
                dbHelper.TB_User_Username + " = ? AND " +
                dbHelper.TB_User_MatKhau + " = ?";
        String[] selectionArgs = {checkLogin_username, checkLogin_password};

        //Thực hiện truy vấn
        Cursor cursor = db.rawQuery(queryLogin, selectionArgs);

        //Kiểm tra xem có bản ghi khớp hay không
        boolean isValid = cursor.moveToFirst();

        // Đóng con trỏ và cơ sở dữ liệu
        cursor.close();
        db.close();

        return isValid;
    }

    //Chuyển đến Đăng Ký
    private void goToSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void Admin(View view) {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }
}