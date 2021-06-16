package com.payment.yatvik.mygooglepay.Model;

public class BillsHistoryModel {
    private String transId;
    private String status;
    private String date;
    private String service;
    private String operator;
    private String amount;
    private String mobile;
    private String message;

    public BillsHistoryModel(String transId, String status, String date, String service, String operator, String amount, String mobile, String message) {
        this.transId = transId;
        this.status = status;
        this.date = date;
        this.service = service;
        this.operator = operator;
        this.amount = amount;
        this.mobile = mobile;
        this.message = message;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
