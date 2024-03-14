package com.example.doanltd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import database.dbHelper;

public class AdminStudentListActivity extends AppCompatActivity {
    FloatingActionButton mfabThemSV;
    database.dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_list);
        mfabThemSV = (FloatingActionButton) findViewById(R.id.fabThemSV);

        dbHelper = new dbHelper(this);

        mfabThemSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStudentPopup();
            }
        });
    }

    //Hiển thị popup để thêm SV
    private void showAddStudentPopup() {
        //Xây dựng AlertDialog để hiển thị popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_add_student, null);
        builder.setView(dialogView);

        //Khởi tạo các trường nhập liệu trong popup
        EditText medt_SV_HoTen = (EditText) dialogView.findViewById(R.id.edt_SV_HoTen);
        EditText medt_SV_NgaySinh = (EditText) dialogView.findViewById(R.id.edt_SV_NgaySinh);
        RadioGroup mrg_SV_GioiTinh = (RadioGroup) dialogView.findViewById(R.id.rg_SV_GioiTinh);
        EditText medt_SV_Email = (EditText) dialogView.findViewById(R.id.edt_SV_Email);
        EditText medt_SV_NienKhoa = (EditText) dialogView.findViewById(R.id.edt_SV_NienKhoa);
        EditText medt_SV_DiaChi = (EditText) dialogView.findViewById(R.id.edt_SV_DiaChi);
        EditText medt_SV_SDT = (EditText) dialogView.findViewById(R.id.edt_SV_SDT);

        builder.setTitle("Thêm sinh viên")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Lấy dữ liệu từ các trường nhập liệu
                        String SV_HoTen = medt_SV_HoTen.getText().toString();
                        String SV_NgaySinh = medt_SV_NgaySinh.getText().toString();
                        RadioButton checkSV_GioiTinh = findViewById(mrg_SV_GioiTinh.getCheckedRadioButtonId());
                        String SV_GioiTinh = checkSV_GioiTinh.getText().toString();
                        String SV_Email = medt_SV_Email.getText().toString();
                        String SV_NienKhoa = medt_SV_NienKhoa.getText().toString();
                        String SV_DiaChi = medt_SV_DiaChi.getText().toString();
                        String SV_SDT = medt_SV_SDT.getText().toString();

                        //Thêm SV vào db
                        addStudentToDatabase(SV_HoTen, SV_NgaySinh, SV_GioiTinh, SV_Email,
                                SV_NienKhoa, SV_DiaChi, SV_SDT);

                        //Đóng dialog sau khi thêm SV
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

    // Hàm thêm sinh viên vào cơ sở dữ liệu
    private void addStudentToDatabase(String name, String dob, String gender, String email, String year, String address, String phoneNumber) {
        // Tạo hoặc mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(dbHelper.TB_SINHVIEN_HOTEN, name);
        values.put(dbHelper.TB_SINHVIEN_NGAYSINH, dob);
        values.put(dbHelper.TB_SINHVIEN_GIOITINH, gender);
        values.put(dbHelper.TB_SINHVIEN_EMAIL, email);
        values.put(dbHelper.TB_SINHVIEN_NIENKHOA, year);
        values.put(dbHelper.TB_SINHVIEN_DIACHI, address);
        values.put(dbHelper.TB_SINHVIEN_SDT, phoneNumber);

        // Thực hiện thêm sinh viên vào cơ sở dữ liệu
        long newRowId = db.insert(dbHelper.TB_SINHVIEN, null, values);

        // Kiểm tra xem thêm sinh viên thành công hay không
        if (newRowId != -1) {
            // Thêm sinh viên thành công
            Toast.makeText(this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
        } else {
            // Thêm sinh viên thất bại
            Toast.makeText(this, "Thêm sinh viên thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu sau khi thêm xong
        db.close();
    }
}