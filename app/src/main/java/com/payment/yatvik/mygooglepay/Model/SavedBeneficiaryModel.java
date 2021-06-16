package com.payment.yatvik.mygooglepay.Model;

public class SavedBeneficiaryModel {

    String id;
    String benifiary_name;
    String mobile_number;
    String beneficiaryAccount;
    String beneficiaryIFSC;

    public SavedBeneficiaryModel(String id, String benifiary_name, String mobile_number, String beneficiaryAccount, String beneficiaryIFSC) {
        this.id = id;
        this.benifiary_name = benifiary_name;
        this.mobile_number = mobile_number;
        this.beneficiaryAccount = beneficiaryAccount;
        this.beneficiaryIFSC = beneficiaryIFSC;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBenifiary_name() {
        return benifiary_name;
    }

    public void setBenifiary_name(String benifiary_name) {
        this.benifiary_name = benifiary_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    public void setBeneficiaryAccount(String beneficiaryAccount) {
        this.beneficiaryAccount = beneficiaryAccount;
    }

    public String getBeneficiaryIFSC() {
        return beneficiaryIFSC;
    }

    public void setBeneficiaryIFSC(String beneficiaryIFSC) {
        this.beneficiaryIFSC = beneficiaryIFSC;
    }
}
