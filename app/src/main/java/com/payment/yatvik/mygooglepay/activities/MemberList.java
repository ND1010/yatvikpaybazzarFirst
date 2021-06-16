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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.payment.yatvik.mygooglepay.Adapter.ProductInventoryAdapter;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.Helpers.User;
import com.payment.yatvik.mygooglepay.Model.ProductInventoryModel;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MemberList extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ProductInventoryModel> astrologerModelList;
    ProductInventoryAdapter astrologerListAdapter;
    Context context;

    SessionHandler session;
    String userId;
    String token;
    TextView totalmembers;
    ImageButton backBtn,backBtn2;
    TextView pro_title;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);


        context = getApplicationContext();
        recyclerView = findViewById(R.id.frag_pro_rec);
        totalmembers = findViewById(R.id.tv_members_count);
        astrologerModelList = new ArrayList<>();

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        userId = user.getID();
        url = Constants.BASE_URL + "getMemberList/" + userId;
        //userId = "18";

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token  = preferences.getString("TOKEN",null);//second parameter default value.
        getProductInventory(url);


        backBtn = findViewById(R.id.backBtn);
        backBtn2 = findViewById(R.id.backBtn2);
        pro_title = findViewById(R.id.pro_title);

        pro_title.setText("Downline Members");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MemberList.this,LoadingActivity.class));
            }
        });


        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.custom_filter_memberlist, null);
                final EditText loginId = alertLayout.findViewById(R.id.et_memberId);
                final EditText name = alertLayout.findViewById(R.id.et_name);
                final EditText contact = alertLayout.findViewById(R.id.et_mobile);
                final EditText aadhar = alertLayout.findViewById(R.id.et_aadhar);
                final Button btn_filter = alertLayout.findViewById(R.id.btn_filter);
                final ImageView dialog_close = alertLayout.findViewById(R.id.dialog_close);

                final AlertDialog.Builder alert = new AlertDialog.Builder(MemberList.this);

                alert.setView(alertLayout);
                alert.setCancelable(true);
                final AlertDialog dialog = alert.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                btn_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String loginIdVal = loginId.getText().toString();
                        String nameVal = name.getText().toString();
                        String contactVal = contact.getText().toString();
                        String aadharVal = aadhar.getText().toString();

                        String param = url+"?loginId="+loginIdVal+"&name="+nameVal+"&contact="+contactVal+"&aadhar="+aadharVal;
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

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int status = jsonObject.optInt("status");
                            String message = jsonObject.optString("message");

                            if (status == 1) {
                                JSONArray jsonArray = jsonObject.optJSONArray("user_member_list");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject dataobj = jsonArray.getJSONObject(i);

                                    String id = dataobj.optString("id");
                                    String kyc_status = (dataobj.optString("kyc_status"));
                                    String name = (dataobj.optString("name"));
                                    String last_name = (dataobj.optString("last_name"));
                                    String contact = (dataobj.optString("contact"));
                                    String package_type = (dataobj.optString("package_type"));
                                    String member_type = (dataobj.optString("member_type"));
                                    String owner_id = (dataobj.optString("owner_id"));
                                    String created_at = (dataobj.optString("created_at"));

                                    String fullname = name +" "+ last_name;

                                    astrologerModelList.add(new ProductInventoryModel(id,kyc_status,fullname,contact,package_type,member_type,owner_id,created_at));

                                }
                                initRecyclerView();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MemberList.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(MemberList.this).addToRequestQueue(stringRequest);


    }

    private void initRecyclerView() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MemberList.this);
        recyclerView.setLayoutManager(mLayoutManager);
        astrologerListAdapter = new ProductInventoryAdapter(astrologerModelList);
        recyclerView.setAdapter(astrologerListAdapter);

        totalmembers.setText("Total " + astrologerModelList.size() + " Members Found");

    }


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(context,LoadingActivity.class));
    }

}