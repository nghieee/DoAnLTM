package database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class dbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "dbDoAn";
    private static final int DB_VERSION = 1;

    //Biến tĩnh bảng User
    public static final String TB_USER = "User";
    public static final String TB_User_Username = "Username";
    public static final String TB_User_MatKhau = "MatKhau";
    public static final String TB_User_HoTen = "HoTen";
    public static final String TB_User_Email = "Email";
    public static final String TB_User_Role = "Role";
    public static final String TB_User_SDT = "SDT";

    //Biến tĩnh bảng Khoa
    public static final String TB_KHOA = "Khoa";
    public static final String TB_KHOA_ID = "Id";
    public static final String TB_KHOA_TEN = "Ten";

    //Biến tĩnh bảng Giảng Viên
    public static final String TB_GIANGVIEN = "GiangVien";
    public static final String TB_GIANGVIEN_ID = "Id";
    public static final String TB_GIANGVIEN_HOTEN = "HoTen";
    public static final String TB_GIANGVIEN_NGAYSINH = "NgaySinh";
    public static final String TB_GIANGVIEN_GIOITINH = "GioiTinh";
    public static final String TB_GIANGVIEN_EMAIL = "Email";
    public static final String TB_GIANGVIEN_SDT = "SDT";
    public static final String TB_GIANGVIEN_IDKHOA = "IdKhoa";

    //Biến tĩnh bảng Chuyên Ngành
    public static final String TB_CHUYENNGANH = "ChuyenNganh";
    public static final String TB_CHUYENNGANH_ID = "Id";
    public static final String TB_CHUYENNGANH_TEN = "Ten";
    public static final String TB_CHUYENGANH_IDKHOA = "IdKhoa";

    //Biến tĩnh bảng Môn Học
    public static final String TB_MONHOC = "MonHoc";
    public static final String TB_MONHOC_ID = "Id";
    public static final String TB_MONHOC_TEN = "Ten";
    public static final String TB_MONHOC_MOTA = "MoTa";
    public static final String TB_MONHOC_IDKHOA = "IdKhoa";

    //Biến tĩnh bảng Lớp Học Phần
    public static final String TB_LOPHOCPHAN = "LopHocPhan";
    public static final String TB_LOPHOCPHAN_ID = "Id";
    public static final String TB_LOPHOCPHAN_IDMONHOC = "IdMonHoc";
    public static final String TB_LOPHOCPHAN_IDGIANGVIEN = "IdGV";
    public static final String TB_LOPHOCPHAN_HOCKY = "HocKy";
    public static final String TB_LOPHOCPHAN_SOLUONGSV = "SoLuongSV";

    //Biến tĩnh bảng Sinh Viên
    public static final String TB_SINHVIEN = "SinhVien";
    public static final String TB_SINHVIEN_ID = "Id";
    public static final String TB_SINHVIEN_HOTEN = "HoTen";
    public static final String TB_SINHVIEN_NGAYSINH = "NgaySinh";
    public static final String TB_SINHVIEN_GIOITINH = "GioiTinh";
    public static final String TB_SINHVIEN_EMAIL = "Email";
    public static final String TB_SINHVIEN_NIENKHOA = "NienKhoa";
    public static final String TB_SINHVIEN_DIACHI = "DiaChi";
    public static final String TB_SINHVIEN_SDT = "SDT";
    public static final String TB_SINHVIEN_IDKHOA = "IdKhoa";
    public static final String TB_SINHVIEN_IDCHUYENNGANH = "IdChuyenNganh";

    //Biến tĩnh bảng Tham Gia Lớp Học
    public static final String TB_THAMGIALOPHOC = "ThamGiaLopHoc";
    public static final String TB_THAMGIALOPHOC_IDLOP = "IdLop";
    public static final String TB_THAMGIALOPHOC_IDSV = "IdSV";
    public static final String TB_THAMGIALOPHOC_DIEMTHI = "DiemThi";
    public static final String TB_THAMGIALOPHOC_DIEMQUATRINH = "DiemQuaTrinh";
    public static final String TB_THAMGIALOPHOC_DIEMHOCPHAN = "DiemHocPhan";

    public dbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tạo bảng User
        String createUserTableQuery = "CREATE TABLE " + TB_USER + " (" +
                TB_User_Username + " TEXT NOT NULL UNIQUE, " +
                TB_User_MatKhau + " TEXT, " +
                TB_User_HoTen + " TEXT, " +
                TB_User_Email + " TEXT, " +
                TB_User_Role + " INTEGER DEFAULT 0 CHECK(" + TB_User_Role + " IN (0, 1)), " +
                TB_User_SDT + " TEXT, " +
                "PRIMARY KEY (" + TB_User_Username + ")" +
                ")";
        db.execSQL(createUserTableQuery);

        //Tạo bảng Khoa
        String createKhoaTableQuery = "CREATE TABLE " + TB_KHOA + " (" +
                TB_KHOA_ID + " INTEGER NOT NULL UNIQUE, " +
                TB_KHOA_TEN + " TEXT, " +
                "PRIMARY KEY (" + TB_KHOA_ID + ")" +
                ")";
        db.execSQL(createKhoaTableQuery);

        //Tạo bảng GiangVien
        String createGiangVienTableQuery = "CREATE TABLE " + TB_GIANGVIEN + " (" +
                TB_GIANGVIEN_ID + " INTEGER NOT NULL UNIQUE, " +
                TB_GIANGVIEN_HOTEN + " TEXT, " +
                TB_GIANGVIEN_NGAYSINH + " TEXT, " +
                TB_GIANGVIEN_GIOITINH + " TEXT, " +
                TB_GIANGVIEN_EMAIL + " TEXT, " +
                TB_GIANGVIEN_SDT + " TEXT, " +
                TB_GIANGVIEN_IDKHOA + " INTEGER, " +
                "FOREIGN KEY (" + TB_GIANGVIEN_IDKHOA + ") REFERENCES " + TB_KHOA + "(" + TB_KHOA_ID + "), " +
                "PRIMARY KEY (" + TB_GIANGVIEN_ID + ")" +
                ")";
        db.execSQL(createGiangVienTableQuery);

        //Tạo bảng ChuyenNganh
        String createChuyenNganhTableQuery = "CREATE TABLE " + TB_CHUYENNGANH + " (" +
                TB_CHUYENNGANH_ID + " INTEGER PRIMARY KEY, " +
                TB_CHUYENNGANH_TEN + " TEXT, " +
                TB_CHUYENGANH_IDKHOA + " INTEGER, " +
                "FOREIGN KEY (" + TB_CHUYENGANH_IDKHOA + ") REFERENCES " + TB_KHOA + "(" + TB_KHOA_ID + ")" +
                ")";
        db.execSQL(createChuyenNganhTableQuery);


        //Tạo bảng MonHoc
        String createMonHocTableQuery = "CREATE TABLE " + TB_MONHOC + " (" +
                TB_MONHOC_ID + " INTEGER NOT NULL UNIQUE, " +
                TB_MONHOC_TEN + " TEXT, " +
                TB_MONHOC_MOTA + " TEXT, " +
                TB_MONHOC_IDKHOA + " INTEGER, " +
                "FOREIGN KEY (" + TB_MONHOC_IDKHOA + ") REFERENCES " + TB_KHOA + "(" + TB_KHOA_ID + "), " +
                "PRIMARY KEY (" + TB_MONHOC_ID + ")" +
                ")";
        db.execSQL(createMonHocTableQuery);

        //Tạo bảng LopHocPhan
        String createLopHocPhanTableQuery = "CREATE TABLE " + TB_LOPHOCPHAN + " (" +
                TB_LOPHOCPHAN_ID + " INTEGER NOT NULL UNIQUE, " +
                TB_LOPHOCPHAN_IDMONHOC + " INTEGER, " +
                TB_LOPHOCPHAN_IDGIANGVIEN + " INTEGER, " +
                TB_LOPHOCPHAN_HOCKY + " INTEGER, " +
                TB_LOPHOCPHAN_SOLUONGSV + " INTEGER, " +
                "FOREIGN KEY (" + TB_LOPHOCPHAN_IDMONHOC + ") REFERENCES " + TB_MONHOC + "(" + TB_MONHOC_ID + "), " +
                "FOREIGN KEY (" + TB_LOPHOCPHAN_IDGIANGVIEN + ") REFERENCES " + TB_GIANGVIEN + "(" + TB_GIANGVIEN_ID + "), " +
                "PRIMARY KEY (" + TB_LOPHOCPHAN_ID + ")" +
                ")";
        db.execSQL(createLopHocPhanTableQuery);

        //Tạo bảng SinhVien
        String createSinhVienTableQuery = "CREATE TABLE " + TB_SINHVIEN + " (" +
                TB_SINHVIEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TB_SINHVIEN_HOTEN + " TEXT, " +
                TB_SINHVIEN_NGAYSINH + " TEXT, " +
                TB_SINHVIEN_GIOITINH + " TEXT, " +
                TB_SINHVIEN_EMAIL + " TEXT, " +
                TB_SINHVIEN_NIENKHOA + " TEXT, " +
                TB_SINHVIEN_DIACHI + " TEXT, " +
                TB_SINHVIEN_SDT + " TEXT, " +
                TB_SINHVIEN_IDKHOA + " INTEGER NOT NULL, " +
                TB_SINHVIEN_IDCHUYENNGANH + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + TB_SINHVIEN_IDKHOA + ") REFERENCES " + TB_KHOA + "(" + TB_KHOA_ID + "), " +
                "FOREIGN KEY (" + TB_SINHVIEN_IDCHUYENNGANH + ") REFERENCES " + TB_CHUYENNGANH + "(" + TB_CHUYENNGANH_ID + ")" +
                ")";
        db.execSQL(createSinhVienTableQuery);

        //Tạo bảng Tham gia lớp học
        String createThamGiaLopHocTableQuery = "CREATE TABLE " + TB_THAMGIALOPHOC + " (" +
                TB_THAMGIALOPHOC_IDLOP + " INTEGER NOT NULL, " +
                TB_THAMGIALOPHOC_IDSV + " INTEGER NOT NULL, " +
                TB_THAMGIALOPHOC_DIEMTHI + " REAL, " +
                TB_THAMGIALOPHOC_DIEMQUATRINH + " REAL, " +
                TB_THAMGIALOPHOC_DIEMHOCPHAN + " REAL, " +
                "FOREIGN KEY (" + TB_THAMGIALOPHOC_IDSV + ") REFERENCES " + TB_SINHVIEN + "(" + TB_SINHVIEN_ID + "), " +
                "FOREIGN KEY (" + TB_THAMGIALOPHOC_IDLOP + ") REFERENCES " + TB_LOPHOCPHAN + "(" + TB_LOPHOCPHAN_ID + "), " +
                "PRIMARY KEY (" + TB_THAMGIALOPHOC_IDLOP + ", " + TB_THAMGIALOPHOC_IDSV + ")" +
                ")";
        db.execSQL(createThamGiaLopHocTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TB_CHUYENNGANH);
        db.execSQL("DROP TABLE IF EXISTS " + TB_SINHVIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TB_GIANGVIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TB_KHOA);
        db.execSQL("DROP TABLE IF EXISTS " + TB_LOPHOCPHAN);
        db.execSQL("DROP TABLE IF EXISTS " + TB_THAMGIALOPHOC);
        db.execSQL("DROP TABLE IF EXISTS " + TB_MONHOC);
        onCreate(db);
    }


}
