package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.doanltd.R;

import java.util.ArrayList;

public class AdminMajorListAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> mMajorList;
    private AdminFacultyListAdapter.OnEditClickListener editClickListener;
    private AdminFacultyListAdapter.OnDeleteClickListener deleteClickListener;

    public interface OnEditClickListener {
        void onEditClick(String facultyId, String facultyName);
    }
    public void setOnEditClickListener(AdminFacultyListAdapter.OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String facultyId);
    }
    public void setOnDeleteClickListener(AdminFacultyListAdapter.OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public AdminMajorListAdapter(Context context, ArrayList<String> majorList) {
        super(context, 0, majorList);
        mContext = context;
        mMajorList = majorList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_major_list, parent, false);
        }

        //Lấy dữ liệu từ danh sách
        String faculty = mMajorList.get(position);

        //Ánh xạ
        TextView mtvMajor = listItem.findViewById(R.id.tvMajor);
        ImageButton btnChuyenNganhEdit = listItem.findViewById(R.id.btnChuyenNganhEdit);
        ImageButton btnChuyenNganhDelete = listItem.findViewById(R.id.btnChuyenNganhDelete);

        //Hiển thị dữ liệu
        mtvMajor.setText(faculty);

        //Xử lý sự kiện khi nhấn nút sửa
        btnChuyenNganhEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String major = mMajorList.get(position);
                String[] majorData = major.split("-");
                String majorId = majorData[0].trim();
                String majorName = majorData[1].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (editClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm nút chỉnh sửa
                    editClickListener.onEditClick(majorId, majorName);
                }
            }
        });

        //Xử lý sự kiện nút xóa item
        btnChuyenNganhDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String major = mMajorList.get(position);
                String[] majorData = major.split(" - ");
                String majorId = majorData[0].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (deleteClickListener != null) {
                    //Gọi callback để thông báo về sự kiện bấm nút xóa
                    deleteClickListener.onDeleteClick(majorId);
                }
            }
        });

        return listItem;
    }
}
