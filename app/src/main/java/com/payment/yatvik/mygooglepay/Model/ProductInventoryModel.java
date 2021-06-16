package com.payment.yatvik.mygooglepay.Model;

public class ProductInventoryModel {
    private String id;
    private String status;
    private String membername;
    private String mobile;
    private String packagename;
    private String membertype;
    private String owner;
    private String regdate;

    public ProductInventoryModel(String id, String status, String membername, String mobile, String packagename, String membertype, String owner, String regdate) {
        this.id = id;
        this.status = status;
        this.membername = membername;
        this.mobile = mobile;
        this.packagename = packagename;
        this.membertype = membertype;
        this.owner = owner;
        this.regdate = regdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getMembertype() {
        return membertype;
    }

    public void setMembertype(String membertype) {
        this.membertype = membertype;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }
}
