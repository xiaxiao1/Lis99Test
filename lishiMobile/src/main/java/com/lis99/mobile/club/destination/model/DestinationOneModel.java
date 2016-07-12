package com.lis99.mobile.club.destination.model;

import com.google.gson.annotations.SerializedName;
import com.lis99.mobile.club.model.BaseModel;

import java.util.List;

/**
 * Created by yy on 16/7/12.
 */
public class DestinationOneModel extends BaseModel {


    /**
     * id : 1
     * title : 嘉年华
     */

    @SerializedName("firstdest")
    public List<FirstdestEntity> firstdest;

    public static class FirstdestEntity {
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;

        public int isSelect;
    }
}
