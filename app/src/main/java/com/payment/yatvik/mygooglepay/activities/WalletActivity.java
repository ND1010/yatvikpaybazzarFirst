package com.payment.yatvik.mygooglepay.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.payment.yatvik.mygooglepay.Adapter.TransactionHistoryAdapter;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.Helpers.User;
import com.payment.yatvik.mygooglepay.Model.TransactionHistoryModel;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<TransactionHistoryModel> astrologerModelList;
    TransactionHistoryAdapter transactionHistoryAdapter;
    Context context;

    SessionHandler session;
    String userId;
    String token;
    ImageButton backBtn,backBtn2;
    TextView pro_title,currentbal,creditbal,debitbal,memId;
    DatePickerDialog picker;
    String url,selectedItem;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.rec_trans_his);
        currentbal = findViewById(R.id.tv_wallet_bal);
        creditbal = findViewById(R.id.tv_credit_bal);
        debitbal = findViewById(R.id.tv_debit_bal);
        memId = findViewById(R.id.tv_mem_id);


        astrologerModelList = new ArrayList<>();

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        userId = user.getID();

        url = Constants.BASE_URL + "userWalletDetails/" + userId;
        //userId = "18";

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token  = preferences.getString("TOKEN",null);//second parameter default value.
        getTransactionsDetails(url);


        final List<String> transTypeList = new ArrayList<>();
        transTypeList.add(0, "All");
        transTypeList.add("Credit");
        transTypeList.add("Debit");

        backBtn = findViewById(R.id.backBtn);
        backBtn2 = findViewById(R.id.backBtn2);
        pro_title = findViewById(R.id.pro_title);

        pro_title.setText("Wallet History");
        getWalletDetails();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(WalletActivity.this,LoadingActivity.class));
            }
        });


        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.custom_filter_wallet, null);
                final Spinner transType = alertLayout.findViewById(R.id.spiTransType);
                final EditText fromdate = alertLayout.findViewById(R.id.et_fromdate);
                final EditText todate = alertLayout.findViewById(R.id.et_todater);
                final Button btn_filter = alertLayout.findViewById(R.id.btn_filter);
                final ImageView dialog_close = alertLayout.findViewById(R.id.dialog_close);

                final AlertDialog.Builder alert = new AlertDialog.Builder(WalletActivity.this);

                alert.setView(alertLayout);
                alert.setCancelable(true);
                final AlertDialog dialog = alert.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                fromdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        picker = new DatePickerDialog(WalletActivity.this,
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
                        picker = new DatePickerDialog(WalletActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        todate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(WalletActivity.this, android.R.layout.simple_list_item_1, transTypeList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                transType.setAdapter(arrayAdapter);

                transType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        selectedItem = parent.getItemAtPosition(position).toString();

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                btn_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String typeVal="";
                        int type = transType.getSelectedItemPosition();
                        if(selectedItem.equals("All")){
                            typeVal="";
                        }else if(selectedItem.equals("Credit")){
                            typeVal="0";
                        }else if(selectedItem.equals("Debit")){
                            typeVal="1";
                        }
                        String fromdateVal = fromdate.getText().toString();
                        String todaterVal = todate.getText().toString();

                        String param = url+"?type="+typeVal+"&from_date="+fromdateVal+"&to_date="+todaterVal;
                        Log.d("param-",param);
                        dialog.dismiss();
                        recyclerView.setAdapter(null);
                        astrologerModelList.clear();
                        transactionHistoryAdapter.notifyDataSetChanged();
                        getTransactionsDetails(param);

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

    private void getWalletDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "userWallet/" + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject dataobj = new JSONObject(response);

                            int status = dataobj.optInt("status");
                            String message = dataobj.optString("message");

                            if (status == 1) {

                                    int id = dataobj.optInt("id");
                                    String name = dataobj.optString("name");
                                    String loginId = dataobj.optString("loginId");
                                    double credit = dataobj.optDouble("credit");
                                    double debit = dataobj.optDouble("debit");
                                    String total_balance = dataobj.optString("total_balance");

                                    currentbal.setText("Rs "+total_balance);
                                    creditbal.setText("Rs "+credit);
                                    debitbal.setText("Rs "+debit);
                                    memId.setText(""+loginId);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(WalletActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(WalletActivity.this).addToRequestQueue(stringRequest);


    }


    private void getTransactionsDetails(String url) {
        displayLoader();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int status = jsonObject.optInt("status");
                            String message = jsonObject.optString("message");
                            String current_balance = jsonObject.optString("current_balance");

                            if (status == 1) {
                                JSONArray jsonArray = jsonObject.optJSONArray("transaction_details");
                                if(jsonArray.length()>=1) {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject dataobj = jsonArray.getJSONObject(i);

                                        int id = dataobj.optInt("id");
                                        int user_id = dataobj.optInt("user_id");
                                        String method = dataobj.optString("method");
                                        int type = dataobj.optInt("type");
                                        String created_at = dataobj.optString("created_at");
                                        Double amount = dataobj.optDouble("amount");
                                        String by_admin = dataobj.optString("by_admin");

                                        pDialog.dismiss();
                                        astrologerModelList.add(new TransactionHistoryModel(id, user_id, method, type, created_at, amount, by_admin));

                                    }
                                    initRecyclerView();
                                }
                                else {
                                    pDialog.dismiss();
                                    Toast.makeText(context, "Sorry No Such Result Found!!!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(WalletActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(WalletActivity.this).addToRequestQueue(stringRequest);


    }

    private void initRecyclerView() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(WalletActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        transactionHistoryAdapter = new TransactionHistoryAdapter(astrologerModelList);
        recyclerView.setAdapter(transactionHistoryAdapter);

    }


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(context,LoadingActivity.class));
    }


    private void displayLoader() {
        pDialog = new ProgressDialog(WalletActivity.this);
        pDialog.setMessage("Fetching your Wallet Details...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();
    }
}