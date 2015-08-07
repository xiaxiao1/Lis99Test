package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/6.
 */
public class ClubHotTopicModel extends BaseModel {

    public int totalpage;
    public int total;

    public ArrayList<Topicslist> topicslist;

    public class Topicslist
    {
        public int topic_id;
        public String topic_title;
        public String category;
        public int replytotal;
        public String headicon;
        public String nickname;
        public String user_id;
        public String club_title;
        public String club_id;
        public int is_hot;
        public ArrayList<Topicimagelist> topicimagelist;
    }

    public class Topicimagelist
    {
        public String images;
    }

}
