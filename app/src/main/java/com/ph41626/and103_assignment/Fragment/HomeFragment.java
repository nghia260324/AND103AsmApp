package com.ph41626.and103_assignment.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ph41626.and103_assignment.Activity.MainActivity;
import com.ph41626.and103_assignment.Adapter.RecyclerViewProductHomeAdapter;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private RecyclerView rcv_productHome;
    private Button btn_gotoOrder;
    private ViewModel viewModel;
    private EditText edt_searchHome;
    private RecyclerViewProductHomeAdapter productHomeAdapter;
    private ArrayList<Product> listProducts = new ArrayList<>();
    private MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);
        FillRecyclerViewProductsHome();
        UpdateRecyclerViewWhenDataChanges();
        GoToFragmentSearch();
        GoToFragmentOrder();
        return view;
    }

    private void GoToFragmentOrder() {
        btn_gotoOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.GoToFragmentOrder();
            }
        });
    }
    private void GoToFragmentSearch() {
        edt_searchHome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mainActivity.GoToFragmentSearch();
                }
            }
        });
    }
    private void UpdateRecyclerViewWhenDataChanges() {
        viewModel.getChangeDataProducts().observe(getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                productHomeAdapter.UpdateData(products);
            }
        });
    }
    public void FillRecyclerViewProductsHome() {
        rcv_productHome.setLayoutManager(new GridLayoutManager(getContext(),2));
        rcv_productHome.setAdapter(productHomeAdapter);
        rcv_productHome.setNestedScrollingEnabled(false);
    }
    private void initUI(View view) {
        mainActivity = (MainActivity) getActivity();
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        rcv_productHome = view.findViewById(R.id.rcv_productHome);
        productHomeAdapter = new RecyclerViewProductHomeAdapter(getContext(), listProducts,mainActivity);
        edt_searchHome = view.findViewById(R.id.edt_searchHome);

        btn_gotoOrder = view.findViewById(R.id.btn_gotoOrder);
    }
}