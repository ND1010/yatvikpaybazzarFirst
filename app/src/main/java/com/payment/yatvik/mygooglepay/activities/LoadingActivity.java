package com.payment.yatvik.mygooglepay.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.Preferences;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoadingActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1500;

    Animation topAnim, bottomAnim;
    TextView logo, slogan;
    ImageView img1,img2;
    Preferences preferences;
    SessionHandler session;
    String oldtoken,newtoken;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        preferences = new Preferences(this);
        session = new SessionHandler(getApplicationContext());
        context = getApplicationContext();


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        oldtoken  = preferences.getString("TOKEN",null);

        //Hooks
        slogan = findViewById(R.id.textView2);
        img1 = findViewById(R.id.cff_logo);
        img2 = findViewById(R.id.cff_full_logo);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Set animation to elements
        slogan.setAnimation(bottomAnim);
        img1.setAnimation(topAnim);
        //img2.setAnimation(bottomAnim);


        refreshToken(oldtoken);

        new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {

                  if(session.isLoggedIn()){

                      startActivity(new Intent(LoadingActivity.this, HomePage.class));
                  }
                  else {
                      Intent intent = new Intent(LoadingActivity.this, Login.class);
                      startActivity(intent);
                      finish();
                  }
              }
          },SPLASH_SCREEN);

        }

    private void refreshToken(final String oldtoken) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL+"refreshToken",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsePincode",response);


                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());

                            int status = jsonObject1.optInt("status");
                            String message = jsonObject1.getString("message");

                            if(status==1){

                                int id = jsonObject1.optInt("id");
                                String email = jsonObject1.optString("email");
                                String name = jsonObject1.optString("name");
                                newtoken = jsonObject1.optString("token");

                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
                                preferences.edit().putString("TOKEN",newtoken).commit();


                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No token found", Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer "+oldtoken);
                return headers;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }



//    private void getLocalAccesstoken() {
//        //displayLoader();
//        final JSONObject request = new JSONObject();
//        try {
//            request.put("email", Config.adminuseremail);
//            request.put("password", Config.adminPassword);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
//                (Request.Method.POST, localgetTokenUrl, request, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("accesstokenlocal-",response.toString());
//                        if (response.optString("status").equals("1")) {
//                            try {
//                                JSONObject dataObj = response.getJSONObject("data");
//
//                                String local_access_token = dataObj.optString("access_token");
//
//                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//                                SharedPreferences.Editor editor = pref.edit();
//                                editor.putString("local_access_token", local_access_token); // Storing string
//                                editor.apply(); // commit changes
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                        else {
//                            Toast.makeText(LoadingActivity.this, "token error!!!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Went wrong! "+error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
//    }

}