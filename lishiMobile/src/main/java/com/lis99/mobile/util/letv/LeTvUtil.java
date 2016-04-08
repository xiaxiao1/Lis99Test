package com.lis99.mobile.util.letv;

import android.content.Context;
import android.os.Bundle;

import com.letv.controller.PlayContext;
import com.letv.controller.PlayProxy;
import com.letv.controller.interfacev1.ILetvPlayerController;
import com.letv.skin.utils.UIPlayContext;
import com.letv.skin.v4.V4PlaySkin;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.widget.ILeVideoView;

/**
 * Created by yy on 16/3/3.
 */
public class LeTvUtil
{

    private ISplayer player;
    private PlayContext playContext;
    private ILeVideoView videoView;
    private Context mContext;
    private V4PlaySkin skin;
    private UIPlayContext uicontext;
    private ILetvPlayerController playerController;

    private Bundle mBundle;
    private String path = "";
    private long lastPosition;

    private int skinBuildType;

//    private static LeTvUtil instance;
//
//    public static LeTvUtil getInstance (Context c )
//    {
//        this.mContext = c;
//        if ( instance == null ) instance = new LeTvUtil();
//        return instance;
//    }


    /**
     * 乐视云点播参数设置
     * 没有的数值传空字符串""
     * @param uuid
     * @param vuid
     * @param checkCode
     * @param userKey
     * @param isPannoVideo
     * @return
     */
    public static Bundle setVodParams(String uuid, String vuid, String checkCode, String userKey, String playName, boolean isPannoVideo) {
        Bundle mBundle = new Bundle();
        mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_VOD);
        mBundle.putString(PlayProxy.PLAY_UUID, uuid);
        mBundle.putString(PlayProxy.PLAY_VUID, vuid);
        mBundle.putString(PlayProxy.PLAY_CHECK_CODE, checkCode);
        mBundle.putString(PlayProxy.PLAY_PLAYNAME, playName);
        mBundle.putString(PlayProxy.PLAY_USERKEY, userKey);
        mBundle.putBoolean(PlayProxy.BUNDLE_KEY_ISPANOVIDEO, isPannoVideo);
        return mBundle;
    }


//    // surfaceView生命周期
//    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            KLog.d();
//            stopAndRelease();
//            KLog.d("ok");
//        }
//
//        @Override
//        public void surfaceCreated(final SurfaceHolder holder) {
//                KLog.d();
//                createOnePlayer(holder.getSurface());
//                KLog.d("ok");
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            if (player != null) {
//                PlayerParamsHelper.setViewSizeChange(player, width, height);
//            }
//        }
//    };
//
//
//    public void init(Context mContext, Bundle mBundle, V4PlaySkin skin) {
//        this.mContext = mContext;
//        this.skin = skin;
//        this.mBundle = mBundle;
//
//        skinBuildType = V4PlaySkin.SKIN_TYPE_A;
//
//        creatVideoView();
//        createPlayContext();
//        createPlayerSkin();
//
//        PlayerAssistant.loadLastPosition(playContext, mBundle.getString(PlayProxy.PLAY_UUID),
//                mBundle.getString(PlayProxy.PLAY_VUID));
//        initDownload();
//
//
//    }
//
//    /**
//     * 初始化下载模块
//     */
//    private void initDownload() {
//        final String uuid = mBundle.getString(PlayProxy.PLAY_UUID);
//        final String vuid = mBundle.getString(PlayProxy.PLAY_VUID);
//        final DownloadCenter downloadCenter = DownloadCenter.getInstances(mContext);
//        if (downloadCenter != null && downloadCenter.isDownloadCompleted(vuid)) {
//            path = downloadCenter.getDownloadFilePath(vuid);
//        }
//        skin.setOnDownloadClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                downloadCenter.allowShowMsg(false);
//                downloadCenter.setDownloadRateText(playContext.getDefinationIdByType(uicontext
//                        .getCurrentRateType()));
//                downloadCenter.downloadVideo("", uuid, vuid);
//            }
//        });
//    }
//
//    /**
//     * 创建一个新的播放器
//     *
//     * @param surface
//     */
//    private void createOnePlayer(Surface surface) {
//        player = PlayerFactory.createOnePlayer(playContext, mBundle, null, surface);
//        if (!TextUtils.isEmpty(path)) {
//            playContext.setUsePlayerProxy(false);
//        }
//        player.setDataSource(path);
//        if (lastPosition > 0 && mBundle.getInt(PlayProxy.PLAY_MODE) == EventPlayProxy.PLAYER_VOD) {
//            player.seekTo(lastPosition);
//        }
//        player.prepareAsync();
//        /**
//         * 皮肤关联播放器
//         */
//        playerController.setPlayer(player);
//        skin.registerController(playerController);
//    }
//
//    //    播放器
//    private void creatVideoView ()
//    {
//        videoView  = new ReSurfaceView(mContext);
//        videoView.getHolder().addCallback(surfaceCallback);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        skin.addView(videoView.getMysef(), params);
//    }
////  创建播放上下文
//    private void createPlayContext ()
//    {
//        playContext = new PlayContext(mContext);
//        playContext.setVideoContainer(skin);
//        playContext.setVideoContentView(videoView.getMysef());
//        playerController = new LetvPlayerControllerImp();
//        playerController.setPlayContext(playContext);
//
//    }
////  创建播放皮肤
//    private void createPlayerSkin ()
//    {
//        uicontext = new UIPlayContext();
//        uicontext.isPanoVideo = false;
//        uicontext.setSkinBuildType(skinBuildType);
//        uicontext.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_PORTRAIT);
////        uicontext.setScreenResolution(UIPlayerControl.SCREEN_ORIENTATION_LANDSCAPE);
//        if(skin instanceof V4PlaySkin){
//            ((V4PlaySkin)skin).changeLayoutParams(16, 9);
//        }
//        skin.build(uicontext);
//    }
//
//
//    /**
//     * 停止播放，并且记录最后播放时间
//     */
//    private void stopAndRelease() {
//        if (player != null) {
//            KLog.d();
//            lastPosition = player.getCurrentPosition();
//            player.stop();
//            player.reset();
//            player.release();
//            player = null;
//            KLog.d("release ok!");
//        }
//    }
//
//
//
//
//    public void onResume() {
//        resume();
//    }
//
//    public void onPause() {
//        pause();
//    }
//
//    public void onDestroy() {
//        destroy();
//    }
//
//
//    private void resume() {
//        if (skin != null) {
//            skin.onResume();
//        }
//        if (player != null && playContext.getErrorCode() == -1) {
//            player.start();
//        }
//    }
//
//    private void pause() {
//        if (skinBuildType == V4PlaySkin.SKIN_TYPE_A && player != null && (int) player.getCurrentPosition() > 0) {
//            PlayerAssistant.saveLastPosition(mContext, mBundle.getString(PlayProxy.PLAY_UUID), mBundle.getString(PlayProxy.PLAY_VUID), (int) (player.getCurrentPosition()),
//                    playContext.getCurrentDefinationType());
//        }
//
//        if (skin != null) {
//            skin.onPause();
//        }
//        if (player != null) {
//            player.pause();
//        }
//    }
//
//    private void destroy() {
//        if (skin != null) {
//            skin.onDestroy();
//        }
//        if (playContext != null) {
//            playContext.destory();
//        }
//    }








































}
