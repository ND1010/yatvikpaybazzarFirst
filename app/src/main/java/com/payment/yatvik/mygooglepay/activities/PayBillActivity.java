package com.payment.yatvik.mygooglepay.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Adapter.BillServiceAdapter;
import com.payment.yatvik.mygooglepay.Helpers.ClickListener;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Model.BillServicesModel;
import com.payment.yatvik.mygooglepay.R;

import java.util.ArrayList;
import java.util.List;

public class PayBillActivity extends AppCompatActivity {

    // For Bill Payment Category listing on Homepage //
    private static final String BillPayUrl = Constants.BASE_URL+"getBillServices";
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter2;
    private List<BillServicesModel> listItems2;

    Context context;
    ImageButton backBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);

        backBtn = findViewById(R.id.backBtn);

        context = getApplicationContext();
        loadRecylerViewData2();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PayBillActivity.this,LoadingActivity.class));
            }
        });
    }

    private void loadRecylerViewData2() {
        recyclerView2 = findViewById(R.id.recylecatbill);
        //recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView2.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(manager);

        listItems2 = new ArrayList<>();


        listItems2.add(new BillServicesModel("Electricity",R.drawable.ic_light_bulb));
        listItems2.add(new BillServicesModel("Water",R.drawable.ic_water_drop));
        listItems2.add(new BillServicesModel("Gas",R.drawable.ic_gas));
        listItems2.add(new BillServicesModel("Mobile",R.drawable.ic_bill));
        listItems2.add(new BillServicesModel("FASTag Recharge",R.drawable.ic_toll));
        listItems2.add(new BillServicesModel("Loan",R.drawable.ic_loan));
        listItems2.add(new BillServicesModel("CableTv",R.drawable.ic_cable_tv));
        listItems2.add(new BillServicesModel("Broadband",R.drawable.ic_wifi_signal));
        listItems2.add(new BillServicesModel("Landline",R.drawable.ic_landline));
        listItems2.add(new BillServicesModel("DTH",R.drawable.ic_tv_box));

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


}