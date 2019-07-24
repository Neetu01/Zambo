package com.efficientindia.zambopay.AppConfig;

import android.content.Context;
import android.content.SharedPreferences;

import com.efficientindia.zambopay.Model.UserData;

/**
 * Created by aftab on 4/30/2018.
 */

public class PrefManager {

    private static final String SHARED_PREF_NAME = Constant.PREFS_NAME;
    private static final String KEY_ID="keyid";
    private static final String KEY_USERID="keyuserid";
    private static final String KEY_NAME="keyname";
    private static final String KEY_COMPANY="keycompany";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_MOBILE = "keymobile";
    private static final String KEY_USERTYPE="keyutype";
    private static final String KEY_KYCSTATUS="keykycstatu";
    private static final String KEY_U_STATUS="keyustatius";
    private static final String KEY_OUTLET_STATUS="keyoutlet";
    private static final String KEY_ADD_LINE1="keyline1";
    private static final String KEY_ADD_LINE2="keyline2";
    private static final String KEY_CITY="keycity";
    private static final String KEY_STATE="keystate";
    private static final String KEY_PIN="keypin";
    private static final String KEY_UDISTRIBUTOR="keyudistributor";
    private static final String KEY_PHOTO="keyphoto";
    private static final String KEY_ACARD="keyAcard";
    private static final String KEY_PCARD="keyPcard";
    private static final String KEY_AIMAGE="keyaimage";
    private static final String KEY_PIMAGE="keypimage";
    private static final String KEY_WALLET_STATUS="key_wallet_status";
    private static final String KEY_WALLET_BALANCE="keybalance";

    private static final String TAG_TOKEN = "tagtoken";

    private static final String KEY_TYPEID="keyid";
    private static final String KEY_ACCESSNAME="keyaccessname";
    private static final String KEY_URLACCESS="keyurlaccess";
    private static final String KEY_PAGEACCESS="keypageaccess";

    private static PrefManager mInstance;
    private static Context mCtx;
    private SharedPreferences prefs;
    public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    public static final String SET_NOTIFY = "set_notify";
    SharedPreferences pref;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    SharedPreferences.Editor editor;


    public PrefManager(Context context) {
        mCtx = context;
      //  prefs = context.getSharedPreferences(FIREBASE_CLOUD_MESSAGING, Context.MODE_PRIVATE);
    }


    public static synchronized PrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(UserData userData) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID,userData.getUid());
        editor.putString(KEY_USERID, userData.getUserId());
        editor.putString(KEY_NAME, userData.getName());
        editor.putString(KEY_COMPANY,userData.getCompany());
        editor.putString(KEY_EMAIL,userData.getEmail());
        editor.putString(KEY_MOBILE,userData.getMobile());
        editor.putString(KEY_USERTYPE,userData.getuType());
        editor.putString(KEY_KYCSTATUS,userData.getKycStatus());
        editor.putString(KEY_U_STATUS,userData.getuStatus());
        editor.putString(KEY_OUTLET_STATUS, userData.getOutletStatus());
        editor.putString(KEY_ADD_LINE1, userData.getAddressLine1());
        editor.putString(KEY_ADD_LINE2,userData.getAddressLine2());
        editor.putString(KEY_CITY,userData.getCity());
        editor.putString(KEY_STATE,userData.getState());
        editor.putString(KEY_PIN,userData.getPin());
        editor.putString(KEY_UDISTRIBUTOR,userData.getuDistributor());
        editor.putString(KEY_PHOTO,userData.getPhoto());
        editor.putString(KEY_ACARD,userData.getAdharCard());
        editor.putString(KEY_PCARD,userData.getPanCard());
        editor.putString(KEY_AIMAGE,userData.getAadharImage());
        editor.putString(KEY_PIMAGE,userData.getPanImage());
        editor.putString(KEY_WALLET_STATUS,userData.getWalletStatus());
        editor.putString(KEY_WALLET_BALANCE,userData.getWallet());
        editor.apply();
    }
    public UserData getUserData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserData(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_USERID,null),
                sharedPreferences.getString(KEY_NAME,null),
                sharedPreferences.getString(KEY_COMPANY,null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_MOBILE,null),
                sharedPreferences.getString(KEY_USERTYPE,null),
                sharedPreferences.getString(KEY_KYCSTATUS,null),
                sharedPreferences.getString(KEY_U_STATUS,null),
                sharedPreferences.getString(KEY_OUTLET_STATUS,null),
                sharedPreferences.getString(KEY_ADD_LINE1,null),
                sharedPreferences.getString(KEY_ADD_LINE2,null),
                sharedPreferences.getString(KEY_CITY,null),
                sharedPreferences.getString(KEY_STATE,null),
                sharedPreferences.getString(KEY_PIN,null),
                sharedPreferences.getString(KEY_UDISTRIBUTOR,null),
                sharedPreferences.getString(KEY_PHOTO,null),
                sharedPreferences.getString(KEY_ACARD,null),
                sharedPreferences.getString(KEY_PCARD,null),
                sharedPreferences.getString(KEY_AIMAGE,null),
                sharedPreferences.getString(KEY_PIMAGE,null),
                sharedPreferences.getString(KEY_WALLET_STATUS,null),
                sharedPreferences.getString(KEY_WALLET_BALANCE,null));
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        // mCtx.startActivity(new Intent(mCtx, SplashMainActivity.class));
    }
    public void saveNotificationSubscription(boolean value){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putBoolean(SET_NOTIFY, value);
        edits.apply();
    }
    public boolean hasUserSubscribeToNotification(){
        return prefs.getBoolean(SET_NOTIFY, false);
    }
    public void clearAllSubscriptions(){
        prefs.edit().clear().apply();
    }
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

}
