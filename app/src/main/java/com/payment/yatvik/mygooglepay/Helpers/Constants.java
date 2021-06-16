package com.payment.yatvik.mygooglepay.Helpers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.text.NumberFormat;
import java.util.Locale;

public class Constants {
    //public static final String BASE_URL= "http://prince.nibble.co.in/api/";   -- for local test api
//    public static final String BASE_URL= "https://b2b.mygooglepe.com/api/";     //-- for live test api
    public static final String BASE_URL = "https://b2b.yatvik.com/api/";     //-- for live test api
    public static final String LOGIN_URL = "https://b2b.yatvik.com/api/androidlogin";
    public static final String JOIN_US_URL = "https://b2b.yatvik.com/api/join-us";
    public static final String AEPS_SERVICE_FOR = "aeps_service_for";
    public static final String HISTORYTYPE = "historytype";

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            redirectToPlayStore(context, packageName);
            return false;
        }
    }

    public static String formatAmount(String amount) {
        if (!TextUtils.isEmpty(amount)) {
            try {
                return formatAmount(Double.parseDouble(amount));
            } catch (NumberFormatException e) {
                return amount;
            }
        } else{
            return formatAmount(0.0);
        }
    }

    public static String formatAmount(Double amount) {
        Locale indianLocal = new Locale("en", "IN");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(indianLocal);
        return formatter.format(amount).trim();
    }

    private static void redirectToPlayStore(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public static boolean haveNetworkConnection(Context mContext) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}