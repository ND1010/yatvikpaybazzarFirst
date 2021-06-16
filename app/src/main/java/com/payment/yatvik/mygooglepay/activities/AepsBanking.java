package com.payment.yatvik.mygooglepay.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.easypay.epmoney.epmoneylib.request_model.finger_model.uid.PidData;
import com.easypay.epmoney.epmoneylib.request_model.finger_model.uid.PidOptions;
import com.payment.yatvik.mygooglepay.DialogAlert;
import com.payment.yatvik.mygooglepay.DialogAlertSuccess;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Model.BankResponse;
import com.payment.yatvik.mygooglepay.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_spinner_item;

public class AepsBanking extends AppCompatActivity {

    private static final String TAG = "AepsBanking";
    private Spinner spinner_finger_device;
    private Spinner spi_Bank;
    private EditText et_mobile;
    private EditText et_aadhaar;
    private EditText et_account_no;
    private EditText et_name;
    private EditText et_amount;
    private Button btn_submit;
    private TextView tv_payfor_title;
    private TextView tvBankAccountNo;
    private TextView tvLblAmount;
    private TextView tv_check_status;
    List<BankResponse> bankDetailModelList;
    private ArrayAdapter<BankResponse> spinnerArrayAdapter;
    private BankResponse selectedBank = null;
    private String serviceFor = "";
    private String fingerData = "";
    private PidData pidData = null;
    private Long mLastClickTime = 0L;
    private Serializer serializer = null;
    private String devicePackageName = "com.mantra.rdservice";
    private String deviceSelected = "MANTRA";
    private final static Long MIN_CLICK_INTERVAL = 1000L;
    private final static int FINGERSCAN_CODE = 1010;
    private String token = "";
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps_banking);
        initView();
        loadBankDetails();
    }

    private void loadBankDetails() {
        bankDetailModelList = new ArrayList<>();
        spinnerArrayAdapter = new ArrayAdapter<BankResponse>(AepsBanking.this, simple_spinner_item, bankDetailModelList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_Bank.setAdapter(spinnerArrayAdapter);
        getBankListApi();
        spi_Bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos != 0) {
                    selectedBank = spinnerArrayAdapter.getItem(pos);
                } else {
                    selectedBank = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getBankListApi() {
        if (!Constants.haveNetworkConnection(AepsBanking.this)) {
            Toast.makeText(this, "No internet connection found!", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "getBanks",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response_bank----> ", response);
                        progressDialog.dismiss();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<BankResponse>>() {
                            }.getType();
                            bankDetailModelList.clear();
                            BankResponse blankBank = new BankResponse();
                            blankBank.setName("Select Bank");
                            bankDetailModelList.add(blankBank);
                            bankDetailModelList.addAll(gson.fromJson(response, type));
                            spinnerArrayAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                dialogAlert.showDialog(error.getMessage(), "OK");
                dialogAlert.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
//                headers.put("authorization", "Bearer "+oldtoken);
                return headers;
            }
        };

        MySingleton.getInstance(AepsBanking.this).addToRequestQueue(stringRequest);
    }

    private void initView() {
        serializer = new Persister();
        spinner_finger_device = findViewById(R.id.spinner_finger_device);
        spi_Bank = findViewById(R.id.spi_Bank);
        et_aadhaar = findViewById(R.id.et_aadhaar);
        et_account_no = findViewById(R.id.et_account_no);
        et_name = findViewById(R.id.et_name);
        et_amount = findViewById(R.id.et_amount);
        tv_payfor_title = findViewById(R.id.tv_payfor_title);
        tv_check_status = findViewById(R.id.tv_check_status);
        et_mobile = findViewById(R.id.et_mobile);
        tv_payfor_title = findViewById(R.id.tv_payfor_title);
        tvBankAccountNo = findViewById(R.id.tvBankAccountNo);
        btn_submit = findViewById(R.id.btn_submit);
        tvLblAmount = findViewById(R.id.tvLblAmount);
        backBtn = findViewById(R.id.backBtn);

        getServiceFor();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token = preferences.getString("TOKEN", null);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long currentClickTime = SystemClock.uptimeMillis();
                long elapsedTime = currentClickTime - mLastClickTime;
                mLastClickTime = currentClickTime;
                if (elapsedTime <= MIN_CLICK_INTERVAL) {
                    return;
                }
                if (!Constants.isAppInstalled(AepsBanking.this, devicePackageName)) {
                    return;
                }
                if (selectedBank == null) {
                    tv_check_status.setText("Please select the bank.");
                    return;
                }

                if (serviceFor.equals("Cash Deposit")) {
                    if (et_account_no.getText().toString().trim().isEmpty()) {
                        tv_check_status.setText("Please enter your deposit bank account number");
                    } else if (et_account_no.getText().toString().trim().length() < 9) {
                        tv_check_status.setText("invalid bank account number");
                    }
                }


                if (et_aadhaar.getText().toString().trim().isEmpty()) {
                    tv_check_status.setText("Please enter Aadhaar Number.");
                    return;
                }
                if (et_aadhaar.getText().toString().trim().length() != 12) {
                    tv_check_status.setText("Invalid Aadhaar Number.");
                    return;
                }

                if (et_mobile.getText().toString().trim().isEmpty()) {
                    tv_check_status.setText("Please enter mobile number.");
                    return;
                }
                if (et_mobile.getText().toString().trim().length() != 10) {
                    tv_check_status.setText("Invalid Mobile number.");
                    return;
                }

                if (et_name.getText().toString().trim().isEmpty()) {
                    tv_check_status.setText("Please enter customer name.");
                    return;
                }
                if (et_name.getText().toString().trim().length() < 3) {
                    tv_check_status.setText("Invalid Customer name(should be >= 3 characters).");
                    return;
                }

                if (serviceFor.equals("Cash Deposit")
                        || serviceFor.equals("Cash Withdrawal") || serviceFor.equals("Aadhar Pay")) {
                    if (et_amount.getText().toString().trim().isEmpty()) {
                        tv_check_status.setText("Please enter amount.");
                        return;
                    }
                    if (Double.parseDouble(et_amount.getText().toString().trim()) < 100.0) {
                        tv_check_status.setText("Amount should be more then 100.0");
                        return;
                    }
                }
                tv_check_status.setText("");
                scanFingerprint();
            }
        });

        spinner_finger_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        devicePackageName = "com.mantra.rdservice";
                        deviceSelected = "MANTRA";
                        break;
                    case 1:
                        devicePackageName = "com.scl.rdservice";
                        deviceSelected = "MORPHO";
                        break;
                    case 2:
                        devicePackageName = "com.tatvik.bio.tmf20";
                        deviceSelected = "TATVIK";
                        break;
                    case 3:
                        devicePackageName = "com.acpl.registersdk";
                        deviceSelected = "STARTEK";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getServiceFor() {
        serviceFor = getIntent().getStringExtra(Constants.AEPS_SERVICE_FOR);
        switch (serviceFor) {
            case "Balance Inquiry":
                tv_payfor_title.setText("AEPS for balance inquiry");
                tvLblAmount.setVisibility(View.GONE);
                et_amount.setVisibility(View.GONE);
                break;
            case "Mini Statement":
                tv_payfor_title.setText("AEPS for Mini Statement");
                tvLblAmount.setVisibility(View.GONE);
                et_amount.setVisibility(View.GONE);
                break;
            case "Aadhar Pay":
                tv_payfor_title.setText("AEPS for Aadhar Pay");
                break;
            case "Cash Withdrawal":
                tv_payfor_title.setText("AEPS for Cash Withdrawal");
                break;
            case "Cash Deposit":
                tv_payfor_title.setText("AEPS for Cash Deposit");
                et_account_no.setVisibility(View.VISIBLE);
                tvBankAccountNo.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void scanFingerprint() {
        try {
            String pidDataProtoBuff = "0";
            if (serviceFor.equals("Mini Statement")
                    || serviceFor.equals("Aadhar Pay")
                    || serviceFor.equals("Cash Deposit")) {
                pidDataProtoBuff = "1";
            } else {
                pidDataProtoBuff = "0";
            }
            Log.e(TAG, "pidDataProtoBuff:" + pidDataProtoBuff);
            String pidOption = PidOptions.getPIDOptions(pidDataProtoBuff); //if (finpayICICIAEPS) "0" else "1"
            if (pidOption != null) {
                Log.e("PidOptions", pidOption);
                Intent intent2 = new Intent();
                intent2.setPackage(devicePackageName);
                intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                intent2.putExtra("PID_OPTIONS", pidOption);
                startActivityForResult(intent2, FINGERSCAN_CODE);
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }


    private void getFingerData(String data) {
        if (isValidResponse(data)) {
            fingerData = data;
            if (serviceFor.equals("Mini Statement")) {
                //call api for mini statement
                apiForMiniStatementIcici();
            } else if (serviceFor.equals("Cash Deposit")) {
                apiForCashDeposit();
            } else {
                //Call api for transactions
                apiForAepsTransaction();
            }

        }
    }

    private void apiForCashDeposit() {
        if (!Constants.haveNetworkConnection(AepsBanking.this)) {
            Toast.makeText(this, "No internet connection found!", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cash Deposit in Processing...");
        progressDialog.show();

        JSONObject request = new JSONObject();
        try {
            request.put("name", et_name.getText().toString().trim());
            request.put("contact", et_mobile.getText().toString().trim());
            request.put("aadhar", et_aadhaar.getText().toString().trim());
            request.put("radio_IIN", selectedBank.getIin());
            request.put("depositAccount", et_account_no.getText().toString().trim());
            request.put("amount", et_amount.getText().toString().trim());
            request.put("pidData", fingerData);
            request.put("IIN", selectedBank.getIin());
            request.put("OP", "AEPS");
            request.put("transType", "MINISTATEMENT");

            Log.e(TAG, "aepsTransaction_request_account_deposit-------->" + request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL + "storeDepositTransaction", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Aeps Response_deposit----> " + response.toString());
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.e(TAG, "onResponse: " + jsonObject.toString(4));
                            if (jsonObject.has("status") && jsonObject.getString("status").equals("1")) {
                                //Success
                                DialogAlertSuccess dialogAlert = new DialogAlertSuccess(AepsBanking.this);
                                dialogAlert.showDialog("Cash Deposit has been successful.", "OK");
                                dialogAlert.show();

                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dialogAlert != null && dialogAlert.isShowing()) {
                                            dialogAlert.dismiss();
                                        }
                                        /*startActivity(new Intent(AepsBanking.this, MiniStatementActivity.class)
                                                .putExtra("transaction", jsonObject.toString())
                                                .putExtra("service_for", serviceFor));*/
                                    }
                                }, 2000);

                            } else if (jsonObject.has("status") && jsonObject.getString("status").equals("0")) {
                                //Failed
                                String errorMsg = "Transaction has has been failed.";
                                if (jsonObject.has("message") && !jsonObject.getString("message").isEmpty()) {
                                    errorMsg = jsonObject.getString("message");
                                }
                                DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                                dialogAlert.showDialog(errorMsg, "OK");
                                dialogAlert.show();
                                //tv_check_status.setText(errorMsg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error.networkResponse.data != null) {
                            String errorJson = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            Log.e(TAG, "onErrorResponse: " + errorJson);
                            try {
                                JSONObject jsonObject = new JSONObject(errorJson);
                                if (jsonObject.has("status") && jsonObject.getString("status").equals("0")) {
                                    String errorMsg = "Transaction has has been failed.";
                                    if (jsonObject.has("message") && !jsonObject.getString("message").isEmpty()) {
                                        errorMsg = jsonObject.getString("message");
                                    }
                                    DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                                    dialogAlert.showDialog(errorMsg, "OK");
                                    dialogAlert.show();
                                    //tv_check_status.setText(errorMsg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                                dialogAlert.showDialog(getString(R.string.some_error), "OK");
                                dialogAlert.show();
                            }
                        } else {
                            DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                            dialogAlert.showDialog("Some error from server side, Please try again", "OK");
                            dialogAlert.show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer " + token);
                return headers;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    private void apiForAepsTransaction() {
        if (!Constants.haveNetworkConnection(AepsBanking.this)) {
            Toast.makeText(this, "No internet connection found!", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        JSONObject request = new JSONObject();
        try {
            request.put("name", et_name.getText().toString().trim());
            request.put("contact", et_mobile.getText().toString().trim());
            request.put("aadhar", et_aadhaar.getText().toString().trim());
            request.put("radio_IIN", selectedBank.getIin());
            request.put("pidData", fingerData);
            request.put("IIN", selectedBank.getIin());
            request.put("OP", "ICICIAEPS");

            switch (serviceFor) {
                case "Balance Inquiry":
                    request.put("amount", "0");
                    request.put("transType", "balanceinfo");
                    break;
                case "Aadhar Pay":
                    request.put("amount", et_amount.getText().toString().trim());
                    request.put("transType", "aadharpay");
                    break;
                case "Cash Withdrawal":
                    request.put("amount", et_amount.getText().toString().trim());
                    request.put("transType", "accountwithdrawal");
                    break;
            }
            Log.e(TAG, "aepsTransaction_request-------->" + request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String postMethodUrl = Constants.BASE_URL + "storeAepsTransaction";
        if (serviceFor.equals("Aadhar Pay")) {
            postMethodUrl = Constants.BASE_URL + "storeAadharTransaction";
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, postMethodUrl, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Aeps Response----> " + response.toString());
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.e(TAG, "onResponse: " + jsonObject.toString(4));
                            if (jsonObject.has("status") && jsonObject.getString("status").equals("1")) {
                                //Success
                                //Success
                                DialogAlertSuccess dialogAlert = new DialogAlertSuccess(AepsBanking.this);
                                dialogAlert.showDialog("AePS Transaction successful", "OK");
                                dialogAlert.show();

                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dialogAlert != null && dialogAlert.isShowing()) {
                                            dialogAlert.dismiss();
                                        }
                                        startActivity(new Intent(AepsBanking.this, AepsTransactionRecieptActivity.class)
                                                .putExtra("transaction", jsonObject.toString())
                                                .putExtra("service_for", serviceFor));
                                    }
                                }, 2000);

                            } else if (jsonObject.has("status") && jsonObject.getString("status").equals("0")) {
                                //Failed
                                String errorMsg = "Transaction has has been failed.";
                                if (jsonObject.has("message") && !jsonObject.getString("message").isEmpty()) {
                                    errorMsg = jsonObject.getString("message");
                                }
                                DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                                dialogAlert.showDialog(errorMsg, "Ok");
                                dialogAlert.show();
                                //tv_check_status.setText(errorMsg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error.networkResponse.data != null) {
                            String errorJson = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            Log.e(TAG, "onErrorResponse: " + errorJson);
                            try {
                                JSONObject jsonObject = new JSONObject(errorJson);
                                if (jsonObject.has("status") && jsonObject.getString("status").equals("0")) {
                                    String errorMsg = "Transaction has has been failed.";
                                    if (jsonObject.has("message") && !jsonObject.getString("message").isEmpty()) {
                                        errorMsg = jsonObject.getString("message");
                                    }
                                    DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                                    dialogAlert.showDialog(errorMsg, "Ok");
                                    dialogAlert.show();
//                                    tv_check_status.setText(errorMsg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(AepsBanking.this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
                                //tv_check_status.setText("Some error from server side, Please try again");
                                DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                                dialogAlert.showDialog(getString(R.string.some_error), "Ok");
                                dialogAlert.show();
                            }
                        } else {
                            DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                            dialogAlert.showDialog(getString(R.string.some_error), "Ok");
                            dialogAlert.show();
                            //tv_check_status.setText("Some error from server side, Please try again");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer " + token);
                return headers;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    private void apiForMiniStatementIcici() {
        if (!Constants.haveNetworkConnection(AepsBanking.this)) {
            Toast.makeText(this, "No internet connection found!", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mini statement in Processing...");
        progressDialog.show();


        JSONObject request = new JSONObject();
        try {

            request.put("name", et_name.getText().toString().trim());
            request.put("contact", et_mobile.getText().toString().trim());
            request.put("aadhar", et_aadhaar.getText().toString().trim());
            request.put("radio_IIN", selectedBank.getIin());
            request.put("amount", "0");
            request.put("pidData", fingerData);
            request.put("IIN", selectedBank.getIin());
            request.put("OP", "AEPS");
            request.put("transType", "ministatment");

            Log.e(TAG, "aepsTransaction_request-------->" + request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL + "storeAepsMiniTransaction", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Aeps Response----> " + response.toString());
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.e(TAG, "onResponse: " + jsonObject.toString(4));
                            if (jsonObject.has("status") && jsonObject.getString("status").equals("1")) {
                                //Success
                                DialogAlertSuccess dialogAlert = new DialogAlertSuccess(AepsBanking.this);
                                dialogAlert.showDialog("Mini statement successful", "OK");
                                dialogAlert.show();

                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dialogAlert != null && dialogAlert.isShowing()) {
                                            dialogAlert.dismiss();
                                        }
                                        startActivity(new Intent(AepsBanking.this, MiniStatementActivity.class)
                                                .putExtra("transaction", jsonObject.toString())
                                                .putExtra("service_for", serviceFor));
                                    }
                                }, 2000);

                            } else if (jsonObject.has("status") && jsonObject.getString("status").equals("0")) {
                                //Failed
                                String errorMsg = "Transaction has has been failed.";
                                if (jsonObject.has("message") && !jsonObject.getString("message").isEmpty()) {
                                    errorMsg = jsonObject.getString("message");
                                }
                                DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                                dialogAlert.showDialog(errorMsg, "OK");
                                dialogAlert.show();
                                //tv_check_status.setText(errorMsg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error.networkResponse.data != null) {
                            String errorJson = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            Log.e(TAG, "onErrorResponse: " + errorJson);
                            try {
                                JSONObject jsonObject = new JSONObject(errorJson);
                                if (jsonObject.has("status") && jsonObject.getString("status").equals("0")) {
                                    String errorMsg = "Transaction has has been failed.";
                                    if (jsonObject.has("message") && !jsonObject.getString("message").isEmpty()) {
                                        errorMsg = jsonObject.getString("message");
                                    }
                                    DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                                    dialogAlert.showDialog(errorMsg, "OK");
                                    dialogAlert.show();
                                    //tv_check_status.setText(errorMsg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
//                                Toast.makeText(AepsBanking.this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
                                DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                                dialogAlert.showDialog(getString(R.string.some_error), "OK");
                                dialogAlert.show();
                                //tv_check_status.setText("Some error from server side, Please try again");
                            }
                        } else {
                            DialogAlert dialogAlert = new DialogAlert(AepsBanking.this);
                            dialogAlert.showDialog("Some error from server side, Please try again", "OK");
                            dialogAlert.show();
                            //tv_check_status.setText("Some error from server side, Please try again");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer " + token);
                return headers;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    private boolean isValidResponse(String data) {
        try {
            JSONObject objJson = XML.toJSONObject(data);
            JSONObject pidJson = objJson.optJSONObject("PidData");
            if (pidJson != null) {
                JSONObject respJson = pidJson.optJSONObject("Resp");
                if (respJson != null) {
                    int code = respJson.optInt("errCode");
                    if (code > 0) {
                        Toast.makeText(this, respJson.get("errInfo").toString(), Toast.LENGTH_SHORT).show();
                        //Utils.showAlert(this@EKYCActivity, respJson.get("errInfo").toString())
                        return false;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == FINGERSCAN_CODE) {
                if (data != null) {
                    String result = data.getStringExtra("PID_DATA");
                    if (result != null) {
                        try {
                            pidData = serializer.read(PidData.class, result);
                            getFingerData(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Unable to capture.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        }
    }
}