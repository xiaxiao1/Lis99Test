package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yy on 16/6/29.
 *      专栏详情
 */
public class SpecialInfoModel extends BaseModel {


    /**
     * id : 1
     * name : 姑娘去哪儿
     * tag_images : http://i3.lis99.com/upload/tag/e/2/e/e2c28a8e4c5e7c6fdcb9b947e6a01fbe.jpg!middle
     * tag_height : 400
     * tag_width : 750
     */

    @SerializedName("taginfo")
    public TaginfoEntity taginfo;
    /**
     * taginfo : {"id":1,"name":"姑娘去哪儿","tag_images":"http://i3.lis99
     * .com/upload/tag/e/2/e/e2c28a8e4c5e7c6fdcb9b947e6a01fbe.jpg!middle","tag_height":400,
     * "tag_width":750}
     * totalpage : 19
     * total : 190
     * topiclist : [{"topic_id":1570893,"activity_code":"","topic_title":"在路上_颠沛流离在稻城亚丁",
     * "replytotal":0,"likenum":64,"content":"一直向往的川藏线，终于踏上了你的土地，亚丁真的是好美","images":"http://i3
     * .lis99.com/upload/club/8/5/2/85e814cd41d17048ef15adf18c6787a2.jpg!middle","image_width":0,
     * "image_height":0,"category":0,"headicon":"http://i3.lis99
     * .com/upload/photo/d/2/3/d20c7cfa940eb0a1e2a8aa8f8be1fbe3.jpg","nickname":"lovesnow",
     * "user_id":432315},{"topic_id":1569947,"activity_code":"","topic_title":"在路上",
     * "replytotal":0,"likenum":44,"content":"西藏是一直让我魂牵梦绕的地方\n米堆冰川","images":"http://i3.lis99
     * .com/upload/club/f/e/5/feab80db3680bd25bfc37c34ebed4ee5.jpg!middle","image_width":0,
     * "image_height":0,"category":0,"headicon":"http://i3.lis99
     * .com/upload/photo/d/2/3/d20c7cfa940eb0a1e2a8aa8f8be1fbe3.jpg","nickname":"lovesnow",
     * "user_id":432315}]
     */

    @SerializedName("totalpage")
    public int totalpage;
    @SerializedName("total")
    public int total;
    /**
     * topic_id : 1570893
     * activity_code :
     * topic_title : 在路上_颠沛流离在稻城亚丁
     * replytotal : 0
     * likenum : 64
     * content : 一直向往的川藏线，终于踏上了你的土地，亚丁真的是好美
     * images : http://i3.lis99.com/upload/club/8/5/2/85e814cd41d17048ef15adf18c6787a2.jpg!middle
     * image_width : 0
     * image_height : 0
     * category : 0
     * headicon : http://i3.lis99.com/upload/photo/d/2/3/d20c7cfa940eb0a1e2a8aa8f8be1fbe3.jpg
     * nickname : lovesnow
     * user_id : 432315
     */

    @SerializedName("topiclist")
    public ArrayList<TopiclistEntity> topiclist;

    public static class TaginfoEntity {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("tag_images")
        public String tagImages;
        @SerializedName("tag_height")
        public int tagHeight;
        @SerializedName("tag_width")
        public int tagWidth;
    }

    public static class TopiclistEntity {
        @SerializedName("topic_id")
        public int topicId;
        @SerializedName("activity_code")
        public String activityCode;
        @SerializedName("topic_title")
        public String topicTitle;
        @SerializedName("replytotal")
        public int replytotal;
        @SerializedName("likenum")
        public int likenum;
        @SerializedName("content")
        public String content;
        @SerializedName("images")
        public String images;
        @SerializedName("image_width")
        public int imageWidth;
        @SerializedName("image_height")
        public int imageHeight;
        @SerializedName("category")
        public int category;
        @SerializedName("headicon")
        public String headicon;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("user_id")
        public int userId;
    }
}
