package com.efficientindia.zambopay.AppConfig;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.efficientindia.zambopay.Activity.AddMoney;
import com.efficientindia.zambopay.Activity.BbpsActivity;
import com.efficientindia.zambopay.Activity.CustomerHomeActivity;
import com.efficientindia.zambopay.Activity.Dmt_Transfer;
import com.efficientindia.zambopay.Activity.HomeActivity;
import com.efficientindia.zambopay.Activity.LoginActivity;
import com.efficientindia.zambopay.Activity.MainActivity;
import com.efficientindia.zambopay.Activity.ProfileActivity;
import com.efficientindia.zambopay.Activity.SplashActivity;
import com.efficientindia.zambopay.Activity.Transfer;
import com.efficientindia.zambopay.Fragment.HomeFragment;
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by aftab on 5/9/2018.
 */


public class WebService {

    private Context mContext;
    public ArrayList<String> stateId;
    public ArrayList<String> stateName;
    public static ArrayList<String> operatorId=new ArrayList<>();
    public static ArrayList<String> operatorName=new ArrayList<>();
    public static ArrayList<String> bankCode;
    public static ArrayList<String> bankName;
    public static ArrayList<String> masterIfsc;

    public WebService(Context context) {
        mContext=context;
    }

   /* public WebService(Context context) {
        mContext=context;
    }*/

    public static void login(final String user, final String pass, final ProgressDialog progressDialog,
                             final FragmentActivity loginActivity, final SessionManager session) {
        String tag_string_req = "login_res";

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LOGIN, response -> {
                    Log.d(TAG, "Login Response: " + response+"----->");
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        //  Log.d(TAG, "Response: " + jarray.toString());
                        Log.d(TAG,"status:"+status);
                        if  (status.equals("1")) {
                            JSONObject jsonObject=jsonObject1.getJSONObject("data");

                            session.setLogin(true);

                            UserData userData = new UserData(
                                    jsonObject.getString("uId"),
                                    jsonObject.getString("uUid"),
                                    jsonObject.getString("uName"),
                                    jsonObject.getString("uCompany"),
                                    jsonObject.getString("uEmail"),
                                    jsonObject.getString("uMobile"),
                                    jsonObject.getString("uType"),
                                    jsonObject.getString("kycStatus"),
                                    jsonObject.getString("uStatus"),
                                    jsonObject.getString("uOutletStatus"),
                                    jsonObject.getString("addressLine1"),
                                    jsonObject.getString("addressLine2"),
                                    jsonObject.getString("city"),
                                    jsonObject.getString("state"),
                                    jsonObject.getString("pin"),
                                    jsonObject.getString("uDistributor"),
                                    jsonObject.getString("uPhoto"),
                                    jsonObject.getString("uAadhaarCard"),
                                    jsonObject.getString("uPanCard"),
                                    jsonObject.getString("uAadhaarImage"),
                                    jsonObject.getString("uPanImage"),
                                    jsonObject.getString("walletStatus"),
                                    jsonObject.getString("walletBalance")
                            );
                            PrefManager.getInstance(loginActivity).userLogin(userData);
                            Intent intent =new Intent(loginActivity,MainActivity.class);
                            loginActivity.startActivity(intent);
                            loginActivity.finish();
                            loginActivity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                        }else {
                            Toast.makeText(loginActivity,
                                    message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(loginActivity,
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.LOGIN_MOBILE, user);
                params.put(Constant.PASSWORD,pass);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void register(final String nam, final String emai, final String mob, final String pass,
                                final ProgressDialog pDialog, final FragmentActivity activity) {
        String tag_string_req = "register_res";

        pDialog.setMessage("Registering...");
        pDialog.show();

        try{Configuration.hideKeyboardFrom(activity);}catch (Exception ignored){}

         HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REGISTER, response -> {
                    Log.d(TAG, "Register Response: " + response);

                    pDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        String message=jsonObject.getString("message");
                        if (status.equals("1")){
                            Toast.makeText(activity,
                                    message, Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(activity,LoginActivity.class);
                            activity.startActivity(intent);
                        }else {
                            Toast.makeText(activity,
                                    message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
                    Log.e(TAG, "Register Error: " + error.getMessage());
                    Toast.makeText(activity,
                            error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.REG_NAME,nam);
                params.put(Constant.REG_EMAIL,emai);
                params.put(Constant.REG_MOBILE,mob);
                params.put(Constant.REG_PASSWORD,pass);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void getBalance(final String userId){

        String tag_string_req = "get_balance";
        HttpsTrustManager.allowAllSSL();
        @SuppressLint("SetTextI18n")
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PROFILE_DATA, response -> {
                    Log.d(TAG, "Register Response: " + response);

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                      //String message=jsonObject.getString("message");
                        if (status.equals("1")){
                            JSONObject jObj=jsonObject.getJSONObject("data");
                            SplashActivity.savePreferences(Constant.BALANCE,jObj.getString("walletBalance"));
                            try {
                                Dmt_Transfer.txtBalance.setText("Bal. \u20B9" + jObj.getString("walletBalance"));
                                Dmt_Transfer.progressBar.setVisibility(View.GONE);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    HomeActivity.txt_balance_rechargee.setText("Wallet Balance : \u20B9" + jObj.getString("walletBalance"));
                                    HomeActivity.progressBar.setVisibility(View.GONE);
                                }

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                BbpsActivity.txtBalanceBbps.setText("\u20B9" + jObj.getString("walletBalance"));
                                BbpsActivity.progressBar.setVisibility(View.GONE);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                HomeFragment.balance.setText("Balance : \u20B9" + jObj.getString("walletBalance"));
                                HomeFragment.progressBar.setVisibility(View.GONE);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                CustomerHomeActivity.txtBalance.setText("Wallet Balance : \u20B9" + jObj.getString("walletBalance"));
                                CustomerHomeActivity.progressBar.setVisibility(View.GONE);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    MainActivity.ui_hot.setText("\u20B9 " + jObj.getString("walletBalance"));
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            try{
                                Transfer.txtBalance.setText("Wallet Balance : \u20B9" + jObj.getString("walletBalance"));
                                Transfer.progressBar.setVisibility(View.GONE);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            try {
                                AddMoney.txtBalance.setText(Html.fromHtml("Available Balance: <b>\u20B9" + jObj.getString("walletBalance")+"</b>"));
                                AddMoney.progressBar.setVisibility(View.GONE);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(TAG, "Register Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void updateProfile(final String userId, final String company, final String addLine1, final String addLine2,
                                     final String city, final String stat, final String pincode, final String imagePhoto,
                                     final ProfileActivity profileActivity, final ProgressDialog pDialog) {
        String tag_string_req = "profile_update";

        pDialog.setMessage("Updating...");
        pDialog.show();

        Log.e(TAG,"UPDATE PROFILE--->"+userId+" "+company+" "+addLine1+" "+addLine2+" "+city+" "+stat+" "+pincode+" "+imagePhoto);

        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.UPDATE_PROFILE, response -> {
                    Log.d(TAG, "Update Response: " + response);

                    pDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        String message=jsonObject.getString("message");
                       /* if (status.equals("1")){
                            Toast.makeText(profileActivity,
                                    message, Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(profileActivity,
                                    message, Toast.LENGTH_LONG).show();
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
            Log.e(TAG, "Update Error: " + error.getMessage());
         //   Toast.makeText(profileActivity,
              //      error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                params.put(Constant.COMPANY_NAME,company);
                params.put(Constant.ADDRESS_LINE1,addLine1);
                params.put(Constant.ADDRESS_LINE2,addLine2);
                params.put(Constant.CITY,city);
                params.put(Constant.STATE,stat);
                params.put(Constant.PINCODE,pincode);
                params.put(Constant.USER_PHOTO,imagePhoto);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static void getActiveService(final String userId, final String userType) {
        String tag_string_req = "active_service";
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ACTIVE_SERVICE, response -> {
                    Log.d(Constraints.TAG, "Active Service Response: " + response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.has("status")){
                            JSONArray jsonArray=jsonObject.getJSONArray("status");
                            if (!jsonArray.isNull(0)){
                                for (int i = 0; i< jsonArray.length(); i++) {
                                    JSONObject jobject = jsonArray.getJSONObject(i);

                                    String  id = jobject.getString("subServiceId");
                                    String  status=jobject.getString("subServicestatus");
                                    String mainId=jobject.getString("mainId");
                                    String mainStatus=jobject.getString("mainStatus");
                                    if (userType.equalsIgnoreCase("CS")) {
                                        if (id.equalsIgnoreCase("19")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_PREPAID, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_PREPAID, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("20")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_DTH, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_DTH, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("21")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_POSTPAID, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_POSTPAID, "unhide");
                                            }
                                        }
                                        if (mainId.equalsIgnoreCase("9")) {
                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.RECHARGE_TWO, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.RECHARGE_TWO, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("22")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_LANDLINE, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_LANDLINE, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("23")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_BROADBAND, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_BROADBAND, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("24")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_ELECTRICITY, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_ELECTRICITY, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("25")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_GAS, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_GAS, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("26")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_WATER, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_WATER, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("28")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_INSURANCE, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_INSURANCE, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("27")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_CREDITCARD, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_CREDITCARD, "unhide");
                                            }
                                        }
                                    }else {
                                        if (mainId.equalsIgnoreCase("19")){

                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.AEPS_2, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.AEPS_2, "unhide");
                                            }
                                        }
                                        if (mainId.equalsIgnoreCase("13")){
                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.AEPS_1, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.AEPS_1, "unhide");
                                            }
                                        }
                                        if (mainId.equalsIgnoreCase("17")){
                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.BBPS, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.BBPS, "unhide");
                                            }
                                        }
                                        if (mainId.equalsIgnoreCase("14")){
                                            Log.d("tag","14"+mainStatus);
                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.DMT_1, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.DMT_1, "unhide");
                                            }
                                        }
                                        if (mainId.equalsIgnoreCase("18")){
                                            Log.d("tag","18"+mainStatus);
                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.DMT_2, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.DMT_2, "unhide");
                                            }
                                        }
                                        if (mainId.equalsIgnoreCase("22")){
                                            Log.d("tag","22"+mainStatus);
                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.DMT_3, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.DMT_3, "unhide");
                                            }
                                        }
                                        if (mainId.equalsIgnoreCase("24")){
                                            Log.d("tag","24"+mainStatus);
                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.DMT_4, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.DMT_4, "unhide");
                                            }
                                        }
                                        if (mainId.equalsIgnoreCase("20")){
                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.RECHARGE_THREE, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.RECHARGE_THREE, "unhide");
                                            }
                                        }if (mainId.equalsIgnoreCase("9")){
                                            if (!mainStatus.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.RECHARGE_TWO, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.RECHARGE_TWO, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("19")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_PREPAID, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_PREPAID, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("20")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_DTH, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_DTH, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("21")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_POSTPAID, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_POSTPAID, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("22")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_LANDLINE, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_LANDLINE, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("23")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_BROADBAND, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_BROADBAND, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("24")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_ELECTRICITY, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_ELECTRICITY, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("25")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_GAS, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_GAS, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("26")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_WATER, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_WATER, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("28")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_INSURANCE, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_INSURANCE, "unhide");
                                            }
                                        }
                                        if (id.equalsIgnoreCase("27")) {
                                            if (!status.equalsIgnoreCase("A")) {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_CREDITCARD, "hide");
                                            } else {
                                                SplashActivity.savePreferences(Constant.CUSTOMER_CREDITCARD, "unhide");
                                            }
                                        }
                                    }

                                   /* if (id.equalsIgnoreCase("1")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.PREPAID,status);
                                        if (!status.equalsIgnoreCase("A")){
                                            SplashActivity.savePreferences(Constant.MOBILE_PREPAID,"hide");
                                        }else {
                                            SplashActivity.savePreferences(Constant.MOBILE_PREPAID,"unhide");
                                        }
                                    }
                                    if (id.equalsIgnoreCase("2")){
                                        //Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.DTH,status);
                                    }
                                    if (id.equalsIgnoreCase("3")){
                                        SplashActivity.savePreferences(Constant.DATACARD,status);
                                       // Log.e("STATUS","STATUS-->"+status);
                                    }
                                    if (id.equalsIgnoreCase("4")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.POSTPAID,status);
                                        if (!status.equalsIgnoreCase("A")){
                                            SplashActivity.savePreferences(Constant.MOBILE_POSTPAID,"hide");
                                        }else {
                                            SplashActivity.savePreferences(Constant.MOBILE_POSTPAID,"unhide");
                                        }
                                    }
                                    if (id.equalsIgnoreCase("5")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.ELECTRICITY,status);
                                    }
                                    if (id.equalsIgnoreCase("6")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.GAS,status);
                                    }
                                    if (id.equalsIgnoreCase("7")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.LANDLINE,status);
                                    }
                                    if (id.equalsIgnoreCase("8")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.INSURANCE,status);
                                    }
                                    if (mainId.equalsIgnoreCase("13")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.AEPS,status);
                                    }
                                    if (mainId.equalsIgnoreCase("14")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                    }*/
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(Constraints.TAG, "Active Service Error: " + error.getMessage())) {
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

    public static void sendResponse(final String respon, final String userId, final AddMoney addMoney, final String type) {
        String tag_string_req = "transaction";
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAYMENT_TRANSACTION, response -> {
                    Log.d(TAG, "transaction Response: " + response);
                    Log.d(TAG, "transaction Response: " + respon+" "+userId);

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");

                        if (status.equals("0")){
                            Toast.makeText(addMoney, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            getBalance(userId);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(TAG, "transaction Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                params.put(Constant.RESPONSE,respon);
                params.put(Constant.USER_TYPE,type);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getStates(final Context context, final ProgressDialog progressDialog, final AutoCompleteTextView state,
                             final String id ) {
        Configuration.showDialog("Please wait...", progressDialog);
        //countries.add("---Select Country---");
        System.out.println("id-->"+stateName+" "+id+"--->"+mContext.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.GET_STATES,
                response -> {

                    try {

                        System.out.println("Bank response--->" + response);
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");

                        if (status.equalsIgnoreCase("1")){
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            stateId = new ArrayList<>();
                            stateName = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                stateId.add(jsonObject1.getString("id"));
                                stateName.add(jsonObject1.getString("name"));
                                //  list.put(bankName.get(i),shortName.get(i));
                            }
                            ArrayAdapter<String> counryAdapter = new ArrayAdapter<>(context, R.layout.spinner_item,
                                    R.id.spinner_text,stateName );
                            state.setAdapter(counryAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        progressDialog.dismiss();
                    }

                }, error -> {

                    progressDialog.dismiss();
                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void getOperatorList(final String service, final FragmentActivity activity, final ProgressDialog progressDialog) {
        Configuration.showDialog("Please wait...", progressDialog);
        progressDialog.setCanceledOnTouchOutside(false);
        HttpsTrustManager.allowAllSSL();
        System.out.println("service-->"+service);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.ROUND_PAY_OPERATOR,
                response -> {
                    progressDialog.dismiss();
                    try {
                        operatorId.clear();
                        operatorName.clear();
                        System.out.println("Operator response--->" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        operatorName.add(0, "Select Operator");
                        operatorId.add(0, "0");
                        if (status.equalsIgnoreCase("S")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                operatorName.add(jsonObject1.getString("text"));
                                operatorId.add(jsonObject1.getString("id"));
                            }
                            Intent intent =new Intent(activity, CustomerHomeActivity.class);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDownBack(activity, R.style.Dialod_UpDown, "error", "main","Something went wrong\nTry after sometime" +
                                        ", Thanks");
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        progressDialog.dismiss();
                    }

                },
                error -> {

                    progressDialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDown(activity, R.style.Dialod_UpDown, "error", "Something went wrong\nTry after sometime" +
                                ", Thanks");
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
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public static void getBankDmt2(Context context, ProgressDialog progressDialog,
                                   AutoCompleteTextView bankname, String id) {

        Configuration.showDialog("Please wait...", progressDialog);
        //countries.add("---Select Country---");
        System.out.println("id-->"+bankname+" "+id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.BANK_LIST_DMT2,
                response -> {

                    try {

                        System.out.println("Bank response--->" + response);
                        bankCode = new ArrayList<>();
                        bankName = new ArrayList<>();
                        masterIfsc = new ArrayList<>();
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("1")){
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                bankName.add(jsonObject1.getString("bankname"));
                                bankCode.add(jsonObject1.getString("bankid"));
                                masterIfsc.add(jsonObject1.getString("masterifsc"));
                            }
                        }

                        ArrayAdapter<String> counryAdapter = new ArrayAdapter<>(context, R.layout.spinner_item,
                                R.id.spinner_text, bankName);
                        bankname.setAdapter(counryAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        //  progressDialog.isShowing();
                        progressDialog.dismiss();
                    }

                },
                error -> {
                    Log.e("PASS ERROR","ERROR--->"+error.toString());

                    //  progressDialog.isShowing();
                    progressDialog.dismiss();
                    //  Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
                }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID, id);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}
