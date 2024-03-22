package com.ph41626.and103_assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ph41626.and103_assignment.Activity.InventoryActivity;
import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.Item_Category_Handle;

import java.util.ArrayList;

public class RecyclerViewCategoryInventoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryInventoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> listCategories;
    private InventoryActivity inventoryActivity;
    private Item_Category_Handle item_category_handle;
    public void Update(ArrayList<Category> categories) {
        listCategories = categories;
        notifyDataSetChanged();
    }
    public RecyclerViewCategoryInventoryAdapter(Context context, ArrayList<Category> listCategories,Item_Category_Handle category_handle, InventoryActivity inventoryActivity) {
        this.context = context;
        this.listCategories = listCategories;
        this.item_category_handle = category_handle;
        this.inventoryActivity = inventoryActivity;
    }

    @NonNull
    @Override
    public RecyclerViewCategoryInventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_inventory,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategoryInventoryAdapter.ViewHolder holder, int position) {
        Category category = listCategories.get(position);
        holder.tv_rank.setText(String.valueOf(position + 1));
        holder.tv_name.setText(category.getName());
        holder.btn_deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_category_handle.Delete(category);
            }
        });
        holder.btn_updateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_category_handle.Update(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategories != null ? listCategories.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rank,tv_name;
        RelativeLayout btn_updateCategory,btn_deleteCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_name = itemView.findViewById(R.id.tv_name);
            btn_updateCategory = itemView.findViewById(R.id.btn_updateCategory);
            btn_deleteCategory = itemView.findViewById(R.id.btn_deleteCategory);
        }
    }
}
