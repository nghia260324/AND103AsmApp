package com.ph41626.and103_assignment.Services;

import static com.ph41626.and103_assignment.Services.ApiServices.BASE_URL;

import android.content.Context;

import com.ph41626.and103_assignment.Model.Cart;
import com.ph41626.and103_assignment.Model.Category;
import com.ph41626.and103_assignment.Model.Distributor;
import com.ph41626.and103_assignment.Model.District;
import com.ph41626.and103_assignment.Model.Product;
import com.ph41626.and103_assignment.Model.Province;
import com.ph41626.and103_assignment.Model.Ward;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Services {
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final String filePathAddressInfo = "AddressInfo.txt";
    public static final  <T> T findObjectById(ArrayList<T> list, String id) {
        for (T item : list) {
            if (item instanceof Distributor) {
                Distributor distributor = (Distributor) item;
                if (distributor.getId().equals(id)) {
                    return item;
                }
            } else if (item instanceof Category) {
                Category category = (Category) item;
                if (category.getId().equals(id)) {
                    return item;
                }
            } else if (item instanceof Product) {
                Product product = (Product) item;
                if (product.getId().equals(id)) {
                    return item;
                }
            } else if (item instanceof Cart) {
                Cart cart = (Cart) item;
                if (cart.getId_product().equals(id)) {
                    return item;
                }
            } else if (item instanceof Province) {
                Province province = (Province) item;
                if (String.valueOf(province.getProvinceID()).equals(id)) {
                    return item;
                }
            } else if (item instanceof District) {
                District district = (District) item;
                if (String.valueOf(district.getDistrictID()).equals(id)) {
                    return item;
                }
            } else if (item instanceof Ward) {
                Ward ward = (Ward) item;
                if (String.valueOf(ward.getWardCode()).equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }
    public static final String convertLocalhostToIpAddress(String url) {
        int index = url.indexOf("3000/");
        String newUrl = "";
        if (index != -1) {
            newUrl = BASE_URL + url.substring(index + 5);
        } else {
            newUrl = url;
        }
        return newUrl;
    }
    public static final String formatPrice(double n, String currency) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        String formattedPrice = decimalFormat.format(n).replaceAll("\\.00$", "");
        return formattedPrice + currency;
    }
    public static final int findCategoryIndexById(ArrayList<Category> listCategories, String categoryId) {
        for (int i = 0; i < listCategories.size(); i++) {
            Category category = listCategories.get(i);
            if (category.getId().equals(categoryId)) {
                return i;
            }
        }
        return -1;
    }
    public static final int findDistributorIndexById(ArrayList<Distributor> listDistributors, String distributorId) {
        for (int i = 0; i < listDistributors.size(); i++) {
            Distributor distributor = listDistributors.get(i);
            if (distributor.getId().equals(distributorId)) {
                return i;
            }
        }
        return -1;
    }
    public static final void WriteObjectToFile(Context context, String path, Object object){
        try {
            FileOutputStream fos = context.openFileOutput(path,Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final Object ReadObjectToFile(Context context, String path){
        Object object = null;
        try {
            FileInputStream fis = context.openFileInput(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            object =  ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
