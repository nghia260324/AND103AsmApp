package com.ph41626.and103_assignment.Activity;

import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;
import static com.ph41626.and103_assignment.Services.Services.formatPrice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.ph41626.and103_assignment.Adapter.ViewPagerMainBottomNavigationAdapter;
import com.ph41626.and103_assignment.Model.Cart;
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
    private static final String ADMIN = "c5S7dGGLygZktlmIUn3OQJ1MuaP2";
    private int mCurrentFragment = FRAGMENT_HOME;
    private ViewPager2 viewPager2;
    private ViewModel viewModel;
    private MeowBottomNavigation bottomNavigation;
    private ViewPagerMainBottomNavigationAdapter navigationAdapter;
    private HttpRequest httpRequest;
    private User user = new User();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Product> listProducts = new ArrayList<>();
    public ArrayList<Category> listCategories = new ArrayList<>();
    public ArrayList<Distributor> listDistributors = new ArrayList<>();
    public ArrayList<Cart> listCarts = new ArrayList<>();
    private boolean isAdmin = false;
    private String messenger = "";
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

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
    public void GoToFragmentSearch() {
        viewPager2.setCurrentItem(1);
        bottomNavigation.show(1,true);
        mCurrentFragment = FRAGMENT_SEARCH;
    }
    public void InventoryManagement() {
        Intent intentInventory = new Intent(this, InventoryActivity.class);
        intentInventory.putExtra("products",listProducts);
        intentInventory.putExtra("categories",listCategories);
        intentInventory.putExtra("distributors",listDistributors);
        startActivity(intentInventory);
    }
    public void LogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intentInventory = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(intentInventory);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    private void GetDataFromAPI() {
        httpRequest.callAPI().getListProducts().enqueue(getProductsFromAPI);
        httpRequest.callAPI().getListCategories().enqueue(getCategoriesFromAPI);
        httpRequest.callAPI().getListDistributors().enqueue(getDistributorsFromAPI);
    }
    private void GetUser() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (uid.equals(ADMIN)) {
            setAdmin(true);
        }
        httpRequest.callAPI().getUserByEmail(email).enqueue(getUser);
    }
    public void AddProductToCart(Product product,int ... quantity) {
        if (findObjectById(listCarts,product.getId()) == null) {
            int finalQuantity;
            if (quantity.length > 0) {
                finalQuantity = quantity[0];
            } else { finalQuantity = 1;}
            httpRequest.callAPI().addCart(new Cart("",user.get_id(),product.getId(),finalQuantity)).enqueue(addCart);
        } else {
            messenger = "Product has been added to the cart!";
            DialogShowMessenger();
        }
    }
    public void OpenDialogProductDetail(Product product) {
        Cart cart = new Cart("",user.get_id(),product.getId(),1);
        final View dialogView = View.inflate(this, R.layout.dialog_product_detail, null);
        final Dialog dialogProductDetail = new Dialog(this);

        dialogProductDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProductDetail.setContentView(dialogView);

        Window window = dialogProductDetail.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogProductDetail.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        dialogProductDetail.getWindow().setAttributes(layoutParams);

        ImageButton btn_back = dialogView.findViewById(R.id.btn_back);
        ImageView img_thumbnail = dialogView.findViewById(R.id.img_thumbnail);
        TextView
                tv_name = dialogView.findViewById(R.id.tv_name),
                tv_price = dialogView.findViewById(R.id.tv_price),
                tv_description = dialogView.findViewById(R.id.tv_description),
                tv_quantity = dialogView.findViewById(R.id.tv_quantity);
        Button btn_addToCart = dialogView.findViewById(R.id.btn_addToCart);
        ImageButton
                btn_Increase = dialogView.findViewById(R.id.btn_Increase),
                btn_Decrease = dialogView.findViewById(R.id.btn_Decrease);

        Glide.with(this).
                load(convertLocalhostToIpAddress(product.getThumbnail())).
                error(R.drawable.img_home_1).
                placeholder(R.drawable.img_home_1).
                into(img_thumbnail);
        tv_name.setText(product.getName());
        tv_price.setText(formatPrice(product.getPrice(),"₫"));
        tv_description.setText(product.getDescription());
        tv_quantity.setText(String.valueOf(cart.getQuantity()));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProductDetail.dismiss();
            }
        });
        btn_Decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getQuantity() > 1) {
                    cart.setQuantity(cart.getQuantity() - 1);
                    tv_quantity.setText(String.valueOf(cart.getQuantity()));
                }
            }
        });
        btn_Increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getQuantity() < 10) {
                    cart.setQuantity(cart.getQuantity() + 1);
                    tv_quantity.setText(String.valueOf(cart.getQuantity()));
                }
            }
        });
        btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductToCart(product,cart.getQuantity());
            }
        });
        dialogProductDetail.show();
    }
    public void OpenProductDetail(Product product) {
        OpenDialogProductDetail(product);
    }
    public void GetListCartById() {
        httpRequest.callAPI().getListCartsById(user.get_id()).enqueue(getCartsById);
    }
    Callback<Response<User>> getUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    user = response.body().getData();
                    viewModel.changeUser(user);
                    GetListCartById();
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
    Callback<Response<Cart>> addCart = new Callback<Response<Cart>>() {
        @Override
        public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Product product = findObjectById(listProducts,response.body().getData().getId_product());
                    messenger = "\"" + product.getName() + "\" has been added to your cart!";
                    DialogShowMessenger();
                    GetListCartById();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Cart>> call, Throwable t) {

        }
    };
    Callback<Response<ArrayList<Cart>>> getCartsById = new Callback<Response<ArrayList<Cart>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listCarts = response.body().getData();
                    viewModel.changeDataCarts(listCarts);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {

        }
    };
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
