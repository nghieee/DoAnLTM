package com.example.doanltd;

import static database.dbHelper.TB_CHUYENGANH_IDKHOA;
import static database.dbHelper.TB_CHUYENNGANH;
import static database.dbHelper.TB_CHUYENNGANH_ID;
import static database.dbHelper.TB_CHUYENNGANH_TEN;
import static database.dbHelper.TB_GIANGVIEN;
import static database.dbHelper.TB_GIANGVIEN_EMAIL;
import static database.dbHelper.TB_GIANGVIEN_GIOITINH;
import static database.dbHelper.TB_GIANGVIEN_HOTEN;
import static database.dbHelper.TB_GIANGVIEN_ID;
import static database.dbHelper.TB_GIANGVIEN_IDKHOA;
import static database.dbHelper.TB_GIANGVIEN_NGAYSINH;
import static database.dbHelper.TB_GIANGVIEN_SDT;
import static database.dbHelper.TB_KHOA;
import static database.dbHelper.TB_KHOA_ID;
import static database.dbHelper.TB_KHOA_TEN;
import static database.dbHelper.TB_SINHVIEN;
import static database.dbHelper.TB_SINHVIEN_DIACHI;
import static database.dbHelper.TB_SINHVIEN_EMAIL;
import static database.dbHelper.TB_SINHVIEN_GIOITINH;
import static database.dbHelper.TB_SINHVIEN_HOTEN;
import static database.dbHelper.TB_SINHVIEN_ID;
import static database.dbHelper.TB_SINHVIEN_IDCHUYENNGANH;
import static database.dbHelper.TB_SINHVIEN_IDKHOA;
import static database.dbHelper.TB_SINHVIEN_NGAYSINH;
import static database.dbHelper.TB_SINHVIEN_NIENKHOA;
import static database.dbHelper.TB_SINHVIEN_SDT;
import static database.dbHelper.TB_USER;
import static database.dbHelper.TB_User_Username;

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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.AdminFacultyListAdapter;
import adapter.AdminStudentListAdapter;
import adapter.AdminTeacherListAdapter;
import database.dbHelper;
import utils.utils;

public class AdminStudentListActivity extends AppCompatActivity {
    FloatingActionButton mfabThemSV;
    database.dbHelper dbHelper;
    ListView lvStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_student_list_activity);
        mfabThemSV = (FloatingActionButton) findViewById(R.id.fabThemSV);

        dbHelper = new dbHelper(this);

        displayStudentList();

        mfabThemSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStudentPopup();
            }
        });
    }

    //1. Hiển thị danh sách sinh viên
    private void displayStudentList() {
        //Lấy danh sách khoa từ cơ sở dữ liệu
        ArrayList<String> arrayList = getStudentList();

        //Tạo adapter và gắn với ListView
        AdminStudentListAdapter adapter = new AdminStudentListAdapter(this, arrayList);
        lvStudent = findViewById(R.id.lvStudent);
        lvStudent.setAdapter(adapter);

        //Xử lý sự kiện cập nhật
        adapter.setOnEditClickListener(new AdminStudentListAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(String studentId, String studentName) {
                showEditStudentPopup(studentId, studentName);
            }
        });

        //Xử lý sự kiện xóa
        adapter.setOnDeleteClickListener(new AdminStudentListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String studentId) {
                showDeleteConfirmationDialog(studentId);
            }
        });

        //Xử lý sự kiện click từng item
        adapter.setOnItemClickListener(new AdminStudentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String studentId, String studentName) {
                showStudentInfoDialog(studentId, studentName);
            }
        });
    }
    //2. Danh sách sinh viên
    public ArrayList<String> getStudentList() {
        ArrayList<String> studentList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            //Truy vấn danh sách tên các khoa từ bảng Khoa
            cursor = db.query(TB_SINHVIEN, new String[]{TB_SINHVIEN_ID, TB_SINHVIEN_HOTEN}, null, null, null, null, null);

            //Duyệt qua các dòng kết quả và thêm tên khoa vào danh sách
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String sinhvienId = cursor.getString(cursor.getColumnIndex(TB_SINHVIEN_ID));
                    @SuppressLint("Range") String sihvienTen = cursor.getString(cursor.getColumnIndex(TB_SINHVIEN_HOTEN));
                    studentList.add(sinhvienId + " - " + sihvienTen); //Thêm vào danh sách dạng "Id - Tên"
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

        return studentList;
    }


    //Hiển thị dialog thêm sinh viên
    private void showAddStudentPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_admin_student_add, null);
        builder.setView(dialogView);

        // Khởi tạo các trường nhập liệu
        EditText edtSV_Id = dialogView.findViewById(R.id.edt_SV_Id);
        EditText edtSV_HoTen = dialogView.findViewById(R.id.edt_SV_HoTen);
        EditText edtSV_NgaySinh = dialogView.findViewById(R.id.edt_SV_NgaySinh);
        RadioGroup rgSV_GioiTinh = dialogView.findViewById(R.id.rg_SV_GioiTinh);
        EditText edtSV_Email = dialogView.findViewById(R.id.edt_SV_Email);
        EditText edtSV_NienKhoa = dialogView.findViewById(R.id.edt_SV_NienKhoa);
        EditText edtSV_DiaChi = dialogView.findViewById(R.id.edt_SV_DiaChi);
        EditText edtSV_SDT = dialogView.findViewById(R.id.edt_SV_SDT);
        Spinner spnSV_IdKhoa = dialogView.findViewById(R.id.spn_SV_IdKhoa);
        Spinner spnSV_IdChuyenNganh = dialogView.findViewById(R.id.spn_SV_IdChuyenNganh);

        //Lấy danh sách các khoa từ cơ sở dữ liệu và gắn vào Spinner
        ArrayList<String> facultyList = getFacultyList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facultyList);
        spnSV_IdKhoa.setAdapter(adapter);

        // Bắt sự kiện khi chọn một khoa từ Spinner để cập nhật danh sách chuyên ngành tương ứng
        spnSV_IdKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFacultyId = getSelectedFacultyId(spnSV_IdKhoa);
                ArrayList<String> majorList = getMajorList(selectedFacultyId);
                ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(AdminStudentListActivity.this, android.R.layout.simple_spinner_dropdown_item, majorList);
                spnSV_IdChuyenNganh.setAdapter(majorAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý khi không có gì được chọn
            }
        });

        builder.setTitle("Thêm sinh viên")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lấy dữ liệu từ ô nhập
                        String SV_Id = edtSV_Id.getText().toString();
                        String SV_HoTen = edtSV_HoTen.getText().toString();
                        String SV_NgaySinh = edtSV_NgaySinh.getText().toString();
                        if (!utils.isValidDate(SV_NgaySinh)) {
                            Toast.makeText(AdminStudentListActivity.this, "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String SV_GioiTinh = rgSV_GioiTinh.getCheckedRadioButtonId() == R.id.radio_male ? "Nam" : "Nữ";
                        String SV_Email = edtSV_Email.getText().toString();
                        if (!utils.isValidEmail(SV_Email)) {
                            Toast.makeText(AdminStudentListActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String SV_NienKhoa = edtSV_NienKhoa.getText().toString();
                        String SV_DiaChi = edtSV_DiaChi.getText().toString();
                        String SV_SDT = edtSV_SDT.getText().toString();
                        if (!utils.isValidPhoneNumber(SV_SDT)) {
                            Toast.makeText(AdminStudentListActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String SV_Khoa = getSelectedFacultyId(spnSV_IdKhoa); // Lấy TB_KHOA_ID từ Spinner
                        String SV_ChuyenNganh = getSelectedMajorId(spnSV_IdKhoa, spnSV_IdChuyenNganh);

                        // Thực hiện thêm chuyên ngành vào database với ID của Khoa đã lấy được
                        addStudentToDatabase(SV_Id, SV_HoTen, SV_NgaySinh, SV_GioiTinh, SV_Email, SV_NienKhoa, SV_DiaChi, SV_SDT,
                                SV_Khoa, SV_ChuyenNganh);

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
    public ArrayList<String> getMajorList(String facultyId) {
        ArrayList<String> majorList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Truy vấn danh sách tên các chuyên ngành từ bảng ChuyenNganh, lọc theo ID của khoa
            cursor = db.query(TB_CHUYENNGANH, new String[]{TB_CHUYENNGANH_ID, TB_CHUYENNGANH_TEN}, TB_CHUYENGANH_IDKHOA + " = ?", new String[]{facultyId}, null, null, null);

            // Duyệt qua các dòng kết quả và thêm tên chuyên ngành vào danh sách
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String chuyenNganhId = cursor.getString(cursor.getColumnIndex(TB_CHUYENNGANH_ID));
                    @SuppressLint("Range") String chuyenNganhTen = cursor.getString(cursor.getColumnIndex(TB_CHUYENNGANH_TEN));
                    majorList.add(chuyenNganhId + " - " + chuyenNganhTen); // Thêm vào danh sách dạng "Id - Tên"
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi thêm dữ liệu vào danh sách rồi: " + e.getMessage());
        } finally {
            // Đóng cursor sau khi sử dụng
            if (cursor != null) {
                cursor.close();
            }
        }

        // Đóng cơ sở dữ liệu sau khi sử dụng
        db.close();

        return majorList;
    }
    @SuppressLint("Range")
    public String getSelectedFacultyId(Spinner spinner) {
        int position = spinner.getSelectedItemPosition();
        String selectedFacultyId = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TB_KHOA, new String[]{TB_KHOA_ID}, null, null, null, null, null);
            if (cursor != null && cursor.moveToPosition(position)) {
                selectedFacultyId = cursor.getString(cursor.getColumnIndex(TB_KHOA_ID));
            } else {
                Toast.makeText(this, "Chưa có Khoa nào được tạo", Toast.LENGTH_SHORT).show();
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
    @SuppressLint("Range")
    public String getSelectedMajorId(Spinner facultySpinner, Spinner majorSpinner) {
        int position = majorSpinner.getSelectedItemPosition();
        String selectedMajorId = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Lấy ID của khoa được chọn từ Spinner
            String selectedFacultyId = getSelectedFacultyId(facultySpinner);

            // Truy vấn danh sách các chuyên ngành thuộc khoa được chọn
            cursor = db.query(TB_CHUYENNGANH, new String[]{TB_CHUYENNGANH_ID}, TB_CHUYENGANH_IDKHOA + " = ?", new String[]{selectedFacultyId}, null, null, null);

            if (cursor != null && cursor.moveToPosition(position)) {
                selectedMajorId = cursor.getString(cursor.getColumnIndex(TB_CHUYENNGANH_ID));
            } else {
                Toast.makeText(this, "Chưa có Chuyên Ngành nào được tạo", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi khi truy vấn Chuyên Ngành từ Spinner: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return selectedMajorId;
    }
    // Hàm thêm sinh viên vào cơ sở dữ liệu
    private void addStudentToDatabase(String id, String name, String dob, String gender, String email, String year, String address, String phone,
    String faculty, String major) {
        // Tạo hoặc mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_SINHVIEN_ID, id);
        values.put(TB_SINHVIEN_HOTEN, name);
        values.put(TB_SINHVIEN_NGAYSINH, dob);
        values.put(TB_SINHVIEN_GIOITINH, gender);
        values.put(TB_SINHVIEN_EMAIL, email);
        values.put(TB_SINHVIEN_NIENKHOA, year);
        values.put(TB_SINHVIEN_DIACHI, address);
        values.put(TB_SINHVIEN_SDT, phone);
        values.put(TB_SINHVIEN_IDKHOA, faculty);
        values.put(TB_SINHVIEN_IDCHUYENNGANH, major);

        // Thực hiện thêm sinh viên vào cơ sở dữ liệu
        long newRowId = db.insert(TB_SINHVIEN, null, values);

        // Kiểm tra xem thêm sinh viên thành công hay không
        if (newRowId != -1) {
            // Thêm sinh viên thành công
            Toast.makeText(this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
            displayStudentList();
        } else {
            // Thêm sinh viên thất bại
            Toast.makeText(this, "Thêm sinh viên thất bại!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu sau khi thêm xong
        db.close();
    }


    //Hiển thị popup để chỉnh sửa sinh viên
    private void showEditStudentPopup(String currentId, String currentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_admin_student_edit, null);
        builder.setView(dialogView);

        //Khởi tạo các trường nhập liệu
        EditText edtNewSVId = dialogView.findViewById(R.id.edt_student_id);
        EditText edtNewSVTen = dialogView.findViewById(R.id.edt_student_name);
        EditText edtNewSVNgaySinh = dialogView.findViewById(R.id.edt_student_dob);
        RadioGroup rgNewSVGioiTinh = dialogView.findViewById(R.id.radio_group_student_gender);
        EditText edtNewSVEmail = dialogView.findViewById(R.id.edt_student_email);
        EditText edtNewSVYear = dialogView.findViewById(R.id.edt_student_year);
        EditText edtNewSVAddress = dialogView.findViewById(R.id.edt_student_address);
        EditText edtNewSVSDT = dialogView.findViewById(R.id.edt_student_phone);
        Spinner spnNewSVKhoa = dialogView.findViewById(R.id.spn_student_faculty);
        Spinner spnNewSVChuyenNganh = dialogView.findViewById(R.id.spn_student_major);

        //Hiển thị thông tin hiện tại của gv trong các trường nhập liệu
        edtNewSVId.setText(currentId);
        edtNewSVTen.setText(currentName);
        rgNewSVGioiTinh.check(R.id.radio_male);
        String[] studentInfo = getStudentInfoFromDatabase(currentId);
        if (studentInfo != null) {
            edtNewSVNgaySinh.setText(studentInfo[2]);
            edtNewSVEmail.setText(studentInfo[4]);
            edtNewSVYear.setText(studentInfo[5]);
            edtNewSVAddress.setText(studentInfo[6]);
            edtNewSVSDT.setText(studentInfo[7]);
        }

        //Lấy danh sách khoa từ db và gán cho spiner
        ArrayList<String> facultyList = getFacultyList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facultyList);
        spnNewSVKhoa.setAdapter(adapter);

        // Thiết lập sự kiện chọn khoa để cập nhật danh sách chuyên ngành tương ứng
        spnNewSVKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFacultyId = getSelectedFacultyId(spnNewSVKhoa);
                ArrayList<String> majorList = getMajorList(selectedFacultyId);
                ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(AdminStudentListActivity.this, android.R.layout.simple_spinner_dropdown_item, majorList);
                spnNewSVChuyenNganh.setAdapter(majorAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý khi không có gì được chọn
            }
        });

        // Thiết lập danh sách chuyên ngành ban đầu cho sinh viên
        String selectedFacultyId = getSelectedFacultyId(spnNewSVKhoa);
        ArrayList<String> initialMajorList = getMajorList(selectedFacultyId);
        ArrayAdapter<String> initialMajorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, initialMajorList);
        spnNewSVChuyenNganh.setAdapter(initialMajorAdapter);

        builder.setTitle("Chỉnh sửa thông tin giảng viên")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lấy dữ liệu từ các trường nhập liệu
                        String newSvId = edtNewSVId.getText().toString();
                        String newSvTen = edtNewSVTen.getText().toString();
                        String newSvNgaySinh = edtNewSVNgaySinh.getText().toString();
                        String newSvGender = rgNewSVGioiTinh.getCheckedRadioButtonId() == R.id.radio_male ? "Nam" : "Nữ";
                        String newSvEmail = edtNewSVEmail.getText().toString();
                        String newSvYear = edtNewSVYear.getText().toString();
                        String newSvAddress = edtNewSVAddress.getText().toString();
                        String newSDT = edtNewSVSDT.getText().toString();

                        // Kiểm tra và xử lý dữ liệu đầu vào
                        if (!utils.isValidIntNumer(Integer.parseInt(newSvId))) {
                            Toast.makeText(AdminStudentListActivity.this, "ID sai kiểu dữ liệu", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!utils.isValidDate(newSvNgaySinh)) {
                            Toast.makeText(AdminStudentListActivity.this, "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!utils.isValidEmail(newSvEmail)) {
                            Toast.makeText(AdminStudentListActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!utils.isValidPhoneNumber(newSDT)) {
                            Toast.makeText(AdminStudentListActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Lấy thông tin khoa và chuyên ngành từ Spinner
                        String newSVKhoa = getSelectedFacultyId(spnNewSVKhoa);
                        String newSVChuyenNganh = getSelectedMajorId(spnNewSVKhoa, spnNewSVChuyenNganh);

                        // Gọi phương thức cập nhật vào cơ sở dữ liệu với thông tin mới
                        updateTeacherInDatabase(currentId, newSvId, newSvTen, newSvNgaySinh, newSvGender, newSvEmail, newSvYear, newSvAddress,
                                newSDT, newSVKhoa, newSVChuyenNganh);

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
    //Phương thức cập nhật gv vào db
    private void updateTeacherInDatabase(String currentId, String newId, String newName, String newNgaySinh,
                                         String newGender, String newEmail, String newYear, String newAddress,
                                         String newSDT, String newFacultyId, String newMajorId) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_SINHVIEN_ID, newId);
        values.put(TB_SINHVIEN_HOTEN, newName);
        values.put(TB_SINHVIEN_NGAYSINH, newNgaySinh);
        values.put(TB_SINHVIEN_GIOITINH, newGender);
        values.put(TB_SINHVIEN_EMAIL, newEmail);
        values.put(TB_SINHVIEN_NIENKHOA, newYear);
        values.put(TB_SINHVIEN_DIACHI, newAddress);
        values.put(TB_SINHVIEN_SDT, newSDT);
        values.put(TB_SINHVIEN_IDKHOA, newFacultyId);
        values.put(TB_SINHVIEN_IDCHUYENNGANH, newMajorId);

        // hực hiện cập nhật thông tin Môn học trong db
        int rowsAffectedFaculty = db.update(TB_SINHVIEN, values, TB_SINHVIEN_ID + " = ?", new String[]{currentId});
        if (rowsAffectedFaculty > 0) {
            // Cập nhật thành công
            Toast.makeText(this, "Đã cập nhật dữ liệu sinh viên", Toast.LENGTH_SHORT).show();

            // Cập nhật lại danh sách môn học trên ListView
            displayStudentList();
        } else {
            // Cập nhật thất bại
            Toast.makeText(this, "Cập nhật sinh viên thất bại", Toast.LENGTH_SHORT).show();
        }

        //Đóng db sau khi sửa xong
        db.close();
    }

    private void showDeleteConfirmationDialog(String studentId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn sẽ xóa thông tin sinh viên " + "?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteStudent(studentId);
                        displayStudentList();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

        //Tạo hộp thoại và hiển thị nó
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteStudent(String studentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            int rowsDeletedTeacher = db.delete(TB_SINHVIEN, TB_SINHVIEN_ID + "=?", new String[]{studentId});
            if (rowsDeletedTeacher > 0) {
                db.setTransactionSuccessful();
                Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đóng cơ sở dữ liệu
            db.close();
        }
    }


    //Click item sv ra thông tin sv
    private void showStudentInfoDialog(String studentId, String studentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin sinh viên");

        // Tạo nội dung cho dialog
        StringBuilder dialogMessage = new StringBuilder();
        dialogMessage.append("ID: ").append(studentId).append("\n");
        dialogMessage.append("Tên: ").append(studentName).append("\n");

        // Lấy thông tin đầy đủ của giảng viên từ cơ sở dữ liệu và thêm vào nội dung dialog
        String[] studentInfo = getStudentInfoFromDatabase(studentId);
        if (studentInfo != null) {
            dialogMessage.append("Ngày sinh: ").append(studentInfo[2]).append("\n");
            dialogMessage.append("Giới tính: ").append(studentInfo[3]).append("\n");
            dialogMessage.append("Email: ").append(studentInfo[4]).append("\n");
            dialogMessage.append("Niên khóa: ").append(studentInfo[5]).append("\n");
            dialogMessage.append("Địa chỉ: ").append(studentInfo[6]).append("\n");
            dialogMessage.append("Số điện thoại: ").append(studentInfo[7]).append("\n");
            dialogMessage.append("Khoa: ").append(studentInfo[8]).append("\n");
            dialogMessage.append("Chuyên ngành: ").append(studentInfo[9]);
        }

        builder.setMessage(dialogMessage.toString());

        // Tạo button "Đóng" để đóng dialog
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //Lấy thông tin sinh viên trong database
    private String[] getStudentInfoFromDatabase(String studentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String[] studentInfo = null;

        try {
            cursor = db.rawQuery("SELECT " +
                    TB_SINHVIEN + "." + TB_SINHVIEN_ID + ", " +
                    TB_SINHVIEN + "." + TB_SINHVIEN_HOTEN + ", " +
                    TB_SINHVIEN + "." + TB_SINHVIEN_NGAYSINH + ", " +
                    TB_SINHVIEN + "." + TB_SINHVIEN_GIOITINH + ", " +
                    TB_SINHVIEN + "." + TB_SINHVIEN_EMAIL + ", " +
                    TB_SINHVIEN + "." + TB_SINHVIEN_NIENKHOA + ", " +
                    TB_SINHVIEN + "." + TB_SINHVIEN_DIACHI + ", " +
                    TB_SINHVIEN + "." + TB_SINHVIEN_SDT + ", " +
                    TB_KHOA + "." + TB_KHOA_TEN + ", " +
                    TB_CHUYENNGANH + "." + TB_CHUYENNGANH_TEN +
                    " FROM " + TB_SINHVIEN +
                    " LEFT JOIN " + TB_KHOA +
                    " ON " + TB_SINHVIEN + "." + TB_SINHVIEN_IDKHOA + " = " + TB_KHOA + "." + TB_KHOA_ID +
                    " LEFT JOIN " + TB_CHUYENNGANH +
                    " ON " + TB_SINHVIEN + "." + TB_SINHVIEN_IDCHUYENNGANH + " = " + TB_CHUYENNGANH + "." + TB_CHUYENNGANH_ID +
                    " WHERE " + TB_SINHVIEN + "." + TB_SINHVIEN_ID + " = ?", new String[]{studentId});


            if (cursor != null && cursor.moveToFirst()) {
                studentInfo = new String[]{
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2) != null ? cursor.getString(2) : "",
                        cursor.getString(3) != null ? cursor.getString(3) : "",
                        cursor.getString(4) != null ? cursor.getString(4) : "", //email
                        cursor.getString(5) != null ? cursor.getString(5) : "", //niên khóa
                        cursor.getString(6) != null ? cursor.getString(6) : "",
                        cursor.getString(7) != null ? cursor.getString(7) : "", //sdt
                        cursor.getString(8) != null ? cursor.getString(8) : "",
                        cursor.getString(9) != null ? cursor.getString(9) : ""
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

        return studentInfo;
    }
}