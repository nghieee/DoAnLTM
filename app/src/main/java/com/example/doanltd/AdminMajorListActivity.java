package com.example.doanltd;

import static database.dbHelper.TB_CHUYENGANH_IDKHOA;
import static database.dbHelper.TB_CHUYENNGANH;
import static database.dbHelper.TB_CHUYENNGANH_ID;
import static database.dbHelper.TB_CHUYENNGANH_TEN;
import static database.dbHelper.TB_KHOA;
import static database.dbHelper.TB_KHOA_ID;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.AdminMajorListAdapter;
import database.dbHelper;

public class AdminMajorListActivity extends AppCompatActivity {
    FloatingActionButton mfabThemChuyenNganh;
    ListView mlvChuyenNganh;
    static dbHelper dbHelper;
    String facultyId;
    ImageButton mbtnBackToFaculty;
    TextView mtvTitleMajorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_major_list_activity);

        // Ánh xạ các thành phần trong layout
        mfabThemChuyenNganh = findViewById(R.id.fabThemChuyenNganh);
        mlvChuyenNganh = findViewById(R.id.lvChuyenNganh);
        dbHelper = new dbHelper(this);

        //Nhận thông tin Khoa từ Intent Khoa
        Intent intent = getIntent();
        mtvTitleMajorList = findViewById(R.id.tvMajorList);
        if (intent != null) {
            facultyId = intent.getStringExtra("facultyId");
            String facultyName = intent.getStringExtra("facultyName");
            mtvTitleMajorList.setText("Danh sách chuyên ngành khoa " + facultyName);

            displayMajorList();
        }

        // Bắt sự kiện click cho FloatingActionButton
        mfabThemChuyenNganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMajorPopup();
            }
        });

        mbtnBackToFaculty = findViewById(R.id.btnBackToFaculty);
        mbtnBackToFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMajorListActivity.this, AdminFacultyListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Hiển thị danh sách Chuyên Ngành
    private void displayMajorList() {
        ArrayList<String> arrayList = getMajorList(facultyId);

        // Tạo adapter và gắn với ListView
        AdminMajorListAdapter adapter = new AdminMajorListAdapter(this, arrayList);
        mlvChuyenNganh.setAdapter(adapter);

        //Xử lý sự kiện cập nhật
        adapter.setOnEditClickListener(new AdminMajorListAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(String majorId, String majorName) {
                showEditMajorPopup(majorId, majorName);
            }
        });

        //Xử lý sự kiện xóa
        adapter.setOnDeleteClickListener(new AdminMajorListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String majorId) {
                showDeleteConfirmationDialog(majorId);
            }
        });
    }

    // Lấy danh sách chuyên ngành theo KHÓA NGOẠI KHOA từ cơ sở dữ liệu
    public ArrayList<String> getMajorList(String facultyId) {
        ArrayList<String> majorList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + TB_CHUYENNGANH_ID + ", " + TB_CHUYENNGANH_TEN +
                    " FROM " + TB_CHUYENNGANH +
                    " WHERE " + TB_CHUYENGANH_IDKHOA + " = ?";
            String[] selectionArgs = {facultyId};
            cursor = db.rawQuery(query, selectionArgs);

            // Duyệt qua các dòng kết quả và thêm vào danh sách
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String majorId = cursor.getString(cursor.getColumnIndex(TB_CHUYENNGANH_ID));
                    @SuppressLint("Range") String majorName = cursor.getString(cursor.getColumnIndex(TB_CHUYENNGANH_TEN));
                    majorList.add(majorId + " - " + majorName); // Thêm vào danh sách dạng "Id - Tên"
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("AdminMajorListActivity", "Lỗi khi lấy danh sách chuyên ngành: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        db.close();

        return majorList;

    }

    //Hiển thị dialog thêm Chuyên Ngành
    private void showAddMajorPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_admin_major_add, null);
        builder.setView(dialogView);

        // Khởi tạo các trường nhập liệu
        EditText edtChuyenNganhID = dialogView.findViewById(R.id.edt_ChuyenNganh_ID);
        EditText edtChuyenNganhTen = dialogView.findViewById(R.id.edt_ChuyenNganh_Ten);

        builder.setTitle("Thêm Chuyên Ngành")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lấy dữ liệu từ ô nhập
                        String chuyenNganhId = edtChuyenNganhID.getText().toString();
                        String chuyenNganhTen = edtChuyenNganhTen.getText().toString();

                        // Thực hiện thêm chuyên ngành vào database với ID của Khoa đã lấy được
                        addMajorToDatabase(chuyenNganhId, chuyenNganhTen, facultyId);

                        // Đóng dialog sau khi thêm chuyên ngành
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

    //Add chuyên ngành vào database
    private void addMajorToDatabase(String chuyenNganhId, String chuyenNganhTen, String facultyId) {
        if (facultyId == null) {
            Log.e("AdminMajorListActivity", "facultyId is null!");
            return;
        }
        // Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Chuẩn bị dữ liệu để chèn vào bảng ChuyenNganh
        ContentValues values = new ContentValues();
        values.put(TB_CHUYENNGANH_ID, chuyenNganhId);
        values.put(TB_CHUYENNGANH_TEN, chuyenNganhTen);
        values.put(TB_CHUYENGANH_IDKHOA, facultyId); // Thiết lập trường TB_CHUYENNGANH_IDKHOA với ID của Khoa

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
            Toast.makeText(this, "Nhập mã trùng với chuyên ngành khác rồi trời!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu
        db.close();
    }

    //Hiên thị dialog để sửa
    private void showEditMajorPopup(String currentMajorId, String currentMajorName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_admin_major_edit, null);
        builder.setView(dialogView);

        // Khởi tạo các trường nhập liệu
        EditText edtNewCNId = dialogView.findViewById(R.id.edt_newChuyenNganhId);
        EditText edtNewCNTen = dialogView.findViewById(R.id.edt_newChuyenNganhTen);

        // Hiển thị thông tin hiện tại của Khoa trong các trường nhập liệu
        edtNewCNId.setText(currentMajorId);
        edtNewCNTen.setText(currentMajorName);

        builder.setTitle("Chỉnh sửa thông tin Chuyên Ngành")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCNId = edtNewCNId.getText().toString();
                        String newCNTen = edtNewCNTen.getText().toString();

                        // Thực hiện cập nhật thông tin CN trong database
                        updateMajorInDatabase(currentMajorId, newCNId, newCNTen);

                        // Đóng dialog sau khi cập nhật
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

    //Cập nhật thông tin CN trong database
    private void updateMajorInDatabase(String currentId, String newId, String newName) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_CHUYENNGANH_ID, newId);
        values.put(TB_CHUYENNGANH_TEN, newName);

        //Thực hiện cập nhật thông tin chuyên ngành trong database
        int rowsAffectedFaculty = db.update(TB_CHUYENNGANH, values, TB_CHUYENNGANH_ID + " = ?", new String[]{currentId});

        if (rowsAffectedFaculty > 0) {
            //Cập nhật thành công
            Toast.makeText(this, "Thông tin chuyên ngaành đã được cập nhật!", Toast.LENGTH_SHORT).show();

            //Cập nhật lại danh sách khoa trên ListView
            displayMajorList();
        } else {
            //Cập nhật thất bại
            Toast.makeText(this, "Cập nhật thông tin chuyên ngành thất bại!", Toast.LENGTH_SHORT).show();
        }

        //Đóng db sau khi thêm xong
        db.close();
    }

    //Hiển thị xác nhận xóa
    private void showDeleteConfirmationDialog(String major) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc chắn muốn xóa?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Xác nhận xóa, gọi hàm xóa Khoa
                        deleteMajor(major);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Chọn hủy, không thực hiện thao tác xóa
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

        //Tạo hộp thoại và hiển thị nó
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Xóa Khoa từ cơ sở dữ liệu
    private void deleteMajor(String majorId) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Thực hiện xóa Khoa từ cơ sở dữ liệu
        int rowsDeleted = db.delete(TB_CHUYENNGANH, TB_CHUYENNGANH_ID + "=?", new String[]{majorId});

        // Kiểm tra xem có dữ liệu nào bị xóa không
        if (rowsDeleted > 0) {
            //Xóa thành công
            Toast.makeText(this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();

            //Cập nhật lại danh sách Khoa trên ListView
            displayMajorList();
        } else {
            //Xóa không thành công
            Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu
        db.close();
    }


    //Lấy dữ liệu môn học để đưa lên spinner

    //Lấy danh sách Chuyên Ngành


}