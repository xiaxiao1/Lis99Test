package com.lis99.mobile.webview;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.WebViewUtil;

/**
 * Created by yy on 15/11/17.
 */
public class ChoicenessRouteWebView extends LSBaseActivity {

    private WebView webView;

    private final String url = "http://m.lis99.com/club/areaweb/destlist";

    private View titleLeft, tv_close, titleRight;

    private TextView title;

    protected TextView setTitle ( String text )
    {
        if ( title != null )
        title.setText(text);
        return title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_route_webview);

        initViews();

        titleLeft = findViewById(R.id.titleLeft);
        titleRight = findViewById(R.id.titleRight);
        tv_close = findViewById(R.id.tv_close);
        title = (TextView) findViewById(R.id.title);

        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        tv_close.setOnClickListener(this);


    }

    @Override
    protected void initViews() {


        webView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        // 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);

        webView.setWebChromeClient(new MyWebChromeClient());

        webView.setWebViewClient(WebViewUtil.getInstance().getWebviewClient());

        webView.addJavascriptInterface(WebViewUtil.getInstance().getTianJinJS(), "LS");


        webView.loadUrl(url);


    }

     class MyWebChromeClient extends WebChromeClient {

        public MyWebChromeClient() {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);

        }


    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId())
        {
            case R.id.titleLeft:
                if ( webView.canGoBack() )
                {
                    webView.goBack();
                }
                else
                {
                    finish();
                }
                break;
            case R.id.titleRight:
                webView.reload();
                break;
            case R.id.tv_close:
                finish();
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
