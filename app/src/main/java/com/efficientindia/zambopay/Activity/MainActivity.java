package com.efficientindia.zambopay.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.efficientindia.zambopay.AppConfig.AppConfig;
import com.efficientindia.zambopay.AppConfig.AppController;
import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.Constant;
import com.efficientindia.zambopay.AppConfig.HttpsTrustManager;
import com.efficientindia.zambopay.AppConfig.PrefManager;
import com.efficientindia.zambopay.AppConfig.SessionManager;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.BuildConfig;
import com.efficientindia.zambopay.Fragment.AddMoneyFragment;
import com.efficientindia.zambopay.Fragment.HistoryFragment;
import com.efficientindia.zambopay.Fragment.HomeFragment;
import com.efficientindia.zambopay.Model.UserData;
import com.efficientindia.zambopay.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

//@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    Enddrawertoggle enddrawertoggle;
    ImageView imageView;
    Animation animationup,animationdown;
    LinearLayout linearLayout;
    BottomSheetBehavior bottomSheetBehavior;
    FragmentTransaction ft;
    Fragment currentFragment = null;
    SessionManager session;
    TextView txtName, txtEmail,txtVersion;
    ProgressDialog progressDialog;
    CircleImageView profileImage;
    @SuppressLint("StaticFieldLeak")
    public static TextView ui_hot;
    BottomNavigationView bottomNavigationView;
    protected static final String TAG = "LocationOnOff";
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    RecyclerView.LayoutManager layoutManager;
    TextView textView;
    HomeFragment homeFragment;
    RecyclerView recyclerView;
    RecyclerView.Adapter recylerview;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recyclerView=findViewById(R.id.recharge);
        imageView=findViewById(R.id.imageView);
        txtVersion=findViewById(R.id.txt_version);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView=findViewById(R.id.bottomnavigation);
      //  BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        animationup= AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_up);
        animationdown=AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_down);
       // recyclerView.setLayoutManager(layoutManager);

        // bottomSheetBehavior=BottomSheetBehavior.from(linearLayout);

//        ViewCompat.setLayoutDirection(toolbar, ViewCompat.LAYOUT_DIRECTION_RTL);
       /* BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }*/
        gpsCheck();
        if (!Configuration.hasNetworkConnection(MainActivity.this)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.openPopupUpDown(MainActivity.this,R.style.Dialod_UpDown,"internetError",
                        "No internet connectivity");
            }
        }
        progressDialog = new ProgressDialog(MainActivity.this);
        session = new SessionManager(MainActivity.this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //   Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'><b> " + "ZAM" + "</b></font>" + "<font color='#FFFFFF'>" + "BO" + "</font>"));
        UserData userData = PrefManager.getInstance(MainActivity.this).getUserData();
        String name = userData.getName();
        String email = userData.getEmail();
        String profilePhoto = userData.getPhoto();
        WebService.getBalance(userData.getUserId());
        String version= BuildConfig.VERSION_NAME;
        txtVersion.setText(version);

      /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

      /*bottomNavigation*/
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment=null;
                switch (item.getItemId()) {

                    case R.id.home:

                        fragment=new HomeFragment();
                        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_main,fragment);
                        overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
                        fragmentTransaction.commit();
                        toolbar.setVisibility(View.VISIBLE);
                        break;

                    case R.id.addmoney:

                        fragment=new AddMoneyFragment();
                        FragmentTransaction fragmentTransaction2=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                        fragmentTransaction2.replace(R.id.frame_main,fragment);
                        fragmentTransaction2.commit();
                        toolbar.setVisibility(View.GONE);
                        break;

                    case R.id.history:

                       // navigationView.removeHeaderView(navigationView.getHeaderView(0));

                        fragment=new HistoryFragment();
                        FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                        fragmentTransaction1.replace(R.id.frame_main,fragment);
                        fragmentTransaction1.commit();
                        toolbar.setVisibility(View.GONE);
                        break;

                    case R.id.more:

                        BottomSheet bottomSheet=new BottomSheet();
                        bottomSheet.show(getSupportFragmentManager(),bottomSheet.getTag());
                        break;
                }
                return false;
            }
        });




      if (Configuration.hasNetworkConnection(MainActivity.this)) {
          getProfile(userData.getUserId());
          final Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                  getProfile(userData.getUserId());
                  handler.postDelayed(this, 6000);
              }
          }, 6000);
      }else {
          Configuration.openPopupUpDown(MainActivity.this,R.style.Dialod_UpDown,"internetError",
                  "No Internet Connectivity"+
                          ", Thanks");
      }
        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new HomeFragment();
        ft.replace(R.id.frame_main, currentFragment);
        ft.commit();

        /*Navigation Icon*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {

                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        toggle.syncState();


/*details on navigation header*/
        initNavigationDrawer();
        txtName = navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.text_email);
        profileImage = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        txtName.setText(name);
        txtEmail.setText(email);
        navigationView.setNavigationItemSelectedListener(this);
        Picasso.with(MainActivity.this).load(profilePhoto).fit().centerCrop()
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(profileImage);
      //  WebService.getBalance(userData.getUserId());

    }

    private void gpsCheck() {
        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainActivity.this)) {
            Log.e("TAG","Gps already enabled");
            enableLoc();
        }else{
            Log.e("TAG","Gps already enabled");
        }
    }
    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }
    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(connectionResult -> Log.d("Location error","Location error " + connectionResult.getErrorCode())).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(result1 -> {
                final Status status = result1.getStatus();
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                }
            });
        }
    }
    public void initNavigationDrawer() {


        enddrawertoggle = new Enddrawertoggle(this,
                drawer,
                toolbar,
                "open",
                "close");

        drawer.addDrawerListener(enddrawertoggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        enddrawertoggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
            if (session.isLoggedIn()){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            Intent launchNextActivity;
                            launchNextActivity = new Intent(Intent.ACTION_MAIN);
                            launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                            launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(launchNextActivity);
                            finish();
                        }).create().show();
            }
            else {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main, menu);
        //UserData userData=PrefManager.getInstance(MainActivity.this).getUserData();
   //     MenuInflater menuInflater = getMenuInflater();
   //     menuInflater.inflate(R.menu.main, menu);
       /* final MenuItem menu_hotlist = menu.findItem(R.id.wallet);
        MenuItemCompat.setActionView(menu_hotlist, R.layout.wallet_count);
        final View menu_hot = MenuItemCompat.getActionView(menu_hotlist);
        ui_hot =  menu_hot.findViewById(R.id.wallet_count1);
        if (Configuration.hasNetworkConnection(MainActivity.this)){
            try {
                ui_hot.setText("\u20B9 "+SplashActivity.getPreferences(Constant.BALANCE, ""));
            }catch (Exception e){
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(ui_hot.getText().toString())) {
                WebService.getBalance(userData.getUserId());
            }
        }else {
            Configuration.openPopupUpDown(MainActivity.this,R.style.Dialod_UpDown,"internetError",R.string.no_internet +
                    ", Thanks");
        }
       /* UserData userData=PrefManager.getInstance(MainActivity.this).getUserData();
        String wallet=userData.getWallet();
        ui_hot.setText("Rs. "+wallet+"/-");*/
      //  View count = menu.findItem(R.id.notic).getActionView();
      /*  View wcount = menu.findItem(R.id.wallet).getActionView();
       *//* walletcountb = (Button) wcount.findViewById(R.id.wallet_count1);
        walletcountb.setText(String.valueOf(walletcount)+"/-");*//*
        kingcountb.setBackgroundResource(R.drawable.wall);
        kingcountb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //	displayView(15);
            }
        });*/
        return true;
    }

  /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.wallet1) {
            return true;
        }else if(id==R.id.wallet)
        {
           if(homeFragment.isShown())
            {
                homeFragment.setVisibility(View.GONE);
                homeFragment.setAnimation(animationup);
            }else{
                homeFragment.setVisibility(View.VISIBLE);
                homeFragment.setAnimation(animationdown);
            }
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      /*if (id == R.id.nav_home) {
            ft=getSupportFragmentManager().beginTransaction();
            currentFragment=new HomeFragment();
            ft.replace(R.id.frame_main,currentFragment);
            ft.commit();
        } else if (id == R.id.nav_history) {
            Intent intent=new Intent(MainActivity.this,HistoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        } else if (id == R.id.nav_myprofile) {
            Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        }else*/ if (id==R.id.nav_logout){
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Logging out...");
            try {
                progressDialog.show();
            }catch (Exception e){e.printStackTrace();}
            final Handler handler = new Handler();
            handler.postDelayed(this::logout,2000);
        }/*else if (id==R.id.nav_add_money){
            Intent intent=new Intent(MainActivity.this,AddMoney.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }*/else if(id==R.id.nav_terms)
        {
            Intent i = new Intent(MainActivity.this, WebActivity.class);
            i.putExtra(WebActivity.WEBSITE_ADDRESS, "https://zambo.in/terms");
            startActivity(i);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        }else if(id==R.id.nav_refund)
        {
            Intent i = new Intent(MainActivity.this, WebActivity.class);
            i.putExtra(WebActivity.WEBSITE_ADDRESS, "https://zambo.in/refund");
            startActivity(i);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }else if(id==R.id.nav_support)
        {
            Intent i = new Intent(MainActivity.this, WebActivity.class);
            i.putExtra(WebActivity.WEBSITE_ADDRESS, "https://zambo.in/contact");
            startActivity(i);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void logout() {
        PrefManager.getInstance(MainActivity.this).logout();
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().clear().apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void getProfile(final String userId) {
        String tag_string_req = "register_res";

        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PROFILE_DATA, response -> {
                    Log.d(TAG, "Profile Response: " + response);

                   // pDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if (status.equals("1")){
                            JSONObject jObj=jsonObject.getJSONObject("data");
                            if (!jObj.getString("uStatus").equalsIgnoreCase("A")){
                                logout();
                                Configuration.openPopupUpDown(MainActivity.this,R.style.Dialod_UpDown,"internetError",
                                        "Session Expired\n Please Login again"+
                                                ", Thanks");
                            }
                        }else {
                            Configuration.openPopupUpDown(MainActivity.this,R.style.Dialod_UpDown,"internetError",
                                    "Something went wrong\nTry after sometime"+
                                            ", Thanks");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
            Log.e(TAG, "Profile Error: " + error.getMessage());
          /*  Toast.makeText(ProfileActivity.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();*/
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
/*Bottom Navigation*/
    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                  //  item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                     //   Toast.makeText(MainActivity.this, "Gps enabled", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                       finish();
                        break;
                    default:
                        break;
                }
                break;
        }
        try {
            if (requestCode == 1) {
                if (resultCode == 0) {
                    Log.e("Mess", " data " + requestCode + " " + resultCode + " " + data.getStringExtra("Message"));
                }
                if (resultCode == 3) {
                    try {
                        assert data != null;
                        String bankRrn = data.getStringExtra("bankRrn");
                        int type = data.getIntExtra("TransactionType", 0); //to get transaction name
                        String transAmount = data.getStringExtra("transAmount"); //to get response
                        String balAmount = data.getStringExtra("balAmount");
                        boolean status = data.getBooleanExtra("Status", false); //to get response message
                        String response = data.getStringExtra("Message"); //to get response message
                        Log.e("totalRes", " " + response);

                        try {
                            String transType = null;
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
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();

                                if (type == Constants.BALANCE_ENQUIRY) {
                                    Toast.makeText(MainActivity.this, "transType :" + transType + "\n"
                                            +"Bank RNN Number :" + bankRrn + "\n"
                                            +"Bank RNN Number :" + bankRrn + "\n"
                                            +"Status :" + status + "\n"
                                            +"balAmount :" + balAmount + "\n", Toast.LENGTH_LONG).show();
                                    Intent intent =new Intent(MainActivity.this,AspsTransfer.class);
                                    intent.putExtra(Constant.RESPONSE,response);
                                    startActivity(intent);
                                    Log.e("totalRes", " " + response);
                                }
                                if (type == Constants.CASH_WITHDRAWAL) {
                                    Toast.makeText(MainActivity.this,"transType :" + transType + "\n"
                                            + "Bank RNN Number :" + bankRrn + "\n"
                                            +"Bank RNN Number :" + bankRrn + "\n"
                                            +"Status :" + status + "\n"
                                            +"transAmount :" + transAmount + "\n", Toast.LENGTH_LONG).show();
                                    Intent intent =new Intent(MainActivity.this,AspsTransfer.class);
                                    intent.putExtra(Constant.RESPONSE,response);
                                    startActivity(intent);
                                    Log.e("totalRes", " " + response);
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
*/

    @Override
    public void onLocationChanged(Location location) {
        Log.e("LOCATION","LOCATION--->"+location.getLatitude()+" "+location.getLongitude());
    }
}
