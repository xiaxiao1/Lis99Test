package com.lis99.mobile.club.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/11/26.
 */
public class ApplyManagerModel extends BaseModel{

    public int totPage;

    public ArrayList<Info> info;

    public class Info
    {
        public int topic_id;
        public String title;
        public int club_id;
        public int applyPass;
        public int applyRefuse;
        public int applyAudit;
        public int applyTotal;
        public int totalApplyInfo;

        public ArrayList<Lists> lists;

        public class Lists
        {
            public int applyid;
            public int applyuserid;
            public int is_vip;
            public String nickname;
            public String headicon;
            /**
             *      支付方式（0:免费活动 1：线下 2:微信 3:支付宝）
             */
            public int pay_type;
            /**
             * 支付状态（0未支付1已支付 2已退款 3退款中 4 线下支付 5免费活动）
             */
            public int pay_status;
            public String createdate;

            public ArrayList<HashMap<String, String>> apply_info;
        }


    }

}
