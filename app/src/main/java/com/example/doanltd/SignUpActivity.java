package com.example.doanltd;

import static database.dbHelper.TB_GIANGVIEN;
import static database.dbHelper.TB_GIANGVIEN_EMAIL;
import static database.dbHelper.TB_GIANGVIEN_HOTEN;
import static database.dbHelper.TB_GIANGVIEN_SDT;
import static database.dbHelper.TB_GIANGVIEN_USERNAME;
import static database.dbHelper.TB_USER;
import static database.dbHelper.TB_User_Email;
import static database.dbHelper.TB_User_HoTen;
import static database.dbHelper.TB_User_MatKhau;
import static database.dbHelper.TB_User_Role;
import static database.dbHelper.TB_User_SDT;
import static database.dbHelper.TB_User_Username;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import database.dbHelper;

public class SignUpActivity extends AppCompatActivity {
    Button mbtnSignup;
    EditText medtName, medtUsername, medtPassword, medtEmail, medtPhone, medtCode;
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
        medtCode = findViewById(R.id.edt_User_Code);
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
                String code =  medtCode.getText().toString();

                //Kiểm tra dữ liệu nhập vào
                if (validateInput(name, username, password, email, phone)) {
                    //Thực hiện thêm User/Admin vào db
                    if (code.equals("admin")) {
                        addAdminToDatabase(name, username, password, email, phone, 1);
                    } else if (code.isEmpty()) {
                        addUserToDatabase(name, username, password, email, phone);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sai code!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mtvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Check values input
    private boolean validateInput(String name, String username, String password, String email, String phone) {
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            //Hiển thị thông báo lỗi nếu một trong các trường nhập liệu không được điền đầy đủ
            Toast.makeText(SignUpActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!utils.utils.isValidFullName(name)) {
            //Hiển thị thông báo lỗi nếu địa chỉ email không hợp lệ
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập họ tên hợp lệ.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!utils.utils.isValidEmail(email)) {
            //Hiển thị thông báo lỗi nếu địa chỉ email không hợp lệ
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập địa chỉ email hợp lệ.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!utils.utils.isValidPhoneNumber(phone)) {
            //Hiển thị thông báo lỗi nếu địa chỉ email không hợp lệ
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập sđt hợp lệ.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //Thêm Admin vào database
    private void addAdminToDatabase(String userName, String userUsername, String userPassword, String userEmail, String userPhone, Integer role) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TB_User_HoTen, userName);
        values.put(TB_User_Username, userUsername);
        values.put(TB_User_MatKhau, userPassword);
        values.put(TB_User_Email, userEmail);
        values.put(TB_User_SDT, userPhone);
        values.put(TB_User_Role, role);

        long newRowAdmin = db.insert(TB_USER, null, values);

        if (newRowAdmin != -1) {
            //Tạo admin thành công
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Đăng ký tài khoản Admin thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //Đăng ký thất bại
            Toast.makeText(this, "Đăng ký tài khoản Admin thất bại!", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    //Thêm User vào database
    private void addUserToDatabase(String userName, String userUsername, String userPassword, String userEmail, String userPhone) {
        //Khai báo db để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_User_HoTen, userName);
        values.put(TB_User_Username, userUsername);
        values.put(TB_User_MatKhau, userPassword);
        values.put(TB_User_Email, userEmail);
        values.put(TB_User_SDT, userPhone);

        //Thực hiện thêm User vào database
        long newRowUser = db.insert(TB_USER, null, values);

        //Kiểm tra SignUp thành công hay không
        if (newRowUser != -1) {
            //Đăng ký thành công
            addTeacherToDatabase(userName, userEmail, userPhone, userUsername);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
            finish();
            //Cập nhật lại danh sách
        } else {
            //Đăng ký thất bại
            Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu sau khi thêm xong
        db.close();
    }

    private void addTeacherToDatabase(String teacherName, String teacherEmail, String teacherPhone, String teacherUsername) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TB_GIANGVIEN_HOTEN, teacherName);
        values.put(TB_GIANGVIEN_EMAIL, teacherEmail);
        values.put(TB_GIANGVIEN_SDT, teacherPhone);
        values.put(TB_GIANGVIEN_USERNAME, teacherUsername);

        long newRowTeacher = db.insert(TB_GIANGVIEN, null, values);

        if (newRowTeacher != -1) {
            // Thêm giảng viên thành công
            Toast.makeText(this, "Thêm giảng viên thành công!", Toast.LENGTH_SHORT).show();
        } else {
            // Thêm giảng viên thất bại
            Toast.makeText(this, "Thêm giảng viên thất bại!", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

}