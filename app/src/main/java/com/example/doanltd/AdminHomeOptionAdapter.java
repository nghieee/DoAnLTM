package com.example.doanltd;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.media.RouteListingPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdminHomeOptionAdapter extends ArrayAdapter<AdminHomeOption> {
    Activity adminHome;
    int idLayout;
    ArrayList<AdminHomeOption> optionList;

    //Tạo constructor để Activity gọi đến và truyền 3 tham số này vào
    public AdminHomeOptionAdapter(Activity adminHome, int idLayout, ArrayList<AdminHomeOption> optionList) {
        super(adminHome, idLayout);
        this.adminHome = adminHome;
        this.idLayout = idLayout;
        this.optionList = optionList;
    }

    //Gọi hàm getView để adapter lấy và hiển thị dữ liệu
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Tạo đế chứa layout
        LayoutInflater optionInflater = adminHome.getLayoutInflater();
        //Tạo View
        convertView = optionInflater.inflate(idLayout, null);
        //Lấy 1 phần tử trong mảng dữ liệu
        AdminHomeOption adminOption = optionList.get(position);

        ImageView img_option = convertView.findViewById(R.id.img_option);
        img_option.setImageResource(adminOption.getImage());

        TextView tv_name = convertView.findViewById(R.id.tv_name);
        tv_name.setText(adminOption.getName());
        return convertView;
    }
}
