package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/12/1.
 */
public class MyJoinActiveDetailModel extends BaseModel {

    public int id;

    public String title;

    public int pay_type;
    public int pay_status;

    public String consts;

    public String totprice;

    public int applyNum;

    public int flag;

    public String payhint;

    public String ordercode;

    public String topic_id;

    public String category;

    public ArrayList<Apply_info> apply_info;

    public class Apply_info extends BaseModel
    {
        public String name;
        public String sex;
        public String mobile;
        public String qq;
        public String postaladdress;
        public String consts;
        public String credentials;
        public String phone;



    }

}
