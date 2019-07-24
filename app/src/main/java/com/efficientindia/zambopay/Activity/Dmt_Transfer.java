package com.efficientindia.zambopay.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.efficientindia.zambopay.Adapter.BeneficiaryDmtAdapter;
import com.efficientindia.zambopay.AppConfig.AppConfig;
import com.efficientindia.zambopay.AppConfig.AppController;
import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.Constant;
import com.efficientindia.zambopay.AppConfig.HttpsTrustManager;
import com.efficientindia.zambopay.AppConfig.PrefManager;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.Interface.Apiinterface;
import com.efficientindia.zambopay.Model.BeneListDmt;
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.volley.VolleyLog.TAG;

public class Dmt_Transfer extends AppCompatActivity implements View.OnClickListener,BeneficiaryDmtAdapter.BeneficiaryDmtAdapterListner {

        ImageView imgBack,imgNO;
        @SuppressLint("StaticFieldLeak")
        public static ProgressBar progressBar;
        @SuppressLint("StaticFieldLeak")
        public static TextView txtBalance;
        @SuppressLint("StaticFieldLeak")
        static RecyclerView requestList;
        @SuppressLint("StaticFieldLeak")
        private static RelativeLayout rlNoData;
        private Button btnAddBenef;
    @SuppressLint("StaticFieldLeak")
    private static Button btnAddSender;
    @SuppressLint("StaticFieldLeak")
    private static Button btnAllreadySender;
    static String limit="";
        TextView txtNo;
    @SuppressLint("StaticFieldLeak")
    private static TextView txtNoSender;
        private EditText editTextMobile,editTextAccount,ediBankIfsc,editBeneName;
        private static ProgressDialog progressDialog;
        @SuppressLint("StaticFieldLeak")
        private static RelativeLayout relativeLayoutrequestList;
        String mobile="";
        static List<BeneListDmt> listData;
        @SuppressLint("StaticFieldLeak")
        static BeneficiaryDmtAdapter loadListAdapter;
        @SuppressLint("StaticFieldLeak")
        static Activity activity;
        static String status;
        //  AutoCompleteTextView editbankName;
        String bankNam,bankAccount="",bankIfsccode,bankBeneName;
    private String bankId;
    private String bankIfsc;
    SwipeRefreshLayout swipeRefreshLayout;
    Button moneytran_1,moneytran_2,moneytran_3,moneytran_4;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmt__transfer);
        relativeLayout=findViewById(R.id.rl_user);
        imgBack=findViewById(R.id.imgback_transfer);

        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        moneytran_1=findViewById(R.id.moneytransfer_1);
        moneytran_2=findViewById(R.id.moneytransfer_2);
        moneytran_3=findViewById(R.id.moneytransfer_3);
        moneytran_4=findViewById(R.id.moneytransfer_4);
        progressBar=findViewById(R.id.prog_transfer);
        imgNO=findViewById(R.id.img_no_beneficiary);
        txtBalance=findViewById(R.id.txt_balance_transfer);
        requestList=findViewById(R.id.recyclerview_transfer);
        rlNoData=findViewById(R.id.rl_no_beneficiary);
        btnAddBenef=findViewById(R.id.btn_add_benef);
        txtNo=findViewById(R.id.txt_no_beneficiary);
        txtNoSender=findViewById(R.id.txt_sender_info);
        activity= Dmt_Transfer.this;
        btnAddSender=findViewById(R.id.btn_add_sender);
        relativeLayoutrequestList=findViewById(R.id.relative_request_list);
        btnAllreadySender=findViewById(R.id.btn_add_allready_sender);
        btnAddSender.setOnClickListener(this);
        editTextMobile=findViewById(R.id.edittext_user_mobile);
        progressDialog=new ProgressDialog(Dmt_Transfer.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        btnAddBenef.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        moneytran_1.setOnClickListener(this);
        moneytran_2.setOnClickListener(this);
        moneytran_3.setOnClickListener(this);
        moneytran_4.setOnClickListener(this);
        btnAllreadySender.setOnClickListener(this);
        relativeLayoutrequestList.setVisibility(View.GONE);
        final UserData userData= PrefManager.getInstance(Dmt_Transfer.this).getUserData();
        if (Configuration.hasNetworkConnection(Dmt_Transfer.this)){
            WebService.getBalance(userData.getUserId());
            WebService.getActiveService(userData.getUserId(), userData.getuType());
        }else {
            rlNoData.setVisibility(View.VISIBLE);
            imgNO.setImageResource(R.drawable.nointernet);
            txtNo.setVisibility(View.VISIBLE);
            txtNo.setText(R.string.no_internet);
            btnAddBenef.setVisibility(View.GONE);
        }
       // Log.d("TAG",""+userData.getUserId());
        getOutletstatus(userData.getUserId(),userData.getMobile());
        Log.d("TAG","uuid"+userData.getUserId());
        editTextMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnAddSender.setVisibility(View.GONE);
                txtNoSender.setVisibility(View.GONE);
                rlNoData.setVisibility(View.GONE);
                relativeLayoutrequestList.setVisibility(View.GONE);
              /*  if (mobile.length()>10||mobile.length()<10){
                    Toast.makeText(Transfer.this, "Invalid Mobile number", Toast.LENGTH_SHORT).show();
                }else {
                    getSenderInfo(mobile);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                mobile =editTextMobile.getText().toString().trim();
                if (mobile.length()==10){
                    if(status.equals("0"))
                    getSenderInfo(mobile,userData.getUserId());
                    else
                    {
                        Toast.makeText(Dmt_Transfer.this, "User not Registered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    mobile =editTextMobile.getText().toString().trim();
                    if (mobile.length()==10){
                        doYourUpdate(mobile,userData.getUserId());
                    }
                }
        );
        listData = new ArrayList<>();
        loadListAdapter = new BeneficiaryDmtAdapter(activity, listData, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        requestList.setLayoutManager(mLayoutManager);
        requestList.setItemAnimator(new DefaultItemAnimator());
        requestList.setAdapter(loadListAdapter);
        editTextMobile.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            mobile =editTextMobile.getText().toString().trim();

            if (actionId == EditorInfo.IME_ACTION_SEND||actionId == EditorInfo.IME_ACTION_GO||actionId ==EditorInfo.IME_ACTION_DONE) {
                if (mobile.length()<10||mobile.length()>10){
                    Toast.makeText(Dmt_Transfer.this, "Invalid Mobile number", Toast.LENGTH_SHORT).show();
                }else {
                    // mobile.length();
                    getSenderInfo(mobile, userData.getUserId());
                }
                handled = true;
            }
            return handled;
        });
    }
    private void doYourUpdate(String mobile, String userId) {
        // TODO implement a refresh
        swipeRefreshLayout.setRefreshing(false); // Disables the refresh icon
        if (this.mobile.length()==10){
            getSenderInfo(mobile,userId);
        }
    }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onClick(View v) {
            final UserData userData=PrefManager.getInstance(Dmt_Transfer.this).getUserData();

            if(v==moneytran_1)
            {
                WebService.getActiveService(userData.getUserId(),userData.getuType());
                try{
                    if (SplashActivity.getPreferences(Constant.DMT_1,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getBaseContext(),R.style.Dialod_UpDown,"error",
                                "This service is not available, we working on it\nplease try after sometime");
                    }else {
                        Configuration.openPopupUpDown(getBaseContext(),R.style.Dialod_UpDown,"error",
                                "This service is not available, we working on it\nplease try after sometime");

                        moneytran_1.setVisibility(View.GONE);
                        moneytran_2.setVisibility(View.GONE);
                        moneytran_3.setVisibility(View.GONE);
                        moneytran_4.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }if(v==moneytran_2)
            {
                WebService.getActiveService(userData.getUserId(),userData.getuType());
                try{
                    if (SplashActivity.getPreferences(Constant.DMT_2,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getBaseContext(),R.style.Dialod_UpDown,"error",
                                "This service is not available, we working on it\nplease try after sometime");
                    }else {
                        moneytran_1.setVisibility(View.GONE);
                        moneytran_2.setVisibility(View.GONE);
                        moneytran_3.setVisibility(View.GONE);
                        moneytran_4.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }if(v==moneytran_3)
            {
                WebService.getActiveService(userData.getUserId(),userData.getuType());
                try{
                    if (SplashActivity.getPreferences(Constant.DMT_3,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getBaseContext(),R.style.Dialod_UpDown,"error",
                                "This service is not available, we working on it\nplease try after sometime");
                    }else {
                        moneytran_1.setVisibility(View.GONE);
                        moneytran_2.setVisibility(View.GONE);
                        moneytran_3.setVisibility(View.GONE);
                        moneytran_4.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }if(v==moneytran_4)
            {
                WebService.getActiveService(userData.getUserId(),userData.getuType());
                try{
                    if (SplashActivity.getPreferences(Constant.DMT_4,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getBaseContext(),R.style.Dialod_UpDown,"error",
                                "This service is not available, we working on it\nplease try after sometime");
                    }else {
                        moneytran_1.setVisibility(View.GONE);
                        moneytran_2.setVisibility(View.GONE);
                        moneytran_3.setVisibility(View.GONE);
                        moneytran_4.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (v==imgBack){
                new AlertDialog.Builder(Dmt_Transfer.this)
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to exit from Money Tranfer?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            WebService.getActiveService(userData.getUserId(), userData.getuType());
                            Intent intent=new Intent(Dmt_Transfer.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                            finish();
                        }).create().show();
            }
            if (v==btnAddBenef||v==btnAllreadySender){
                final Dialog dialg=new Dialog(Dmt_Transfer.this);
                dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialg.setContentView(R.layout.layout_add_benef);
                dialg.setCanceledOnTouchOutside(false);
                dialg.setCancelable(false);
                TextView txtClose =  dialg.findViewById(R.id.txt_close);
                // editbankName =dialg.findViewById(R.id.edit_benf_bank);
                editTextAccount=dialg.findViewById(R.id.edit_acc_benef);
                ediBankIfsc=dialg.findViewById(R.id.edit_ifsc_benef);
                editBeneName=dialg.findViewById(R.id.edit_name_benef);
                Button btnAdd=dialg.findViewById(R.id.btn_add_ben);
                AutoCompleteTextView bankName=dialg.findViewById(R.id.edit_benf_bank);
                TextView txtVerifyBene=dialg.findViewById(R.id.txt_verify);
                //  bankNam= editbankName.getText().toString();
                WebService.getBankDmt2(Dmt_Transfer.this,progressDialog,bankName,userData.getUserId());

                bankName.setOnItemClickListener((parent, view, position, id) -> {
                    String bank =parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < WebService.bankName.size(); i++) {
                        if (WebService.bankName.get(i).equals(bank)) {
                            bankId = WebService.bankCode.get(i);
                            bankIfsc=WebService.masterIfsc.get(i);
                            bankName.setText(WebService.bankName.get(i));
                            if (!WebService.masterIfsc.get(i).equalsIgnoreCase("NULL")){
                                ediBankIfsc.setText(WebService.masterIfsc.get(i));
                            }else {
                                ediBankIfsc.setText("");
                            }
                           // Log.d(TAG, "BN--" + bankName.getText().toString() + " CD-->" + bankId);
                            try{Configuration.hideKeyboardFrom(Dmt_Transfer.this);}catch (Exception e){e.printStackTrace();}
                          //  getBankDetail(code);
                        }
                    }
                    System.out.println("Bank code-->" + bank + "code---->" + bankId+" ifsc:>"+bankIfsc);
                });
                bankName.setOnTouchListener((paramView, paramMotionEvent) -> {
                    // TODO Auto-generated method stub
                    bankName.showDropDown();
                    bankName.requestFocus();
                    return false;
                });


                txtVerifyBene.setOnClickListener(v16 -> {
                    bankAccount=editTextAccount.getText().toString();
                    bankIfsccode=ediBankIfsc.getText().toString();
                    bankNam=bankName.getText().toString();
                    mobile=editTextMobile.getText().toString();
                    bankBeneName=editBeneName.getText().toString();
                if (bankNam.isEmpty()){
                    bankName.setError("Enter bank name");
                }else if (bankAccount.isEmpty()){
                        editTextAccount.setError("Enter account number");
                    }else if (bankIfsccode.isEmpty()){
                        ediBankIfsc.setError("Enter IFSC");
                    }else if (Configuration.hasNetworkConnection(Dmt_Transfer.this)){
                        verifyBeneficiary(userData.getUserId(),bankBeneName,bankAccount,bankIfsccode,mobile,bankId,dialg,editBeneName);
                    }else {
                        Toast.makeText(Dmt_Transfer.this, "No internet connectivity", Toast.LENGTH_SHORT).show();
                    }
                });
                btnAdd.setOnClickListener(v15 -> {
                    bankAccount=editTextAccount.getText().toString();
                    bankIfsccode=ediBankIfsc.getText().toString();
                    bankBeneName=editBeneName.getText().toString();
                    bankNam=bankName.getText().toString();
                    if (bankNam.isEmpty()){
                        bankName.setError("Enter bank name");
                    }else if (bankAccount.isEmpty()){
                        editTextAccount.setError("Enter account number");
                    }else if (bankIfsccode.isEmpty()){
                        ediBankIfsc.setError("Enter IFSC");
                    }else if (bankBeneName.isEmpty()){
                        editBeneName.setError("Enter Benificiary Name");
                    }else if (Configuration.hasNetworkConnection(Dmt_Transfer.this)){
                        addBeneficiary(userData.getUserId(),bankBeneName,bankAccount,bankIfsccode,mobile,bankId,dialg);
                    }else {
                        Toast.makeText(Dmt_Transfer.this, "No internet connectivity", Toast.LENGTH_SHORT).show();
                    }
                });
                txtClose.setOnClickListener(v13 -> dialg.dismiss());
                dialg.show();
                Window window = dialg.getWindow();
                assert window != null;
                window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            }
            if (v==btnAddSender){
                final Dialog dialg=new Dialog(Dmt_Transfer.this);
                dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialg.setContentView(R.layout.layout_add_sender_dmt);
                dialg.setCanceledOnTouchOutside(false);
                dialg.setCancelable(false);
                RelativeLayout imgClose =  dialg.findViewById(R.id.rl_close);
                //final LinearLayout linearSenderInfo=dialg.findViewById(R.id.linear_sender_info);
                final TextView txtMessage=dialg.findViewById(R.id.txt_otp_sender);
                TextView txtResendOtp=dialg.findViewById(R.id.txt_resend_sender_otp);
                final EditText editMobileno=dialg.findViewById(R.id.edittext_sender_mobile);
                final EditText editTextNameSender=dialg.findViewById(R.id.edittext_sender_name);
                final EditText editTextSenderOtp=dialg.findViewById(R.id.edittext_sender_otp);
                final EditText editTextLastName=dialg.findViewById(R.id.edittext_sender_lastname);
                final Button btnAddSn=dialg.findViewById(R.id.btn_add_name_sender);
               // Button btnVerifyOtp=dialg.findViewById(R.id.btn_verify_sender_otp);
                editMobileno.setText(mobile);
              //  editTextNameSender.setText(customerName);
                final String mob=editMobileno.getText().toString().trim();

                btnAddSn.setOnClickListener(v14 -> {
                    final String otp1=editTextSenderOtp.getText().toString();
                    String name=editTextNameSender.getText().toString();
                    String lastName=editTextLastName.getText().toString();
                    if (mob.isEmpty()){
                        editMobileno.setError("Enter sender mobile");
                    }else if (otp1.isEmpty()){
                        editTextSenderOtp.setError("Enter otp sent to your mobile number");
                    }else if (name.isEmpty()) {
                        editTextNameSender.setError("Enter Sender name");
                    }else if (Configuration.hasNetworkConnection(Dmt_Transfer.this)){
                        createSender(userData.getUserId(),mob,name,otp1,dialg,lastName);
                    }
                });
                txtResendOtp.setOnClickListener(v1 -> {
                    if (Configuration.hasNetworkConnection(Dmt_Transfer.this)){
                        resendOtp(mob,txtMessage,userData.getUserId());
                    }
                });

                imgClose.setOnClickListener(v12 -> dialg.dismiss());
                dialg.show();
                Window window = dialg.getWindow();
                assert window != null;
                window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            }

        }

    private void resendOtp(final String mob, final TextView txtMessage, final String userId) {
        String tag_string_req = "resend_otp";
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Dmt_Transfer.this);
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
            Toast.makeText(Dmt_Transfer.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void createSender(final String userId, final String mobile, final String name, final String otp, Dialog dialg,
                              String lastName) {
        String tag_string_req = "create_sender";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Dmt_Transfer.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.CREATE_SENDER_DMT, response -> {
                    Log.d(TAG, "CREATE Response: " + response+" "+name+" "+mobile+" "+otp);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        if  (status.equals("1")) {
                            dialg.dismiss();
                            getSenderInfo(mobile,userId);
                            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(Dmt_Transfer.this,R.style.Dialod_UpDown,"",
                                        message);
                            }*/
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(Dmt_Transfer.this,R.style.Dialod_UpDown,"error",
                                        message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e(TAG, "Create Error: " + error.getMessage());
            Toast.makeText(Dmt_Transfer.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.CUST_FIRSTNAME,name);
                params.put(Constant.CUST_LASTTNAME,lastName);
                params.put(Constant.SENDER_OTP,otp);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void getOutletstatus(final String userId,String mobile) {
        String tag_string_req = "sender_info";
        Log.d("TAG","TAGUSERiD"+userId);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(activity);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.outletcheckstatus, response -> {
            Log.d(TAG, "SENDER OUTLET STATUS: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                if  (status.equals("0")) {
                    getSenderInfo(userId,mobile);

                }
                else if (status.equals("1")) {
                    Log.d("TAG","1userId"+userId);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "SENDER INFO Error: " + error.getMessage());
           /* Toast.makeText(Dmt_Transfer.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();*/
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(Constant.U_UID,userId);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void getSenderInfo(final String mobile, final String userId) {
        String tag_string_req = "sender_info";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(activity);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_SENDER_INFO_DMT, response -> {
                    Log.d(TAG, "SENDER INFO Response: " + response);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        if  (status.equals("0")) {
                            getSenderlimit(mobile,userId);
                            getbenificary(mobile,userId);
                            txtNoSender.setVisibility(View.VISIBLE);
                            txtNoSender.setText(limit);
                            if (jsonObject1.has("beneList")) {
                                SplashActivity.savePreferences(Constant.MOBILE_SENDER,mobile);
                                Object list = jsonObject1.get("beneList");
                                JSONArray jsonArray;
                                JSONObject jsonObject;
                                if (list instanceof JSONArray) {
                                    rlNoData.setVisibility(View.GONE);
                                    relativeLayoutrequestList.setVisibility(View.VISIBLE);
                                    requestList.setVisibility(View.VISIBLE);
                                    jsonArray = (JSONArray) list;
                                    List<BeneListDmt> items = new Gson().fromJson(jsonArray.toString(),
                                            new TypeToken<List<BeneListDmt>>() {
                                            }.getType());
                                    listData.clear();
                                    listData.addAll(items);
                                    loadListAdapter.notifyDataSetChanged();

                                } else if (list instanceof JSONObject) {
                                    rlNoData.setVisibility(View.GONE);
                                    relativeLayoutrequestList.setVisibility(View.VISIBLE);
                                    requestList.setVisibility(View.VISIBLE);
                                    jsonObject = (JSONObject) list;
                                    /*List<BeneListDmt> items = new Gson().fromJson(jsonObject.toString(),
                                            new TypeToken<List<BeneListDmt>>() {
                                            }.getType());*/

                                    BeneListDmt beneficiaryList = new Gson().fromJson(jsonObject.toString(), BeneListDmt.class);

                                    listData.clear();
                                    listData.addAll(Collections.singleton(beneficiaryList));
                                    loadListAdapter.notifyDataSetChanged();
                                }else {
                                    rlNoData.setVisibility(View.VISIBLE);
                                    relativeLayoutrequestList.setVisibility(View.VISIBLE);
                                    requestList.setVisibility(View.GONE);
                                }
                            }
                        }else if (status.equalsIgnoreCase("3")){
                            txtNoSender.setVisibility(View.VISIBLE);
                            txtNoSender.setText(jsonObject1.getString("customerDetail"));
                            relativeLayoutrequestList.setVisibility(View.VISIBLE);
                            rlNoData.setVisibility(View.VISIBLE);
                            requestList.setVisibility(View.VISIBLE);
                            btnAllreadySender.setVisibility(View.GONE);
                        }else if (status.equalsIgnoreCase("2")){
                            txtNoSender.setVisibility(View.VISIBLE);
                            txtNoSender.setText(message);
                            btnAddSender.setVisibility(View.VISIBLE);

                        }else if (status.equalsIgnoreCase("0")){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDownBack(activity,R.style.Dialod_UpDown,"main","error",message+
                                        ", Thanks");
                            }
                            txtNoSender.setVisibility(View.VISIBLE);
                            btnAddSender.setVisibility(View.VISIBLE);
                            txtNoSender.setText(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e(TAG, "SENDER INFO Error: " + error.getMessage());
           /* Toast.makeText(Dmt_Transfer.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();*/
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.U_UID,userId);
                Log.e(TAG,"SENDR INF--->"+Constant.SENDER_MOBILE+"=="+mobile);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void getSenderlimit(String mobile, String userId)
    {
        String tag_string_req = "sender_limit";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(activity);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_SENDER_LIMIT, response -> {
                Log.d(TAG, "SENDER LIMIT RESPONSE " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
              Log.d("TAG","statusgetben"+status);
                String message=jsonObject1.getString("message");


               if(status.equals("0"))
               {
                    limit=jsonObject1.getString("customerData");
                  //  Log.d("TAG","limit0"+limit);
               }else{
                   //Log.d("TAG","limit1"+limit);
               }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "SENDER INFO Error: " + error.getMessage());
           /* Toast.makeText(Dmt_Transfer.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();*/
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.U_UID,userId);
                Log.e(TAG,"SENDR INF--->"+Constant.SENDER_MOBILE+"=="+mobile);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    public static void getbenificary(final String mobile,final String userId) {
        String tag_string_req = "sender_info";
        Log.d("TAG","TAGUSERiD"+userId);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(activity);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.outletcheckstatus, response -> {
            Log.d(TAG, "SENDER OUTLET STATUS: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                if  (status.equals("0")) {


                }
                else if (status.equals("1")) {
                    Log.d("TAG","1userId"+userId);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "SENDER INFO Error: " + error.getMessage());
           /* Toast.makeText(Dmt_Transfer.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();*/
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(Constant.U_UID,userId);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


     @Override
        public void onBackPressed() {
            new AlertDialog.Builder(Dmt_Transfer.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit from Money Tranfer?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                        UserData userData=PrefManager.getInstance(Dmt_Transfer.this).getUserData();
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                        Intent intent=new Intent(Dmt_Transfer.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                        finish();
                    }).create().show();
        }

    @Override
    public void onBeneficiarySelcted(BeneListDmt beneficiaryList) {

    }

    private void verifyBeneficiary(String userId, String bankBeneName, String bankAccount, String bankIfsccode,
                                   String mobile, String bankId, Dialog dialg, EditText editBeneName) {
        String tag_string_req = "verify_bene";
        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Dmt_Transfer.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.VERIFY_SENDER_DMT, response -> {
                    Log.d(TAG, "verify_bene  Response: " + response+" "+dialg.toString());
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        if  (status.equals("1")) {
                           // dialg.dismiss();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(Dmt_Transfer.this,R.style.Dialod_UpDown,"",
                                        "Account Successfully verified with name"+jsonObject1.getString("benename"));
                            }
                            editBeneName.setText(jsonObject1.getString("benename"));
                            btnAddSender.setVisibility(View.GONE);
                            txtNoSender.setVisibility(View.GONE);
                           // addBeneficiary(userId,bankBeneName,bankAccount,bankIfsccode,mobile,bankId,dialg);
                           // getSenderInfo(mobile,userId);
                            // getBenificiaryList(mob);
                        }else if (status.equals("0")){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(Dmt_Transfer.this,R.style.Dialod_UpDown,"error",
                                        message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e(TAG, "verify otp Error: " + error.getMessage());
            Toast.makeText(Dmt_Transfer.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.BANK_ACCOUNT,bankAccount);
                params.put(Constant.IFSC,bankIfsccode);
                params.put(Constant.BANK_NAME,bankId);
                params.put(Constant.BENE_NAME,bankBeneName);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void addBeneficiary(String userId, String bankBeneName, String bankAccount,
                                String bankIfsccode, String mobile, String bankId,
                                Dialog dialg) {

        String tag_string_req = "add_bene";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Dmt_Transfer.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ADD_BENEFICIARY_DMT, response -> {
                    Log.d(TAG, "Add Benificiary Response: " + response+" "+bankAccount+" "+bankIfsccode);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        Toast.makeText(Dmt_Transfer.this, message, Toast.LENGTH_SHORT).show();
                        if  (status.equals("1")) {
                            //getBenificiaryList(mobile);
                          //  getSenderInfo(mobile,userId);
                            dialg.dismiss();
                            openOtpBeneficiary(userId,mobile,bankAccount);
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(Dmt_Transfer.this,R.style.Dialod_UpDown,"error",
                                        message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e(TAG, "Add Benificiary Error: " + error.getMessage());
            Toast.makeText(Dmt_Transfer.this,error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.BANK_ACCOUNT,bankAccount);
                params.put(Constant.IFSC,bankIfsccode);
                params.put(Constant.BANK_NAME,bankId);
                params.put(Constant.BENE_NAME,bankBeneName);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void openOtpBeneficiary(String userId, String mobile, String bankAccount) {

        final Dialog dialg=new Dialog(Dmt_Transfer.this);
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
            if (Configuration.hasNetworkConnection(Dmt_Transfer.this)){
                resendOtp(mobile,txtMessage,userId);
            }
        });
        btnVerify.setOnClickListener(v -> {
            String otp=editTextOtp.getText().toString();
            if (otp.isEmpty()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDown(Dmt_Transfer.this,R.style.Dialod_UpDown,"error",
                            "Enter otp sent to your mobile no");
                }
            }else if (Configuration.hasNetworkConnection(Dmt_Transfer.this)){
                otpBeneVerify(userId,mobile,bankAccount,otp,dialg);
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDown(Dmt_Transfer.this,R.style.Dialod_UpDown,"internetError",
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

    private void otpBeneVerify(String userId, String mobile, String bankAccount, String otp, Dialog dialg) {
        String tag_string_req = "add_bene";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Dmt_Transfer.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.OTP_VERIFY_BENE, response -> {
                    Log.d(TAG, "Add Benificiary Response: " + response+" "+bankAccount+" "+bankIfsccode);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        Toast.makeText(Dmt_Transfer.this, message, Toast.LENGTH_SHORT).show();
                        if  (status.equals("1")) {
                             dialg.dismiss();
                            //getBenificiaryList(mobile);
                              getSenderInfo(mobile,userId);
                           // openOtpBeneficiary(userId,mobile,bankAccount);
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(Dmt_Transfer.this,R.style.Dialod_UpDown,"error",
                                        message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e(TAG, "Add Benificiary Error: " + error.getMessage());
            Toast.makeText(Dmt_Transfer.this,error.getMessage(), Toast.LENGTH_LONG).show();
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
}
