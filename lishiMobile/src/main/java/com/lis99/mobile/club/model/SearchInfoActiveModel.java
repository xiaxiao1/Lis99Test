package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/7.
 */
public class SearchInfoActiveModel extends BaseModel {

    public int totpage;
    public int clubtopictot;

    public ArrayList<Clubtopiclist> clubtopiclist;


    public class Clubtopiclist
    {
        public int id;
        public String title;
        public String dtime;
        public String image;
        public int height;
        public int width;


    }




}
