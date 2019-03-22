package com.example.githubsearchdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * {@link WebView} to display a selected repository.
 */
public class WebViewActivity extends AppCompatActivity {

    private static String TAG = WebViewActivity.class.getSimpleName();
    public static final String WEBSITE_URL = "html_url";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url  = getIntent().getStringExtra(WEBSITE_URL);
        if (url == null || url.isEmpty()) finish();

        setContentView(R.layout.activity_webview);
        WebView webView = findViewById(R.id.webView);
        //webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
