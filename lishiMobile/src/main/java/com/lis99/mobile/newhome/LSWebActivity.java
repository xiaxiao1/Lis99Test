package com.lis99.mobile.newhome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lis99.mobile.R;

public class LSWebActivity extends Activity {
	
	WebView webView;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getIntent().getStringExtra("url");
		setContentView(R.layout.activity_lsweb);
		webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl(url);
		
		webView.getSettings().setJavaScriptEnabled(true);
		
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		
		View backView = findViewById(R.id.iv_back);
		backView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		
	}
	
	
	
}
