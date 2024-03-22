package com.ph41626.and103_assignment.Activity;

import static com.ph41626.and103_assignment.Services.Services.PICK_IMAGE_REQUEST;
import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_assignment.Services.Services.findCategoryIndexById;
import static com.ph41626.and103_assignment.Services.Services.findDistributorIndexById;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ph41626.and103_assignment.Adapter.SpinnerCategoryAdapter;
import com.ph41626.and103_assignment.Adapter.SpinnerDistributorAdapter;
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
import java.util.stream.IntStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateProductActivity extends AppCompatActivity {
    private EditText edt_name,edt_price,edt_description,edt_quantity;
    private RadioButton rdo_available,rdo_soldOut;
    private Spinner spinner_category,spinner_distributor;
    private ImageButton img_thumbnail;
    private ImageButton btn_back;
    private Button btn_addProduct;
    private Product product = new Product();
    public ArrayList<Category> listCategories = new ArrayList<>();
    public ArrayList<Distributor> listDistributors = new ArrayList<>();
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

        product = (Product) intent.getSerializableExtra("product");
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
        if (product != null) {
            edt_name.setText(product.getName());
            edt_quantity.setText(String.valueOf(product.getQuantity()));
            edt_price.setText(String.valueOf(product.getPrice()));
            if(product.getStatus() == 0) {
                rdo_available.setChecked(true);
                rdo_soldOut.setChecked(false);
            } else {
                rdo_available.setChecked(false);
                rdo_soldOut.setChecked(true);
            }
            edt_description.setText(product.getDescription());
        }
        int indexCategory = findCategoryIndexById(listCategories,product.getId_category());
        int indexDistributor = findDistributorIndexById(listDistributors,product.getId_distributor());
        spinner_category.setSelection(indexCategory);
        spinner_distributor.setSelection(indexDistributor);
        Glide.with(UpdateProductActivity.this)
                .load(convertLocalhostToIpAddress(product.getThumbnail()))
                .error(R.drawable.img_home_1)
                .placeholder(R.drawable.img_home_1)
                .into(img_thumbnail);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        initUI();
        GetDataFromInventoryActivity();
        CloseForm();
        UpdateProduct();
        SelectedThumbnail();
    }
    private void SelectedThumbnail() {
        img_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });
    }
    private void UpdateProduct() {
        btn_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                String quantity = edt_quantity.getText().toString().trim();
                String price = edt_price.getText().toString().trim();
                int status = rdo_available.isChecked() ? 0 : 1;
                String description = edt_description.getText().toString().trim();

                product.setName(name);
                product.setQuantity(Integer.parseInt(quantity));
                product.setPrice(Integer.parseInt(price));
                product.setStatus(status);
                product.setDescription(description);
                product.setId_category(idCategorySelected);
                product.setId_distributor(idDistributorSelected);

                RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
                RequestBody _quantity = RequestBody.create(MediaType.parse("multipart/form-data"), quantity);
                RequestBody _price = RequestBody.create(MediaType.parse("multipart/form-data"), price);
                RequestBody _status = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(status));
                RequestBody _description = RequestBody.create(MediaType.parse("multipart/form-data"), description);
                RequestBody _id_category = RequestBody.create(MediaType.parse("multipart/form-data"), idCategorySelected);
                RequestBody _id_distributor = RequestBody.create(MediaType.parse("multipart/form-data"), idDistributorSelected);

                MultipartBody.Part multipartBody = null;
                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("thumbnail", file.getName(), requestFile);
                    httpRequest.callAPI().updateProduct(
                            product.getId(),
                            _name,
                            _quantity,
                            _price,
                            _status,
                            multipartBody,
                            _description,
                            _id_category,
                            _id_distributor).enqueue(updateProduct);
                } else {
                    httpRequest.callAPI().updateProductWithoutThumbnail(product.getId(), product).enqueue(updateProduct);
                }
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
    Callback<Response<Product>> updateProduct = new Callback<Response<Product>>() {
        @Override
        public void onResponse(Call<Response<Product>> call, retrofit2.Response<Response<Product>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(UpdateProductActivity.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                }
            } else {
                Log.e("Check Product", product.toString());
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
            Glide.with(UpdateProductActivity.this)
                    .load(file)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.img_home_1)
                    .placeholder(R.drawable.img_home_1)
                    .into(img_thumbnail);
        }
    }
    private File CreateFileFormUri (Uri path, String name) {
        File _file = new File(UpdateProductActivity.this.getCacheDir(),name + ".png");
        try {
            InputStream in = UpdateProductActivity.this.getContentResolver().openInputStream(path);
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