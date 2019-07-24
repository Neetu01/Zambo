package com.efficientindia.zambopay.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.efficientindia.zambopay.Activity.Dmt_Transfer;
import com.efficientindia.zambopay.Activity.SplashActivity;
import com.efficientindia.zambopay.AppConfig.AppConfig;
import com.efficientindia.zambopay.AppConfig.AppController;
import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.Constant;
import com.efficientindia.zambopay.AppConfig.HttpsTrustManager;
import com.efficientindia.zambopay.AppConfig.PrefManager;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.Model.BeneListDmt;
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class BeneficiaryDmtAdapter extends RecyclerView.Adapter<BeneficiaryDmtAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<BeneListDmt> loadList;
    private List<BeneListDmt> loadListFiltered;
    private BeneficiaryDmtAdapterListner listener;
    private ProgressDialog progressDialog;
    private String mobile;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtAccount,txtBank,txtIfsc;
        Button btnTransfer, btnVerify;
        CardView cardView;
        MyViewHolder(View view) {
            super(view);
            txtName=view.findViewById(R.id.txt_name_send);
            txtAccount=view.findViewById(R.id.txt_account);
            txtBank=view.findViewById(R.id.txt_bank);
            txtIfsc=view.findViewById(R.id.txt_ifsc);
            btnTransfer=view.findViewById(R.id.btn_transfer);
            btnVerify =view.findViewById(R.id.btn_verify_beneficiary);
            cardView=view.findViewById(R.id.cardview_u);
        }
    }


    public BeneficiaryDmtAdapter(Context context, List<BeneListDmt> contactList, BeneficiaryDmtAdapterListner listener) {
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
        final BeneListDmt contact = loadListFiltered.get(position);
        UserData userData=PrefManager.getInstance(context).getUserData();
        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        try{
            mobile= SplashActivity.getPreferences(Constant.MOBILE_SENDER,"");
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.txtName.setText(contact.getBenename());
        holder.txtAccount.setText("A/C No.: "+contact.getBeneaccno());
        holder.txtIfsc.setText("IFSC: "+contact.getIfsc());
        holder.txtBank.setText("Bank: "+contact.getBankname());
        if (contact.getStatus().equalsIgnoreCase("NV")){
            holder.btnVerify.setVisibility(View.VISIBLE);
            holder.btnTransfer.setVisibility(View.GONE);
        }else if (contact.getStatus().equalsIgnoreCase("V")){
            holder.btnVerify.setVisibility(View.GONE);
            holder.btnTransfer.setVisibility(View.VISIBLE);
        }else {
            holder.btnVerify.setVisibility(View.GONE);
            holder.btnTransfer.setVisibility(View.GONE);
        }
        if (position%2==0){
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.card_color));
        }
        holder.btnVerify.setOnClickListener(v -> {
            resendOtpOne(mobile,userData.getUserId(),contact.getBeneaccno());
        });
        holder.btnTransfer.setOnClickListener(v -> {
            final Dialog dialog=new Dialog(context);
            dialog.setTitle("Send to "+contact.getBenename());
            dialog.setContentView(R.layout.layout_amount_tranfer);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
           // final UserData userData= PrefManager.getInstance(context).getUserData();
            final RadioGroup radioGroup;
           // final RadioButton rbImps,rbNeft;
            radioGroup=dialog.findViewById(R.id.rg_type);
            TextView txtAcc=dialog.findViewById(R.id.txt_acc);
            TextView txtDialog=dialog.findViewById(R.id.dialog);
            txtDialog.setVisibility(View.GONE);
            txtAcc.setText("A/C No: "+contact.getBeneaccno());
            final TextView txtCancel=dialog.findViewById(R.id.txt_cancel);
            final EditText editAmount=dialog.findViewById(R.id.edittext_amount_view);
            Button btnSend1=dialog.findViewById(R.id.btn_send);
            radioGroup.setVisibility(View.GONE);
          /*  radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
                if (rbImps.isChecked()){
                    type="2";
                }
                if (rbNeft.isChecked()){
                    type="1";
                }
            });*/
            txtCancel.setOnClickListener(v1 -> dialog.dismiss());
            btnSend1.setOnClickListener(v12 -> {
                if (Configuration.hasNetworkConnection(context)){
                    String amount=editAmount.getText().toString().trim();
                    progressDialog = new ProgressDialog(context,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);


                    if (amount.isEmpty()){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                    "Enter amount");
                        }else {
                            Toast.makeText(context,"Enter amount",Toast.LENGTH_LONG).show();
                        }
                    }else if (Integer.valueOf(amount)<=0){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                    "Invalid amount");
                        }
                    }else if (Configuration.hasNetworkConnection(context)){
                        openDialog(userData.getUserId(),mobile,contact.getBeneaccno(),contact.getBenename(),contact.getIfsc(),
                                dialog,contact.getBankname(),contact.getBankid(),contact.getBenemobile(),contact.getId(),amount);

                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"internetError",
                                    "No internet Connection");
                        }else {
                            Toast.makeText(context,"check your internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"internetError",
                                "No internet Connection");
                    }else {
                        Toast.makeText(context, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        });
    }

    private void openDialog(String userId, String mobile, String beneaccno, String benename,
                            String ifsc, Dialog dialog, String bankname, String bankid,
                            String benemobile, String id, String amount) {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.layout_dialog_confirmation);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);

        TextView txtMessage=dialg.findViewById(R.id.txt_dialog);
        TextView btnCancel=dialg.findViewById(R.id.btn_cancel_dialog);
        TextView btnContinue=dialg.findViewById(R.id.btn_continue_dialog);
        txtMessage.setText(Html.fromHtml("Do you want to Transfer amount of "
                +"<font color='#000000'><b>\u20B9" + amount + "</b></font>"+
                " to "+"<font color='#000000'><br><b>" + benename +" (A/C No." +beneaccno+")."+ "</b></font>"+
                "<br> please continue for transfer otherwise cancel."));
        btnCancel.setOnClickListener(v12 -> dialg.dismiss());
        btnContinue.setOnClickListener(v1 -> {
            dialg.dismiss();
            sendMoney(userId,mobile,beneaccno,benename,ifsc, dialog,bankname,bankid,benemobile,id,amount);
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

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
                    List<BeneListDmt> filteredList = new ArrayList<>();
                    for (BeneListDmt row : loadList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBenename().toLowerCase().contains(charString.toLowerCase())
                                || row.getBeneaccno().contains(charSequence)||row.getBankname().contains(charSequence)) {
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
                loadListFiltered = (ArrayList<BeneListDmt>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface BeneficiaryDmtAdapterListner {
        void onBeneficiarySelcted(BeneListDmt beneficiaryList);
    }
    @SuppressLint("SetTextI18n")
    private void openPopup(String message, String status, String tid) {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_recharge);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_status_recharge);
        TextView txtStatus=dialg.findViewById(R.id.txt_status);
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
        TextView txtTransactionId=dialg.findViewById(R.id.txt_status_recharge);

        Button btnOk= dialg.findViewById(R.id.btn_okay);
       // txtTransactionId.setVisibility(View.GONE);

        txtTransactionId.setText(message);

        btnOk.setOnClickListener(v -> dialg.dismiss());

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
    private void openOtpBeneficiary(String userId, String mobile, String bankAccount) {

        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.layout_otp_bene);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);

        RelativeLayout imgClose =  dialg.findViewById(R.id.rl_close);
        TextView txtResendOtp=dialg.findViewById(R.id.txt_resend_sender_otp_bene);
        TextView txtMessage=dialg.findViewById(R.id.txt_otp_sender_bene);
        final EditText editTextOtp=dialg.findViewById(R.id.edittext_sender_otp_bene);
        final Button btnVerify=dialg.findViewById(R.id.btn_verify_bene);

        txtResendOtp.setOnClickListener(v -> {
            if (Configuration.hasNetworkConnection(context)){
                resendOtp(mobile,txtMessage,userId);
            }
        });
        btnVerify.setOnClickListener(v -> {
            String otp=editTextOtp.getText().toString();
            if (otp.isEmpty()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                            "Enter otp sent to your mobile no");
                }
            }else if (Configuration.hasNetworkConnection(context)){
                otpBeneVerify(userId,mobile,bankAccount,otp,dialg);
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"internetError",
                            "Enter otp sent to your mobile no");
                }
            }
        });

        imgClose.setOnClickListener(v -> dialg.dismiss());

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }

    private void resendOtp(final String mob, final TextView txtMessage, final String userId) {
        String tag_string_req = "resend_otp";
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom((Activity) context);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RESEND_OTP_DMT, response -> {
                    Log.d(TAG, "resend otp Response: " + response);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        if  (status.equals("1")) {
                            txtMessage.setText(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e(TAG, "resend otp Error: " + error.getMessage());
                    progressDialog.dismiss();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mob);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void otpBeneVerify(String userId, String mobile, String bankAccount, String otp, Dialog dialg) {
        String tag_string_req = "add_bene";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom((Activity) context);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.OTP_VERIFY_BENE, response -> {
                    Log.d(TAG, "Add Benificiary Response: " + response+" "+bankAccount);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        if  (status.equals("1")) {
                            dialg.dismiss();
                            //getBenificiaryList(mobile);
                            Dmt_Transfer.getSenderInfo(mobile,userId);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"",
                                        "Beneficiary successfully verified");
                            }
                            // openOtpBeneficiary(userId,mobile,bankAccount);
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                        message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e(TAG, "Add Benificiary Error: " + error.getMessage());
                    progressDialog.dismiss();
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.BANK_ACCOUNT,bankAccount);
                params.put(Constant.BENE_OTP,otp);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void resendOtpOne(String mobile, String userId, String beneaccno) {
        String tag_string_req = "resend_otp";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom((Activity) context);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RESEND_OTP_DMT, response -> {
            Log.d(TAG, "resend otp Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                if (status.equalsIgnoreCase("1")){
                    openOtpBeneficiary(userId,mobile,beneaccno);
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "resend otp Error: " + error.getMessage());
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void sendMoney(String userId, String mobile, String beneaccno, String benename,
                           String ifsc, Dialog dialog, String bankname, String bankid,
                           String benemobile, String id, String amount) {

        String tag_string_req = "transfer_money";
        progressDialog=new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Sending");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.TRANSFER_DMT, response -> {

                    Log.d(TAG, "transfer_money Response: " + response);
                    progressDialog.dismiss();
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        String tid = null;
                        if  (status.equals("1")) {
                            if (jsonObject1.has("tid")){
                                tid=jsonObject1.getString("tid");
                            }
                            WebService.getBalance(userId);
                            openPopup(message,status,tid);

                        }else {
                            WebService.getBalance(userId);
                            openPopup(message,status,tid);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                     Log.e(TAG, "transfer_money Error: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.RECIPIENT_ID,id);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.ACCOUNT,beneaccno);
                params.put(Constant.NAME,benename);
                params.put(Constant.TRANS_IFSC,ifsc);
                params.put(Constant.BANK_NAME_DMT,bankname);
                params.put(Constant.BANK_ID,bankid);
                params.put(Constant.BENE_MOBILE,benemobile);
                params.put(Constant.TRANS_AMOUNT,amount);
                params.put(Constant.SOURCE,"API");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

}
