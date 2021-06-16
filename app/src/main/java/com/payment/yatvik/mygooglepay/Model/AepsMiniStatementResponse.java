package com.payment.yatvik.mygooglepay.Model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AepsMiniStatementResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
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
    @SerializedName("paidAmount")
    @Expose
    private String paidAmount;
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
    @SerializedName("miniStatement")
    @Expose
    private List<MiniStatement> miniStatement = null;

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

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
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

    public List<MiniStatement> getMiniStatement() {
        return miniStatement;
    }

    public void setMiniStatement(List<MiniStatement> miniStatement) {
        this.miniStatement = miniStatement;
    }

    public class MiniStatement {

        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("txnType")
        @Expose
        private String txnType;
        @SerializedName("narration")
        @Expose
        private String narration;
        @SerializedName("amount")
        @Expose
        private String amount;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTxnType() {
            return txnType;
        }

        public void setTxnType(String txnType) {
            this.txnType = txnType;
        }

        public String getNarration() {
            return narration;
        }

        public void setNarration(String narration) {
            this.narration = narration;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

    }
}
