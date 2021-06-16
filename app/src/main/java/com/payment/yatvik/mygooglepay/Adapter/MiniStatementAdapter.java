package com.payment.yatvik.mygooglepay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Helpers.ClickListener;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Model.AepsMiniStatementResponse;
import com.payment.yatvik.mygooglepay.R;

import java.util.List;

public class MiniStatementAdapter extends RecyclerView.Adapter<MiniStatementAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private final ClickListener listener;
    private List<AepsMiniStatementResponse.MiniStatement> listItems;
    private Context context;


    public MiniStatementAdapter(Context context,List<AepsMiniStatementResponse.MiniStatement> listItems, ClickListener listener) {
        this.listItems = listItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_mini_statement, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AepsMiniStatementResponse.MiniStatement listItem = listItems.get(position);

        holder.tvLedgerName.setText(listItem.getNarration());
        if (listItem.getTxnType().equals("Cr")) {
            holder.tvLedgerAmount.setText("+ " + Constants.formatAmount(Double.parseDouble(listItem.getAmount())));
            holder.tvLedgerAmount.setTextColor(ContextCompat.getColor(context, R.color.ppp_green));
        } else {
            holder.tvLedgerAmount.setText("- " + Constants.formatAmount(Double.parseDouble(listItem.getAmount())));
            holder.tvLedgerAmount.setTextColor(ContextCompat.getColor(context, R.color.bizzad_orange));
        }
        holder.tvLedgerDate.setText(listItem.getDate());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView id;
        TextView tvLedgerName;
        TextView tvLedgerAmount;
        TextView tvLedgerDate;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvLedgerName = itemView.findViewById(R.id.tvLedgerName);
            tvLedgerDate = itemView.findViewById(R.id.tvLedgerDate);
            tvLedgerAmount = itemView.findViewById(R.id.tvLedgerAmount);
        }
    }
}
