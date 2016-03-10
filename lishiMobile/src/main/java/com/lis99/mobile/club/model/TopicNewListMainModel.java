package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yy on 16/3/8.
 */
public class TopicNewListMainModel extends TopicNewListMainModelEquip {


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
    }
}
