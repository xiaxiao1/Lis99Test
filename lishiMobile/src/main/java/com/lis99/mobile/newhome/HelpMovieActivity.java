package com.lis99.mobile.newhome;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.PushManager;
import com.lis99.mobile.util.SharedPreferencesHelper;

/**
 * Created by yy on 16/2/18.
 */
public class HelpMovieActivity extends Activity implements View.OnClickListener {


    private VideoView videoView;
    private ImageView ivName;
    private int mPositionWhenPaused = -1;
    private Button btn;

    private String isStting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);//去掉信息

        setContentView(R.layout.help_movie);

        isStting = getIntent().getStringExtra("ISSETTING");

        videoView = (VideoView) findViewById(R.id.videoView);
        ivName = (ImageView) findViewById(R.id.iv_name);
        btn = (Button) findViewById(R.id.startbtn);
        btn.setOnClickListener(this);

        ivName.setVisibility(View.GONE);
//        btn.setVisibility(View.GONE);
        btn.setVisibility(View.VISIBLE);

        String uri = "android.resource://" + getPackageName() + "/" + R.raw.lis99_movie;

        videoView.setVideoPath(uri);

        videoView.requestFocus();
        videoView.start();


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.start();
                if ( ivName.getVisibility() == View.GONE )
                {
                    ivName.setVisibility(View.VISIBLE);
//                    btn.setVisibility(View.VISIBLE);
                }
//                Log.w("MYUTIL", "1111111111111111111");
//                toast("play");
            }
        });


    }

    public void onPause() {
        // Stop video when the activity is pause.
        mPositionWhenPaused = videoView.getCurrentPosition();
        videoView.stopPlayback();

        super.onPause();
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub

        if(mPositionWhenPaused >= 0) {
            videoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }

        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
        videoView = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startbtn:
                //TODO implement
                startbutton();
                break;
        }
    }


    public void startbutton() {

        if ( "ISSETTING".equals(isStting))
        {
            finish();
            return;
        }

        Intent intent = new Intent();
        intent.setClass(this, NewHomeActivity.class);

        intent.putExtra(PushManager.TAG, PushManager.getInstance().getPushModel(this.getIntent()));

        startActivity(intent);
        SharedPreferencesHelper.saveHelp("help");
        this.finish();
    }


}
