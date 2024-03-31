package com.ph41626.and103_assignment.Model;

import java.io.Serializable;

public class AddressInfo implements Serializable {
    private String id_user;
    private String phone;
    private String address;
    private Province province;
    private District district;
    private Ward ward;

    public AddressInfo() {
    }

    public AddressInfo(String id_user, String phone, String address, Province province, District district, Ward ward) {
        this.id_user = id_user;
        this.phone = phone;
        this.address = address;
        this.province = province;
        this.district = district;
        this.ward = ward;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    @Override
    public String toString() {
        return "AddressInfo{" +
                "phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", province=" + province.toString() +
                ", district=" + district.toString() +
                ", ward=" + ward.toString() +
                '}';
    }
}
