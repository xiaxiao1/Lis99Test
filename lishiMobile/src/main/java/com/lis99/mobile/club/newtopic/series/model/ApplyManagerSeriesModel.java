package com.lis99.mobile.club.newtopic.series.model;

import com.google.gson.annotations.SerializedName;
import com.lis99.mobile.club.model.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 16/6/2.
 */
public class ApplyManagerSeriesModel extends BaseModel {


    /**
     * error :
     * activity_id : 2410
     * title : 行走川西—牛背山
     * reportinfo : 5
     * applyPass : 4
     * applyRefuse : 1
     * applyAudit : 0
     * applyTotal : 5
     * applyTotPeople : 14
     * applylist : [{"pay_status":1,"pay_type":2,"orderid":7295,"orderuserid":419906,"is_vip":0,
     * "nickname":"密码886","headicon":"http://api.lis99.com/img/i2015/boy_default.png",
     * "createdate":"2016-05-29 18:33","applyinfoList":[{"name":"振宇","mobile":"13703846258",
     * "const":"1250.00"},{"name":"宋丽","mobile":"15083735257","const":"1250.00"},{"name":"宋艳霞",
     * "mobile":"13809372528","const":"1250.00"},{"name":"齐琪","mobile":"15038363928",
     * "const":"1250.00"},{"name":"曾静","mobile":"13193037379","const":"1250.00"}]},
     * {"pay_status":1,"pay_type":2,"orderid":6728,"orderuserid":433076,"is_vip":0,
     * "nickname":"walker123","headicon":"http://api.lis99.com/img/i2015/boy_default.png",
     * "createdate":"2016-05-20 21:27","applyinfoList":[{"name":"疯子","mobile":"13548560394",
     * "const":"1250.00"},{"name":"张樟","mobile":"13766395726","const":"1250.00"},{"name":"陈义鑫",
     * "mobile":"13284736286","const":"1250.00"}]},{"pay_status":1,"pay_type":2,"orderid":5904,
     * "orderuserid":300684,"is_vip":0,"nickname":"Apple487786","headicon":"http://i3.lis99
     * .com/upload/photo/1/2/3aab3fb82050f62cf15b44f40f01fb9d3.jpg","createdate":"2016-05-08
     * 21:16","applyinfoList":[{"name":"王威","mobile":"13051352330","const":"1250.00"},
     * {"name":"王寒","mobile":"15801357498","const":"1250.00"}]},{"pay_status":1,"pay_type":2,
     * "orderid":5899,"orderuserid":427758,"is_vip":0,"nickname":"风过无痕581484",
     * "headicon":"http://i3.lis99.com/upload/photo/1/2/3a0da630efd47905f36ed7dc9ad89556d.jpg",
     * "createdate":"2016-05-08 18:21","applyinfoList":[{"name":"尚珩","mobile":"15120052689",
     * "const":"1250.00"},{"name":"苏杭","mobile":"15120071978","const":"1250.00"}]}]
     * totalNum : 4
     * totPage : 1
     */

        @SerializedName("activity_id")
        public int activityId;
        @SerializedName("title")
        public String title;
        @SerializedName("reportinfo")
        public int reportinfo;
        @SerializedName("applyPass")
        public int applyPass;
        @SerializedName("applyRefuse")
        public int applyRefuse;
        @SerializedName("applyAudit")
        public int applyAudit;
        @SerializedName("applyTotal")
        public int applyTotal;
        @SerializedName("applyTotPeople")
        public int applyTotPeople;
        @SerializedName("totalNum")
        public int totalNum;
        @SerializedName("totPage")
        public int totPage;
        /**
         * pay_status : 1
         * pay_type : 2
         * orderid : 7295
         * orderuserid : 419906
         * is_vip : 0
         * nickname : 密码886
         * headicon : http://api.lis99.com/img/i2015/boy_default.png
         * createdate : 2016-05-29 18:33
         * applyinfoList : [{"name":"振宇","mobile":"13703846258","const":"1250.00"},{"name":"宋丽",
         * "mobile":"15083735257","const":"1250.00"},{"name":"宋艳霞","mobile":"13809372528",
         * "const":"1250.00"},{"name":"齐琪","mobile":"15038363928","const":"1250.00"},
         * {"name":"曾静","mobile":"13193037379","const":"1250.00"}]
         */

        @SerializedName("applylist")
        public ArrayList<ApplylistEntity> applylist;

        public static class ApplylistEntity implements Serializable{
            @SerializedName("pay_status")
            public int payStatus;
            @SerializedName("pay_type")
            public int payType;
            @SerializedName("orderid")
            public int orderid;
            @SerializedName("orderuserid")
            public int orderuserid;
            @SerializedName("is_vip")
            public int isVip;
            @SerializedName("nickname")
            public String nickname;
            @SerializedName("headicon")
            public String headicon;
            @SerializedName("createdate")
            public String createdate;

            @SerializedName("batch_id")
            public int batch_id;

            /**
             * name : 振宇
             * mobile : 13703846258
             * const : 1250.00
             */

            @SerializedName("applyinfoList")
            public ArrayList<HashMap<String, String>> applyinfoList;

        }
}
