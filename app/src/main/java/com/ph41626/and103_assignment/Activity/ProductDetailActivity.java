package com.ph41626.and103_assignment.Activity;

import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;
import static com.ph41626.and103_assignment.Services.Services.formatPrice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ph41626.and103_assignment.Model.Cart;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.R;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private ImageView img_thumbnail;
    private TextView tv_name,tv_price,tv_description,tv_quantity;
    private Button btn_addToCart;
    private ImageButton btn_Increase,btn_Decrease;
    private Product product = new Product();
    private Cart cart = new Cart();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initUI();
        GetProductFromItem();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_Decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getQuantity() > 1) {
                    cart.setQuantity(cart.getQuantity() - 1);
                    setQuantity(cart.getQuantity());
                }
            }
        });
        btn_Increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getQuantity() < 10) {
                    cart.setQuantity(cart.getQuantity() + 1);
                    setQuantity(cart.getQuantity());
                }
            }
        });
//        btn_addToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (findObjectById(listCarts,product.getId()) == null) {
//                    httpRequest.callAPI().addCart(cart).enqueue(addCart);
//                } else {
//                    messenger = "Product has been added to the cart!";
//                    DialogShowMessenger();
//                }
//            }
//        });
    }
    private void DialogShowMessenger () {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Product has been added to the cart!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void setQuantity(int quantity) {
        tv_quantity.setText(String.valueOf(quantity));
    }
    private void GetProductFromItem() {
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("productDetail");
        cart = (Cart) intent.getSerializableExtra("cartItem");
        Glide.with(this).
                load(convertLocalhostToIpAddress(product.getThumbnail())).
                error(R.drawable.img_home_1).
                placeholder(R.drawable.img_home_1).
                into(img_thumbnail);
        tv_name.setText(product.getName());
        tv_price.setText(formatPrice(product.getPrice(),"₫"));
        tv_description.setText(product.getDescription());
        tv_quantity.setText(String.valueOf(cart.getQuantity()));
    }

    private void initUI() {
        btn_back = findViewById(R.id.btn_back);
        img_thumbnail = findViewById(R.id.img_thumbnail);
        tv_name = findViewById(R.id.tv_name);
        tv_price = findViewById(R.id.tv_price);
        tv_description = findViewById(R.id.tv_description);
        tv_quantity = findViewById(R.id.tv_quantity);
        btn_Increase = findViewById(R.id.btn_Increase);
        btn_Decrease = findViewById(R.id.btn_Decrease);
        btn_addToCart = findViewById(R.id.btn_addToCart);
    }
}