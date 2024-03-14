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
                // Lấy thông tin từ các trường nhập liệu
                String Login_Username = medt_LogIn_Username.getText().toString();
                String Login_Password = medt_LogIn_Password.getText().toString();

                //Kiểm tra xem tài khoản và mật khẩu có hợp lệ không
                if (Login_Username.isEmpty() || Login_Password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản và mật khẩu của bạn", Toast.LENGTH_SHORT).show();
                } else if (validateLogin(Login_Username, Login_Password)) {
                    int userRole = getUserRole(Login_Username, Login_Password);
                    if (userRole == 0) {
                        //Đăng nhập thành công role Admin
                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                    } else if (userRole == 1) {
                        //Đăng nhập thành công role GV
                        Intent intent = new Intent(LoginActivity.this, ClassListActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi role", Toast.LENGTH_SHORT).show();
                    }
                    finish(); //Đóng hoạt động đăng nhập sau khi đăng nhập thành công
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

    //Phương thức để lấy role của người dùng từ cơ sở dữ liệu
    @SuppressLint("Range")
    private int getUserRole(String checkLogin_username, String checkLogin_password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Câu lệnh truy vấn
        String query = "SELECT " + dbHelper.TB_User_Role + " FROM " + dbHelper.TB_USER +
                " WHERE " + dbHelper.TB_User_Username + " = ? AND " +
                dbHelper.TB_User_MatKhau + " = ?";
        String[] selectionArgs = {checkLogin_username, checkLogin_password};

        // Thực hiện truy vấn
        Cursor cursor = db.rawQuery(query, selectionArgs);

        int role = -1;
        // Kiểm tra xem có kết quả không và lấy giá trị role
        if (cursor.moveToFirst()) {
            role = cursor.getInt(cursor.getColumnIndex(dbHelper.TB_User_Role));
        }

        // Đóng con trỏ và cơ sở dữ liệu
        cursor.close();
        db.close();

        return role;
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