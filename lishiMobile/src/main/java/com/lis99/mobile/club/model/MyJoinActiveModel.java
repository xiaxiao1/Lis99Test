package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/12/1.
 */
public class MyJoinActiveModel extends BaseModel {

    public int totalpage;

    public ArrayList<Lists> lists;

    public class Lists
    {
        public int id;
        public int topic_id;
        public String topic_title;
        public String image;
        public int pay_type;
        public int pay_status;
        public int flag;
        public String createTime;
    }

}
