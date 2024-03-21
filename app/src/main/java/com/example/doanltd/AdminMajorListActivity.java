package com.example.doanltd;

import static database.dbHelper.TB_CHUYENGANH_IDKHOA;
import static database.dbHelper.TB_CHUYENNGANH;
import static database.dbHelper.TB_CHUYENNGANH_ID;
import static database.dbHelper.TB_CHUYENNGANH_TEN;
import static database.dbHelper.TB_KHOA;
import static database.dbHelper.TB_KHOA_ID;
import static database.dbHelper.TB_KHOA_TEN;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.AdminFacultyListAdapter;
import adapter.AdminMajorListAdapter;
import database.dbHelper;

public class AdminMajorListActivity extends AppCompatActivity {
    TextView mtvMajorList;
    FloatingActionButton mfabThemChuyenNganh;
    ListView mlvChuyenNganh;
    database.dbHelper dbHelper;
    private String facultyId;
    private String facultyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_major_list);
        mtvMajorList = (TextView) findViewById(R.id.tvMajorList);
        mfabThemChuyenNganh = (FloatingActionButton) findViewById(R.id.fabThemChuyenNganh);
        dbHelper = new dbHelper(this);

        displayMajorList();

        // Nhận thông tin từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            facultyId = intent.getStringExtra("TB_KHOA_ID");
            facultyName = intent.getStringExtra("TB_KHOA_TEN");

            // Hiển thị thông tin Khoa và tên Khoa lên TextView
            mtvMajorList.setText("Danh sách các chuyên ngành khoa " + facultyName);
        }

        mfabThemChuyenNganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMajorPopup();
            }
        });
    }

    //Hiển thị danh sách Chuyên Ngành
    private void displayMajorList() {
        //Lấy danh sách khoa từ cơ sở dữ liệu
        ArrayList<String> arrayList = getMajorList(facultyId);

        //Tạo adapter và gắn với ListView
        AdminMajorListAdapter adapter = new AdminMajorListAdapter(this, arrayList);
        mlvChuyenNganh = findViewById(R.id.lvChuyenNganh);
        mlvChuyenNganh.setAdapter(adapter);

    }

    //Danh sách Khoa
    public ArrayList<String> getMajorList(String facultyId) {
        ArrayList<String> majorList = new ArrayList<>();
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            //Truy vấn danh sách tên các khoa từ bảng Khoa
            cursor = db.query(TB_CHUYENNGANH, new String[]{TB_CHUYENNGANH_ID, TB_CHUYENNGANH_TEN}, TB_CHUYENGANH_IDKHOA + "=?", new String[]{facultyId}, null, null, null);

            //Duyệt qua các dòng kết quả và thêm tên khoa vào danh sách
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String chuyenNganhId = cursor.getString(cursor.getColumnIndex(TB_CHUYENNGANH_ID));
                    @SuppressLint("Range") String chuyenNganhTen = cursor.getString(cursor.getColumnIndex(TB_CHUYENNGANH_TEN));
                    majorList.add(chuyenNganhId + " - " + chuyenNganhTen); //Thêm vào danh sách dạng "Id - Tên"
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi thêm dữ liệu vào danh sách rồi: " + e.getMessage());
        } finally {
            //Đóng cursor sau khi sử dụng
            if (cursor != null) {
                cursor.close();
            }
        }

        //Đóng cơ sở dữ liệu sau khi sử dụng
        db.close();

        return majorList;
    }

    //Hiển thị popup để thêm chuyên ngành
    private void showAddMajorPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_add_major, null);
        builder.setView(dialogView);

        //Khởi tạo các trường nhập liệu
        EditText medt_ChuyenNganh_ID = dialogView.findViewById(R.id.edt_ChuyenNganh_ID);
        EditText medt_ChuyenNganh_Ten = dialogView.findViewById(R.id.edt_ChuyenNganh_Ten);

        builder.setTitle("Thêm Chuyên Ngành")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Lấy dữ liệu từ ô nhập
                        String ChuyenNganh_Id = medt_ChuyenNganh_ID.getText().toString();
                        String ChuyenNganh_Ten = medt_ChuyenNganh_Ten.getText().toString();

                        // Thực hiện thêm Khoa vào database
                        addMajorToDatabase(ChuyenNganh_Id, ChuyenNganh_Ten);

                        // Đóng dialog sau khi thêm Khoa
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
    //Thêm chuyên ngành vào database
    private void addMajorToDatabase(String chuyenNganhId, String chuyenNganhTen) {
        // Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Chuẩn bị dữ liệu để chèn vào bảng ChuyenNganh
        ContentValues values = new ContentValues();
        values.put(TB_CHUYENNGANH_ID, chuyenNganhId);
        values.put(TB_CHUYENNGANH_TEN, chuyenNganhTen);
        values.put(TB_CHUYENGANH_IDKHOA, facultyId); //Thay TB_CHUYENGANH_IDKHOA bằng giá trị của idKhoa hiện tại

        // Thực hiện chèn dữ liệu vào bảng ChuyenNganh
        long newRowId = db.insert(TB_CHUYENNGANH, null, values);

        // Kiểm tra kết quả chèn dữ liệu
        if (newRowId != -1) {
            // Thêm chuyên ngành thành công
            Toast.makeText(this, "Thêm chuyên ngành thành công!", Toast.LENGTH_SHORT).show();
            // Cập nhật lại danh sách chuyên ngành
            displayMajorList();
        } else {
            // Thêm chuyên ngành thất bại
            Toast.makeText(this, "Thêm chuyên ngành thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu
        db.close();
    }

}