package com.ph41626.and103_assignment.Activity;

import static com.ph41626.and103_assignment.Services.Services.PICK_IMAGE_REQUEST;
import static com.ph41626.and103_assignment.Services.Services.ReadObjectToFile;
import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;
import static com.ph41626.and103_assignment.Services.Services.filePathAddressInfo;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ph41626.and103_assignment.Model.AddressInfo;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.User;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UserInformationActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ACTIVITY_BACK = 1;
    private TextView tv_address;
    private ImageView img_thumbnail;
    private ImageButton btn_back;
    private EditText edt_name;
    private LinearLayout btn_selectedAddress;
    private Button btn_update;
    private User user = new User();
    private File file = null;
    private AddressInfo addressInfo = new AddressInfo();
    private HttpRequest httpRequest;
    private boolean isChange = false;

    @Override
    protected void onStop() {
        super.onStop();
        if (isChange) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        initUI();
        GetDataFromProfile();
        SelectedAddress();
        UpdateUserInformation();
        addressInfo = (AddressInfo) ReadObjectToFile(this,user.get_id() + filePathAddressInfo);
        if (addressInfo != null) {
            FillInfo();
        } else {
            addressInfo = new AddressInfo();
        }

        img_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ChooseImage();}});
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChange) {
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
    }
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
            file = CreateFileFormUri(imageUri,"avatar");
            Glide.with(UserInformationActivity.this)
                    .load(file)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.img_home_1)
                    .placeholder(R.drawable.img_home_1)
                    .into(img_thumbnail);
        } else if (requestCode == REQUEST_CODE_ACTIVITY_BACK && resultCode == RESULT_OK ) {
            addressInfo = (AddressInfo) ReadObjectToFile(this,user.get_id() + filePathAddressInfo);
            if (addressInfo != null) {
                FillInfo();
            } else {
                addressInfo = new AddressInfo();
            }
        }
    }
    private File CreateFileFormUri (Uri path, String name) {
        File _file = new File(UserInformationActivity.this.getCacheDir(),name + ".png");
        try {
            InputStream in = UserInformationActivity.this.getContentResolver().openInputStream(path);
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
    private void UpdateUserInformation() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange = true;
                String name = edt_name.getText().toString().trim();
                if (!ValidateName(name) && !name.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                    edt_name.setError("Name is required!");
                    return;
                }
                MultipartBody.Part multipartBody = null;
                RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"),name);
                RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"),user.getEmail());
                RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"),user.getPassword());
                if (file == null) {
                    user.setName(name);
                    httpRequest.callAPI().updateUserWithoutAvatar(
                            user.get_id(),
                            user
                    ).enqueue(updateUser);
                } else {
                    user.setName(name);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                    httpRequest.callAPI().updateUser(
                            user.get_id(),
                            _name,
                            _email,
                            _password,
                            multipartBody
                    ).enqueue(updateUser);
                }
            }
        });
    }
    Callback<Response<User>> updateUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(UserInformationActivity.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {

        }
    };
    private boolean ValidateName(String name) {
        return !name.isEmpty();
    }
    private void SelectedAddress() {
        btn_selectedAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange = true;
                Intent intent = new Intent(UserInformationActivity.this, AddressesActivity.class);
                intent.putExtra("user",user);
                startActivityForResult(intent,REQUEST_CODE_ACTIVITY_BACK);
            }
        });
    }
    private void FillInfo() {
        String addressLine = user.getName() + " | " + addressInfo.getPhone() + "\n" + addressInfo.getAddress() + "\n";
        addressLine += addressInfo.getProvince().getProvinceName() + ", ";
        addressLine += addressInfo.getDistrict().getDistrictName() + ", ";
        addressLine += addressInfo.getWard().getWardName();
        tv_address.setText(addressLine);
    }
    private void GetDataFromProfile() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        edt_name.setText(user.getName());
        Glide.with(this)
                .load(convertLocalhostToIpAddress(user.getAvatar()))
                .error(R.drawable.circle_user_profile_avatar)
                .placeholder(R.drawable.circle_user_profile_avatar)
                .into(img_thumbnail);
    }

    private void initUI() {
        tv_address = findViewById(R.id.tv_address);
        img_thumbnail = findViewById(R.id.img_thumbnail);
        edt_name = findViewById(R.id.edt_name);
        btn_update = findViewById(R.id.btn_update);
        btn_selectedAddress = findViewById(R.id.btn_selectedAddress);
        btn_back = findViewById(R.id.btn_back);

        httpRequest = new HttpRequest();
    }
}