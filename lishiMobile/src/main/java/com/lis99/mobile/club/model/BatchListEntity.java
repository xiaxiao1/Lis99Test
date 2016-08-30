package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yy on 16/8/26.
 */
public class BatchListEntity extends BaseModel {

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
    /**
     *   1 是已过期
     */
    @SerializedName("is_end")
    public int isEnd;
    @SerializedName("people")
    public int people;
    @SerializedName("price")
    public String price;
    @SerializedName("describe")
    public String describe;
    /**
     *  1 是已报名
     */
    @SerializedName("is_baoming")
    public int isBaoming;
}
