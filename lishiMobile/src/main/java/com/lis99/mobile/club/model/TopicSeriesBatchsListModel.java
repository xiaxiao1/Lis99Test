package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yy on 16/5/31.
 *
 *      系列活动批次信息
 */
public class TopicSeriesBatchsListModel extends BaseModel{


        @SerializedName("activity_id")
        public int activityId;
        /**
         * batch_id : 1
         * starttime : 2016-06-05
         * endtime : 2016-06-10
         * settime : 2016-06-05 06:40
         * deadline : 2016-06-01
         * is_end : 1
         * people : 20
         * price : 1250.00
         * describe : 暂无数据
         * is_baoming : 1
         */

        @SerializedName("batchList")
        public ArrayList<BatchListEntity> batchList;

        public static class BatchListEntity {
            @SerializedName("batch_id")
            public int batchId;
            @SerializedName("starttime")
            public String starttime;
            @SerializedName("endtime")
            public String endtime;
            @SerializedName("settime")
            public String settime;
            @SerializedName("deadline")
            public String deadline;
            @SerializedName("is_end")
            public int isEnd;
            @SerializedName("people")
            public int people;
            @SerializedName("price")
            public String price;
            @SerializedName("describe")
            public String describe;
            @SerializedName("is_baoming")
            public int isBaoming;
        }
}
