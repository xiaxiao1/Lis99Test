package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/6.
 */
public class LSClubSpecialList extends BaseModel {

    public int totalpage;

    public int total;

    public ArrayList<Taglist> taglist;

    public static class Taglist
    {
        public int id;
//      主标题
        public String name;

        public String images;

        public int topicTotal;
//      复标题
        public String title;

    }

}
