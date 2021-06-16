package com.payment.yatvik.mygooglepay.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WalletTopUp extends AppCompatActivity implements PaymentResultListener {

    EditText et_mobile,et_email,et_amount;
    Button btn_recharge;
    private static final String TAG = WalletTopUp.class.getSimpleName();
    private static final String KEY_EMPTY = "";
    String amountVal,email,mobile,token,aftergstamount;
    double amount;
    Context context;
    TextView tv_check_status;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_top_up);

        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        et_amount = findViewById(R.id.et_amount);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        btn_recharge = findViewById(R.id.btn_recharge);
        tv_check_status = findViewById(R.id.tv_check_status);
        context = getApplicationContext();
        backBtn = findViewById(R.id.backBtn);


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token  = preferences.getString("TOKEN",null);

        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email= et_email.getText().toString();
                mobile = et_mobile.getText().toString();
                amount = Double.parseDouble(amountVal);

                try
                {
                    Log.d("amount-d",amountVal);

                    // it means it is double
                    if (validateInputs()){
                        WallettopUp();
                    }

                } catch (Exception e1) {
                    // this means it is not double
                    e1.printStackTrace();
                }



            }
        });

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                amountVal = et_amount.getText().toString().trim();
                if(!amountVal.isEmpty()) {

                    amount = Double.parseDouble(amountVal);
                    Log.d("amount-dd", amountVal);

                    // TODO Auto-generated method stub
                    if (count >= 1) {
                        checkBalance(s);

                    } else if (count == 0) {
                        tv_check_status.setVisibility(View.INVISIBLE);
                    }
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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(WalletTopUp.this,LoadingActivity.class));
            }
        });


    }

    private void checkBalance(CharSequence s) {

        tv_check_status.setVisibility(View.VISIBLE);
        double aftergstamount = amount - (amount*(18.00/100.00));
        String Printval = String.format("%.2f", aftergstamount);
        tv_check_status.setText("Amount: "+Printval +" will be Recharged");


    }

    private void WallettopUp() {
         /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "MyGooglePe");
            options.put("description", "Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mobile);

            options.put("prefill", preFill);

            Log.d("optios-",options.toString());

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {

        try {
            walletOnlineRecharge(razorpayPaymentID,amountVal);

            Log.d("razorpayPaymentID-",razorpayPaymentID);
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }



    private void walletOnlineRecharge(String id, String amount) {
        JSONObject request = new JSONObject();
        try {
            request.put("amount",amount);
            request.put("txnId",id);
            Log.d("localres-",request.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"walletOnlineRecharge", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());

                            int status = jsonObject1.optInt("status");
                            String message = jsonObject1.getString("message");

                            if(status==1){

                                new SweetAlertDialog(WalletTopUp.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Wallet TopUp")
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

                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("walletOnlineRecharge",response.toString());
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


    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }


    private boolean validateInputs() {
        if (KEY_EMPTY.equals(amountVal)) {
            et_amount.setError("Amount cannot be empty");
            et_amount.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(mobile)) {
            et_mobile.setError("Mobile cannot be empty");
            et_mobile.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(email)) {
            et_email.setError("Email cannot be empty");
            et_email.requestFocus();
            return false;

        }

        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(WalletTopUp.this,LoadingActivity.class));

    }
}