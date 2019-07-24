package com.efficientindia.zambopay.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.SessionManager;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.Fragment.FragmentFgtPassword;
import com.efficientindia.zambopay.Fragment.FragmentRegistration;
import com.efficientindia.zambopay.R;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout frameLayout;
    private ScrollView scrollView;
    private TextView txtForgot, txtRegister;
    private EditText editTextUsername,editTextPassword;
    private ImageView imgShowPass;
    private boolean showpass=false;
    private Button btnLogin;
    private ProgressDialog pDialog;
    SessionManager session;
    Fragment currentFragment;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        frameLayout=findViewById(R.id.frame_login);
        scrollView=findViewById(R.id.scrollview_login);
        txtForgot=findViewById(R.id.forgot);
         txtRegister=findViewById(R.id.register);
        session=new SessionManager(LoginActivity.this);
        editTextPassword=findViewById(R.id.password_login);
        editTextUsername=findViewById(R.id.username_login);
        imgShowPass=findViewById(R.id.img_showpass);
        pDialog=new ProgressDialog(LoginActivity.this);
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        imgShowPass.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        txtForgot.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v==txtForgot){
            frameLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            ft=getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentFgtPassword();
            ft.replace(R.id.frame_login,currentFragment);
            ft.commit();
        }
        if (v==imgShowPass){
            int start, end;
            if (showpass) {
                start = editTextPassword.getSelectionStart();
                end = editTextPassword.getSelectionEnd();
                editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                editTextPassword.setSelection(start, end);
                showpass = false;
                imgShowPass.setImageResource(R.drawable.hide);
            } else {
                start = editTextPassword.getSelectionStart();
                end = editTextPassword.getSelectionEnd();
                editTextPassword.setTransformationMethod(null);
                editTextPassword.setSelection(start, end);
                showpass = true;
                imgShowPass.setImageResource(R.drawable.show);
            }
        }
        if (v==txtRegister){
            frameLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            ft=getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentRegistration();
            ft.replace(R.id.frame_login,currentFragment);
            ft.commit();
        }
        if (v==btnLogin){
            String username=editTextUsername.getText().toString();
            String password=editTextPassword.getText().toString();
            if (Configuration.hasNetworkConnection(LoginActivity.this)){
                if (username.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please enter details",Toast.LENGTH_LONG).show();
                } else {
                    WebService.login(username,password,pDialog,LoginActivity.this,session);
                }
            }else {
                Toast.makeText(LoginActivity.this,"Check your internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent launchNextActivity;
                        launchNextActivity = new Intent(Intent.ACTION_MAIN);
                        launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                        launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(launchNextActivity);
                        finish();
                    }
                }).create().show();
    }
}
