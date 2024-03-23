package com.example.doanltd;

import static database.dbHelper.TB_CHUYENGANH_IDKHOA;
import static database.dbHelper.TB_CHUYENNGANH;
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
import static database.dbHelper.TB_MONHOC;
import static database.dbHelper.TB_MONHOC_ID;
import static database.dbHelper.TB_MONHOC_IDKHOA;
import static database.dbHelper.TB_MONHOC_TEN;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.AdminSubjectListAdapter;
import adapter.AdminTeacherListAdapter;
import database.dbHelper;
import utils.utils;

public class AdminTeacherListActivity extends AppCompatActivity {
    database.dbHelper dbHelper;
    ListView mlvGV;
    ImageView mbtnGvToAdminHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher_list);

        dbHelper = new dbHelper(this);

        displayTeacherList();

        mbtnGvToAdminHome = findViewById(R.id.btnGvToAdHome);
        mbtnGvToAdminHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTeacherListActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Hiển thị danh sách GV
    private void displayTeacherList() {
        //Lấy danh sách khoa từ cơ sở dữ liệu
        ArrayList<String> arrayList = getTeacherList();

        //Tạo adapter và gắn với ListView
        AdminTeacherListAdapter adapter = new AdminTeacherListAdapter(this, arrayList);
        mlvGV = findViewById(R.id.lvGV);
        mlvGV.setAdapter(adapter);

        //Xử lý sự kiện cập nhật
        adapter.setOnEditClickListener(new AdminTeacherListAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(String teacherId, String teacherName) {
                showEditTeacherPopup(teacherId, teacherName);
            }
        });

        //Xử lý sự kiện xóa
        adapter.setOnDeleteClickListener(new AdminTeacherListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String teacherId, String teacherName) {
                showDeleteConfirmationDialog(teacherId, teacherName);
            }
        });

        //Xử lý sự kiện click từng item
        adapter.setOnItemClickListener(new AdminTeacherListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String teacherId, String teacherName) {
                showTeacherInfoDialog(teacherId, teacherName);
            }
        });
    }



    //Danh sách gv lên listview
    public ArrayList<String> getTeacherList() {
        ArrayList<String> teacherList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            //Truy vấn danh sách tên các khoa từ bảng gv
            cursor = db.query(TB_GIANGVIEN, new String[]{TB_GIANGVIEN_ID, TB_GIANGVIEN_HOTEN}, null, null, null, null, null);

            //Duyệt qua các dòng kết quả và thêm tên khoa vào danh sách
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String gvId = cursor.getString(cursor.getColumnIndex(TB_GIANGVIEN_ID));
                    @SuppressLint("Range") String gvTen = cursor.getString(cursor.getColumnIndex(TB_GIANGVIEN_HOTEN));
                    teacherList.add(gvId + " - " + gvTen); //Thêm vào danh sách dạng "Id - Tên"
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

        return teacherList;
    }



    //Hiển thị popup để chỉnh sửa giảng viên
    private void showEditTeacherPopup(String currentId, String currentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_edit_teacher, null);
        builder.setView(dialogView);

        //Khởi tạo các trường nhập liệu
        EditText edtNewGvId = dialogView.findViewById(R.id.edt_teacher_id);
        EditText edtNewGvTen = dialogView.findViewById(R.id.edt_teacher_name);
        EditText edtNewNgaySinh = dialogView.findViewById(R.id.edt_teacher_dob);
        RadioGroup rgNewGioiTinh = dialogView.findViewById(R.id.radio_group_gender);
        EditText edtNewEmail = dialogView.findViewById(R.id.edt_teacher_email);
        EditText edtNewSDT = dialogView.findViewById(R.id.edt_teacher_phone);
        Spinner spnNewGvKhoa = dialogView.findViewById(R.id.spn_gv_faculty);

        //Hiển thị thông tin hiện tại của gv trong các trường nhập liệu
        edtNewGvId.setText(currentId);
        edtNewGvTen.setText(currentName);
        rgNewGioiTinh.check(R.id.radio_male);
        String[] teacherInfo = getTeacherInfoFromDatabase(currentId);
        if (teacherInfo != null) {
            edtNewEmail.setText(teacherInfo[4]);
            edtNewSDT.setText(teacherInfo[5]);
        }

        //Lấy danh sách gv từ db
        ArrayList<String> facultyList = getFacultyList();

        //Tạo một ArrayAdapter để liên kết dữ liệu với Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facultyList);

        //Gán adapter cho Spinner
        spnNewGvKhoa.setAdapter(adapter);

        builder.setTitle("Chỉnh sửa thông tin giảng viên")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newGvId = edtNewGvId.getText().toString();
                        if (!utils.isValidIntNumer(Integer.parseInt(newGvId))){
                            Toast.makeText(AdminTeacherListActivity.this, "ID sai kiểu dữ liệu", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String newGvTen = edtNewGvTen.getText().toString();
                        String newNgaySinh = edtNewNgaySinh.getText().toString();
                        if (!utils.isValidDate(newNgaySinh)) {
                            Toast.makeText(AdminTeacherListActivity.this, "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String newGender = rgNewGioiTinh.getCheckedRadioButtonId() == R.id.radio_male ? "Nam" : "Nữ";
                        String newEmail = edtNewEmail.getText().toString();
                        if (!utils.isValidEmail(newEmail)) {
                            Toast.makeText(AdminTeacherListActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String newSDT = edtNewSDT.getText().toString();
                        if (!utils.isValidPhoneNumber(newSDT)) {
                            Toast.makeText(AdminTeacherListActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String newGvKhoa = getSelectedFacultyId(spnNewGvKhoa); // Lấy TB_KHOA_ID từ Spinner

                        //Gọi phương thức cập nhật vào db
                        updateTeacherInDatabase(currentId, newGvId, newGvTen, newNgaySinh, newGender, newEmail, newSDT, newGvKhoa);

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
    //Phương thức lấy ds Khoa
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
    //Lấy idkhoa để hiển thị spinner
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
    //Phương thức cập nhật gv vào db
    private void updateTeacherInDatabase(String currentId, String newId, String newName, String newNgaySinh,
                                         String newGender, String newEmail, String newSDT, String newFacultyId) {
        //Mở cơ sở dữ liệu để ghi
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Tạo một đối tượng ContentValues để chứa dữ liệu mới
        ContentValues values = new ContentValues();
        values.put(TB_GIANGVIEN_ID, newId);
        values.put(TB_GIANGVIEN_HOTEN, newName);
        values.put(TB_GIANGVIEN_NGAYSINH, newNgaySinh);
        values.put(TB_GIANGVIEN_GIOITINH, newGender);
        values.put(TB_GIANGVIEN_EMAIL, newEmail);
        values.put(TB_GIANGVIEN_SDT, newSDT);
        values.put(TB_GIANGVIEN_IDKHOA, newFacultyId);

        // hực hiện cập nhật thông tin Môn học trong db
        int rowsAffectedFaculty = db.update(TB_GIANGVIEN, values, TB_GIANGVIEN_ID + " = ?", new String[]{currentId});
        if (rowsAffectedFaculty > 0) {
            // Cập nhật thành công
            Toast.makeText(this, "Đã cập nhật dữ liệu giảng viên", Toast.LENGTH_SHORT).show();

            // Cập nhật lại danh sách môn học trên ListView
            displayTeacherList();
        } else {
            // Cập nhật thất bại
            Toast.makeText(this, "Cập nhật giảng viên thất bại", Toast.LENGTH_SHORT).show();
        }

        //Đóng db sau khi sửa xong
        db.close();
    }



    //Click item gv ra thông tin gv
    private void showTeacherInfoDialog(String teacherId, String teacherName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin giảng viên");

        // Tạo nội dung cho dialog
        StringBuilder dialogMessage = new StringBuilder();
        dialogMessage.append("ID: ").append(teacherId).append("\n");
        dialogMessage.append("Tên: ").append(teacherName).append("\n");

        // Lấy thông tin đầy đủ của giảng viên từ cơ sở dữ liệu và thêm vào nội dung dialog
        String[] teacherInfo = getTeacherInfoFromDatabase(teacherId);
        if (teacherInfo != null) {
            dialogMessage.append("Ngày sinh: ").append(teacherInfo[2]).append("\n");
            dialogMessage.append("Giới tính: ").append(teacherInfo[3]).append("\n");
            dialogMessage.append("Email: ").append(teacherInfo[4]).append("\n");
            dialogMessage.append("Số điện thoại: ").append(teacherInfo[5]).append("\n");
            dialogMessage.append("Khoa: ").append(teacherInfo[6]);
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
    //Lấy thông tin đầy đủ của giảng viên từ cơ sở dữ liệu
    private String[] getTeacherInfoFromDatabase(String teacherId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String[] teacherInfo = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TB_GIANGVIEN +
                    " LEFT JOIN " + TB_KHOA +
                    " ON " + TB_GIANGVIEN + "." + TB_GIANGVIEN_IDKHOA + " = " + TB_KHOA + "." + TB_KHOA_ID +
                    " WHERE " + TB_GIANGVIEN + "." + TB_GIANGVIEN_ID + " = ?", new String[]{teacherId});

            if (cursor != null && cursor.moveToFirst()) {
                teacherInfo = new String[]{
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2) != null ? cursor.getString(2) : "",
                        cursor.getString(3) != null ? cursor.getString(3) : "",
                        cursor.getString(4) != null ? cursor.getString(4) : "",
                        cursor.getString(5) != null ? cursor.getString(5) : "",
                        cursor.getString(6) != null ? cursor.getString(6) : ""
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



    //Hiển thị xác nhận xóa
    private void showDeleteConfirmationDialog(String teacherId, String teacherName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn sẽ xóa thông tin giảng viên và tài khoản của " + teacherName + "?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Lấy username của giảng viên từ cơ sở dữ liệu để xóa tài khoản
                        String teacherUsername = getTeacherUsername(teacherId);
                        if (teacherUsername != null) {
                            //Xác nhận xóa, gọi hàm xóa giảng viên và tài khoản
                            deleteTeacherAndUser(teacherId, teacherUsername);
                            displayTeacherList();
                        } else {
                            //Hiển thị thông báo nếu không tìm thấy username của giảng viên
                            Toast.makeText(AdminTeacherListActivity.this, "Không tìm thấy username của giảng viên", Toast.LENGTH_SHORT).show();
                        }
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
    //Xóa gv và tài khoản của gv
    private void deleteTeacherAndUser(String teacherId, String teacherUsername) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            // Xóa giảng viên
            int rowsDeletedTeacher = db.delete(TB_GIANGVIEN, TB_GIANGVIEN_ID + "=?", new String[]{teacherId});

            // Xóa tài khoản người dùng liên quan
            int rowsDeletedUser = db.delete(TB_USER, TB_User_Username + "=?", new String[]{teacherUsername});

            // Kiểm tra xem có thành công không
            if (rowsDeletedTeacher > 0 && rowsDeletedUser > 0) {
                // Xóa thành công cả giảng viên và tài khoản
                db.setTransactionSuccessful();
                Toast.makeText(this, "Đã xóa giảng viên và tài khoản liên quan", Toast.LENGTH_SHORT).show();
            } else {
                // Xóa thất bại
                Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Kết thúc transaction
            db.endTransaction();
            // Đóng cơ sở dữ liệu
            db.close();
        }
    }
    //Lấy username của gv trong bảng TB_USER để xóa tài khoản của gv
    @SuppressLint("Range")
    private String getTeacherUsername(String teacherId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String teacherUsername = null;

        try {
            cursor = db.query(TB_GIANGVIEN, new String[]{TB_GIANGVIEN_USERNAME}, TB_GIANGVIEN_ID + "=?", new String[]{teacherId}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                teacherUsername = cursor.getString(cursor.getColumnIndex(TB_GIANGVIEN_USERNAME));
            }
        } catch (Exception e) {
            Log.e("dbHelper", "Lỗi khi lấy thông tin username của giảng viên: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return teacherUsername;
    }
}