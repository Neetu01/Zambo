package com.efficientindia.zambopay.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.efficientindia.zambopay.Adapter.MenuItemHomeAdapter;
import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.Constant;
import com.efficientindia.zambopay.AppConfig.PrefManager;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.R;

public class AgentRechargeThree extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView imgBack;
    GridView gridViewRechargeThree;
    public String[] nameItem={"Mobile Prepaid","Mobile Postpaid ","DTH","Broadband",
            "Electricity","Landline","Gas","Water","Credit Card","Insurance"};

    public Integer[] mThumbIds = {
            R.drawable.mobile,R.drawable.postpaid,R.drawable.dth,
            R.drawable.broadband,R.drawable.electricity,R.drawable.landline,
            R.drawable.gas,R.drawable.water,R.drawable.credit_card,
            R.drawable.insurance,};
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_recharge_three);
        progressDialog=new ProgressDialog(AgentRechargeThree.this);
        imgBack=findViewById(R.id.imgback_agent);
        imgBack.setOnClickListener(this);
        gridViewRechargeThree=findViewById(R.id.gridview_recharge_three_agent);

        MenuItemHomeAdapter adapter = new MenuItemHomeAdapter(AgentRechargeThree.this,nameItem,mThumbIds);
        gridViewRechargeThree.setAdapter(adapter);
        gridViewRechargeThree.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v==imgBack){
            Intent intent = new Intent(AgentRechargeThree.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AgentRechargeThree.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserData userData= PrefManager.getInstance(AgentRechargeThree.this).getUserData();
        SplashActivity.savePreferences(Constant.AGENT,"agent");
        if (position==0){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_PREPAID,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","Prepaid mobile service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"mobilePrepaid");
                WebService.getOperatorList("Prepaid",AgentRechargeThree.this,progressDialog);
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==1){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_POSTPAID,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","Postpaid mobile bill service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("postpaid",AgentRechargeThree.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"mobilePostpaid");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==2){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_DTH,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","DTH recharge service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("DTH",AgentRechargeThree.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"dth");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==3){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_BROADBAND,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","Broadband recharge service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Broadband",AgentRechargeThree.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"broadband");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==4){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_ELECTRICITY,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","Electricity bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Electricity",AgentRechargeThree.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"electricity");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==5){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_LANDLINE,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","Landline bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Landline",AgentRechargeThree.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"landline");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==6){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_GAS,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","Gas bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Gas",AgentRechargeThree.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"gas");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==7){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_WATER,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","Water bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("WaterBill",AgentRechargeThree.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"water");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==8){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_CREDITCARD,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","Credit card bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("CreditCard",AgentRechargeThree.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"creditCard");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else {
            if (SplashActivity.getPreferences(Constant.CUSTOMER_INSURANCE,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeThree.this,R.style.Dialod_UpDown,"error","insurance premium payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Insurance",AgentRechargeThree.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"insurance");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }
    }
}
