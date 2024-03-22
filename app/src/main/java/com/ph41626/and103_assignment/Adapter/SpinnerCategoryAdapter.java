package com.ph41626.and103_assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.R;

import java.util.ArrayList;

public class SpinnerCategoryAdapter extends ArrayAdapter<Category> {

    private Context context;
    private ArrayList<Category> listCategories;

    TextView tv_name;

    public SpinnerCategoryAdapter(@NonNull Context context, int resource, ArrayList<Category> categories) {
        super(context, resource,categories);
        this.context = context;
        this.listCategories = categories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final Category category = listCategories.get(position);

        if (category != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(category.getName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_spinner,parent,false);
        final Category category = listCategories.get(position);

        if (category != null) {
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_name.setText(category.getName());
        }

        return convertView;
    }
}
