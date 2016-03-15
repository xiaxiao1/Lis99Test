package com.lis99.mobile.util.letv;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;

import com.letv.skin.v4.V4PlaySkin;
import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.Common;

/**
 * Created by yy on 16/3/3.
 */
public class MovieActivity extends LSBaseActivity
{

    private V4PlaySkin skin;
    private Bundle bundle;

    private String uuid = "t5yp4dejpl", vuid = "2710826954", skey = "696ed275226b5c81a4e53cd6d2314e95";

    private LeTvUtil leTvUtil;

    private LetvSimplePlayBoard playBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_activity);

        vuid = getIntent().getStringExtra("VUID");

        skin = (V4PlaySkin) findViewById(R.id.videoView);

        if ( TextUtils.isEmpty(vuid) )
        {
            Common.toast("视频地址错误");
            return;
        }

        playBoard = new LetvSimplePlayBoard();

        bundle = LeTvUtil.setVodParams(uuid, vuid, "", skey, "", false);

        playBoard.init(this, bundle, skin);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playBoard != null) {
            playBoard.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playBoard != null) {
            playBoard.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playBoard != null) {
            playBoard.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (playBoard != null) {
            playBoard.onConfigurationChanged(newConfig);
        }
    }

}
