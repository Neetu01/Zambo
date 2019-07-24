package com.efficientindia.zambopay.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class AspsTransfer extends AppCompatActivity implements View.OnClickListener {
    TextView txtRrn,txtCustNo,txtBankMesg,txtBalanceDetail,txtStatus;
    ImageView imgClose,imgStatus;
    private Button btnGOAEPS;
    private ProgressDialog progressDialog;
    String phone="",roundMobile="",roundPassword="";
    int permissionCheck;
    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asps_transfer);
        imgClose=findViewById(R.id.img_back_aeps);
        progressDialog=new ProgressDialog(AspsTransfer.this,R.style.AppTheme_Dark_Dialog);
        imgClose.setOnClickListener(this);
        imgStatus=findViewById(R.id.img_status);
        permissionCheck = ContextCompat.checkSelfPermission(AspsTransfer.this, Manifest.permission.READ_PHONE_STATE);
        txtStatus=findViewById(R.id.txt_status);
        btnGOAEPS=findViewById(R.id.btn_go_back_aeps);
        btnGOAEPS.setOnClickListener(this);
        txtRrn=findViewById(R.id.txt_rrn);
        txtCustNo=findViewById(R.id.txt_custno);
        txtBalanceDetail=findViewById(R.id.txt_Balance_Details);
        txtBankMesg=findViewById(R.id.txt_bankmessage);
        ReadPhoneStatePermission();
        try {
            String message=getIntent().getStringExtra(Constant.MESSAGE);
            String bankRRN= getIntent().getStringExtra(Constant.BANK_RRN);
            String transTtype= getIntent().getStringExtra(Constant.TRANS_TYPE);
            String amount= getIntent().getStringExtra(Constant.AMOUNT);
            String status= getIntent().getStringExtra(Constant.STATUS);
            Log.e(TAG,"myValue2454--->"+message);
           // String transStatus="",balanceDetail="";
            Log.e("STATUS","STATUS--->"+status);
            txtCustNo.setText(transTtype);
            /*if (jsonObject.has("TXN_Status")){
                transStatus=jsonObject.getString("TXN_Status");
                txtCustNo.setText(transStatus+" for Customer No. : "+jsonObject.getString("CustNo")+" is "+jsonObject.getString("bankmessage"));
            }else {
             //   txtCustNo.setVisibility(View.GONE);
                txtBalanceDetail.setVisibility(View.GONE);
                txtCustNo.setText("Message : "+jsonObject.getString("bankmessage"));
            }
            if (jsonObject.has("Balance_Details")){
                balanceDetail=jsonObject.getString("Balance_Details");
            }
            String statusCode=jsonObject.getString("StatusCode");
            if (statusCode.equalsIgnoreCase("009")){
                imgStatus.setImageResource(R.drawable.failed);
            }else {
                imgStatus.setImageResource(R.drawable.success);
            }
            if (transStatus.equalsIgnoreCase("SUCCESS")){
                imgStatus.setImageResource(R.drawable.success);
            }else {
                imgStatus.setImageResource(R.drawable.failed);
            }*/
            imgStatus.setImageResource(R.drawable.success);
            txtStatus.setText(status);
            txtRrn.setText("RRN : "+bankRRN);
            txtBankMesg.setText(message);
            txtBalanceDetail.setText("Amount : \u20B9 "+amount);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void ReadPhoneStatePermission() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(AspsTransfer.this, Manifest.permission.READ_PHONE_STATE)) {

            ActivityCompat.requestPermissions(AspsTransfer.this, PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(AspsTransfer.this, PERMISSIONS, REQUEST_PERMISSIONS);
        }
        // END_INCLUDE(contacts_permission_request)
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(AspsTransfer.this, "Permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(AspsTransfer.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onClick(View v) {
        final UserData userData=PrefManager.getInstance(AspsTransfer.this).getUserData();
        if (v==imgClose){
            new AlertDialog.Builder(AspsTransfer.this)
                    .setTitle("Really Close?")
                    .setMessage("Do you want to close")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            WebService.getActiveService(userData.getUserId(), userData.getuType());
                            Intent intent =new Intent(AspsTransfer.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                    }).create().show();
        }
        if (v==btnGOAEPS){
            if (Configuration.hasNetworkConnection(AspsTransfer.this)){
                getUserState(userData.getUserId());
            }else {
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public  void getUserState(final String userId) {
        String tag_string_req = "aeps_response";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.AEPS_OUTLET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "AEPS Response: " + response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject1=new JSONObject(response);
                    String status = jsonObject1.getString("status");
                    String message=jsonObject1.getString("message");
                    //  Log.d(TAG, "Response: " + jarray.toString());

                    Log.d(TAG,"status:"+status);
                    if  (status.equals("1")) {

                        Toast.makeText(AspsTransfer.this,
                                message, Toast.LENGTH_LONG).show();
                        if (jsonObject1.has("data")){
                            JSONObject jsonObject=jsonObject1.getJSONObject("data");
                            phone=jsonObject.getString("phone1");
                         //   String outlet_status=jsonObject.getString("outlet_status");
                         //   String kyc_status=jsonObject.getString("kyc_status");
                        }
                        if (jsonObject1.has("authKey")){
                            JSONObject jObj=jsonObject1.getJSONObject("authKey");
                            roundMobile =jObj.getString("mobile");
                            roundPassword=jObj.getString("password");
                        }

                      /*  if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AspsTransfer.this, new String[]
                                            {Manifest.permission.READ_PHONE_STATE},
                                    REQUEST_READ_PHONE_STATE);
                        } else {
                            //TODO*/
                        Intent i = new Intent(AspsTransfer.this, MainActivity.class);
                        startActivity(i);
                        finish();
                      //  }

                    }else {
                        Toast.makeText(AspsTransfer.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "AEPS Error: " + error.getMessage());
                Toast.makeText(AspsTransfer.this,error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        final UserData userData=PrefManager.getInstance(AspsTransfer.this).getUserData();
        new AlertDialog.Builder(AspsTransfer.this)
                .setTitle("Really Close?")
                .setMessage("Do you want to close")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                        Intent intent =new Intent(AspsTransfer.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                }).create().show();
    }
}
