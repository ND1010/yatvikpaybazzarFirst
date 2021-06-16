package com.payment.yatvik.mygooglepay.Model;

public class PayoutReportModel {
    private String orderId;
    private String status;
    private String date;
    private String accout;
    private String company;
    private String amount;
    private String message;

    public PayoutReportModel(String orderId, String status, String date, String accout, String company, String amount, String message) {
        this.orderId = orderId;
        this.status = status;
        this.date = date;
        this.accout = accout;
        this.company = company;
        this.amount = amount;
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getAccout() {
        return accout;
    }

    public void setAccout(String accout) {
        this.accout = accout;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
