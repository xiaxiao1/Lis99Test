package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yy on 16/6/29.
 */
public class CommunityInfoModel extends BaseModel {


    /**
     * totalpage : 1
     * total : 3
     * lists : [{"user_id":4976,"nickname":"砾石技术部","headicon":"http://i3.lis99
     * .com/upload/photo/9/3/5/93438582d3b5f5a369c9482cb6cc4b65.jpg","note":"Hi
     * 这里是砾石技术部账号，因为测试的关系，可能会临时加为您的俱乐部领队。","tottopics":6,"totfans":9,
     * "usercatelist":[{"title":"砾石技术部"}]},{"user_id":276594,"nickname":"Jeremy",
     * "headicon":"http://i3.lis99.com/upload/photo/9/8/5/98cb3ac3f440165f332ae12cf872eff5.gif",
     * "note":"户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n
     * 户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n
     * 户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n
     * 户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n
     * 户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n
     * 户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n户外撕逼小能手！户外撕逼小能手！\n
     * 户外撕逼小能手！户外撕逼小能手！","tottopics":1,"totfans":35,"usercatelist":[]},{"user_id":3456,
     * "nickname":"痞子哦","headicon":"http://i3.lis99
     * .com/upload/photo/b/7/2/b705527f29bfac7bd126e5f19c450c02.jpg","note":"测试吧","tottopics":0,
     * "totfans":25,"usercatelist":[]}]
     */

    @SerializedName("totalpage")
    public int totalpage;
    @SerializedName("total")
    public int total;
    /**
     * user_id : 4976
     * nickname : 砾石技术部
     * headicon : http://i3.lis99.com/upload/photo/9/3/5/93438582d3b5f5a369c9482cb6cc4b65.jpg
     * note : Hi 这里是砾石技术部账号，因为测试的关系，可能会临时加为您的俱乐部领队。
     * tottopics : 6
     * totfans : 9
     * usercatelist : [{"title":"砾石技术部"}]
     */

    @SerializedName("lists")
    public List<ListsEntity> lists;

    public static class ListsEntity {
        @SerializedName("user_id")
        public int userId;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("headicon")
        public String headicon;
        @SerializedName("note")
        public String note;
        @SerializedName("tottopics")
        public int tottopics;
        @SerializedName("totfans")
        public int totfans;
        @SerializedName("is_follow")
        public int is_follow;

        /**
         * title : 砾石技术部
         */
        @SerializedName("usercatelist")
        public List<UsercatelistEntity> usercatelist;

        public static class UsercatelistEntity {
            @SerializedName("title")
            public String title;
        }
    }
}
