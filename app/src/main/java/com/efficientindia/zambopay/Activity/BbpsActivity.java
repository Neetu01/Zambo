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

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Constraints;

import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easebuzz.payment.kit.PWECouponsActivity;
import com.efficientindia.zambopay.Adapter.MenuItemHomeAdapter;
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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import aeps.roundpay.solutions.com.mylibrary.AEPS2.GetLocation;
import datamodels.StaticDataModel;

public class BbpsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = BbpsActivity.class.getSimpleName();
    private ImageView imgBack;
    private ScrollView scrollView;
    private CardView cardView;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtTitle,txtBalanceBbps;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;
    Button btnBbps,btnFetchBill,btnPaybill;
    private Spinner spinnerOperatorList;
    TextInputLayout inputFirst,inputSecond,inputThird,inputFour;
    EditText editTextFirst,editTextSecond,editTextThird,editTextFour;
    String service,opId,MinLength1="0",MaxLength1="5",MinLength2="0",MaxLength2="5",MinLength3="0",MaxLength3="5",MinLength4="0",MaxLength4="5",billerMode="",
            name1="",name2="",name3="",name4="",billerId,ipAddress;
    String parameter1,parameter2,parameter3,parameter4;
    TextView txtService,txtCustNo,txtCustName,txtBillNo,txtBillPeriod,txtBillDate,txtDueDate,txtDueAmount,txtCancel;
    String payment="";

    public String[] nameItem={"Mobile Postpaid ","DTH","Broadband Postpaid","Broadband Prepaid",
            "Electricity","Landline","Gas","Water"};
    ExpandableRelativeLayout expandableRelativeLayout;
    public Integer[] mThumbIds = {R.drawable.postpaidcolor,R.drawable.dthcolor,
            R.drawable.broadbandprepaidcolor,R.drawable.broadbandpostpaidcolor,R.drawable.electricitycolor,R.drawable.landlinecolor,
            R.drawable.gascolor,R.drawable.watercolor};

    GridView gridViewBbps;
    private ProgressDialog progressDialog;
    private ArrayList<String> opratorName;
    private ArrayList<String> opratorId;
    private String providerName;
    private String serviceName;
    private String balance="0";
    private String merchant_trxnId;
    private String address = "";
    private String city="";
    private String state="";
    private String pin="";
    private String country="";
    private String first="";
    private String second="";
    private String third="";
    private String four="";
    private String transId="";
    Dialog dialog;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbps);
        btnFetchBill=findViewById(R.id.btn_fetchbill);
        txtBalanceBbps=findViewById(R.id.txt_balance_bbps);
        progressBar=findViewById(R.id.progressbar_bbps);
        opratorName = new ArrayList<>();
        opratorId = new ArrayList<>();
        progressDialog=new ProgressDialog(BbpsActivity.this);
        gridViewBbps=findViewById(R.id.gridview_bbps);
        inputFirst=findViewById(R.id.input_first);
        inputSecond=findViewById(R.id.input_second);
        inputThird=findViewById(R.id.input_third);
        inputFour=findViewById(R.id.input_four);
        spinnerOperatorList=findViewById(R.id.operator_list_bbps);
        btnBbps=findViewById(R.id.btn_bbps);
        editTextFirst=findViewById(R.id.edittext_first);
        editTextSecond=findViewById(R.id.edittext_second);
        editTextThird=findViewById(R.id.edittext_three);
        editTextFour=findViewById(R.id.edittext_four);
        expandableRelativeLayout=findViewById(R.id.expandablelayout);
       // txtTitle=findViewById(R.id.txt_title_bbps);
        scrollView=findViewById(R.id.scrollview_bbps);
        cardView=findViewById(R.id.cardview_bbps);
        imgBack=findViewById(R.id.img_back_bbps);
        imgBack.setOnClickListener(this);
        btnBbps.setOnClickListener(this);
        MenuItemHomeAdapter adapter = new MenuItemHomeAdapter(BbpsActivity.this,nameItem,mThumbIds);
        gridViewBbps.setAdapter(adapter);
        gridViewBbps.setOnItemClickListener(this);
        scrollView.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
        btnFetchBill.setOnClickListener(this);
        getLocalIpAddress();
        if(expandableRelativeLayout.isExpanded())
            expandableRelativeLayout.toggle();
        GetLocation Location = new GetLocation(this);
        getAddress(Location.getLatitude(),Location.getLongitude());
        UserData userData=PrefManager.getInstance(BbpsActivity.this).getUserData();
        if (Configuration.hasNetworkConnection(BbpsActivity.this)){
            WebService.getBalance(userData.getUserId());
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"internetError",
                        "No internet connectivity");
            }
        }
        try{
            balance=SplashActivity.getPreferences(Constant.BALANCE,"");
        }catch (Exception e){
            e.printStackTrace();
        }

        spinnerOperatorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String operator = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < opratorName.size(); i++) {
                    if (opratorName.get(i).equals(operator)){
                        opId = opratorId.get(i);
                        providerName=opratorName.get(i);
                    }
                }
                System.out.println("Operator code-->" + opId + ", Operator Name---->" + operator);
                if (!opId.equalsIgnoreCase("0")) {
                    try {
                        inputFirst.setVisibility(View.GONE);
                        editTextFirst.setVisibility(View.GONE);
                        editTextFirst.setText("");
                        inputSecond.setVisibility(View.GONE);
                        editTextSecond.setVisibility(View.GONE);
                        editTextSecond.setText("");
                        inputThird.setVisibility(View.GONE);
                        editTextThird.setVisibility(View.GONE);
                        editTextThird.setText("");

                        inputFour.setVisibility(View.GONE);
                        editTextFour.setVisibility(View.GONE);
                        editTextFour.setText("");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    getParameter(opId);
                    getBillerMode(opId);
                  /*  if (billerMode.equalsIgnoreCase("2")){
                        inputSecond.setVisibility(View.VISIBLE);
                        btnFetchBill.setVisibility(View.GONE);
                        inputSecond.setHint("Enter Amount \u20B9");
                        btnBbps.setVisibility(View.VISIBLE);
                        editTextSecond.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        editTextSecond.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.valueOf(MaxLength)) });
                    }else {
                        btnFetchBill.setVisibility(View.VISIBLE);
                        btnBbps.setVisibility(View.GONE);
                    }*/
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.wallet1)
        {
            if(expandableRelativeLayout.isExpanded())
            expandableRelativeLayout.toggle();
            else
                expandableRelativeLayout.toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        UserData userData= PrefManager.getInstance(BbpsActivity.this).getUserData();
        if (v==imgBack){
            if (scrollView.isShown()){
                cardView.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                txtTitle.setText(R.string.bbps);
                btnFetchBill.setVisibility(View.GONE);
                inputFirst.setVisibility(View.GONE);
                inputSecond.setVisibility(View.GONE);
                btnBbps.setVisibility(View.GONE);

                editTextFirst.setText("");
                editTextSecond.setText("");
                try{
                    Configuration.hideKeyboardFrom(BbpsActivity.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                Intent intent = new Intent(BbpsActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        }
        if (v==btnFetchBill){
            first=editTextFirst.getText().toString();
            payment="bill";
           // String third ,four, second;
            if (inputSecond.isShown()) {
                second = editTextSecond.getText().toString();
            }else {
                second="";
                parameter2="";
            }
            if (inputThird.isShown()) {
                third = editTextThird.getText().toString();
            }else {
                third="";
                parameter3="";
            }
            if (inputFour.isShown()) {
                four = editTextFour.getText().toString();
            }else {
                four="";
                parameter4="";
            }
            if (spinnerOperatorList.getSelectedItemPosition()==0){
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        "Please select service provider name");
            }else if (inputFirst.isShown()&&first.isEmpty()){
                editTextFirst.setError(name1);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        name1);
            }else if (inputFirst.isShown()&&first.length()<Integer.valueOf(MinLength1)&&first.length()>Integer.valueOf(MaxLength1)){
                editTextFirst.setError("Invalid "+name1);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        "Invalid "+name1);
            }else if (inputSecond.isShown()&&second.isEmpty()){
                if (!name2.isEmpty()) {
                    editTextSecond.setError(name2);
                    Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                            name2);
                }else {
                    editTextSecond.setError("Enter amount");
                    Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                            "Enter Amount" +
                                    "");
                }
            }else if (inputSecond.isShown()&&first.length()<Integer.valueOf(MinLength2)&&first.length()>Integer.valueOf(MaxLength2)){
                if (!name2.isEmpty()) {
                    editTextSecond.setError(name2);
                    Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                            name2);
                }else {
                    editTextSecond.setError("Enter amount");
                    Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                            "Enter Amount" +
                                    "");
                }
            }else if (inputThird.isShown()&&second.isEmpty()){
                editTextThird.setError(name3);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        name3);
            }else if (inputThird.isShown()&&third.length()<Integer.valueOf(MinLength3)&&third.length()>Integer.valueOf(MaxLength3)){
                editTextThird.setError("Invalid "+name3);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        "Invalid "+name3);
            }else if (inputFour.isShown()&&second.isEmpty()){
                editTextFour.setError(name4);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        name4);
            }else if (inputFour.isShown()&&four.length()<Integer.valueOf(MinLength4)&&four.length()>Integer.valueOf(MaxLength4)){
                editTextFour.setError("Invalid "+name4);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        "Invalid "+name4);
            }else if (Configuration.hasNetworkConnection(BbpsActivity.this)){
                getBill(userData.getUserId(),userData.getuType(),userData.getMobile(),billerId,first,second,third,four);
            }else {
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"internetError",
                        "No internet connectivity");
            }
        }
        if (v==btnBbps){
            first=editTextFirst.getText().toString();
            payment="direct";
           // String third ,four, second;
            if (inputSecond.isShown()) {
                second = editTextSecond.getText().toString();
            }else {
                second="";
                parameter2="";
            }
            if (inputThird.isShown()) {
                third = editTextThird.getText().toString();
            }else {
                third="";
                parameter3="";
            }
            if (inputFour.isShown()) {
                four = editTextFour.getText().toString();
            }else {
                four="";
                parameter4="";
            }
            if (spinnerOperatorList.getSelectedItemPosition()==0){
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        "Please select service provider name");
            }else if (inputFirst.isShown()&&first.isEmpty()){
                editTextFirst.setError(name1);
               // parameter1=name1.replace(" ","_").toLowerCase();
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        name1);
            }else if (inputFirst.isShown()&&first.length()<Integer.valueOf(MinLength1)&&first.length()>Integer.valueOf(MaxLength1)){
                editTextFirst.setError("Invalid "+name1);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        "Invalid "+name1);
            }else if (inputSecond.isShown()&&second.isEmpty()){
                if (!name2.isEmpty()) {
                    editTextSecond.setError(name2);
                    Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                            name2);
                }else {
                    editTextSecond.setError("Enter amount");
                    Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                            "Enter Amount" +
                                    "");
                }
            }else if (inputSecond.isShown()&&first.length()<Integer.valueOf(MinLength2)&&first.length()>Integer.valueOf(MaxLength2)){
                editTextSecond.setError("Invalid "+name2);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        "Invalid "+name2);
            }else if (inputThird.isShown()&&second.isEmpty()){
                editTextThird.setError(name3);
              //  parameter3=name3.replace(" ","_").toLowerCase();
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        name3);
            }else if (inputThird.isShown()&&third.length()<Integer.valueOf(MinLength3)&&third.length()>Integer.valueOf(MaxLength3)){
                editTextThird.setError("Invalid "+name3);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        "Invalid "+name3);
            }else if (inputFour.isShown()&&second.isEmpty()){
                editTextFour.setError(name4);
               // parameter4=name4.replace(" ","_").toLowerCase();
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        name4);
            }else if (inputFour.isShown()&&four.length()<Integer.valueOf(MinLength4)&&four.length()>Integer.valueOf(MaxLength4)){
                editTextFour.setError("Invalid "+name4);
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                        "Invalid "+name4);
            }else if (!Configuration.hasNetworkConnection(BbpsActivity.this)){
                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"internetError",
                        "No internet connectivity");
            }else if (Float.valueOf(second)>Float.valueOf(balance)){
                final float amt;
                if (Float.valueOf(balance)<Float.valueOf(second)){
                    amt=Float.valueOf(second)-Float.valueOf(balance);
                }else {
                    amt= Float.parseFloat(second);
                }
                final Dialog dialg=new Dialog(BbpsActivity.this);
                dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialg.setContentView(R.layout.layout_dialog_confirmation);
                dialg.setCanceledOnTouchOutside(false);
                dialg.setCancelable(false);

                TextView txtMessage=dialg.findViewById(R.id.txt_dialog);
                TextView btnCancel=dialg.findViewById(R.id.btn_cancel_dialog);
                TextView btnContinue=dialg.findViewById(R.id.btn_continue_dialog);
                txtMessage.setText(Html.fromHtml("Your available balance \u20B9"
                        +"<font color='#000000'><b>" + balance + "</b></font>"+
                        "is lower to complete payment.\n please continue to add balance and pay your bills"));
                btnCancel.setOnClickListener(v12 -> dialg.dismiss());
                btnContinue.setOnClickListener(v1 -> {
                    // dialo.dismiss();
                    getRandomNumberString();
                    goPayment(amt);
                });

                dialg.show();
                Window window = dialg.getWindow();
                assert window != null;
                window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(BbpsActivity.this);
                builder.setMessage("Are you sure you want to pay bill?")
                        .setTitle("Final Submit")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialo, id) -> {
                            if (Configuration.hasNetworkConnection(BbpsActivity.this)) {
                                dialo.dismiss();
                                payBill(userData.getUserId(),userData.getuType(),dialog,opId,serviceName,
                                        first,transId,userData.getMobile(),second,third,four);
                            }else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"internetError",
                                            "No internet connectivity");
                                }
                                dialo.dismiss();
                            }
                        })
                        .setNegativeButton(" No ", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (scrollView.isShown()){
            cardView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            txtTitle.setText(R.string.bbps);
            btnFetchBill.setVisibility(View.GONE);
            inputFirst.setVisibility(View.GONE);
            inputSecond.setVisibility(View.GONE);
            btnBbps.setVisibility(View.GONE);
            editTextFirst.setText("");
            editTextSecond.setText("");
            try{
                Configuration.hideKeyboardFrom(BbpsActivity.this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Intent intent = new Intent(BbpsActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position==0){
            service="6";
            serviceName="Mobile Postpaid";
            txtTitle.setText(R.string.pay_mobile);
            getOperatorList(service);

        }
        if (position==1){
            service="4";
            serviceName="DTH";
            txtTitle.setText(R.string.pay_dth);
            getOperatorList(service);
        }
        if (position==2){
            service="7";
            serviceName="Broadband Postpaid";
            txtTitle.setText(R.string.pay_broadband);
            getOperatorList(service);
        }
        if (position==3){
            service="8";
            serviceName="Broadband Prepaid";
            txtTitle.setText(R.string.pay_broadband);
            getOperatorList(service);
        }
        if (position==4){
            service="3";
            serviceName="Electricity";
            txtTitle.setText(R.string.electricity_bill);
            getOperatorList(service);
        }
        if (position==5){
            service="5";
            serviceName="Landline Postpaid";
            txtTitle.setText(R.string.pay_landline_bill);
            getOperatorList(service);
        }
        if (position==6){
            service="2";
            serviceName="Gas Utility";
            txtTitle.setText(R.string.pay_gas_bill);
            getOperatorList(service);
        }
        if (position==7){
            service="9";
            serviceName="Water";
            txtTitle.setText(R.string.pay_water_bill);
            getOperatorList(service);
        }
    }

    private void getOperatorList(final String service) {
        Configuration.showDialog("Please wait...", progressDialog);
        //countries.add("---Select Country---");
        System.out.println("id-->"+service);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.GET_OPERATOR_BBPS,
                response -> {

                    try {

                        System.out.println("Operator response--->" + response);
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");

                        if (status.equalsIgnoreCase("S")){
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            cardView.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            btnBbps.setText(R.string.pay_bill);

                            opratorId.clear();
                            opratorName.clear();
                            System.out.println("Operator response--->" + response);
                            opratorName.add(0, "Select Operator");
                            opratorId.add(0, "0");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                opratorId.add(jsonObject1.getString("id"));
                                opratorName.add(jsonObject1.getString("text"));
                                //  list.put(bankName.get(i),shortName.get(i));
                            }
                            ArrayAdapter<String> oparatorAdapter = new ArrayAdapter<>(BbpsActivity.this, R.layout.spinner_item,
                                    R.id.spinner_text,opratorName );
                            spinnerOperatorList.setAdapter(oparatorAdapter);

                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                                        jsonObject.getString("message"));
                            }
                            txtTitle.setText(R.string.bbps);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        progressDialog.dismiss();
                    }

                },
                error -> {

                    progressDialog.dismiss();
                    Toast.makeText(BbpsActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.SERVICE,service);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(BbpsActivity.this);
        requestQueue.add(stringRequest);
    }
    private void getParameter(final String opId) {
        String tag_string_req = "parameters_service";
        Configuration.showDialog("Please wait...", progressDialog);
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_PARAMETERS_BBPS, response -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d(Constraints.TAG, "parameter Service Response: " + response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");

                        if (status.equalsIgnoreCase("S")){
                            JSONObject jObj=jsonObject.getJSONObject("data");
                           //String id=jObj.getString("id");
                            String FieldType ;

                            if (jObj.has("parameter0")){
                                JSONObject jsonObject1=jObj.getJSONObject("parameter0");
                                name1 = jsonObject1.getString("name");
                                billerId = jsonObject1.getString("billerId");
                                MinLength1 = jsonObject1.getString("MinLength");
                                MaxLength1 = jsonObject1.getString("MaxLength");
                                FieldType = jsonObject1.getString("FieldType");
                                inputFirst.setVisibility(View.VISIBLE);
                                editTextFirst.setVisibility(View.VISIBLE);
                                inputFirst.setHint(name1);
                                parameter1=name1.replace(" ","_").toLowerCase();
                                assert FieldType != null;
                                if (FieldType.equalsIgnoreCase("NUMERIC")){
                                    editTextFirst.setInputType(InputType.TYPE_CLASS_NUMBER);
                                }else {
                                    editTextFirst.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                                }
                                editTextFirst.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.valueOf(MaxLength1))});
                            }
                            if (jObj.has("parameter1")){
                                JSONObject jsonObject1=jObj.getJSONObject("parameter1");
                                name2 = jsonObject1.getString("name");
                                billerId = jsonObject1.getString("billerId");
                                MinLength2 = jsonObject1.getString("MinLength");
                                MaxLength2 = jsonObject1.getString("MaxLength");
                                FieldType = jsonObject1.getString("FieldType");
                                inputSecond.setVisibility(View.VISIBLE);
                                editTextSecond.setVisibility(View.VISIBLE);
                                inputSecond.setHint(name2);
                                parameter2=name2.replace(" ","_").toLowerCase();
                                assert FieldType != null;
                                if (FieldType.equalsIgnoreCase("NUMERIC")){
                                    editTextSecond.setInputType(InputType.TYPE_CLASS_NUMBER);
                                }else {
                                    editTextSecond.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                                }
                                editTextSecond.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.valueOf(MaxLength2))});
                            }
                            if (jObj.has("parameter2")){
                                JSONObject jsonObject1=jObj.getJSONObject("parameter2");
                                name3 = jsonObject1.getString("name");
                                billerId = jsonObject1.getString("billerId");
                                MinLength3 = jsonObject1.getString("MinLength");
                                MaxLength3 = jsonObject1.getString("MaxLength");
                                FieldType = jsonObject1.getString("FieldType");
                                inputThird.setVisibility(View.VISIBLE);
                                editTextThird.setVisibility(View.VISIBLE);
                                inputThird.setHint(name3);
                                parameter3=name3.replace(" ","_").toLowerCase();
                                assert FieldType != null;
                                if (FieldType.equalsIgnoreCase("NUMERIC")){
                                    editTextThird.setInputType(InputType.TYPE_CLASS_NUMBER);
                                }else {
                                    editTextThird.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                                }
                                editTextThird.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.valueOf(MaxLength3))});
                            }

                            if (jObj.has("parameter3")){
                                JSONObject jsonObject1=jObj.getJSONObject("parameter3");
                                name4 = jsonObject1.getString("name");
                                billerId = jsonObject1.getString("billerId");
                                MinLength4 = jsonObject1.getString("MinLength");
                                MaxLength4 = jsonObject1.getString("MaxLength");
                                FieldType = jsonObject1.getString("FieldType");
                                inputFour.setVisibility(View.VISIBLE);
                                editTextFour.setVisibility(View.VISIBLE);
                                inputFour.setHint(name4);
                                parameter4=name4.replace(" ","_").toLowerCase();
                                assert FieldType != null;
                                if (FieldType.equalsIgnoreCase("NUMERIC")){
                                    editTextFour.setInputType(InputType.TYPE_CLASS_NUMBER);
                                }else {
                                    editTextFour.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                                }
                                editTextFour.setFilters(new InputFilter[] { new InputFilter.LengthFilter(Integer.valueOf(MaxLength4))});
                            }


                          //  String IsMandatory=jObj.getString("IsMandatory");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(Constraints.TAG, "parameter Service Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.OPERATOR,opId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void getBillerMode(final String opId) {
        String tag_string_req = "billerMode";
        Configuration.showDialog("Please wait...", progressDialog);
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_BILLER_MODE_BBPS, response -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d(Constraints.TAG, "billerMode Service Response: " + response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if (status.equalsIgnoreCase("S")){
                            JSONObject jObj=jsonObject.getJSONObject("data");
                             billerMode=jObj.getString("BillerMode");
                            if (billerMode.equalsIgnoreCase("2")){
                              //  inputSecond.setVisibility(View.VISIBLE);
                                btnFetchBill.setVisibility(View.GONE);
                                inputSecond.setHint("Enter Amount (\u20B9)");
                                btnBbps.setVisibility(View.VISIBLE);
                                inputSecond.setVisibility(View.VISIBLE);
                                editTextSecond.setInputType(InputType.TYPE_CLASS_NUMBER);
                            }else {
                                btnFetchBill.setVisibility(View.VISIBLE);
                                btnBbps.setVisibility(View.GONE);
                              //  inputSecond.setVisibility(View.GONE);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(Constraints.TAG, "billerMode Service Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.OPERATOR,opId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getBill(final String userId, final String userType, final String mobile, final String billerId,
                         final String first, final String second, final String third, final String four) {
        String tag_string_req = "fetch_bill";
        Log.d(Constraints.TAG, "fetch_bill Service Response: " +" "+parameter1+" "+first+" "
                +parameter2+" "+second+" "+parameter3+" "+third+" "+parameter4+" "+four+" "+userType);
        if (TextUtils.isEmpty(parameter1)){
            parameter1="";
        }
        if (TextUtils.isEmpty(parameter2)){
            parameter2="";
        }
        if (TextUtils.isEmpty(parameter3)){
            parameter3="";
        }
        if (TextUtils.isEmpty(parameter4)){
            parameter4="";
        }
        Configuration.showDialog("Please wait...", progressDialog);
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FETCH_BILL_BBPS, response -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d(Constraints.TAG, "fetch_bill Service Response: " + response+" "+parameter1 +"  "+first+" "
                            +parameter2+" "+second+" "+parameter3+" "+third+" "+parameter4+" "+four);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if (status.equalsIgnoreCase("S")){
                            JSONObject jObj=jsonObject.getJSONObject("data");
                            payment="bill";

                            String billdate=jObj.getString("BillDate");
                            String billNumber=jObj.getString("BillNumber");
                            String billPeriod=jObj.getString("BillPeriod");
                            String CustomerName=jObj.getString("CustomerName");
                            final String amount=jObj.getString("BillAmount");
                            String duedate=jObj.getString("BillDueDate");
                          //  final String reference_id=jsonObject.getString("reference_id");
                            transId=jObj.getString("TransactionId");
                            openDialog(billdate,billNumber,billPeriod,CustomerName,amount,duedate,transId,first,second,third,four);

                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                                        jsonObject.getString("message"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(Constraints.TAG, "fetch_bill Service Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_MOBILE,mobile);
                params.put(Constant.USER_ID,userId);
                params.put(Constant.BILLER_ID,billerId);
                params.put(Constant.FIRST,first);
                params.put(Constant.SECOND,second);
                params.put(Constant.THIRD,third);
                params.put(Constant.FOUR,four);
                params.put(Constant.IP,ipAddress);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void payBill(final String userId, final String userType, final Dialog dialog, final String opId,
                         final String serviceName, final String first, final String transId, final String mobile,
                         final String amount, final String third, final String four) {
        String tag_string_req = "pay_bill";
        Configuration.showDialog("Please wait...", progressDialog);
        HttpsTrustManager.allowAllSSL();
        Log.d(Constraints.TAG, "pay_bill Service Response: " +" "+parameter1+" "+first+" "
                +parameter2+" "+amount+" "+parameter3+" "+third+" "+parameter4+" "+four);
        if (TextUtils.isEmpty(parameter1)){
            parameter1="";
        }
        if (TextUtils.isEmpty(parameter2)){
            parameter2="";
        }
        if (TextUtils.isEmpty(parameter3)){
            parameter3="";
        }
        if (TextUtils.isEmpty(parameter4)){
            parameter4="";
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAY_BILL_AMOUNT_BBPS, response -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d(Constraints.TAG, "pay_bill Service Response: " + response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        String message=jsonObject.getString("message");
                        if (status.equalsIgnoreCase("S")){
                            WebService.getBalance(userId);
                           // dialog.dismiss();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"",
                                        "Status: Success\n"+message);
                            }
                            editTextFirst.setText("");
                            editTextSecond.setText("");
                            inputFirst.setVisibility(View.GONE);
                            inputSecond.setVisibility(View.GONE);
                            btnFetchBill.setVisibility(View.GONE);
                            btnBbps.setVisibility(View.GONE);
                            spinnerOperatorList.setSelection(0);
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                                        message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(Constraints.TAG, "pay_bill Service Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.USER_TYPE,userType);
                params.put(Constant.OPERATOR,opId);
                params.put(Constant.SERVICE,serviceName);
                params.put(Constant.MOBILE,first);
                params.put(Constant.TRANS_ID,transId);
                params.put(Constant.USER_MOBILE,mobile);
                params.put(Constant.AMOUNT,amount);
                params.put(Constant.FIRST,first);
                params.put(Constant.SECOND,second);
                params.put(Constant.THIRD,third);
                params.put(Constant.FOUR,four);
                params.put(Constant.IP,ipAddress);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    public void getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        ipAddress=ip;
                        Log.i(TAG, "***** IP="+ ip);
                        return;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void payBillAmount(final String userId, final String userType, Dialog dialog, final String opId, final String serviceName,
                               final String first, final String mobile, final String amount, final String ipAddress,
                               final String third, final String four) {
        String tag_string_req = "pay_bill";
        Log.d(Constraints.TAG, "pay_bill Service Response: " +" "+parameter1+" "+first+" "
                +parameter2+" "+amount+" "+parameter3+" "+third+" "+parameter4+" "+four);
        if (TextUtils.isEmpty(parameter1)){
            parameter1="";
        }
        if (TextUtils.isEmpty(parameter2)){
            parameter2="";
        }
        if (TextUtils.isEmpty(parameter3)){
            parameter3="";
        }
        if (TextUtils.isEmpty(parameter4)){
            parameter4="";
        }
        Configuration.showDialog("Please wait...", progressDialog);
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAY_BILL_BBPS, response -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d(Constraints.TAG, "pay_bill Service Response: " + response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        String message=jsonObject.getString("message");
                        if (status.equalsIgnoreCase("S")){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"",
                                        message);
                            }
                            editTextFirst.setText("");
                            editTextSecond.setText("");
                            inputFirst.setVisibility(View.GONE);
                            inputSecond.setVisibility(View.GONE);
                            btnFetchBill.setVisibility(View.GONE);
                            btnBbps.setVisibility(View.GONE);
                            spinnerOperatorList.setSelection(0);

                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"error",
                                        message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(Constraints.TAG, "pay_bill Service Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.USER_TYPE,userType);
                params.put(Constant.OPERATOR,opId);
                params.put(Constant.SERVICE,serviceName);
                params.put(Constant.MOBILE,first);
                params.put(Constant.TRANS_ID,transId);
                params.put(Constant.USER_MOBILE,mobile);
                params.put(Constant.AMOUNT,amount);
                params.put(Constant.FIRST,first);
                params.put(Constant.SECOND,second);
                params.put(Constant.THIRD,third);
                params.put(Constant.FOUR,four);
                params.put(Constant.IP,ipAddress);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private void openDialog(String billdate, String billNumber, String billPeriod, String CustomerName,
                            final String amount, String duedate, final String transId, final String first,
                            String second, final String third, final String four) {
        final UserData userData=PrefManager.getInstance(BbpsActivity.this).getUserData();

        dialog=new Dialog(BbpsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_bill);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        txtService=dialog.findViewById(R.id.txt_service);
        txtCustNo=dialog.findViewById(R.id.txt_customer_no);
        txtCustName=dialog.findViewById(R.id.txt_cust_name);
        txtBillNo=dialog.findViewById(R.id.txt_billnumber);
        txtBillPeriod=dialog.findViewById(R.id.txt_billperiod);
        txtBillDate=dialog.findViewById(R.id.txt_billdate);
        txtDueDate=dialog.findViewById(R.id.txt_duedate);
        txtDueAmount=dialog.findViewById(R.id.txt_dueamount);
        txtCancel=dialog.findViewById(R.id.txt_canc_bill);
        btnPaybill=dialog.findViewById(R.id.btn_pay_bill);
        txtService.setText("Service Provider : "+providerName);
        txtCustNo.setText(name1+" : "+first);
        txtCustName.setText("Name : "+CustomerName);
        txtBillNo.setText("Bill Number : "+billNumber);
        txtBillPeriod.setText("Bill Period : "+billPeriod);
        txtBillDate.setText("Bill Date : "+billdate);
        txtDueDate.setText("Due Date : "+duedate);
        txtDueAmount.setText("Due Amount : "+amount);



        txtCancel.setOnClickListener(v -> {
            dialog.dismiss();
            progressDialog.dismiss();
        });
        btnPaybill.setOnClickListener(v -> {
            if (Float.valueOf(amount)>Float.valueOf(balance)){
                final float amt;
                if (Float.valueOf(balance)<Float.valueOf(amount)){
                    amt=Float.valueOf(amount)-Float.valueOf(balance);
                }else {
                    amt= Float.parseFloat(amount);
                }
                final Dialog dialg=new Dialog(BbpsActivity.this);
                dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialg.setContentView(R.layout.layout_dialog_confirmation);
                dialg.setCanceledOnTouchOutside(false);
                dialg.setCancelable(false);

                TextView txtMessage=dialg.findViewById(R.id.txt_dialog);
                TextView btnCancel=dialg.findViewById(R.id.btn_cancel_dialog);
                TextView btnContinue=dialg.findViewById(R.id.btn_continue_dialog);
                txtMessage.setText(Html.fromHtml("Your available balance \u20B9"
                        +"<font color='#000000'><b>" + balance + "</b></font>"+
                        "is lower to complete payment.\n please continue to add balance and pay your bills"));
                btnCancel.setOnClickListener(v12 -> dialg.dismiss());
                btnContinue.setOnClickListener(v1 -> {
                   // dialo.dismiss();
                    getRandomNumberString();
                    goPayment(amt);
                });

                dialg.show();
                Window window = dialg.getWindow();
                assert window != null;
                window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(BbpsActivity.this);
                builder.setMessage("Are you sure you want to pay bill?")
                        .setTitle("Final Submit")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialo, id) -> {
                            if (Configuration.hasNetworkConnection(BbpsActivity.this)) {
                                dialo.dismiss();
                                payBillAmount(userData.getUserId(),userData.getuType(),dialog,opId,serviceName,
                                        first,transId,userData.getMobile(),second,third,four);
                            }else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    Configuration.openPopupUpDown(BbpsActivity.this,R.style.Dialod_UpDown,"internetError",
                                            "No internet connectivity");
                                }
                                dialo.dismiss();
                            }
                        })
                        .setNegativeButton(" No ", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }

    private void goPayment(float amt) {
        UserData userData=PrefManager.getInstance(BbpsActivity.this).getUserData();
        Intent intentProceed = new Intent(BbpsActivity.this, PWECouponsActivity.class);
        intentProceed.putExtra("trxn_id",merchant_trxnId);
        intentProceed.putExtra("trxn_amount",amt);
        intentProceed.putExtra("trxn_prod_info","billpaybbps");
        intentProceed.putExtra("trxn_firstname",userData.getName());
        intentProceed.putExtra("trxn_email_id",userData.getEmail());
        intentProceed.putExtra("trxn_phone",userData.getMobile());
        intentProceed.putExtra("trxn_key",Constant.MERCHANT_ID);
        intentProceed.putExtra("trxn_udf1","");
        intentProceed.putExtra("trxn_udf2","");
        intentProceed.putExtra("trxn_udf3","");
        intentProceed.putExtra("trxn_udf4","");
        intentProceed.putExtra("trxn_udf5","");
        intentProceed.putExtra("trxn_address1",userData.getAddressLine1());
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
    }
    @SuppressLint("DefaultLocale")
    public void getRandomNumberString() {
        UserData userData=PrefManager.getInstance(BbpsActivity.this).getUserData();
        Random rnd = new Random();
        int number = rnd.nextInt(999999)+1000000;
        merchant_trxnId=userData.getUserId()+"TX"+String.format("%06d",number);
        Log.i("NUMBER",""+String.format("%06d", number));
    }
    private void getAddress(double latitude, double longitude) {
       // String adrs = "";
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
           // adrs = address    ;
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
            //adrs = null;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UserData userData=PrefManager.getInstance(BbpsActivity.this).getUserData();
        assert data != null;
        String result = data.getStringExtra("result");
        String response = data.getStringExtra("payment_response");
        Log.e("TOTELRES","RESPONSE--->"+response+"  \n"+result);
        sendResponse(response,userData.getUserId(),userData.getuType(),opId,serviceName,first,
                transId,userData.getMobile(),second,third,four,dialog);
        WebService.getBalance(userData.getUserId());
        getRandomNumberString();

        try {
            JSONObject jsonObject=new JSONObject(response);
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            if (jsonObject.has("status")) {
              //  String status = jsonObject.getString("status");
               /* if (status.equalsIgnoreCase("success")) {

                } else {*/
                    if (jsonObject.has("error_msg")) {
                        Toast.makeText(this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
               // }
            }else {
                Toast.makeText(this, "You have cancelled your transaction", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendResponse(String response, final String userId, final String userType, String opId, String serviceName, String first,
                              String transId, final String mobile, final String second, String third, String four, Dialog dialog) {
        String tag_string_req = "transaction_recharge";
        HttpsTrustManager.allowAllSSL();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAYMENT_TRANSACTION, response1 -> {
            Log.d(TAG, "transaction recharge Response: " + response1);

            try {
                JSONObject jsonObject=new JSONObject(response1);
                String status=jsonObject.getString("status");
                progressDialog.dismiss();
                if (status.equals("0")){
                    if (payment.equalsIgnoreCase("bill")) {
                        payBillAmount(userId, userType, dialog, opId, serviceName,
                                first, transId, mobile, second, third, four);
                    }else {
                        payBill(userId, userType, dialog, opId, serviceName,
                                first, transId, mobile, second, third, four);
                    }
                }else {
                    WebService.getBalance(userId);
                    Toast.makeText(BbpsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            WebService.getBalance(userId);
            Log.e(TAG, "transaction recharge Error: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.RESPONSE,response);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
