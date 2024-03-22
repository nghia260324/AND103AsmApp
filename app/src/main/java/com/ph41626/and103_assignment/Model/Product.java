package com.ph41626.and103_assignment.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private int quantity;
    private int price;
    private int status;
    private String thumbnail;
    private String description;
    private String id_category;
    private String id_distributor;

    public Product() {
    }

    public Product(String id, String name, int quantity, int price, int status, String thumbnail, String description, String id_category, String id_distributor) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.thumbnail = thumbnail;
        this.description = description;
        this.id_category = id_category;
        this.id_distributor = id_distributor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getId_distributor() {
        return id_distributor;
    }

    public void setId_distributor(String id_distributor) {
        this.id_distributor = id_distributor;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", status=" + status +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                ", id_category='" + id_category + '\'' +
                ", id_distributor='" + id_distributor + '\'' +
                '}';
    }
}
