package com.ph41626.and103_assignment.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ph41626.and103_assignment.Activity.MainActivity;
import com.ph41626.and103_assignment.Activity.SignInActivity;
import com.ph41626.and103_assignment.Adapter.RecyclerViewSearchProductAdapter;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;
import com.ph41626.and103_assignment.Services.TokenManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    private Button btn_sort;
    private RecyclerView rcv_searchProduct;
    private RecyclerViewSearchProductAdapter searchProductAdapter;
    private EditText edt_searchProduct,edt_price;
    private MainActivity mainActivity;
    private HttpRequest httpRequest;
    private ArrayList<Product> listProducts = new ArrayList<>();
    private ViewModel viewModel;
    private Spinner spinner_sort;
    private boolean isSort = true; // True = Ascending; False = Decrease
//    private InputMethodManager imm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initUI(view);
        RecyclerViewManagement();
        SearchProduct();
        if (listProducts == null || listProducts.isEmpty()) {
            edt_searchProduct.requestFocus();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //imm.showSoftInput(edt_searchProduct, InputMethodManager.SHOW_IMPLICIT);
        }
        SpinnerSort();
        viewModel.getChangeDataSearch().observe(getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                listProducts = products;
                searchProductAdapter.UpdateData(products);
            }
        });

        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isSort) {
//                    Collections.sort(listProducts, Comparator.comparingInt(Product::getPrice));
//                } else {
//                    Collections.sort(listProducts, Comparator.comparingInt(Product::getPrice).reversed());
//                }
//                searchProductAdapter.UpdateData(listProducts);
                boolean isValidate = true;
                String key = edt_searchProduct.getText().toString().trim();
                String price = edt_price.getText().toString().trim();
                if (!price.isEmpty()) {
                    try {
                        int priceValue = Integer.parseInt(price);
                        if (priceValue <= 0) {
                            edt_price.setError("Price must be a positive number!");
                            isValidate = false;
                        }
                    } catch (NumberFormatException e) {
                        edt_price.setError("Price must be a valid number!");
                        isValidate = false;
                    }
                }
                if (isValidate) {
                    Map<String,String> map = getMapFilter(key,price,isSort);
                    httpRequest.callAPI().searchProduct(
                            TokenManager.getInstance(getContext()).getToken(),
                            map).enqueue(searchProduct);
                }
            }
        });

        return view;
    }

    private void SpinnerSort() {
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.spinner_price, R.layout.item_spinner_sort);
        spinner_sort.setAdapter(sortAdapter);
        spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence value = (CharSequence) parent.getAdapter().getItem(position);
                if (value.toString().equals("Ascending")) {
                    isSort = true;
                } else if (value.toString().equals("Decrease")) {
                    isSort = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void SearchProduct() {
        edt_searchProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean isValidate = true;
                String key = edt_searchProduct.getText().toString().trim();
                String price = edt_price.getText().toString().trim();
                if (!price.isEmpty()) {
                    try {
                        int priceValue = Integer.parseInt(price);
                        if (priceValue <= 0) {
                            edt_price.setError("Price must be a positive number!");
                            isValidate = false;
                        }
                    } catch (NumberFormatException e) {
                        edt_price.setError("Price must be a valid number!");
                        isValidate = false;
                    }
                }
                if (isValidate) {
                    Map<String,String> map = getMapFilter(key,price,isSort);
                    httpRequest.callAPI().searchProduct(
                            TokenManager.getInstance(getContext()).getToken(),
                            map).enqueue(searchProduct);
                }
                return true;
            }
        });
    }
    private Map<String,String> getMapFilter (String name,String price,boolean isSort) {
        Map<String,String> map = new HashMap<>();
        map.put("name",name.equals("") ? "" : name);
        map.put("price",price);
        map.put("sort",String.valueOf(isSort ? 1:-1));
        return map;
    }
    Callback<Response<ArrayList<Product>>> searchProduct = new Callback<Response<ArrayList<Product>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    listProducts = response.body().getData();
                    Toast.makeText(mainActivity, listProducts.size() + "", Toast.LENGTH_SHORT).show();
                    viewModel.changeDataSearch(response.body().getData());
                } else {
                    viewModel.changeDataSearch(null);

                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
            Log.e("CHECK ERR SEARCH",t.getMessage());

        }
    };
    private void RecyclerViewManagement() {
        searchProductAdapter = new RecyclerViewSearchProductAdapter(getContext(),null,mainActivity);
        rcv_searchProduct.setLayoutManager(new GridLayoutManager(getContext(),2));
        rcv_searchProduct.setAdapter(searchProductAdapter);
    }

    private void initUI(View view) {
//        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        rcv_searchProduct = view.findViewById(R.id.rcv_searchProduct);
        mainActivity = (MainActivity) getActivity();
        edt_searchProduct = view.findViewById(R.id.edt_searchProduct);
        edt_price = view.findViewById(R.id.edt_price);
        httpRequest = new HttpRequest();
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        btn_sort = view.findViewById(R.id.btn_sort);
        spinner_sort = view.findViewById(R.id.spinner_sort);
    }
}