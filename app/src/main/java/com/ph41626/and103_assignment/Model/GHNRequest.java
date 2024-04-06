package com.ph41626.and103_assignment.Model;

import static com.ph41626.and103_assignment.Services.GHNServices.GHN_URL;

import com.ph41626.and103_assignment.Services.GHNServices;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GHNRequest {
    public final static String SHOPID = "Your SHOPID";

    public final static String TokenGHN = "Your TokenGHN";
    private GHNServices ghnServicesInterface;

    public GHNRequest() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("ShopId", SHOPID)
                        .addHeader("Token",TokenGHN)
                        .build();
                return chain.proceed(request);
            }
        });

        ghnServicesInterface = new Retrofit.Builder()
                .baseUrl(GHN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(GHNServices.class);
    }
    public  GHNServices callAPI() {
        return  ghnServicesInterface;
    }
}
