package com.payment.yatvik.mygooglepay.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Model.TransactionHistoryModel;
import com.payment.yatvik.mygooglepay.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.AstroVH> implements Filterable {

    private static final String TAG = "TransactionHistoryAdapter";
    List<TransactionHistoryModel> astrologerModelList;
    List<TransactionHistoryModel> astrologerModelListFull;

    public TransactionHistoryAdapter(List<TransactionHistoryModel> astrologerModelList) {
        this.astrologerModelList = astrologerModelList;
        astrologerModelListFull = new ArrayList<>(astrologerModelList);
    }

    @NonNull
    @Override
    public AstroVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transaction_history,parent,false);
        return new AstroVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AstroVH holder, int position) {
        TransactionHistoryModel astrologerModel = astrologerModelList.get(position);

        holder.id.setText("#"+astrologerModel.getId());
        holder.date.setText(astrologerModel.getCreated_at().substring(0,19));
        holder.method.setText(astrologerModel.getMethod());
        holder.method.setSelected(true);


        if(astrologerModel.getType()==1){
            holder.amount.setText("+"+astrologerModel.getAmount());
            holder.amount.setTextColor(Color.GREEN);
        }
        else if(astrologerModel.getType()==0){
            holder.amount.setText("-"+astrologerModel.getAmount());
            holder.amount.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return astrologerModelList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TransactionHistoryModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(astrologerModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TransactionHistoryModel item : astrologerModelListFull) {
                    if (item.getCreated_at().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    else if (item.getCreated_at().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            astrologerModelList.clear();
            astrologerModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class AstroVH extends RecyclerView.ViewHolder {

        private static final String TAG = "AstroVH";
        TextView id,method,date,amount;


        public AstroVH(final View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.tv_trans_id);
            method = itemView.findViewById(R.id.tv_trans_method);
            date = itemView.findViewById(R.id.tv_trans_datetime);
            amount = itemView.findViewById(R.id.tv_trans_amount);
        }
    }
}
