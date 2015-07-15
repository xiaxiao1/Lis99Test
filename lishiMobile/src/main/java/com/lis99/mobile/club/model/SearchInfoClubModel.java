package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/7.
 */
public class SearchInfoClubModel extends BaseModel{

    public int totpage;
    public int clubtot;

    public ArrayList<Clublist> clublist;

    public class Clublist
    {
        public int id;
        public String title;
        public String image;
        public int height;
        public int width;
        public int cate_id;
        public String catename;
        public int city;
        public String cityname;
    }

}
