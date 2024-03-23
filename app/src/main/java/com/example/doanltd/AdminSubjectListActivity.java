package com.example.doanltd;

import static database.dbHelper.TB_KHOA;
import static database.dbHelper.TB_KHOA_ID;
import static database.dbHelper.TB_KHOA_TEN;
import static database.dbHelper.TB_MONHOC;
import static database.dbHelper.TB_MONHOC_ID;
import static database.dbHelper.TB_MONHOC_IDKHOA;
import static database.dbHelper.TB_MONHOC_TEN;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.AdminFacultyListAdapter;
import adapter.AdminSubjectListAdapter;
import database.dbHelper;

public class AdminSubjectListActivity extends AppCompatActivity {
    FloatingActionButton mfabThemMonHoc;
    ListView mlvMonHoc;
    database.dbHelper dbHelper;
    ImageButton mbtnAdSubjectToAdHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_subject_list);

        dbHelper = new dbHelper(this);

        displaySubjectList();

        mfabThemMonHoc = findViewById(R.id.fabThemMonHoc);
        mfabThemMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSubjectPopup();
            }
        });

        mbtnAdSubjectToAdHome = findViewById(R.id.btnAdSubjectToAdHome);
        mbtnAdSubjectToAdHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSubjectListActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //1. Hiển thị danh sách môn học
    private void displaySubjectList() {
        //Lấy danh sách môn học từ cơ sở dữ liệu
        ArrayList<String> arrayList = getSubjectList();

        //Tạo adapter và gắn với ListView
        AdminSubjectListAdapter adapter = new AdminSubjectListAdapter(this, arrayList);
        mlvMonHoc = findViewById(R.id.lvMonHoc);
        mlvMonHoc.setAdapter(adapter);

        //Xử lý sự kiện cập nhật
        adapter.setOnEditClickListener(new AdminSubjectListAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(String subjectId, String subjectName, String subject_majorID) {
                showEditSubjectPopup(subjectId, subjectName, subject_majorID);
            }
        });

        //Xử lý sự kiện xóa
        adapter.setOnDeleteClickListener(new AdminSubjectListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String subjectId, String subjectName) {
                showDeleteConfirmationDialog(subjectId, subjectName);
            }
        });
    }
    //2. Danh sách môn học
    public ArrayList<String> getSubjectList() {
        ArrayList<String> subjectList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Truy vấn danh sách môn học kèm theo tên khoa
            cursor = db.rawQuery("SELECT " + TB_MONHOC + "." + TB_MONHOC_ID + ", " + TB_MONHOC + "." + TB_MONHOC_TEN + ", " + TB_KHOA + "." + TB_KHOA_TEN +
                    " FROM " + TB_MONHOC +
                    " INNER JOIN " + TB_KHOA +
                    " ON " + TB_MONHOC + "." + TB_MONHOC_IDKHOA + " = " + TB_KHOA + "." + TB_KHOA_ID, null);


            //Duyệt qua các dòng kết quả và thêm vào danh sách
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String monhocId = cursor.getString(0); //TB_MONHOC_ID
                    String monhocTen = cursor.getString(1); //TB_MONHOC_TEN
                    String khoaTen = cursor.getString(2); //TB_KHOA_TEN
                    subjectList.add(monhocId + " - " + monhocTen + " - " + khoaTen); //Thêm vào danh sách dạng "Id - Tên - IdKhoa"
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi thêm dữ liệu vào danh sách môn học: " + e.getMessage());
        } finally {
            //Đóng cursor sau khi sử dụng
            if (cursor != null) {
                cursor.close();
            }
        }

        //Đóng cơ sở dữ liệu sau khi sử dụng
        db.close();

        return subjectList;
    }

    //3. Hiển thị popup để thêm Khoa
    private void showAddSubjectPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_add_subject, null);
        builder.setView(dialogView);

        // Khởi tạo các trường nhập liệu
        EditText medt_MonHoc_Id = dialogView.findViewById(R.id.edt_MonHoc_ID);
        EditText medt_MonHoc_Ten = dialogView.findViewById(R.id.edt_MonHoc_Ten);
        Spinner mspnMonHocIdKhoa = dialogView.findViewById(R.id.spn_MonHoc_IdKhoa); // Thêm spinner

        // Lấy danh sách các khoa từ cơ sở dữ liệu và gắn vào Spinner
        ArrayList<String> facultyList = getFacultyList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facultyList);
        mspnMonHocIdKhoa.setAdapter(adapter);

        builder.setTitle("Thêm Môn Học")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Lấy dữ liệu từ ô nhập
                        String MonHoc_Id = medt_MonHoc_Id.getText().toString();
                        String MonHoc_Ten = medt_MonHoc_Ten.getText().toString();
                        String selectedFacultyId = getSelectedFacultyId(mspnMonHocIdKhoa); // Lấy TB_KHOA_ID từ Spinner

                        // Thực hiện thêm vào database
                        addSubjectToDatabase(MonHoc_Id, MonHoc_Ten, selectedFacultyId);

                        // Đóng dialog sau khi thêm
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

    // Lấy TB_KHOA_ID từ Spinner đã chọn
    @SuppressLint("Range")
    private String getSelectedFacultyId(Spinner spinner) {
        int position = spinner.getSelectedItemPosition();
        String selectedFacultyId = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TB_KHOA, new String[]{TB_KHOA_ID}, null, null, null, null, null);
            if (cursor != null && cursor.moveToPosition(position)) {
                selectedFacultyId = cursor.getString(cursor.getColumnIndex(TB_KHOA_ID));
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi khi truy vấn TB_KHOA_ID từ Spinner: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return selectedFacultyId;
    }
    public ArrayList<String> getFacultyList() {
        ArrayList<String> facultyList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TB_KHOA, new String[]{TB_KHOA_ID, TB_KHOA_TEN}, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String khoaId = cursor.getString(cursor.getColumnIndex(TB_KHOA_ID));
                    @SuppressLint("Range") String khoaTen = cursor.getString(cursor.getColumnIndex(TB_KHOA_TEN));
                    facultyList.add(khoaId + " - " + khoaTen); // Thêm vào danh sách dạng "Id - Tên"
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi khi lấy danh sách khoa từ cơ sở dữ liệu: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return facultyList;
    }

    //3.1 Thêm vào database
    private void addSubjectToDatabase(String subjectId, String subjectName, String selectedFacultyId) {
        //Tạo hoặc mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_MONHOC_ID, subjectId);
        values.put(TB_MONHOC_TEN, subjectName);
        values.put(TB_MONHOC_IDKHOA, selectedFacultyId); // Thêm trường IDKHOA

        //Thực hiện thêm vào cơ sở dữ liệu
        long newRowFaculty = db.insert(TB_MONHOC, null, values);

        //Kiểm tra xem thêm thành công hay không
        if (newRowFaculty != -1) {
            //Thêm Môn học thành công
            Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            //Cập nhật lại danh sách Khoa trên ListView
            displaySubjectList();
        } else {
            //Thêm Môn học thất bại
            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu sau khi thêm xong
        db.close();
    }

    //4. Hiển thị popup để chỉnh sửa Môn học
    private void showEditSubjectPopup(String currentId, String currentName, String currentFacultyId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_edit_subject, null);
        builder.setView(dialogView);

        //Khởi tạo các trường nhập liệu
        EditText edtNewMonHocId = dialogView.findViewById(R.id.edt_newMonHocId);
        EditText edtNewMonHocTen = dialogView.findViewById(R.id.edt_newMonHocTen);
        Spinner mspnNewMonHocIdKhoa = dialogView.findViewById(R.id.spn_newMonHoc_IdKhoa);

        //Hiển thị thông tin hiện tại của Môn học trong các trường nhập liệu
        edtNewMonHocId.setText(currentId);
        edtNewMonHocTen.setText(currentName);

        //Lấy danh sách khoa từ cơ sở dữ liệu
        ArrayList<String> facultyList = getFacultyList();

        //Tạo một ArrayAdapter để liên kết dữ liệu với Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facultyList);

        //Gán adapter cho Spinner
        mspnNewMonHocIdKhoa.setAdapter(adapter);

        //Set giá trị mặc định cho Spinner là khoa hiện tại của môn học
        int index = getFacultyList().indexOf(currentFacultyId);
        if (index != -1) {
            mspnNewMonHocIdKhoa.setSelection(index);
        }

        builder.setTitle("Chỉnh sửa thông tin Môn Học")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newMonHocId = edtNewMonHocId.getText().toString();
                        String newMonHocTen = edtNewMonHocTen.getText().toString();
                        String selectedFacultyId = getSelectedFacultyId(mspnNewMonHocIdKhoa); // Lấy TB_KHOA_ID từ Spinner

                        //Thực hiện cập nhật thông tin Môn học trong cơ sở dữ liệu
                        updateSubjectInDatabase(currentId, newMonHocId, newMonHocTen, selectedFacultyId);

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

    //4.1 Cập nhật thông tin môn trong db
    private void updateSubjectInDatabase(String currentId, String newId, String newName, String selectedFacultyId) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_MONHOC_ID, newId);
        values.put(TB_MONHOC_TEN, newName);
        values.put(TB_MONHOC_IDKHOA, selectedFacultyId); // Thêm trường IDKHOA

        // hực hiện cập nhật thông tin Môn học trong db
        int rowsAffectedFaculty = db.update(TB_MONHOC, values, TB_MONHOC_ID + " = ?", new String[]{currentId});
        if (rowsAffectedFaculty > 0) {
            // Cập nhật thành công
            Toast.makeText(this, "Thông tin Môn Học đã được cập nhật!", Toast.LENGTH_SHORT).show();

            // Cập nhật lại danh sách môn học trên ListView
            displaySubjectList();
        } else {
            // Cập nhật thất bại
            Toast.makeText(this, "Cập nhật thông tin Môn Học thất bại!", Toast.LENGTH_SHORT).show();
        }

        //Đóng db sau khi sửa xong
        db.close();
    }

    //5. Hiển thị xác nhận xóa
    private void showDeleteConfirmationDialog(String subjectId, String subjectName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Xác nhận xóa môn học " + subjectName + "?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Xác nhận xóa, gọi hàm xóa Môn học
                        deleteSubject(subjectId);
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

    private void deleteSubject(String subjectId) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Thực hiện xóa từ cơ sở dữ liệu
        int rowsDeleted = db.delete(TB_MONHOC, TB_MONHOC_ID + "=?", new String[]{subjectId});

        // Kiểm tra xem có dữ liệu nào bị xóa không
        if (rowsDeleted > 0) {
            //Xóa thành công
            Toast.makeText(this, ("Đã xóa môn học"), Toast.LENGTH_SHORT).show();

            //Cập nhật lại danh sách trên ListView
            displaySubjectList();
        } else {
            //Xóa không thành công
            Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu
        db.close();
    }
}