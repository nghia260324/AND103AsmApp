package com.ph41626.and103_assignment.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ph41626.and103_assignment.Adapter.SpinnerCategoryAdapter;
import com.ph41626.and103_assignment.Adapter.SpinnerDistributorAdapter;
import com.ph41626.and103_assignment.Adapter.ViewPagerInventoryBottomNavigationAdapter;
import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.Model.Distributor;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class InventoryActivity extends AppCompatActivity {

    private static final int FRAGMENT_PRODUCTS = 0;
    private static final int FRAGMENT_CATEGORIES = 1;
    private static final int FRAGMENT_DISTRIBUTORS = 2;
    private int mCurrentFragment = FRAGMENT_PRODUCTS;
    private ImageButton btn_back;
    private ViewPager2 viewPager2_inventory;
    private BottomNavigationView bottomNavigationView;
    public ArrayList<Product> listProducts = new ArrayList<>();
    public ArrayList<Category> listCategories = new ArrayList<>();
    public ArrayList<Distributor> listDistributors = new ArrayList<>();
    private HttpRequest httpRequest;
    private ViewModel viewModel;

    @Override
    protected void onResume() {
        super.onResume();
        GetDataFromAPI();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        initUI();
        GetDataFromMainActivity();
        BottomNavigation();
        CloseForm();
    }

    private void CloseForm() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    Callback<Response<ArrayList<Product>>> getProductsFromAPI = new Callback<Response<ArrayList<Product>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listProducts = response.body().getData();
                    viewModel.changeDataProducts(listProducts);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
            Log.e("Err",t.getMessage());
        }
    };
    Callback<Response<ArrayList<Category>>> getCategoriesFromAPI = new Callback<Response<ArrayList<Category>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Category>>> call, retrofit2.Response<Response<ArrayList<Category>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listCategories = response.body().getData();
                    viewModel.changeDataCategories(listCategories);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Category>>> call, Throwable t) {

        }
    };
    Callback<Response<ArrayList<Distributor>>> getDistributorsFromAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listDistributors = response.body().getData();
                    viewModel.changeDataDistributors(listDistributors);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {

        }
    };
    public void GetDataFromAPI() {
        httpRequest.callAPI().getListProducts().enqueue(getProductsFromAPI);
        httpRequest.callAPI().getListCategories().enqueue(getCategoriesFromAPI);
        httpRequest.callAPI().getListDistributors().enqueue(getDistributorsFromAPI);
    }
    private void GetDataFromMainActivity() {
        Intent intent = getIntent();
        listProducts = (ArrayList<Product>) intent.getSerializableExtra("products");
        listCategories = (ArrayList<Category>) intent.getSerializableExtra("categories");
        listDistributors = (ArrayList<Distributor>) intent.getSerializableExtra("distributors");
    }
    private void BottomNavigation() {
        ViewPagerInventoryBottomNavigationAdapter bottomNavigationAdapter = new ViewPagerInventoryBottomNavigationAdapter(this);
        viewPager2_inventory.setAdapter(bottomNavigationAdapter);
        viewPager2_inventory.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0: bottomNavigationView.getMenu().findItem(R.id.products).setChecked(true);
                        break;
                    case 1: bottomNavigationView.getMenu().findItem(R.id.categories).setChecked(true);
                        break;
                    case 2: bottomNavigationView.getMenu().findItem(R.id.distributors).setChecked(true);
                        break;
                }
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.products) {
                    if (mCurrentFragment != FRAGMENT_PRODUCTS) {
                        viewPager2_inventory.setCurrentItem(0);
                        mCurrentFragment = FRAGMENT_PRODUCTS;
                    }
                } else if(item.getItemId() == R.id.categories) {
                    if (mCurrentFragment != FRAGMENT_CATEGORIES) {
                        viewPager2_inventory.setCurrentItem(1);
                        mCurrentFragment = FRAGMENT_CATEGORIES;
                    }
                } else if (item.getItemId() == R.id.distributors) {
                    if (mCurrentFragment != FRAGMENT_DISTRIBUTORS) {
                        viewPager2_inventory.setCurrentItem(2);
                        mCurrentFragment = FRAGMENT_DISTRIBUTORS;
                    }
                }
                return true;
            }
        });
    }
    private void initUI() {
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        httpRequest = new HttpRequest();
        viewPager2_inventory = findViewById(R.id.viewPager2_inventory);
        btn_back = findViewById(R.id.btn_back);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewInventory);
    }
}