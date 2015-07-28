package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/27.
 */
public class ActiveAllCity extends BaseModel {

    public ArrayList<Hotcity> hotcity;

    public ArrayList<Citylist> citylist;

    public class Citylist
    {
        public int id;
        public String name;
        public String pinyin;
    }

    public class Hotcity
    {
        public int id;
        public String name;
    }

}
