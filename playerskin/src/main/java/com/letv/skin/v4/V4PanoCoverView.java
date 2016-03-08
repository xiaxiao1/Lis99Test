package com.letv.skin.v4;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lecloud.js.config.model.LeConfig;
import com.lecloud.leutils.ReUtils;
import com.lecloud.leutils.SPHelper;
import com.letv.skin.BaseView;
import com.letv.skin.activity.FeedBackActivity;
import com.letv.skin.interfacev1.OnNetWorkChangeListener;
import com.letv.skin.utils.NetworkUtils;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.IPlayer;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.notice.UIObserver;

import org.json.JSONObject;

import java.util.Observable;

/**
 * 提示view
 *
 * @author pys
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class V4PanoCoverView extends BaseView implements UIObserver {
    private static final String TAG = "V4PanoCoverView";

    private final String XMLNAME = "notic_config";
    private final String PREF_PANO_NOTIC_KEY = "pano_touch_notic";

    public V4PanoCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4PanoCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4PanoCoverView(Context context) {
        super(context);
        setVisibility(View.GONE);
    }

    private void saveShowed() {
        SPHelper spHelper = new SPHelper(context, XMLNAME);
        spHelper.putBoolean(PREF_PANO_NOTIC_KEY, true);
    }

    public boolean isShowed() {
        SPHelper spHelper = new SPHelper(context, XMLNAME);
        return spHelper.getBoolean(PREF_PANO_NOTIC_KEY, false);

    }

    @Override
    protected void initPlayer() {
        if(!isShowed()) {
            player.attachObserver(this);
        }
    }

    @Override
    protected void initView(final Context context) {
        if (!isShowed()) {
            LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_v4_pano_cover_layout"), this);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveShowed();
                    setVisibility(View.GONE);
                    uiPlayContext.panoNoticShowing = false;
                }
            });
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle) data;
        if (IPlayer.MEDIA_EVENT_FIRST_RENDER == bundle.getInt("state")) {
            setVisibility(View.VISIBLE);
            uiPlayContext.panoNoticShowing = true;
        }
    }
}
