package com.payment.yatvik.mygooglepay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Helpers.ClickListener;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Model.BillServicesModel;
import com.payment.yatvik.mygooglepay.R;
import com.payment.yatvik.mygooglepay.activities.AepsBanking;
import com.payment.yatvik.mygooglepay.activities.ComingSoonActivity;
import com.payment.yatvik.mygooglepay.activities.DmrAddBeneficiary;
import com.payment.yatvik.mygooglepay.activities.PayoutRegisterForm;

import java.util.List;

public class HomeBankingServicesAdapter extends RecyclerView.Adapter<HomeBankingServicesAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private final ClickListener listener;
    private List<BillServicesModel> listItems;
    private Context context;


    public HomeBankingServicesAdapter(List<BillServicesModel> listItems, Context context, ClickListener listener) {
        this.listItems = listItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BillServicesModel listItem = listItems.get(position);

        holder.category_name.setText(listItem.getName());
        holder.category_image.setImageResource(listItem.getImgId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(context, listItem.getName(), Toast.LENGTH_SHORT).show();

            String cat = listItem.getName();
            if(cat.equals("Move to Bank")){
                context.startActivity(new Intent(context, PayoutRegisterForm.class));

            }
            else if(cat.equals("Money Transfer")){
                context.startActivity(new Intent(context, DmrAddBeneficiary.class));
            }else if(cat.equals("Balance Inquiry")||cat.equals("Mini Statement")||cat.equals("Aadhar Pay")||cat.equals("Cash Withdrawal")||cat.equals("Cash Deposit")){
                context.startActivity(new Intent(context, AepsBanking.class).putExtra(Constants.AEPS_SERVICE_FOR,cat));
            }
            else{
                context.startActivity(new Intent(context, ComingSoonActivity.class));
            }


            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "nnnnn", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView id;
        TextView category_name;
        ImageView category_image;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            category_name = itemView.findViewById(R.id.name);
            category_image = (ImageView) itemView.findViewById(R.id.image);

        }
    }
}
