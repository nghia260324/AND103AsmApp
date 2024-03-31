package com.ph41626.and103_assignment.Activity;

import static com.ph41626.and103_assignment.Services.Services.ReadObjectToFile;
import static com.ph41626.and103_assignment.Services.Services.WriteObjectToFile;
import static com.ph41626.and103_assignment.Services.Services.filePathAddressInfo;
import static com.ph41626.and103_assignment.Services.Services.findObjectById;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ph41626.and103_assignment.Adapter.SpinnerDistrictAdapter;
import com.ph41626.and103_assignment.Adapter.SpinnerProvinceAdapter;
import com.ph41626.and103_assignment.Adapter.SpinnerWardAdapter;
import com.ph41626.and103_assignment.Model.AddressInfo;
import com.ph41626.and103_assignment.Model.District;
import com.ph41626.and103_assignment.Model.DistrictRequest;
import com.ph41626.and103_assignment.Model.GHNRequest;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Province;
import com.ph41626.and103_assignment.Model.ResponeGHN;
import com.ph41626.and103_assignment.Model.User;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.Model.Ward;
import com.ph41626.and103_assignment.R;
import com.ph41626.and103_assignment.Services.HttpRequest;
import com.ph41626.and103_assignment.Services.Services;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AddressesActivity extends AppCompatActivity {
    private EditText edt_myAddress,edt_myPhone;
    private TextView tv_address;
    private Button btn_update;
    private Spinner spinner_province,spinner_district,spinner_ward;
    private SpinnerProvinceAdapter spinnerProvinceAdapter;
    private SpinnerDistrictAdapter spinnerDistrictAdapter;
    private SpinnerWardAdapter spinnerWardAdapter;
    private User user;
    private GHNRequest ghnRequest;
    private String myAddress = "Your Address";
    private String myPhone = "Your Phone";
    private Province getProvince = null;
    private District getDistrict = null;
    private Ward getWard = null;
    private AddressInfo addressInfo = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresses);

        initUI();
        GetDataFromOrderActivity();
        GetMyAddressAndPhone();
        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinner_province) {
                    Province province = (Province)parent.getAdapter().getItem(position);
                    getProvince = province;
                    DistrictRequest districtRequest = new DistrictRequest(province.getProvinceID());
                    ghnRequest.callAPI().getListDistrict(districtRequest).enqueue(getDistricts);
                } else if (parent.getId() == R.id.spinner_district) {
                    District district = (District) parent.getAdapter().getItem(position);
                    getDistrict = district;
                    ghnRequest.callAPI().getListWard(district.getDistrictID()).enqueue(getWards);
                } else if (parent.getId() == R.id.spinner_ward) {
                    Ward ward = (Ward) parent.getAdapter().getItem(position);
                    getWard = ward;
                }
                FillAddress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner_province.setOnItemSelectedListener(onItemSelectedListener);
        spinner_district.setOnItemSelectedListener(onItemSelectedListener);
        spinner_ward.setOnItemSelectedListener(onItemSelectedListener);
        spinner_province.setSelection(0);
        ghnRequest.callAPI().getListProvince().enqueue(getProvinces);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressInfo = new AddressInfo();
                addressInfo.setId_user(user.get_id());
                addressInfo.setAddress(myAddress);
                addressInfo.setPhone(myPhone);
                addressInfo.setProvince(getProvince);
                addressInfo.setDistrict(getDistrict);
                addressInfo.setWard(getWard);
                WriteObjectToFile(AddressesActivity.this,user.get_id() + filePathAddressInfo,addressInfo);
                Toast.makeText(AddressesActivity.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void GetDataFromOrderActivity() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        addressInfo = (AddressInfo) ReadObjectToFile(AddressesActivity.this,user.get_id() + filePathAddressInfo);
        if (addressInfo != null && addressInfo.getId_user().equals(user.get_id())) {
            getProvince = addressInfo.getProvince();
            getDistrict = addressInfo.getDistrict();
            getWard = addressInfo.getWard();
            myPhone = addressInfo.getPhone();
            myAddress = addressInfo.getAddress();
        } else {
            addressInfo = null;
        }
    }
    private void GetMyAddressAndPhone() {
        edt_myAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myAddress = s.toString();
                FillAddress();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_myPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myPhone = s.toString();
                FillAddress();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void FillAddress() {
        String addressLine = user.getName() + " | " + myPhone + "\n" + myAddress + "\n";
        if (getProvince != null) {
            addressLine += getProvince.getProvinceName() + ", ";
        }
        if (getDistrict != null) {
            addressLine += getDistrict.getDistrictName() + ", ";
        }
        if (getWard != null) {
            addressLine += getWard.getWardName();
        }
        tv_address.setText(addressLine);
    }
    Callback<ResponeGHN<ArrayList<Province>>> getProvinces = new Callback<ResponeGHN<ArrayList<Province>>>() {
        @Override
        public void onResponse(Call<ResponeGHN<ArrayList<Province>>> call, retrofit2.Response<ResponeGHN<ArrayList<Province>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<Province> provinces = new ArrayList<>(response.body().getData());
                    spinnerProvinceAdapter = new SpinnerProvinceAdapter(AddressesActivity.this,R.layout.item_view_spinner,provinces);
                    spinner_province.setAdapter(spinnerProvinceAdapter);
                    if (addressInfo != null) {
                        int index = provinces.indexOf(findObjectById(provinces,String.valueOf(addressInfo.getProvince().getProvinceID())));
                        spinner_province.setSelection(index);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeGHN<ArrayList<Province>>> call, Throwable t) {

        }
    };
    Callback<ResponeGHN<ArrayList<District>>> getDistricts = new Callback<ResponeGHN<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<ResponeGHN<ArrayList<District>>> call, retrofit2.Response<ResponeGHN<ArrayList<District>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<District> districts = new ArrayList<>(response.body().getData());
                    spinnerDistrictAdapter = new SpinnerDistrictAdapter(AddressesActivity.this,R.layout.item_view_spinner,districts);
                    spinner_district.setAdapter(spinnerDistrictAdapter);
                    if (addressInfo != null) {
                        int index = districts.indexOf(findObjectById(districts,String.valueOf(addressInfo.getDistrict().getDistrictID())));
                        spinner_district.setSelection(index);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeGHN<ArrayList<District>>> call, Throwable t) {

        }
    };
    Callback<ResponeGHN<ArrayList<Ward>>> getWards = new Callback<ResponeGHN<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<ResponeGHN<ArrayList<Ward>>> call, retrofit2.Response<ResponeGHN<ArrayList<Ward>>> response) {
            if (response != null && response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<Ward> wards = new ArrayList<>(response.body().getData());
                    spinnerWardAdapter = new SpinnerWardAdapter(AddressesActivity.this,R.layout.item_view_spinner,wards);
                    spinner_ward.setAdapter(spinnerWardAdapter);
                    if (addressInfo != null) {
                        int index = wards.indexOf(findObjectById(wards,String.valueOf(addressInfo.getWard().getWardCode())));
                        spinner_ward.setSelection(index);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponeGHN<ArrayList<Ward>>> call, Throwable t) {

        }
    };
    private void initUI() {
        spinner_province = findViewById(R.id.spinner_province);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_ward = findViewById(R.id.spinner_ward);

        tv_address = findViewById(R.id.tv_address);
        edt_myAddress = findViewById(R.id.edt_myAddress);
        edt_myPhone = findViewById(R.id.edt_myPhone);
        btn_update = findViewById(R.id.btn_update);

        ghnRequest = new GHNRequest();
    }
}