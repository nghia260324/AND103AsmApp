package com.ph41626.and103_assignment.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ph41626.and103_assignment.Adapter.RecyclerViewMyPurchasesAdapter;
import com.ph41626.and103_assignment.Model.GHNOrderDetail;
import com.ph41626.and103_assignment.Model.GHNRequest;
import com.ph41626.and103_assignment.Model.Order;
import com.ph41626.and103_assignment.Model.OrderDetail;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.ResponeGHN;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.User;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;

public class MyPurchasesActivity extends AppCompatActivity {
    private static final int MAX_THREAD = 10;
    private RecyclerView rcv_myPurchases;
    private GHNRequest ghnRequest;
    private HttpRequest httpRequest;
    private User user = new User();
    private ArrayList<Order> listOrders = new ArrayList<>();
    public ArrayList<Product> listProducts = new ArrayList<>();
    private ArrayList<OrderDetail> listOrderDetails = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RecyclerViewMyPurchasesAdapter myPurchasesAdapter;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);

        initUI();
        GetDataFromMainActivity();
        RecyclerViewManagement();
        Back();
    }
    public void GetOrderDetails(ArrayList<Order> listOrders) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait for seconds.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        for (Order order : listOrders) {
            httpRequest.callAPI().getOrderDetail(order.get_id()).enqueue(new Callback<Response<OrderDetail>>() {
                @Override
                public void onResponse(Call<Response<OrderDetail>> call, retrofit2.Response<Response<OrderDetail>> response) {
                    if (response != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            OrderDetail orderDetail = response.body().getData();
                            listOrderDetails.add(orderDetail);
                            if (listOrderDetails.size() == listOrders.size()) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                myPurchasesAdapter.Update(listOrderDetails);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<OrderDetail>> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
    private void Back() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void RecyclerViewManagement() {
        myPurchasesAdapter = new RecyclerViewMyPurchasesAdapter(this,null,this);
        rcv_myPurchases.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rcv_myPurchases.setAdapter(myPurchasesAdapter);
    }

    private void GetDataFromMainActivity() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        listProducts = (ArrayList<Product>) intent.getSerializableExtra("products");
        listOrders = (ArrayList<Order>) intent.getSerializableExtra("orders");

        GetOrderDetails(listOrders);
    }
    private void initUI() {
        ghnRequest = new GHNRequest();
        httpRequest = new HttpRequest();
        rcv_myPurchases = findViewById(R.id.rcv_myPurchases);

        btn_back = findViewById(R.id.btn_back);
    }
}