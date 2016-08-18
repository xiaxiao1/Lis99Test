package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 16/3/8.
 */
public class TopicNewListMainModel extends TopicNewListMainModelEquip implements ShareInterface {

   /* public int is_tagid;
    public String reason;

    public int getIs_tagid() {
        return is_tagid;
    }

    public void setIs_tagid(int is_tagid) {
        this.is_tagid = is_tagid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }*/

    /**
     * content : 测试内容
     * images : http://i3.lis99.com/upload/topicsimages/f/5/c/f57ac8556bd2fb0abcee661ee911459c.jpg
     * images_height : 768
     * images_width : 1024
     * videoid : 0
     * videoimg :
     * videoimg_height : 0
     * videoimg_width : 0
     */

    @SerializedName("topicsdetaillist")
    public List<TopicsdetaillistEntity> topicsdetaillist;

    //    1创始人，2管理员，4成员,8网站编辑，-1无权限信息
    @SerializedName("is_jion")
    public int is_jion;
    /**
     * id : 1
     * user_id : 3456
     * nickname : 痞子哦
     * headicon : http://i3.lis99.com/upload/photo/b/7/2/b705527f29bfac7bd126e5f19c450c02.jpg
     * reply_id : 0
     * reply_content :
     * reply_floor : 0
     * reply_nickname : 未知
     * floor : 2
     * content : 测试信息
     * images : null
     * height : 0
     * width : 0
     * createtime : 2016-03-08 10:52:02
     */

    @SerializedName("topicsreplylist")
    public List<TopicsreplylistEntity> topicsreplylist;

    public static class TopicsdetaillistEntity {
        @SerializedName("content")
        public String content;
        @SerializedName("images")
        public String images;
        @SerializedName("images_height")
        public int imagesHeight;
        @SerializedName("images_width")
        public int imagesWidth;
        @SerializedName("videoid")
        public String videoid;
        @SerializedName("videoimg")
        public String videoimg;
        @SerializedName("videoimg_height")
        public int videoimgHeight;
        @SerializedName("videoimg_width")
        public int videoimgWidth;


    }

    public static class TopicsreplylistEntity {

        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("headicon")
        public String headicon;
        @SerializedName("reply_id")
        public String replyId;
        @SerializedName("reply_content")
        public String replyContent;
        @SerializedName("reply_floor")
        public int replyFloor;
        @SerializedName("reply_nickname")
        public String replyNickname;
        @SerializedName("floor")
        public String floor;
        @SerializedName("content")
        public String content;
        @SerializedName("images")
        public Object images;
        @SerializedName("height")
        public int height;
        @SerializedName("width")
        public int width;
        @SerializedName("createtime")
        public String createtime;
        /**
         *      是否楼主：0否，1是
         */
        @SerializedName("is_floor")
        public int is_floor;
        /**
         *      否是版主：0否，1是
         */
        @SerializedName("moderator")
        public int moderator;
        @SerializedName("usercatelist")
        public ArrayList<Usercatelist> usercatelist;

        public static class Usercatelist
        {
            @SerializedName("title")
            public String title;
        }


    }


    /**
     * 是否是置顶 2置顶， 其他 解除置顶
     */
    @Override
    public String getStick() {
        return null;
    }

    /**
     * 是否是置顶 2置顶， 其他 解除置顶
     *
     * @param s
     */
    @Override
    public void setStick(String s) {

    }

    /**
     * 是否显示报名管理， 1线下活动， 2 线上活动， 显示管理， 其他不显示
     */
    @Override
    public String getCategory() {
        return "999";
    }

    /**
     * 创始人标记， 是否显示删除 1 创始人（显示删除）
     */
    @Override
    public String getIsJoin() {
        return ""+is_jion;
    }

    /**
     * 是否为新版的线下活动贴, null 否， 不为空 是新版活动帖
     *
     * @return
     */
    @Override
    public String getNewActive() {
        return null;
    }

    /**
     * 俱乐部Id
     *
     * @return
     */
    @Override
    public String getClubId() {
        return ""+clubId;
    }

    /**
     * 帖子Id
     *
     * @return
     */
    @Override
    public String getTopicId() {
        return topicsId;
    }
    public String imgShareUrl;
    /**
     * 图片Url 地址
     *
     * @return
     */
    @Override
    public String getImageUrl() {
        return imgShareUrl;
    }

    /**
     * 分享显示标题
     *
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * 分享内容
     *
     * @return
     */
    @Override
    public String getShareTxt() {
        return sharetxt;
    }

    /**
     * 分享连接地址
     *
     * @return
     */
    @Override
    public String getShareUrl() {
        return shareurl;
    }

    /**
     * 帖子类型
     *
     * @return 0, 其他， 1， 线上
     */
    @Override
    public int getType() {
        return 0;
    }


}
