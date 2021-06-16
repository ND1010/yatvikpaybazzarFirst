package com.payment.yatvik.mygooglepay.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Model.AepsTransactionResponse;
import com.payment.yatvik.mygooglepay.R;
import com.google.gson.Gson;

public class AepsTransactionRecieptActivity extends AppCompatActivity {

    private Button btn_done;
    private TextView tv_title;
    private TextView tv_balance;
    private TextView tv_orderId;
    private TextView tv_rrn;
    private TextView tv_date;
    private TextView tvAmount;
    private ImageButton backBtn;
    private String jsonTransaction;
    private AepsTransactionResponse transactionData;
    private String service_for = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps_transaction_reciept);
        initview();


    }

    private void initview() {
        btn_done = findViewById(R.id.btn_done);
        tv_title = findViewById(R.id.tv_title);
        tv_balance = findViewById(R.id.tv_balance);
        tv_orderId = findViewById(R.id.tv_orderId);
        tv_rrn = findViewById(R.id.tv_rrn);
        tv_date = findViewById(R.id.tv_date);
        tvAmount = findViewById(R.id.tvAmount);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        jsonTransaction = getIntent().getStringExtra("transaction");
        service_for = getIntent().getStringExtra("service_for");
        transactionData = new Gson().fromJson(jsonTransaction, AepsTransactionResponse.class);

        tv_orderId.setText(transactionData.getTxnId());
        tv_date.setText(transactionData.getTxnDate());
        tv_rrn.setText(transactionData.getRrn());

        if (transactionData != null) {
            switch (service_for) {
                case "Balance Inquiry":
                    tv_title.setText("Balance info successful.");
                    tvAmount.setText(Constants.formatAmount(transactionData.getBalanceAmount()));
                    tv_balance.setText(Constants.formatAmount(transactionData.getBalanceAmount()));
                    break;
                case "Aadhar Pay":
                    tv_title.setText("Aadhaar Pay Transaction successful.");
                    tvAmount.setText(Constants.formatAmount(transactionData.getAmount()));
                    tv_balance.setText(Constants.formatAmount(transactionData.getBalanceAmount()));
                    break;
                case "Cash Withdrawal":
                    tv_title.setText("Cash Withdrawal successful.");
                    tvAmount.setText(Constants.formatAmount(transactionData.getAmount()));
                    tv_balance.setText(Constants.formatAmount(transactionData.getBalanceAmount()));
                    break;
                case "Cash Deposit":
                    tv_title.setText("Cash Deposit successful.");
                    break;
            }
        }
    }

}