package com.payment.yatvik.mygooglepay.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.payment.yatvik.mygooglepay.Adapter.BillServiceAdapter;
import com.payment.yatvik.mygooglepay.Adapter.HomeBankingServicesAdapter;
import com.payment.yatvik.mygooglepay.Adapter.MyAdapterCategory;
import com.payment.yatvik.mygooglepay.Adapter.SliderAdapter;
import com.payment.yatvik.mygooglepay.Helpers.ClickListener;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.Helpers.SliderItem;
import com.payment.yatvik.mygooglepay.Helpers.User;
import com.payment.yatvik.mygooglepay.Model.BillServicesModel;
import com.payment.yatvik.mygooglepay.Model.CatListGetSet;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    Context context;
    View root;
    SessionHandler session;
    User user;
    String userID;
    CircleImageView profile_pic;

    // For Category listing on Homepage //
    private static final String CategoryUrl = Constants.BASE_URL + "getUserModules/18";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<CatListGetSet> listItems;


    // For Bill Payment Category listing on Homepage //
    private static final String BillPayUrl = Constants.BASE_URL + "getBillServices";
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter2;
    private List<BillServicesModel> listItems2;


    // For Mini Statement Category listing on Homepage //
    //private static final String BillPayUrl = Constants.BASE_URL+"getBillServices";
    private RecyclerView recyclerView3;
    private RecyclerView.Adapter adapter3;
    private List<BillServicesModel> listItems3;

    RequestQueue queue;
    String oldtoken, newtoken;
    TextView tv_username;
    TextView tvWalletBal;

    ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        context = getContext();
        //Picasso.setSingletonInstance(new Picasso.Builder(context).build());
        session = new SessionHandler(context);
        viewPager2 = root.findViewById(R.id.viewPagerImageSlider);
        tv_username = root.findViewById(R.id.tv_username);
        profile_pic = root.findViewById(R.id.nav_proflepic);
        tvWalletBal = root.findViewById(R.id.tvWalletBal);

        user = session.getUserDetails();

        userID = user.getID();
        tv_username.setText(user.getFullName() + " (" + user.getLoginId() + ")");


        final List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.slide1));
        sliderItems.add(new SliderItem(R.drawable.slide2));
        sliderItems.add(new SliderItem(R.drawable.slide2));
        sliderItems.add(new SliderItem(R.drawable.slide4));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnanle);
                sliderHandler.postDelayed(sliderRunnanle, 3000);
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        oldtoken = preferences.getString("TOKEN", null);//second parameter default value.
        Glide.with(context).load(user.getProfilepic()).into(profile_pic);


        getActivity().setTitle("MyGooglePe");
        loadRecylerViewData2();
        loadRecylerViewData3();
        refreshToken(oldtoken);

        return root;
    }

    private Runnable sliderRunnanle = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };


    private void loadRecylerViewData(final String newtoken) {
        recyclerView = root.findViewById(R.id.recylecat);
        //recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        listItems = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "getUserModules/" + userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        Log.d("category-res-", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("user_modules");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = jsonArray.getJSONObject(i);
                                CatListGetSet item = new CatListGetSet(
                                        o.getString("id"),
                                        o.getString("user_id"),
                                        o.getString("module_slug"),
                                        o.getString("module_sta"));
                                listItems.add(item);
                            }


                            Log.d("category-list- ", listItems.toString());
                            adapter = new MyAdapterCategory(listItems, context, new ClickListener() {
                                @Override
                                public void onPositionClicked(int position) {
                                    //Toast.makeText(context, "gf" , Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onLongClicked(int position) {
                                    //Toast.makeText(context,"huuju", Toast.LENGTH_LONG).show();
                                }
                            });
                            recyclerView.setAdapter(adapter);

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
                headers.put("authorization", "Bearer " + newtoken);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void loadRecylerViewData2() {
        recyclerView2 = root.findViewById(R.id.recylecatbill);
        //recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView2.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(manager);

        listItems2 = new ArrayList<>();


        listItems2.add(new BillServicesModel("Electricity", R.drawable.ic_light_bulb));
        listItems2.add(new BillServicesModel("Water", R.drawable.ic_water_drop));
        listItems2.add(new BillServicesModel("Gas", R.drawable.ic_gas));
        listItems2.add(new BillServicesModel("Mobile", R.drawable.ic_bill));
        listItems2.add(new BillServicesModel("FASTag Recharge", R.drawable.ic_toll));
        listItems2.add(new BillServicesModel("Loan", R.drawable.ic_loan));
        listItems2.add(new BillServicesModel("CableTv", R.drawable.ic_cable_tv));
        listItems2.add(new BillServicesModel("Broadband", R.drawable.ic_wifi_signal));
        listItems2.add(new BillServicesModel("Landline", R.drawable.ic_landline));
        listItems2.add(new BillServicesModel("DTH", R.drawable.ic_tv_box));

        adapter2 = new BillServiceAdapter(listItems2, context, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                //Toast.makeText(context, "gf" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClicked(int position) {
                //Toast.makeText(context,"huuju", Toast.LENGTH_LONG).show();
            }
        });
        recyclerView2.setAdapter(adapter2);


    }


    private void loadRecylerViewData3() {
        recyclerView3 = root.findViewById(R.id.recyleministatement);
        //recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView3.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        recyclerView3.setLayoutManager(manager);

        listItems3 = new ArrayList<>();


        listItems3.add(new BillServicesModel("Balance Inquiry", R.drawable.ic_balance_inquiry));
        listItems3.add(new BillServicesModel("Mini Statement", R.drawable.ic_mini_statement));
        listItems3.add(new BillServicesModel("Aadhar Pay", R.drawable.ic_aadhar_pay));
        listItems3.add(new BillServicesModel("Cash Withdrawal", R.drawable.ic_case_withdrawal));
//        listItems3.add(new BillServicesModel("Cash Deposit", R.drawable.ic_cash_deposit));
//        listItems3.add(new BillServicesModel("Money Transfer", R.drawable.ic_domestic_monet_transfer));
//        listItems3.add(new BillServicesModel("Move to Bank", R.drawable.ic_balance_inquiry));

        adapter3 = new HomeBankingServicesAdapter(listItems3, context, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                //Toast.makeText(context, "gf" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClicked(int position) {
                //Toast.makeText(context,"huuju", Toast.LENGTH_LONG).show();
            }
        });
        recyclerView3.setAdapter(adapter3);

    }


    private void refreshToken(final String oldtoken) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "refreshToken",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsePincode", response);


                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());

                            int status = jsonObject1.optInt("status");
                            String message = jsonObject1.getString("message");

                            if (status == 1) {

                                int id = jsonObject1.optInt("id");
                                String email = jsonObject1.getString("email");
                                String name = jsonObject1.getString("name");
                                newtoken = jsonObject1.getString("token");

                                SharedPreferences preferences = getActivity().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
                                preferences.edit().putString("TOKEN", newtoken).apply();

                                loadRecylerViewData(newtoken);
                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Bearer " + newtoken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (oldtoken!=null && !oldtoken.isEmpty()){
            getWalletDetails();
        }
    }

    private void getWalletDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_URL + "userWallet/" + userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject dataobj = new JSONObject(response);
                            int status = dataobj.optInt("status");
                            if (status == 1) {

                            /*int id = dataobj.optInt("id");
                                String name = dataobj.optString("name");
                                String loginId = dataobj.optString("loginId");
                                double credit = dataobj.optDouble("credit");
                                double debit = dataobj.optDouble("debit");*/
                                String total_balance = dataobj.optString("total_balance");
                                if (!total_balance.isEmpty()) {
                                    tvWalletBal.setText(Constants.formatAmount(total_balance));
                                } else {
                                    tvWalletBal.setText(Constants.formatAmount("0.0"));
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer " + oldtoken);
                return headers;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}