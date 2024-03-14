package com.example.doanltd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.AdminFacultyListAdapter;
import database.dbHelper;

public class AdminFacultyListActivity extends AppCompatActivity {
    FloatingActionButton mfabThemKhoa;
    database.dbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_faculty_list);

        mfabThemKhoa = findViewById(R.id.fabThemKhoa);

        dbHelper = new dbHelper(this);

        displayFacultyList();

        mfabThemKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFacultyPopup();
            }
        });


    }

    //Hiển thị danh sách khoa
    private void displayFacultyList() {
        //Lấy danh sách khoa từ cơ sở dữ liệu
        ArrayList<String> arrayList = dbHelper.getFacultyList();

        //Tạo adapter và gắn với ListView
        AdminFacultyListAdapter adapter = new AdminFacultyListAdapter(this, arrayList);
        ListView mlvKhoa = findViewById(R.id.lvKhoa);
        mlvKhoa.setAdapter(adapter);

        //Xử lý sự kiện cập nhật
        adapter.setOnEditClickListener(new AdminFacultyListAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(String facultyId, String facultyName) {
                showEditFacultyPopup(facultyId, facultyName);
            }
        });

        //Xử lý sự kiện xóa
        adapter.setOnDeleteClickListener(new AdminFacultyListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String facultyId) {
                showDeleteConfirmationDialog(facultyId);
            }
        });
    }

    //Hiển thị popup để thêm Khoa
    private void showAddFacultyPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_add_faculty, null);
        builder.setView(dialogView);

        //Khởi tạo các trường nhập liệu
        EditText medt_Khoa_Id = dialogView.findViewById(R.id.edt_Khoa_ID);
        EditText medt_Khoa_Ten = dialogView.findViewById(R.id.edt_Khoa_Ten);

        builder.setTitle("Thêm Khoa")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Lấy dữ liệu từ ô nhập
                        String Khoa_Id = medt_Khoa_Id.getText().toString();
                        String Khoa_Ten = medt_Khoa_Ten.getText().toString();

                        // Thực hiện thêm Khoa vào database
                        addFacultyToDatabase(Khoa_Id, Khoa_Ten);

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

    //Thêm Khoa vào database
    private void addFacultyToDatabase(String facultyId, String facultyName) {
        // Tạo hoặc mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(dbHelper.TB_KHOA_ID, facultyId);
        values.put(dbHelper.TB_KHOA_TEN, facultyName);

        // Thực hiện thêm Khoa vào cơ sở dữ liệu
        long newRowFaculty = db.insert(dbHelper.TB_KHOA, null, values);

        // Kiểm tra xem thêm Khoa thành công hay không
        if (newRowFaculty != -1) {
            // Thêm Khoa thành công
            Toast.makeText(this, "Thêm Khoa thành công!", Toast.LENGTH_SHORT).show();
            //Cập nhật lại danh sách Khoa trên ListView
            displayFacultyList();
        } else {
            // Thêm Khoa thất bại
            Toast.makeText(this, "Thêm Khoa thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu sau khi thêm xong
        db.close();
    }

    //Hiển thị popup để chỉnh sửa Khoa
    private void showEditFacultyPopup(String currentId, String currentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_edit_faculty, null);
        builder.setView(dialogView);

        // Khởi tạo các trường nhập liệu
        EditText edtNewKhoaId = dialogView.findViewById(R.id.edt_newKhoaId);
        EditText edtNewKhoaTen = dialogView.findViewById(R.id.edt_newKhoaTen);

        // Hiển thị thông tin hiện tại của Khoa trong các trường nhập liệu
        edtNewKhoaId.setText(currentId);
        edtNewKhoaTen.setText(currentName);

        builder.setTitle("Chỉnh sửa thông tin Khoa")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newKhoaId = edtNewKhoaId.getText().toString();
                        String newKhoaTen = edtNewKhoaTen.getText().toString();

                        // Thực hiện cập nhật thông tin Khoa trong cơ sở dữ liệu
                        updateFacultyInDatabase(currentId, newKhoaId, newKhoaTen);

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

    //Cập nhật thông tin Khoa trong db
    private void updateFacultyInDatabase(String currentId, String newId, String newName) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(dbHelper.TB_KHOA_ID, newId);
        values.put(dbHelper.TB_KHOA_TEN, newName);

        //Thực hiện cập nhật thông tin Khoa trong db
        int rowsAffectedFaculty = db.update(dbHelper.TB_KHOA, values, dbHelper.TB_KHOA_ID + " = ?", new String[]{currentId});

        if (rowsAffectedFaculty > 0) {
            //Cập nhật thành công
            Toast.makeText(this, "Thông tin Khoa đã được cập nhật!", Toast.LENGTH_SHORT).show();

            //Cập nhật lại danh sách khoa trên ListView
            displayFacultyList();
        } else {
            //Cập nhật thất bại
            Toast.makeText(this, "Cập nhật thông tin Khoa thất bại!", Toast.LENGTH_SHORT).show();
        }

        //Đóng db sau khi thêm xong
        db.close();
    }

    //Hiển thị xác nhận xóa
    private void showDeleteConfirmationDialog(String faculty) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc chắn muốn xóa?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Xác nhận xóa, gọi hàm xóa Khoa
                        deleteFaculty(faculty);
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
    private void deleteFaculty(String facultyId) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Xác định điều kiện xóa
        String selection = dbHelper.TB_KHOA_ID + "=?";
        String[] selectionArgs = {facultyId};

        //Thực hiện xóa Khoa từ cơ sở dữ liệu
        int rowsDeleted = db.delete(dbHelper.TB_KHOA, selection, selectionArgs);

        // Kiểm tra xem có dữ liệu nào bị xóa không
        if (rowsDeleted > 0) {
            //Xóa thành công
            Toast.makeText(this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();

            //Cập nhật lại danh sách Khoa trên ListView
            displayFacultyList();
        } else {
            //Xóa không thành công
            Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu
        db.close();
    }
}