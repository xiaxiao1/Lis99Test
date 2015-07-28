package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/27.
 */
public class ActiveAll extends BaseModel {

    public int totalpage;
    public int total;

    public ArrayList<Clubtopiclist> clubtopiclist;

    public class Clubtopiclist
    {
        public int id;
        public String title;
        public int cate_id;
        public String catename;
        public String times;
        public String is_image;
        public String image;
        public int club_id;
        public String club_title;
        public String club_img;
    }

}
