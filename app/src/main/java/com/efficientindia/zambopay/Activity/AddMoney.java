package com.efficientindia.zambopay.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easebuzz.payment.kit.PWECouponsActivity;
import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.Constant;
import com.efficientindia.zambopay.AppConfig.PrefManager;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import aeps.roundpay.solutions.com.mylibrary.AEPS2.GetLocation;
import datamodels.StaticDataModel;

public class AddMoney extends AppCompatActivity implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    public static TextView txtBalance;
    EditText editTextAmount;
    private ImageView imgBack;
    private Button btnAddMoney;
    ProgressDialog progressDialog;
    String merchant_trxnId="",pin="";
    String adrs = "", address = "",city="",state="";
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;
    String amot="";


    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private String country="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        progressBar=findViewById(R.id.prog_money);
        txtBalance=findViewById(R.id.txt_balance_add_money);
        imgBack=findViewById(R.id.imgback_add_money);
        imgBack.setOnClickListener(this);
        progressDialog=new ProgressDialog(AddMoney.this);
        editTextAmount=findViewById(R.id.edittext_amount_add_money);
        btnAddMoney=findViewById(R.id.btn_add_money);
        btnAddMoney.setOnClickListener(this);
        ReadPhoneStatePermission();
        UserData userData=PrefManager.getInstance(AddMoney.this).getUserData();
        GetLocation Location = new GetLocation(this);
        getAddress(Location.getLatitude(),Location.getLongitude());
        if (Configuration.hasNetworkConnection(AddMoney.this)){
            WebService.getBalance(userData.getUserId());
           // getEazeBuzzId();
        }else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
        try {
            amot=getIntent().getStringExtra(Constant.AMOUNT);
            editTextAmount.setText(amot);
        }catch (Exception e){
            e.printStackTrace();
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
            //lati= String.valueOf(lat);
            //lng= String.valueOf(lon);
            city=addresses.get(0).getLocality();
            state=addresses.get(0).getAdminArea();
            pin=addresses.get(0).getPostalCode();
            country=addresses.get(0).getCountryName();
            //Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
            Log.e("ADDRESS","ADDRESS-->"+address);
        } catch (Exception ex) {
            ex.printStackTrace();
            adrs = null;
        }
    }

    public void ReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddMoney.this, Manifest.permission.READ_PHONE_STATE)) {
            ActivityCompat.requestPermissions(AddMoney.this, PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(AddMoney.this, PERMISSIONS, REQUEST_PERMISSIONS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
       /* if (requestCode == REQUEST_PERMISSIONS) {
           *//* if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(AddMoney.this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                //  Toast.makeText(AddMoney.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }*//*
        }*/
    }

/*
    private void getEazeBuzzId() {
        String tag_string_req = "EAZE_BUZZ";
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.GET_EAZEBUSS_ID, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "EAZE_BUZZ Response: " + response);
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                try {
                    progressDialog.dismiss();
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
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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


    @Override
    public void onClick(View v) {
        UserData userData=PrefManager.getInstance(AddMoney.this).getUserData();
        if (v==imgBack){
            String back=SplashActivity.getPreferences(Constant.HOME,"");
            if (back.equalsIgnoreCase("home")){
                Intent intent = new Intent(AddMoney.this, HomeActivity.class);
                intent.putExtra(Constant.RECHARGE,"rechar");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                SplashActivity.getPreferences(Constant.HOME,"");
            }else {
                Intent intent = new Intent(AddMoney.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        }
        if (v==btnAddMoney){
            String amount=editTextAmount.getText().toString().trim();
            getRandomNumberString();
            if (amount.isEmpty()){
                editTextAmount.setError("Enter Amount");
                Log.e("DATA","DA-->"+merchant_trxnId+" "+pin);
            }else if (Configuration.hasNetworkConnection(AddMoney.this)&&!merchant_trxnId.isEmpty()){
                Log.e("DATA","DA-->"+merchant_trxnId+" "+pin);
                Intent intentProceed = new Intent(AddMoney.this, PWECouponsActivity.class);
                intentProceed.putExtra("trxn_id",merchant_trxnId);
                intentProceed.putExtra("trxn_amount",Float.valueOf(amount));
                intentProceed.putExtra("trxn_prod_info","Add Money");
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
                intentProceed.putExtra("trxn_address2",address);
                intentProceed.putExtra("trxn_city",city);
                intentProceed.putExtra("trxn_state",state);
                intentProceed.putExtra("trxn_country",country);
                intentProceed.putExtra("trxn_zipcode",pin);
                intentProceed.putExtra("trxn_is_coupon_enabled",0);
                intentProceed.putExtra("trxn_salt",Constant.MERCH_SALT);
                intentProceed.putExtra("unique_id",userData.getUserId());
                intentProceed.putExtra("pay_mode",Constant.PAY_MODE);
                startActivityForResult(intentProceed, StaticDataModel.PWE_REQUEST_CODE);
            }else {
                Toast.makeText(this, "Try again after 2 minutes", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UserData userData=PrefManager.getInstance(AddMoney.this).getUserData();
        assert data != null;
        String result = data.getStringExtra("result");
        String response = data.getStringExtra("payment_response");
        Log.e("TOTELRES","RESPONSE--->"+response+"  \n"+result);
        WebService.sendResponse(response,userData.getUserId(),AddMoney.this,userData.getuType());
        WebService.getBalance(userData.getUserId());
        getRandomNumberString();

        try {
            JSONObject jsonObject=new JSONObject(response);
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            if (jsonObject.has("status")) {
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    String back=SplashActivity.getPreferences(Constant.HOME,"");
                    if (back.equalsIgnoreCase("home")){
                        Intent intent = new Intent(AddMoney.this, HomeActivity.class);
                        intent.putExtra(Constant.RECHARGE,"rechar");
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        SplashActivity.getPreferences(Constant.HOME,"");
                    }else {
                        Intent intent = new Intent(AddMoney.this, MainActivity.class);
                        intent.putExtra(Constant.RESPONSE, response);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
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
    @SuppressLint("DefaultLocale")
    public void getRandomNumberString() {
        UserData userData=PrefManager.getInstance(AddMoney.this).getUserData();
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999)+1000000;
        // this will convert any number sequence into 6 character.
        merchant_trxnId=userData.getUserId()+"TX"+String.format("%06d",number);
        Log.i("NUMBER",""+String.format("%06d", number));
    }

    @Override
    public void onBackPressed() {
        String back=SplashActivity.getPreferences(Constant.HOME,"");
        if (back.equalsIgnoreCase("home")){
            Intent intent = new Intent(AddMoney.this, HomeActivity.class);
            intent.putExtra(Constant.RECHARGE,"rechar");
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            SplashActivity.getPreferences(Constant.HOME,"");
        }else {
            Intent intent = new Intent(AddMoney.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }
}
