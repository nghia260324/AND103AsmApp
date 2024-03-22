package com.ph41626.and103_assignment.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.GetChars;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ph41626.and103_assignment.Activity.AddProductActivity;
import com.ph41626.and103_assignment.Activity.InventoryActivity;
import com.ph41626.and103_assignment.Activity.MainActivity;
import com.ph41626.and103_assignment.Activity.UpdateProductActivity;
import com.ph41626.and103_assignment.Adapter.RecyclerViewProductInventoryAdapter;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;
import com.ph41626.and103_assignment.Services.Item_Product_Handle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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

    private RecyclerView rcv_productInventory;
    private RecyclerViewProductInventoryAdapter productInventoryAdapter;
    private InventoryActivity inventoryActivity;
    private ViewModel viewModel;
    private FloatingActionButton btn_addProduct;
    private HttpRequest httpRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        initUI(view);
        RecyclerViewManagement();
        AddProduct();
        UpdateRecyclerViewWhenDataChanges();
        return view;
    }

    private void UpdateRecyclerViewWhenDataChanges() {
        viewModel.getChangeDataProducts().observe(getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                productInventoryAdapter.UpdateData(products);
            }
        });
    }
    private void AddProduct() {
        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                intent.putExtra("categories",inventoryActivity.listCategories);
                intent.putExtra("distributors",inventoryActivity.listDistributors);
                startActivity(intent);
            }
        });
    }
    private void RecyclerViewManagement() {
        productInventoryAdapter = new RecyclerViewProductInventoryAdapter(getContext(), inventoryActivity.listProducts, new Item_Product_Handle() {
            @Override
            public void Delete(String id,String name) {
                DialogShowMessenger(id,name);
            }

            @Override
            public void Update(Product product) {
                Intent intent = new Intent(getActivity(), UpdateProductActivity.class);
                intent.putExtra("categories",inventoryActivity.listCategories);
                intent.putExtra("distributors",inventoryActivity.listDistributors);
                intent.putExtra("product",product);
                startActivity(intent);
            }
        }, inventoryActivity);
        rcv_productInventory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rcv_productInventory.setAdapter(productInventoryAdapter);
    }
    private void DialogShowMessenger (String id,String name) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete the product '" + name + "' ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                httpRequest.callAPI().deleteProduct(id).enqueue(deleteProduct);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    Callback<Response<Product>> deleteProduct = new Callback<Response<Product>>() {
        @Override
        public void onResponse(Call<Response<Product>> call, retrofit2.Response<Response<Product>> response) {
            if (response.body().getStatus() == 200) {
                Toast.makeText(inventoryActivity, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                inventoryActivity.GetDataFromAPI();
            }
        }

        @Override
        public void onFailure(Call<Response<Product>> call, Throwable t) {

        }
    };
    private void initUI(View view) {
        httpRequest = new HttpRequest();
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        inventoryActivity = (InventoryActivity) getActivity();
        rcv_productInventory = view.findViewById(R.id.rcv_productInventory);
        btn_addProduct = view.findViewById(R.id.btn_addProduct);
    }
}