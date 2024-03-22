package com.ph41626.and103_assignment.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.ph41626.and103_assignment.Adapter.ViewPagerMainBottomNavigationAdapter;
import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.Model.Distributor;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.User;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_SEARCH = 1;
    private static final int FRAGMENT_CART = 2;
    private static final int FRAGMENT_PROFiLE = 3;
    private int mCurrentFragment = FRAGMENT_HOME;
    private ViewPager2 viewPager2;
    private ViewModel viewModel;
    private MeowBottomNavigation bottomNavigation;
    private ViewPagerMainBottomNavigationAdapter navigationAdapter;
    private HttpRequest httpRequest;
    private User user = new User();
    public ArrayList<Product> listProducts = new ArrayList<>();
    public ArrayList<Category> listCategories = new ArrayList<>();
    public ArrayList<Distributor> listDistributors = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        GetDataFromAPI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        BottomNavigationManager();
        GetUser();
    }
    public void InventoryManagement() {
        Intent intentInventory = new Intent(this, InventoryActivity.class);
        intentInventory.putExtra("products",listProducts);
        intentInventory.putExtra("categories",listCategories);
        intentInventory.putExtra("distributors",listDistributors);
        startActivity(intentInventory);
    }
    private void GetDataFromAPI() {
        httpRequest.callAPI().getListProducts().enqueue(getProductsFromAPI);
        httpRequest.callAPI().getListCategories().enqueue(getCategoriesFromAPI);
        httpRequest.callAPI().getListDistributors().enqueue(getDistributorsFromAPI);
    }
    private void GetUser() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        httpRequest.callAPI().getUserByEmail(email).enqueue(getUser);
    }
    Callback<Response<User>> getUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    user = response.body().getData();
                    viewModel.changeUser(user);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {

        }
    };
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
    private void BottomNavigationManager() {
        viewPager2.setAdapter(navigationAdapter);
        viewPager2.setUserInputEnabled(false);
        bottomNavigation.add(new MeowBottomNavigation.Model(0,R.drawable.baseline_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.baseline_search));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.baseline_shopping_cart));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.baseline_account_circle));
        bottomNavigation.show(0,true);
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 0:
                        if (mCurrentFragment != FRAGMENT_HOME) {
                            viewPager2.setCurrentItem(0);
                            mCurrentFragment = FRAGMENT_HOME;
                        }
                        break;
                    case 1:
                        if (mCurrentFragment != FRAGMENT_SEARCH) {
                            viewPager2.setCurrentItem(1);
                            mCurrentFragment = FRAGMENT_SEARCH;
                        }
                        break;
                    case 2:
                        if (mCurrentFragment != FRAGMENT_CART) {
                            viewPager2.setCurrentItem(2);
                            mCurrentFragment = FRAGMENT_CART;
                        }
                        break;
                    case 3:
                        if (mCurrentFragment != FRAGMENT_PROFiLE) {
                            viewPager2.setCurrentItem(3);
                            mCurrentFragment = FRAGMENT_PROFiLE;
                        }
                        break;
                    default: break;
                }
                return null;
            }
        });
    }
    private void initUI() {
        httpRequest = new HttpRequest();
        bottomNavigation = findViewById(R.id.bottomNavigation);
        viewPager2 = findViewById(R.id.viewPager2);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        navigationAdapter = new ViewPagerMainBottomNavigationAdapter(this);
    }
}
