package com.ph41626.and103_assignment.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.ph41626.and103_assignment.Activity.MainActivity.REQUEST_CODE_ACTIVITY_BACK;
import static com.ph41626.and103_assignment.Services.Services.PICK_IMAGE_REQUEST;
import static com.ph41626.and103_assignment.Services.Services.ReadObjectToFile;
import static com.ph41626.and103_assignment.Services.Services.filePathAddressInfo;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;
import static com.ph41626.and103_assignment.Services.Services.formatPrice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.ph41626.and103_assignment.Activity.MainActivity;
import com.ph41626.and103_assignment.Activity.OrderActivity;
import com.ph41626.and103_assignment.Activity.UserInformationActivity;
import com.ph41626.and103_assignment.Adapter.RecyclerViewProductCartAdapter;
import com.ph41626.and103_assignment.Model.AddressInfo;
import com.ph41626.and103_assignment.Model.Cart;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.User;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;

import java.util.ArrayList;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private Button btn_order,btn_deleteCartItem;
    private TextView tv_amount;
    private MainActivity mainActivity;
    private RecyclerView rcv_cart;
    private RecyclerViewProductCartAdapter productCartAdapter;
    private HttpRequest httpRequest;
    private ViewModel viewModel;
    private static final long DEBOUNCE_INTERVAL = 600;
    private ArrayList<Cart> listCarts = new ArrayList<>();
    private ArrayList<Cart> listCartDeletes = new ArrayList<>();

    private Handler handlerItem = new Handler();
    private Handler handlerCart = new Handler();
    private Runnable runnableItem,runnableCart;
    private int lastItemUpdate = -1;
    private int currentItemUpdate = -1;
    private void debounceClickItem(Cart cart) {
        if (runnableItem != null) {
            handlerItem.removeCallbacks(runnableItem);
        }
        runnableItem = new Runnable() {
            @Override
            public void run() {
                httpRequest.callAPI().updateCartItem(cart.get_id(),cart).enqueue(updateCartItem);
            }
        };
        handlerItem.postDelayed(runnableItem, DEBOUNCE_INTERVAL);
    }
    private void debounceClickCart() {
        if (runnableCart != null) {
            handlerCart.removeCallbacks(runnableCart);
        }
        runnableCart = new Runnable() {
            @Override
            public void run() {
                httpRequest.callAPI().updateCart(listCarts).enqueue(updateCart);
            }
        };
        handlerCart.postDelayed(runnableCart, DEBOUNCE_INTERVAL);
    }
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initUI(view);
        RecyclerViewManagement();
        UpdateRecyclerViewWhenDataChanges();
        Order();
        DeleteCartItem();

        return view;
    }

    private void DeleteCartItem() {

        btn_deleteCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please wait for seconds!");
                progressDialog.show();
                ArrayList<String> cartIdArray = new ArrayList<>();
                for (Cart cart : listCartDeletes) {
                    cartIdArray.add(cart.get_id());
                }
                httpRequest.callAPI().deleteCartItems(cartIdArray).enqueue(deleteCartItems);
            }
        });
    }

    private void Order() {
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                ArrayList<Cart> items = new ArrayList<>();
                for (Cart cart:listCarts) {
                    if (cart.isSelected()) {
                        items.add(cart);
                    }
                }
                intent.putExtra("cartItems",items);
                intent.putExtra("products",mainActivity.listProducts);
                intent.putExtra("user",mainActivity.getUser());
                startActivityForResult(intent,REQUEST_CODE_ACTIVITY_BACK);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ACTIVITY_BACK) {
            if (resultCode == RESULT_OK) {
                mainActivity.GetListCartById();
            }
        }
    }

    private void UpdateRecyclerViewWhenDataChanges() {
        viewModel.getChangeDataCarts().observe(getViewLifecycleOwner(), new Observer<ArrayList<Cart>>() {
            @Override
            public void onChanged(ArrayList<Cart> carts) {
                listCarts = carts;
                productCartAdapter.Update(carts);
                CalculatorAmount(carts);
                isItemSelected();
            }
        });
    }

    private void isItemSelected() {
        listCartDeletes.clear();
        for (Cart cart:listCarts) {
            if (cart.isSelected()) {
                listCartDeletes.add(cart);
            }
        }
        if (listCartDeletes.size() > 0) {
            btn_deleteCartItem.setVisibility(View.VISIBLE);
        } else {
            btn_deleteCartItem.setVisibility(View.GONE);
        }
    }

    private void CalculatorAmount(ArrayList<Cart> carts) {
        int amount = 0;
        for (Cart cart:carts) {
            if (cart.isSelected()) {
                amount += findObjectById(mainActivity.listProducts,cart.getId_product()).getPrice() * cart.getQuantity();
            }
        }
        tv_amount.setText(formatPrice(amount,"₫"));
    }
    Callback<Response<ArrayList<Cart>>> deleteCartItems = new Callback<Response<ArrayList<Cart>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    mainActivity.GetListCartById();
                    progressDialog.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {
            progressDialog.dismiss();
        }
    };
    Callback<Response<Cart>> updateCartItem = new Callback<Response<Cart>>() {
        @Override
        public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    mainActivity.GetListCartById();
                    lastItemUpdate = currentItemUpdate = -1;
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Cart>> call, Throwable t) {

        }
    };
    Callback<Response<ArrayList<Cart>>> updateCart = new Callback<Response<ArrayList<Cart>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    mainActivity.GetListCartById();
                    lastItemUpdate = currentItemUpdate = -1;
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {

        }
    };
    private void RecyclerViewManagement() {
        productCartAdapter = new RecyclerViewProductCartAdapter(getContext(), mainActivity.listCarts, mainActivity, new RecyclerViewProductCartAdapter.OnCartChangedListener() {
            @Override
            public void onChanged(Cart cart, int position) {
                lastItemUpdate = lastItemUpdate == -1 ? position : currentItemUpdate;
                currentItemUpdate = position;
                if (currentItemUpdate == lastItemUpdate) {
                    debounceClickItem(cart);
                } else {
                    if (runnableItem != null) {
                        handlerItem.removeCallbacks(runnableItem);
                    }
                    debounceClickCart();
                }
            }
        });
        rcv_cart.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rcv_cart.setAdapter(productCartAdapter);
    }
    private void initUI(View view) {
        btn_order = view.findViewById(R.id.btn_order);
        btn_deleteCartItem = view.findViewById(R.id.btn_deleteCartItem);
        tv_amount = view.findViewById(R.id.tv_amount);
        mainActivity = (MainActivity) getActivity();
        rcv_cart = view.findViewById(R.id.rcv_cart);
        httpRequest = new HttpRequest();
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
    }
}