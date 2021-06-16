package com.payment.yatvik.mygooglepay.Model;

public class SubCategoryModel {

    String id;
    String name;
    private String reg_exp;
    private String display_value;
    private int has_grouping;

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

    public String getReg_exp() {
        return reg_exp;
    }

    public void setReg_exp(String reg_exp) {
        this.reg_exp = reg_exp;
    }

    public String getDisplay_value() {
        return display_value;
    }

    public void setDisplay_value(String display_value) {
        this.display_value = display_value;
    }

    public int getHas_grouping() {
        return has_grouping;
    }

    public void setHas_grouping(int has_grouping) {
        this.has_grouping = has_grouping;
    }
}
