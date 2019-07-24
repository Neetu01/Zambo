package com.efficientindia.zambopay.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import aeps.roundpay.solutions.com.mylibrary.AEPS2.GetLocation;
import datamodels.StaticDataModel;



public class CustomerHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBack;
    TextView txtTitle;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtBalance;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;
    TextInputLayout inputNumber,inputSecond,inputAmount,inputThree;
    private EditText editTextNumber,editTextAmount,editTextSecond,editTextThree;
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerOperatotList;
    private Button btnSubmit;
    private ProgressDialog progressDialog;
    WebService webService;
    String operatorId,service,serviceProvider;
    private String merchant_trxnId="";
    String adrs = "", address = "",city="",state="",pin="",country="";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        inputSecond=findViewById(R.id.input_second);
        inputAmount=findViewById(R.id.input_amount);
        editTextSecond=findViewById(R.id.edittext_second);
        inputThree=findViewById(R.id.input_three);
        editTextThree=findViewById(R.id.edittext_three);
        progressDialog=new ProgressDialog(CustomerHomeActivity.this);
        editTextNumber=findViewById(R.id.edittext_number);
        progressBar=findViewById(R.id.prog_bar_customer);
        txtBalance=findViewById(R.id.txt_balance_customer);
        spinnerOperatotList =findViewById(R.id.operator_list_customer);
        editTextAmount=findViewById(R.id.edittext_amount_customer);
        btnSubmit=findViewById(R.id.btn_customer);
        inputNumber=findViewById(R.id.input_mobile);
        imgBack=findViewById(R.id.imgback_customer);
        txtTitle=findViewById(R.id.txt_title_customer);
        webService=new WebService(CustomerHomeActivity.this);
        imgBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        inputAmount.setHint("Enter Amount (\u20B9)");
        GetLocation Location = new GetLocation(this);
        getAddress(Location.getLatitude(),Location.getLongitude());
        UserData userData= PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        if (Configuration.hasNetworkConnection(CustomerHomeActivity.this)){
            WebService.getBalance(userData.getUserId());
        }else {
            Configuration.openPopupUpDownBack(CustomerHomeActivity.this,R.style.Dialod_UpDown,
                    "main","internetError","No Internet connectivity"+
                    ", Thanks");
        }
        try{
            service=SplashActivity.getPreferences(Constant.CUSTOMER_SERVICE,"");
            if (service.equalsIgnoreCase("mobilePrepaid")){
                serviceProvider="Prepaid";
                txtTitle.setText("Mobile Recharge");
                btnSubmit.setText("Continue to Recharge");
                editTextNumber.setInputType(InputType.TYPE_CLASS_PHONE);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                inputNumber.setHint("Enter 10 digit mobile number");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("mobilePostpaid")){
                serviceProvider="postpaid";
                txtTitle.setText("Postpaid Bill Payment");
                btnSubmit.setText("Continue to Pay Bill");
                editTextNumber.setInputType(InputType.TYPE_CLASS_PHONE);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                inputNumber.setHint("Enter 10 digit mobile number");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("dth")){
                serviceProvider="DTH";
                txtTitle.setText("DTH Recharge");
                btnSubmit.setText("Continue to DTH Recharge");
                editTextNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter  Number");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("broadband")){
                serviceProvider="Broadband";
                txtTitle.setText("Broadband Recharge");
                btnSubmit.setText("Continue to Recharge");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter your subscribing id");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("electricity")){
                serviceProvider="Electricity";
                txtTitle.setText("Electricity Bill Payment");
                btnSubmit.setText("Continue to pay bill");
                editTextNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter Number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Account Number");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("landline")){
                serviceProvider="Landline";
                txtTitle.setText("Landline Bill pay");
                btnSubmit.setText("Continue to pay bill");
                editTextNumber.setInputType(InputType.TYPE_CLASS_PHONE);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter phone number without std code");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.VISIBLE);
                inputThree.setHint("Enter STD Code");
            }else if (service.equalsIgnoreCase("gas")){
                serviceProvider="Gas";
                txtTitle.setText("Gas Bill payments");
                btnSubmit.setText("Continue to pay bill");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter Number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("water")){
                serviceProvider="WaterBill";
                txtTitle.setText("Water Bill Payment");
                btnSubmit.setText("Continue to pay bills");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter Number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("creditCard")){
                serviceProvider="CreditCard";
                txtTitle.setText("Credit card Bill Payment");
                btnSubmit.setText("Continue to pay bills");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter your credit card number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.GONE);
            }else {
                serviceProvider="Insurance";
                txtTitle.setText("Insurance premium");
                btnSubmit.setText("Continue to pay premium");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter your policy number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        final ArrayAdapter<String> counryAdapter = new ArrayAdapter<>(CustomerHomeActivity.this, R.layout.spinner_item,
                R.id.spinner_text, WebService.operatorName);
        spinnerOperatotList.setAdapter(counryAdapter);
        spinnerOperatotList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String operator = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < WebService.operatorName.size(); i++) {
                    if (WebService.operatorName.get(i).equals(operator)){
                        operatorId = WebService.operatorId.get(i);
                    }
                }
                System.out.println("Operator code-->" + operatorId + ", Operator Name---->" + operator);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editTextNumber.setOnClickListener(v -> {
            if (spinnerOperatotList.getSelectedItemPosition()==0){
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please select service provider name");
                try {
                    Configuration.hideKeyboardFrom(CustomerHomeActivity.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        editTextAmount.setOnClickListener(v -> {
            if (spinnerOperatotList.getSelectedItemPosition()==0){
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please select service provider name");
                try {
                    Configuration.hideKeyboardFrom(CustomerHomeActivity.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        final UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        if (v==imgBack){
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"");
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                WebService.getActiveService(userData.getUserId(),userData.getuType());
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (SplashActivity.getPreferences(Constant.AGENT,"").equalsIgnoreCase("agent")){
                    Intent intent = new Intent(CustomerHomeActivity.this, AgentRechargeTwo.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }else {
                    Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }
            }, 1000);
        }
        if (v==btnSubmit){
            String number=editTextNumber.getText().toString().trim();
            String amount=editTextAmount.getText().toString().trim();
            String second=editTextSecond.getText().toString().trim();
            String three=editTextThree.getText().toString().trim();
            getRandomNumberString();
            final String balance=SplashActivity.getPreferences(Constant.BALANCE,"");
            if (spinnerOperatotList.getSelectedItemPosition()==0){
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please select service provider name");
            }else if (number.isEmpty()){
                editTextNumber.setError("Enter number");
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please enter number");
            }else if (inputSecond.isShown()&&second.isEmpty()){
                editTextSecond.setError("Enter account number");
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please enter account number");
            }else if (inputThree.isShown()&&three.isEmpty()){
                editTextThree.setError("Enter STD Code");
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please enter std code");
            }else if (amount.isEmpty()){
                editTextAmount.setError("Enter amount");
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please enter amount");
            }else if (Float.valueOf(amount)>Float.valueOf(balance)){
                Float amt;
                if (Float.valueOf(balance)<Float.valueOf(amount)){
                    amt=Float.valueOf(amount)-Float.valueOf(balance);
                }else {
                    amt=Float.valueOf(amount);
                }
                Intent intentProceed = new Intent(CustomerHomeActivity.this, PWECouponsActivity.class);
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
            }else if (Configuration.hasNetworkConnection(CustomerHomeActivity.this)){
                ViewDialog(userData.getUserId(),operatorId, number,second,three,amount);
               // rechargeProcess(userData.getUserId(),operatorId,number,second,three,amount);
            }else {
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"internetError",
                        "No internet connectivity");
            }
        }
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


    @Override
    public void onBackPressed() {
        final UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"");
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            WebService.getActiveService(userData.getUserId(), userData.getuType());
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if (SplashActivity.getPreferences(Constant.AGENT,"").equalsIgnoreCase("agent")){
                Intent intent = new Intent(CustomerHomeActivity.this, AgentRechargeTwo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }else {
                Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        }, 1000);
    }

    @SuppressLint("SetTextI18n")
    private void ViewDialog(final String userId,final String operatorId,final String  number,
                            final String second,final String three,final String amount) {
        getRandomNumberString();
        final UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        final Dialog dialog=new Dialog(CustomerHomeActivity.this);
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
                "<font color='#FFFFFF'><b> " +number + "</b></font>"+" ?"));
        btnNo=dialog.findViewById(R.id.btn_no);
        btnYes=dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(v -> {
            if (Float.valueOf(amount)<10){
                editTextAmount.setError("Minimum Amount is Rs.10");
            }else if (Float.valueOf(amount)>Float.valueOf(balance)){
                Float amt;
                if (Float.valueOf(balance)<Float.valueOf(amount)){
                    amt=Float.valueOf(amount)-Float.valueOf(balance);
                }else {
                    amt=Float.valueOf(amount);
                }
                Intent intentProceed = new Intent(CustomerHomeActivity.this, PWECouponsActivity.class);
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
                if(operatorId.equals("3000")) {

                    recharge2AirtelProcess(userData.getUserId(), operatorId, number, second, three, amount);

                }
                else {

                    rechargeProcess(userData.getUserId(),operatorId,number,second,three,amount);
                }
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void rechargeProcess(final String userId, final String operatorId, final String number, final String second, final String three, final String amount) {

        String tag_string_req = "recharge_process";

        progressDialog.setMessage("Processing...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RECHARGE_PROCESS3, response -> {
                    Log.d("TAG", "RECHARGE_PROCESS3 Response: " + response+" "+userId+" "+operatorId+" "+number+" "+second+" "+three+" "+amount);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");

                        Log.d("TAG","status:"+status);
                        if (status.equals("S")){
                            WebService.getBalance(userId);
                            openPopup(message,"S");
                        }else if (status.equalsIgnoreCase("P")){
                            WebService.getBalance(userId);
                            openPopup(message,"P");
                        }else {
                            WebService.getBalance(userId);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                                        message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e("TAG", "RECHARGE_PROCESS3 Error: " + error.getMessage());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Try again after sometime,please wait untill you get clear response");
            }
            progressDialog.dismiss();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                params.put(Constant.MOBILE, number);
                params.put(Constant.OPERATOR, operatorId);
                params.put(Constant.AMOUNT, amount);
                params.put(Constant.SERVICE, serviceProvider);
                params.put(Constant.ACCOUNT_NUMBER, second);
                params.put(Constant.STD_CODE, three);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void recharge2Process(final String userId, final String operatorId, final String number, final String second, final String three, final String amount) {

        String tag_string_req = "recharge_process";

        progressDialog.setMessage("Processing...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RECHARGE2_PROCESS, response -> {
            Log.d("TAG", "RECHARGE_PROCESS3 Response: " + response+" "+userId+" "+operatorId+" "+number+" "+second+" "+three+" "+amount);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");

                Log.d("TAG","status:"+status);
                if (status.equals("S")){
                    WebService.getBalance(userId);
                    openPopup(message,"S");
                }else if (status.equalsIgnoreCase("P")){
                    WebService.getBalance(userId);
                    openPopup(message,"P");
                }else {
                    WebService.getBalance(userId);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                                message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("TAG", "RECHARGE_PROCESS3 Error: " + error.getMessage());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Try again after sometime,please wait untill you get clear response");
            }
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                params.put(Constant.MOBILE, number);
                params.put(Constant.OPERATOR, operatorId);
                params.put(Constant.AMOUNT, amount);
                params.put(Constant.SERVICE, serviceProvider);
                params.put(Constant.ACCOUNT_NUMBER, second);
                params.put(Constant.STD_CODE, three);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void recharge2AirtelProcess(final String userId, final String operatorId, final String number, final String second, final String three, final String amount) {

        String tag_string_req = "recharge_process";

        progressDialog.setMessage("Processing...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RECHARGE2_AIRTELPROCESS, response -> {
            Log.d("TAG", "RECHARGE_PROCESS3 Response: " + response+" "+userId+" "+operatorId+" "+number+" "+second+" "+three+" "+amount);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");

                Log.d("TAG","status:"+status);
                if (status.equals("S")){
                    WebService.getBalance(userId);
                    openPopup(message,"S");
                }else if (status.equalsIgnoreCase("P")){
                    WebService.getBalance(userId);
                    openPopup(message,"P");
                }else {
                    WebService.getBalance(userId);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                                message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("TAG", "RECHARGE_PROCESS3 Error: " + error.getMessage());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Try again after sometime,please wait untill you get clear response");
            }
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                params.put(Constant.MOBILE, number);
                params.put(Constant.OPERATOR, operatorId);
                params.put(Constant.AMOUNT, amount);
                params.put(Constant.SERVICE, serviceProvider);
                params.put(Constant.ACCOUNT_NUMBER, second);
                params.put(Constant.STD_CODE, three);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @SuppressLint("DefaultLocale")
    public void getRandomNumberString() {
        UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999)+1000000;
        // this will convert any number sequence into 6 character.
        merchant_trxnId=userData.getUserId()+"BO"+String.format("%06d",number);
        Log.i("NUMBER",""+String.format("%06d", number));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        String account=editTextNumber.getText().toString();
        String amount=editTextAmount.getText().toString();
        String second=editTextSecond.getText().toString();
        String three=editTextThree.getText().toString();
        assert data != null;
        String result = data.getStringExtra("result");
        String response = data.getStringExtra("payment_response");
        Log.e("TOTELRES","RESPONSE--->"+response+"  \n"+result);
        sendResponse(response,userData.getUserId(),account,amount,userData.getuType(),second,three);
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

    private void sendResponse(final String response, final String userId, final String account, final String amount, final String type,
                              final String second, final String three) {
        String tag_string_req = "transaction_recharge";
        HttpsTrustManager.allowAllSSL();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAYMENT_TRANSACTION, response1 -> {
                    Log.d("TAG", "transaction recharge Response: " + response1);
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject=new JSONObject(response1);
                        String status=jsonObject.getString("status");
                        if (status.equals("0")){
                            rechargeProcess(userId,operatorId,account,second,three,amount);
                        }else {
                            WebService.getBalance(userId);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                                        jsonObject.getString("message"));
                            }
                            //  Toast.makeText(CustomerHomeActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    WebService.getBalance(userId);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Log.e("TAG", "transaction recharge Error: " + error.getMessage());
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
    @SuppressLint("SetTextI18n")
    private void openPopup(String message, final String type) {
        final Dialog dialg=new Dialog(CustomerHomeActivity.this);
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

        btnOk.setOnClickListener(v -> {
            dialg.dismiss();
            Intent intent  = new Intent(CustomerHomeActivity.this,CustomerHomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
}
