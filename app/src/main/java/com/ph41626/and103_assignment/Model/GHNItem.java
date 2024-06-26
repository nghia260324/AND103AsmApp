package com.ph41626.and103_assignment.Model;

import java.io.Serializable;

public class GHNItem implements Serializable {
    private String name,code;
    private int quantity,price,weight;

    public GHNItem() {
    }

    public GHNItem(String name, String code, int quantity, int price, int weight) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.price = price;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "GHNItem{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", weight=" + weight +
                '}';
    }
}
