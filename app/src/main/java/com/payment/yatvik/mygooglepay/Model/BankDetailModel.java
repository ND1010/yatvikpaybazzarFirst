package com.payment.yatvik.mygooglepay.Model;

public class BankDetailModel {

    String id;
    String ifsc;
    String bankname;

    public BankDetailModel(String id, String ifsc, String bankname) {
        this.id = id;
        this.ifsc = ifsc;
        this.bankname = bankname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    @Override
    public String toString() {
        return "BankDetailModel{" +
                "id='" + id + '\'' +
                ", ifsc='" + ifsc + '\'' +
                ", bankname='" + bankname + '\'' +
                '}';
    }
}
