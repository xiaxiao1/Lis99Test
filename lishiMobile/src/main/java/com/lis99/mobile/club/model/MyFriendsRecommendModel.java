package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/17.
 */
public class MyFriendsRecommendModel extends BaseModel {

    public int totPage;
    public int totRecommend;

    public ArrayList<Lists> lists;

    public class Lists
    {
        public int is_follow;
        public int follow_time;
        public int weight;
        public String headicon;
        public int user_id;
        public String nickname;
        public int is_vip;
        public String topic_title;
    }

}
