package com.payment.yatvik.mygooglepay.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.Model.MenuModelNav;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText etEmail,etPassword;
    Button btnLogin;
    String email,password;
    ImageView iv_back;
    String token;
    TextView create_acc,forget_pass;
    Button btnJoinUs;
    int id;
    SessionHandler session;
    AlertDialog.Builder builder;
    RequestQueue queue;
    Context context;
    //List<String> models = new ArrayList<>();
    //List<MenuModelNav> posts = new ArrayList<>();
    ArrayList<MenuModelNav> mExampleList;
    String[] arr = new String[15];
    String menusting="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        iv_back = findViewById(R.id.iv_back);
        forget_pass = findViewById(R.id.forget_pass);
        btnJoinUs = findViewById(R.id.btnJoinUs);
        context = getApplicationContext();
        session = new SessionHandler(getApplicationContext());

        mExampleList = new ArrayList<>();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                loginUser();
            }
        });

        btnJoinUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(Login.this,RegisterForm.class);
                startActivity(i);
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(Login.this,ForgetPassword.class);
                startActivity(i);
            }
        });
    }


    private void loginUser() {
        JSONObject request = new JSONObject();
        try {
            request.put("email", email);
            request.put("password", password);

            Log.d("completeRequest",request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"androidlogin", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginresponse-",response.toString());

                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());

                            int status = jsonObject1.optInt("status");
                            String message = jsonObject1.getString("message");

                            if(status==1){

                                id = jsonObject1.optInt("id");
                                String email = jsonObject1.getString("email");
                                String name = jsonObject1.getString("name");
                                String pic = jsonObject1.getString("pic");
                                String loginId = jsonObject1.getString("loginId");
                                token = jsonObject1.getString("token");

                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
                                preferences.edit().putString("TOKEN",token).apply();


                                session.loginUser(String.valueOf(id), email, name, password,loginId,pic);
                                getModules(String.valueOf(id));

//                                startActivity(new Intent(Login.this, MainActivity.class));
//                                finish();

                                Toast.makeText(Login.this, ""+message, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Login.this, ""+message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }



    private void getModules(String userID) {
        queue = Volley.newRequestQueue(context);

        //listItems = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "getUserModules/" + userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //progressDialog.dismiss();
                        Log.d("category-res-", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("user_modules");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = jsonArray.getJSONObject(i);

                                int status = o.getInt("module_sta");
                                if(status==1){
                                    String menus = o.getString("module_slug");
                                    //mExampleList.add(new MenuModelNav(menus));
                                    mExampleList.add(getCarModel(menus));
                                    //String animals = "Dog, Cat, Bird, Cow";
                                    //menusting = menus+",";

                                }
                            }

                            //String[] result = menusting.split(",");
                            //Log.d("res--",result.toString());
                            //Log.d("menusting--",menusting);

                            Gson gson = new Gson();

                            String jsonCars = gson.toJson(mExampleList);

                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
                            preferences.edit().putString("MENUS",jsonCars).commit();


//                            Intent intent = new Intent(Login.this, MainActivity.class);
//                            intent.putExtra("list_as_string", jsonCars);
//                            startActivity(intent);

                            finish();
                            startActivity(new Intent(Login.this, HomePage.class));

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
                headers.put("authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(stringRequest);
    }




    private MenuModelNav getCarModel(String name){
        MenuModelNav cars = new MenuModelNav();
        cars.name = name;
        return cars;
    }


}
