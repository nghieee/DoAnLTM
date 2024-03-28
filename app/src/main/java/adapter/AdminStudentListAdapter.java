package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.doanltd.R;

import java.util.ArrayList;

public class AdminStudentListAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> mStudentList;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnItemClickListener itemClickListener;

    public interface OnEditClickListener {
        void onEditClick(String studentId, String studentName);
    }
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }


    public interface OnDeleteClickListener {
        void onDeleteClick(String studentId);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(String studentId, String studentName);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public AdminStudentListAdapter(Context context, ArrayList<String> studentList) {
        super(context, 0, studentList);
        mContext = context;
        mStudentList = studentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_student_list, parent, false);
        }

        //Lấy dữ liệu từ danh sách
        String student = mStudentList.get(position);

        //Ánh xạ
        TextView mtvStudent = listItem.findViewById(R.id.tvStudent);
        ImageButton btnSV_Edit = listItem.findViewById(R.id.btnSV_Edit);
        ImageButton btnSV_Delete = listItem.findViewById(R.id.btnSV_Delete);

        //Hiển thị dữ liệu
        mtvStudent.setText(student);

        //Xử lý sự kiện khi nhấn nút sửa
        btnSV_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String student = mStudentList.get(position);
                String[] studentData = student.split("-");
                String studentId = studentData[0].trim();
                String studentName = studentData[1].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (editClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm nút chỉnh sửa
                    editClickListener.onEditClick(studentId, studentName);
                }
            }
        });

        //Xử lý sự kiện nút xóa item
        btnSV_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String student = mStudentList.get(position);
                String[] studentData = student.split("-");
                String studentId = studentData[0].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (deleteClickListener != null) {
                    //Gọi callback để thông báo về sự kiện bấm nút xóa
                    deleteClickListener.onDeleteClick(studentId);
                }
            }
        });

        //Xử lý sự kiện ấn vào từng item
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ danh sách
                String student = mStudentList.get(position);
                String[] studentData = student.split("-");
                String studentId = studentData[0].trim();
                String studentName = studentData[1].trim();

                // Kiểm tra xem callback đã được thiết lập hay chưa
                if (itemClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm vào item
                    itemClickListener.onItemClick(studentId, studentName);
                }
            }
        });

        return listItem;
    }
}
