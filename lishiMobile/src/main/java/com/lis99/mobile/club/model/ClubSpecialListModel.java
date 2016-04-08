package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/6.
 */
public class ClubSpecialListModel extends BaseModel {

    public Taginfo taginfo;

    public int totalpage;

    public int total;

    public ArrayList<Topiclist> topiclist;


    public class Topiclist
    {
        public String activity_code;
        public int topic_id;
        public String topic_title;
        public int replytotal;
        public String category;
        public String nickname;
        public int user_id;
        public String headicon;
        public int is_hot;
        public String topic_type;

        public ArrayList<Topicimagelist> topicimagelist;
    }

    public class Topicimagelist
    {
        public String images;
    }

    public class Taginfo
    {
        public int id;
        public String name;
        public String tag_images;
    }

}
