package com.efficientindia.zambopay.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.VolleyLog.TAG;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_CAMERA =111 ,PICK_IMAGE_GALLERY=222;
    TextView txtName,txtUserid,txtEmail,txtMobile;
    private ImageView imgBack,imgChangeImage;
    private EditText editTextCompany,editTextAdd1,editTextAdd2,editTextcity,editTextPincode;
    private AutoCompleteTextView state;
    private Button btnUpdateProfile,btnRedeemAeps;
    ProgressDialog pDialog;
    CircleImageView userImage;
    TextInputLayout inputCompany,inputAdd1,inputAdd2,inputCity,inputPincode;
    public  static final int RequestPermissionCode  = 1 ;
    InputStream inputStreamImg;
    Bitmap bitmap;
    String image ="",imgPath;
    File destination = null;
    WebService webservice;
    TextView txtAmountAeps;
    String stateId="",accountNo="",bankName="",amountAeps;
    BottomNavigationView bottomNavigationView;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        btnRedeemAeps=findViewById(R.id.btn_aeps_redeem);
        txtName=findViewById(R.id.txt_name_user);
        txtUserid=findViewById(R.id.txt_user_id);
        txtEmail=findViewById(R.id.txt_user_email);
        txtMobile=findViewById(R.id.txt_user_mobile);
        userImage=findViewById(R.id.user_image);
        inputAdd1=findViewById(R.id.input_add_1);
        inputCity=findViewById(R.id.input_city);
        imgChangeImage=findViewById(R.id.img_change_profile);
        imgBack=findViewById(R.id.img_back_profile);
        inputPincode=findViewById(R.id.input_pincode);
        inputAdd2=findViewById(R.id.input_add_2);
        inputCompany=findViewById(R.id.input_company);
        pDialog=new ProgressDialog(ProfileActivity.this);
        editTextCompany=findViewById(R.id.edittext_company);
        editTextAdd1=findViewById(R.id.edittext_add_1);
        editTextAdd2=findViewById(R.id.edittext_add_2);
        editTextcity=findViewById(R.id.edittext_city);
        editTextPincode=findViewById(R.id.edittext_pincode);
        state=findViewById(R.id.auto_state);
        btnUpdateProfile=findViewById(R.id.btn_update_profile);
        btnUpdateProfile.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgChangeImage.setOnClickListener(this);
        btnRedeemAeps.setOnClickListener(this);
        webservice=new WebService(ProfileActivity.this);
        webservice.stateId=new ArrayList<>();
        webservice.stateName=new ArrayList<>();


        UserData userData=PrefManager.getInstance(ProfileActivity.this).getUserData();
        txtName.setText(userData.getName());
        txtUserid.setText("User Id : "+userData.getUserId());
        txtEmail.setText("Email id : "+userData.getEmail());
        txtMobile.setText("Mobile : "+userData.getMobile());
        if (userData.getuType().equalsIgnoreCase("CS")) {
            btnRedeemAeps.setVisibility(View.GONE);
        }else {
            btnRedeemAeps.setVisibility(View.VISIBLE);
        }

        if (Configuration.hasNetworkConnection(ProfileActivity.this)){
            getProfile(userData.getUserId());
            getProfileData(userData.getUserId());
        }else {
            Toast.makeText(this, "No Internet connectivity", Toast.LENGTH_SHORT).show();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getProfileData(userData.getUserId());
                handler.postDelayed(this, 2000);
            }
        }, 2000);
        webservice.getStates(ProfileActivity.this, pDialog, state, "");



        state.setOnItemClickListener((parent, view, position, id) -> {

            String sta = parent.getItemAtPosition(position).toString();
            for (int i = 0; i < webservice.stateName.size(); i++) {
                if (webservice.stateName.get(i).equals(sta)) {
                    stateId = webservice.stateId.get(i);

                    state.setText(webservice.stateName.get(i));
                }
                System.out.println("Bank code12-->" + sta + "code12---->" + stateId);
            }

        });
        state.setOnTouchListener((paramView, paramMotionEvent) -> {
            // TODO Auto-generated method stub
            /*operatorList.setText("");*/
            state.showDropDown();
            state.requestFocus();
            return false;
        });

    }

    private void getProfileData(String userId) {

        String tag_string_req = "register_res";



        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PROFILE_DATA, response -> {
                    Log.d(TAG, "Profile Response1: " + response);

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        String message=jsonObject.getString("message");
                        if (status.equals("1")){
                            JSONObject jObj=jsonObject.getJSONObject("data");
                            btnRedeemAeps.setText("Redeem Aeps Wallet ( ₹"+jObj.getString("aepsWallet")+")");
                            amountAeps=jObj.getString("aepsWallet");
                            accountNo=jObj.getString("accountNo");
                            bankName=jObj.getString("bankName");
                            try{
                                txtAmountAeps.setText("₹"+jObj.getString("aepsWallet"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                           /* if (!TextUtils.isEmpty(jObj.getString("uPhoto"))){
                                Picasso.with(ProfileActivity.this).load(jObj.getString("uPhoto")).fit().centerCrop()
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.user)
                                        .into(userImage);
                            }else if (jObj.getString("uPhoto").equalsIgnoreCase("http://www.zambo.in/uploads/userDocs/ZAM715269/")){
                                userImage.setImageResource(R.drawable.logo);
                            }
                            if (!TextUtils.isEmpty(jObj.getString("uCompany"))){
                                editTextCompany.setText(jObj.getString("uCompany"));
                                editTextCompany.setCursorVisible(false);
                            }else {
                                inputCompany.setHint(getResources().getString(R.string.enter_company));
                                editTextCompany.setCursorVisible(true);
                            }

                            if (!jObj.getString("addressLine1").equalsIgnoreCase("null")){
                                editTextAdd1.setText(jObj.getString("addressLine1"));
                            }else {
                                inputAdd1.setHint(getResources().getString(R.string.add_line_one));
                            }

                            if (!jObj.getString("addressLine2").equalsIgnoreCase("null")){
                                editTextAdd2.setText(jObj.getString("addressLine2"));
                            }else {
                                inputAdd2.setHint(getResources().getString(R.string.add_line_two));
                            }

                            if (!jObj.getString("city").equalsIgnoreCase("null")){
                                editTextcity.setText(jObj.getString("city"));
                            }else {
                                inputCity.setHint(getResources().getString(R.string.city));
                            }
                            if (!jObj.getString("stateName").equalsIgnoreCase("null")){
                                state.setText(jObj.getString("stateName"));
                            }else {
                                state.setHint(getResources().getString(R.string.state));
                            }

                            if (!jObj.getString("pin").equalsIgnoreCase("null")){
                                editTextPincode.setText(jObj.getString("pin"));
                            }else {
                                inputPincode.setHint(getResources().getString(R.string.pin));
                            }*/

                        }else {
                            Toast.makeText(ProfileActivity.this,
                                    message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
            Log.e(TAG, "Profile Error: " + error.getMessage());
            Toast.makeText(ProfileActivity.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
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


    private void getProfile(final String userId) {
        String tag_string_req = "register_res";

        pDialog.setMessage("Loading Data...");
        pDialog.setIndeterminate(true);
        pDialog.show();


        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PROFILE_DATA, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile Response: " + response);

                pDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String message=jsonObject.getString("message");
                    if (status.equals("1")){
                        JSONObject jObj=jsonObject.getJSONObject("data");
                        btnRedeemAeps.setText("Redeem Aeps Wallet ( ₹"+jObj.getString("aepsWallet")+")");
                        amountAeps=jObj.getString("aepsWallet");
                        accountNo=jObj.getString("accountNo");
                        bankName=jObj.getString("bankName");
                        if (!TextUtils.isEmpty(jObj.getString("uPhoto"))){
                            Picasso.with(ProfileActivity.this).load(jObj.getString("uPhoto")).fit().centerCrop()
                                    .placeholder(R.drawable.user)
                                    .error(R.drawable.user)
                                    .into(userImage);
                        }else if (jObj.getString("uPhoto").equalsIgnoreCase("http://www.zambo.in/uploads/userDocs/ZAM715269/")){
                            userImage.setImageResource(R.drawable.logo);
                        }
                        if (!TextUtils.isEmpty(jObj.getString("uCompany"))){
                            editTextCompany.setText(jObj.getString("uCompany"));
                            editTextCompany.setCursorVisible(false);
                        }else {
                            inputCompany.setHint(getResources().getString(R.string.enter_company));
                            editTextCompany.setCursorVisible(true);
                        }

                        if (!jObj.getString("addressLine1").equalsIgnoreCase("null")){
                            editTextAdd1.setText(jObj.getString("addressLine1"));
                        }else {
                            inputAdd1.setHint(getResources().getString(R.string.add_line_one));
                        }

                        if (!jObj.getString("addressLine2").equalsIgnoreCase("null")){
                            editTextAdd2.setText(jObj.getString("addressLine2"));
                        }else {
                            inputAdd2.setHint(getResources().getString(R.string.add_line_two));
                        }

                        if (!jObj.getString("city").equalsIgnoreCase("null")){
                            editTextcity.setText(jObj.getString("city"));
                        }else {
                            inputCity.setHint(getResources().getString(R.string.city));
                        }
                        if (!jObj.getString("stateName").equalsIgnoreCase("null")){
                            state.setText(jObj.getString("stateName"));
                        }else {
                            state.setHint(getResources().getString(R.string.state));
                        }

                        if (!jObj.getString("pin").equalsIgnoreCase("null")){
                            editTextPincode.setText(jObj.getString("pin"));
                        }else {
                            inputPincode.setHint(getResources().getString(R.string.pin));
                        }

                    }else {
                        Toast.makeText(ProfileActivity.this,
                                message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, error -> {
            Log.e(TAG, "Profile Error: " + error.getMessage());
            Toast.makeText(ProfileActivity.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        UserData userData=PrefManager.getInstance(ProfileActivity.this).getUserData();
        if (v==btnRedeemAeps){
            if (TextUtils.isEmpty(accountNo)||TextUtils.isEmpty(bankName)){
               /* Configuration.openPopupUpDown(ProfileActivity.this, R.style.Dialod_UpDown,
                        "error", "Your account detaild are not available\n" +
                                "please ");*/
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Final submit?")
                        .setMessage("You can not transfer balance to  Bank account as your account details are not available." +
                                "Please continue to transfer balance to main wallet otherwise cancel.")
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.continu, (arg0, arg1) -> openDialog("hide")).create().show();
            }else {
                openDialog("show");
            }
        }
        if (v==imgBack){
            Intent intent =new Intent(ProfileActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
        }
        if (v==btnUpdateProfile){
            String company=editTextCompany.getText().toString();
            String addLine1=editTextAdd1.getText().toString();
            String addLine2=editTextAdd2.getText().toString();
            String city=editTextcity.getText().toString();
            String stat=state.getText().toString();
            String pincode=editTextPincode.getText().toString();
            String imagePhoto=image;
            if (company.isEmpty()){
                editTextCompany.setError("Enter company Name");
            }else if (addLine1.isEmpty()){
                editTextAdd1.setError("Enter Address");
            }else if (addLine2.isEmpty()){
                editTextAdd2.setError("Enter Address");
            }else if (city.isEmpty()){
                editTextcity.setError("Enter city");
            }else if (stat.isEmpty()){
                state.setError("Select State");
            }else if (pincode.isEmpty()){
             editTextPincode.setError("Enter pincode");
            }else if (stateId.isEmpty()){
                try{
                    String st=  state.getText().toString();

                    for (int i = 0; i < webservice.stateName.size(); i++) {
                        if (webservice.stateName.get(i).equals(st)) {
                            stateId = webservice.stateId.get(i);
                            }
                    }
                    System.out.println("State code12-->" + st + "code12---->" + stateId);
                }catch (Exception ignored){}
            }else if (Configuration.hasNetworkConnection(ProfileActivity.this)){
                WebService.updateProfile(userData.getUserId(),company,addLine1,addLine2,city,stateId,pincode,
                        imagePhoto,ProfileActivity.this,pDialog);
            }else {
                Toast.makeText(this, "No Internet Connectivity", Toast.LENGTH_SHORT).show();
            }
        }
        if (v==imgChangeImage){
            EnableRuntimePermissionToAccessCamera();
            selectImage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private void openDialog(String type) {
        UserData userData = PrefManager.getInstance(ProfileActivity.this).getUserData();
        final Dialog dialog=new Dialog(ProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_redeem);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        txtAmountAeps=dialog.findViewById(R.id.txt_amount_aeps);
        TextView txtAccNumber=dialog.findViewById(R.id.txt_account_number);
        TextView txtBankName=dialog.findViewById(R.id.txt_bank_name);
        LinearLayout lnAccountNumber=dialog.findViewById(R.id.ln_account_number);
        LinearLayout lnBankName=dialog.findViewById(R.id.ln_bank_name);
        LinearLayout lnMainWallet=dialog.findViewById(R.id.ln_main_wallet);
        LinearLayout lnBankAccount=dialog.findViewById(R.id.ln_bank_account);
        EditText editTextMainWallet=dialog.findViewById(R.id.edittext_wallet_amount);
        EditText editTextBankAccount=dialog.findViewById(R.id.edittext_bank_amount);
        Button btnContinue=dialog.findViewById(R.id.btn_continue_redeem);
        Button btnCancel=dialog.findViewById(R.id.btn_no_redeem);
        txtAmountAeps.setText(": ₹"+amountAeps);

        if (type.equalsIgnoreCase("hide")){
            lnAccountNumber.setVisibility(View.GONE);
            lnBankAccount.setVisibility(View.GONE);
            lnBankName.setVisibility(View.GONE);
            lnMainWallet.setVisibility(View.VISIBLE);
            editTextMainWallet.setText("0");
            editTextBankAccount.setText("0");
        }else {
            lnAccountNumber.setVisibility(View.VISIBLE);
            lnBankAccount.setVisibility(View.VISIBLE);
            lnBankName.setVisibility(View.VISIBLE);
            lnMainWallet.setVisibility(View.VISIBLE);
            txtAccNumber.setText(": "+accountNo);
            txtBankName.setText(": "+bankName);
            editTextMainWallet.setText("0");
            editTextBankAccount.setText("0");
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnContinue.setOnClickListener(v -> {
            String mainWallet=editTextMainWallet.getText().toString();
            String bankAccount=editTextBankAccount.getText().toString();

            if (mainWallet.isEmpty()){
                editTextMainWallet.setError("Enter Amount");
                editTextMainWallet.requestFocus();
            }else if (bankAccount.isEmpty()){
                editTextBankAccount.setError("Enter Amount");
                editTextBankAccount.requestFocus();
            }else if (Float.valueOf(mainWallet) + Float.valueOf(bankAccount) >Float.valueOf(amountAeps)){
                Configuration.openPopupUpDown(ProfileActivity.this, R.style.Dialod_UpDown,
                        "error", "Redeem amount should be less than available aeps amount balance.\n" +
                                "i.e To Main Wallet + To Bank Account should be less than AEPS Balance.");
            }else if (Configuration.hasNetworkConnection(ProfileActivity.this)){
                redeemBalance(mainWallet,bankAccount,dialog,userData.getUserId());
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDown(ProfileActivity.this, R.style.Dialod_UpDown,
                            "internetError", "No Internet Conectivity");
                }
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void redeemBalance(String mainWallet, String bankAccount, Dialog dialog, String userId) {
        String tag_string_req = "redeem_balance";
        pDialog.setMessage("Please wait...");
        pDialog.show();
        HttpsTrustManager.allowAllSSL();
        try{
            Configuration.hideKeyboardFrom(ProfileActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REDEEM_BALANCE_AEPS, response -> {
                    Log.d(TAG, "Redeem Response: " + response+" "+bankAccount+" "+mainWallet);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        if  (status.equals("1")) {
                            dialog.dismiss();
                            openPopup(jsonObject1.getString("message"),"S");
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(ProfileActivity.this, R.style.Dialod_UpDown,
                                        "error", jsonObject1.getString("message"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e(TAG, "Redeem Error: " + error.getMessage());
                    pDialog.dismiss();
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.TO_MAIN,mainWallet);
                params.put(Constant.TO_BANK,bankAccount);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @SuppressLint("SetTextI18n")
    private void openPopup(String message, final String type) {
        final Dialog dialg=new Dialog(ProfileActivity.this);
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
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(ProfileActivity.this,"CAMERA permission allows us to Access CAMERA app",
                    Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent();
                            pickPhoto.setType("image/*");
                            pickPhoto.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(pickPhoto, "Select Image"), PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(ProfileActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* UserData userData=PrefManager.getInstance(ProfileActivity.this).getUserData();
        String userId=userData.getUserId();*/
        inputStreamImg= null;
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK && data!=null) {
            try {
               // Uri selectedImage = data.getData();
                bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                byte[] imageBytes = bytes.toByteArray();
                image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                // imgPathDL= destinationDl.getName();
                 userImage.setImageBitmap(bitmap);
               // txtDl.setText(destination.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }  if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data!=null) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");
                imgPath = getRealPathFromURI(selectedImage);
                byte[] imageBytes = bytes.toByteArray();
                image= Base64.encodeToString(imageBytes, Base64.DEFAULT);
                //  imgPathDL=""+selectedImage;
                destination = new File(imgPath);
                userImage.setImageBitmap(bitmap);

               // txt.setText(destinationDl.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
