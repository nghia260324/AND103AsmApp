package com.ph41626.and103_assignment.Activity;

import static com.ph41626.and103_assignment.Services.Services.PICK_IMAGE_REQUEST;
import static com.ph41626.and103_assignment.Services.Services.ValidateInputFields;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ph41626.and103_assignment.Adapter.SpinnerCategoryAdapter;
import com.ph41626.and103_assignment.Adapter.SpinnerDistributorAdapter;
import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.Model.Distributor;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.User;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;
import com.ph41626.and103_assignment.Services.TokenManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddProductActivity extends AppCompatActivity {
    private EditText edt_name,edt_price,edt_description,edt_quantity;
    private RadioButton rdo_available,rdo_soldOut;
    private Spinner spinner_category,spinner_distributor;
    private ImageButton img_thumbnail;
    private ImageButton btn_back;
    private Button btn_addProduct;
    public ArrayList<Category> listCategories = new ArrayList<>();
    public ArrayList<Distributor> listDistributors = new ArrayList<>();
    @Override
    protected void onResume() {
        super.onResume();
        GetDataFromInventoryActivity();
    }
    private SpinnerCategoryAdapter spinnerCategoryAdapter;
    private SpinnerDistributorAdapter spinnerDistributorAdapter;
    private String idCategorySelected;
    private String idDistributorSelected;
    private File file;
    private HttpRequest httpRequest;
    private void GetDataFromInventoryActivity() {
        Intent intent = getIntent();
        listCategories = (ArrayList<Category>) intent.getSerializableExtra("categories");
        listDistributors = (ArrayList<Distributor>) intent.getSerializableExtra("distributors");

        spinnerCategoryAdapter = new SpinnerCategoryAdapter(this,R.layout.item_view_spinner,listCategories);
        spinner_category.setAdapter(spinnerCategoryAdapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idCategorySelected = listCategories.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerDistributorAdapter = new SpinnerDistributorAdapter(this,R.layout.item_view_spinner,listDistributors);
        spinner_distributor.setAdapter(spinnerDistributorAdapter);
        spinner_distributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idDistributorSelected = listDistributors.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initUI();
        CloseForm();
        AddProduct();
        SelectedThumbnail();
    }
    private void AddProduct() {
        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                String quantity = edt_quantity.getText().toString().trim();
                String price = edt_price.getText().toString().trim();
                int status = rdo_available.isChecked() ? 0 : 1;
                String description = edt_description.getText().toString().trim();

                if (!ValidateInputFields(
                        name,edt_name,
                        quantity,edt_quantity,
                        price,edt_price,
                        description,edt_description)) {
                    return;
                }
                if (file == null) {
                    Toast.makeText(AddProductActivity.this, "Thumbnail is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"),name);
                RequestBody _quantity = RequestBody.create(MediaType.parse("multipart/form-data"),quantity);
                RequestBody _price = RequestBody.create(MediaType.parse("multipart/form-data"),price);
                RequestBody _status = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(status));
                RequestBody _description = RequestBody.create(MediaType.parse("multipart/form-data"),description);
                RequestBody _id_category = RequestBody.create(MediaType.parse("multipart/form-data"),idCategorySelected);
                RequestBody _id_distributor = RequestBody.create(MediaType.parse("multipart/form-data"),idDistributorSelected);

                MultipartBody.Part multipartBody;
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
                multipartBody = MultipartBody.Part.createFormData("thumbnail",file.getName(),requestFile);
                httpRequest.callAPI().addProduct(
                        TokenManager.getInstance(AddProductActivity.this).getToken(),
                        _name,
                        _quantity,
                        _price,
                        _status,
                        multipartBody,
                        _description,
                        _id_category,
                        _id_distributor).enqueue(addProduct);
            }
        });
    }
    private void SelectedThumbnail() {
        img_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });
    }
    private void CloseForm() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    Callback<Response<Product>> addProduct = new Callback<Response<Product>>() {
        @Override
        public void onResponse(Call<Response<Product>> call, retrofit2.Response<Response<Product>> response) {
            if(response.body().getStatus() == 200) {
                Toast.makeText(AddProductActivity.this, "Add Product Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onFailure(Call<Response<Product>> call, Throwable t) {

        }
    };
    private void ChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            file = CreateFileFormUri(imageUri,"product");
            Glide.with(AddProductActivity.this)
                    .load(file)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.img_home_1)
                    .placeholder(R.drawable.img_home_1)
                    .into(img_thumbnail);
        }
    }
    private File CreateFileFormUri (Uri path, String name) {
        File _file = new File(AddProductActivity.this.getCacheDir(),name + ".png");
        try {
            InputStream in = AddProductActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len=in.read(buf)) > 0) {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
            return _file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void initUI() {
        rdo_available = findViewById(R.id.rdo_available);
        rdo_soldOut = findViewById(R.id.rdo_soldOut);
        edt_name = findViewById(R.id.edt_name);
        edt_quantity = findViewById(R.id.edt_quantity);
        edt_price = findViewById(R.id.edt_price);
        edt_description = findViewById(R.id.edt_description);
        spinner_category = findViewById(R.id.spinner_category);
        spinner_distributor = findViewById(R.id.spinner_distributor);
        img_thumbnail = findViewById(R.id.img_thumbnail);
        btn_back = findViewById(R.id.btn_back);
        btn_addProduct = findViewById(R.id.btn_addProduct);
        httpRequest = new HttpRequest();
    }
}