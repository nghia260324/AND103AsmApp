package com.ph41626.and103_assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ph41626.and103_assignment.Activity.MainActivity;
import com.ph41626.and103_assignment.Model.Setting;
import com.ph41626.and103_assignment.R;

import java.util.ArrayList;

public class RecyclerViewSettingsAdapter extends RecyclerView.Adapter<RecyclerViewSettingsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Setting> listSettings;
    private MainActivity mainActivity;
    public RecyclerViewSettingsAdapter(Context context, ArrayList<Setting> listSettings,MainActivity mainActivity) {
        this.context = context;
        this.listSettings = listSettings;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerViewSettingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting,null,false);
//        return new ViewHolder(view);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        );
        view.setLayoutParams(layoutParams);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewSettingsAdapter.ViewHolder holder, int position) {
        if (!mainActivity.isAdmin() && holder.getAdapterPosition() == 1) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            param.height = 0;
            holder.itemView.setLayoutParams(param);
            holder.itemView.setVisibility(View.GONE);

        }
        Setting setting = listSettings.get(position);
        holder.img_icon.setImageResource(setting.getIcon());
        holder.tv_name.setText(setting.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.getAdapterPosition()) {
                    case 0:
                        break;
                    case 1:
                        mainActivity.InventoryManagement();
                        break;
                    case 2:
                        break;
                    case 3:
                        mainActivity.LogOut();
                        break;
                    default: return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSettings != null ? listSettings.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_icon;
        TextView tv_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
