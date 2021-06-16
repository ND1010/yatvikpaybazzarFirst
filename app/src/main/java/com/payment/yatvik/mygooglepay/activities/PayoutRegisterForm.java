package com.payment.yatvik.mygooglepay.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.Helpers.User;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PayoutRegisterForm extends AppCompatActivity {

    EditText et_fname,et_lname,et_email,et_mobile,et_gst,et_account,et_ifsc,et_shopname,et_shopaddress,et_amount_val;
    TextView tv_acc_no,tv_ifsc,tv_member_id,tv_mobile,tv_member_name,tv_credit,tv_debit,tv_total,amount_condition,tv_check_status;
    Button btn_submit,btn_submit2;
    ImageButton backBtn;
    Context context;
    SessionHandler session;
    String token;
    String userId;
    AlertDialog.Builder alert;
    String YouEditTextValue;
    Spinner city_spinner;
    int otp;
    String resamount,payment_type,orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_register_form);

        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_gst = findViewById(R.id.et_gst);
        et_account = findViewById(R.id.et_account);
        et_ifsc = findViewById(R.id.et_ifsc);
        et_shopname = findViewById(R.id.et_shopname);
        et_shopaddress = findViewById(R.id.et_shopaddress);
        btn_submit = findViewById(R.id.btn_submit);
        backBtn = findViewById(R.id.backBtn);

        context = getApplicationContext();
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        //userId = user.getID();
        userId = "18";

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token  = preferences.getString("TOKEN",null);

        alert = new AlertDialog.Builder(this);


        getPayoutStatus();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKyc();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(context,LoadingActivity.class));
            }
        });
    }

    private void getPayoutStatus() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "getPayoutStatus/" + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("payoutres",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int status = jsonObject.optInt("status");
                            String message = jsonObject.optString("message");
                            double debit = jsonObject.optDouble("debit");
                            double credit = jsonObject.optDouble("credit");
                            double total_balance = jsonObject.optDouble("total_balance");


                            if (status == 1) {
                                JSONArray jsonArray = jsonObject.optJSONArray("user_details");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject dataobj = jsonArray.getJSONObject(i);


                                    String loginId = dataobj.optString("loginId");
                                    String fname = dataobj.optString("name");
                                    String lname = dataobj.optString("last_name");
                                    int payout_status = dataobj.optInt("payout_status");
                                    String email = dataobj.optString("email");
                                    String contact = dataobj.optString("contact");
                                    String ifsc_code = dataobj.optString("ifsc_code");
                                    String shop_name = dataobj.optString("shop_name");
                                    String shop_address = dataobj.optString("shop_address");
                                    String gst_number = dataobj.optString("gst_number");
                                    String account_no = dataobj.optString("account_no");

                                    if(payout_status==0){
                                        //setContentView(R.layout.activity_payout_register_form);
                                        et_fname.setText(fname);
                                        et_lname.setText(lname);
                                        et_email.setText(email);
                                        et_mobile.setText(contact);
                                        et_gst.setText(gst_number);
                                        et_account.setText(account_no);
                                        et_ifsc.setText(ifsc_code);
                                        et_shopname.setText(shop_name);
                                        et_shopaddress.setText(shop_address);

                                    }
                                    else if(payout_status==1){
                                        setContentView(R.layout.activity_payout_detail_form);

                                        final String[] city_list = new String[4];

                                        city_list[0] = "Choose Type";
                                        city_list[1] = "IMPS";
                                        city_list[2] = "NEFT";


                                        tv_acc_no = findViewById(R.id.tv_acc_no);
                                        tv_ifsc = findViewById(R.id.tv_ifsc);
                                        tv_member_id = findViewById(R.id.tv_member_id);
                                        tv_mobile = findViewById(R.id.tv_mobile);
                                        tv_member_name = findViewById(R.id.tv_member_name);
                                        tv_credit = findViewById(R.id.tv_credit_bal);
                                        tv_debit = findViewById(R.id.tv_debit_bal);
                                        tv_total = findViewById(R.id.tv_total_bal);
                                        amount_condition = findViewById(R.id.amount_condition);
                                        btn_submit2 = findViewById(R.id.btn_submit2);
                                        et_amount_val = findViewById(R.id.et_amount_val);
                                        city_spinner = findViewById(R.id.spiPaymentType);
                                        tv_check_status = findViewById(R.id.tv_check_status);
                                        backBtn = findViewById(R.id.backBtn2);

                                        tv_acc_no.setText(account_no);
                                        tv_ifsc.setText(ifsc_code);
                                        tv_member_id.setText(loginId);
                                        tv_mobile.setText(contact);
                                        tv_member_name.setText(fname+" "+lname);
                                        tv_credit.setText(Double.toString(credit));
                                        tv_debit.setText(Double.toString(debit));
                                        tv_total.setText(Double.toString(total_balance));
                                        amount_condition.setText("Amount should be less than the Total Amount which is "+total_balance);


                                        List<String> footballPlayers = new ArrayList<>();
                                        footballPlayers.add(0, "Choose Type");
                                        footballPlayers.add("IMPS");
                                        footballPlayers.add("NEFT");

                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, footballPlayers);
                                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        city_spinner.setAdapter(arrayAdapter);

//
                                        backBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finish();
                                                startActivity(new Intent(context,LoadingActivity.class));
                                            }
                                        });

                                        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                if (parent.getItemAtPosition(position).equals("Choose Type")){
                                                    Toast.makeText(parent.getContext(),"Choose Payment Type", Toast.LENGTH_SHORT).show();

                                                }
                                                else {
                                                    String item = parent.getItemAtPosition(position).toString();
                                                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                            }
                                        });

                                        et_amount_val.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                // TODO Auto-generated method stub
                                                if(count>=1){
                                                    checkBalance(s);
                                                }else{
                                                    tv_check_status.setVisibility(View.INVISIBLE);
                                                }
                                            }

                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                // TODO Auto-generated method stub
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {

                                                // TODO Auto-generated method stub
                                            }
                                        });


                                        btn_submit2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // check api for wallet amount when clicked this button then proceed
                                                //checkBalance(et_amount_val.getText().toString());
                                                sendPayoutOtp(et_amount_val.getText().toString(),city_spinner.getSelectedItem().toString());
                                            }
                                        });

                                    }
                                }
                            }
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
                headers.put("Authorization", "bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void checkBalance(CharSequence amounval) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "calculateWalletBalance/"+amounval,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("resendotpres",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //int status = jsonObject.optInt("status");
                            String message = jsonObject.optString("message");
                            String amount = jsonObject.optString("amount");
                            String surcharge = jsonObject.optString("surcharge");
                            String wallet = jsonObject.optString("wallet");
                            String remaining = jsonObject.optString("remaining");

                            Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            tv_check_status.setVisibility(View.VISIBLE);
                            tv_check_status.setText("Amount: "+amount +",Surcharge: "+surcharge+",Wallet: "+wallet+",Remaining: "+remaining);


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
                headers.put("Authorization", "bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    private void updateKyc() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting Payout Details...");
        progressDialog.show();


        JSONObject request = new JSONObject();
        try {
            request.put("first_name", et_fname.getText().toString());
            request.put("last_name", et_lname.getText().toString());
            request.put("email", et_email.getText().toString());
            request.put("contact", et_mobile.getText().toString());
            request.put("gst_number", et_gst.getText().toString());
            request.put("account", et_account.getText().toString());
            request.put("ifsc", et_ifsc.getText().toString());
            request.put("shop_name", et_shopname.getText().toString());
            request.put("shop_address", et_shopaddress.getText().toString());

            Log.d("payoutformreq-",request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"payoutKyc", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("payoutdetailres-",response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            int status = jsonObject.optInt("status");
                            String message = jsonObject.getString("message");

                            progressDialog.dismiss();
                            if (status==1) {
                                withEditText(0);
                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context, "Failed: "+message, Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer "+token);
                return headers;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);

    }

    public void withEditText(final int forwhich) {

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("OTP Required");
//
//        final EditText input = new EditText(context);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        input.setLayoutParams(lp);
//        builder.setView(input);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                YouEditTextValue = input.getText().toString();
//                verifyOtp(input.getText().toString());
//
//
//                Toast.makeText(getApplicationContext(), "Text entered is " + input.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.show();


        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_otp_verify, null);
        final EditText otp = alertLayout.findViewById(R.id.et_otp);
        final Button btn_verify = alertLayout.findViewById(R.id.btn_verify);
        final ImageView dialog_close = alertLayout.findViewById(R.id.dialog_close);
        final TextView resend_otp = alertLayout.findViewById(R.id.tv_resend_otp);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forwhich == 0) {
                    Toast.makeText(context, "otp is- " + otp.getText().toString(), Toast.LENGTH_SHORT).show();
                    verifyOtp(otp.getText().toString());
                }
                else if(forwhich==1){
                    verifyOtpAndPayoutTransaction(otp.getText().toString());
                    // write code fot verifyotp for payment time - ask to vinay sir for this
                }
            }
        });

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forwhich == 0) {
                    resendOtp("resendOtp");
                }
                else if(forwhich==1){
                    resendOtp("resendOtpPay");
                }
            }
        });

    }



    private void verifyOtp(final String otp) {
        JSONObject request = new JSONObject();
        try {
            request.put("otp",otp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"otpVerify", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("verifyotpres",response.toString());
                        int status = response.optInt("status");
                        String message = response.optString("message");

                        if(status==1) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(context,PayoutRegisterForm.class));
                        }
                        else if(status==0){
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "error-"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
    }


    private void verifyOtpAndPayoutTransaction(final String otp) {
        JSONObject request = new JSONObject();
        try {
            request.put("otp",otp);
            request.put("amount",resamount);
            request.put("payment_type",payment_type);
            request.put("orderId",orderId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"verifyOtpAndPayoutTransaction", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("verifyOtpAndPayout",response.toString());
                        int status = response.optInt("status");
                        String message = response.optString("message");

                        if(status==1) {

                            new SweetAlertDialog(PayoutRegisterForm.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Payout")
                                    .setContentText(""+message)
                                    .setConfirmText("Done")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            finish();
                                            startActivity(new Intent(context,LoadingActivity.class));
                                        }
                                    })
                                    .show();
                        }
                        else if(status==0){
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "error-"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
    }


    private void sendPayoutOtp(final String amount, String type) {
        JSONObject request = new JSONObject();
        try {
            request.put("amount",amount);
            request.put("payment_type",type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"sendPayoutOtp", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("paymentOtpVerifyres",response.toString());
                        int status = response.optInt("status");
                        otp = response.optInt("otp");
                        resamount = response.optString("amount");
                        payment_type = response.optString("payment_type");
                        orderId = response.optString("orderId");
                        String message = response.optString("message");

                        if(status==1) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            withEditText(1);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            Log.d("jsonError-",jsonError);

                            try {

                                JSONObject JO = new JSONObject(jsonError);
                                String message = JO.getString("message");
                                //Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();

                                new SweetAlertDialog(PayoutRegisterForm.this,SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Opps!")
                                        .setContentText(""+message)
                                        .show();

                            }catch (JSONException e){

                                e.printStackTrace();
                            }


                        }

//
//
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(context,
//                                    context.getString(R.string.error_network_timeout),
//                                    Toast.LENGTH_LONG).show();
//                        } else if (error instanceof AuthFailureError) {
//                            //TODO
//                            Toast.makeText(context, "Wrong1..", Toast.LENGTH_SHORT).show();
//
//                        } else if (error instanceof ServerError) {
//                            //TODO
//                            Toast.makeText(context, "Wrong2..", Toast.LENGTH_SHORT).show();
//
//                        } else if (error instanceof NetworkError) {
//                            //TODO
//                            Toast.makeText(context, "Wrong3..", Toast.LENGTH_SHORT).show();
//
//                        } else if (error instanceof ParseError) {
//                            //TODO
//                        } else if(error.networkResponse.statusCode==422){
//                            Toast.makeText(context, "Wrong4..", Toast.LENGTH_SHORT).show();
//                        }


                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
    }


    private void resendOtp(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("resendotpres",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int status = jsonObject.optInt("status");
                            String message = jsonObject.optString("message");

                            if (status == 1) {
                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            }
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
                headers.put("Authorization", "bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(context,LoadingActivity.class));
    }


}