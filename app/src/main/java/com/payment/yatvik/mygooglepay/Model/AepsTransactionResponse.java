package com.payment.yatvik.mygooglepay.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AepsTransactionResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("txnDate")
    @Expose
    private String txnDate;
    @SerializedName("Aadhar")
    @Expose
    private String aadhar;
    @SerializedName("IIN")
    @Expose
    private String iin;
    @SerializedName("STAN")
    @Expose
    private String stan;
    @SerializedName("RRN")
    @Expose
    private String rrn;
    @SerializedName("BalanceAmount")
    @Expose
    private String balanceAmount;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("TxnId")
    @Expose
    private String txnId;
    @SerializedName("OP")
    @Expose
    private String op;
    @SerializedName("ST")
    @Expose
    private String st;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }
}