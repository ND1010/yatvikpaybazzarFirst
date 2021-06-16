package com.payment.yatvik.mygooglepay.Model;

public class BillServicesModel {
    private String name;
    private int imgId;

    public BillServicesModel() {

    }

    public BillServicesModel(String name, int imgId) {
        this.name = name;
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
