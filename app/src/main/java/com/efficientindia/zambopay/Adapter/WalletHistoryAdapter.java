package com.efficientindia.zambopay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.efficientindia.zambopay.Model.WalletHistory;
import com.efficientindia.zambopay.R;

import java.util.ArrayList;
import java.util.List;

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.MyViewHolder>
        implements Filterable {


    private Context context;
    private List<WalletHistory> loadList;
    private List<WalletHistory> loadListFiltered;
    WalletHistoryAdapterListener listener;
    private PopupWindow pw;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTransId,txtDate,txtTransAmt,txtRemarks,txtNew;
        LinearLayout lnItem;
        CardView cardView;
        MyViewHolder(View view) {
            super(view);
            txtTransId = view.findViewById(R.id.txt_trans_id);
            txtTransAmt=view.findViewById(R.id.txt_cre_deb_trans);
            txtDate=view.findViewById(R.id.txt_date_trans);
            txtRemarks=view.findViewById(R.id.txt_remarks_trasns);
            txtNew=view.findViewById(R.id.txt_new_balance);
            lnItem=view.findViewById(R.id.ln_item_trans);
            cardView=view.findViewById(R.id.cardview_transaction);
        }
    }


    public WalletHistoryAdapter(Context context, List<WalletHistory> rechargeList, WalletHistoryAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.loadList = rechargeList;
        this.loadListFiltered = rechargeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recharge_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final WalletHistory contact = loadListFiltered.get(position);
        holder.txtDate.setText(contact.getDate().substring(0,10));
        holder.txtNew.setText(contact.getNewBalance());
        holder.txtRemarks.setText("Status : "+contact.getRemarks());
        holder.txtTransId.setText(contact.getTransactionId());

        if (contact.getType().equals("CREDIT")){
            holder.txtTransAmt.setTextColor(context.getResources().getColor(R.color.green));
            holder.txtTransAmt.setText("Cr. Rs."+contact.getAmount());
        }else{
            holder.txtTransAmt.setTextColor(context.getResources().getColor(R.color.red));
            holder.txtTransAmt.setText("Dr. Rs."+contact.getAmount());
        }

        if(position % 2 == 0){
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.card_color));
        }

        holder.lnItem.setOnClickListener(v -> {
            try {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                View layout = inflater.inflate(R.layout.popup_status,
                         v.findViewById(R.id.relative_element));
                pw = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
                pw.showAtLocation(v, Gravity.CENTER, 0, 0);
                pw.setFocusable(true);

                TextView txtStatus,txtDate,txtAmount,txtChargedAmount,txtClosingBalance;
                ImageView imgStatus=layout.findViewById(R.id.img_status);
                if (contact.getStatus().equalsIgnoreCase("SUCCESS")){
                    imgStatus.setImageResource(R.drawable.success);

                }else if (contact.getStatus().equalsIgnoreCase("Failed")){
                    imgStatus.setImageResource(R.drawable.failed);

                }else {
                    imgStatus.setImageResource(R.drawable.pending);
                }

                txtStatus=layout.findViewById(R.id.txt_status_reciept);
                txtDate=layout.findViewById(R.id.txt_date);
                txtAmount=layout.findViewById(R.id.txt_amount);
                txtChargedAmount=layout.findViewById(R.id.txt_charged_amount);
                txtClosingBalance=layout.findViewById(R.id.txt_closing_balance);

                txtStatus.setText("Status : "+contact.getStatus()+"\nTransaction Id : "+contact.getTransactionId());
                txtDate.setText("Date : "+contact.getDate().substring(0,10));
                txtAmount.setText("Amount : Rs."+contact.getAmount());
                txtChargedAmount.setText("Old balance : Rs."+contact.getOldBalance());
                txtClosingBalance.setText("New Balance : Rs."+contact.getNewBalance());

                ImageView btnCancel =  layout.findViewById(R.id.button_okay);

                btnCancel.setOnClickListener(view -> pw.dismiss());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public int getItemCount() {
        return loadListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    loadListFiltered = loadList;
                } else {
                    List<WalletHistory> filteredList = new ArrayList<>();
                    for (WalletHistory row : loadList) {

                        if (row.getTransactionId().toLowerCase().contains(charString.toLowerCase())
                                || row.getId().contains(charSequence)||row.getAmount().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    loadListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = loadListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                loadListFiltered = (ArrayList<WalletHistory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface WalletHistoryAdapterListener {
        void onContactSelected(WalletHistory contact);
    }
}
