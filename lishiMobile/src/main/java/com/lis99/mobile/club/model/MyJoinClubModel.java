package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/10/23.
 */
public class MyJoinClubModel extends BaseModel {

    public int totalpage;

    public ArrayList<Clublist> clublist;

    public class Clublist
    {
        public int id;
        public String title;
        public String image;
        public String clubtopic;
        public String content;
        public int joinNum;
    }

}
