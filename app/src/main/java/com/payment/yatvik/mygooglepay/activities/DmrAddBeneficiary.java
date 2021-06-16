package com.payment.yatvik.mygooglepay.activities;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.payment.yatvik.mygooglepay.Adapter.SavedBeneficiaryAdapter;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Model.BankDetailModel;
import com.payment.yatvik.mygooglepay.Model.SavedBeneficiaryModel;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.R.layout.simple_spinner_item;

public class DmrAddBeneficiary extends AppCompatActivity {
    EditText et_name,et_mobile,et_account_no,et_ifsc;
    Spinner spi_bank;
    Button btn_add;
    private static final String KEY_EMPTY = "";
    String name,mobile,account,ifsc,selectedBank,token;
    Context context;
    List<BankDetailModel> bankDetailModelList;
    private ArrayList<String> bank_name;
    RecyclerView recyclerView;
    List<SavedBeneficiaryModel> astrologerModelList;
    SavedBeneficiaryAdapter astrologerListAdapter;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmr_add_beneficiary);

        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        et_account_no = findViewById(R.id.et_account_no);
        et_ifsc = findViewById(R.id.et_ifsc);
        spi_bank = findViewById(R.id.spi_Bank);
        btn_add = findViewById(R.id.btn_submit);
        recyclerView = findViewById(R.id.rec_saved_beneficiary);
        backBtn = findViewById(R.id.backBtn);


        context = getApplicationContext();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token  = preferences.getString("TOKEN",null);
        astrologerModelList = new ArrayList<>();

        loadBankDetails();
        getAllBeneficary();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = et_name.getText().toString();
                account = et_account_no.getText().toString();
                ifsc = et_ifsc.getText().toString();
                mobile = et_mobile.getText().toString();

                if (validateInputs()) {
                    addBeneficiary();

                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DmrAddBeneficiary.this,LoadingActivity.class));
            }
        });


        spi_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBank = bankDetailModelList.get(i).getIfsc();
                //Toast.makeText(getApplicationContext(),spi_bank.getSelectedItem().toString()+"--"+selectedBank,Toast.LENGTH_LONG).show();
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                ((TextView) adapterView.getChildAt(0)).setTextSize(13);
                et_ifsc.setText(selectedBank);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void loadBankDetails() {

        bankDetailModelList = new ArrayList<>();
        bank_name = new ArrayList<>();

        String json = null;
        try {
            InputStream is = getApplication().getAssets().open("bank_details.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject object = new JSONObject(json);
            JSONArray jr = object.optJSONArray("banknames");


            for(int i=0;i<jr.length();i++)
            {
                JSONObject jsonObject = jr.getJSONObject(i);
                bankDetailModelList.add(new BankDetailModel(
                        jsonObject.optString("id"),
                        jsonObject.optString("ifsc"),
                        jsonObject.optString("bank_name")));
            }

            for (int k  = 0; k < bankDetailModelList.size(); k++) {
                bank_name.add(bankDetailModelList.get(k).getBankname());
            }

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, simple_spinner_item, bank_name);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spi_bank.setAdapter(spinnerArrayAdapter);


        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private void addBeneficiary() {
        JSONObject request = new JSONObject();
        try {
            request.put("benifiary_name",name);
            request.put("mobile_number",mobile);
            request.put("beneficiaryAccount",account);
            request.put("beneficiaryIFSC",ifsc);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"addBenificiaryDmr", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("addBenificiaryres",response.toString());
                        int status = response.optInt("status");

                        String message = response.optString("message");

                        if(status==1) {
                            new SweetAlertDialog(DmrAddBeneficiary.this,SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Great Job!")
                                    .setContentText(""+message)
                                    .show();
                            //astrologerListAdapter.notifyDataSetChanged();
                            getAllBeneficary();
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

                                new SweetAlertDialog(DmrAddBeneficiary.this,SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Opps!")
                                        .setContentText(""+message)
                                        .show();

                            }catch (JSONException e){

                                e.printStackTrace();
                            }
                        }
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

    private void getAllBeneficary() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "getAllBenificiary",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //JSONObject jsonObject = new JSONObject(response);

                            //int status = jsonObject.optInt("status");
                            //String message = jsonObject.optString("message");

                            //if (status == 1) {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject dataobj = jsonArray.getJSONObject(i);

                                    String id = dataobj.optString("id");
                                    String orderId = (dataobj.optString("orderId"));
                                    String benifiary_name = (dataobj.optString("benifiary_name"));
                                    String mobile_number = (dataobj.optString("mobile_number"));
                                    String beneficiaryAccount = (dataobj.optString("beneficiaryAccount"));
                                    String beneficiaryIFSC = (dataobj.optString("beneficiaryIFSC"));
                                    String otp = (dataobj.optString("otp"));

                                    astrologerModelList.add(new SavedBeneficiaryModel(orderId,benifiary_name,mobile_number,beneficiaryAccount,beneficiaryIFSC));

                                }
                                initRecyclerView();


                            //}
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

    private void initRecyclerView() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        astrologerListAdapter = new SavedBeneficiaryAdapter(astrologerModelList,this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(astrologerListAdapter);

    }

    public void showTransferPopUp(final String name, final String account, final String ifsc, final String orderId){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_cust_verify_popup, null);
        final EditText etname = alertLayout.findViewById(R.id.et_name);
        final EditText etaccount = alertLayout.findViewById(R.id.et_account);
        final EditText etifsc = alertLayout.findViewById(R.id.et_ifsc);
        final EditText etamount = alertLayout.findViewById(R.id.et_amount);
        final Button btnpaynow = alertLayout.findViewById(R.id.btn_paynow);
        final ImageView dialog_close = alertLayout.findViewById(R.id.dialog_close);

        etname.setText(name);
        etaccount.setText(account);
        etifsc.setText(ifsc);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnpaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = etamount.getText().toString();

                if(!amount.equals("")){
                    transferNow(name,account,ifsc,orderId,amount);
                }

            }
        });

    }

    private void transferNow(String name, String account, String ifsc, String orderId, String amount) {

        Date d=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("HHmmssSS");
        String ref = "PR"+sdf.format(d);
        Log.d("time-",ref);

        JSONObject request = new JSONObject();
        try {
            request.put("beneficiaryAccount",account);
            request.put("beneficiaryIFSC",ifsc);
            request.put("amount",amount);
            request.put("orderId",ref);
            request.put("benifiary_name",name);

            Log.d("dmrreq-",request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"dmrTrasaction", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("dmrTrasactionres",response.toString());
                        int status = response.optInt("status");

                        String message = response.optString("message");

                        if(status==1) {
                            new SweetAlertDialog(DmrAddBeneficiary.this,SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Yeah!!!")
                                    .setContentText(""+message)
                                    .show();
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

                                new SweetAlertDialog(DmrAddBeneficiary.this,SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Opps!")
                                        .setContentText(""+message)
                                        .show();

                            }catch (JSONException e){

                                e.printStackTrace();
                            }
                        }
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


    private boolean validateInputs() {
        if (KEY_EMPTY.equals(name)) {
            et_name.setError("Name cannot be empty");
            et_name.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(mobile)) {
            et_mobile.setError("Mobile cannot be empty");
            et_mobile.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(account)) {
            et_account_no.setError("Mobile cannot be empty");
            et_account_no.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(ifsc)) {
            et_ifsc.setError("IFSC Code cannot be empty");
            et_ifsc.requestFocus();
            return false;

        }
        return true;
    }

}