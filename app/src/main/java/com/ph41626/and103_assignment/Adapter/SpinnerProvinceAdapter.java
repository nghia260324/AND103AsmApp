package com.ph41626.and103_assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ph41626.and103_assignment.Model.Province;
import com.ph41626.and103_assignment.R;

import java.util.ArrayList;

public class SpinnerProvinceAdapter extends ArrayAdapter<Province> {

    private Context context;
    private ArrayList<Province> listProvinces;

    private TextView tv_name;

    public SpinnerProvinceAdapter(@NonNull Context context, int resource, ArrayList<Province> provinces) {
        super(context, resource,provinces);
        this.context = context;
        this.listProvinces = provinces;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final Province province = listProvinces.get(position);

        if (province != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(province.getProvinceName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final Province province = listProvinces.get(position);

        if (province != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(province.getProvinceName());
        }

        return convertView;
    }
}
