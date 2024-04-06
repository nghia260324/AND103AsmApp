package com.ph41626.and103_assignment.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class ViewModel extends androidx.lifecycle.ViewModel {
    private MutableLiveData<ArrayList<Product>> listProducts = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Category>> listCategories = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Distributor>> listDistributors = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Cart>> listCarts = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Order>> listOrders = new MutableLiveData<>();
    private MutableLiveData<User> saveUser = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Product>> listProductsSearch = new MutableLiveData<>();


    public void changeDataProducts(ArrayList<Product> products) {
        listProducts.setValue(products);
    }
    public void changeDataCategories(ArrayList<Category> categories) {
        listCategories.setValue(categories);
    }
    public void changeDataCarts(ArrayList<Cart> carts) {
        listCarts.setValue(carts);
    }
    public void changeDataDistributors(ArrayList<Distributor> distributors) {
        listDistributors.setValue(distributors);
    }
    public void changeDataOrders(ArrayList<Order> orders) {
        listOrders.setValue(orders);
    }
    public void changeUser(User getUser){saveUser.setValue(getUser);}
    public void changeDataSearch(ArrayList<Product> products) {listProductsSearch.setValue(products);}
    public LiveData<ArrayList<Product>> getChangeDataProducts() {
        return listProducts;
    }
    public LiveData<ArrayList<Category>> getChangeDataCategories() {return listCategories;}
    public LiveData<ArrayList<Distributor>> getChangeDataDistributors() {return listDistributors;}
    public LiveData<ArrayList<Cart>> getChangeDataCarts() {return listCarts;}
    public LiveData<ArrayList<Order>> getChangeDataOrders() {return listOrders;}
    public LiveData<User> getChangeUser() {return saveUser;}
    public LiveData<ArrayList<Product>> getChangeDataSearch() {return listProductsSearch;}
}
