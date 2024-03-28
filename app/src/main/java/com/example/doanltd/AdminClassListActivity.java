package com.example.doanltd;

import static database.dbHelper.TB_GIANGVIEN;
import static database.dbHelper.TB_GIANGVIEN_HOTEN;
import static database.dbHelper.TB_GIANGVIEN_ID;
import static database.dbHelper.TB_GIANGVIEN_IDKHOA;
import static database.dbHelper.TB_KHOA;
import static database.dbHelper.TB_KHOA_ID;
import static database.dbHelper.TB_KHOA_TEN;
import static database.dbHelper.TB_LOPHOCPHAN;
import static database.dbHelper.TB_LOPHOCPHAN_ID;
import static database.dbHelper.TB_LOPHOCPHAN_IDGIANGVIEN;
import static database.dbHelper.TB_LOPHOCPHAN_IDKHOA;
import static database.dbHelper.TB_LOPHOCPHAN_IDMONHOC;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.AdminFacultyListAdapter;
import database.dbHelper;

public class AdminClassListActivity extends AppCompatActivity {
    FloatingActionButton mfabThemLop;
    static database.dbHelper dbHelper;
    ListView mlvLop;
    ImageView mbtnClassToAdminHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_class_list_activity);

        mbtnClassToAdminHome = findViewById(R.id.btnClassToAdminHome);
        mfabThemLop = findViewById(R.id.fabThemLop);
        dbHelper = new dbHelper(this);
        displayClassList();

        mfabThemLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddClassPopup();
            }
        });

        mbtnClassToAdminHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminClassListActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void displayClassList() {
        ArrayList<String> arrayList = getClassList();
        AdminFacultyListAdapter adapter = new AdminFacultyListAdapter(this, arrayList);
        mlvLop = findViewById(R.id.lvLop);
        mlvLop.setAdapter(adapter);
    }
    public ArrayList<String> getClassList() {
        ArrayList<String> classList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TB_LOPHOCPHAN, new String[]{TB_LOPHOCPHAN_ID, TB_LOPHOCPHAN_IDMONHOC, TB_LOPHOCPHAN_IDGIANGVIEN}, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String lop_Id = cursor.getString(cursor.getColumnIndex(TB_LOPHOCPHAN_ID));
                    @SuppressLint("Range") String lop_IdMonHoc = cursor.getString(cursor.getColumnIndex(TB_LOPHOCPHAN_IDMONHOC));
                    @SuppressLint("Range") String lop_IdGV = cursor.getString(cursor.getColumnIndex(TB_LOPHOCPHAN_IDGIANGVIEN));
                    classList.add(lop_Id + " - " + lop_IdMonHoc + " - " + lop_IdGV);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi thêm dữ liệu vào danh sách rồi: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        db.close();

        return classList;
    }


    private void showAddClassPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_admin_class_add, null);
        builder.setView(dialogView);

        // Khởi tạo các trường nhập liệu
        EditText edtClassId = dialogView.findViewById(R.id.edt_ma_lop);
        Spinner spnFaculty = dialogView.findViewById(R.id.spinner_khoa);
        Spinner spnCourse = dialogView.findViewById(R.id.spinner_mon_hoc);
        Spinner spnTeacher = dialogView.findViewById(R.id.spinner_giang_vien);

        //Lấy danh sách các khoa từ cơ sở dữ liệu và gắn vào Spinner
        ArrayList<String> facultyList = getFacultyList();
        ArrayAdapter<String> facultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facultyList);
        spnFaculty.setAdapter(facultyAdapter);

        //Bắt sự kiện khi chọn một khoa từ Spinner để cập nhật danh sách môn học và giảng viên tương ứng
        spnFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFacultyId = getSelectedFacultyId(spnFaculty);
                ArrayList<String> courseList = getCourseList(selectedFacultyId);
                ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(AdminClassListActivity.this, android.R.layout.simple_spinner_dropdown_item, courseList);
                spnCourse.setAdapter(courseAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý khi không có gì được chọn
            }
        });
        spnTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFacultyId = getSelectedFacultyId(spnFaculty);
                ArrayList<String> teacherList = getCourseList(selectedFacultyId);
                ArrayAdapter<String> teacherAdapter = new ArrayAdapter<>(AdminClassListActivity.this, android.R.layout.simple_spinner_dropdown_item, teacherList);
                spnTeacher.setAdapter(teacherAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý khi không có gì được chọn
            }
        });

        builder.setTitle("Thêm lớp học")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String classId = edtClassId.getText().toString();
                        String selectedFacultyId = getSelectedFacultyId(spnFaculty);
                        String selectedTeacherAndCourse = spnTeacher.getSelectedItem().toString();

                        String[] parts = selectedTeacherAndCourse.split(":");
                        String teacherId = parts[1].trim().split("-")[0].trim();
                        String courseId = parts[3].trim().split("-")[0].trim();

                        addClassToDatabase(classId, selectedFacultyId, teacherId, courseId);

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
    public ArrayList<String> getTeacherList(String selectedFacultyId) {
        ArrayList<String> teacherList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + TB_GIANGVIEN_ID + ", " + TB_GIANGVIEN_HOTEN +
                    " FROM " + TB_GIANGVIEN +
                    " WHERE " + TB_GIANGVIEN_IDKHOA + " = ?";
            cursor = db.rawQuery(query, new String[]{selectedFacultyId});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String teacherId = cursor.getString(cursor.getColumnIndex(TB_GIANGVIEN_ID));
                    @SuppressLint("Range") String teacherName = cursor.getString(cursor.getColumnIndex(TB_GIANGVIEN_HOTEN));
                    teacherList.add("Giảng viên: " + teacherId + " - " + teacherName);
                } while (cursor.moveToNext());
            } else {
                teacherList.add("Không có giảng viên thuộc khoa này.");
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi thêm dữ liệu vào danh sách giảng viên: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        db.close();
        return teacherList;
    }
    public ArrayList<String> getCourseList(String selectedFacultyId) {
        ArrayList<String> courseList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + TB_MONHOC_ID + ", " + TB_MONHOC_TEN +
                    " FROM " + TB_MONHOC +
                    " WHERE " + TB_MONHOC_IDKHOA + " = ?";
            cursor = db.rawQuery(query, new String[]{selectedFacultyId});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String courseId = cursor.getString(cursor.getColumnIndex(TB_MONHOC_ID));
                    @SuppressLint("Range") String courseName = cursor.getString(cursor.getColumnIndex(TB_MONHOC_TEN));
                    courseList.add("Môn học: " + courseId + " - " + courseName);
                } while (cursor.moveToNext());
            } else {
                courseList.add("Không có môn học thuộc khoa này.");
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi thêm dữ liệu vào danh sách môn học: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        db.close();
        return courseList;
    }
    public ArrayList<String> getFacultyList() {
        ArrayList<String> facultyList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Truy vấn danh sách các khoa từ bảng Khoa
            cursor = db.query(TB_KHOA, new String[]{TB_KHOA_ID, TB_KHOA_TEN}, null, null, null, null, null);

            // Duyệt qua các dòng kết quả và thêm tên khoa vào danh sách
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String facultyId = cursor.getString(cursor.getColumnIndex(TB_KHOA_ID));
                    @SuppressLint("Range") String facultyName = cursor.getString(cursor.getColumnIndex(TB_KHOA_TEN));
                    facultyList.add(facultyId + " - " + facultyName); // Thêm vào danh sách dạng "Id - Tên"
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi khi lấy danh sách khoa từ cơ sở dữ liệu: " + e.getMessage());
        } finally {
            // Đóng cursor sau khi sử dụng
            if (cursor != null) {
                cursor.close();
            }
            // Đóng cơ sở dữ liệu sau khi sử dụng
            db.close();
        }

        return facultyList;
    }
    public String getSelectedFacultyId(Spinner facultySpinner) {
        String selectedFacultyId = null;

        // Lấy vị trí của mục đã chọn trong Spinner
        int position = facultySpinner.getSelectedItemPosition();

        // Kiểm tra nếu vị trí là -1 (không có mục nào được chọn)
        if (position != -1) {
            // Lấy danh sách khoa từ Spinner
            ArrayList<String> facultyList = (ArrayList<String>) facultySpinner.getTag();

            // Kiểm tra nếu danh sách không rỗng và vị trí nằm trong phạm vi của danh sách
            if (facultyList != null && position < facultyList.size()) {
                // Tách mã khoa từ chuỗi "Id - Tên"
                String[] parts = facultyList.get(position).split(" - ");
                if (parts.length > 0) {
                    selectedFacultyId = parts[0]; // Lấy mã khoa
                }
            }
        }

        return selectedFacultyId;
    }
    private void addClassToDatabase(String classId, String selectedFacultyId, String courseId, String teacherId) {
        // Tạo hoặc mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_LOPHOCPHAN_ID, classId);
        values.put(TB_LOPHOCPHAN_IDKHOA, selectedFacultyId);
        values.put(TB_LOPHOCPHAN_IDMONHOC, courseId);
        values.put(TB_LOPHOCPHAN_IDGIANGVIEN, teacherId);

        // Thực hiện thêm lớp vào cơ sở dữ liệu
        long newRowId = db.insert(TB_LOPHOCPHAN, null, values);

        // Kiểm tra xem thêm lớp thành công hay không
        if (newRowId != -1) {
            // Thêm lớp thành công
            Toast.makeText(this, "Thêm lớp thành công!", Toast.LENGTH_SHORT).show();
            // Hiển thị danh sách lớp mới
            displayClassList();
        } else {
            // Thêm lớp thất bại
            Toast.makeText(this, "Thêm lớp thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu sau khi thêm xong
        db.close();
    }


}