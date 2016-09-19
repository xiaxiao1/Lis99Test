package com.lis99.mobile.mine.model;

import com.lis99.mobile.club.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by zhangjie on 5/21/16.
 */
public class LSMyActivity extends BaseModel{
    public int id;
    public int orderid;
    public int club_id;
    public int leader_userid;
    public int create_userid;
    public int topic_id;
    public int width;
    public int height;
    public int is_comment;
    public int pay_type;
    public int pay_status;
    public int applyNum;
    public int star;
    public int flag;
    public String leader_mobile;
    public int batch;

    public double consts;
    public double totprice;

    public String ordercode;
    public String topic_title;
    public String title;
    public String activity_code;
    public String image;
    public String createTime;
    public String comment;
    public String leader_nickname;
    public String create_user_nickname;
    public String create_user_headicon;
    public String leader_headicon;
//  备注
    public String remark;
//    取消报名理由
    public String reason;

    public ArrayList<String> leader_tag;
    public ArrayList<Applicant> apply_info;

    public static class Applicant extends BaseModel{
//        public String name;
//        public String names;
//        public String mobile;
//        public String mobiles;
//        public String consts;
//        public String constss;

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
