package com.payment.yatvik.mygooglepay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Helpers.ClickListener;
import com.payment.yatvik.mygooglepay.Model.BillServicesModel;
import com.payment.yatvik.mygooglepay.R;
import com.payment.yatvik.mygooglepay.activities.SubCategory;

import java.util.List;

public class BillServiceAdapter extends RecyclerView.Adapter<BillServiceAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private final ClickListener listener;
    private List<BillServicesModel> listItems;
    private Context context;


    public BillServiceAdapter(List<BillServicesModel> listItems, Context context, ClickListener listener) {
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

                Intent intent = new Intent(context, SubCategory.class);
                String data = listItem.getName();
                intent.putExtra("catproid",data);
                context.startActivity(intent);
            }
        });
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
