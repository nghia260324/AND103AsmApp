package com.ph41626.and103_assignment.Adapter;

import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_assignment.Services.Services.formatPrice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerViewProductHomeAdapter extends RecyclerView.Adapter<RecyclerViewProductHomeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> listProducts;
    public void UpdateData(ArrayList<Product> products) {
        listProducts = products;
        notifyDataSetChanged();
    }
    public RecyclerViewProductHomeAdapter(Context context, ArrayList<Product> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
    }

    @NonNull
    @Override
    public RecyclerViewProductHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_home,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewProductHomeAdapter.ViewHolder holder, int position) {
        Product product = listProducts.get(position);
        holder.tv_name.setText(product.getName());
        holder.tv_price.setText(formatPrice(product.getPrice(),"₫"));
        Glide.with(context).load(convertLocalhostToIpAddress(product.getThumbnail())).error(R.drawable.img_home_1).placeholder(R.drawable.img_home_1).into(holder.img_thumbnail);
    }

    @Override
    public int getItemCount() {
        return listProducts != null? listProducts.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_price;
        ImageView img_thumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
        }
    }
}
