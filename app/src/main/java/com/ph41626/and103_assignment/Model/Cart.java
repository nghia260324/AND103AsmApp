package com.ph41626.and103_assignment.Model;

import java.io.Serializable;

public class Cart implements Serializable {
    private String _id;
    private String id_user;
    private String id_product;
    private int quantity;
    private boolean isSelected;

    public Cart() {
    }

    public Cart(String _id, String id_user, String id_product, int quantity) {
        this._id = _id;
        this.id_user = id_user;
        this.id_product = id_product;
        this.quantity = quantity;
        isSelected = false;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "_id='" + _id + '\'' +
                ", id_user='" + id_user + '\'' +
                ", id_product='" + id_product + '\'' +
                ", quantity=" + quantity +
                ", isSelected=" + isSelected +
                '}';
    }
}
