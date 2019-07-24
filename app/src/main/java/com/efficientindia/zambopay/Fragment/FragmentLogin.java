package com.efficientindia.zambopay.Fragment;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficientindia.zambopay.AppConfig.Configuration;
import com.efficientindia.zambopay.AppConfig.SessionManager;
import com.efficientindia.zambopay.AppConfig.WebService;
import com.efficientindia.zambopay.R;

import java.util.Objects;


public class FragmentLogin extends Fragment implements View.OnClickListener {
    private TextView txtForgot, txtRegister;
    private EditText editTextUsername,editTextPassword;
    private ImageView imgShowPass;
    private boolean showpass=false;
    private Button btnLogin;
    private ProgressDialog pDialog;
    SessionManager session;
    Fragment currentFragment;
    FragmentTransaction ft;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        txtForgot=view.findViewById(R.id.forgot);
        txtRegister=view.findViewById(R.id.register);
        session=new SessionManager(getActivity());
        editTextPassword=view.findViewById(R.id.password_login);
        editTextUsername=view.findViewById(R.id.username_login);
        imgShowPass=view.findViewById(R.id.img_showpass);
        pDialog=new ProgressDialog(getActivity());
        btnLogin=view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        imgShowPass.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        txtForgot.setOnClickListener(this);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v==txtForgot){
            ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentFgtPassword();
            ft.replace(R.id.frame_login,currentFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
            ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentRegistration();
            ft.replace(R.id.frame_login,currentFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
        if (v==btnLogin){
            String username=editTextUsername.getText().toString();
            String password=editTextPassword.getText().toString();
            if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))){
                if (username.isEmpty()||password.isEmpty()){
                    Toast.makeText(getActivity(),"Please enter details",Toast.LENGTH_LONG).show();
                } else {
                    WebService.login(username,password,pDialog,getActivity(),session);
                }
            }else {
                Toast.makeText(getActivity(),"Check your internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }
}
