package com.payment.yatvik.mygooglepay.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Adapter.MiniStatementAdapter;
import com.payment.yatvik.mygooglepay.Helpers.ClickListener;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Model.AepsMiniStatementResponse;
import com.payment.yatvik.mygooglepay.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MiniStatementActivity extends AppCompatActivity {

    private final static String TAG = "MiniStatementActivity";
    private AepsMiniStatementResponse aepsMiniStatementResponse;
    private TextView tvMainWalletBalance;
    private TextView tvAgentNameValue;
    private TextView tvAgentAadharNoValue;
    private RecyclerView recyclerviewStatement;
    private String jsonTransaction = "";
    private String service_for;
    private AepsMiniStatementResponse transactionData;
    private ArrayList<AepsMiniStatementResponse.MiniStatement> arrayListMiniStatement;
    private LinearLayoutManager layoutManager;
    private MiniStatementAdapter miniBankStatementAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_statement);
        initview();

    }

    private void initview() {
        recyclerviewStatement = findViewById(R.id.recyclerviewStatement);
        tvMainWalletBalance = findViewById(R.id.tvMainWalletBalance);
        tvAgentNameValue = findViewById(R.id.tvAgentNameValue);
        tvAgentAadharNoValue = findViewById(R.id.tvAgentAadharNoValue);
        jsonTransaction = getIntent().getStringExtra("transaction");
        service_for = getIntent().getStringExtra("service_for");
        transactionData = new Gson().fromJson(jsonTransaction, AepsMiniStatementResponse.class);
        fillTheDetails();
    }

    private void fillTheDetails() {
        arrayListMiniStatement= new ArrayList<>();
        if (transactionData != null) {
            arrayListMiniStatement.addAll(transactionData.getMiniStatement());
        }
        if (transactionData != null) {
            tvMainWalletBalance.setText(Constants.formatAmount(transactionData.getAmount()));
            tvAgentNameValue.setText(transactionData.getRrn());
            tvAgentAadharNoValue.setText(Constants.formatAmount(transactionData.getAadhar()));

            layoutManager = new LinearLayoutManager(MiniStatementActivity.this, RecyclerView.VERTICAL, false);
            miniBankStatementAdapter = new MiniStatementAdapter(MiniStatementActivity.this, arrayListMiniStatement, new ClickListener() {
                @Override
                public void onPositionClicked(int position) {

                }

                @Override
                public void onLongClicked(int position) {

                }
            });
            recyclerviewStatement.setHasFixedSize(true);
            recyclerviewStatement.setLayoutManager(layoutManager);
            recyclerviewStatement.setAdapter(miniBankStatementAdapter);
        }
    }
}