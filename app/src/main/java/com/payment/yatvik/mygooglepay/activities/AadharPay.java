package com.payment.yatvik.mygooglepay.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.payment.yatvik.mygooglepay.R;


public class AadharPay extends AppCompatActivity {
    String token, access_token;
    Context context;
    String par = "<PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"0\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"1\" pidVer=\"2.0\" timeout=\"10000\" otp=\"\" wadh=\"$WADH\" posh=\"\"/></PidOptions>";
    String url = "http://127.0.0.1:11100/capture";
    int DEVICE_INFO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhar_pay);

        context = getApplicationContext();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token = preferences.getString("TOKEN", null);

        //getAllBeneficary();
        //capture();
        //captureRdRequest(par,url);


//        Intent intent = new Intent("in.gov.uidai.rdservice.fp.INFO");
//        intent.setPackage("com.scl.rdservice");
//        startActivityForResult(intent, DEVICE_INFO);


        Intent intent = new Intent("in.gov.uidai.rdservice.fp.CAPTURE");
        intent.setPackage("com.scl.rdservice");
        intent.putExtra("PID_OPTIONS", par);
        startActivityForResult(intent, 2);




//
//        try {
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        MediaType mediaType = MediaType.parse("text/xml");
//        RequestBody body = RequestBody.create(mediaType, "<PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"0\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"1\" pidVer=\"2.0\" timeout=\"10000\" otp=\"\" wadh=\"$WADH\" posh=\"\"/></PidOptions>");
//        Request request = new Request.Builder()
//                .url("http://127.0.0.1:11100/capture")
//                .method("CAPTURE", body)
//                .addHeader("Content-Type", "application/xml")
//                .addHeader("Accept", "text/xml")
//                .build();
//
//            Response response = client.newCall(request).execute();
//            Log.d("responses-",response.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        try {
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .build();
//            MediaType mediaType = MediaType.parse("text/xml");
//            RequestBody body = RequestBody.create(mediaType, "<PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"0\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"1\" pidVer=\"2.0\" timeout=\"10000\" otp=\"\" wadh=\"$WADH\" posh=\"\"/></PidOptions>");
//            okhttp3.Request request = new okhttp3.Request.Builder()
//                    .url("http://127.0.0.1:11100/capture")
//                    .method("CAPTURE", body)
//                    .addHeader("Content-Type", "application/xml")
//                    .addHeader("Accept", "text/xml")
//                    .build();
//
//            okhttp3.Response response = client.newCall(request).execute();
//            Log.d("resokhttp", response.toString());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        //new UpdateTask().execute();

    }

//    private void captureRdRequest(final String pidOpt, final String reqUrl) {
//
//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try  {
//
//                    OkHttpClient client = new OkHttpClient().newBuilder()
//                            .build();
//                    MediaType mediaType = MediaType.parse("text/xml");
//                    RequestBody body = RequestBody.create(mediaType, pidOpt);
//                    okhttp3.Request request = new okhttp3.Request.Builder()
//                            .url(reqUrl)
//                            .method("CAPTURE", body)
//                            .addHeader("Content-Type", "text/xml")
//                            .addHeader("Accept", "text/xml")
//                            .build();
//                    okhttp3.Response response = client.newCall(request).execute();
//                    Log.d("sdf", response.toString());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    // in this variable you will get Pid data
                    String pidData = b.getString("PID_DATA");
                    showLogInfoDialog("pid",pidData);

                    // you will get value in this variable when your finger print device not connected
                    String dnc = b.getString("DNC", "");
                    showLogInfoDialog("dnc",dnc);

                    // you will get value in this variable when your finger print device not registered.
                    String dnr = b.getString("DNR", "");
                    showLogInfoDialog("dnr",dnr);
                }
            }
        }
    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK)
//        {
//            Bundle b = data.getExtras();
//            if (b != null)
//            {
//                String deviceInfo = b.getString("DEVICE_INFO", "");
//                String rdServiceInfo = b.getString("RD_SERVICE_INFO", "");
//                String dnc = b.getString("DNC", "");
//                String dnr = b.getString("DNR", "");
//                if (!dnc.isEmpty() || !dnr.isEmpty())
//                {
//                    showLogInfoDialog("Device Info", dnc + dnr + " " + deviceInfo +
//                            rdServiceInfo);
//                }
//                else
//                {
//                    showLogInfoDialog("Device Info", deviceInfo + rdServiceInfo);
//                }
//            }
//        }
//    }
//
    private void showLogInfoDialog(String device_info, String s) {
        Toast.makeText(context, device_info + s, Toast.LENGTH_SHORT).show();
    }







}


//class UpdateTask extends AsyncTask<String, String, Void> {
//        protected Void doInBackground(String... urls) {
//
//            try {
//                String requestXml = "<PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"0\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"1\" pidVer=\"2.0\" timeout=\"10000\" otp=\"\" wadh=\"$WADH\" posh=\"\"/></PidOptions>";
//
//                Unirest.setTimeouts(0, 0);
//
//                HttpResponse<String> response = Unirest.post("http://127.0.0.1:11100/capture")
//                        .header("Accept", "text/xml")
//                        .header("Content-Type", "text/xml")
//                        .body(requestXml)
//                        .asString();
//
//                Log.d("ressss-", response.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//}

    //public void capture(){
//
//        DefaultClientConfig config = new DefaultClientConfig();
//        config.getProperties().put(URLConnectionClientHandler.PROPERTY_HTTP_URL_CONNECTION_SET_METHOD_WORKAROUND, true);
//        Client c = Client.create(config);
//        WebResource r = c.resource("http://google.com");
//        String reponse = r.method("FOOBAR", String.class);






//
//    private void getAllBeneficary() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "getAadharPayToken",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            access_token = jsonObject.optString("access_token");
//                            Toast.makeText(context, ""+access_token, Toast.LENGTH_SHORT).show();
//                            //addBeneficiary(access_token);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                headers.put("Accept", "application/json");
//                headers.put("Authorization", "bearer "+token);
//                return headers;
//            }
//        };
//
//        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
//
//
//    }
//
//
//    private void addBeneficiary(final String acc) {
//
//        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,"https://uat2epmoney.easypay.co.in/epMoney/rest/aeps/getrddatahash",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String RESP_CODE = jsonObject.getString("RESP_CODE");
//                            String RESP_MSG = jsonObject.getString("RESP_MSG");
//                            JSONObject DATA = jsonObject.getJSONObject("DATA");
//                            String pidOpt = DATA.getString("pidOpt");
//                            String reqUrl = DATA.getString("reqUrl");
//
//                            Toast.makeText(AadharPay.this, "requrl-"+reqUrl, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(AadharPay.this, "pidOpt-"+pidOpt, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(AadharPay.this, "RESP_MSG-"+RESP_MSG, Toast.LENGTH_SHORT).show();
//
//                            //captureRdRequest(pidOpt,reqUrl);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyLog.d("volley", "Error: " + error.getMessage());
//                        error.printStackTrace();
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("jsonData", "{   \"HEADER\": {     \"ST\": \"WITHDRAWAL\",     \"AID\": \"RS00789\",     \"OP\": \"AEPS\"   },   \"DATA\": {     \"CUSTOMER_MOBILE\": \"8210812740\",     \"DEVICE\": \"MORPHO_PROTOBUF\"   } }");
//                return params;
//            }
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                headers.put("Accept", "application/json");
//                headers.put("Authorization", "bearer "+acc);
//
//                    return headers;
//            }
//
//        };
//
//
//        MySingleton.getInstance(context).addToRequestQueue(jsonObjRequest);
//    }
//

//}
//}



