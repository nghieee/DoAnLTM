package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.doanltd.R;

import java.util.ArrayList;

public class AdminFacultyListAdapter extends ArrayAdapter<String>{
    private Context mContext;
    private ArrayList<String> mFacultyList;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    public interface OnEditClickListener {
        void onEditClick(String facultyId, String facultyName);
    }
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String facultyId);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }



    public AdminFacultyListAdapter(Context context, ArrayList<String> facultyList) {
        super(context, 0, facultyList);
        mContext = context;
        mFacultyList = facultyList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_faculty_list, parent, false);
        }

        // Lấy dữ liệu từ danh sách
        String faculty = mFacultyList.get(position);

        //Ánh xạ
        TextView mtvFaculty = listItem.findViewById(R.id.tvFaculty);
        ImageButton btnKhoaEdit = listItem.findViewById(R.id.btnKhoaEdit);
        ImageButton btnKhoaDelete = listItem.findViewById(R.id.btnKhoaDelete);

        //Hiển thị dữ liệu
        mtvFaculty.setText(faculty);

        //Xử lý sự kiện khi nhấn nút sửa
        btnKhoaEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ danh sách
                String faculty = mFacultyList.get(position);
                String[] facultyData = faculty.split("-");
                String facultyId = facultyData[0].trim();
                String facultyName = facultyData[1].trim();

                // Kiểm tra xem callback đã được thiết lập hay chưa
                if (editClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm nút chỉnh sửa
                    editClickListener.onEditClick(facultyId, facultyName);
                }
            }
        });

        btnKhoaDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ danh sách
                String faculty = mFacultyList.get(position);
                String[] facultyData = faculty.split(" - ");
                String facultyId = facultyData[0].trim();

                // Kiểm tra xem callback đã được thiết lập hay chưa
                if (deleteClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm nút xóa
                    deleteClickListener.onDeleteClick(facultyId);
                }
            }
        });

        return listItem;
    }
}
