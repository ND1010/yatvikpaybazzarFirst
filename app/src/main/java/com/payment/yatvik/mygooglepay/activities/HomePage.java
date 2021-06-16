package com.payment.yatvik.mygooglepay.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.ExpandableListAdapter;
import com.payment.yatvik.mygooglepay.Helpers.MenuModel;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Helpers.SessionHandler;
import com.payment.yatvik.mygooglepay.Helpers.User;
import com.payment.yatvik.mygooglepay.Model.MenuModelNav;
import com.payment.yatvik.mygooglepay.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private AppBarConfiguration mAppBarConfiguration;
    User user;
    SessionHandler session;
    String userID;
    NavigationView navigationView;
    TextView username;
    TextView usermail;
    ImageView userphoto;
    TextView tv_bal;
    Button add_money;
    String token;
    RequestQueue queue;
    Context context;

    List<String> menusList = new ArrayList<String>();

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    List<MenuModelNav> mExampleList;
    String menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        //Picasso.setSingletonInstance(new Picasso.Builder(context).build());

        tv_bal = findViewById(R.id.tv_bal);
        add_money = findViewById(R.id.btn_add_money);
        expandableListView = findViewById(R.id.expandableListView);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);

        username = headerView.findViewById(R.id.nav_username);
        usermail = headerView.findViewById(R.id.nav_useremail);
        userphoto = headerView.findViewById(R.id.nav_proflepic);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token = preferences.getString("TOKEN", null);//second parameter default value.
        menus = preferences.getString("MENUS",null);

        Log.d("menus----",menus);

        Gson gson = new Gson();
        Type type = new TypeToken<List<MenuModelNav>>(){}.getType();
        mExampleList = new ArrayList<>();
        mExampleList = gson.fromJson(menus, type);



        for (MenuModelNav cars : mExampleList){
            Log.i("CarData", cars.name);
            menusList.add(cars.name);

        }

        prepareMenuData();
        populateExpandableList();

        if (session.isLoggedIn()) {
            userID = user.getID();
            Log.d("userId-", userID);
            username.setText(user.getFullName());
            usermail.setText(user.getUsername());

            Glide.with(context).load(user.getProfilepic()).into(userphoto);

            //getWalletDetails();

        } else {
            username.setText("Not Logged in");
            tv_bal.setText("");
            //navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            //navigationView.getMenu().findItem(R.id.nav_changePassword).setVisible(false);
        }


        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSS");
        String currentDateTimeString = sdf.format(d);
        Log.d("time-", currentDateTimeString + "PP");

        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(HomePage.this, WalletTopUp.class));
            }
        });

        tv_bal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(HomePage.this, WalletActivity.class));
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_dashboard) {
            Toast.makeText(HomePage.this, "Dashboard", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_members) {
            finish();
            startActivity(new Intent(HomePage.this, MemberList.class));

        } else if (id == R.id.nav_profle) {
            Toast.makeText(HomePage.this, "Profile", Toast.LENGTH_SHORT).show();

        }/* else if (id == R.id.nav_aeps) {
            startActivity(new Intent(HomePage.this, MainActivity.class));

        }*/ else if (id == R.id.nav_billhistory) {
            Intent intentBill = new Intent(HomePage.this, BillsHistoryActivity.class);
            intentBill.putExtra(Constants.HISTORYTYPE,"BILL_HISTORY");
            startActivity(intentBill);
        } else if (id == R.id.nav_logout) {
            signOut();
            startActivity(new Intent(HomePage.this, Login.class));
            finishAffinity();
        } else if (id == R.id.nav_recharge) {
            startActivity(new Intent(HomePage.this, DmrAddBeneficiary.class));
        }

//        else if (id == R.id.nav_share ) {
//            Intent a = new Intent(Intent.ACTION_SEND);
//            //this is to get the app link in the playstore without launching your app.
//            final String appPackageName = getApplicationContext().getPackageName();
//            String strAppLink = "";
//            try {
//                strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
//            } catch (android.content.ActivityNotFoundException anfe) {
//                strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
//            }
//            // this is the sharing part
//            a.setType("text/link");
//            String shareBody = "Hey! Download Cheapflyfares Android App by clicking the link below" + "\nLink: "+strAppLink;
//            Log.d("sharebody",shareBody);
//
//            String shareSub = "Chaepflyfares Android App";
//            a.putExtra(Intent.EXTRA_SUBJECT, shareSub);
//            a.putExtra(Intent.EXTRA_TEXT, shareBody);
//            startActivity(Intent.createChooser(a, "Share Using"));
//            //savefile();
//        }

        else if (id == R.id.nav_billpayment) {

        } else if (id == R.id.nav_wallet) {
            finish();
            startActivity(new Intent(HomePage.this, WalletActivity.class));
        } else if (id == R.id.nav_payout) {
            finish();
            startActivity(new Intent(HomePage.this, PayoutReport.class));
            // Create Alert using Builder
//            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
//                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
//                    .setTitle("You've hit the limit")
//
//                    .setMessage("Looks like you've hit your a Request which is not Valid, Please Contact Our Customer Support for More Deatils and Booking!!")
//                    .addButton("Finish", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
//                        Toast.makeText(MainActivity.this, "Thanks!!", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    });
//            builder.setHeaderView(R.layout.cfdialog_imageview_header);
//            builder.show();

            //startActivity(new Intent(MainActivity.this, ForgetPasswordActivity.class));
        } else if (id == R.id.nav_payout_register) {
            finish();
            startActivity(new Intent(HomePage.this, PayoutRegisterForm.class));
        } else if (id == R.id.nav_changePassword) {
            startActivity(new Intent(HomePage.this, ChangePassword.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void signOut() {

        session.logoutUser();
        username.setText("Username");
        usermail.setText("emailid");
        userphoto.setImageResource(R.drawable.ic_user_solid);
        Toast.makeText(this, "Logged Out Successfully.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                            String message = dataobj.optString("message");

                            if (status == 1) {

                                int id = dataobj.optInt("id");
                                String name = dataobj.optString("name");
                                String loginId = dataobj.optString("loginId");
                                double credit = dataobj.optDouble("credit");
                                double debit = dataobj.optDouble("debit");
                                String total_balance = dataobj.optString("total_balance");

                                //tv_bal.setText("Balance\nRs " + total_balance);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomePage.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer " + token);
                return headers;
            }
        };

        MySingleton.getInstance(HomePage.this).addToRequestQueue(stringRequest);

    }


    private void prepareMenuData() {


        MenuModel menuModel = new MenuModel(R.drawable.ic_dashboard, "Dashboard", true, false, "HomePage.class"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        /*menuModel = new MenuModel(R.drawable.ic_user_solid, "Members", true, true, "MemberList"); //Menu of Java Tutorials
        headerList.add(menuModel);*/
        /*List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel(R.drawable.ic_dashboard, "Member List", false, false, "MemberList");
        childModelsList.add(childModel);*/


        /*if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }*/
        /*childModelsList = new ArrayList<>();
        menuModel = new MenuModel(R.drawable.ic_user_solid, "My Profile", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);*/

        /*childModel = new MenuModel(R.drawable.ic_dashboard, "Change Password", false, false, "ChangePassword");
        childModelsList.add(childModel);*/

        /*if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }*/

        List<MenuModel> childModelsList = new ArrayList<>();

        MenuModel childModel;
        if(menusList.contains("apes") || menusList.contains("aeps")) {

            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(R.drawable.ic_apes, "AEPS", true, true, ""); //Menu of Python Tutorials
            headerList.add(menuModel);
            /*childModel = new MenuModel(R.drawable.ic_dashboard, "Deposit History", false, false, "BillsHistoryActivity");
            childModelsList.add(childModel);*/

            childModel = new MenuModel(R.drawable.ic_dashboard, "AEPS History", false, false, "BillsHistoryActivity");
            childModelsList.add(childModel);

            if (menuModel.hasChildren) {
                childList.put(menuModel, childModelsList);
            }
        }

        if(menusList.contains("aadharPay")) {

            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(R.drawable.ic_recharge, "Aadhar Pay", true, true, ""); //Menu of Python Tutorials
            headerList.add(menuModel);
            /*childModel = new MenuModel(R.drawable.ic_dashboard, "Withdrawal  ", false, false, "AadharPay");
            childModelsList.add(childModel);*/

            childModel = new MenuModel(R.drawable.ic_dashboard, "Aadhar Pay History", false, false, "BillsHistoryActivity");
            childModelsList.add(childModel);

            if (menuModel.hasChildren) {
                childList.put(menuModel, childModelsList);
            }
        }

        /*if(menusList.contains("bill")) {
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(R.drawable.ic_bill, "Utilities Services", true, true, ""); //Menu of Python Tutorials
            headerList.add(menuModel);
            childModel = new MenuModel(R.drawable.ic_dashboard, "Pay Bills", false, false, "PayBillActivity");
            childModelsList.add(childModel);

            childModel = new MenuModel(R.drawable.ic_dashboard, "Bill History", false, false, "BillsHistoryActivity");
            childModelsList.add(childModel);

            if (menuModel.hasChildren) {
                childList.put(menuModel, childModelsList);
            }
        }*/

        /*childModelsList = new ArrayList<>();
        menuModel = new MenuModel(R.drawable.ic_wallet, "Wallet", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel(R.drawable.ic_dashboard, "Balance", false, false, "WalletActivity");
        childModelsList.add(childModel);

        childModel = new MenuModel(R.drawable.ic_dashboard, "Top Up", false, false, "WalletTopUp");
        childModelsList.add(childModel);*/

        /*if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }*/

        /*if(menusList.contains("dmr")) {
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(R.drawable.ic_dmr, "Domestic Money", true, true, ""); //Menu of Python Tutorials
            headerList.add(menuModel);
            childModel = new MenuModel(R.drawable.ic_dashboard, "DMR", false, false, "DmrAddBeneficiary");
            childModelsList.add(childModel);

            childModel = new MenuModel(R.drawable.ic_dashboard, "DMR Report", false, false, "DmrReport");
            childModelsList.add(childModel);

            if (menuModel.hasChildren) {
                childList.put(menuModel, childModelsList);
            }
        }*/


        /*if(menusList.contains("payout")) {

            childModelsList = new ArrayList<>();
            menuModel = new MenuModel(R.drawable.ic_payout, "Move to Bank", true, true, ""); //Menu of Python Tutorials
            headerList.add(menuModel);
            childModel = new MenuModel(R.drawable.ic_dashboard, "Payout", false, false, "PayoutRegisterForm");
            childModelsList.add(childModel);

            childModel = new MenuModel(R.drawable.ic_dashboard, "Payout Report", false, false, "PayoutReport");
            childModelsList.add(childModel);

            if (menuModel.hasChildren) {
                childList.put(menuModel, childModelsList);
            }
        }*/




        /*childModelsList = new ArrayList<>();
        menuModel = new MenuModel(R.drawable.ic_setting, "Setting", true, true, ""); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel(R.drawable.ic_dashboard, "Regestration Fees", false, false, "ComingSoonActivity");
        childModelsList.add(childModel);

        childModel = new MenuModel(R.drawable.ic_dashboard, "Rec. Commision", false, false, "ComingSoonActivity");
        childModelsList.add(childModel);

        childModel = new MenuModel(R.drawable.ic_dashboard, "PP Rec. Commision", false, false, "ComingSoonActivity");
        childModelsList.add(childModel);

        childModel = new MenuModel(R.drawable.ic_dashboard, "AEPS Commision", false, false, "ComingSoonActivity");
        childModelsList.add(childModel);

        childModel = new MenuModel(R.drawable.ic_dashboard, "AadharPay Setting", false, false, "ComingSoonActivity");
        childModelsList.add(childModel);

        childModel = new MenuModel(R.drawable.ic_dashboard, "DMR Surcharge", false, false, "ComingSoonActivity");
        childModelsList.add(childModel);

        childModel = new MenuModel(R.drawable.ic_dashboard, "Payout Surcharge", false, false, "ComingSoonActivity");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }*/

        menuModel = new MenuModel(R.drawable.ic_logout, "Logout", true, false, "signOut"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        if (headerList.get(groupPosition).url.equals("signOut")) {
                            signOut();
                            startActivity(new Intent(HomePage.this, Login.class));

                        }

                        String activityToStart = "com.payment.yatvik.mygooglepay.activities." + headerList.get(groupPosition).url;
                        try {
                            Class<?> c = Class.forName(activityToStart);
                            Intent intent = new Intent(HomePage.this, c);
                            startActivity(intent);
                        } catch (ClassNotFoundException ignored) {
                        }

                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    String activityToStart;
                    if(model.url.equals("MainActivity")){
                        activityToStart = "com.payment.yatvik.mygooglepay." + model.url;

                    }else {
                         activityToStart = "com.payment.yatvik.mygooglepay.activities." + model.url;
                    }

                    try {
                        Class<?> c = Class.forName(activityToStart);
                        Intent intent = new Intent(HomePage.this, c);
                        intent.putExtra(Constants.HISTORYTYPE,model.menuName);
                        startActivity(intent);
                    } catch (ClassNotFoundException ignored) {
                    }
                    onBackPressed();
                }

                return false;
            }
        });
    }



    private MenuModelNav getCarModel(String name){
        MenuModelNav cars = new MenuModelNav();
        cars.name = name;
        return cars;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //getWalletDetails();
    }
}
