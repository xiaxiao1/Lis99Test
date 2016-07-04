package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChoicenessBannerModel extends BaseModel {


    /**
     * version : 350
     * channel : 12
     * image : http://i3.lis99.com/upload/ad/4/5/6/45ef86d25d390af1bad5aa8a81fc5ba6.jpg
     * height : 470
     * width : 750
     * type : 3
     * link : http://www.ispo.com.cn/page/dccJ2EO/shanghai.html
     * id :
     */

    @SerializedName("adlist")
    public ArrayList<AdlistEntity> adlist;
    /**
     * topic_id : 3878
     * category : 4
     * title : 8月去最美的新疆赏最美的景
     * column_id : 5
     * column_name : 你们真会玩
     * user_id : 317330
     * nickname : 威威109597
     * headicon : http://i3.lis99.com/upload/photo/1/2/35e8e9cda026f3f588dac2348ca9c8622.jpg
     * images : http://i3.lis99.com/upload/column/a/e/5/ae664d6daa0a05755d597dcbf015b285.png
     * width : 752
     * height : 669
     */

    @SerializedName("daylist")
    public List<DaylistEntity> daylist;
    /**
     * user_id : 4976
     * nickname : 半夏微凉yuan
     * headicon : http://i3.lis99.com/upload/photo/c/2/7/c242850c3eb3d500afb30e95f7b421b7.jpg
     * short_desc : 变态狂
     */

    @SerializedName("startlist")
    public ArrayList<StartlistEntity> startlist;
    /**
     * title : 测试信息
     * hlist : [{"images":"http://i3.lis99.com/upload/club/f/e/a/fe9dffecc3d5aa504d216c2e7aed4e5a
     * .png","width":900,"height":500,"id":1581601,"category":2,"title":"每日一聊｜你最喜欢的户外装备是什么？",
     * "nickname":"砾石小助手","headicon":"http://i3.lis99
     * .com/upload/photo/5/1/5/51c391a71fc5af2a6e87aadb00369745.jpg"},{"images":"http://i3.lis99
     * .com/upload/club/4/1/4/4192a3d1d340f44ee54076911c7b1744.jpg","width":3264,"height":2448,
     * "id":3863,"category":4,"title":"高山牧场朝天背-峡砬子-大坪坨","nickname":"无盐","headicon":"http://i3
     * .lis99.com/upload/photo/7/f/0/7fa8283650d4498d4a1414bce47c7110.jpg"}]
     */

    @SerializedName("hotlist")
    public ArrayList<HotlistEntity> hotlist;

    public static class AdlistEntity {
        @SerializedName("version")
        public int version;
        @SerializedName("channel")
        public int channel;
        @SerializedName("image")
        public String image;
        @SerializedName("height")
        public int height;
        @SerializedName("width")
        public int width;
        @SerializedName("type")
        public int type;
        @SerializedName("link")
        public String link;
        @SerializedName("id")
        public String id;
    }

    public static class DaylistEntity {
        @SerializedName("topic_id")
        public int topicId;
        @SerializedName("category")
        public int category;
        @SerializedName("title")
        public String title;
        @SerializedName("column_id")
        public int columnId;
        @SerializedName("column_name")
        public String columnName;
        @SerializedName("user_id")
        public int userId;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("headicon")
        public String headicon;
        @SerializedName("images")
        public String images;
        @SerializedName("width")
        public int width;
        @SerializedName("height")
        public int height;
    }

    public static class StartlistEntity {
        @SerializedName("user_id")
        public int userId;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("headicon")
        public String headicon;
        @SerializedName("short_desc")
        public String shortDesc;
    }

    public static class HotlistEntity {
        @SerializedName("title")
        public String title;
        /**
         * images : http://i3.lis99.com/upload/club/f/e/a/fe9dffecc3d5aa504d216c2e7aed4e5a.png
         * width : 900
         * height : 500
         * id : 1581601
         * category : 2
         * title : 每日一聊｜你最喜欢的户外装备是什么？
         * nickname : 砾石小助手
         * headicon : http://i3.lis99.com/upload/photo/5/1/5/51c391a71fc5af2a6e87aadb00369745.jpg
         */

        @SerializedName("hlist")
        public ArrayList<HlistEntity> hlist;

        public static class HlistEntity {
            @SerializedName("images")
            public String images;
            @SerializedName("width")
            public int width;
            @SerializedName("height")
            public int height;
            @SerializedName("id")
            public int id;
            @SerializedName("category")
            public int category;
            @SerializedName("title")
            public String title;
            @SerializedName("nickname")
            public String nickname;
            @SerializedName("headicon")
            public String headicon;
        }
    }
}
