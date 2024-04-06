package com.ph41626.and103_assignment.Model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OrderDetail implements Serializable {
    private String _id;
    private String id_order;
    private List<GHNItem> items;
    private String to_name;
    private String to_phone;
    private String to_address;
    private String to_ward_code;
    private int to_district_id;
    private int cod_amount;

    public OrderDetail() {
    }

    public OrderDetail(String _id, String id_order, List<GHNItem> items, String to_name, String to_phone, String to_address, String to_ward_code, int to_district_id, int cod_amount) {
        this._id = _id;
        this.id_order = id_order;
        this.items = items;
        this.to_name = to_name;
        this.to_phone = to_phone;
        this.to_address = to_address;
        this.to_ward_code = to_ward_code;
        this.to_district_id = to_district_id;
        this.cod_amount = cod_amount;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public List<GHNItem> getItems() {
        return items;
    }

    public void setItems(List<GHNItem> items) {
        this.items = items;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_phone() {
        return to_phone;
    }

    public void setTo_phone(String to_phone) {
        this.to_phone = to_phone;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getTo_ward_code() {
        return to_ward_code;
    }

    public void setTo_ward_code(String to_ward_code) {
        this.to_ward_code = to_ward_code;
    }

    public int getTo_district_id() {
        return to_district_id;
    }

    public void setTo_district_id(int to_district_id) {
        this.to_district_id = to_district_id;
    }

    public int getCod_amount() {
        return cod_amount;
    }

    public void setCod_amount(int cod_amount) {
        this.cod_amount = cod_amount;
    }
}
