package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/27.
 */
public class ActiveAllModel extends BaseModel {

    public int totalpage;
    public int total;

    public ArrayList<Clubtopiclist> activitylist;

    public class Clubtopiclist
    {
        public int id;
        public String title;
        public String times;
        public String images;
    }

}
