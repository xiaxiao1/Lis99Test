package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/***
 *
 * @author yy
 */
public class ClubTopicNewReplyList extends BaseModel {


    /**
     * totpage : 1
     * topictot : 7
     * topicsreplylist : [{"id":"2","user_id":"4976","is_floor":1,"nickname":"金剛Kongo",
     * "headicon":"http://i3.lis99.com/upload/photo/d/d/c/dd593b8c3b9f3ae4d1942d2c4a0c879c.gif",
     * "moderator":0,"is_vip":1,"usercatelist":[{"title":"装备点评团"},{"title":"撰稿人"},
     * {"title":"Orz\"么\"么"}],"reply_id":null,"reply_content":"","reply_floor":0,
     * "reply_nickname":"未知","floor":null,"content":null,"images":null,"height":0,"width":0,
     * "createtime":null}]
     */

    @SerializedName("totpage")
    public int totpage;
    @SerializedName("topictot")
    public int topictot;
    /**
     * id : 2
     * user_id : 4976
     * is_floor : 1
     * nickname : 金剛Kongo
     * headicon : http://i3.lis99.com/upload/photo/d/d/c/dd593b8c3b9f3ae4d1942d2c4a0c879c.gif
     * moderator : 0
     * is_vip : 1
     * usercatelist : [{"title":"装备点评团"},{"title":"撰稿人"},{"title":"Orz\"么\"么"}]
     * reply_id : null
     * reply_content :
     * reply_floor : 0
     * reply_nickname : 未知
     * floor : null
     * content : null
     * images : null
     * height : 0
     * width : 0
     * createtime : null
     */

    @SerializedName("topicsreplylist")
    public List<TopicsreplylistEntity> topicsreplylist;

    public static class TopicsreplylistEntity {
        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("is_floor")
        public int isFloor;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("headicon")
        public String headicon;
        @SerializedName("moderator")
        public int moderator;
        @SerializedName("is_vip")
        public int isVip;
        @SerializedName("reply_id")
        public int replyId;
        @SerializedName("reply_content")
        public String replyContent;
        @SerializedName("reply_floor")
        public int replyFloor;
        @SerializedName("reply_nickname")
        public String replyNickname;
        @SerializedName("floor")
        public int floor;
        @SerializedName("content")
        public String content;
        @SerializedName("images")
        public String images;
        @SerializedName("height")
        public int height;
        @SerializedName("width")
        public int width;
        @SerializedName("createtime")
        public String createtime;
        /**
         * title : 装备点评团
         */

        @SerializedName("usercatelist")
        public List<UsercatelistEntity> usercatelist;

        public static class UsercatelistEntity {
            @SerializedName("title")
            public String title;
        }
    }
}
