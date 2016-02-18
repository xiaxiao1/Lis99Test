package com.lis99.mobile.newhome;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by yy on 16/2/18.
 */
public class HelpMovieActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息



    }






}
