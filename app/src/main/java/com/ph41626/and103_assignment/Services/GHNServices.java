package com.ph41626.and103_assignment.Services;

import com.ph41626.and103_assignment.Model.District;
import com.ph41626.and103_assignment.Model.DistrictRequest;
import com.ph41626.and103_assignment.Model.GHNOrderDetail;
import com.ph41626.and103_assignment.Model.GHNOrderRequest;
import com.ph41626.and103_assignment.Model.GHNOrderRespone;
import com.ph41626.and103_assignment.Model.Province;
import com.ph41626.and103_assignment.Model.ResponeGHN;
import com.ph41626.and103_assignment.Model.Ward;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GHNServices {
    public static String GHN_URL = "https://dev-online-gateway.ghn.vn/";
    @GET("shiip/public-api/master-data/province")
    Call<ResponeGHN<ArrayList<Province>>> getListProvince();
    @POST("shiip/public-api/master-data/district")
    Call<ResponeGHN<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest);
    @GET("shiip/public-api/master-data/ward")
    Call<ResponeGHN<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);
    @POST("shiip/public-api/v2/shipping-order/create")
    Call<ResponeGHN<GHNOrderRespone>> GHNOrder(@Body GHNOrderRequest ghnOrderRequest);
    @GET("shiip/public-api/v2/shipping-order/detail")
    Call<ResponeGHN<GHNOrderDetail>> GHNOrderDetail (@Query("order_code") String order_code);
}
