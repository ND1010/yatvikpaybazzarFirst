package com.payment.yatvik.mygooglepay;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

;import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


public class DialogAlert extends Dialog {

    private final Context mContext;
    private LayoutInflater layoutInflater;
    private View view;
    private Button btn_verify;
    private TextView tvMessage;
    private ImageView dialog_close;

    public DialogAlert(@NonNull Context context) {
        super(context);
        mContext = context;
        layoutInflater = LayoutInflater.from(context);

        view = layoutInflater.inflate(R.layout.activity_alert, null);
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(mContext, android.R.color.transparent));
        super.setContentView(view);
    }


    public void showDialog(String message, String btnName) {
        tvMessage = view.findViewById(R.id.tvMessage);
        dialog_close = view.findViewById(R.id.dialog_close);
        btn_verify = view.findViewById(R.id.btn_verify);
        tvMessage.setText(message);
        btn_verify.setText(btnName);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

}



