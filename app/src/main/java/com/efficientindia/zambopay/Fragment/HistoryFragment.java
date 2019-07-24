package com.efficientindia.zambopay.Fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.efficientindia.zambopay.Activity.HistoryActivity;
import com.efficientindia.zambopay.Adapter.PagerAdapterTransaction;
import com.efficientindia.zambopay.Adapter.WalletHistoryAdapter;
import com.efficientindia.zambopay.AppConfig.AppConfig;
import com.efficientindia.zambopay.AppConfig.AppController;
import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.Constant;
import com.efficientindia.zambopay.AppConfig.HttpsTrustManager;
import com.efficientindia.zambopay.AppConfig.PrefManager;
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.Model.WalletHistory;
import com.efficientindia.zambopay.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements View.OnClickListener , TabLayout.OnTabSelectedListener,
        WalletHistoryAdapter.WalletHistoryAdapterListener{

    private static final String TAG = HistoryActivity.class.getSimpleName();
    private ImageView imgBack;
    TabLayout tabLayout;
    @SuppressLint("StaticFieldLeak")
    public static ViewPager viewPager;
    RecyclerView requestListTransaction;
    private List<WalletHistory> listData;
    private WalletHistoryAdapter loadListAdapter;
    private ProgressDialog progressDialog;
    private RelativeLayout rlNoData;
    private ImageView imgData;
    private TextView txtData;
    LinearLayout lnTitle;

      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_history, container, false);
          rlNoData=view.findViewById(R.id.rl_wl_trans_recharge);
          imgData=view.findViewById(R.id.img_wl_trans_recharge);
          txtData=view.findViewById(R.id.txt_wl_trans_recharge);
          imgBack=view.findViewById(R.id.img_back_history);
          lnTitle=view.findViewById(R.id.ln_title_recharge);
          progressDialog=new ProgressDialog(getContext());
          requestListTransaction=view.findViewById(R.id.recyclerview_customer_history);
          imgBack.setOnClickListener(this);
          tabLayout = view.findViewById(R.id.tablayout_transaction);
          viewPager = view.findViewById(R.id.viewpager_transaction);
          UserData userData= PrefManager.getInstance(getActivity()).getUserData();
          if (!Configuration.hasNetworkConnection(getActivity())){
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                  Configuration.openPopupUpDownBack(getActivity(),R.style.Dialod_UpDown,"main","internetError",
                          "No internet connectivity");
              }
          }

          if (userData.getuType().equalsIgnoreCase("CS")){
              tabLayout.setVisibility(View.GONE);
              viewPager.setVisibility(View.GONE);
              lnTitle.setVisibility(View.VISIBLE);
              requestListTransaction.setVisibility(View.VISIBLE);
              if (Configuration.hasNetworkConnection(getActivity())){
                  getCustomerHistory(userData.getuType(),userData.getUserId());
              }else {
                  rlNoData.setVisibility(View.VISIBLE);
                  imgData.setImageResource(R.drawable.nointernet);
                  txtData.setText(R.string.no_internet);
                  requestListTransaction.setVisibility(View.GONE);
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                      Configuration.openPopupUpDownBack(getActivity(),R.style.Dialod_UpDown,"main","internetError",
                              "No internet connectivity");
                  }
              }
          }else {
              tabLayout.setVisibility(View.VISIBLE);
              viewPager.setVisibility(View.VISIBLE);
              lnTitle.setVisibility(View.GONE);
              requestListTransaction.setVisibility(View.GONE);
           /* tabLayout.addTab(tabLayout.newTab().setText("Recharge History"));
            tabLayout.addTab(tabLayout.newTab().setText("Bill Payment History"));*/
              tabLayout.addTab(tabLayout.newTab().setText("Wallet Transaction"));
              tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
              //  tabLayout.setba

              PagerAdapterTransaction adapter = new PagerAdapterTransaction(getChildFragmentManager(), tabLayout.getTabCount());
              viewPager.setAdapter(adapter);
              tabLayout.setOnTabSelectedListener(this);
              tabLayout.setupWithViewPager(viewPager);
          }
          listData = new ArrayList<>();
          loadListAdapter = new WalletHistoryAdapter(getActivity(), listData, this);
          RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
          requestListTransaction.setLayoutManager(mLayoutManager);
          requestListTransaction.setItemAnimator(new DefaultItemAnimator());
          requestListTransaction.setAdapter(loadListAdapter);
          return view;
      }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        if (v==imgBack){
           /* Intent intent=new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
            finish();*/
            Fragment fragment=new HomeFragment();
            FragmentTransaction fragmentTransaction1=getChildFragmentManager().beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
            fragmentTransaction1.replace(R.id.frame_main,fragment);
            fragmentTransaction1.commit();
        }
    }

    public void onBackPressed() {
        Fragment fragment=new ProfileFragment();
        FragmentTransaction fragmentTransaction1=getChildFragmentManager().beginTransaction();
        fragmentTransaction1.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
        fragmentTransaction1.replace(R.id.frame_main,fragment);
        fragmentTransaction1.commit();
        /*Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
        finish();*/
    }

    private void getCustomerHistory(String userType, String userId) {
        String tag_string_req = "fund_req";

        Configuration.showDialog("Please wait...",progressDialog);

        HttpsTrustManager.allowAllSSL();

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                AppConfig.WALLET_STATEMENT,
                response -> {

                    Log.d(TAG,"Wallet RESPONSED"+response+" --->"+userType+" ---->"+userId);

                    try {

                        JSONObject jsonObject=new JSONObject(response);
                        String responseCode=jsonObject.getString("status");

                        if (responseCode.equalsIgnoreCase("1")){
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            if (jsonArray.isNull(0)){
                                rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                                txtData.setText("No Transaction");
                                requestListTransaction.setVisibility(View.GONE);
                            }else {
                                rlNoData.setVisibility(View.GONE);
                                requestListTransaction.setVisibility(View.VISIBLE);
                                List<WalletHistory> items = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<WalletHistory>>() {
                                        }.getType());

                                listData.clear();
                                listData.addAll(items);
                                loadListAdapter.notifyDataSetChanged();

                            }
                        }else {
                            rlNoData.setVisibility(View.VISIBLE);
                            imgData.setImageResource(R.drawable.norequest);
                            txtData.setText("No Transaction/Something went wrong");
                            requestListTransaction.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        rlNoData.setVisibility(View.VISIBLE);
                        imgData.setImageResource(R.drawable.norequest);
                        txtData.setText("No Transaction");
                        requestListTransaction.setVisibility(View.GONE);
                    }

                    progressDialog.dismiss();
                },
                error -> {

                    progressDialog.dismiss();
                    // Toast.makeText(getActivity(),"Try again",Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDownBack(getActivity(),R.style.Dialod_UpDown,"main","error",
                                "Something went wrong\n Try after sometime");
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    @Override
    public void onContactSelected(WalletHistory contact) {

    }
}
