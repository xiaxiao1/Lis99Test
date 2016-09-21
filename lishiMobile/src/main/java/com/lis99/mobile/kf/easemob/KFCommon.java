package com.lis99.mobile.kf.easemob;

import android.app.Activity;
import android.content.Intent;

import com.easemob.chat.EMMessage;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.kf.easemob.helpdesk.domain.OrderMessageEntity;
import com.lis99.mobile.kf.easemob.helpdesk.ui.LoginActivity;
import com.lis99.mobile.util.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yy on 16/9/18.
 *
 *          关联名称        砾石
            AppKey      lis99#lis99
            ClientId    YXA6UdUVYHMXEeaZ_L0u0cjtiw
            ClientSecret       YXA6IKCzidLpOi-tbaUme7ne22WF4tk
            IM服务号  lis99
            IM Password
            租户ID            26980
            ProjectId:      209742

 *
 */
public class KFCommon {

    public static String APPKEY = "lis99#lis99";

    public static String clientId = "YXA6UdUVYHMXEeaZ_L0u0cjtiw";

    public static String clientSecret = "YXA6IKCzidLpOi-tbaUme7ne22WF4tk";

    public static String imCode = "ceshi";

    public static long TenantId = 26980;

    public static long ProjectId = 209742;

    private static int RESULT = 987;
    public static final String ENTITY = "SEND_ENTITY";

    public static void goKFActivity (Activity a, OrderMessageEntity entity )
    {
        Intent intent = new Intent();
//        intent.putExtra(Constant.INTENT_CODE_IMG_SELECTED_KEY, 1);
        intent.putExtra(ENTITY, entity);
        intent.setClass(a, LoginActivity.class);
        a.startActivity(intent);
    }

    public static void goKFActivityOnResult (Activity a, OrderMessageEntity entity )
    {
        Intent intent = new Intent();
//        intent.putExtra(Constant.INTENT_CODE_IMG_SELECTED_KEY, 1);
        intent.putExtra(ENTITY, entity);
        intent.setClass(a, LoginActivity.class);
        a.startActivityForResult(intent,RESULT);
    }

    /**
     * 设置用户的属性，
     * 通过消息的扩展，传递客服系统用户的属性信息
     * @param message
     */
    public static EMMessage setUserInfoAttribute(EMMessage message) {

        JSONObject weichatJson = getWeichatJSONObject(message);
        String currentUserNick = DataManager.getInstance().getUser().getNickname();
        try {
            JSONObject visitorJson = new JSONObject();
            visitorJson.put("userNickname", currentUserNick);
            visitorJson.put("trueName", currentUserNick);
            visitorJson.put("qq", "1");
            visitorJson.put("phone", DataManager.getInstance().getUser().getMobile());
            visitorJson.put("companyName", "Android");
            visitorJson.put("description", Common.getUserId());
            visitorJson.put("email", DataManager.getInstance().getUser().getEmail());
            weichatJson.put("visitor", visitorJson);

            message.setAttribute("weichat", weichatJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 获取消息中的扩展 weichat是否存在并返回jsonObject
     * @param message
     * @return
     */
    private static JSONObject getWeichatJSONObject(EMMessage message){
        JSONObject weichatJson = null;
        try {
            String weichatString = message.getStringAttribute("weichat", null);
            if(weichatString == null){
                weichatJson = new JSONObject();
            }else{
                weichatJson = new JSONObject(weichatString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weichatJson;
    }

    /**
     *          商品信息
     * @param titleOrder    后台看到的标题+userId
     * @param imgUrl        帖子大图
     * @param topicId       帖子Id
     * @param title         前台看到的标题
     * @param topicUrl      帖子连接地址
     * @return
     */
    public static OrderMessageEntity getMessageExtFromPicture(String titleOrder, String imgUrl, String topicId, String title, String topicUrl) {
//        OrderMessageEntity entity1 = new OrderMessageEntity(1, "test_order1", "订单号：7890", "￥128",
//                "2015早春新款高腰复古牛仔裙", "https://www.baidu.com/img/bdlogo.png", "http://www.baidu.com");
        OrderMessageEntity entity1 = new OrderMessageEntity(1, titleOrder, "帖子编号："+topicId, "",
                title, imgUrl, topicUrl);
        return entity1;
    }


}
