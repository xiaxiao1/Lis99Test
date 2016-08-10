package com.lis99.mobile.club.model;

/**
 * Created by yy on 15/11/23.
 *      报名人员信息
 */
public class NewApplyUpData extends BaseModel{


        public String name;
        //居住城市
        public String address;
        public String sex = "男";
        public String qq;
        public String credentials;
        public String mobile;
//        紧急联系人电话
        public String phone;
//        邮寄地址
        public String postaladdress;
//      默认是1， 其他为0
        public String is_default = "0";

        public String id;

}
