package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/7.
 */
public class SearchInfoTopicModel extends BaseModel{


    public int totpage;

    public int clubtopictot;

    public ArrayList<Clubtopiclist> clubtopiclist;

    public class Clubtopiclist
    {
        public int id;
        public String title;
        public String createdate;
        public int club_id;
        public String clubname;
        public int user_id;
        public String nickname;
    }





}
