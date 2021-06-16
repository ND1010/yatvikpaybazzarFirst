package com.payment.yatvik.mygooglepay.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.yatvik.mygooglepay.R;

public class ComingSoonActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.activity_coming_soon, null);
        final Button btn_verify = alertLayout.findViewById(R.id.btn_verify);
        final ImageView dialog_close = alertLayout.findViewById(R.id.dialog_close);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ComingSoonActivity.this,LoadingActivity.class));
            }
        });

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ComingSoonActivity.this,LoadingActivity.class));
                //dialog.dismiss();
            }
        });


    }
}