package com.payment.yatvik.mygooglepay.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.payment.yatvik.mygooglepay.Adapter.AepsHistoyAdapter;
import com.payment.yatvik.mygooglepay.Adapter.BillsHistoyAdapter;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.Helpers.User;
import com.payment.yatvik.mygooglepay.Model.AepsHistoryResponse;
import com.payment.yatvik.mygooglepay.Model.BillHistoryResponse;
import com.payment.yatvik.mygooglepay.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillsHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<BillHistoryResponse.BillHistory> astrologerModelList;
    List<AepsHistoryResponse.ApesHistory> aepsHistoryList;
    BillsHistoyAdapter astrologerListAdapter;
    AepsHistoyAdapter aepsTransactionAdapter;
    Context context;

    SessionHandler session;
    String userId;
    String token;
    TextView totalmembers;
    ImageButton backBtn, backBtn2;
    CardView nav_left, nav_right, nav_search;
    EditText et_pageno;
    String aadharPayUrl, aepsHistoryUrl, BillHistoryUrl,depositAepsUrl;
    TextView pro_title;
    DatePickerDialog picker;
    private String type;

    private enum HistoryType {
        BILL_HISTORY,
        AEPS_TXN_HISTORY,
        AADHARPAY_TXN_HISTORY,
        DEPOSIT
    }

    private HistoryType historyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_history);

        context = BillsHistoryActivity.this;
        recyclerView = findViewById(R.id.frag_pro_rec);
        totalmembers = findViewById(R.id.tv_members_count);
        nav_left = findViewById(R.id.nav_left);
        nav_right = findViewById(R.id.nav_right);
        nav_search = findViewById(R.id.nav_search);
        et_pageno = findViewById(R.id.et_pageno);
        astrologerModelList = new ArrayList<>();
        aepsHistoryList = new ArrayList<>();

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        userId = user.getID();
        BillHistoryUrl = Constants.BASE_URL + "getBillHistory/" + userId;
        aepsHistoryUrl = Constants.BASE_URL + "getAllApesTransaction";
        aadharPayUrl = Constants.BASE_URL + "getAllAadharTransaction";
        depositAepsUrl = Constants.BASE_URL + "getAllDepositTransaction";
        //userId = "18";

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token = preferences.getString("TOKEN", null);//second parameter default value.

        backBtn = findViewById(R.id.backBtn);
        backBtn2 = findViewById(R.id.backBtn2);
        pro_title = findViewById(R.id.pro_title);

        getHistoryType();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.custom_filter_billhistory, null);
                final EditText orderId = alertLayout.findViewById(R.id.et_orderId);
                final EditText loginId = alertLayout.findViewById(R.id.et_loginId);
                final TextView tvOrderLbl = alertLayout.findViewById(R.id.tvOrderLbl);
                final TextView tvContactNum = alertLayout.findViewById(R.id.tvContactNum);
                final TextView tvStatusLbl = alertLayout.findViewById(R.id.tvStatusLbl);
                final EditText status = alertLayout.findViewById(R.id.et_status);
                final EditText mobile = alertLayout.findViewById(R.id.et_mobile);
                final EditText fromdate = alertLayout.findViewById(R.id.et_fromdate);
                final EditText todate = alertLayout.findViewById(R.id.et_todate);
                final Button btn_filter = alertLayout.findViewById(R.id.btn_filter);
                final ImageView dialog_close = alertLayout.findViewById(R.id.dialog_close);

                final AlertDialog.Builder alert = new AlertDialog.Builder(BillsHistoryActivity.this);

                alert.setView(alertLayout);
                alert.setCancelable(true);
                final AlertDialog dialog = alert.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                switch (type) {
                    case "Bill History":
                        break;
                    case "AEPS History":
                    case "Deposit History":
                    case "Aadhar Pay History":
                        loginId.setHint("Bank RRN");
                        status.setHint("Aadhaar No");
                        tvOrderLbl.setText("RRN No");
                        tvStatusLbl.setText("Aadhaar No");
                        tvContactNum.setVisibility(View.GONE);
                        mobile.setVisibility(View.GONE);
                        break;
                }

                dialog.show();

                fromdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(BillsHistoryActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        fromdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });


                todate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(BillsHistoryActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        todate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });

                btn_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (type) {
                            case "Bill History":
                                String orderIdIdVal = orderId.getText().toString();
                                String loginIdVal = loginId.getText().toString();
                                String statusVal = status.getText().toString();
                                String contactVal = mobile.getText().toString();
                                String fromdateVal = fromdate.getText().toString();
                                String todaterVal = todate.getText().toString();

                                String param = BillHistoryUrl + "?orderId=" + orderIdIdVal + "&loginId=" + loginIdVal + "&status=" + statusVal + "&contact=" + contactVal + "&from_date=" + fromdateVal + "&to_date=" + todaterVal;
                                Log.d("param-", param);
                                dialog.dismiss();
                                recyclerView.setAdapter(null);
                                astrologerModelList.clear();
                                astrologerListAdapter.notifyDataSetChanged();
                                getBillsHistory(param);
                                break;
                            case "AEPS History":
                                String orderIdVal = orderId.getText().toString();
                                String rrnVal = loginId.getText().toString();
                                String aadharVal = status.getText().toString();
                                String fromdateValAeps = fromdate.getText().toString();
                                String todaterValAeps = todate.getText().toString();

                                String paramaeps = aepsHistoryUrl + "?orderId=" + orderIdVal + "&aadhar=" + aadharVal + "&rrn=" + rrnVal + "&from_date=" + fromdateValAeps + "&to_date=" + todaterValAeps;
                                Log.d("param-aeps- ", paramaeps);
                                dialog.dismiss();
                                recyclerView.setAdapter(null);
                                aepsHistoryList.clear();
                                getAepsHistory(paramaeps);
                                break;
                            case "Deposit History":
                                String deporderIdVal = orderId.getText().toString();
                                String deprrnVal = loginId.getText().toString();
                                String depaadharVal = status.getText().toString();
                                String depfromdateValAeps = fromdate.getText().toString();
                                String deptodaterValAeps = todate.getText().toString();

                                String paramaepsD = depositAepsUrl + "?orderId=" + deporderIdVal + "&aadhar=" + depaadharVal + "&rrn=" + deprrnVal + "&from_date=" + depfromdateValAeps + "&to_date=" + deptodaterValAeps;
                                Log.d("param-deposit- ", paramaepsD);
                                dialog.dismiss();
                                recyclerView.setAdapter(null);
                                aepsHistoryList.clear();
                                getDepositTransaction(paramaepsD);
                                break;
                            case "Aadhar Pay History":
                                String txnId = orderId.getText().toString();
                                String rrn = loginId.getText().toString();
                                String aadhar = status.getText().toString();
                                String from = fromdate.getText().toString();
                                String todater = todate.getText().toString();

                                String paramAadhar = aadharPayUrl + "?txnID=" + txnId + "&aadhar=" + aadhar + "&rrn=" + rrn + "&from_date=" + from + "&to_date=" + todater;
                                Log.d("param-aadharpay ", paramAadhar);
                                dialog.dismiss();
                                recyclerView.setAdapter(null);
                                aepsHistoryList.clear();
                                getAadhaarpayHistory(paramAadhar);
                                break;
                        }
                    }
                });

                dialog_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


    }

    private void getHistoryType() {
        type = getIntent().getStringExtra(Constants.HISTORYTYPE);
        Log.e("TAG", "getHistoryType: " + historyType);
        switch (type) {
            case "Bill History":
                this.historyType = HistoryType.BILL_HISTORY;
                getBillsHistory(BillHistoryUrl);
                pro_title.setText("Bills History");
                break;
            case "AEPS History":
                this.historyType = HistoryType.AEPS_TXN_HISTORY;
                getAepsHistory(aepsHistoryUrl);
                pro_title.setText("AePS History");
                break;
            case "Aadhar Pay History":
                pro_title.setText("AadhaarPay History");
                this.historyType = HistoryType.AADHARPAY_TXN_HISTORY;
                getAadhaarpayHistory(aadharPayUrl);
                break;
            case "Deposit History":
                pro_title.setText("AePS Deposit History");
                this.historyType = HistoryType.DEPOSIT;
                getDepositTransaction(depositAepsUrl);
                break;
        }
    }

    private void getDepositTransaction(String aepsHistoryUrl) {
        if (!Constants.haveNetworkConnection(BillsHistoryActivity.this)) {
            Toast.makeText(this, "No internet connection found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Log.e("TAG", "getBillsHistory: " + aepsHistoryUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, aepsHistoryUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("TAG", "onResponse: " + jsonObject.toString(4));

                            if (jsonObject.has("status") && jsonObject.optInt("status") == 1) {
                                Log.e("TAG", "onResponse_deposit_history: " + jsonObject.toString(4));
                                AepsHistoryResponse aepsHistoryResponse = new Gson().fromJson(jsonObject.toString(), AepsHistoryResponse.class);
                                if (aepsHistoryResponse != null && !aepsHistoryResponse.getApesHistory().isEmpty()) {
                                    aepsHistoryList.addAll(aepsHistoryResponse.getApesHistory());
                                    initRecyclerView();
                                }
                            } else {
                                totalmembers.setText("Total Deposit " + 0 + " Found");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer " + token);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void getAepsHistory(String aepsHistoryUrl) {
        if (!Constants.haveNetworkConnection(BillsHistoryActivity.this)) {
            Toast.makeText(this, "No internet connection found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Log.e("TAG", "getBillsHistory: " + aepsHistoryUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, aepsHistoryUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("TAG", "onResponse: " + jsonObject.toString(4));

                            if (jsonObject.has("status") && jsonObject.optInt("status") == 1) {
                                Log.e("TAG", "onResponse_aeps_history: " + jsonObject.toString(4));
                                AepsHistoryResponse aepsHistoryResponse = new Gson().fromJson(jsonObject.toString(), AepsHistoryResponse.class);
                                if (aepsHistoryResponse != null && !aepsHistoryResponse.getApesHistory().isEmpty()) {
                                    aepsHistoryList.addAll(aepsHistoryResponse.getApesHistory());
                                    initRecyclerView();
                                }
                            } else {
                                totalmembers.setText("Total AePS " + 0 + " Found");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer " + token);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void getBillsHistory(String url) {
        if (!Constants.haveNetworkConnection(BillsHistoryActivity.this)) {
            Toast.makeText(this, "No internet connection found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Log.e("TAG", "getBillsHistory: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("status") && jsonObject.optInt("status") == 1) {
                                Log.e("TAG", "onResponse: " + jsonObject.toString(4));
                                BillHistoryResponse billHistoryResponse = new Gson().fromJson(jsonObject.toString(), BillHistoryResponse.class);
                                if (billHistoryResponse != null && !billHistoryResponse.getBillHistory().isEmpty()) {
                                    astrologerModelList.addAll(billHistoryResponse.getBillHistory());
                                    initRecyclerView();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer " + token);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /*Aadhar pay AEPS History api*/
    private void getAadhaarpayHistory(String aadharpayUrl) {
        if (!Constants.haveNetworkConnection(BillsHistoryActivity.this)) {
            Toast.makeText(this, "No internet connection found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Log.e("TAG", "getAadhaarHistiry: " + aadharpayUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, aadharpayUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("TAG", "onResponse: " + jsonObject.toString(4));

                            if (jsonObject.has("status") && jsonObject.optInt("status") == 1) {
                                Log.e("TAG", "onResponse_aadhaar_history: " + jsonObject.toString(4));
                                AepsHistoryResponse aepsHistoryResponse = new Gson().fromJson(jsonObject.toString(), AepsHistoryResponse.class);
                                if (aepsHistoryResponse != null && !aepsHistoryResponse.getApesHistory().isEmpty()) {
                                    aepsHistoryList.addAll(aepsHistoryResponse.getApesHistory());

                                    initRecyclerView();
                                }
                            } else {
                                totalmembers.setText("Total AadharPay " + 0 + " Found");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer " + token);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        type = getIntent().getStringExtra(Constants.HISTORYTYPE);
        switch (type) {
            case "Bill History":
                astrologerListAdapter = new BillsHistoyAdapter(astrologerModelList);
                recyclerView.setAdapter(astrologerListAdapter);
                totalmembers.setText("Total " + astrologerModelList.size() + " Bills Found");
                break;
            case "AEPS History":
                aepsTransactionAdapter = new AepsHistoyAdapter(aepsHistoryList);
                recyclerView.setAdapter(aepsTransactionAdapter);
                totalmembers.setText("Total AePS " + aepsHistoryList.size() + " Found");
                break;
            case "Aadhar Pay History":
                aepsTransactionAdapter = new AepsHistoyAdapter(aepsHistoryList);
                recyclerView.setAdapter(aepsTransactionAdapter);
                totalmembers.setText("Total AadhaarPay " + aepsHistoryList.size() + " Found");
                break;
            case "Deposit History":
                aepsTransactionAdapter = new AepsHistoyAdapter(aepsHistoryList);
                recyclerView.setAdapter(aepsTransactionAdapter);
                totalmembers.setText("Total Deposit " + aepsHistoryList.size() + " Found");
                break;
        }
    }

}
