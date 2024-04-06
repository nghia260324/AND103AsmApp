package com.ph41626.and103_assignment.Services;

import com.ph41626.and103_assignment.Activity.UserInformationActivity;
import com.ph41626.and103_assignment.Model.Cart;
import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.Model.Distributor;
import com.ph41626.and103_assignment.Model.Order;
import com.ph41626.and103_assignment.Model.OrderDetail;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.User;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiServices {
    //public static final String BASE_URL = "http://10.0.2.2:3000/";
    //public static final String BASE_URL = "http://192.168.0.4:3000/";
    public static final String BASE_URL = "http://192.168.181.223:3000/";
    @GET("/check-email")
    Call<Response<Boolean>> checkEmailExists(@Query("email") String email);
    @GET("/get-user-by-email")
    Call<Response<User>> getUserByEmail(@Query("email") String email);
    @Multipart
    @POST("/register-account")
    Call<Response<User>> registerAccount(
            @Part("email")RequestBody email,
            @Part("password")RequestBody password,
            @Part("name")RequestBody name,
            @Part MultipartBody.Part avatar);
    @GET("/get-list-categories")
    Call<Response<ArrayList<Category>>> getListCategories(
            @Header("authorization") String token
    );
    @POST("/add-category")
    Call<Response<Category>> addCategory(
            @Header("authorization") String token,
            @Body Category category);
    @DELETE("/delete-category/{id}")
    Call<Response<Category>> deleteCategory(
            @Header("authorization") String token,
            @Path("id")String id);
    @PUT("/update-category/{id}")
    Call<Response<Category>> updateCategory(
            @Header("authorization") String token,
            @Path("id")String id,
            @Body Category category);
    @GET("/check-category/{id}")
    Call<Response<Category>> checkCategory(@Path("id")String id);

    //////////
    @GET("/get-list-distributors")
    Call<Response<ArrayList<Distributor>>> getListDistributors(
            @Header("authorization") String token
    );
    @POST("/add-distributor")
    Call<Response<Distributor>> addDistributor(
            @Header("authorization") String token,
            @Body Distributor distributor);
    @DELETE("/delete-distributor/{id}")
    Call<Response<Distributor>> deleteDistributor(
            @Header("authorization") String token,
            @Path("id")String id);
    @PUT("/update-distributor/{id}")
    Call<Response<Distributor>> updateDistributor(
            @Header("authorization") String token,
            @Path("id")String id,
            @Body Distributor distributor);
    @GET("/check-distributor/{id}")
    Call<Response<Distributor>> checkDistributor(@Path("id")String id);

    /////
    @GET("/get-list-products")
    Call<Response<ArrayList<Product>>> getListProducts(@Header("authorization")String token);
    @Multipart
    @POST("/add-product")
    Call<Response<Product>> addProduct(
            @Header("authorization") String token,
            @Part("name")RequestBody name,
            @Part("quantity")RequestBody quantity,
            @Part("price")RequestBody price,
            @Part("status")RequestBody status,
            @Part MultipartBody.Part thumbnail,
            @Part("description")RequestBody description,
            @Part("id_category")RequestBody id_category,
            @Part("id_distributor")RequestBody id_distributor);
    @Multipart
    @PUT("/update-product/{id_product}")
    Call<Response<Product>> updateProduct(
            @Header("authorization") String token,
            @Path("id_product")String id_product,
            @Part("name") RequestBody name,
            @Part("quantity") RequestBody quantity,
            @Part("price") RequestBody price,
            @Part("status") RequestBody status,
            @Part MultipartBody.Part thumbnail,
            @Part("description") RequestBody description,
            @Part("id_category") RequestBody id_category,
            @Part("id_distributor") RequestBody id_distributor);

    @POST("/update-product-without-thumbnail/{id_product}")
    Call<Response<Product>> updateProductWithoutThumbnail(
            @Header("authorization") String token,
            @Path("id_product")String id,
            @Body Product product);
    @DELETE("/delete-product/{id}")
    Call<Response<Product>> deleteProduct(
            @Header("authorization") String token,
            @Path("id")String id);

    @GET("/search-product")
    Call<Response<ArrayList<Product>>> searchProduct(
            @Header("authorization") String token,
            @QueryMap Map<String,String> data);


    /////
    @POST("/add-cart")
    Call<Response<Cart>> addCart (
            @Header("authorization") String token,
            @Body Cart cart);
    @GET("/get-list-cart-by-id/{id_user}")
    Call<Response<ArrayList<Cart>>> getListCartsById(
            @Header("authorization") String token,
            @Path("id_user") String id_user);
    @PUT("/update-cart/{id_cart}")
    Call<Response<Cart>> updateCartItem (@Path("id_cart") String id_cart,@Body Cart cart);
    @PUT("/update-cart")
    Call<Response<ArrayList<Cart>>> updateCart (@Body ArrayList<Cart> carts);
    @DELETE("/delete-cartItem")
    Call<Response<ArrayList<Cart>>> deleteCartItems (@Query("carts") ArrayList<String> carts);

    @Multipart
    @PUT("/update-account/{id_user}")
    Call<Response<User>> updateUser(
            @Path("id_user")String id_user,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part thumbnail);


    @POST("/update-account-without-avatar/{id_user}")
    Call<Response<User>> updateUserWithoutAvatar(@Path("id_user")String id, @Body User user);

    @POST("/add-order")
    Call<Response<Order>> order(
            @Header("authorization") String token,
            @Body Order order);
    @POST("/add-orderDetail")
    Call<Response<OrderDetail>> addOrderDetail(
            @Header("authorization") String token,
            @Body OrderDetail orderDetail
    );
    @GET("/get-list-order")
    Call<Response<ArrayList<Order>>> getListOrders(@Query("id_user")String id_user);

    @GET("/get-orderDetail-by-id/{id}")
    Call<Response<OrderDetail>> getOrderDetail(@Path("id")String id);
}
