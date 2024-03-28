package com.example.doanltd;

import static database.dbHelper.TB_CHUYENGANH_IDKHOA;
import static database.dbHelper.TB_CHUYENNGANH;
import static database.dbHelper.TB_KHOA;
import static database.dbHelper.TB_KHOA_ID;
import static database.dbHelper.TB_KHOA_TEN;
import static database.dbHelper.TB_MONHOC;
import static database.dbHelper.TB_MONHOC_IDKHOA;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.AdminFacultyListAdapter;
import database.dbHelper;

public class AdminFacultyListActivity extends AppCompatActivity {
    FloatingActionButton mfabThemKhoa;
    static database.dbHelper dbHelper;
    ListView mlvKhoa;
    ImageView mbtnBackToAdminHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_faculty_list_activity);

        mfabThemKhoa = findViewById(R.id.fabThemKhoa);

        dbHelper = new dbHelper(this);

        displayFacultyList();

        mfabThemKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFacultyPopup();
            }
        });

        mbtnBackToAdminHome = findViewById(R.id.btnBackToAdminHome);
        mbtnBackToAdminHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminFacultyListActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //1. Hiển thị danh sách khoa
    private void displayFacultyList() {
        //Lấy danh sách khoa từ cơ sở dữ liệu
        ArrayList<String> arrayList = getFacultyList();

        //Tạo adapter và gắn với ListView
        AdminFacultyListAdapter adapter = new AdminFacultyListAdapter(this, arrayList);
        mlvKhoa = findViewById(R.id.lvKhoa);
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

        //Xử lý sự kiện click từng item
        adapter.setOnItemClickListener(new AdminFacultyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String facultyId) {
                // Lấy tên Khoa tương ứng với IdKhoa
                String facultyName = getFacultyNameById(facultyId);

                // Kiểm tra xem tên Khoa có null hay không
                if (facultyName != null) {
                    // Tạo Intent để chuyển sang AdminMajorListActivity
                    Intent intent = new Intent(AdminFacultyListActivity.this, AdminMajorListActivity.class);
                    intent.putExtra("facultyId", facultyId);
                    intent.putExtra("facultyName", facultyName);
                    intent.putExtra("title", "Danh sách các chuyên ngành Khoa " + facultyName);

                    // Chuyển sang AdminMajorListActivity
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    //2. Danh sách Khoa
    public ArrayList<String> getFacultyList() {
        ArrayList<String> facultyList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            //Truy vấn danh sách tên các khoa từ bảng Khoa
            cursor = db.query(TB_KHOA, new String[]{TB_KHOA_ID, TB_KHOA_TEN}, null, null, null, null, null);

            //Duyệt qua các dòng kết quả và thêm tên khoa vào danh sách
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String khoaId = cursor.getString(cursor.getColumnIndex(TB_KHOA_ID));
                    @SuppressLint("Range") String khoaTen = cursor.getString(cursor.getColumnIndex(TB_KHOA_TEN));
                    facultyList.add(khoaId + " - " + khoaTen); //Thêm vào danh sách dạng "Id - Tên"
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

        return facultyList;
    }

    //3. Hiển thị popup để thêm Khoa
    private void showAddFacultyPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_admin_faculty_add, null);
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

    //3.1 Thêm Khoa vào database
    private void addFacultyToDatabase(String facultyId, String facultyName) {
        // Tạo hoặc mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_KHOA_ID, facultyId);
        values.put(TB_KHOA_TEN, facultyName);

        // Thực hiện thêm Khoa vào cơ sở dữ liệu
        long newRowFaculty = db.insert(TB_KHOA, null, values);

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

    //4. Hiển thị popup để chỉnh sửa Khoa
    private void showEditFacultyPopup(String currentId, String currentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_admin_faculty_edit, null);
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

    //4.1 Cập nhật thông tin Khoa trong db
    private void updateFacultyInDatabase(String currentId, String newId, String newName) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_KHOA_ID, newId);
        values.put(TB_KHOA_TEN, newName);

        //Thực hiện cập nhật thông tin Khoa trong db
        int rowsAffectedFaculty = db.update(TB_KHOA, values, TB_KHOA_ID + " = ?", new String[]{currentId});
        updateMajorForeignKey(newId, currentId);
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

    // Phương thức cập nhật khóa ngoại trong bảng TB_CHUYENNGANH khi khóa chính của bảng TB_KHOA thay đổi
    private void updateMajorForeignKey(String newFacultyId, String oldFacultyId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Tạo truy vấn cập nhật khóa ngoại trong bảng TB_CHUYENNGANH
        String updateQuery = "UPDATE " + TB_CHUYENNGANH +
                " SET " + TB_CHUYENGANH_IDKHOA + " = ?" +
                " WHERE " + TB_CHUYENGANH_IDKHOA + " = ?";

        // Thực hiện truy vấn với giá trị mới và giá trị cũ của idKhoa
        String[] updateArgs = {newFacultyId, oldFacultyId};
        db.execSQL(updateQuery, updateArgs);

        // Đóng cơ sở dữ liệu
        db.close();
    }

    //5. Hiển thị xác nhận xóa
    private void showDeleteConfirmationDialog(String faculty) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn sẽ xóa luôn những chuyên ngành thuộc khoa này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Xác nhận xóa, gọi hàm xóa Khoa
                        deleteFaculty(faculty);
                        deleteRelatedMajors(faculty);
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

    //5.1 Xóa Khoa từ cơ sở dữ liệu
    private void deleteFaculty(String facultyId) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Thực hiện xóa Khoa từ cơ sở dữ liệu
        int rowsDeleted = db.delete(TB_KHOA, TB_KHOA_ID + "=?", new String[]{facultyId});

        // Kiểm tra xem có dữ liệu nào bị xóa không
        if (rowsDeleted > 0) {
            //Xóa thành công
            Toast.makeText(this, ("Đã xóa khoa "), Toast.LENGTH_SHORT).show();

            //Cập nhật lại danh sách Khoa trên ListView
            displayFacultyList();
        } else {
            //Xóa không thành công
            Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu
        db.close();
    }
    //Xóa tham chiếu đến khóa phụ bảng khác
    private void deleteRelatedMajors(String facultyId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Thực hiện xóa các chuyên ngành có FK = TB_KHOA_ID
        int deletedFKmajor = db.delete(TB_CHUYENNGANH, TB_CHUYENGANH_IDKHOA + " = ?", new String[]{facultyId});

        // Kiểm tra xem có bao nhiêu dòng đã bị xóa
        if (deletedFKmajor > 0) {
            // Có dòng bị xóa, thông báo thành công hoặc thực hiện các tác vụ khác tùy thuộc vào ứng dụng của bạn
            Toast.makeText(this, "Đã xóa " + deletedFKmajor + " chuyên ngành liên quan", Toast.LENGTH_SHORT).show();
        } else {
            // Không có dòng nào bị xóa, thông báo không có chuyên ngành nào được xóa
            Toast.makeText(this, "Không có chuyên ngành nào được xóa", Toast.LENGTH_SHORT).show();
        }

        //Thực hiện xóa các chuyên ngành có FK = TB_KHOA_ID
        int deletedFKsubject = db.delete(TB_MONHOC, TB_MONHOC_IDKHOA + " = ?", new String[]{facultyId});

        // Kiểm tra xem có bao nhiêu dòng được cập nhật
        if (deletedFKsubject > 0) {
            // Có dòng bị xóa, thông báo thành công hoặc thực hiện các tác vụ khác tùy thuộc vào ứng dụng của bạn
            Toast.makeText(this, "Đã xóa " + deletedFKsubject + " môn học liên quan", Toast.LENGTH_SHORT).show();
        } else {
            // Không có dòng nào bị xóa, thông báo không có chuyên ngành nào được xóa
            Toast.makeText(this, "Không có môn học nào được xóa", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu
        db.close();
    }

    //6. Phương thức này sẽ lấy tên Khoa từ IdKhoa
    @SuppressLint("Range")
    private String getFacultyNameById(String facultyId) {
        //Đọc database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Khởi tạo một biến String để lưu trữ tên Khoa
        String facultyName = null;

        //Thực hiện truy vấn
        Cursor cursor = db.rawQuery("SELECT " + TB_KHOA_TEN + " FROM " + TB_KHOA + " WHERE " + TB_KHOA_ID + " = ?", new String[]{facultyId});

        //Kiểm tra xem cursor có dữ liệu không
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy tên Khoa từ cursor
            facultyName = cursor.getString(cursor.getColumnIndex(TB_KHOA_TEN));
            // Đóng cursor
            cursor.close();
        }

        db.close();

        return facultyName;
    }
}