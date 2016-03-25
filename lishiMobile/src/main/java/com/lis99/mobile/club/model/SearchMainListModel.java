package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

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
        @SerializedName("activity_code")
        public String activity_code;
        /**
         *      帖子类型：0旧版话题贴，1旧版线路活动帖, 2旧版线上活动，3新版话题贴，4新版活动贴
         */
        @SerializedName("category")
        public int category;

    }

    public class Huodonglist
    {
        public int id;
        public String title;
        public String dtime;
        public String image;
        public String height;
        public String width;
        //      新版活动
        public String activity_code;
        /**
         *
         */
        @SerializedName("category")
        public int category;
    }

}
