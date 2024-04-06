package com.ph41626.and103_assignment.Activity;

import static com.ph41626.and103_assignment.Activity.MainActivity.REQUEST_CODE_ACTIVITY_BACK;
import static com.ph41626.and103_assignment.Services.Services.ReadObjectToFile;
import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_assignment.Services.Services.filePathAddressInfo;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;
import static com.ph41626.and103_assignment.Services.Services.formatPrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ph41626.and103_assignment.Model.AddressInfo;
import com.ph41626.and103_assignment.Model.Cart;
import com.ph41626.and103_assignment.Model.District;
import com.ph41626.and103_assignment.Model.DistrictRequest;
import com.ph41626.and103_assignment.Model.GHNItem;
import com.ph41626.and103_assignment.Model.GHNOrderRequest;
import com.ph41626.and103_assignment.Model.GHNOrderRespone;
import com.ph41626.and103_assignment.Model.GHNRequest;
import com.ph41626.and103_assignment.Model.Order;
import com.ph41626.and103_assignment.Model.OrderDetail;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Province;
import com.ph41626.and103_assignment.Model.ResponeGHN;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.User;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.Model.Ward;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;
import com.ph41626.and103_assignment.Services.Services;
import com.ph41626.and103_assignment.Services.TokenManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderActivity extends AppCompatActivity {
    private GHNRequest ghnRequest;
    private TextView tv_address,tv_totalPayment;
    private Button btn_order;
    private LinearLayout btn_selectedAddress,layout_itemProducts;
    private User user;
    private HttpRequest httpRequest;
    private ArrayList<Product> listProducts = new ArrayList<>();
    private ArrayList<Cart> listCarts = new ArrayList<>();
    private ArrayList<GHNItem> ghnItems = new ArrayList<>();
    private ProgressDialog progressDialog;
    private GHNOrderRequest ghnOrderRequest;
    private AddressInfo addressInfo = new AddressInfo();
    private OrderDetail orderDetail = new OrderDetail();
    private int totalPayment;
    private String messenger = "";
    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    @Override
    protected void onStop() {
        super.onStop();
        setResult(RESULT_CANCELED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initUI();
        GetDataFromCart();
        SelectedAddress();
        addressInfo = (AddressInfo) ReadObjectToFile(this,user.get_id() + filePathAddressInfo);
        if (addressInfo != null) {
            FillInfo();
        } else {
            addressInfo = new AddressInfo();
        }
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addressInfo == null) {
                    messenger = "Please update your personal information before ordering!";
                    DialogShowMessenger();
                    return;
                }
                progressDialog = new ProgressDialog(OrderActivity.this);
                progressDialog.setMessage("Please wait for seconds");
                progressDialog.show();
                progressDialog.setCancelable(false);

                ghnOrderRequest = new GHNOrderRequest(
                        user.getName(),
                        addressInfo.getPhone(),
                        addressInfo.getAddress(),
                        addressInfo.getWard().getWardCode(),
                        addressInfo.getDistrict().getDistrictID(),
                        ghnItems);
                ghnRequest.callAPI().GHNOrder(ghnOrderRequest).enqueue(addOrderGHN);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ACTIVITY_BACK) {
            addressInfo = (AddressInfo) ReadObjectToFile(this,user.get_id() + filePathAddressInfo);
            if (addressInfo != null) {
                FillInfo();
            } else {
                addressInfo = new AddressInfo();
            }
        }
    }
    private void DialogShowMessenger () {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(messenger);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void FillInfo() {
        String addressLine = user.getName() + " | " + addressInfo.getPhone() + "\n" + addressInfo.getAddress() + "\n";
        addressLine += addressInfo.getProvince().getProvinceName() + ", ";
        addressLine += addressInfo.getDistrict().getDistrictName() + ", ";
        addressLine += addressInfo.getWard().getWardName();
        tv_address.setText(addressLine);
    }

    private void SelectedAddress() {
        btn_selectedAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, AddressesActivity.class);
                intent.putExtra("user",user);
                startActivityForResult(intent,REQUEST_CODE_ACTIVITY_BACK);
            }
        });
    }

    private void GetDataFromCart() {
        Intent intent = getIntent();
        listProducts = (ArrayList<Product>) intent.getSerializableExtra("products");
        listCarts = (ArrayList<Cart>) intent.getSerializableExtra("cartItems");
        user = (User) intent.getSerializableExtra("user");
        for (Cart cart:listCarts) {
            Product product = findObjectById(listProducts,cart.getId_product());
            GHNItem ghnItem = new GHNItem();
            ghnItem.setName(product.getName());
            ghnItem.setCode(product.getId());
            ghnItem.setPrice(product.getPrice() * cart.getQuantity());
            ghnItem.setQuantity(cart.getQuantity());
            ghnItem.setWeight(50);

            ghnItems.add(ghnItem);

            setTotalPayment(getTotalPayment() + (cart.getQuantity() * product.getPrice()));
            FillProduct(product,cart);
        }
        tv_totalPayment.setText(formatPrice(getTotalPayment(),"₫"));
    }
    private void FillProduct(Product product,Cart cart) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_product_order,null,false);
        ImageView img_thumbnail = view.findViewById(R.id.img_thumbnail);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_description = view.findViewById(R.id.tv_description);
        TextView tv_price = view.findViewById(R.id.tv_price);
        TextView tv_itemOrderTotal = view.findViewById(R.id.tv_itemOrderTotal);
        TextView tv_itemOrderTotalPrice = view.findViewById(R.id.tv_itemOrderTotalPrice);
        tv_name.setText(product.getName());
        tv_description.setText(product.getDescription());
        tv_price.setText(formatPrice(product.getPrice(),"₫"));
        Glide.with(this).
                load(convertLocalhostToIpAddress(product.getThumbnail())).
                error(R.drawable.img_home_1).
                placeholder(R.drawable.img_home_1).
                into(img_thumbnail);
        tv_itemOrderTotal.setText("Order Total (" + cart.getQuantity() + " items)");
        tv_itemOrderTotalPrice.setText(formatPrice((product.getPrice() * cart.getQuantity()),"₫"));
        layout_itemProducts.addView(view);
    }
    Callback<ResponeGHN<GHNOrderRespone>> addOrderGHN = new Callback<ResponeGHN<GHNOrderRespone>>() {
        @Override
        public void onResponse(Call<ResponeGHN<GHNOrderRespone>> call, retrofit2.Response<ResponeGHN<GHNOrderRespone>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    Order order = new Order();
                    order.setOrder_code(response.body().getData().getOrder_code());
                    order.setId_user(user.get_id());

                    httpRequest.callAPI().order(
                            TokenManager.getInstance(OrderActivity.this).getToken(),
                            order).enqueue(addOrderDB);
                }
            } else {
                messenger = "Invalid personal information detected. Please update your personal information to continue using our services accurately and securely!";
                DialogShowMessenger();
                progressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(Call<ResponeGHN<GHNOrderRespone>> call, Throwable t) {

        }
    };
    Callback<Response<Order>> addOrderDB = new Callback<Response<Order>>() {
        @Override
        public void onResponse(Call<Response<Order>> call, retrofit2.Response<Response<Order>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Order order = response.body().getData();

                    orderDetail = new OrderDetail();
                    orderDetail.setId_order(order.get_id());
                    orderDetail.setItems(ghnOrderRequest.getItems());
                    orderDetail.setTo_name(ghnOrderRequest.getTo_name());
                    orderDetail.setTo_phone(ghnOrderRequest.getTo_phone());
                    orderDetail.setTo_address(ghnOrderRequest.getTo_address());
                    orderDetail.setTo_ward_code(ghnOrderRequest.getTo_ward_code());
                    orderDetail.setTo_district_id(ghnOrderRequest.getTo_district_id());
                    orderDetail.setCod_amount(ghnOrderRequest.getCod_amount());

                    httpRequest.callAPI().addOrderDetail(
                            TokenManager.getInstance(OrderActivity.this).getToken(),
                            orderDetail).enqueue(addOrderDetail);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Order>> call, Throwable t) {

        }
    };
    Callback<Response<OrderDetail>> addOrderDetail = new Callback<Response<OrderDetail>>() {
        @Override
        public void onResponse(Call<Response<OrderDetail>> call, retrofit2.Response<Response<OrderDetail>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(OrderActivity.this, "Order Success!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<OrderDetail>> call, Throwable t) {

        }
    };
    private void initUI() {
        httpRequest = new HttpRequest();
        btn_order = findViewById(R.id.btn_order);
        ghnRequest = new GHNRequest();

        tv_address = findViewById(R.id.tv_address);
        tv_totalPayment = findViewById(R.id.tv_totalPayment);
        btn_selectedAddress = findViewById(R.id.btn_selectedAddress);
        layout_itemProducts =findViewById(R.id.layout_itemProducts);
    }
}