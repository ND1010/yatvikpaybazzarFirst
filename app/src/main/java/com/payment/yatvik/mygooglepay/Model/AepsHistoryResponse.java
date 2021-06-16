package com.payment.yatvik.mygooglepay.Model;


import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AepsHistoryResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("apes_history")
    @Expose
    private ArrayList<ApesHistory> apesHistory = new ArrayList<>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<ApesHistory> getApesHistory() {
        return apesHistory;
    }

    public void setApesHistory(ArrayList<ApesHistory> apesHistory) {
        this.apesHistory = apesHistory;
    }


    public class User {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("email_verified_at")
        @Expose
        private Object emailVerifiedAt;
        @SerializedName("password_new")
        @Expose
        private String passwordNew;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("member_type")
        @Expose
        private String memberType;
        @SerializedName("package_type")
        @Expose
        private String packageType;
        @SerializedName("aadhar")
        @Expose
        private String aadhar;
        @SerializedName("pancard")
        @Expose
        private String pancard;
        @SerializedName("profilePic")
        @Expose
        private String profilePic;
        @SerializedName("shop_name")
        @Expose
        private String shopName;
        @SerializedName("shop_address")
        @Expose
        private String shopAddress;
        @SerializedName("gst_number")
        @Expose
        private Object gstNumber;
        @SerializedName("bpancard")
        @Expose
        private Object bpancard;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("post_code")
        @Expose
        private String postCode;
        @SerializedName("contact")
        @Expose
        private String contact;
        @SerializedName("landStd")
        @Expose
        private Object landStd;
        @SerializedName("landNumber")
        @Expose
        private Object landNumber;
        @SerializedName("owner_id")
        @Expose
        private String ownerId;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("loginId")
        @Expose
        private String loginId;
        @SerializedName("kyc_status")
        @Expose
        private String kycStatus;
        @SerializedName("panCardImage")
        @Expose
        private String panCardImage;
        @SerializedName("aadharFront")
        @Expose
        private String aadharFront;
        @SerializedName("aadharBack")
        @Expose
        private String aadharBack;
        @SerializedName("signature")
        @Expose
        private String signature;
        @SerializedName("account_no")
        @Expose
        private String accountNo;
        @SerializedName("ifsc_code")
        @Expose
        private String ifscCode;
        @SerializedName("payout_otp")
        @Expose
        private String payoutOtp;
        @SerializedName("ContactXpressID")
        @Expose
        private Object contactXpressID;
        @SerializedName("payout_status")
        @Expose
        private Integer payoutStatus;
        @SerializedName("payout_payment_otp")
        @Expose
        private String payoutPaymentOtp;
        @SerializedName("payment_id")
        @Expose
        private Object paymentId;
        @SerializedName("order_id")
        @Expose
        private Object orderId;
        @SerializedName("paid_amount")
        @Expose
        private Object paidAmount;
        @SerializedName("child_approve_status")
        @Expose
        private Integer childApproveStatus;
        @SerializedName("apes_kyc_status")
        @Expose
        private Integer apesKycStatus;
        @SerializedName("terminal_id")
        @Expose
        private Object terminalId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(Object emailVerifiedAt) {
            this.emailVerifiedAt = emailVerifiedAt;
        }

        public String getPasswordNew() {
            return passwordNew;
        }

        public void setPasswordNew(String passwordNew) {
            this.passwordNew = passwordNew;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }

        public String getPackageType() {
            return packageType;
        }

        public void setPackageType(String packageType) {
            this.packageType = packageType;
        }

        public String getAadhar() {
            return aadhar;
        }

        public void setAadhar(String aadhar) {
            this.aadhar = aadhar;
        }

        public String getPancard() {
            return pancard;
        }

        public void setPancard(String pancard) {
            this.pancard = pancard;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(String shopAddress) {
            this.shopAddress = shopAddress;
        }

        public Object getGstNumber() {
            return gstNumber;
        }

        public void setGstNumber(Object gstNumber) {
            this.gstNumber = gstNumber;
        }

        public Object getBpancard() {
            return bpancard;
        }

        public void setBpancard(Object bpancard) {
            this.bpancard = bpancard;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public Object getLandStd() {
            return landStd;
        }

        public void setLandStd(Object landStd) {
            this.landStd = landStd;
        }

        public Object getLandNumber() {
            return landNumber;
        }

        public void setLandNumber(Object landNumber) {
            this.landNumber = landNumber;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getKycStatus() {
            return kycStatus;
        }

        public void setKycStatus(String kycStatus) {
            this.kycStatus = kycStatus;
        }

        public String getPanCardImage() {
            return panCardImage;
        }

        public void setPanCardImage(String panCardImage) {
            this.panCardImage = panCardImage;
        }

        public String getAadharFront() {
            return aadharFront;
        }

        public void setAadharFront(String aadharFront) {
            this.aadharFront = aadharFront;
        }

        public String getAadharBack() {
            return aadharBack;
        }

        public void setAadharBack(String aadharBack) {
            this.aadharBack = aadharBack;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getIfscCode() {
            return ifscCode;
        }

        public void setIfscCode(String ifscCode) {
            this.ifscCode = ifscCode;
        }

        public String getPayoutOtp() {
            return payoutOtp;
        }

        public void setPayoutOtp(String payoutOtp) {
            this.payoutOtp = payoutOtp;
        }

        public Object getContactXpressID() {
            return contactXpressID;
        }

        public void setContactXpressID(Object contactXpressID) {
            this.contactXpressID = contactXpressID;
        }

        public Integer getPayoutStatus() {
            return payoutStatus;
        }

        public void setPayoutStatus(Integer payoutStatus) {
            this.payoutStatus = payoutStatus;
        }

        public String getPayoutPaymentOtp() {
            return payoutPaymentOtp;
        }

        public void setPayoutPaymentOtp(String payoutPaymentOtp) {
            this.payoutPaymentOtp = payoutPaymentOtp;
        }

        public Object getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(Object paymentId) {
            this.paymentId = paymentId;
        }

        public Object getOrderId() {
            return orderId;
        }

        public void setOrderId(Object orderId) {
            this.orderId = orderId;
        }

        public Object getPaidAmount() {
            return paidAmount;
        }

        public void setPaidAmount(Object paidAmount) {
            this.paidAmount = paidAmount;
        }

        public Integer getChildApproveStatus() {
            return childApproveStatus;
        }

        public void setChildApproveStatus(Integer childApproveStatus) {
            this.childApproveStatus = childApproveStatus;
        }

        public Integer getApesKycStatus() {
            return apesKycStatus;
        }

        public void setApesKycStatus(Integer apesKycStatus) {
            this.apesKycStatus = apesKycStatus;
        }

        public Object getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(Object terminalId) {
            this.terminalId = terminalId;
        }

    }

    public class ApesHistory {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("service")
        @Expose
        private String service;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("transType")
        @Expose
        private String transType;
        @SerializedName("aadhar")
        @Expose
        private String aadhar;
        @SerializedName("RRN")
        @Expose
        private String rrn;
        @SerializedName("UIDAIAuthenticationCode")
        @Expose
        private String uIDAIAuthenticationCode;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName(value = "ORDER_ID",alternate = "RetailerTxnId")
        @Expose
        private String orderId;
        @SerializedName(value = "txnStatus", alternate = "tra_status")
        @Expose
        private String txnStatus;
        @SerializedName("apiResponse")
        @Expose
        private String apiResponse;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("user")
        @Expose
        private User user;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTransType() {
            return transType;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public String getAadhar() {
            return aadhar;
        }

        public void setAadhar(String aadhar) {
            this.aadhar = aadhar;
        }

        public String getRrn() {
            return rrn;
        }

        public void setRrn(String rrn) {
            this.rrn = rrn;
        }

        public String getUIDAIAuthenticationCode() {
            return uIDAIAuthenticationCode;
        }

        public void setUIDAIAuthenticationCode(String uIDAIAuthenticationCode) {
            this.uIDAIAuthenticationCode = uIDAIAuthenticationCode;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getTxnStatus() {
            return txnStatus;
        }

        public void setTxnStatus(String txnStatus) {
            this.txnStatus = txnStatus;
        }

        public String getApiResponse() {
            return apiResponse;
        }

        public void setApiResponse(String apiResponse) {
            this.apiResponse = apiResponse;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

}
