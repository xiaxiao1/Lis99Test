//package com.lis99.mobile.util.letv;
//
//import android.os.Bundle;
//
//import com.letv.skin.v4.V4PlaySkin;
//import com.lis99.mobile.R;
//import com.lis99.mobile.club.LSBaseActivity;
//
///**
// * Created by yy on 16/3/3.
// */
//public class MovieActivity extends LSBaseActivity
//{
//
//    private V4PlaySkin skin;
//    private Bundle bundle;
//
//    private final String uuid = "t5yp4dejpl", vuid = "2710826954", skey = "696ed275226b5c81a4e53cd6d2314e95";
//
//    private LeTvUtil leTvUtil;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.movie_activity);
//
//        skin = (V4PlaySkin) findViewById(R.id.videoView);
//
//        bundle = LeTvUtil.setVodParams(uuid, vuid, "", skey, "", false);
//
//
//
//        initViews();
//
//    }
//
//
//    @Override
//    protected void initViews()
//    {
//        super.initViews();
//
//        leTvUtil = new LeTvUtil();
//        leTvUtil.init(this, bundle, skin);
//
//
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if ( leTvUtil != null )
//        {
//            leTvUtil.onResume();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if ( leTvUtil != null )
//        {
//            leTvUtil.onPause();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if ( leTvUtil != null )
//        {
//            leTvUtil.onDestroy();
//        }
//    }
//}
