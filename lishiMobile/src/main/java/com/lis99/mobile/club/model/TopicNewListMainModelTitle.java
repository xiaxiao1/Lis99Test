package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yy on 16/3/8.
 */
public class TopicNewListMainModelTitle extends BaseModel {


    /**
     * topics_id : 1
     * topic_code : QPVFH3iOZw
     * user_id : 4976
     * nickname : 金剛Kongo
     * headicon : http://i3.lis99.com/upload/photo/d/d/c/dd593b8c3b9f3ae4d1942d2c4a0c879c.gif
     * club_id : 190
     * club_title : 在这里测试
     * title : 测试视频
     * browsenums : 100
     * createtime : 4天前
     * topicsdetaillist : [{"content":"测试内容","images":"http://i3.lis99
     * .com/upload/topicsimages/f/5/c/f57ac8556bd2fb0abcee661ee911459c.jpg","images_height":768,
     * "images_width":1024,"videoid":"0","videoimg":"","videoimg_height":0,"videoimg_width":0},
     * {"content":"","images":"","images_height":0,"images_width":0,"videoid":"2710826954",
     * "videoimg":"http://i3.lis99
     * .com/upload/topicsvideoimg/4/8/9/48725f98259f5f44c9bd602b5c008849.jpg",
     * "videoimg_height":768,"videoimg_width":1024}]
     * totpage : 1
     * topictot : 3
     * topicsreplylist : [{"id":"1","user_id":"3456","nickname":"痞子哦","headicon":"http://i3.lis99
     * .com/upload/photo/b/7/2/b705527f29bfac7bd126e5f19c450c02.jpg","reply_id":"0",
     * "reply_content":"","reply_floor":0,"reply_nickname":"未知","floor":"2","content":"测试信息",
     * "images":null,"height":0,"width":0,"createtime":"2016-03-08 10:52:02"},{"id":"2",
     * "user_id":"4976","nickname":"金剛Kongo","headicon":"http://i3.lis99
     * .com/upload/photo/d/d/c/dd593b8c3b9f3ae4d1942d2c4a0c879c.gif","reply_id":"1",
     * "reply_content":"测试信息","reply_floor":"2","reply_nickname":"痞子哦","floor":"3",
     * "content":"测试信息","images":null,"height":0,"width":0,"createtime":"2016-03-08 10:51:57"},
     * {"id":"3","user_id":"1","nickname":"Leslee","headicon":"http://i3.lis99
     * .com/upload/photo/7/e/2/7e50c0cf2f4584c9a117a0eda7516012.jpg","reply_id":"0",
     * "reply_content":"","reply_floor":0,"reply_nickname":"未知","floor":"4","content":"成功",
     * "images":null,"height":0,"width":0,"createtime":"2016-03-08 10:52:10"}]
     */

    @SerializedName("topics_id")
    public String topicsId;
    @SerializedName("topic_code")
    public String topicCode;
    @SerializedName("user_id")
    public int userId;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("headicon")
    public String headicon;
    @SerializedName("club_id")
    public int clubId;
    @SerializedName("club_title")
    public String clubTitle;
    @SerializedName("title")
    public String title;
    @SerializedName("browsenums")
    public int browsenums;
    @SerializedName("createtime")
    public String createtime;
    @SerializedName("totpage")
    public int totpage;
    @SerializedName("topictot")
    public int topictot;
    @SerializedName("attenStatus")
    public int attenStatus;
    @SerializedName("moderator")
    public int moderator;
    @SerializedName("shareurl")
    public String shareurl;
    @SerializedName("sharetxt")
    public String sharetxt;

    @SerializedName("usercatelist")
    public ArrayList<Usercatelist> usercatelist;

    public static class Usercatelist
    {
        @SerializedName("title")
        public String title;
    }

}
