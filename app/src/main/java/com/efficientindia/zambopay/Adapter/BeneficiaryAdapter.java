package com.efficientindia.zambopay.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.efficientindia.zambopay.AppConfig.AppConfig;
import com.efficientindia.zambopay.AppConfig.AppController;
import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.Constant;
import com.efficientindia.zambopay.AppConfig.HttpsTrustManager;
import com.efficientindia.zambopay.AppConfig.PrefManager;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.Model.BeneficiaryList;
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.R;
import com.efficientindia.zambopay.Activity.Transfer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<BeneficiaryList> loadList;
    private List<BeneficiaryList> loadListFiltered;
    private BeneficiaryListner listener;
    ProgressDialog progressDialog;
    private String type;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtAccount,txtBank,txtIfsc;
        Button btnTransfer,btnDeleteBene;
        CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            txtName=view.findViewById(R.id.txt_name_send);
            txtAccount=view.findViewById(R.id.txt_account);
            txtBank=view.findViewById(R.id.txt_bank);
            txtIfsc=view.findViewById(R.id.txt_ifsc);
            btnTransfer=view.findViewById(R.id.btn_transfer);
            btnDeleteBene=view.findViewById(R.id.btn_verify_beneficiary);
            cardView=view.findViewById(R.id.cardview_u);
        }
    }


    public BeneficiaryAdapter(Context context, List<BeneficiaryList> contactList, BeneficiaryListner listener) {
        this.context = context;
        this.listener = listener;
        this.loadList = contactList;
        this.loadListFiltered = contactList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_beneficiary, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final BeneficiaryList contact = loadListFiltered.get(position);
        holder.txtName.setText(contact.getNAME());
        holder.txtAccount.setText("A/C No.: "+contact.getACCOUNT());
        holder.txtIfsc.setText("IFSC: "+contact.getIFSC());
        holder.txtBank.setText("Bank: "+contact.getBANK());
        if (position%2==0){
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.card_color));
        }
        holder.btnDeleteBene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setMessage("Are you sure you want to delete beneficiary?")
                        .setTitle("Deactivate Beneficiary")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteBeneficiary(contact.getRECIPIENTID(),contact.getSenderMobileNo(),position,holder);
                            }
                        })
                        .setNegativeButton(" No ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(context);
                dialog.setTitle("Send to "+contact.getNAME());
                dialog.setContentView(R.layout.layout_amount_tranfer);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                final UserData userData=PrefManager.getInstance(context).getUserData();
                final RadioGroup radioGroup;
                final RadioButton rbImps,rbNeft;
                radioGroup=(RadioGroup)dialog.findViewById(R.id.rg_type);
                rbImps=(RadioButton)dialog.findViewById(R.id.radio_imps);
                rbNeft=(RadioButton)dialog.findViewById(R.id.radio_neft);
                TextView txtAcc=(TextView)dialog.findViewById(R.id.txt_acc);
                txtAcc.setText("A/C No: "+contact.getACCOUNT());
                final TextView txtCancel=(TextView)dialog.findViewById(R.id.txt_cancel);
                final EditText editAmount=(EditText)dialog.findViewById(R.id.edittext_amount_view);
                Button btnSend1=(Button)dialog.findViewById(R.id.btn_send);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (rbImps.isChecked()){
                            type="2";
                        }
                        if (rbNeft.isChecked()){
                            type="1";
                        }
                    }
                });
                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnSend1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Configuration.hasNetworkConnection(context)){
                            String amount=editAmount.getText().toString().trim();
                            progressDialog = new ProgressDialog(context,
                                    R.style.AppTheme_Dark_Dialog);
                            progressDialog.setIndeterminate(true);

                            if (radioGroup.getCheckedRadioButtonId()==-1){
                                Toast.makeText(context,"please select transfer type",Toast.LENGTH_LONG).show();
                            }else if (amount.isEmpty()){
                                Toast.makeText(context,"Enter amount",Toast.LENGTH_LONG).show();
                            }else if (Configuration.hasNetworkConnection(context)){
                                sendMoney(userData.getUserId(),contact.getACCOUNT(),amount,type,contact.getIFSC(),
                                        dialog,contact.getNAME(),contact.getRECIPIENTID(),contact.getSenderMobileNo());
                            }else {
                                Toast.makeText(context,"check your internet connection",Toast.LENGTH_LONG).show();
                            }//dialog.dismiss();
                        }else {
                            Toast.makeText(context, "No internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void sendMoney(final String userId, final String account, final String amount, final String type,
                           final String ifsc, final Dialog dialog, final String name, final String recipientid,
                           final String mobile) {
        String tag_string_req = "transfer_money";
        progressDialog=new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Sending");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.TRANSFER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Benificiary Response: " + response);
                progressDialog.dismiss();
                dialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String status = jsonObject1.getString("status");
                    String message=jsonObject1.getString("message");
                    String tid = null;
                    if (jsonObject1.has("tid")){
                        tid=jsonObject1.getString("tid");
                    }
                    WebService.getBalance(userId);
                    openPopup(message,status,tid);
                    if  (status.equals("1")) {
                       /* WebService.getBalance(userId);
                        openPopup(message,status);*/
                    }else if (status.equals("0")){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Benificiary Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.RECIPIENT_ID,recipientid);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.ACCOUNT,account);
                params.put(Constant.TRANS_AMOUNT,amount);
                params.put(Constant.TRANS_MODE,type);
                params.put(Constant.TRANS_IFSC,ifsc);
                params.put(Constant.NAME,name);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void deleteBeneficiary(final String recipientid,final String mobile, final int position, final MyViewHolder holder) {
        String tag_string_req = "delete_beneficiary";
        progressDialog=new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Deleting");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.DELETE_BENEFICIARY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Benificiary Response: " + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String status = jsonObject1.getString("status");
                    String message=jsonObject1.getString("message");
                    if  (status.equals("1")) {
                      //  deleteList(position);
                        holder.itemView.setVisibility(View.GONE);
                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                        Transfer.getBenificiaryList(mobile);
                        notifyDataSetChanged();
                    }else if (status.equals("0")){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Benificiary Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constant.RECIPIENT_ID,recipientid);
                params.put(Constant.SENDER_MOBILE,mobile);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void deleteList(int position) {
       // loadListFiltered.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, loadList.size());
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
                    List<BeneficiaryList> filteredList = new ArrayList<>();
                    for (BeneficiaryList row : loadList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNAME().toLowerCase().contains(charString.toLowerCase())
                                || row.getACCOUNT().contains(charSequence)||row.getBANK().contains(charSequence)) {
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
                loadListFiltered = (ArrayList<BeneficiaryList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface BeneficiaryListner {
        void onBeneficiarySelcted(BeneficiaryList beneficiaryList);
    }
    @SuppressLint("SetTextI18n")
    private void openPopup(String message, String status, String tid) {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_recharge);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView = (ImageView) dialg.findViewById(R.id.img_status_recharge);
        TextView txtStatus=(TextView)dialg.findViewById(R.id.txt_status);
        if (status.equalsIgnoreCase("1")){
            imageView.setImageResource(R.drawable.success);
            txtStatus.setText("Status : Success");
        }else if (status.equalsIgnoreCase("P")){
            imageView.setImageResource(R.drawable.pending);
            txtStatus.setText("Status : Pending");
        }else if (status.equals("0")){
            imageView.setImageResource(R.drawable.failed);
            txtStatus.setText("Status : Failed");
        }
        TextView txtTransactionId=(TextView)dialg.findViewById(R.id.txt_status_recharge);

        Button btnOk=(Button) dialg.findViewById(R.id.btn_okay);

        txtTransactionId.setText(message+"\n Trans. Id : "+tid);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialg.dismiss();
            }
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
}
