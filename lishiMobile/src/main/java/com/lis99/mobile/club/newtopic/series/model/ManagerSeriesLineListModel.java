package com.lis99.mobile.club.newtopic.series.model;

import com.google.gson.annotations.SerializedName;
import com.lis99.mobile.club.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by yy on 16/6/1.
 */
public class ManagerSeriesLineListModel extends BaseModel {


        @SerializedName("activity_id")
        public int activityId;
        /**
         * batch_id : 1
         * starttime : 2016.06.05
         * endtime : 2016.06.10
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
        }
}
