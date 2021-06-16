package com.payment.yatvik.mygooglepay.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class Preferences extends AppCompatActivity {

    public final static String PREF_NAME = "PrincePayPoint";
    public final static String WITH_LOGIN = "withlogin";

    public final static int MODE = 0;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor mEditor;

    public Preferences(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, MODE);
    }

    public void set(String k, String v) {
        mEditor = preferences.edit();
        mEditor.putString(k, v);
        mEditor.apply();
    }

    public void setLong(String k, Long v) {
        mEditor = preferences.edit();
        mEditor.putLong(k, v);
        mEditor.apply();
    }

    public void setInt(String k, Integer v) {
        mEditor = preferences.edit();
        mEditor.putLong(k, v);
        mEditor.apply();
    }

    public String get(String k) {
        return preferences.getString(k, "");
    }

    public Long getLong(String k) {
        return preferences.getLong(k, 0);
    }

    public Integer getInt(String k) {
        return preferences.getInt(k, 2);
    }

    public void setFirst(String k, boolean b) {
        mEditor = preferences.edit();
        mEditor.putBoolean(k, b);
        mEditor.apply();
    }

    /*public boolean cheackFirstLogin() {
        return preferences.getBoolean(First, true);
    }*/

    public void prefClear() {
        mEditor = preferences.edit();
        mEditor.clear();
        mEditor.apply();
    }


    public boolean isSession() {
        return preferences.getBoolean("withoutlogin", false);
    }

    public void setSession( boolean b) {
        preferences.edit().putBoolean("withoutlogin", b).apply();
    }

    public boolean ismanager() {
        return preferences.getBoolean("mngr", false);
    }

    public void setmanager( boolean b) {
        preferences.edit().putBoolean("mngr", b).apply();
    }

    public boolean isTl() {
        return preferences.getBoolean("TL", false);
    }

    public void setTl( boolean b) {
        preferences.edit().putBoolean("TL", b).apply();
    }

    public boolean isLoggedIn(){ return preferences.getBoolean("isLogin", false);}

    public void setLoggedIn( boolean b) {
        preferences.edit().putBoolean("isLogin", b).apply();
    }

}
