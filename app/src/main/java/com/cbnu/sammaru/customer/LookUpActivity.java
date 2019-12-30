package com.cbnu.sammaru.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cbnu.sammaru.R;

public class LookUpActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;
    private String destinationUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_up);

        destinationUrl = getIntent().getStringExtra("destinationUrl");

        Log.d("LookUpActivity ", "url " + destinationUrl);

        mWebView = findViewById(R.id.activity_look_up_webView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(destinationUrl);
    }
}
