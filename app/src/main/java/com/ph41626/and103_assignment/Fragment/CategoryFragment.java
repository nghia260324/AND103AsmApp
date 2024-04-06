package com.ph41626.and103_assignment.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ph41626.and103_assignment.Activity.InventoryActivity;
import com.ph41626.and103_assignment.Adapter.RecyclerViewCategoryInventoryAdapter;
import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;
import com.ph41626.and103_assignment.Services.Item_Category_Handle;
import com.ph41626.and103_assignment.Services.TokenManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
    private RecyclerView rcv_categoryInventory;
    private RecyclerViewCategoryInventoryAdapter categoryInventoryAdapter;
    private InventoryActivity inventoryActivity;
    private Dialog dialogAddCategory;
    private FloatingActionButton btn_addCategory;
    private HttpRequest httpRequest;
    private ViewModel viewModel;
    private String messenger = "";
    private Category categoryUpdate = new Category();
    private Category categoryDelete = new Category();
    private boolean typeHandle = true; //True = Add-Category,False = Update-Category
    private EditText edt_name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        initUI(view);
        RecyclerViewManagement();
        AddCategory();
        UpdateRecyclerViewWhenDataChanges();

        return view;
    }
    private void UpdateRecyclerViewWhenDataChanges() {
        viewModel.getChangeDataCategories().observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                categoryInventoryAdapter.Update(categories);
            }
        });
    }
    private void AddCategory() {
        btn_addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeHandle = true;
                OpenDialogAddCategory();
            }
        });
    }
    private void RecyclerViewManagement() {
        categoryInventoryAdapter = new RecyclerViewCategoryInventoryAdapter(getContext(), inventoryActivity.listCategories, new Item_Category_Handle() {
            @Override
            public void Delete(Category category) {
                categoryDelete = category;
                httpRequest.callAPI().checkCategory(category.getId()).enqueue(checkCategory);
            }

            @Override
            public void Update(Category category) {
                typeHandle = false;
                categoryUpdate = category;
                OpenDialogAddCategory();
            }
        }, inventoryActivity);
        rcv_categoryInventory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rcv_categoryInventory.setLayoutManager(new GridLayoutManager(getContext(),1));
        rcv_categoryInventory.setAdapter(categoryInventoryAdapter);
    }
    private void OpenDialogAddCategory() {
        final View dialogView = View.inflate(getContext(), R.layout.dialog_add_category, null);
        dialogAddCategory = new Dialog(getContext());

        dialogAddCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddCategory.setContentView(dialogView);

        Window window = dialogAddCategory.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogAddCategory.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialogAddCategory.getWindow().setAttributes(layoutParams);

        TextView tv_title = dialogView.findViewById(R.id.tv_title);
        ImageButton btn_back = dialogView.findViewById(R.id.btn_back);
        Button btn_addCategoryDialog = dialogView.findViewById(R.id.btn_addCategoryDialog);
        edt_name = dialogView.findViewById(R.id.edt_name);

        if (!typeHandle) {
            tv_title.setText("Update Category");
            btn_addCategoryDialog.setText("Update Category");
            edt_name.setText(categoryUpdate.getName());
        }

        btn_addCategoryDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                if (!ValidateCategoryForm(name)) {
                    return;
                }
                if (typeHandle) {
                    messenger = "Add Category Successfully!";
                    httpRequest.callAPI().addCategory(
                            TokenManager.getInstance(getContext()).getToken(),
                            new Category("",name)).enqueue(addCategory);
                } else {
                    messenger = "Update Category Successfully!";
                    categoryUpdate.setName(name);
                    httpRequest.callAPI().updateCategory(
                            TokenManager.getInstance(getContext()).getToken(),
                            categoryUpdate.getId(),categoryUpdate).enqueue(addCategory);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddCategory.dismiss();
            }
        });

        dialogAddCategory.show();
    }
    private boolean ValidateCategoryForm(String name) {
        boolean isValid = true;

        if (name.isEmpty()) {
            isValid = false;
            edt_name.setError("Name is required!");
        }

        return isValid;
    }
    private void DialogShowMessenger (String id,String name) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete the category '" + name + "' ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                httpRequest.callAPI().deleteCategory(
                        TokenManager.getInstance(getContext()).getToken(),
                        id).enqueue(addCategory);
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
    private void DialogShowMessengerCantDelete () {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage("The following item can't be deleted because they have dependent items!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    Callback<Response<Category>> checkCategory = new Callback<Response<Category>>() {
        @Override
        public void onResponse(Call<Response<Category>> call, retrofit2.Response<Response<Category>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() != 200) {
                    DialogShowMessengerCantDelete();
                } else {
                    messenger = "Delete Category Successfully!";
                    DialogShowMessenger(categoryDelete.getId(),categoryDelete.getName());
                }
            } else {
                Toast.makeText(inventoryActivity, "Err", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Response<Category>> call, Throwable t) {
            Log.e("Failure", t.getMessage());

        }
    };
    Callback<Response<Category>> addCategory = new Callback<Response<Category>>() {
        @Override
        public void onResponse(Call<Response<Category>> call, retrofit2.Response<Response<Category>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(getContext(), messenger, Toast.LENGTH_SHORT).show();
                    dialogAddCategory.dismiss();
                    inventoryActivity.GetDataFromAPI();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Category>> call, Throwable t) {

        }
    };
    private void initUI(View view) {
        rcv_categoryInventory = view.findViewById(R.id.rcv_categoryInventory);
        inventoryActivity = (InventoryActivity) getActivity();
        btn_addCategory = view.findViewById(R.id.btn_addCategory);
        httpRequest = new HttpRequest();
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
    }
}