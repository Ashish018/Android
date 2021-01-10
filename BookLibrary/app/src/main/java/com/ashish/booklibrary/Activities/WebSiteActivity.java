package com.ashish.booklibrary.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ashish.booklibrary.R;

public class WebSiteActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_site);
        webView=findViewById(R.id.webView);
        webView.loadUrl("https://www.google.com/");
        webView.setWebViewClient(new WebViewClient()); // This will open the web page in app only and not in new browser like chrome
    }

    @Override
    public void onBackPressed() {

        if(webView.canGoBack()) webView.goBack(); // if we dont put this then on pressing back button user will come to the app page and not on the previous web page
        else super.onBackPressed();
    }
}