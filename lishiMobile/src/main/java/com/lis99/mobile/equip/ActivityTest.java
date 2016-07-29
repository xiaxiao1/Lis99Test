package com.lis99.mobile.equip;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;

public class ActivityTest extends LSBaseActivity implements View.OnClickListener {


    private TextView text3;
    private ImageView iv;

    private AlertDialog al;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        text3 = (TextView) findViewById(R.id.Text3);
        text3.setMovementMethod(new ScrollingMovementMethod());
        iv = (ImageView) findViewById(R.id.iv);

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                httpGet();

                break;

            case R.id.button1:

                httpPost();
                break;
            case R.id.button2:

                getImage();
                break;

            case R.id.button3:

                aNR();
                break;

        }
    }




    private void aNR ()
    {

        try {
            Thread.sleep(24000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private void httpGet()
    {

        String url = C.CLUB_DETAIL_HEAD + "190";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.setTimeout(20000);

        Common.log("asyncHttpClient.getConnectTimeout()=="+asyncHttpClient.getConnectTimeout());

        asyncHttpClient.get(this, url, new AsyncHttpResponseHandler()
        {

            @Override
            public void onStart() {
                super.onStart();
                al = new AlertDialog.Builder(ActivityTest.this).setTitle("提示").setMessage("loading...").show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = null;
                result = new String(bytes).toString();
                result = Common.convert(result);
                Common.log("result = "+result);
                text3.setText(result);

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Common.log("Error = "+throwable.toString());
                text3.setText(throwable.toString());
            }

            @Override
            public void onFinish() {
                if ( al != null && al.isShowing() )
                {
                    al.cancel();
                }
                super.onFinish();
            }
        });

    }


    private void httpPost ()
    {

        String url = C.LS_SIGNIN;

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("email", "yy-cj@163.com");
        params.put("pwd", "");

        asyncHttpClient.post(this, url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Common.log("response="+response.toString());

                text3.setText(response.toString());


            }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String
                            responseString, Throwable throwable) {

                        text3.setText(throwable.toString());

                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        al = new AlertDialog.Builder(ActivityTest.this).setTitle("提示").setMessage("loading...").show();
                    }

                    @Override
                    public void onFinish() {
                        if ( al != null && al.isShowing() )
                        {
                            al.cancel();
                        }
                        super.onFinish();
                    }

        }

        );


    }

    private void getImage()
    {
        ImageLoader.getInstance().displayImage("http://i1.lis99.com/upload/photo/7/1/2/140-140_71d1c619574b8bb13c311915719b53e2.jpg", iv);
    }




}
