package com.payment.yatvik.mygooglepay.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.payment.yatvik.mygooglepay.Helpers.Constants;
import com.payment.yatvik.mygooglepay.Helpers.MySingleton;
import com.payment.yatvik.mygooglepay.Model.AepsHistoryResponse;
import com.payment.yatvik.mygooglepay.Model.BillsHistoryModel;
import com.payment.yatvik.mygooglepay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AepsHistoyAdapter extends RecyclerView.Adapter<AepsHistoyAdapter.AstroVH> implements Filterable {

    private static final String TAG = "AepsHistoyAdapter";
    List<AepsHistoryResponse.ApesHistory> astrologerModelList;
    List<BillsHistoryModel> astrologerModelListFull;
    Context context;
    String token;

    public AepsHistoyAdapter(List<AepsHistoryResponse.ApesHistory> astrologerModelList) {
        this.astrologerModelList = astrologerModelList;
        astrologerModelListFull = new ArrayList(astrologerModelList);
    }

    @NonNull
    @Override
    public AstroVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aeps_list_item_bill_history,parent,false);
        context = view.getContext();

        SharedPreferences preferences = context.getSharedPreferences("PrincePayPoint", Context.MODE_PRIVATE);
        token  = preferences.getString("TOKEN",null);

        return new AstroVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AstroVH holder, int position) {
        final AepsHistoryResponse.ApesHistory astrologerModel = astrologerModelList.get(position);

        holder.transId.setText(astrologerModel.getOrderId());
        holder.status.setText(astrologerModel.getTxnStatus());
        holder.date.setText(astrologerModel.getUpdatedAt());
        holder.service.setText(astrologerModel.getTransType());
        holder.operator.setText("AePS");
        holder.amount.setText(astrologerModel.getAmount());
        holder.mobile.setText(astrologerModel.getMobile());
        holder.message.setText(astrologerModel.getRrn());
        holder.btn_check.setVisibility(View.GONE);
        /*holder.tvLblMobile.setVisibility(View.GONE);
        holder.mobile.setVisibility(View.GONE);*/

        /*if(astrologerModel.getTxnStatus().equals("SUCCESS")){
            holder.btn_check.setVisibility(View.GONE);
        }
        else{
            holder.btn_check.setVisibility(View.VISIBLE);
        }

        holder.btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCheckStatus(astrologerModel.getOrderId());
            }
        });*/


    }

    private void getCheckStatus(String transId) {


        JSONObject request = new JSONObject();
        try {
            request.put("ref_id", transId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.BASE_URL+"checkBillStatus", request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginresponse-",response.toString());

                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());

                            int status = jsonObject1.optInt("status");
                            String message = jsonObject1.getString("message");

                            new SweetAlertDialog(context)
                                    .setTitleText("Status")
                                    .setContentText(""+message)
                                    .show();




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "bearer "+token);

                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);

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
            List<BillsHistoryModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(astrologerModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (BillsHistoryModel item : astrologerModelListFull) {
                    if (item.getTransId().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    else if (item.getTransId().toLowerCase().contains(filterPattern)) {
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
        TextView transId,status,date,service,operator,amount,mobile,message,tvLblMobile;
        Button btn_check;


        public AstroVH(final View itemView) {
            super(itemView);

            transId = itemView.findViewById(R.id.trans_id_val);
            status = itemView.findViewById(R.id.status_val);
            date = itemView.findViewById(R.id.date_val);
            tvLblMobile = itemView.findViewById(R.id.tvLblMobile);

            service = itemView.findViewById(R.id.service_val);
            operator = itemView.findViewById(R.id.operator_val);
            amount = itemView.findViewById(R.id.amount_val);
            mobile =  itemView.findViewById(R.id.mobile_val);
            message = itemView.findViewById(R.id.message_val);
            btn_check = itemView.findViewById(R.id.btn_check);

        }
    }
}
