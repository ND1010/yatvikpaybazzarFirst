package com.payment.yatvik.mygooglepay.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.Helpers.User;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {


    ImageView iv_show_hide_pass2,iv_show_hide_pass3;
    EditText etNewPassword,etConNewPassword;
    Button btn_update_password;

    private static final String KEY_EMPTY = "";
    String NewPassVal,ConNewPassVal;
    ProgressDialog pDialog;

    SessionHandler session;
    String userId;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        iv_show_hide_pass2 = findViewById(R.id.iv_show_hide_pass2);
        iv_show_hide_pass3 = findViewById(R.id.iv_show_hide_pass3);
        etNewPassword = findViewById(R.id.etNewPass);
        etConNewPassword = findViewById(R.id.etConfNewPass);
        btn_update_password = findViewById(R.id.btn_update_password);

        //pDialog = new ProgressDialog(getApplicationContext());
        if(session.isLoggedIn()){
            userId = user.getID();
        }


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token  = preferences.getString("TOKEN",null);


        iv_show_hide_pass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etNewPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()){
                    iv_show_hide_pass2.setImageResource(R.drawable.ic_visibility);

                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    iv_show_hide_pass2.setImageResource(R.drawable.ic_invisible);

                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        iv_show_hide_pass3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etConNewPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()){
                    iv_show_hide_pass3.setImageResource(R.drawable.ic_visibility);

                    etConNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    iv_show_hide_pass3.setImageResource(R.drawable.ic_invisible);

                    etConNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewPassVal = etNewPassword.getText().toString();
                ConNewPassVal = etConNewPassword.getText().toString();

                if(ValidAllField()){
                    requestUpdatePassword(NewPassVal,ConNewPassVal);
                }
                else {
                    Toast.makeText(ChangePassword.this, "Wrong!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void requestUpdatePassword(String pass1, String pass2) {

        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("password", NewPassVal);
            request.put("password_confirmation", ConNewPassVal);

            Log.d("completeRequest",request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"userUpdatePassword/"+userId, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginresponse-",response.toString());

                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());

                            int status = jsonObject1.optInt("status");
                            String message = jsonObject1.getString("message");

                            pDialog.dismiss();
                            if(status==1){
                                Toast.makeText(ChangePassword.this, ""+message, Toast.LENGTH_SHORT).show();

                                finish();
                                startActivity(new Intent(ChangePassword.this, Login.class));

                            }
                            else{
                                Toast.makeText(ChangePassword.this, ""+message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer "+token);

                return headers;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);


    }


    public boolean ValidAllField() {
        if (KEY_EMPTY.equals(NewPassVal)){
            etNewPassword.setError("Required New Password");
            etNewPassword.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(ConNewPassVal)){
            etConNewPassword.setError("Required Confirm Password");
            etConNewPassword.requestFocus();
            return false;
        }

        if (!NewPassVal.equals(ConNewPassVal)) {
            etConNewPassword.setError("New Password and Confirm Password does not match");
            etConNewPassword.requestFocus();
            return false;
        }

        return true;
    }


    private void displayLoader() {
        pDialog = new ProgressDialog(ChangePassword.this);
        pDialog.setMessage("Updating Password, Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
}
