package com.payment.yatvik.mygooglepay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.payment.yatvik.mygooglepay.Helpers.ClickListener;
import com.payment.yatvik.mygooglepay.Model.CatListGetSet;
import com.payment.yatvik.mygooglepay.R;

import java.util.List;

public class MyAdapterCategory extends RecyclerView.Adapter<MyAdapterCategory.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private final ClickListener listener;
    private List<CatListGetSet> listItems;
    private Context context;


    public MyAdapterCategory(List<CatListGetSet> listItems, Context context, ClickListener listener) {
        this.listItems = listItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CatListGetSet listItem = listItems.get(position);

        holder.category_name.setText(listItem.getModule_slug());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, listItem.getModule_slug(), Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(context, CategoryProduct.class);
//                intent.putExtra("catproid",listItem.getId());
//                context.startActivity(intent);
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
