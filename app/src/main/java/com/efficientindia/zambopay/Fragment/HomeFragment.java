package com.efficientindia.zambopay.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.efficientindia.zambopay.Activity.AddMoney;
import com.efficientindia.zambopay.Activity.AgentRechargeThree;
import com.efficientindia.zambopay.Activity.AgentRechargeTwo;
import com.efficientindia.zambopay.Activity.AspsTransfer;
import com.efficientindia.zambopay.Activity.BbpsActivity;
import com.efficientindia.zambopay.Activity.Dmt_Transfer;
import com.efficientindia.zambopay.Activity.Enddrawertoggle;
import com.efficientindia.zambopay.Activity.HistoryActivity;
import com.efficientindia.zambopay.Activity.HomeActivity;
import com.efficientindia.zambopay.Activity.SplashActivity;
import com.efficientindia.zambopay.Activity.Transfer;
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
import com.google.android.material.internal.NavigationMenu;
import com.tapits.fingpay.utils.Constants;
import org.egram.aepslib.DashboardActivity;
import org.egram.microatm.BluetoothMacSearchActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import aeps.roundpay.solutions.com.mylibrary.CALL_AEPS;
import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Created by aftab on 6/6/2018.
 */
//@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    Enddrawertoggle enddrawertoggle;
    private static final int INTENT_CODE = 1;
    private CardView cardViewAeps,cardViewMoney,cardViewMobile,cardViewDth,cardViewDatacard,
            cardViewElectricity,cardViewGas,cardViewLandline,cardViewInsuarance,cardViewAeps2;
    ProgressDialog progressDialog;
    static String phone = "",roundMobile = "",roundPassword = "";
    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    String transType;
    LinearLayout lnAeps,lnMoney2,lnRecharge3,lnBbps;
    public static ProgressBar progressBar;
    LinearLayout wallet;
    GridView recharges,billpayment;
    private CardView cardViewCustomer,cardViewBbps,cardViewRechargeThree,cardViewMoneytransferTwo,cardViewRechargeTwo,cardviewmicroatm;
    private Button transfer,addmoney;
    public String[] nameItem={"Mobile Prepaid","Mobile Postpaid ","DTH","Broadband",
            "Electricity","Landline","Gas","Water","Credit Card","Insurance"};

    public Integer[] mThumbIds = {
            R.drawable.mobile,R.drawable.postpaid,R.drawable.dth,
            R.drawable.broadband,R.drawable.electricity,R.drawable.landline,
            R.drawable.gas,R.drawable.water,R.drawable.credit_card,
            R.drawable.insurancecolor,};

    public String[] rechargeItem={"Mobile Prepaid","DTH"};

    public Integer[] rechargeIcons = {
            R.drawable.postpaidcolor,R.drawable.dthcolor,
            };
    public String[] billItem={"Gas","Insurance","Water Bill","Landline","Credit Card","Broadband","Electricity","Postpaid"};
    public Integer[] billIcons = {R.drawable.gascolor,R.drawable.insurancecolor,R.drawable.watercolor,R.drawable.landlinecolor,R.drawable.creditcardcolor,R.drawable.broadbandpostpaidcolor,R.drawable.electricitycolor,R.drawable.postpaidcolor};
    public TextView transaction;
    public static TextView balance;
    UserData userData;

    @Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ReadPhoneStatePermission();
        userData = PrefManager.getInstance(getActivity()).getUserData();
        billpayment=view.findViewById(R.id.billpayment);
        lnAeps=view.findViewById(R.id.linear_aeps);
        lnBbps=view.findViewById(R.id.linear_bbps);
        progressBar=view.findViewById(R.id.progressbar);
        transaction=view.findViewById(R.id.transactions);
        cardviewmicroatm=view.findViewById(R.id.cardview_microatm);
        cardViewRechargeTwo=view.findViewById(R.id.cardview_recharge_two);
        cardViewMoneytransferTwo=view.findViewById(R.id.cardview_moneytransfer_two);
        cardViewRechargeThree=view.findViewById(R.id.cardview_recharge_three);
        cardViewBbps=view.findViewById(R.id.cardview_bbps);
        cardViewCustomer=view.findViewById(R.id.cardview_customer);
        transfer=view.findViewById(R.id.paymoney);
        addmoney=view.findViewById(R.id.addmoney);
        recharges=view.findViewById(R.id.recharge);
        billpayment=view.findViewById(R.id.billpayment);
        wallet=view.findViewById(R.id.wallet);

        //lnMoney2 =view.findViewById(R.id.linear_money_transfer);
        lnRecharge3 =view.findViewById(R.id.linear_recharge);
    //    gridViewRechargeThree=view.findViewById(R.id.gridview_recharge_three);
        progressDialog = new ProgressDialog(getActivity());
        progressBar.setVisibility(View.GONE);
        cardViewAeps = view.findViewById(R.id.cardview_aeps);
        cardViewAeps2=view.findViewById(R.id.cardview_aeps_two);
        cardViewMoney = view.findViewById(R.id.cardview_moneytransfer);
        balance=view.findViewById(R.id.balance);

      /*  cardViewMobile = view.findViewById(R.id.cardview_mobile);
        cardViewDth = view.findViewById(R.id.cardview_dth);
        cardViewDatacard = view.findViewById(R.id.cardview_datacard);
        cardViewElectricity = view.findViewById(R.id.cardview_electricity);
        cardViewGas = view.findViewById(R.id.cardview_gas);
        cardViewLandline = view.findViewById(R.id.cardview_landline);
        cardViewInsuarance = view.findViewById(R.id.cardview_insurance);*/
      lnRecharge3.setOnClickListener(this);
  //       lnMoney2.setOnClickListener(this);
        cardViewRechargeThree.setOnClickListener(this);
        cardViewMoneytransferTwo.setOnClickListener(this);
        cardViewBbps.setOnClickListener(this);
        addmoney.setOnClickListener(this);
        transfer.setOnClickListener(this);
        transaction.setOnClickListener(this);
        cardViewAeps.setOnClickListener(this);
        cardViewMoney.setOnClickListener(this);
        wallet.setOnClickListener(this);
        cardviewmicroatm.setOnClickListener(this);
        cardViewRechargeTwo.setOnClickListener(this);


        billpayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
 /*   cardViewMobile.setOnClickListener(this);
        cardViewDth.setOnClickListener(this);
        cardViewDatacard.setOnClickListener(this);
        cardViewElectricity.setOnClickListener(this);
        cardViewGas.setOnClickListener(this);
        cardViewLandline.setOnClickListener(this);
        cardViewInsuarance.setOnClickListener(this);*/

        cardViewAeps2.setOnClickListener(this);
        cardViewRechargeTwo.setOnClickListener(this);
        //final UserData userData = PrefManager.getInstance(getActivity()).getUserData();

        String userType = userData.getuType();
//        lnMoney2.setVisibility(View.GONE);
        lnBbps.setVisibility(View.GONE);
        if (userType.equalsIgnoreCase("CS")) {
            //cardViewMoneyTransfer.setVisibility(View.GONE);
           /* cardViewAeps.setVisibility(View.GONE);
            cardViewMoney.setVisibility(View.GONE);*/
           cardViewAeps.setVisibility(View.GONE);
           cardviewmicroatm.setVisibility(View.GONE);
            cardViewAeps2.setVisibility(View.GONE);
            lnAeps.setVisibility(View.GONE);
          //  lnMoney2.setVisibility(View.GONE);
            lnRecharge3.setVisibility(View.GONE);
            cardViewBbps.setVisibility(View.GONE);
            lnBbps.setVisibility(View.GONE);
            cardViewMoneytransferTwo.setVisibility(View.GONE);
//            gridViewRechargeThree.setVisibility(View.VISIBLE);
            cardViewCustomer.setVisibility(View.VISIBLE);
        } else {
            cardViewMoneytransferTwo.setVisibility(View.GONE);
            if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))){
                assignService(userData.getUserId());
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (!userData.getuType().equalsIgnoreCase("CS")){
                        assignService1(userData.getUserId());
                    }
                }, 6000);
            }else {
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"internetError",
                        "No Internet Connectivity");
            }
//            lnMoney2.setVisibility(View.VISIBLE);
            lnBbps.setVisibility(View.VISIBLE);
            lnRecharge3.setVisibility(View.VISIBLE);
       //     gridViewRechargeThree.setVisibility(View.GONE);
            cardViewCustomer.setVisibility(View.GONE);
            cardViewAeps.setVisibility(View.GONE);
            cardViewAeps2.setVisibility(View.GONE);
            cardViewBbps.setVisibility(View.GONE);
         //   cardViewMoney.setVisibility(View.GONE);
            cardViewRechargeThree.setVisibility(View.GONE);
            cardViewRechargeTwo.setVisibility(View.VISIBLE);

        }

      /*  MenuItemHomeAdapter adapter = new MenuItemHomeAdapter(getActivity(),nameItem,mThumbIds);
s        gridViewRechargeThree.setAdapter(adapter);
        gridViewRechargeThree.setOnItemClickListener(this);*/
        /*logout*/
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP
                    && keyCode == KeyEvent.KEYCODE_BACK) {

                new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            Intent launchNextActivity;
                            launchNextActivity = new Intent(Intent.ACTION_MAIN);
                            launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                            launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(launchNextActivity);
                            getActivity().finish();
                        }).create().show();

                return true;
            }
            return true;
        });

        if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))) {
            cardViewAeps.setVisibility(View.GONE);
            cardViewAeps2.setVisibility(View.GONE);
            cardViewBbps.setVisibility(View.GONE);
            cardViewMoney.setVisibility(View.GONE);
            cardViewRechargeThree.setVisibility(View.GONE);
            cardViewRechargeTwo.setVisibility(View.VISIBLE);
            //  assignService(userData.getUserId());
            WebService.getBalance(userData.getUserId());
            getActiveService(userData.getUserId(),userData.getuType());
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                WebService.getActiveService(userData.getUserId(), userData.getuType());
                getActiveService(userData.getUserId(),userData.getuType());
            }, 6000);
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
      /*  try{
            String prepaid=SplashActivity.getPreferences(Constant.PREPAID,"");
            String postpaid=SplashActivity.getPreferences(Constant.POSTPAID,"");
            String aeps=SplashActivity.getPreferences(Constant.AEPS,"");
            String money=SplashActivity.getPreferences(Constant.MONEY,"");
            String electricity=SplashActivity.getPreferences(Constant.ELECTRICITY,"");
            String gas=SplashActivity.getPreferences(Constant.GAS,"");
            String landline=SplashActivity.getPreferences(Constant.LANDLINE,"");
            String insurance=SplashActivity.getPreferences(Constant.INSURANCE,"");
            String dth=SplashActivity.getPreferences(Constant.DTH,"");
            String datacard=SplashActivity.getPreferences(Constant.DATACARD,"");
            if (userType.equalsIgnoreCase("CS")) {d
                //cardViewMoneyTransfer.setVisibility(View.GONE);
                cardViewAeps.setVisibility(View.GONE);
                cardViewMoney.setVisibility(View.GONE);
            } else {
                //   cardViewMoneyTransfer.setVisibility(View.VISIBLE);
                cardViewAeps.setVisibility(View.VISIBLE);
                cardViewMoney.setVisibility(View.VISIBLE);
            }
            if (!prepaid.equalsIgnoreCase("A")){
                SplashActivity.savePreferences(Constant.MOBILE_PREPAID,"hide");
            }else {
                SplashActivity.savePreferences(Constant.MOBILE_PREPAID,"unhide");
            }
            if (!postpaid.equalsIgnoreCase("A")){
                SplashActivity.savePreferences(Constant.MOBILE_POSTPAID,"hide");
            }else {
                SplashActivity.savePreferences(Constant.MOBILE_POSTPAID,"unhide");
            }
            if (aeps.equalsIgnoreCase("A")){
                cardViewAeps.setVisibility(View.VISIBLE);
            }else {
                cardViewAeps.setVisibility(View.GONE);
            }
            if (money.equalsIgnoreCase("A")){
                cardViewMoney.setVisibility(View.VISIBLE);
            }else {
                cardViewMoney.setVisibility(View.GONE);
            }
            if (electricity.equalsIgnoreCase("A")){
                cardViewElectricity.setVisibility(View.VISIBLE);
            }else {
                cardViewElectricity.setVisibility(View.GONE);
            }
            if (gas.equalsIgnoreCase("A")){
                cardViewGas.setVisibility(View.VISIBLE);
            }else {
                cardViewGas.setVisibility(View.GONE);
            }
            if (landline.equalsIgnoreCase("A")){
                cardViewLandline.setVisibility(View.VISIBLE);
            }else {
                cardViewLandline.setVisibility(View.GONE);
            }
            if (insurance.equalsIgnoreCase("A")){
                cardViewInsuarance.setVisibility(View.VISIBLE);
            }else {
                cardViewInsuarance.setVisibility(View.GONE);
            }
            if (dth.equalsIgnoreCase("A")){
                cardViewDth.setVisibility(View.VISIBLE);
            }else {
                cardViewDth.setVisibility(View.GONE);
            }
            if (datacard.equalsIgnoreCase("A")){
                cardViewDatacard.setVisibility(View.VISIBLE);
            }else {
                cardViewDatacard.setVisibility(View.GONE);
            }
            if (SplashActivity.getPreferences(Constant.MOBILE_PREPAID,"").equals("hide")&&
                    SplashActivity.getPreferences(Constant.MOBILE_POSTPAID,"").equals("hide")){
                cardViewMobile.setVisibility(View.GONE);
            }else {
                cardViewMobile.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
        MenuItemHomeAdapter adapter = new MenuItemHomeAdapter(getActivity(),rechargeItem,rechargeIcons);
        recharges.setAdapter(adapter);

        recharges.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_PREPAID,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Prepaid mobile service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"mobilePrepaid");
                        WebService.getOperatorList("Prepaid", (FragmentActivity) getContext(),progressDialog);
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }
                }else if (position==1){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_DTH,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Postpaid mobile bill service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        WebService.getOperatorList("postpaid", getActivity(),progressDialog);
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"dth");
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }
                }

            }
        });

       MenuItemHomeAdapter adapter1 = new MenuItemHomeAdapter(getActivity(),billItem,billIcons);
        billpayment.setAdapter(adapter1);
        billpayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_GAS,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Prepaid mobile service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"gas");
                        WebService.getOperatorList("Prepaid", (FragmentActivity) getContext(),progressDialog);
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }
                }else if (position==1){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_INSURANCE,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Postpaid mobile bill service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        WebService.getOperatorList("postpaid", getActivity(),progressDialog);
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"insurance");
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }
                }else if (position==2){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_WATER,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","DTH recharge service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        WebService.getOperatorList("DTH", getActivity(),progressDialog);
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"water");
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }
                }else if (position==3){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_LANDLINE,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Broadband recharge service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        WebService.getOperatorList("Broadband",getActivity(),progressDialog);
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"landline");
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }
                }else if (position==4){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_CREDITCARD,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Electricity bill payment service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        WebService.getOperatorList("Electricity",getActivity(),progressDialog);
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"creditcard");
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }
                }else if (position==5){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_BROADBAND,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Landline bill payment service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        WebService.getOperatorList("Landline",getActivity(),progressDialog);
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"broadband");
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }
                }else if (position==6){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_ELECTRICITY,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Gas bill payment service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        WebService.getOperatorList("Gas", getActivity(),progressDialog);
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"electricity");
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }
                }else if (position==7){
                    if (SplashActivity.getPreferences(Constant.CUSTOMER_POSTPAID,"").equalsIgnoreCase("hide")){
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Water bill payment service not" +
                                " available due to some system problem\n" +
                                "please wait untill we start it asap"+
                                ", Thanks");
                    }else {
                        WebService.getOperatorList("WaterBill",getActivity(),progressDialog);
                        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"mobilePostpaid");
                        WebService.getActiveService(userData.getUserId(), userData.getuType());
                    }

                }
            }
        });
        return view;
    }

    public void ReadPhoneStatePermission() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.READ_PHONE_STATE)) {

            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSIONS);
        }
        // END_INCLUDE(contacts_permission_request)
        /*recharge gridview*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Make sure it's our original READ_CONTACTS request
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor"})
    @Override
    public void onClick(View v) {

        if(v==wallet)
        {
            Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                    "This service is not available, we working on it\nplease try after sometime");
        }
        if (v == cardViewAeps) {
            SplashActivity.savePreferences("money", "aeps");
            try{
                if (SplashActivity.getPreferences(Constant.AEPS_1,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This service is not available, we working on it\nplease try after sometime");
                }else{
                    getUserState(userData.getUserId());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (v == transaction) {
          Intent intent=new Intent(getContext(), HistoryActivity.class);
          startActivity(intent);

            getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
        if (v ==addmoney){
            Intent intent=new Intent(getContext(), AddMoney.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
        if(v==transfer)
        {
            Intent intent=new Intent(getContext(),Dmt_Transfer.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
        if (v== cardViewRechargeThree){
            try{
                if (SplashActivity.getPreferences(Constant.RECHARGE_THREE,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This service is not available, we working on it\nplease try after sometime");
                }else {
                    Intent intent =new Intent(getActivity(), AgentRechargeThree.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }if (v== cardViewRechargeTwo){
            try{
                if (SplashActivity.getPreferences(Constant.RECHARGE_2,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This service is not available, we working on it\nplease try after sometime");
                }else {
                    Intent intent =new Intent(getActivity(), AgentRechargeTwo.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (v==cardViewMoneytransferTwo){
            try{
                if (SplashActivity.getPreferences(Constant.DMT_2,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This service is not available, we working on it\nplease try after sometime");
                }else {
                    Intent intent =new Intent(getActivity(), Dmt_Transfer.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(v==cardviewmicroatm){
            try{
                if (SplashActivity.getPreferences(Constant.Micro_atm,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This service is not available, we working on it\nplease try after sometime");
                }else {
                   microatm(userData.getUserId());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (v==cardViewAeps2){
            try{
                if (SplashActivity.getPreferences(Constant.AEPS_2,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This service is not available, we working on it\nplease try after sometime");
                }else {
                  getAeps2(userData.getUserId());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (v==cardViewBbps){
            try{
                if (SplashActivity.getPreferences(Constant.BBPS,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This service is not available, we working on it\nplease try after sometime");
                }else {
                    Intent intent =new Intent(getActivity(), BbpsActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (v == cardViewMoney) {
            SplashActivity.savePreferences("money", "transfer");
            Intent intent = new Intent(getActivity(), Transfer.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewMobile) {
            SplashActivity.savePreferences("recharge", "mobile");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewDth) {
            SplashActivity.savePreferences("recharge", "dth");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewDatacard) {
            SplashActivity.savePreferences("recharge", "datacard");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewElectricity) {
            SplashActivity.savePreferences("recharge", "electricity");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewGas) {
            SplashActivity.savePreferences("recharge", "gas");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewLandline) {
            SplashActivity.savePreferences("recharge", "landline");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewInsuarance) {
            SplashActivity.savePreferences("recharge", "insurance");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }
    /*user state*/
    public  void getUserState(final String userId) {
        String tag_string_req = "login_res";

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.AEPS_OUTLET, response -> {
            Log.d(TAG, "AEPS Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                //  Log.d(TAG, "Response: " + jarray.toString());

                Log.d(TAG,"status:"+status);
                if  (status.equals("1")) {

                          /*  Toast.makeText(getActivity(),
                                    message, Toast.LENGTH_LONG).show();*/
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


                          /*  if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Objects.requireNonNull(getActivity()))) {
                               // Toast.makeText(getActivity(),"Gps already enabled",Toast.LENGTH_SHORT).show();
                                enableLoc();
                            }else {*/
                    Log.e("AEPS","AEPS-->"+phone+" "+roundMobile+" "+roundPassword);
                    Intent i = new Intent(getActivity(), CALL_AEPS.class);
                    i.putExtra("MOBILE",phone);// user mobile no
                    i.putExtra("MERCHANT_USERID",roundMobile);// Roundpay Mobile no
                    i.putExtra("MERCHANT_PASSWORD",roundPassword);
                    startActivityForResult(i, 1);
                    //  }
                }else {
                    // Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "AEPS Error: " + error.getMessage());
            //  Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == INTENT_CODE) {
                //assert data != null;
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    data.getStringExtra("StatusCode");
                    data.getStringExtra("Message");
                    Log.e(TAG,"ok code"+ data.getStringExtra("StatusCode")+" "+data.getStringExtra("Message"));
                }else{
                    assert data != null;
                    data.getStringExtra("StatusCode");
                    data.getStringExtra("Message");
                    Log.e(TAG,"data code"+ data.getStringExtra("StatusCode")+" "+data.getStringExtra("Message"));
                }
            }
            if (requestCode == 1) {
                if (resultCode == 0) {
                    Log.e("Message", " data0 " + requestCode + " " + resultCode + " " + data.getStringExtra("Message"));
                }
                if (resultCode == 3) {
                    try {
                        String bankRrn = data.getStringExtra("bankRrn");
                        int type = data.getIntExtra("TransactionType", 0); //to get transaction name
                        String transAmount = data.getStringExtra("transAmount"); //to get response
                        String balAmount = data.getStringExtra("balAmount");
                        boolean status = data.getBooleanExtra("Status", false); //to get response message
                        String response = data.getStringExtra("Message"); //to get response message
                        Log.e("totalRes", " " + response);
                        Log.e("totalRESPONSE", " " + data);

                        try {
                            switch (type) {

                                case Constants.CASH_WITHDRAWAL:
                                    transType = "Cash Withdrawal";
                                    break;

                                case Constants.BALANCE_ENQUIRY:
                                    transType = "Balance Enquiry";
                                    break;

                                default:
                                    break;
                            }
                            if (response != null) {
                                // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();


                                if (type == Constants.BALANCE_ENQUIRY) {
                                  /*  Toast.makeText(getActivity(), "transType :" + transType + "\n"
                                            +"Bank RNN Number :" + bankRrn + "\n"
                                            +"Bank RNN Number :" + bankRrn + "\n"
                                            +"Status :" + status + "\n"
                                            +"balAmount :" + balAmount + "\n", Toast.LENGTH_LONG).show();*/
                                    Intent intent =new Intent(getActivity(),AspsTransfer.class);
                                    intent.putExtra(Constant.MESSAGE,response);
                                    intent.putExtra(Constant.BANK_RRN,bankRrn);
                                    intent.putExtra(Constant.TRANS_TYPE,transType);
                                    intent.putExtra(Constant.AMOUNT,balAmount);
                                    intent.putExtra(Constant.STATUS,status);
                                    startActivity(intent);
                                    Log.e("totalRes", " " + response+" "+ status);
                                }
                                if (type == Constants.CASH_WITHDRAWAL) {
                                   /* Toast.makeText(getActivity(),"transType :" + transType + "\n"
                                            + "Bank RNN Number :" + bankRrn + "\n"
                                            +"Bank RNN Number :" + bankRrn + "\n"
                                            +"Status :" + status + "\n"
                                            +"transAmount :" + transAmount + "\n", Toast.LENGTH_LONG).show();*/
                                    Intent intent =new Intent(getActivity(),AspsTransfer.class);
                                    intent.putExtra(Constant.MESSAGE,response);
                                    intent.putExtra(Constant.BANK_RRN,bankRrn);
                                    intent.putExtra(Constant.TRANS_TYPE,transType);
                                    intent.putExtra(Constant.AMOUNT,transAmount);
                                    intent.putExtra(Constant.STATUS,status);
                                    startActivity(intent);
                                    Log.e("totalRes", " " + response+" "+ status);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getActiveService(final String userId, final String userType) {
        String tag_string_req = "active_service";
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ACTIVE_SERVICE, response -> {
            Log.e(TAG, "Service Response: " + response);
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    JSONArray jsonArray=jsonObject.getJSONArray("status");
                    if (!jsonArray.isNull(0)){
                        for (int i = 0; i< jsonArray.length(); i++) {
                            JSONObject jobject = jsonArray.getJSONObject(i);

                            String id = jobject.getString("subServiceId");
                            String status = jobject.getString("subServicestatus");
                            String mainStatus=jobject.getString("mainStatus");
                            String mainId=jobject.getString("mainId");
                            //  String mainId = jobject.getString("mainId");

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
                                if (mainId.equalsIgnoreCase("18")){
                                    if (!mainStatus.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.DMT_2, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.DMT_2, "unhide");
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
                                        Log.d("tag","else ");
                                        SplashActivity.savePreferences(Constant.RECHARGE_TWO, "hide");
                                    } else {
                                        Log.d("tag","9serviceIdmain");
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
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.PREPAID,status);
                                        if (!status.equalsIgnoreCase("A")){
                                            SplashActivity.savePreferences(Constant.MOBILE_PREPAID,"hide");
                                        }else {
                                            SplashActivity.savePreferences(Constant.MOBILE_PREPAID,"unhide");
                                        }
                                    }
                                    if (id.equalsIgnoreCase("2")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.DTH,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewDth.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewDth.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("3")){
                                        SplashActivity.savePreferences(Constant.DATACARD,status);
                                       // Log.e("STATUS","STATUS-->"+status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewDatacard.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewDatacard.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("4")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.POSTPAID,status);
                                        if (!status.equalsIgnoreCase("A")){
                                            SplashActivity.savePreferences(Constant.MOBILE_POSTPAID,"hide");
                                        }else {
                                            SplashActivity.savePreferences(Constant.MOBILE_POSTPAID,"unhide");
                                        }
                                    }
                                    if (id.equalsIgnoreCase("5")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.ELECTRICITY,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewElectricity.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewElectricity.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("6")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.GAS,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewGas.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewGas.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("7")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.LANDLINE,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewLandline.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewLandline.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("8")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.INSURANCE,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewInsuarance.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewInsuarance.setVisibility(View.GONE);
                                        }
                                    }
                                    if (mainId.equalsIgnoreCase("13")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.AEPS,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewAeps.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewAeps.setVisibility(View.GONE);
                                        }
                                    }
                                    if (mainId.equalsIgnoreCase("14")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.MONEY,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewMoney.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewMoney.setVisibility(View.GONE);
                                        }
                                    }
                                    if (SplashActivity.getPreferences(Constant.MOBILE_PREPAID,"").equals("hide")&&
                                            SplashActivity.getPreferences(Constant.MOBILE_POSTPAID,"").equals("hide")){
                                        cardViewMobile.setVisibility(View.GONE);
                                    }else {
                                        cardViewMobile.setVisibility(View.VISIBLE);
                                    }
                                }*/
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e(TAG, "Active Service Error: " + error.getMessage())) {
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

    /*Micro atm*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void microatm(final String userId) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String tag_string_req = "aeps_2";
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.MICRO_ATM, response -> {
            Log.d(TAG, "Micro atm " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        UserData userData=PrefManager.getInstance(getActivity()).getUserData();
                        JSONObject jObj = jsonObject.getJSONObject("data");
                        JSONObject authObject = jsonObject.getJSONObject("authKey");

                        Log.e(TAG,"msg"+
                                authObject.getString("saltkey")+" "+
                                authObject.getString("secretkey")+" "+
                                jObj.getString("bc_id")+" "+
                                userId+" "+
                                jObj.getString("emailid")+" "+
                                jObj.getString("phone1"));

                        Intent intent = new Intent(getContext(), BluetoothMacSearchActivity.class);
                        intent.putExtra("saltKey", authObject.getString("saltkey"));
                        intent.putExtra("secretKey", authObject.getString("secretkey"));
                        intent.putExtra("BcId",  jObj.getString("bc_id"));
                        intent.putExtra("UserId", userData.getUid().replace("ZAM",""));
                        intent.putExtra("bcEmailId", jObj.getString("emailid"));
                        intent.putExtra("Phone1", jObj.getString("phone1"));
                        intent.putExtra("cpid","Your CP ID");//(If any)
                        startActivityForResult(intent, INTENT_CODE);

                               /* Intent intent = new Intent(getActivity(),DashboardActivity.class);
                                intent.putExtra("saltKey", authObject.getString("saltkey"));
                                intent.putExtra("secretKey", authObject.getString("secretkey"));
                                intent.putExtra("BcId", jObj.getString("bc_id"));
                                intent.putExtra("UserId", userData.getUid());
                                intent.putExtra("bcEmailId", jObj.getString("emailid"));
                                intent.putExtra("Phone1", jObj.getString("phone1"));
                                intent.putExtra("cpid", "Your CP ID");//(If any)
                                startActivityForResult(intent, INTENT_CODE);*/

                    }else {
                        String message=jsonObject.getString("message");
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Log.e(TAG, "AEPS Error: " + error.getMessage());
        }) {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserData userData=PrefManager.getInstance(getActivity()).getUserData();
        if (position==0){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_PREPAID,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Prepaid mobile service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"mobilePrepaid");
                WebService.getOperatorList("Prepaid", (FragmentActivity) getContext(),progressDialog);
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==1){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_POSTPAID,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Postpaid mobile bill service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("postpaid", getActivity(),progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"mobilePostpaid");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==2){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_DTH,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","DTH recharge service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("DTH", getActivity(),progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"dth");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==3){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_BROADBAND,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Broadband recharge service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Broadband",getActivity(),progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"broadband");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==4){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_ELECTRICITY,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Electricity bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Electricity",getActivity(),progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"electricity");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==5){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_LANDLINE,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Landline bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Landline",getActivity(),progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"landline");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==6){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_GAS,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Gas bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Gas", getActivity(),progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"gas");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==7){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_WATER,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Water bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("WaterBill",getActivity(),progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"water");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==8){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_CREDITCARD,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Credit card bill payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("CreditCard",  getActivity(),progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"creditCard");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else {
            if (SplashActivity.getPreferences(Constant.CUSTOMER_INSURANCE,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","insurance premium payment service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("Insurance", getActivity(),progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"insurance");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }
    }

    private void assignService(final String userId) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String tag_string_req = "assigned_service";
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ASSIGNED_SERVICE, response -> {
            Log.d(TAG, "assigned_service Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("S")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        if (!jsonArray.isNull(0)) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobject = jsonArray.getJSONObject(i);

                                String id = jobject.getString("id");
                                String serviceSatus = jobject.getString("status");
                                if (id.equalsIgnoreCase("20")){
                                    SplashActivity.savePreferences(Constant.RECHARGE_3,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        Log.d(TAG,"9");
                                        cardViewRechargeThree.setVisibility(View.VISIBLE);
                                    }else {
                                        Log.d(TAG,"no service");
                                        cardViewRechargeThree.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("9")){
                                    SplashActivity.savePreferences(Constant.DMT_2,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewMoneytransferTwo.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewMoneytransferTwo.setVisibility(View.GONE);
                                    }
                                }if (id.equalsIgnoreCase("18")){
                                    SplashActivity.savePreferences(Constant.RECHARGE_2,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewRechargeTwo.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewRechargeTwo.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("13")){
                                    SplashActivity.savePreferences(Constant.AEPS,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewAeps.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewAeps.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("14")){
                                    SplashActivity.savePreferences(Constant.MONEY,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewMoney.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewMoney.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("19")){
                                    SplashActivity.savePreferences(Constant.AEPS_2,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewAeps2.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewAeps2.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("17")){
                                    SplashActivity.savePreferences(Constant.BBPS,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewBbps.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewBbps.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }

                    }else {
                        String message=jsonObject.getString("message");
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Log.e(TAG, "assigned_service Error: " + error.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void assignService1(String userId) {

        String tag_string_req = "assigned_service";
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ASSIGNED_SERVICE, response -> {
            Log.d(TAG, "assigned_service Response: " + response);
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("S")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        if (!jsonArray.isNull(0)) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobject = jsonArray.getJSONObject(i);

                                String id = jobject.getString("id");
                                String serviceSatus = jobject.getString("status");
                                if (id.equalsIgnoreCase("20")){
                                    SplashActivity.savePreferences(Constant.RECHARGE_3,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewRechargeThree.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewRechargeThree.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("18")){
                                    SplashActivity.savePreferences(Constant.DMT_2,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewMoneytransferTwo.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewMoneytransferTwo.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("13")){
                                    SplashActivity.savePreferences(Constant.AEPS,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewAeps.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewAeps.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("14")){
                                    SplashActivity.savePreferences(Constant.MONEY,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewMoney.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewMoney.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("19")){
                                    SplashActivity.savePreferences(Constant.AEPS_2,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewAeps2.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewAeps2.setVisibility(View.GONE);
                                    }
                                }
                                if (id.equalsIgnoreCase("17")){
                                    SplashActivity.savePreferences(Constant.BBPS,status);
                                    if (serviceSatus.equalsIgnoreCase("A")){
                                        cardViewBbps.setVisibility(View.VISIBLE);
                                    }else {
                                        cardViewBbps.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }

                    }else {
                        String message=jsonObject.getString("message");
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            //  progressDialog.dismiss();
            Log.e(TAG, "assigned_service Error: " + error.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void getAeps2(final String userId) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String tag_string_req = "aeps_2";
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.AEPS_2_SERVICE, response -> {
            Log.d(TAG, "AEPS Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        UserData userData=PrefManager.getInstance(getActivity()).getUserData();
                        JSONObject jObj = jsonObject.getJSONObject("data");
                        JSONObject authObject = jsonObject.getJSONObject("authKey");

                        Log.e(TAG,""+
                                authObject.getString("saltkey")+" "+
                                authObject.getString("secretkey")+" "+
                                jObj.getString("bc_id")+" "+
                                userId+" "+
                                jObj.getString("emailid")+" "+
                                jObj.getString("phone1"));

                        Intent intent = new Intent(getContext(), DashboardActivity.class);
                        intent.putExtra("saltKey", authObject.getString("saltkey"));
                        intent.putExtra("secretKey", authObject.getString("secretkey"));
                        intent.putExtra("BcId",  jObj.getString("bc_id"));
                        intent.putExtra("UserId", userData.getUid().replace("ZAM",""));
                        intent.putExtra("bcEmailId", jObj.getString("emailid"));
                        intent.putExtra("Phone1", jObj.getString("phone1"));
                        intent.putExtra("cpid","Your CP ID");//(If any)
                        startActivityForResult(intent, INTENT_CODE);

                               /* Intent intent = new Intent(getActivity(),DashboardActivity.class);
                                intent.putExtra("saltKey", authObject.getString("saltkey"));
                                intent.putExtra("secretKey", authObject.getString("secretkey"));
                                intent.putExtra("BcId", jObj.getString("bc_id"));
                                intent.putExtra("UserId", userData.getUid());
                                intent.putExtra("bcEmailId", jObj.getString("emailid"));
                                intent.putExtra("Phone1", jObj.getString("phone1"));
                                intent.putExtra("cpid", "Your CP ID");//(If any)
                                startActivityForResult(intent, INTENT_CODE);*/

                    }else {
                        String message=jsonObject.getString("message");
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Log.e(TAG, "AEPS Error: " + error.getMessage());
        }) {
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

}
