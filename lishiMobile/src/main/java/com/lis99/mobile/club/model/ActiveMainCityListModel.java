package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yy on 16/3/28.
 */
public class ActiveMainCityListModel extends BaseModel {


    /**
     * id : 2
     * name : 北京
     * pinyin : beijing
     */

    @SerializedName("citylist")
    public ArrayList<CitylistEntity> citylist;

    public static class CitylistEntity {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("pinyin")
        public String pinyin;

//        0, 默认， 1 选择
        public String select;
    }
}
