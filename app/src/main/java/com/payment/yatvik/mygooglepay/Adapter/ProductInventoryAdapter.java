package com.payment.yatvik.mygooglepay.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Model.ProductInventoryModel;
import com.payment.yatvik.mygooglepay.R;

import java.util.ArrayList;
import java.util.List;

public class ProductInventoryAdapter extends RecyclerView.Adapter<ProductInventoryAdapter.AstroVH> implements Filterable {

    private static final String TAG = "AstrologerListAdapter";
    List<ProductInventoryModel> astrologerModelList;
    List<ProductInventoryModel> astrologerModelListFull;

    public ProductInventoryAdapter(List<ProductInventoryModel> astrologerModelList) {
        this.astrologerModelList = astrologerModelList;
        astrologerModelListFull = new ArrayList<>(astrologerModelList);
    }

    @NonNull
    @Override
    public AstroVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_products,parent,false);
        return new AstroVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AstroVH holder, int position) {
        ProductInventoryModel astrologerModel = astrologerModelList.get(position);

        holder.id.setText("ID: "+astrologerModel.getId());
        holder.status.setText(astrologerModel.getStatus());
        holder.name.setText(astrologerModel.getMembername());
        holder.contact.setText(astrologerModel.getMobile());
        holder.packagename.setText(astrologerModel.getPackagename());
        holder.membertype.setText(astrologerModel.getMembertype());
        holder.owner.setText(astrologerModel.getOwner());
        holder.regdate.setText(astrologerModel.getRegdate());
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
            List<ProductInventoryModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(astrologerModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ProductInventoryModel item : astrologerModelListFull) {
                    if (item.getMembername().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    else if (item.getMembername().toLowerCase().contains(filterPattern)) {
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
        TextView status,id,name,contact,packagename,membertype,owner,regdate;


        public AstroVH(final View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id_val);
            status = itemView.findViewById(R.id.status_val);
            name = itemView.findViewById(R.id.name_val);
            contact = itemView.findViewById(R.id.contact_val);
            packagename = itemView.findViewById(R.id.package_val);
            membertype = itemView.findViewById(R.id.membertype_val);;
            owner =  itemView.findViewById(R.id.owner_val);
            regdate = itemView.findViewById(R.id.regdate_val);



        }
    }
}
