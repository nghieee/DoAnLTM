package com.example.doanltd;

import static database.dbHelper.TB_GIANGVIEN;
import static database.dbHelper.TB_GIANGVIEN_EMAIL;
import static database.dbHelper.TB_GIANGVIEN_GIOITINH;
import static database.dbHelper.TB_GIANGVIEN_HOTEN;
import static database.dbHelper.TB_GIANGVIEN_ID;
import static database.dbHelper.TB_GIANGVIEN_IDKHOA;
import static database.dbHelper.TB_GIANGVIEN_NGAYSINH;
import static database.dbHelper.TB_GIANGVIEN_SDT;
import static database.dbHelper.TB_GIANGVIEN_USERNAME;
import static database.dbHelper.TB_KHOA;
import static database.dbHelper.TB_KHOA_ID;
import static database.dbHelper.TB_KHOA_TEN;
import static database.dbHelper.TB_USER;
import static database.dbHelper.TB_User_MatKhau;
import static database.dbHelper.TB_User_Username;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import database.dbHelper;
import utils.utils;

public class UserTeacherDetailActivity extends AppCompatActivity {
    // Khai báo các view
    TextView textViewId, textViewName, textViewDoB, textViewGender, textViewEmail, textViewPhone, textViewFaculty, textViewUsername,
            textViewPassword;
    ImageButton buttonBack, btnDetailUpdate;
    int teacherId;
    database.dbHelper dbHelper;
    private String teacherUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_teacher_detail);

        textViewId = findViewById(R.id.textViewId);
        textViewName = findViewById(R.id.textViewName);
        textViewDoB = findViewById(R.id.textViewDOB);
        textViewGender = findViewById(R.id.textViewGender);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewFaculty = findViewById(R.id.textViewFaculty);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewPassword = findViewById(R.id.textViewPassword);
        btnDetailUpdate = findViewById(R.id.btn_update_detail);

        dbHelper = new dbHelper(this);

        // Nhận ID của giảng viên từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            teacherUsername = intent.getStringExtra("teacherUsername");

            displayTeacherInfo(teacherUsername);
        }

        buttonBack = findViewById(R.id.btn_UdetailToUhome);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDetailUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(UserTeacherDetailActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.user_update_detail, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.option_update_detail) {
                            showEditTeacherPopup(teacherUsername);
                            return true;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    // Phương thức để hiển thị thông tin giảng viên
    private void displayTeacherInfo(String teacherUsername) {
        String[] teacherInfo = getTeacherInfoFromDatabase(teacherUsername);

        if (teacherInfo != null) {
            // Gán dữ liệu vào các TextViews
            textViewId.setText("ID: " + teacherInfo[0]);
            textViewName.setText("Tên: " + teacherInfo[1]);
            textViewDoB.setText("Ngày sinh: " + teacherInfo[2]);
            textViewGender.setText("Giới tính: " + teacherInfo[3]);
            textViewEmail.setText("Email: " + teacherInfo[4]);
            textViewPhone.setText("SĐT: " + teacherInfo[5]);
            textViewFaculty.setText("Khoa: " + teacherInfo[9]);
            textViewUsername.setText("Tên đăng nhập: " + teacherInfo[7]);
            textViewPassword.setText("Mật khẩu: " + teacherInfo[8]);
        }
    }

    //Lấy dữ liệu gv thông qua truy vấn xuất lên detailActivity
    private String[] getTeacherInfoFromDatabase(String teacherUsername) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String[] teacherInfo = null;

        try {
            cursor = db.rawQuery("SELECT " + TB_GIANGVIEN + ".*, " + TB_USER + "." + TB_User_MatKhau + ", " +
                    TB_KHOA + "." + TB_KHOA_TEN +
                    " FROM " + TB_GIANGVIEN +
                    " LEFT JOIN " + TB_USER +
                    " ON " + TB_GIANGVIEN + "." + TB_GIANGVIEN_USERNAME + " = " + TB_USER + "." + TB_User_Username +
                    " LEFT JOIN " + TB_KHOA +
                    " ON " + TB_GIANGVIEN + "." + TB_GIANGVIEN_IDKHOA + " = " + TB_KHOA + "." + TB_KHOA_ID +
                    " WHERE " + TB_GIANGVIEN + "." + TB_GIANGVIEN_USERNAME + " = ?", new String[]{teacherUsername});

            if (cursor != null && cursor.moveToFirst()) {
                teacherInfo = new String[]{
                        cursor.getString(0), //Id
                        cursor.getString(1), //Ten
                        cursor.getString(2), //Ngaysinh
                        cursor.getString(3), //Gioitinh
                        cursor.getString(4), //Email
                        cursor.getString(5), //SDT
                        cursor.getString(6), //GvKhoa
                        cursor.getString(7), //GvUsername
                        cursor.getString(8), //UserMK
                        cursor.getString(9), //KhoaTen
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return teacherInfo;
    }

    //Hiển thị popup sửa
    private void showEditTeacherPopup(String teacherUsername) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_user_teacher_edit, null);
        builder.setView(dialogView);

        //Khởi tạo các trường nhập liệu
        EditText edtNewGvTen = dialogView.findViewById(R.id.edt_u_teacher_name);
        EditText edtNewNgaySinh = dialogView.findViewById(R.id.edt_u_teacher_dob);
        RadioGroup rgNewGioiTinh = dialogView.findViewById(R.id.radio_u_group_gender);
        EditText edtNewEmail = dialogView.findViewById(R.id.edt_u_teacher_email);
        EditText edtNewSDT = dialogView.findViewById(R.id.edt_u_teacher_phone);
        EditText edtNewMK = dialogView.findViewById(R.id.edt_u_teacher_password);

        //Hiển thị thông tin hiện tại của gv trong các trường nhập liệu
        rgNewGioiTinh.check(R.id.radio_u_male);
        String[] teacherInfo = getTeacherInfoFromDatabase(teacherUsername);
        if (teacherInfo != null) {
            edtNewGvTen.setText(teacherInfo[1]);
            edtNewNgaySinh.setText(teacherInfo[2]);
            edtNewEmail.setText(teacherInfo[4]);
            edtNewSDT.setText(teacherInfo[5]);
            edtNewMK.setText(teacherInfo[8]);
        }

        builder.setTitle("Chỉnh sửa thông tin giảng viên")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newGvTen = edtNewGvTen.getText().toString();
                        String newNgaySinh = edtNewNgaySinh.getText().toString();
                        if (!utils.isValidDate(newNgaySinh)) {
                            Toast.makeText(UserTeacherDetailActivity.this, "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String newGender = rgNewGioiTinh.getCheckedRadioButtonId() == R.id.radio_u_male ? "Nam" : "Nữ";
                        String newEmail = edtNewEmail.getText().toString();
                        if (!utils.isValidEmail(newEmail)) {
                            Toast.makeText(UserTeacherDetailActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String newSDT = edtNewSDT.getText().toString();
                        if (!utils.isValidPhoneNumber(newSDT)) {
                            Toast.makeText(UserTeacherDetailActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String newMK = edtNewMK.getText().toString();
                        //Gọi phương thức cập nhật vào db
                        updateTeacherInDatabase(teacherUsername, newGvTen, newNgaySinh, newGender, newEmail, newSDT, newMK);

                        //Đóng dialog sau khi cập nhật
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateTeacherInDatabase(String teacherUsername ,String newName, String newNgaySinh,
                                         String newGender, String newEmail, String newSDT, String newMK) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_GIANGVIEN_HOTEN, newName);
        values.put(TB_GIANGVIEN_NGAYSINH, newNgaySinh);
        values.put(TB_GIANGVIEN_GIOITINH, newGender);
        values.put(TB_GIANGVIEN_EMAIL, newEmail);
        values.put(TB_GIANGVIEN_SDT, newSDT);

        ContentValues values1 = new ContentValues();
        values1.put(TB_User_MatKhau, newMK);

        // hực hiện cập nhật thông tin Môn học trong db
        int rowsAffected = db.update(TB_GIANGVIEN, values, TB_GIANGVIEN_USERNAME + " = ?", new String[]{teacherUsername});
        int rowsAffected1 = db.update(TB_USER, values1, TB_User_Username + " = ?", new String[]{teacherUsername});
        if (rowsAffected > 0 && rowsAffected1 > 0) {
            // Cập nhật thành công
            Toast.makeText(this, "Đã cập nhật dữ liệu giảng viên", Toast.LENGTH_SHORT).show();

            // Cập nhật lại danh sách môn học trên ListView
            displayTeacherInfo(teacherUsername);
        } else {
            // Cập nhật thất bại
            Toast.makeText(this, "Cập nhật giảng viên thất bại", Toast.LENGTH_SHORT).show();
        }

        //Đóng db sau khi sửa xong
        db.close();
    }
}