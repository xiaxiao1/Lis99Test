package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yy on 16/5/24.
 */
public class BenefitListModel extends BaseModel {

    public int welfareTotal;
    public int welfarePage;

    public ArrayList<BenefitItem> lists;

    public static class BenefitItem implements Serializable {

        public int id;
        public int welfare_id;
        public String title;
        public String enddate;
        /**
             * 1：装备 2：积分 3：在线支付优惠券 4：线下支付优惠券，其余一次往下增加
         */
        public int type;
        /**
         *   1:已领取 2:已过期，未领取 3:没过期，未领取
        * */
        public int flag;
        /**
         * 图片地址
         * */
        public String thumb;
/**
 *  积分数量
 *  */
        public String content;
/** 说明
 * */
        public String readme;

    }

}
