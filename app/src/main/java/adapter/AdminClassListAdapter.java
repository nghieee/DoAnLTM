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

public class AdminClassListAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> mClassList;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnItemClickListener itemClickListener;

    public interface OnEditClickListener {
        void onEditClick(String classId, String teacherName);
    }
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String classId, String teacherName);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String classId, String teacherName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public AdminClassListAdapter(Context context, ArrayList<String> classList) {
        super(context, 0, classList);
        mContext = context;
        mClassList = classList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_class_list, parent, false);
        }

        //Lấy dữ liệu từ danh sách
        String classes = mClassList.get(position);

        //Ánh xạ
        TextView mtvClass = listItem.findViewById(R.id.tvClass);
        ImageButton btnClassEdit = listItem.findViewById(R.id.btnClassEdit);
        ImageButton btnClassDelete = listItem.findViewById(R.id.btnClassDelete);

        //Hiển thị dữ liệu
        mtvClass.setText(classes);

        //Xử lý sự kiện khi nhấn nút sửa
        btnClassEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String classes = mClassList.get(position);
                String[] classesData = classes.split("-");
                String classId = classesData[0].trim();
                String teacherName = classesData[1].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (editClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm nút chỉnh sửa
                    editClickListener.onEditClick(classId, teacherName);
                }
            }
        });

        //Xử lý sự kiện nút xóa item
        btnClassDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String classes = mClassList.get(position);
                String[] classesData = classes.split("-");
                String classId = classesData[0].trim();
                String teacherName = classesData[1].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (deleteClickListener != null) {
                    //Gọi callback để thông báo về sự kiện bấm nút xóa
                    deleteClickListener.onDeleteClick(classId, teacherName);
                }
            }
        });

        //Xử lý sự kiện ấn vào từng item
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ danh sách
                String classes = mClassList.get(position);
                String[] classesData = classes.split("-");
                String classId = classesData[0].trim();
                String teacherName = classesData[1].trim();

                // Kiểm tra xem callback đã được thiết lập hay chưa
                if (itemClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm vào item
                    itemClickListener.onItemClick(classId, teacherName);
                }
            }
        });

        return listItem;
    }
}
