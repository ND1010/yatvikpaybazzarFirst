package com.payment.yatvik.mygooglepay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Model.DmrReportModel;
import com.payment.yatvik.mygooglepay.R;

import java.util.ArrayList;
import java.util.List;

public class DmrReportAdapter extends RecyclerView.Adapter<DmrReportAdapter.AstroVH> implements Filterable {

    private static final String TAG = "DmrReportAdapter";
    List<DmrReportModel> astrologerModelList;
    List<DmrReportModel> astrologerModelListFull;
    Context context;

    public DmrReportAdapter(List<DmrReportModel> astrologerModelList) {
        this.astrologerModelList = astrologerModelList;
        astrologerModelListFull = new ArrayList<>(astrologerModelList);
    }

    @NonNull
    @Override
    public AstroVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dmr_report,parent,false);

        context = view.getContext();
        return new AstroVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AstroVH holder, final int position) {
        DmrReportModel astrologerModel = astrologerModelList.get(position);

        holder.orderId.setText(astrologerModel.getOrderId());
        holder.status.setText(astrologerModel.getStatus());
        holder.sendername.setText(astrologerModel.getSendername());
        holder.account.setText(astrologerModel.getAccount());
        holder.benename.setText(astrologerModel.getBeneficiaryname());
        holder.amount.setText("Rs "+astrologerModel.getAmount());
        holder.ifsc.setText(astrologerModel.getIfsc());

//        if(astrologerModel.getStatus().equals("PENDING")){
//            holder.check.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.check.setVisibility(View.GONE);
//        }
//
//        holder.check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "id-"+astrologerModelList.get(position).getOrderId(), Toast.LENGTH_SHORT).show();
//            }
//        });
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
            List<DmrReportModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(astrologerModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DmrReportModel item : astrologerModelListFull) {
                    if (item.getStatus().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    else if (item.getStatus().toLowerCase().contains(filterPattern)) {
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

        TextView amount,orderId,status,sendername,account,ifsc,benename;
        Button check;


        public AstroVH(final View itemView) {
            super(itemView);

            sendername = itemView.findViewById(R.id.tv_sender_name);
            benename = itemView.findViewById(R.id.tv_bene_name);
            ifsc= itemView.findViewById(R.id.tv_ifsc);
            account = itemView.findViewById(R.id.tv_bank_acc);
            amount = itemView.findViewById(R.id.tv_amount);
            status = itemView.findViewById(R.id.tv_status);
            orderId =  itemView.findViewById(R.id.tv_orderId);
        }
    }
}
