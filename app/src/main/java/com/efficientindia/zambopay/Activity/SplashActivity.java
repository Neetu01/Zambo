package com.efficientindia.zambopay.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.efficientindia.zambopay.AppConfig.SessionManager;
import com.efficientindia.zambopay.R;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    static SharedPreferences sharedPreferences;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session=new SessionManager(SplashActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

        progressBar =  findViewById(R.id.progress_bar);
        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    private void doWork() {
        for (int progress=0; progress<100; progress+=30) {
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startApp() {

       /* Intent intent =new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);*/
        if (session.isLoggedIn()){
            Intent intent =new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent =new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

    public static void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getPreferences(String key, String val) {
        return sharedPreferences.getString(key, val);
    }
}
