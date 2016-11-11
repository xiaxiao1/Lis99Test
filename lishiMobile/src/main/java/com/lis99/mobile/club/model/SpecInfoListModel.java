package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yy on 2016/11/4.
 */

public class SpecInfoListModel extends BaseModel {


    /**
     * activity_id : 5394
     * batch_id : 2
     * guigelist : [{"guige_id":11447,"name":"标准价","price":79}]
     */

    @SerializedName("activity_id")
    public int activityId;
    @SerializedName("batch_id")
    public int batchId;
    /**
     * guige_id : 11447
     * name : 标准价
     * price : 79
     */

    @SerializedName("guigelist")
    public List<GuigelistEntity> guigelist;

    public static class GuigelistEntity implements Serializable{
        @SerializedName("guige_id")
        public int guigeId;
        @SerializedName("name")
        public String name;
        @SerializedName("price")
        public String price;
//      选择的数量
        public int selectNum;
    }
}
