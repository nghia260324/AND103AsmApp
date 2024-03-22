package com.ph41626.and103_assignment.Adapter;

import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;
import static com.ph41626.and103_assignment.Services.Services.formatPrice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ph41626.and103_assignment.Activity.InventoryActivity;
import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.Model.Distributor;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.Item_Product_Handle;

import java.util.ArrayList;

public class RecyclerViewProductInventoryAdapter extends RecyclerView.Adapter<RecyclerViewProductInventoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> listProducts;
    private InventoryActivity inventoryActivity;
    private Item_Product_Handle item_product_handle;
    public void UpdateData(ArrayList<Product> products) {
        listProducts = products;
        notifyDataSetChanged();
    }
    public RecyclerViewProductInventoryAdapter(Context context, ArrayList<Product> listProducts,Item_Product_Handle product_handle, InventoryActivity inventoryActivity) {
        this.context = context;
        this.listProducts = listProducts;
        this.item_product_handle = product_handle;
        this.inventoryActivity = inventoryActivity;
    }

    @NonNull
    @Override
    public RecyclerViewProductInventoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_inventory,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewProductInventoryAdapter.ViewHolder holder, int position) {
        Product product = listProducts.get(position);
        Distributor distributor = findObjectById(inventoryActivity.listDistributors,product.getId_distributor());
        Category category = findObjectById(inventoryActivity.listCategories,product.getId_category());
        holder.tv_name.setText(product.getName());
        holder.tv_price.setText("Price: " + formatPrice(product.getPrice(),"₫"));
        holder.tv_description.setText("Description: " + product.getDescription());
        holder.tv_distributor.setText(distributor.getName());
        holder.tv_category.setText(category.getName());
        Glide.with(context).load(convertLocalhostToIpAddress(product.getThumbnail())).error(R.drawable.img_home_1).placeholder(R.drawable.img_home_1).into(holder.img_thumbnail);

        holder.btn_deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_product_handle.Delete(product.getId(),product.getName());
            }
        });
        holder.btn_updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_product_handle.Update(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProducts != null ? listProducts.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_thumbnail;
        TextView tv_name,tv_price,tv_category,tv_distributor,tv_description;
        RelativeLayout btn_deleteProduct,btn_updateProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_distributor = itemView.findViewById(R.id.tv_distributor);
            tv_description = itemView.findViewById(R.id.tv_description);
            btn_updateProduct = itemView.findViewById(R.id.btn_updateProduct);
            btn_deleteProduct = itemView.findViewById(R.id.btn_deleteProduct);

        }
    }
}
