package com.ph41626.and103_assignment.Adapter;

import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;
import static com.ph41626.and103_assignment.Services.Services.formatPrice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ph41626.and103_assignment.Activity.MyPurchasesActivity;
import com.ph41626.and103_assignment.Model.GHNItem;
import com.ph41626.and103_assignment.Model.OrderDetail;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.R;

import java.util.ArrayList;

public class RecyclerViewMyPurchasesAdapter extends RecyclerView.Adapter<RecyclerViewMyPurchasesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderDetail> listOrderDetails;
    private MyPurchasesActivity myPurchasesActivity;
    public RecyclerViewMyPurchasesAdapter(Context context, ArrayList<OrderDetail> listOrderDetails, MyPurchasesActivity myPurchasesActivity) {
        this.context = context;
        this.listOrderDetails = listOrderDetails;
        this.myPurchasesActivity = myPurchasesActivity;
    }

    public void Update(ArrayList<OrderDetail> orderDetails) {
        listOrderDetails = orderDetails;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerViewMyPurchasesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_purchases,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewMyPurchasesAdapter.ViewHolder holder, int position) {
        OrderDetail orderDetail = listOrderDetails.get(position);
        int numberOfProduct = orderDetail.getItems().size();
        holder.layout_itemProducts.setVisibility(View.GONE);
        holder.tv_name.setText(orderDetail.getTo_name() + " | " + orderDetail.getTo_phone());
        holder.tv_address.setText(orderDetail.getTo_address());
        holder.tv_itemOrderTotal.setText("Order Total (" + numberOfProduct + " items)");
        holder.tv_itemOrderTotalPrice.setText(formatPrice(orderDetail.getCod_amount(),"₫"));
        for (int i = 0; i < orderDetail.getItems().size(); i++) {
            GHNItem ghnItem = orderDetail.getItems().get(i);
            Product product = findObjectById(myPurchasesActivity.listProducts,ghnItem.getCode());
            View view = LayoutInflater.from(context).inflate(R.layout.item_product_my_purchases,null,false);
            TextView
                    tv_name = view.findViewById(R.id.tv_name),
                    tv_price = view.findViewById(R.id.tv_price),
                    tv_itemOrderTotal = view.findViewById(R.id.tv_itemOrderTotal),
                    tv_itemOrderTotalPrice = view.findViewById(R.id.tv_itemOrderTotalPrice);
            ImageView img_thumbnail = view.findViewById(R.id.img_thumbnail);
            tv_name.setText(product.getName());
            tv_price.setText(formatPrice(product.getPrice(),"₫"));
            tv_itemOrderTotal.setText("Order Total (" + ghnItem.getQuantity() + " items)");
            tv_itemOrderTotalPrice.setText(formatPrice(ghnItem.getPrice(),"₫"));
            Glide.with(context).
                    load(convertLocalhostToIpAddress(product.getThumbnail())).
                    error(R.drawable.img_home_1).
                    placeholder(R.drawable.img_home_1).
                    into(img_thumbnail);
            holder.layout_itemProducts.addView(view);
            if (i == (orderDetail.getItems().size() - 1)) {
                RelativeLayout layout_bgr = view.findViewById(R.id.layout_bgr);
                layout_bgr.setBackground(null);
            }
        }
        holder.btn_hideProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.layout_itemProducts.getVisibility() == View.VISIBLE) {
                    holder.layout_itemProducts.setVisibility(View.GONE);
                } else {
                    holder.layout_itemProducts.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrderDetails != null ? listOrderDetails.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_address,tv_itemOrderTotal,tv_itemOrderTotalPrice;
        LinearLayout layout_itemProducts,btn_hideProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_itemOrderTotal = itemView.findViewById(R.id.tv_itemOrderTotal);
            tv_itemOrderTotalPrice = itemView.findViewById(R.id.tv_itemOrderTotalPrice);
            layout_itemProducts = itemView.findViewById(R.id.layout_itemProducts);
            btn_hideProduct = itemView.findViewById(R.id.btn_hideProduct);
        }
    }
}
