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

import com.payment.yatvik.mygooglepay.Model.SavedBeneficiaryModel;
import com.payment.yatvik.mygooglepay.R;
import com.payment.yatvik.mygooglepay.activities.DmrAddBeneficiary;

import java.util.ArrayList;
import java.util.List;

public class SavedBeneficiaryAdapter extends RecyclerView.Adapter<SavedBeneficiaryAdapter.AstroVH> implements Filterable {

    private static final String TAG = "AstrologerListAdapter";
    List<SavedBeneficiaryModel> astrologerModelList;
    List<SavedBeneficiaryModel> astrologerModelListFull;
    Context context;

    public SavedBeneficiaryAdapter(List<SavedBeneficiaryModel> astrologerModelList, Context context) {
        this.astrologerModelList = astrologerModelList;
        this.context = context;
        astrologerModelListFull = new ArrayList<>(astrologerModelList);
    }

    @NonNull
    @Override
    public AstroVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_saved_beneficiary,parent,false);
        context = view.getContext();
        return new AstroVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AstroVH holder, final int position) {
        final SavedBeneficiaryModel astrologerModel = astrologerModelList.get(position);

        holder.name.setText(astrologerModel.getBenifiary_name());
        holder.number.setText(astrologerModel.getMobile_number());
        holder.account.setText(astrologerModel.getBeneficiaryAccount());
        holder.ifsc.setText(astrologerModel.getBeneficiaryIFSC());

//        holder.transfer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Transfer to "+astrologerModelList.get(position).getBenifiary_name(), Toast.LENGTH_SHORT).show();
//            }
//        });

        holder.transfer.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof DmrAddBeneficiary) {
                    ((DmrAddBeneficiary)context).showTransferPopUp(astrologerModel.getBenifiary_name(),astrologerModel.getBeneficiaryAccount(),astrologerModel.getBeneficiaryIFSC(),astrologerModel.getId());
                }
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
            List<SavedBeneficiaryModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(astrologerModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SavedBeneficiaryModel item : astrologerModelListFull) {
                    if (item.getBenifiary_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    else if (item.getBenifiary_name().toLowerCase().contains(filterPattern)) {
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
        TextView name,number,bank,account,ifsc;
        Button transfer;

        public AstroVH(final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            number = itemView.findViewById(R.id.tv_mobile);
            bank = itemView.findViewById(R.id.tv_bank);
            account = itemView.findViewById(R.id.tv_account);
            ifsc = itemView.findViewById(R.id.tv_ifsc);
            transfer = itemView.findViewById(R.id.btn_transfer);
        }
    }
}
