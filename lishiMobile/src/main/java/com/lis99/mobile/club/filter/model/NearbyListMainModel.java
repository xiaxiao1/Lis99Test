package com.lis99.mobile.club.filter.model;

import com.google.gson.annotations.SerializedName;
import com.lis99.mobile.club.model.BaseModel;

import java.util.List;

/**
 * Created by yy on 16/7/14.
 */
public class NearbyListMainModel extends BaseModel {



    @SerializedName("totalpage")
    public int totalpage;
    @SerializedName("total")
    public int total;
    /**
     * id : 4319
     * activity_code : El6WXSkZ
     * title : 海坨登山一日游
     * harddesc : 北京·海坨山
     * cate_id : 7
     * cate_name : 徒步穿越
     * images : http://i3.lis99.com/upload/club/f/e/f/fe9b2d6468cf6d718ce00b1fbfc6029f.png
     * width : 862
     * height : 584
     * starttime : 2016-07-16 07:00:00
     * price : 70.00
     */

    @SerializedName("lists")
    public List<ListsEntity> lists;

    public static class ListsEntity {
        @SerializedName("id")
        public String id;
        @SerializedName("activity_code")
        public String activityCode;
        @SerializedName("title")
        public String title;
        @SerializedName("harddesc")
        public String harddesc;
        @SerializedName("cate_id")
        public String cateId;
        @SerializedName("cate_name")
        public String cateName;
        @SerializedName("images")
        public String images;
        @SerializedName("width")
        public int width;
        @SerializedName("height")
        public int height;
        @SerializedName("starttime")
        public String starttime;
        @SerializedName("price")
        public String price;

        //        出发城市
        public String setcityname;
        //        批次总数
        public String batch_count;
        //        批次里价格最小
        public String min_price;
        //          活动批次区间
        public String starttime_intval;
        //          行成天数
        public String trip_days;

    }
}
