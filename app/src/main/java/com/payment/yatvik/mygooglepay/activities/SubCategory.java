package com.payment.yatvik.mygooglepay.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Model.SubCategoryModel;
import com.payment.yatvik.mygooglepay.Model.SubDistrictModel;
import com.payment.yatvik.mygooglepay.Model.SubDivisionModel;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_spinner_item;

public class SubCategory extends AppCompatActivity {

    String catproid;
    private Context context;

    List<SubCategoryModel> subcatlist;
    private ArrayList<String> subcatname;
    Spinner spinner,spinner2,spinner3;
    String ChangedVal;
    int hasgrouping;
    String oldtoken,newtoken;
    LinearLayout forgrouping;

    List<SubDistrictModel> subDistrictModelList;
    ArrayList<String> subdistrict;

    List<SubDivisionModel> subDivisionModelList;
    ArrayList<String> subdivision;

    TextView tv_service_for,tv_cust_id,tv_cont_num,tv_payfor_title,tv_message;
    Button btn_submit;
    EditText et_account,et_mobile;
    ImageButton backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        context = getApplicationContext();

        this.spinner = (Spinner) findViewById(R.id.spiCat);
        this.spinner2 = (Spinner) findViewById(R.id.spi_district);
        this.spinner3 = (Spinner) findViewById(R.id.spi_divison);
        forgrouping = findViewById(R.id.forgrouping);
        tv_service_for = findViewById(R.id.tv_service_for);
        tv_cust_id = findViewById(R.id.tv_cust_id);
        tv_cont_num = findViewById(R.id.tv_cont_num);
        tv_payfor_title = findViewById(R.id.tv_payfor_title);
        btn_submit = findViewById(R.id.btn_submit);
        et_account = findViewById(R.id.et_account);
        et_mobile = findViewById(R.id.et_mobile);
        backBtn = findViewById(R.id.backBtn);
        tv_message = findViewById(R.id.tv_message);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        oldtoken  = preferences.getString("TOKEN",null);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                catproid = null;
                Toast.makeText(context, "Server Error. Check later", Toast.LENGTH_SHORT).show();
            } else {
                catproid = extras.getString("catproid");

                tv_payfor_title.setText("Pay " + catproid + " Bill For:");
                tv_service_for.setText("Select "+catproid+" Service");
                if(catproid.equals("Mobile")){
                    tv_cont_num.setText("Enter Rechare Amount");
                    et_account.setHint("Enter Mobile Number");
                    et_mobile.setHint("Enter Recharge Amount");
                }
                else {
                    tv_cont_num.setText("Enter Customer Mobile Number");
                }
                refreshToken(oldtoken);
            }
        } else {
            catproid = (String) savedInstanceState.getSerializable("catproid");
            refreshToken(oldtoken);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ChangedVal = subcatlist.get(i).getId();
                hasgrouping = subcatlist.get(i).getHas_grouping();

                String name = spinner.getSelectedItem().toString();
                boolean isFound = name.contains("Prepaid");

                //Toast.makeText(getApplicationContext(),spinner.getSelectedItem().toString()+"--"+ChangedVal+hasgrouping,Toast.LENGTH_LONG).show();
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                ((TextView) adapterView.getChildAt(0)).setTextSize(13);



                if(subcatlist.get(i).getHas_grouping()==0){
                    Toast.makeText(context, "hasvalue=0", Toast.LENGTH_SHORT).show();
                    forgrouping.setVisibility(View.GONE);


                }
                if(subcatlist.get(i).getHas_grouping()==0 && isFound){
                    Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
                    forgrouping.setVisibility(View.GONE);


                }
                else if(subcatlist.get(i).getHas_grouping()==1){
                    //Toast.makeText(context, "hasvalue=1", Toast.LENGTH_SHORT).show();
                    forgrouping.setVisibility(View.VISIBLE);
                    getSubDistrict(ChangedVal,hasgrouping);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = spinner2.getSelectedItemPosition();
                getSubDivision(pos,ChangedVal,hasgrouping);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catproid.equals("Mobile")){
                    payBill();
                }
                else {
                    getBillingData();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SubCategory.this,LoadingActivity.class));
            }
        });


    }

    private void getSubDistrict(String billerId, int hasgrouping) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL+"getBillItenary/"+billerId+"/"+hasgrouping,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("billitenary-",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                                JSONObject jsonObject1 = jsonObject.optJSONObject("original");
                                JSONArray jsonPincodeArray = jsonObject1.optJSONArray("bill_details_input_parameters");


                                if (jsonPincodeArray.length() >= 1) {


                                    for (int i = 1; i < jsonPincodeArray.length(); i++) {

                                        JSONObject jsonObject2 = jsonPincodeArray.getJSONObject(i);
                                        JSONObject jsonObject3 = jsonObject2.optJSONObject("values");
                                        JSONObject jsonObject4 = jsonObject3.optJSONObject("groupingHeirarchy");


                                        subDistrictModelList = new ArrayList<>();
                                        subdistrict = new ArrayList<>();
                                        for(int j = 0; j<jsonObject4.names().length(); j++){
                                            Log.v("Keys-", jsonObject4.names().getString(j));
                                            Log.v("keyss-", "key = " + jsonObject4.names().getString(j) + "value = " + jsonObject4.get(jsonObject4.names().getString(j)));
                                            //subDistrictModelList.add(jsonObject4.names().toString(j));

                                            SubDistrictModel spinnerModel = new SubDistrictModel();
                                            spinnerModel.setDistricname(jsonObject4.names().getString(j));
                                            subDistrictModelList.add(spinnerModel);

                                        }

                                        for (int k  = 0; k < subDistrictModelList.size(); k++) {
                                            subdistrict.add(subDistrictModelList.get(k).getDistricname());
                                        }

                                        Log.d("subdis-",subdistrict.toString());


                                    }
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, simple_spinner_item, subdistrict);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner2.setAdapter(spinnerArrayAdapter);
                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer "+newtoken);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }


    private void getSubDivision(final int pos, String billerId, int hasgrouping) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL+"getBillItenary/"+billerId+"/"+hasgrouping,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("billitenary-",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                                JSONObject jsonObject1 = jsonObject.optJSONObject("original");
                                JSONArray jsonPincodeArray = jsonObject1.optJSONArray("bill_details_input_parameters");


                                if (jsonPincodeArray.length() >= 1) {


                                    for (int i = 1; i < jsonPincodeArray.length(); i++) {

                                        JSONObject jsonObject2 = jsonPincodeArray.getJSONObject(i);
                                        JSONObject jsonObject3 = jsonObject2.optJSONObject("values");
                                        JSONObject jsonObject4 = jsonObject3.optJSONObject("groupingHeirarchy");


                                        subDivisionModelList = new ArrayList<>();
                                        subdivision = new ArrayList<>();
                                        for(int j = pos; j<=pos; j++){

                                            JSONObject jsonObject5 = jsonObject4.getJSONObject(jsonObject4.names().getString(j));

                                            Iterator<String> iter = jsonObject5.keys(); //This should be the iterator you want.
                                            while(iter.hasNext()) {
                                                String key = iter.next();
                                                Log.v("Keys3-", key);

                                                SubDivisionModel spinnerModel = new SubDivisionModel();
                                                spinnerModel.setSuddivision(key);
                                                subDivisionModelList.add(spinnerModel);
                                            }
                                        }

                                        for (int k  = 0; k < subDivisionModelList.size(); k++) {
                                            subdivision.add(subDivisionModelList.get(k).getSuddivision());
                                        }

                                        Log.d("subdis-",subdivision.toString());


                                    }
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, simple_spinner_item, subdivision);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner3.setAdapter(spinnerArrayAdapter);
                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer "+newtoken);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }


    private void loadProducts(final String newtoken) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL+"getBillCategoryDetails/"+catproid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        Log.d("subcatres-",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int status = jsonObject.optInt("status");
                            String message = jsonObject.optString("message");

                            if (status==1) {

                                JSONArray jsonPincodeArray = jsonObject.optJSONArray("bill_category_details");
                                if (jsonPincodeArray.length() >= 1) {

                                    subcatlist = new ArrayList<>();
                                    subcatname = new ArrayList<>();
                                    for (int i = 0; i < jsonPincodeArray.length(); i++) {

                                        SubCategoryModel spinnerModel = new SubCategoryModel();
                                        JSONObject dataobj = jsonPincodeArray.optJSONObject(i);

                                        spinnerModel.setId(dataobj.optString("biller_id"));
                                        spinnerModel.setName(dataobj.optString("biller_name"));
                                        spinnerModel.setReg_exp(dataobj.optString("reg_exp"));
                                        spinnerModel.setDisplay_value(dataobj.optString("display_value"));
                                        spinnerModel.setHas_grouping(dataobj.optInt("has_grouping"));

                                        tv_cust_id.setText(dataobj.optString("display_value"));

                                        subcatlist.add(spinnerModel);
                                    }

                                    for (int i = 0; i < subcatlist.size(); i++) {
                                        subcatname.add(subcatlist.get(i).getName());
                                    }

                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, simple_spinner_item, subcatname);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner.setAdapter(spinnerArrayAdapter);

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer "+newtoken);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
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
                                String email = jsonObject1.getString("email");
                                String name = jsonObject1.getString("name");
                                newtoken = jsonObject1.getString("token");

                                // Calling Application class (see application tag in AndroidManifest.xml)
                                //final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                //globalVariable.setToken(newtoken);

                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
                                preferences.edit().putString("TOKEN",newtoken).commit();

                                loadProducts(newtoken);

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


    private void getBillingData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Payment Details...");
        progressDialog.show();

        final String account = et_account.getText().toString();
        final String mobile = et_mobile.getText().toString();

        JSONObject request = new JSONObject();
        try {
            request.put("account_number", account);
            request.put("customer_mobile", mobile);
            request.put("fieldValue", tv_cust_id.getText());
            request.put("id", ChangedVal);
            request.put("hasGroup", String.valueOf(hasgrouping));
            request.put("sub_district", null);
            request.put("sub_division", null);

            Log.d("billingrequest-",request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"getBillDesign", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("billdesign-",response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            String status = jsonObject.optString("status");
                            progressDialog.dismiss();
                            if (status.equals("SUCCESS")) {
                                Date d=new Date();
                                SimpleDateFormat sdf= new SimpleDateFormat("HHmmssSS");
                                String ref = sdf.format(d)+"PP";
                                Log.d("time-",ref);

                                Intent intent = new Intent(context,BillCreateActivity.class);
                                intent.putExtra("response",response.toString());
                                intent.putExtra("title",spinner.getSelectedItem().toString());
                                intent.putExtra("account",account);
                                intent.putExtra("ref",ref);
                                intent.putExtra("filed_value",tv_cust_id.getText());
                                intent.putExtra("mobile",mobile);
                                startActivity(intent);
                            }
                            else if(status.equals("FAILURE")){
                                String message = jsonObject.getString("message");
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
                headers.put("authorization", "Bearer "+newtoken);
                return headers;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);

    }


    private void payBill() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Payment Processing...");
        progressDialog.show();


        final String account = et_account.getText().toString();
        final String amount = et_mobile.getText().toString();

        Date d=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("HHmmssSS");
        String ref = sdf.format(d)+"PP";
        Log.d("time-",ref);

        JSONObject request = new JSONObject();
        try {
            request.put("biller_id", ChangedVal);
            request.put("account_number", account);
            request.put("sub_district", null);
            request.put("sub_division", null);
            request.put("amount", amount);
            request.put("postal_code", "110054");
            request.put("fieldValue", tv_cust_id.getText().toString());
            request.put("refid", ref);
            request.put("customer_mobile", account);

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
                headers.put("authorization", "Bearer "+newtoken);
                return headers;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);

    }

}
