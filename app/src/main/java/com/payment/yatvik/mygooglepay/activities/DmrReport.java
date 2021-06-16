package com.payment.yatvik.mygooglepay.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import com.payment.yatvik.mygooglepay.Adapter.DmrReportAdapter;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.Helpers.User;
import com.payment.yatvik.mygooglepay.Model.DmrReportModel;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DmrReport extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DmrReportModel> astrologerModelList;
    DmrReportAdapter astrologerListAdapter;
    Context context;

    SessionHandler session;
    String userId;
    String token;
    ImageButton backBtn,backBtn2;
    TextView pro_title,count;
    CardView nav_left,nav_right,nav_search;
    EditText et_pageno;
    String prev_page_url,next_page_url,url;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmr_report);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.frag_payout_rec);
        count = findViewById(R.id.tv_payout_count);
        nav_left = findViewById(R.id.nav_left);
        nav_right = findViewById(R.id.nav_right);
        nav_search = findViewById(R.id.nav_search);
        et_pageno = findViewById(R.id.et_pageno);
        astrologerModelList = new ArrayList<>();

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        userId = user.getID();
        //userId = "18";

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token  = preferences.getString("TOKEN",null);//second parameter default value.
        url = Constants.BASE_URL + "getAllDmrTransaction";
        getProductInventory(url);

        backBtn = findViewById(R.id.backBtn);
        backBtn2 = findViewById(R.id.backBtn2);
        pro_title = findViewById(R.id.pro_title);

        pro_title.setText("Dmr Report");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(context,LoadingActivity.class));
            }
        });

        nav_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(null);
                astrologerModelList.clear();
                astrologerListAdapter.notifyDataSetChanged();
                getProductInventory(prev_page_url);
            }
        });

        nav_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(null);
                astrologerModelList.clear();
                astrologerListAdapter.notifyDataSetChanged();
                getProductInventory(next_page_url);

            }
        });

        nav_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(null);
                astrologerModelList.clear();
                astrologerListAdapter.notifyDataSetChanged();
                getProductInventory(url+"?page="+et_pageno.getText().toString());
            }
        });


        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.custom_filter_dmr, null);
                final EditText benename = alertLayout.findViewById(R.id.et_benename);
                final EditText fromdate = alertLayout.findViewById(R.id.et_fromdate);
                final EditText todate = alertLayout.findViewById(R.id.et_todater);
                final Button btn_filter = alertLayout.findViewById(R.id.btn_filter);
                final ImageView dialog_close = alertLayout.findViewById(R.id.dialog_close);

                final AlertDialog.Builder alert = new AlertDialog.Builder(DmrReport.this);

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
                        picker = new DatePickerDialog(DmrReport.this,
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
                        picker = new DatePickerDialog(DmrReport.this,
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

                        String benenameVal = benename.getText().toString();
                        String fromdateVal = fromdate.getText().toString();
                        String todaterVal = todate.getText().toString();

                        String param = url+"?bname="+benenameVal+"&from_date="+fromdateVal+"&to_date="+todaterVal;
                        Log.d("param-",param);
                        dialog.dismiss();
                        recyclerView.setAdapter(null);
                        astrologerModelList.clear();
                        astrologerListAdapter.notifyDataSetChanged();
                        getProductInventory(param);

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

    private void getProductInventory(String url) {
        if(url.length()>=1) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                int current_page = jsonObject.optInt("current_page");
                                prev_page_url = jsonObject.optString("prev_page_url");
                                next_page_url = jsonObject.optString("next_page_url");
                                int total = jsonObject.optInt("total");
                                count.setText("Total " + total + " Dmr Report Found");
                                et_pageno.setText("" + current_page);

                                JSONArray jsonArray = jsonObject.optJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject dataobj = jsonArray.getJSONObject(i);

                                    String Amount = (dataobj.optString("amount"));
                                    String status = (dataobj.optString("status"));
                                    String sendername = (dataobj.optString("custname"));
                                    String OrderId = dataobj.optString("order_id");
                                    String benename = (dataobj.optString("ben_name"));
                                    String ifsc = (dataobj.optString("bank"));
                                    String AccountNo = (dataobj.optString("account"));

                                    astrologerModelList.add(new DmrReportModel(Amount, status, sendername, OrderId, benename, ifsc, AccountNo));

                                }
                                initRecyclerView();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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
        else {
            Toast.makeText(context, "No Further Record Found!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        astrologerListAdapter = new DmrReportAdapter(astrologerModelList);
        recyclerView.setAdapter(astrologerListAdapter);

        //count.setText("Total " + astrologerModelList.size() + " Dmr Report Found");
    }


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(context,LoadingActivity.class));
    }


}