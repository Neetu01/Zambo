package com.efficientindia.zambopay.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easebuzz.payment.kit.PWECouponsActivity;
import com.efficientindia.zambopay.AppConfig.AppConfig;
import com.efficientindia.zambopay.AppConfig.AppController;
import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.Constant;
import com.efficientindia.zambopay.AppConfig.HttpsTrustManager;
import com.efficientindia.zambopay.AppConfig.PrefManager;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import aeps.roundpay.solutions.com.mylibrary.AEPS2.GetLocation;
import datamodels.StaticDataModel;

//@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG =HomeActivity.class.getSimpleName() ;
    private EditText editTextMobile,editTextAmount;
    private Button btnRecharge;
     RadioGroup radioGroup;
    private RadioButton rbPrepaid,rbPostpaid;
    TextView txtTitle,txtHead;
    private ImageView imgBack;
    TextInputLayout txtInput;
    @SuppressLint("StaticFieldLeak")
    public static TextView txt_balance_rechargee;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;
    ProgressDialog pDialog;
    private ArrayList<String> operatorId;
    private ArrayList<String> operatorName;
    String serviceProviderId="",service="",url="";
    private AutoCompleteTextView operatorList;
    private String merchant_trxnId="";
    String rechar="";
   // String customers_unique_id="";
    String adrs = "", address = "",city="",state="",pin="",country="";
    //TextView txtRrn,txtCustNo,txtBankMesg,txtBalanceDetail;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        editTextAmount=findViewById(R.id.edittext_amount);
        editTextMobile=findViewById(R.id.edittext_mobile);
        btnRecharge=findViewById(R.id.btn_recharge);
        radioGroup=findViewById(R.id.radio_mobile);
        rbPostpaid=findViewById(R.id.post_mob);
        rbPrepaid=findViewById(R.id.prep_mob);
        txtHead=findViewById(R.id.txt_home_title);
        operatorList=findViewById(R.id.operator_lis);
        pDialog=new ProgressDialog(HomeActivity.this);
        txt_balance_rechargee=findViewById(R.id.txt_balance_rechargee);
        progressBar=findViewById(R.id.prog_fund);
        txtTitle=findViewById(R.id.txt_title);
        imgBack=findViewById(R.id.imgback_recharge);
        txtInput=findViewById(R.id.input_mobile);
        imgBack.setOnClickListener(this);
        btnRecharge.setOnClickListener(this);
        operatorId = new ArrayList<>();
        operatorName = new ArrayList<>();
      //  rbPrepaid.setChecked(true);

        GetLocation Location = new GetLocation(this);
        getAddress(Location.getLatitude(),Location.getLongitude());
        final UserData userData=PrefManager.getInstance(HomeActivity.this).getUserData();
        if (Configuration.hasNetworkConnection(HomeActivity.this)) {
            WebService.getBalance(userData.getUserId());
           // getEazeBuzzId();
        }else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            finish();
        }
        try {
            rechar=getIntent().getStringExtra(Constant.RECHARGE);
        }catch (Exception e){
            e.printStackTrace();
        }

      /*  if (Configuration.hasNetworkConnection(HomeActivity.this)) {
            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    WebService.getActiveService(userData.getUserId());
                    handler.postDelayed(this, 6000);
                }
            }, 6000);
        } else {
            Toast.makeText(HomeActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }*/

        try{
            String recharge=SplashActivity.getPreferences("recharge","");
            String prepaid=SplashActivity.getPreferences(Constant.MOBILE_PREPAID,"");
            String postpaid=SplashActivity.getPreferences(Constant.MOBILE_POSTPAID,"");
            if (prepaid.equalsIgnoreCase("unhide")){
                rbPrepaid.setVisibility(View.VISIBLE);
                rbPrepaid.setChecked(true);
              //  btnRecharge.setText("");
            }else if (prepaid.equalsIgnoreCase("hide")){
                rbPrepaid.setVisibility(View.GONE);
                rbPostpaid.setChecked(true);
            }
            if (postpaid.equalsIgnoreCase("unhide")){
                rbPostpaid.setVisibility(View.VISIBLE);
            }else if (postpaid.equalsIgnoreCase("hide")){
                rbPostpaid.setVisibility(View.GONE);
                rbPrepaid.setChecked(true);
            }
           /* if (rbPrepaid.isShown()){
                rbPrepaid.setChecked(true);
            }else {
                rbPostpaid.setChecked(true);
            }*/
            if (recharge.equalsIgnoreCase("mobile")){
                radioGroup.setVisibility(View.VISIBLE);
                txtInput.setHint(getResources().getString(R.string.mobile_no));
                if (rbPostpaid.isChecked()){
                    getOperatorList("Postpaid");
                    service="Postpaid";
                    url=AppConfig.BILL_PAYMENT_PROCESS;
                    txtHead.setText(R.string.bill);
                    txtTitle.setText(R.string.pay_mob);
                    btnRecharge.setText("Continue to Pay Bill");
                }
                if (rbPrepaid.isChecked()){
                    getOperatorList("Prepaid");
                    service="Prepaid";
                    url=AppConfig.RECHARGE_PROCESS;
                    txtHead.setText(R.string.recharge);
                    txtTitle.setText(R.string.mobile_recharge);
                    btnRecharge.setText("Continue to Recharge");

                }
            }else if (recharge.equalsIgnoreCase("dth")){
                txtTitle.setText(R.string.dth);
                radioGroup.setVisibility(View.GONE);
                txtHead.setText(R.string.recharge);
                txtInput.setHint(getResources().getString(R.string.dth_no));
                getOperatorList("dth");
                service="dth";
                url=AppConfig.RECHARGE_PROCESS;
                btnRecharge.setText("Continue to Recharge");
            }else if (recharge.equalsIgnoreCase("datacard")){
                txtTitle.setText(R.string.datacard);
                radioGroup.setVisibility(View.GONE);
                txtInput.setHint(getResources().getString(R.string.datacard_no));
                txtHead.setText(R.string.recharge);
                getOperatorList("DataRecharge");
                service="DataRecharge";
                url=AppConfig.RECHARGE_PROCESS;
                btnRecharge.setText("Continue to Recharge");
            }else if (recharge.equalsIgnoreCase("electricity")){
                txtTitle.setText(R.string.pay_electricity);
                radioGroup.setVisibility(View.GONE);
                txtInput.setHint(getResources().getString(R.string.account_no));
                txtHead.setText(R.string.bill_payment);
                getOperatorList("electricity");
                service="electricity";
                url=AppConfig.BILL_PAYMENT_PROCESS;
                btnRecharge.setText("Continue to Pay Bill");
            }else if (recharge.equalsIgnoreCase("gas")){
                txtTitle.setText(R.string.pay_gas);
                radioGroup.setVisibility(View.GONE);
                txtInput.setHint(getResources().getString(R.string.customer_acc));
                txtHead.setText(R.string.bill_payment);
                getOperatorList("gas");
                service="gas";
                url=AppConfig.BILL_PAYMENT_PROCESS;
                btnRecharge.setText("Continue to Pay Bill");
            }else if (recharge.equalsIgnoreCase("landline")){
                txtTitle.setText(R.string.pay_landline);
                radioGroup.setVisibility(View.GONE);
                txtInput.setHint(getResources().getString(R.string.number));
                txtHead.setText(R.string.bill_payment);
                getOperatorList("landline");
                service="landline";
                url=AppConfig.BILL_PAYMENT_PROCESS;
                btnRecharge.setText("Continue to Pay Bill");
            }else if (recharge.equalsIgnoreCase("insurance")){
                txtTitle.setText(R.string.pay_insurance);
                radioGroup.setVisibility(View.GONE);
                txtInput.setHint(getResources().getString(R.string.account_no));
                txtHead.setText(R.string.bill_payment);
                getOperatorList("insurance");
                service="insurance";
                url=AppConfig.BILL_PAYMENT_PROCESS;
                btnRecharge.setText("Continue to Pay for Insurance");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbPostpaid.isChecked()){
                    getOperatorList("Postpaid");
                    service="Postpaid";
                    url=AppConfig.BILL_PAYMENT_PROCESS;
                }
                if (rbPrepaid.isChecked()){
                    getOperatorList("Prepaid");
                    service="Prepaid";
                    url=AppConfig.RECHARGE_PROCESS;
                }
            }
        });
    }

    private void getAddress(double latitude, double longitude) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            adrs = address    ;
            //  lati= String.valueOf(lat);
            // lng= String.valueOf(lon);
            city=addresses.get(0).getLocality();
            state=addresses.get(0).getAdminArea();
            country=addresses.get(0).getCountryName();
            pin=addresses.get(0).getPostalCode();

            //  Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
            Log.e("ADDRESS","ADDRESS-->"+address);

        } catch (Exception ex) {
            ex.printStackTrace();
            adrs = null;
        }
    }


    private void getOperatorList(final String service) {
       // Configuration.showDialog("Please wait...", pDialog);
      //  pDialog.setCanceledOnTouchOutside(false);
        //countries.add("---Select Country---");
        HttpsTrustManager.allowAllSSL();
        System.out.println("service-->"+service);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.GET_OPERATOR_LIST,
                new Response.Listener<String>() {

                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @SuppressLint("ClickableViewAccessibility")
                   // @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();

                        try {

                            operatorId.clear();
                            operatorName.clear();
                            System.out.println("Operator response--->" + response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    operatorName.add(jsonObject1.getString("text"));
                                    operatorId.add(jsonObject1.getString("id"));
                                }

                                ArrayAdapter<String> counryAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item,
                                        R.id.spinner_text, operatorName);
                                operatorList.setThreshold(1);
                                operatorList.setAdapter(counryAdapter);

                                operatorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        String bank = parent.getItemAtPosition(position).toString();
                                       // String sho = operatorName.get(position).toString();
                                        for (int i = 0; i < operatorName.size(); i++) {
                                            if (operatorName.get(i).equals(bank)) {
                                                serviceProviderId = operatorId.get(i);

                                                operatorList.setText(operatorName.get(i));

                                                // getParameter(type);
                                            }
                                        }
                                        System.out.println("Bank code12-->" + bank + "code12---->" + serviceProviderId);
                                        Log.d(TAG, "BN--" + operatorList.getText().toString() + " CD-->" + serviceProviderId);


                                    }
                                });
                                operatorList.setOnTouchListener(new View.OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                                        // TODO Auto-generated method stub
                                        /*operatorList.setText("");*/
                                        operatorList.showDropDown();
                                        operatorList.requestFocus();
                                        return false;
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            pDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pDialog.dismiss();
                        Toast.makeText(HomeActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.SERVICE, service);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        UserData userData=PrefManager.getInstance(HomeActivity.this).getUserData();
        if (v==imgBack){
            SplashActivity.savePreferences("recharge","");
            WebService.getActiveService(userData.getUserId(), userData.getuType());
            Intent intent =new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
        if (v==btnRecharge){
            String operator=operatorList.getText().toString();
            String account=editTextMobile.getText().toString();
            String amount=editTextAmount.getText().toString();
            getRandomNumberString();
            final String balance=SplashActivity.getPreferences(Constant.BALANCE,"");
          /*  if (rechar.equalsIgnoreCase("rechar")){
                ViewDialog(userData.getUserId(),account,serviceProviderId,amount,service);
            }else*/ if (editTextMobile.getText().toString().isEmpty()){
                editTextMobile.setError("Enter Detail");
            }else if (operator.isEmpty()){
                operatorList.setError("Select Operator");
            }else if (editTextAmount.getText().toString().isEmpty()){
                editTextAmount.setError("Enter Amount");
            }else if (Float.valueOf(amount)<10){
                editTextAmount.setError("Minimum Amount is Rs.10");
            }else if (Float.valueOf(amount)>Float.valueOf(balance)){
                  Float amt;
                    if (Float.valueOf(balance)<Float.valueOf(amount)){
                        amt=Float.valueOf(amount)-Float.valueOf(balance);
                    }else {
                        amt=Float.valueOf(amount);
                    }
                Intent intentProceed = new Intent(HomeActivity.this, PWECouponsActivity.class);
                intentProceed.putExtra("trxn_id",merchant_trxnId);
                intentProceed.putExtra("trxn_amount",amt);
                intentProceed.putExtra("trxn_prod_info","Recharge");
                intentProceed.putExtra("trxn_firstname",userData.getName());
                intentProceed.putExtra("trxn_email_id",userData.getEmail());
                intentProceed.putExtra("trxn_phone",userData.getMobile());
                intentProceed.putExtra("trxn_key",Constant.MERCHANT_ID);
                intentProceed.putExtra("trxn_udf1","");
                intentProceed.putExtra("trxn_udf2","");
                intentProceed.putExtra("trxn_udf3","");
                intentProceed.putExtra("trxn_udf4","");
                intentProceed.putExtra("trxn_udf5","");
                intentProceed.putExtra("trxn_address1",address);
                intentProceed.putExtra("trxn_address2","");
                intentProceed.putExtra("trxn_city",city);
                intentProceed.putExtra("trxn_state",state);
                intentProceed.putExtra("trxn_country",country);
                intentProceed.putExtra("trxn_zipcode",pin);
                intentProceed.putExtra("trxn_is_coupon_enabled",0);
                intentProceed.putExtra("trxn_salt",Constant.MERCH_SALT);
                intentProceed.putExtra("unique_id",userData.getUserId());
                intentProceed.putExtra("pay_mode",Constant.PAY_MODE);
                startActivityForResult(intentProceed, StaticDataModel.PWE_REQUEST_CODE);
            }else if (Configuration.hasNetworkConnection(HomeActivity.this)){
                ViewDialog(userData.getUserId(),account,serviceProviderId,amount,service);
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private void ViewDialog(final String userId, final String account, final String serviceProviderId,
                            final String amount, final String service) {
        getRandomNumberString();
        final UserData userData=PrefManager.getInstance(HomeActivity.this).getUserData();
        final Dialog dialog=new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button btnNo,btnYes;
        TextView txtMsg=dialog.findViewById(R.id.txt_msg);
        final String balance=SplashActivity.getPreferences(Constant.BALANCE,"");
        //  Html.fromHtml("<font color='#FFFFFF'><b> " + "ZAM" + "</b></font>"+"<font color='#0093dd'>" + "BO" + "</font>");
        txtMsg.setText(Html.fromHtml("<font color='#FFFFFF'><b> " + "Do you want to proceed for payment of" +
                "</b></font><br/>"+"<font color='#FFFFFF'><b> " + "Rs."+amount + "</b></font>"+" for "+
                "<font color='#FFFFFF'><b> " +account + "</b></font>"+" ?"));
        btnNo=dialog.findViewById(R.id.btn_no);
        btnYes=dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.valueOf(amount)<10){
                    editTextAmount.setError("Minimum Amount is Rs.10");
                }else if (Float.valueOf(amount)>Float.valueOf(balance)){
                   /* Intent intent=new Intent(HomeActivity.this,AddMoney.class);
                    SplashActivity.savePreferences(Constant.HOME,"home");
                    startActivity(intent);*/

                    Float amt;
                    if (Float.valueOf(balance)<Float.valueOf(amount)){
                        amt=Float.valueOf(amount)-Float.valueOf(balance);
                    }else {
                        amt=Float.valueOf(amount);
                    }
                    Intent intentProceed = new Intent(HomeActivity.this, PWECouponsActivity.class);
                    intentProceed.putExtra("trxn_id",merchant_trxnId);
                    intentProceed.putExtra("trxn_amount",amt);
                    intentProceed.putExtra("trxn_prod_info","Recharge");
                    intentProceed.putExtra("trxn_firstname",userData.getName());
                    intentProceed.putExtra("trxn_email_id",userData.getEmail());
                    intentProceed.putExtra("trxn_phone",userData.getMobile());
                    intentProceed.putExtra("trxn_key",Constant.MERCHANT_ID);
                    intentProceed.putExtra("trxn_udf1","");
                    intentProceed.putExtra("trxn_udf2","");
                    intentProceed.putExtra("trxn_udf3","");
                    intentProceed.putExtra("trxn_udf4","");
                    intentProceed.putExtra("trxn_udf5","");
                    intentProceed.putExtra("trxn_address1",address);
                    intentProceed.putExtra("trxn_address2","");
                    intentProceed.putExtra("trxn_city",city);
                    intentProceed.putExtra("trxn_state",state);
                    intentProceed.putExtra("trxn_country",country);
                    intentProceed.putExtra("trxn_zipcode",pin);
                    intentProceed.putExtra("trxn_is_coupon_enabled",0);
                    intentProceed.putExtra("trxn_salt",Constant.MERCH_SALT);
                    intentProceed.putExtra("unique_id",userId);
                    intentProceed.putExtra("pay_mode",Constant.PAY_MODE);
                    startActivityForResult(intentProceed, StaticDataModel.PWE_REQUEST_CODE);
                }else {
                    rechargeProcess(userId, account, serviceProviderId, amount, service);
                    dialog.dismiss();
                }
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void rechargeProcess(final String userId,final String account,final String serviceProviderId,
                                 final String amount,final String service) {
        String tag_string_req = "recharge_process";

        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Recharge Response: " + response+" URL-->"+url+" service-->"+service+" servicePID-->"+serviceProviderId);

                if (pDialog.isShowing()||pDialog!=null){
                    pDialog.dismiss();
                }

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String message=jsonObject.getString("message");
                    if (status.equals("S")){
                       /* Toast.makeText(HomeActivity.this,
                                message, Toast.LENGTH_LONG).show();*/
                        WebService.getBalance(userId);
                        openPopup(message,"S");
                    }else if (status.equalsIgnoreCase("P")){
                        WebService.getBalance(userId);
                        openPopup(message,"P");
                       /* Toast.makeText(HomeActivity.this,
                                message, Toast.LENGTH_LONG).show();*/
                    }else {
                        openPopup(message,"F");
                        WebService.getBalance(userId);
                       /* Toast.makeText(HomeActivity.this,
                                message, Toast.LENGTH_LONG).show();*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                if (pDialog.isShowing()||pDialog!=null){
                    pDialog.dismiss();
                }
                Toast.makeText(HomeActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                params.put(Constant.MOBILE_NUMBER,account);
                params.put(Constant.OPERATOR,serviceProviderId);
                params.put(Constant.AMOUNT,amount);
                params.put(Constant.RECHARGE_TYPE,service);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @SuppressLint("SetTextI18n")
    private void openPopup(String message, final String type) {
        final Dialog dialg=new Dialog(HomeActivity.this);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_recharge);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_status_recharge);
        TextView txtStatus=dialg.findViewById(R.id.txt_status);
        if (type.equalsIgnoreCase("S")){
            imageView.setImageResource(R.drawable.success);
            txtStatus.setText("Status : Success");
        }else if (type.equalsIgnoreCase("P")){
            imageView.setImageResource(R.drawable.pending);
            txtStatus.setText("Status : Pending");
        }else {
            imageView.setImageResource(R.drawable.failed);
            txtStatus.setText("Status : Failed");
        }
        TextView txtTransactionId=dialg.findViewById(R.id.txt_status_recharge);

        Button btnOk= dialg.findViewById(R.id.btn_okay);

        txtTransactionId.setText(message);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialg.dismiss();
                Intent intent  = new Intent(HomeActivity.this,HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onBackPressed() {
        UserData userData=PrefManager.getInstance(HomeActivity.this).getUserData();
        SplashActivity.savePreferences("recharge","");
        WebService.getActiveService(userData.getUserId(), userData.getuType());
        Intent intent =new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  finish();
        UserData userData=PrefManager.getInstance(HomeActivity.this).getUserData();
        SplashActivity.savePreferences("recharge","");
        WebService.getActiveService(userData.getUserId(), userData.getuType());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UserData userData=PrefManager.getInstance(HomeActivity.this).getUserData();
        String account=editTextMobile.getText().toString();
        String amount=editTextAmount.getText().toString();
        assert data != null;
        String result = data.getStringExtra("result");
        String response = data.getStringExtra("payment_response");
        Log.e("TOTELRES","RESPONSE--->"+response+"  \n"+result);
        sendResponse(response,userData.getUserId(),account,amount,userData.getuType());
        WebService.getBalance(userData.getUserId());
        getRandomNumberString();

        try {
            JSONObject jsonObject=new JSONObject(response);
            if (jsonObject.has("status")) {
                String status=jsonObject.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    Log.e("STATUS","STATUS--->"+status);
                } else {
                    if (jsonObject.has("error_msg")) {
                        Toast.makeText(this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(this, "You have cancelled your transaction", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(final String response, final String userId, final String account, final String amount, final String type) {
        String tag_string_req = "transaction_recharge";
        HttpsTrustManager.allowAllSSL();
        pDialog.setMessage("Please wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAYMENT_TRANSACTION, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "transaction recharge Response: " + response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    pDialog.dismiss();
                    if (status.equals("0")){
                        rechargeProcess(userId,account,serviceProviderId,amount,service);
                    }else {
                        WebService.getBalance(userId);
                        Toast.makeText(HomeActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                WebService.getBalance(userId);
                Log.e(TAG, "transaction recharge Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                params.put(Constant.RESPONSE,response);
                params.put(Constant.USER_TYPE,type);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

/*
    private void getEazeBuzzId() {
        String tag_string_req = "EAZE_BUZZ";
        pDialog.setMessage("Please wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        pDialog.show();
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.GET_EAZEBUSS_ID, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "EAZE_BUZZ Response: " + response);
                if (pDialog.isShowing()){
                    pDialog.dismiss();
                }

                try {
                    pDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    merchant_trxnId=jsonObject.getString("tid");
                    customers_unique_id=jsonObject.getString("customerId");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "EAZE_BUZZ Error: " + error.getMessage());
            }
        }) */
/*{
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.UID,userId);
                return params;
            }
        }*//*
;
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
*/

    @SuppressLint("DefaultLocale")
    public void getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999)+1000000;
        // this will convert any number sequence into 6 character.
        merchant_trxnId="ZAM"+String.format("%06d",number);
        Log.i("NUMBER",""+String.format("%06d", number));
    }

}
