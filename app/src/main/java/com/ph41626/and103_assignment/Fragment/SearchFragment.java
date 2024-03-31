package com.ph41626.and103_assignment.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import java.util.ArrayList;

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
    private RecyclerView rcv_searchProduct;
    private RecyclerViewSearchProductAdapter searchProductAdapter;
    private EditText edt_searchProduct;
    private MainActivity mainActivity;
    private HttpRequest httpRequest;
    private ArrayList<Product> listProducts = new ArrayList<>();
    private ViewModel viewModel;
    private InputMethodManager imm;
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
        viewModel.getChangeDataSearch().observe(getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                searchProductAdapter.UpdateData(products);
            }
        });

        return view;
    }

    private void SearchProduct() {
        edt_searchProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String key = edt_searchProduct.getText().toString().trim();
                httpRequest.callAPI().searchProduct(key).enqueue(searchProduct);
                return true;
            }
        });
    }
    Callback<Response<ArrayList<Product>>> searchProduct = new Callback<Response<ArrayList<Product>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    viewModel.changeDataSearch(response.body().getData());
                } else {
                    viewModel.changeDataSearch(null);
                    Toast.makeText(mainActivity, "Null Check", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mainActivity, "Failed! Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {

        }
    };
    private void RecyclerViewManagement() {
        searchProductAdapter = new RecyclerViewSearchProductAdapter(getContext(),null,mainActivity);
        rcv_searchProduct.setLayoutManager(new GridLayoutManager(getContext(),2));
        rcv_searchProduct.setAdapter(searchProductAdapter);
    }

    private void initUI(View view) {
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        rcv_searchProduct = view.findViewById(R.id.rcv_searchProduct);
        mainActivity = (MainActivity) getActivity();
        edt_searchProduct = view.findViewById(R.id.edt_searchProduct);
        httpRequest = new HttpRequest();
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
    }
}