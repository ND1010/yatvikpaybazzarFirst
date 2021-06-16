package com.payment.yatvik.mygooglepay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Model.PayoutReportModel;
import com.payment.yatvik.mygooglepay.R;

import java.util.ArrayList;
import java.util.List;

public class PayoutReportAdapter extends RecyclerView.Adapter<PayoutReportAdapter.AstroVH> implements Filterable {

    private static final String TAG = "PayoutReportAdapter";
    List<PayoutReportModel> astrologerModelList;
    List<PayoutReportModel> astrologerModelListFull;
    Context context;

    public PayoutReportAdapter(List<PayoutReportModel> astrologerModelList) {
        this.astrologerModelList = astrologerModelList;
        astrologerModelListFull = new ArrayList<>(astrologerModelList);
    }

    @NonNull
    @Override
    public AstroVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_payout,parent,false);

        context = view.getContext();
        return new AstroVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AstroVH holder, final int position) {
        PayoutReportModel astrologerModel = astrologerModelList.get(position);

        holder.orderId.setText(astrologerModel.getOrderId());
        holder.status.setText(astrologerModel.getStatus());
        holder.date.setText(astrologerModel.getDate());
        holder.account.setText(astrologerModel.getAccout());
        holder.company.setText(astrologerModel.getCompany());
        holder.amount.setText(astrologerModel.getAmount());
        holder.message.setText(astrologerModel.getMessage());

        if(astrologerModel.getStatus().equals("PENDING")){
            holder.check.setVisibility(View.VISIBLE);
        }
        else {
            holder.check.setVisibility(View.GONE);
        }

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "id-"+astrologerModelList.get(position).getOrderId(), Toast.LENGTH_SHORT).show();
            }
        });
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
            List<PayoutReportModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(astrologerModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PayoutReportModel item : astrologerModelListFull) {
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

        TextView orderId,status,date,account,company,amount,message;
        Button check;


        public AstroVH(final View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.tv_orderId);
            status = itemView.findViewById(R.id.tv_status);
            date = itemView.findViewById(R.id.tv_date);
            account = itemView.findViewById(R.id.tv_acc_no);
            company = itemView.findViewById(R.id.tv_company);
            amount = itemView.findViewById(R.id.tv_amount);
            message =  itemView.findViewById(R.id.tv_message);
            check =  itemView.findViewById(R.id.btn_check);
        }
    }
}
