package com.payment.yatvik.mygooglepay.Model;

public class DmrReportModel {
    private String amount;
    private String status;
    private String sendername;
    private String orderId;
    private String beneficiaryname;
    private String ifsc;
    private String account;

    public DmrReportModel(String amount, String status, String sendername, String orderId, String beneficiaryname, String ifsc, String account) {
        this.amount = amount;
        this.status = status;
        this.sendername = sendername;
        this.orderId = orderId;
        this.beneficiaryname = beneficiaryname;
        this.ifsc = ifsc;
        this.account = account;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBeneficiaryname() {
        return beneficiaryname;
    }

    public void setBeneficiaryname(String beneficiaryname) {
        this.beneficiaryname = beneficiaryname;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
