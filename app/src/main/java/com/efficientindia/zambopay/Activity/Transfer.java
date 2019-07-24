package com.efficientindia.zambopay.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.efficientindia.zambopay.Adapter.BeneficiaryAdapter;
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

import static com.android.volley.VolleyLog.TAG;

public class Transfer extends AppCompatActivity implements View.OnClickListener,BeneficiaryAdapter.BeneficiaryListner {

    ImageView imgBack,imgNO;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtBalance;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView requestList;
    @SuppressLint("StaticFieldLeak")
    private static RelativeLayout rlNoData;
    private Button btnAddBenef,btnAddSender,btnAllreadySender;
    TextView txtNo,txtNoSender;
    private EditText editTextMobile,editTextAccount,ediBankIfsc,editBeneName;
    private static ProgressDialog progressDialog;
    @SuppressLint("StaticFieldLeak")
    private static RelativeLayout relativeLayoutrequestList;
    String mobile="";
    private static List<BeneficiaryList> listData;
    @SuppressLint("StaticFieldLeak")
    private static BeneficiaryAdapter loadListAdapter;
    @SuppressLint("StaticFieldLeak")
    static Activity activity;

  //  AutoCompleteTextView editbankName;
    String /*bankNam,*/bankAccount="",bankIfsccode,bankBeneName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        imgBack=findViewById(R.id.imgback_transfer);
        progressBar=findViewById(R.id.prog_transfer);
        imgNO=findViewById(R.id.img_no_beneficiary);
        txtBalance=findViewById(R.id.txt_balance_transfer);
        requestList=findViewById(R.id.recyclerview_transfer);
        rlNoData=findViewById(R.id.rl_no_beneficiary);
        btnAddBenef=findViewById(R.id.btn_add_benef);
        txtNo=findViewById(R.id.txt_no_beneficiary);
        txtNoSender=findViewById(R.id.txt_sender_info);
        activity=Transfer.this;
        btnAddSender=findViewById(R.id.btn_add_sender);
        relativeLayoutrequestList=findViewById(R.id.relative_request_list);
        btnAllreadySender=findViewById(R.id.btn_add_allready_sender);
        btnAddSender.setOnClickListener(this);
        editTextMobile=findViewById(R.id.edittext_user_mobile);
        progressDialog=new ProgressDialog(Transfer.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        btnAddBenef.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        btnAllreadySender.setOnClickListener(this);
        relativeLayoutrequestList.setVisibility(View.GONE);
        UserData userData=PrefManager.getInstance(Transfer.this).getUserData();
        if (Configuration.hasNetworkConnection(Transfer.this)){
            WebService.getBalance(userData.getUserId());
            WebService.getActiveService(userData.getUserId(), userData.getuType());
        }else {
            rlNoData.setVisibility(View.VISIBLE);
            imgNO.setImageResource(R.drawable.nointernet);
            txtNo.setVisibility(View.VISIBLE);
            txtNo.setText(R.string.no_internet);
            btnAddBenef.setVisibility(View.GONE);
        }

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
                    getSenderInfo(mobile);
                }
            }
        });
        listData = new ArrayList<>();
        loadListAdapter = new BeneficiaryAdapter(activity, listData, this);
        editTextMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
               mobile =editTextMobile.getText().toString().trim();

                if (actionId == EditorInfo.IME_ACTION_SEND||actionId == EditorInfo.IME_ACTION_GO||actionId ==EditorInfo.IME_ACTION_DONE) {
                    if (mobile.length()<10||mobile.length()>10){
                        Toast.makeText(Transfer.this, "Invalid Mobile number", Toast.LENGTH_SHORT).show();
                    }else {
                       // mobile.length();
                        getSenderInfo(mobile);
                    }
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onClick(View v) {
        final UserData userData=PrefManager.getInstance(Transfer.this).getUserData();
        if (v==imgBack){
            new AlertDialog.Builder(Transfer.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit from Maney Tranfer?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            WebService.getActiveService(userData.getUserId(), userData.getuType());
                            Intent intent=new Intent(Transfer.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                            finish();
                        }
                    }).create().show();
        }
        if (v==btnAddBenef||v==btnAllreadySender){
            final Dialog dialg=new Dialog(Transfer.this);
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
            TextView txtVerifyBene=dialg.findViewById(R.id.txt_verify);
          //  bankNam= editbankName.getText().toString();


            txtVerifyBene.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bankAccount=editTextAccount.getText().toString();
                    bankIfsccode=ediBankIfsc.getText().toString();
                   // bankBeneName=editBeneName.getText().toString();
                    /*if (bankNam.isEmpty()){
                        editbankName.setError("Enter bank name");
                    }else*/ if (bankAccount.isEmpty()){
                        editTextAccount.setError("Enter account number");
                    }else if (bankIfsccode.isEmpty()){
                        ediBankIfsc.setError("Enter IFSC");
                    }else if (Configuration.hasNetworkConnection(Transfer.this)){
                        verifyBeneficiary(userData.getUserId(),bankAccount,bankIfsccode,mobile);
                    }else {
                        Toast.makeText(Transfer.this, "No internet connectivity", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bankAccount=editTextAccount.getText().toString();
                    bankIfsccode=ediBankIfsc.getText().toString();
                    bankBeneName=editBeneName.getText().toString();
                    if (bankAccount.isEmpty()){
                        editTextAccount.setError("Enter account number");
                    }else if (bankIfsccode.isEmpty()){
                        ediBankIfsc.setError("Enter IFSC");
                    }else if (bankBeneName.isEmpty()){
                        editBeneName.setError("Enter Benificiary Name");
                    }else if (Configuration.hasNetworkConnection(Transfer.this)){
                        addBeneficiary(userData.getUserId(),bankBeneName,bankAccount,bankIfsccode,mobile,dialg);
                    }else {
                        Toast.makeText(Transfer.this, "No internet connectivity", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            txtClose.setOnClickListener(new View.OnClickListener() {
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
        if (v==btnAddSender){
            final Dialog dialg=new Dialog(Transfer.this);
            dialg.setContentView(R.layout.layout_add_sender);
            dialg.setCanceledOnTouchOutside(false);
            dialg.setCancelable(false);
            ImageView imgClose =  dialg.findViewById(R.id.img_close_add_sender);
            final LinearLayout linearSenderInfo=dialg.findViewById(R.id.linear_sender_info);
            final TextView txtMessage=dialg.findViewById(R.id.txt_sender_otp_message);
            TextView txtResendOtp=dialg.findViewById(R.id.txt_resend_sender_otp);
            final LinearLayout lnOtp=dialg.findViewById(R.id.linear_sender_otp);
            final EditText editMobileno=dialg.findViewById(R.id.edittext_sender_mobile);
            final EditText editTextNameSender=dialg.findViewById(R.id.edittext_sender_name);
            final EditText editTextSenderOtp=dialg.findViewById(R.id.edittext_sender_otp);
            final Button btnAddSn=dialg.findViewById(R.id.btn_add_name_sender);
            Button btnVerifyOtp=dialg.findViewById(R.id.btn_verify_sender_otp);
            editMobileno.setText(mobile);
            final String mob=editMobileno.getText().toString().trim();

            btnAddSn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name=editTextNameSender.getText().toString();
                    if (mob.isEmpty()){
                        editMobileno.setError("Enter sender mobile");
                    }else if (name.isEmpty()) {
                        editTextNameSender.setError("Enter Sender name");
                    }else if (Configuration.hasNetworkConnection(Transfer.this)){
                        createSender(userData.getUserId(),mob,name,lnOtp,btnAddSn,txtMessage,linearSenderInfo);
                    }
                }
            });
            txtResendOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Configuration.hasNetworkConnection(Transfer.this)){
                        resendOtp(mob,txtMessage);
                    }
                }
            });

            btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String otp=editTextSenderOtp.getText().toString().trim();
                    if (otp.isEmpty()){
                        editTextSenderOtp.setError("Enter otp");
                    }else if (Configuration.hasNetworkConnection(Transfer.this)){
                        verifySender(mob,otp,dialg);
                    }else {
                        Toast.makeText(Transfer.this, "No internet connectivity", Toast.LENGTH_SHORT).show();
                    }
                }
            });



            imgClose.setOnClickListener(new View.OnClickListener() {
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

    private void addBeneficiary(final String userId, final String bankBeneName, final String bankAccount,
                                final String bankIfsccode, final String mobile, final Dialog dialg) {
        String tag_string_req = "add_bene";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Transfer.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ADD_BENEFICIARY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Add Benificiary Response: " + response+" "+bankAccount+" "+bankIfsccode);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String status = jsonObject1.getString("status");
                    String message=jsonObject1.getString("message");
                    Toast.makeText(Transfer.this, message, Toast.LENGTH_SHORT).show();
                    if  (status.equals("1")) {
                        //getBenificiaryList(mobile);
                        getSenderInfo(mobile);
                        dialg.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Add Benificiary Error: " + error.getMessage());
                Toast.makeText(Transfer.this,error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.BENE_NAME,bankBeneName);
                params.put(Constant.BANK_ACCOUNT,bankAccount);
                params.put(Constant.IFSC,bankIfsccode);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void verifyBeneficiary(final String userId,final String bankAccount,
                                   final String bankIfsccode,final String mobile) {
        String tag_string_req = "verify_bene";
        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Transfer.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.VERIFY_BENEFICIARY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Verify Benificiary Response: " + response+" "+bankAccount+" "+bankIfsccode);
                progressDialog.dismiss();
               /* try {
                   // JSONObject jsonObject1=new JSONObject(response);
                    //String status = jsonObject1.getString("status");
                   *//* String message=jsonObject1.getString("message");
                    if  (status.equals("1")) {

                    }else if (status.equals("0")){

                    }*//*
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Verify Benificiary Error: " + error.getMessage());
                Toast.makeText(Transfer.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.BANK_ACCOUNT,bankAccount);
                params.put(Constant.IFSC,bankIfsccode);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void verifySender(final String mob, final String otp, final Dialog dialg) {

        String tag_string_req = "verify_otp";
        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Transfer.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.VERIFY_SENDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "verify otp Response: " + response+" "+mob+" "+otp);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String status = jsonObject1.getString("status");
                    String message=jsonObject1.getString("message");
                    if  (status.equals("1")) {
                        dialg.dismiss();
                        btnAddSender.setVisibility(View.GONE);
                        txtNoSender.setVisibility(View.GONE);
                        Toast.makeText(Transfer.this,message,Toast.LENGTH_SHORT).show();
                        getSenderInfo(mobile);
                       // getBenificiaryList(mob);
                    }else if (status.equals("0")){
                        Toast.makeText(Transfer.this,message,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "verify otp Error: " + error.getMessage());
                Toast.makeText(Transfer.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.SENDER_OTP,otp);
                params.put(Constant.SENDER_MOBILE,mob);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void getBenificiaryList(final String mob) {
        String tag_string_req = "beni_list";
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(activity);
        }catch (Exception e){
            e.printStackTrace();
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.BENEFICIARY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Benificiary Response: " + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String status = jsonObject1.getString("status");
                   // String message=jsonObject1.getString("message");
                    if  (status.equals("1")) {
                        rlNoData.setVisibility(View.GONE);
                        relativeLayoutrequestList.setVisibility(View.VISIBLE);
                       // listData.clear();
                        if (jsonObject1.has("List")){
                            Object list = jsonObject1.get("List");
                            JSONArray jsonArray;
                            JSONObject jsonObject;
                            if (list instanceof JSONArray) {
                                jsonArray = (JSONArray)list;
                                List<BeneficiaryList> items = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<BeneficiaryList>>(){}.getType());

                                listData.clear();
                                listData.addAll(items);
                                loadListAdapter.notifyDataSetChanged();

                            } else if (list instanceof JSONObject) {
                                jsonObject = (JSONObject)list;
                               /* List<BeneficiaryList> items = new Gson().fromJson(jsonObject.toString(),
                                        new TypeToken<List<BeneficiaryList>>(){}.getType());*/

                                BeneficiaryList beneficiaryList = new Gson().fromJson(jsonObject.toString(), BeneficiaryList.class);

                                listData.clear();
                                listData.addAll(Collections.singleton(beneficiaryList));
                                loadListAdapter.notifyDataSetChanged();

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                                requestList.setLayoutManager(mLayoutManager);
                                requestList.setItemAnimator(new DefaultItemAnimator());
                                requestList.setAdapter(loadListAdapter);
                            }
                        }
                    }else if (status.equals("0")){
                        rlNoData.setVisibility(View.VISIBLE);
                        relativeLayoutrequestList.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Benificiary Error: " + error.getMessage());
                Toast.makeText(activity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.SENDER_MOBILE,mob);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void resendOtp(final String mob, final TextView txtMessage) {
        String tag_string_req = "resend_otp";
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Transfer.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RESEND_OTP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
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
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "resend otp Error: " + error.getMessage());
                Toast.makeText(Transfer.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.SENDER_MOBILE,mob);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void createSender(final String userId, final String mobile, final String name, final LinearLayout lnOtp,
                              final Button btnAddSn, final TextView txtMessage, final LinearLayout linearSenderInfo) {
        String tag_string_req = "create_sender";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(Transfer.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.CREATE_SENDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "CREATE Response: " + response+" "+btnAddSn.toString());
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String status = jsonObject1.getString("status");
                    String message=jsonObject1.getString("message");
                    if  (status.equals("1")) {
                        lnOtp.setVisibility(View.VISIBLE);
                        linearSenderInfo.setVisibility(View.GONE);
                        txtMessage.setText(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Create Error: " + error.getMessage());
                Toast.makeText(Transfer.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.SENDER_NAME,name);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getSenderInfo(final String mobile) {
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
                AppConfig.GET_SENDER_INFO, new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SENDER INFO Response: " + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String status = jsonObject1.getString("status");
                    String message=jsonObject1.getString("message");
                    if  (status.equals("1")) {
                      //  JSONObject jObj=jsonObject1.getJSONObject("data");
                       // JSONObject jsonObject=jObj.getJSONObject("TABLE");
                        txtNoSender.setVisibility(View.VISIBLE);
                        txtNoSender.setText(jsonObject1.getString("customerDetail"));
                        getBenificiaryList(mobile);
                    }else if (status.equals("0")){
                        Configuration.openPopupUpDownBack(Transfer.this,R.style.Dialod_UpDown,"main","error",message+
                                ", Thanks");
                        txtNoSender.setVisibility(View.VISIBLE);
                        btnAddSender.setVisibility(View.VISIBLE);
                        txtNoSender.setText(message);
                        /*Toast.makeText(Transfer.this,
                                message, Toast.LENGTH_LONG).show();*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "SENDER INFO Error: " + error.getMessage());
                Toast.makeText(Transfer.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.SENDER_MOBILE,mobile);
                Log.e(TAG,"SENDR INF--->"+Constant.SENDER_MOBILE+"=="+mobile);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Transfer.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit from Money Tranfer?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        UserData userData=PrefManager.getInstance(Transfer.this).getUserData();
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                        Intent intent=new Intent(Transfer.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onBeneficiarySelcted(BeneficiaryList beneficiaryList) {

    }
}
