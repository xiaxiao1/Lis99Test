package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yy on 16/7/11.
 */
public class ActiveMainHeadModel extends BaseModel {


    /**
     * id : 1
     * title : 砾石推荐
     * actlist : [{"topic_id":4205,"topic_title":"杏树山头伏漂流活动召集","activity_code":"kzkJ4zkZ",
     * "harddesc":"黑龙江·杏树山","price":"70.00","images":"http://i3.lis99
     * .com/upload/club/f/2/8/f28ed555d36f3be45728d3bfd3b93678.jpg","width":1024,"height":683},
     * {"topic_id":4204,"topic_title":"#碧水蓝天止锚湾，看日出免费钓螃蟹，七彩烟花照海滩，绥中海滩两日休闲之旅",
     * "activity_code":"ywOY2zkZ","harddesc":"","price":"350.00","images":"http://i3.lis99
     * .com/upload/club/2016/07/05/1467698803_AEQaCS4B.jpg","width":600,"height":397},
     * {"topic_id":4203,"topic_title":"夏日Fiesta 清凉一夏，畅游井空里激情仙龙峡","activity_code":"hCrh2zkZ",
     * "harddesc":"浙江·井空里-仙龙峡","price":"320.00","images":"http://i3.lis99
     * .com/upload/club/5/c/e/5c918f0adf2a3a551acd807883968a1e.jpg","width":720,"height":480}]
     */

    @SerializedName("hotlist")
    public List<HotlistEntity> hotlist;
    /**
     * user_id : 4976
     * nickname : 砾石技术部
     * tag : 傲娇
     * description : 西木野真姬 音乃木坂学院一年级生。
     * headicon : http://i3.lis99
     * .com/upload/recommendleader/4/6/e/4636cfaddb1d65538f8ea0b7c8b5611e.jpg
     * width : 360
     * height : 280
     */

    @SerializedName("leaderlist")
    public List<LeaderlistEntity> leaderlist;

    public static class HotlistEntity {
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        /**
         * topic_id : 4205
         * topic_title : 杏树山头伏漂流活动召集
         * activity_code : kzkJ4zkZ
         * harddesc : 黑龙江·杏树山
         * price : 70.00
         * images : http://i3.lis99.com/upload/club/f/2/8/f28ed555d36f3be45728d3bfd3b93678.jpg
         * width : 1024
         * height : 683
         */

        @SerializedName("actlist")
        public List<ActlistEntity> actlist;

        public static class ActlistEntity {
            @SerializedName("topic_id")
            public int topicId;
            @SerializedName("topic_title")
            public String topicTitle;
            @SerializedName("activity_code")
            public String activityCode;
            @SerializedName("harddesc")
            public String harddesc;
            @SerializedName("cate_name")
            public String cate_name;
            @SerializedName("price")
            public String price;
            @SerializedName("images")
            public String images;
            @SerializedName("width")
            public int width;
            @SerializedName("height")
            public int height;
        }
    }

    public static class LeaderlistEntity {
        @SerializedName("user_id")
        public int userId;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("tag")
        public String tag;
        @SerializedName("description")
        public String description;
        @SerializedName("headicon")
        public String headicon;
        @SerializedName("width")
        public int width;
        @SerializedName("height")
        public int height;
    }
}
