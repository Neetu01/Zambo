package com.efficientindia.zambopay.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.efficientindia.zambopay.R;

public class WebActivity extends AppCompatActivity {

    public static final String WEBSITE_ADDRESS = "website_address";
    WebView webView;
    TextView textView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView=findViewById(R.id.webview);
        textView=findViewById(R.id.txt_home_title);
        imageView=findViewById(R.id.imgback_add_money);
        String url  = getIntent().getStringExtra(WEBSITE_ADDRESS);
        switch (url)
        {
            case "https://zambo.in/terms":
                textView.setText(R.string.pr_pol);
                break;

            case "https://zambo.in/contact":
                textView.setText(R.string.support_desk);
                break;

            case "https://zambo.in/refund":
                textView.setText(R.string.refund);
                break;
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(WebActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
            }
        });
        if (url == null || url.isEmpty()) finish();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
