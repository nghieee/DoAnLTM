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

public class AdminSubjectListAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> mSubjectList;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    public interface OnEditClickListener {
        void onEditClick(String subjectId, String subjectName, String subject_majorID);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String subjectId, String subjectName);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public AdminSubjectListAdapter(Context context, ArrayList<String> subjectList) {
        super(context, 0, subjectList);
        mContext = context;
        mSubjectList = subjectList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_subject_list, parent, false);
        }

        //Lấy dữ liệu từ danh sách
        String subject = mSubjectList.get(position);

        //Ánh xạ
        TextView mtvSubject = listItem.findViewById(R.id.tvSubject);
        ImageButton btnSubjectEdit = listItem.findViewById(R.id.btnSubjectEdit);
        ImageButton btnSubjectDelete = listItem.findViewById(R.id.btnSubjectDelete);

        //Hiển thị dữ liệu
        mtvSubject.setText(subject);

        //Xử lý sự kiện khi nhấn nút sửa
        btnSubjectEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String subject = mSubjectList.get(position);
                String[] subjectData = subject.split("-");
                String subjectId = subjectData[0].trim();
                String subjecName = subjectData[1].trim();
                String subject_majorID = subjectData[2].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (editClickListener != null) {
                    // Gọi callback để thông báo về sự kiện bấm nút chỉnh sửa
                    editClickListener.onEditClick(subjectId, subjecName, subject_majorID);
                }
            }
        });

        //Xử lý sự kiện nút xóa item
        btnSubjectDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ danh sách
                String subject = mSubjectList.get(position);
                String[] subjectData = subject.split(" - ");
                String subjectId = subjectData[0].trim();
                String subjectName = subjectData[1].trim();

                //Kiểm tra xem callback đã được thiết lập hay chưa
                if (deleteClickListener != null) {
                    //Gọi callback để thông báo về sự kiện bấm nút xóa
                    deleteClickListener.onDeleteClick(subjectId, subjectName);
                }
            }
        });

        return listItem;
    }
}
