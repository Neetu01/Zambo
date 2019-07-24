package com.efficientindia.zambopay.Fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficientindia.zambopay.Activity.LoginActivity;
import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.R;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentRegistration extends Fragment implements View.OnClickListener {

    private TextView txtLogin;
    private EditText name,email,mobile,password;
    private Button btnRegister;
    private ProgressDialog pDialog;
    private ImageView imgShowPass,imgBack;
    private boolean showpass=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_registration,container,false);
        txtLogin=view.findViewById(R.id.txt_login_reg);
        txtLogin.setOnClickListener(this);
        pDialog=new ProgressDialog(getActivity());
      //  sponsor=view.findViewById(R.id.sponsor);
        imgBack=view.findViewById(R.id.img_back_register);
        imgBack.setOnClickListener(this);
        imgShowPass=view.findViewById(R.id.img_show_pass_login);
        name= view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        mobile=view.findViewById(R.id.mobile);
        password=view.findViewById(R.id.password);
        btnRegister=view.findViewById(R.id.btnregister);
        btnRegister.setOnClickListener(this);
        imgShowPass.setOnClickListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    /*ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    currentFragment=new FragmentLogin();
                    ft.replace(R.id.frame_login,currentFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();*/
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);

                    return true;
                }
                return true;
            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v==imgBack){
          /*  ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentLogin();
            ft.replace(R.id.frame_login,currentFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();*/
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);

        }
        if (v==txtLogin){
           /* ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentLogin();
            ft.replace(R.id.frame_login,currentFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();*/
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        }
        if (v==imgShowPass){
            int start, end;
            if (showpass) {
                start = password.getSelectionStart();
                end = password.getSelectionEnd();
                password.setTransformationMethod(new PasswordTransformationMethod());
                password.setSelection(start, end);
                showpass = false;
                imgShowPass.setImageResource(R.drawable.hide);

            } else {
                start = password.getSelectionStart();
                end = password.getSelectionEnd();
                password.setTransformationMethod(null);
                password.setSelection(start, end);
                showpass = true;
                imgShowPass.setImageResource(R.drawable.show);
            }
        }
        if (v==btnRegister){
         //   String spon=sponsor.getText().toString();
            String nam=name.getText().toString();
            String emai=email.getText().toString();
            String mob=mobile.getText().toString();
            String pass=password.getText().toString();
            if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))){
                /*if (spon.isEmpty()){
                    sponsor.setError("Spomsor Id");
                }
                else */if (nam.isEmpty()){
                    name.setError("Name");
                }
                else if (emai.isEmpty()){
                    email.setError("Email");
                }
                else if (mob.isEmpty()){
                    mobile.setError("Mobile");
                }
                else if (pass.isEmpty()){
                    password.setError("Password");
                }else {
                    //String regis="regis";
                    WebService.register(nam,emai,mob,pass,pDialog,getActivity());
                }
            }else {
                Toast.makeText(getActivity(),"check your internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }
}
