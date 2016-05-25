package com.lis99.mobile.util;

import android.text.TextUtils;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.model.BaseModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

import java.util.HashMap;

/**
 * Created by yy on 15/8/31.
 *      砾石积分系统
 */
public class LSScoreManager {

    private static LSScoreManager instance;

//    private static final String URL = C.DOMAIN + "/v3/user/incrUserPoints";
//    /v3/user/newIncrUserPoints
private static final String URL = C.DOMAIN + "/v3/user/newIncrUserPoints";

    private static String /*action,*/ user_id, version, platform, channel, topicid;

    private CallBack callBack;
    //注册信息
    public static final String register = "register";
    //签到
    public static final String sign = "sign";
    //发话题贴
    public static final String pubtalktopic = "pubtalktopic";
    //发活动贴（没有）
    public static final String pubactivetopic = "pubactivetopic";
    //回帖带图
    public static final String replytopicbyimg = "replytopicbyimg";
    //回帖不带图
    public static final String replytopicbynoimg = "replytopicbynoimg";
    //报名被通过
    public static final String enroll = "enroll";
    //分享到微信朋友圈
    public static final String sharewechatfriendscircle = "sharewechatfriendscircle";
    //分享到微信好友
    public static final String sharewechatfriends = "sharewechatfriends";
    //分享微博
    public static final String shareweibo = "shareweibo";
    //分享QQ空间
    public static final String shareqzone = "shareqzone";
    //所发话题贴被删
    public static final String delpubtalktopic = "delpubtalktopic";
    //所发活动贴被删
    public static final String delpubactivetopic = "delpubactivetopic";
    //带图回帖被删
    public static final String delreplytopicbyimg = "delreplytopicbyimg";
    //无图回帖被删
    public static final String delreplytopicbynoimg = "delreplytopicbynoimg";
    //活动贴被删报名获得的积分需要扣除(取消)
    public static final String delactivetopicenrollpoints = "delactivetopicenrollpoints";
    //新版话题贴回复
    public static final String replytopicsnoimg = "replytopicsnoimg";
    //新版话题贴发布
    public static final String pubtopics = "pubtopics";


    public static LSScoreManager getInstance ()
    {
        if ( instance == null )
        {
            instance = new LSScoreManager();
            version = ""+DeviceInfo.CLIENTVERSIONCODE;
            platform = "Android";
            channel = DeviceInfo.CHANNELVERSION;
        }
        return instance;
    }

    public void setCallBack ( CallBack callBack )
    {
        this.callBack = callBack;
    }


//判断是否有登录
    private static boolean getUserId ()
    {
        user_id = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(user_id))
        {
            return false;
        }
        return true;
    }
//action, user_id, version, platform, channel;
    private void setMap ( HashMap<String, Object> map, String action )
    {
        map.put("action", action);
        map.put("user_id", user_id);
        map.put("version", version);
        map.put("platform", platform);
        map.put("channel", channel);
        map.put("topicid", topicid);
    }

    public void sendScore ( String action, String topicid )
    {
        if ( !getUserId() )
        {
            return;
        }
        this.topicid = topicid;
        BaseModel model = new BaseModel();
        HashMap<String, Object> map = new HashMap<String, Object>();

        setMap(map, action);

        MyRequestManager.getInstance().requestPostNoDialog(URL, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                if ( callBack == null ) return;
                callBack.handler(mTask);
                callBack = null;
            }
        });
    }

    public void sendScore ( String UserId, String action, String topicid )
    {
        user_id = UserId;
        this.topicid = topicid;
        BaseModel model = new BaseModel();
        HashMap<String, Object> map = new HashMap<String, Object>();

        setMap(map, action);

        MyRequestManager.getInstance().requestPostNoDialog(URL, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                if ( callBack == null ) return;
                callBack.handler(mTask);
                callBack = null;
            }
        });
    }

}
