package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/9/25.
 */
public class EquipReplyList extends BaseModel {

    public int totalpage;

    public int totstart;

    public ArrayList<Commentlist> commentlist;

    public class Commentlist
    {
        public int id;
        public String comment;
        public String star;
        public int user_id;
        public String headicon;
        public String nickname;
    }

}
