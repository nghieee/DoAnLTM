package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.doanltd.R;

import java.util.ArrayList;

public class AdminTeacherListAdapter extends ArrayAdapter {
    private Context mContext;
    private ArrayList<String> mTeacherList;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnItemClickListener itemClickListener;

    public interface OnEditClickListener {
        void onEditClick(String teacherId, String teacherName);
    }
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String teacherId, String teacherName);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String teacherId, String teacherName);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public AdminTeacherListAdapter(Context context, ArrayList<String> teacherList) {
        super(context, 0, teacherList);
        mContext = context;
        mTeacherList = teacherList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_teacher_list, parent, false);
        }

        //Lấy dữ liệu từ danh sách
        String teacher = mTeacherList.get(position);

        //Ánh xạ
        TextView mtvTeacher = listItem.findViewById(R.id.tvTeacher);
        ImageButton btnTeacherEdit = listItem.findViewById(R.id.btnTeacherEdit);
        ImageButton btnTeacherDelete = listItem.findViewById(R.id.btnTeacherDelete);

        //Hiển thị dữ liệu
        mtvTeacher.setText(teacher);

        //Xử lý sự kiện khi nhấn nút sửa
        btnTeacherEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String subject = mTeacherList.get(position);
                String[] subjectData = subject.split("-");
                String teacherId = subjectData[0].trim();
                String teacherName = subjectData[1].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (editClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm nút chỉnh sửa
                    editClickListener.onEditClick(teacherId, teacherName);
                }
            }
        });

        //Xử lý sự kiện nút xóa item
        btnTeacherDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String teacher = mTeacherList.get(position);
                String[] teacherData = teacher.split(" - ");
                String teacherId = teacherData[0].trim();
                String teacherName = teacherData[1].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (deleteClickListener != null) {
                    //Gọi callback để thông báo về sự kiện bấm nút xóa
                    deleteClickListener.onDeleteClick(teacherId, teacherName);
                }
            }
        });

        //Xử lý sự kiện ấn vào từng item
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ danh sách
                String teacher = mTeacherList.get(position);
                String[] teacherData = teacher.split(" - ");
                String teacherId = teacherData[0].trim();
                String teacherName = teacherData[1].trim();

                // Kiểm tra xem callback đã được thiết lập hay chưa
                if (itemClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm vào item
                    itemClickListener.onItemClick(teacherId, teacherName);
                }
            }
        });

        return listItem;
    }
}
