package com.ph41626.and103_assignment.Services;

import com.ph41626.and103_assignment.Model.Cart;
import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.Model.Distributor;
import com.ph41626.and103_assignment.Model.Order;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Response;
import com.ph41626.and103_assignment.Model.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {
    //public static final String BASE_URL = "http://10.0.2.2:3000/";
    public static final String BASE_URL = "http://192.168.0.3:3000/";
    //public static final String BASE_URL = "http://192.168.171.223:3000/";
    @GET("/check-email")
    Call<Response<Boolean>> checkEmailExists(@Query("email") String email);
    @GET("/get-user-by-email")
    Call<Response<User>> getUserByEmail(@Query("email") String email);
    @Multipart
    @POST("/register-account")
    Call<Response<User>> registerAccount(@Part("email")RequestBody email,
                                         @Part("password")RequestBody password,
                                         @Part("name")RequestBody name,
                                         @Part MultipartBody.Part avatar);
    @GET("/get-list-categories")
    Call<Response<ArrayList<Category>>> getListCategories();
    @POST("/add-category")
    Call<Response<Category>> addCategory(@Body Category category);
    @DELETE("/delete-category/{id}")
    Call<Response<Category>> deleteCategory(@Path("id")String id);
    @PUT("/update-category/{id}")
    Call<Response<Category>> updateCategory(@Path("id")String id,@Body Category category);
    @GET("/check-category/{id}")
    Call<Response<Category>> checkCategory(@Path("id")String id);

    //////////
    @GET("/get-list-distributors")
    Call<Response<ArrayList<Distributor>>> getListDistributors();
    @POST("/add-distributor")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);
    @DELETE("/delete-distributor/{id}")
    Call<Response<Distributor>> deleteDistributor(@Path("id")String id);
    @PUT("/update-distributor/{id}")
    Call<Response<Distributor>> updateDistributor(@Path("id")String id,@Body Distributor distributor);
    @GET("/check-distributor/{id}")
    Call<Response<Distributor>> checkDistributor(@Path("id")String id);

    /////
    @GET("/get-list-products")
    Call<Response<ArrayList<Product>>> getListProducts();
    @Multipart
    @POST("/add-product")
    Call<Response<Product>> addProduct(@Part("name")RequestBody name,
                                       @Part("quantity")RequestBody quantity,
                                       @Part("price")RequestBody price,
                                       @Part("status")RequestBody status,
                                       @Part MultipartBody.Part thumbnail,
                                       @Part("description")RequestBody description,
                                       @Part("id_category")RequestBody id_category,
                                       @Part("id_distributor")RequestBody id_distributor);
    @Multipart
    @PUT("/update-product/{id_product}")
    Call<Response<Product>> updateProduct(@Path("id_product")String id_product,
                                          @Part("name") RequestBody name,
                                          @Part("quantity") RequestBody quantity,
                                          @Part("price") RequestBody price,
                                          @Part("status") RequestBody status,
                                          @Part MultipartBody.Part thumbnail,
                                          @Part("description") RequestBody description,
                                          @Part("id_category") RequestBody id_category,
                                          @Part("id_distributor") RequestBody id_distributor);

    @POST("/update-product-without-thumbnail/{id_product}")
    Call<Response<Product>> updateProductWithoutThumbnail(@Path("id_product")String id,@Body Product product);
    @DELETE("/delete-product/{id}")
    Call<Response<Product>> deleteProduct(@Path("id")String id);

    @GET("/search-product")
    Call<Response<ArrayList<Product>>> searchProduct(@Query("key")String key);


    /////
    @POST("/add-cart")
    Call<Response<Cart>> addCart (@Body Cart cart);
    @GET("/get-list-cart-by-id/{id_user}")
    Call<Response<ArrayList<Cart>>> getListCartsById(@Path("id_user") String id_user);
    @PUT("/update-cart/{id_cart}")
    Call<Response<Cart>> updateCartItem (@Path("id_cart") String id_cart,@Body Cart cart);
    @PUT("/update-cart")
    Call<Response<ArrayList<Cart>>> updateCart (@Body ArrayList<Cart> carts);
//    @POST("/add-bill")
//    Call<Response<>>
//
    @POST("/add-order")
    Call<Response<Order>> order(@Body Order order);
}
