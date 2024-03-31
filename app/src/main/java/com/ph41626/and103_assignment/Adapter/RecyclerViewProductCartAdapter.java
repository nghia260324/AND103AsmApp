package com.ph41626.and103_assignment.Adapter;

import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;
import static com.ph41626.and103_assignment.Services.Services.formatPrice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ph41626.and103_assignment.Activity.MainActivity;
import com.ph41626.and103_assignment.Model.Cart;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.R;

import java.util.ArrayList;

public class RecyclerViewProductCartAdapter extends RecyclerView.Adapter<RecyclerViewProductCartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Cart> listCarts;
    private MainActivity mainActivity;

    public void Update(ArrayList<Cart> carts) {
        listCarts = carts;
        notifyDataSetChanged();
    }
    public interface OnCartChangedListener {
        void onChanged(Cart cart, int position);
    }
    private OnCartChangedListener onCartChangedListener;
    public RecyclerViewProductCartAdapter(Context context, ArrayList<Cart> listCarts, MainActivity mainActivity,OnCartChangedListener cartChangedListener) {
        this.context = context;
        this.listCarts = listCarts;
        this.mainActivity = mainActivity;
        this.onCartChangedListener = cartChangedListener;
    }

    @NonNull
    @Override
    public RecyclerViewProductCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewProductCartAdapter.ViewHolder holder, int position) {
        Cart cart = listCarts.get(position);
        Product product = findObjectById(mainActivity.listProducts,cart.getId_product());
        Glide.with(context).
                load(convertLocalhostToIpAddress(product.getThumbnail())).
                error(R.drawable.img_home_1).
                placeholder(R.drawable.img_home_1).
                into(holder.img_thumbnail);
        holder.tv_name.setText(product.getName());
        holder.tv_price.setText(formatPrice(product.getPrice(),"₫"));
        holder.tv_quantity.setText(String.valueOf(cart.getQuantity()));
        holder.cbo_isSelected.setChecked(cart.isSelected() ? true : false);
        holder.btn_Increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getQuantity() < 10) {
                    cart.setQuantity(cart.getQuantity() + 1);
                    holder.setQuantity(cart.getQuantity());
                    onCartChangedListener.onChanged(cart,holder.getAdapterPosition());
                }
            }
        });
        holder.btn_Decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getQuantity() > 1) {
                    cart.setQuantity(cart.getQuantity() - 1);
                    holder.setQuantity(cart.getQuantity());
                    onCartChangedListener.onChanged(cart,holder.getAdapterPosition());
                }
            }
        });
        holder.cbo_isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cart.setSelected(true);
                    buttonView.setChecked(true);
                } else {
                    cart.setSelected(false);
                    buttonView.setChecked(false);
                }
                onCartChangedListener.onChanged(cart,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCarts != null ? listCarts.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_thumbnail;
        ImageButton btn_Increase,btn_Decrease;
        TextView tv_name, tv_price, tv_quantity;
        CheckBox cbo_isSelected;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_thumbnail = itemView.findViewById(R.id.img_thumbnail);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            btn_Increase = itemView.findViewById(R.id.btn_Increase);
            btn_Decrease = itemView.findViewById(R.id.btn_Decrease);
            cbo_isSelected = itemView.findViewById(R.id.cbo_isSelected);
        }
        public void setQuantity(int quantity) {
            tv_quantity.setText(String.valueOf(quantity));
        }
    }
}
