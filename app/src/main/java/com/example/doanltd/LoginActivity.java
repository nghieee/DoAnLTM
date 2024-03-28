package com.example.doanltd;

import static database.dbHelper.TB_USER;
import static database.dbHelper.TB_User_MatKhau;
import static database.dbHelper.TB_User_Role;
import static database.dbHelper.TB_User_Username;

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
        setContentView(R.layout.activity_login);
        //Ánh xạ
        medt_LogIn_Username = (EditText) findViewById(R.id.edt_LogIn_Username);
        medt_LogIn_Password = (EditText) findViewById(R.id.edt_LogIn_Password);
        mbtnLogin = (Button) findViewById(R.id.btnLogin);
        mtvSignup = (TextView) findViewById(R.id.tvSignup);
//        btnAdmin = (Button) findViewById(R.id.btnAdmin);

        //Khởi tạo dbHelper
        dbHelper = new dbHelper(this);

        //Xử lý sự kiện Đăng ký
        mtvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
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
                int Login_Role = getUserRole(Login_Username, Login_Password);

                //Check tk mk
                if (Login_Username.isEmpty() || Login_Password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản và mật khẩu của bạn", Toast.LENGTH_SHORT).show();
                } else if (validateLogin(Login_Username, Login_Password)) {
                    if (Login_Role != -1) {
                        if (Login_Role == 0) {
                            //Role 0 vai trò Giảng Viên
                            Intent intent = new Intent(LoginActivity.this, UserClassListActivity.class);
                            intent.putExtra("loginUsername", Login_Username);
                            startActivity(intent);
                            finish();

                        } else {
                            //Role 1 vai trò Admin
                            Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Role ở tài khoản này null nên hong có truy vấn được :((", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Đăng nhập thất bại
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không chính xác.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        btnAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Admin(v);
//            }
//        });
    }

    //Truy vấn kiểm tra tài khoản Đăng Nhập
    private boolean validateLogin(String checkLogin_username, String checkLogin_password) {
        //Mở cơ sở dữ liệu để đọc
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Thiết lập câu lệnh truy vấn
        String queryLogin = "SELECT * FROM " +TB_USER + " WHERE " +
                TB_User_Username + " = ? AND " +
                TB_User_MatKhau + " = ?";
        String[] selectionArgs = {checkLogin_username, checkLogin_password};

        //Thực hiện truy vấn
        Cursor cursor = db.rawQuery(queryLogin, selectionArgs);

        //Kiểm tra xem có truy vấn được dữ liệu không
        boolean isValid = cursor.moveToFirst();

        //Đóng con trỏ và database
        cursor.close();
        db.close();

        return isValid;
    }
    //Truy vấn Role để đăng nhập
    @SuppressLint("Range")
    private int getUserRole(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " + TB_User_Role + " FROM " + TB_USER + " WHERE " +
                TB_User_Username + " = ? AND " +
                TB_User_MatKhau + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        int role = -1;

        if (cursor.moveToFirst()) {
            role = cursor.getInt(cursor.getColumnIndex(TB_User_Role));
        }

        cursor.close();
        db.close();

        return role;
    }

    private void Admin(View view) {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        startActivity(intent);
    }
}