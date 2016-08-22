package com.lis99.mobile.entry.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lis99.mobile.entry.LsStartupActivity;

/**
 * Created by yy on 16/8/22.
 *      空activity， 用来防止启动太慢的问题
 */
public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LsStartupActivity.class));
        finish();
    }
}
