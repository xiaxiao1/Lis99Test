package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/17.
 */
public class DynamicListModel extends BaseModel {

    public int totalpage;
    public int total;
    //是否有关注， 0 是没有， 1是有
    public int nofans;

    public ArrayList<Topicslist> topicslist;

    public class Topicslist
    {
        public int topic_id;
        public String topic_title;
        public String createtime;
        public int category;
        public int replytot;
        public String headicon;
        public String nickname;
        public int user_id;
        public int is_vip;
        public String club_title;
        public int club_id;
        public String catename;
        public String image;
        public String replycontent;
    }

}
