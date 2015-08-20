package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/6.
 */
public class LSClubSpecialList extends BaseModel {

    public int totalpage;

    public int total;

    public ArrayList<Taglist> taglist;

    public class Taglist
    {
        public int id;

        public String name;

        public String images;

    }

}
