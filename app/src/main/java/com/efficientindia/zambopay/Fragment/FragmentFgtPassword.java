package com.efficientindia.zambopay.Fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.efficientindia.zambopay.Activity.LoginActivity;
import com.efficientindia.zambopay.R;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentFgtPassword extends Fragment implements View.OnClickListener {

    private TextView txtLogin,txtRegister;
    Fragment currentFragment;
    FragmentTransaction ft;
    private ImageView imgBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_forgot,container,false);
        txtLogin=view.findViewById(R.id.login);
        txtRegister=view.findViewById(R.id.register_fgt);
        imgBack=view.findViewById(R.id.img_back_forgot);
        imgBack.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                  /*  ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
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
        return  view;
    }


    @Override
    public void onClick(View v) {
        if (v==imgBack){
           /* ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
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
        if (v==txtRegister){
            ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentRegistration();
            ft.replace(R.id.frame_login,currentFragment);
            ft.commit();
        }
    }
}
