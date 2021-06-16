package com.payment.yatvik.mygooglepay.Model;

public class TransactionHistoryModel {
    private int id;
    private int user_id;
    private String method;
    private int type;
    private String created_at;
    private Double amount;
    private String by_admin;

    public TransactionHistoryModel(int id, int user_id, String method, int type, String created_at, Double amount, String by_admin) {
        this.id = id;
        this.user_id = user_id;
        this.method = method;
        this.type = type;
        this.created_at = created_at;
        this.amount = amount;
        this.by_admin = by_admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBy_admin() {
        return by_admin;
    }

    public void setBy_admin(String by_admin) {
        this.by_admin = by_admin;
    }
}
