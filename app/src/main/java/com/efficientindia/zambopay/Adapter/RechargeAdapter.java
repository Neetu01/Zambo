package com.efficientindia.zambopay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.efficientindia.zambopay.Model.RechargeItem;
import com.efficientindia.zambopay.R;

import java.util.ArrayList;
import java.util.List;

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<RechargeItem> loadList;
    private List<RechargeItem> loadListFiltered;
    private RechargeAdapterListener listener;
    PopupWindow pw;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTransId,txtDate,txtTransAmt,txtRemarks,txtNew;
        LinearLayout lnItem;
        CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            txtTransId = (TextView)view.findViewById(R.id.txt_trans_id);
            txtTransAmt=(TextView)view.findViewById(R.id.txt_cre_deb_trans);
            txtDate=(TextView)view.findViewById(R.id.txt_date_trans);
            txtRemarks=(TextView)view.findViewById(R.id.txt_remarks_trasns);
            txtNew=(TextView)view.findViewById(R.id.txt_new_balance);
            lnItem=(LinearLayout)view.findViewById(R.id.ln_item_trans);
            cardView=(CardView)view.findViewById(R.id.cardview_transaction);
        }
    }


    public RechargeAdapter(Context context, List<RechargeItem> rechargeList, RechargeAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.loadList = rechargeList;
        this.loadListFiltered = rechargeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recharge_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final RechargeItem contact = loadListFiltered.get(position);
        holder.txtDate.setText(contact.getDate().substring(0,10));
        holder.txtNew.setText(contact.getAmount());
        holder.txtRemarks.setText("Status : "+contact.getStatus());
        holder.txtTransAmt.setText(contact.getNumber());
        holder.txtTransId.setText(contact.getTransId());

        if(position % 2 == 0){
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.card_color));
        }

        holder.lnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert inflater != null;
                    View layout = inflater.inflate(R.layout.popup_status,
                            (ViewGroup) v.findViewById(R.id.relative_element));
                    pw = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
                    pw.showAtLocation(v, Gravity.CENTER, 0, 0);
                    pw.setFocusable(true);

                    TextView txtStatus,txtDate,txtAmount,txtChargedAmount,txtClosingBalance;
                    ImageView imgStatus=(ImageView)layout.findViewById(R.id.img_status);
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

                    txtStatus.setText("Status : "+contact.getStatus()+"\nTransaction Id : "+contact.getTransId());
                    txtDate.setText("Date : "+contact.getDate().substring(0,10));
                    txtAmount.setText("Amount : Rs."+contact.getAmount());
                    txtChargedAmount.setText("Charged Amount : Rs."+contact.getChargedAmount());
                    txtClosingBalance.setText("Closing Balance : Rs."+contact.getClosingBalance());

                    ImageView btnCancel = (ImageView) layout.findViewById(R.id.button_okay);

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pw.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    List<RechargeItem> filteredList = new ArrayList<>();
                    for (RechargeItem row : loadList) {

                        if (row.getNumber().toLowerCase().contains(charString.toLowerCase())
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
                loadListFiltered = (ArrayList<RechargeItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface RechargeAdapterListener {
        void onContactSelected(RechargeItem contact);
    }
}
