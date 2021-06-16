package com.payment.yatvik.mygooglepay.Helpers;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class GlobalClass extends Application {

    public final static String rupee = "\u20B9 ";
    public final static String getAddressByIDUrl = "http://airveg.in/beta/admin/Api/getAddressByID/";
    public final static String addresDeleteUrl = "http://airveg.in/beta/admin/Api/deleteAddressByID/";
    public final static String deliverableArea = "http://airveg.in/beta/admin/Api/isDeliverable/";
    public final static String deliverablePincode = "http://airveg.in/beta/admin/Api/getPincodes/";
    public final static String BaseUrlForProduct = "http://airveg.in/beta/admin/vendordata/product/";


    private String userId;
    private String token;

    private List<String> menuslist = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    private String someVariable;

    public String getWhichButton() {
        return someVariable;
    }

    public void setWhichButton(String someVariable) {
        this.someVariable = someVariable;
    }

    public List<String> getMenuslist() {
        return menuslist;
    }

    public void setMenuslist(List<String> menuslist) {
        this.menuslist = menuslist;
    }
}
