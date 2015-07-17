package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/7.
 */
public class SearchMainListModel extends BaseModel{


    public ArrayList<Clublist> clublist;

    public ArrayList<Huatilist> huatilist;

    public ArrayList<Huodonglist> huodonglist;


    public int clubtot;

    public int huatitot;

    public int huodongtot;


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
    public class Huatilist
    {
        public int id;
        public String title;
        public String createdate;
        public int club_id;
        public String clubname;
        public int user_id;
        public String nickname;
    }

    public class Huodonglist
    {
        public int id;
        public String title;
        public String dtime;
        public String image;
        public String height;
        public String width;
    }

}
