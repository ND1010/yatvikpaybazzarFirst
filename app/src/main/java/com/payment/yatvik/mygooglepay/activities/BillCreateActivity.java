package com.payment.yatvik.mygooglepay.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BillCreateActivity extends AppCompatActivity {

    TextView tv_cust_name, tv_invoice_id, tv_billdate, tv_duedate, tv_payable_amount, tv_service,tv_message;
    EditText et_agent_mobile, et_pincode;
    Button btn_paynow;
    String token, data,title;
    Context context;
    String ref_id, biller_id,amount,post_code,mobile,account,filed_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_create);

        tv_cust_name = findViewById(R.id.tv_cust_name);
        tv_invoice_id = findViewById(R.id.tv_invoice_id);
        tv_billdate = findViewById(R.id.tv_billdate);
        tv_duedate = findViewById(R.id.tv_duedate);
        tv_payable_amount = findViewById(R.id.tv_payable_amount);
        tv_service = findViewById(R.id.tv_service);
        et_agent_mobile = findViewById(R.id.et_agent_mobile);
        et_pincode = findViewById(R.id.et_pincode);
        btn_paynow = findViewById(R.id.btn_paynow);
        tv_message = findViewById(R.id.tv_message);

        et_agent_mobile.setKeyListener(null);
        et_pincode.setKeyListener(null);
        context = getApplicationContext();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token = preferences.getString("TOKEN", null);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            data = extras.getString("response");
            Log.d("intentdata",data);
            title  = extras.getString("title");
            account  = extras.getString("account");
            filed_value  = extras.getString("filed_value");
            ref_id  = extras.getString("ref");
            mobile  = extras.getString("mobile");
            tv_service.setText(""+title);
            getBillingData(data);
        }


        btn_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBill();
            }
        });


    }

    private void payBill() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Payment Processing...");
        progressDialog.show();


        JSONObject request = new JSONObject();
        try {
            request.put("biller_id", biller_id);
            request.put("account_number", account);
            request.put("sub_district", null);
            request.put("sub_division", null);
            request.put("amount", amount);
            request.put("postal_code", post_code);
            request.put("fieldValue", filed_value);
            request.put("refid", ref_id);
            request.put("customer_mobile", mobile);

            Log.d("paybillreq-",request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"payBill", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("paybillres-",response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            progressDialog.dismiss();
                            String status = jsonObject.optString("status");
                            String error = jsonObject.optString("error");
                            String message = jsonObject.optString("message");

                            tv_message.setVisibility(View.VISIBLE);
                            tv_message.setText(""+message);


                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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



    private void getBillingData(String data) {

        try {
            JSONObject jsonObject = new JSONObject(data);

            String status = jsonObject.optString("status");
            String message = jsonObject.optString("message");
            //ref_id = jsonObject.optString("ref_id");
            String agent_mobile = jsonObject.optString("mobile");
            post_code = jsonObject.optString("post_code");

            et_agent_mobile.setText("" + agent_mobile);
            et_pincode.setText("" + post_code);


            if (status.equals("SUCCESS")) {

                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONObject biller_details_obj = jsonObject1.getJSONObject("biller_details");
                JSONObject bill_details_obj = jsonObject1.getJSONObject("bill_details");

                biller_id = biller_details_obj.optString("biller_id");

                Boolean editable = bill_details_obj.getBoolean("editable");
                amount = bill_details_obj.optString("amount");
                String Customer_name = bill_details_obj.optString("Customer Name");
                String Office_code = bill_details_obj.optString("Office Code");
                String Binder_number = bill_details_obj.optString("Binder Number");
                String Invoice_Id = bill_details_obj.optString("Invoice Id");
                String Bill_Date = bill_details_obj.optString("Bill Date");
                String Due_Date = bill_details_obj.optString("Due Date");
                String Amount_Payable = bill_details_obj.optString("Amount Payable");

                tv_cust_name.setText("" + Customer_name);
                tv_invoice_id.setText("" + Invoice_Id);
                tv_billdate.setText("" + Bill_Date);
                tv_duedate.setText("" + Due_Date);
                tv_payable_amount.setText("Payable Amount: " + amount);
                tv_duedate.setText("" + Due_Date);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}