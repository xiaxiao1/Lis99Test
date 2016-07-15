package com.lis99.mobile.club.filter.model;

import com.google.gson.annotations.SerializedName;
import com.lis99.mobile.club.model.BaseModel;

import java.util.List;

/**
 * Created by yy on 16/7/14.
 */
public class NearbyFilterModel extends BaseModel {


    /**
     * id : 3
     * name : 运动类型
     * lists : [{"id":202,"name":"徒步"},{"id":203,"name":"高海拔徒步"},{"id":204,"name":"攀岩"},
     * {"id":205,"name":"攀冰"},{"id":206,"name":"高海拔攀登"},{"id":207,"name":"潜水"},{"id":208,
     * "name":"冲浪"},{"id":209,"name":"骑行"},{"id":210,"name":"自驾"},{"id":211,"name":"滑雪"},
     * {"id":212,"name":"露营"},{"id":213,"name":"户外腐败"},{"id":221,"name":"重装徒步"},{"id":222,
     * "name":"轻装徒步"},{"id":223,"name":"登山"},{"id":224,"name":"摄影"},{"id":225,"name":"深度游"},
     * {"id":226,"name":"越野跑"},{"id":227,"name":"亲子户外"},{"id":228,"name":"骑马"},{"id":229,
     * "name":"溯溪"},{"id":230,"name":"垂钓"},{"id":231,"name":"体育健身"},{"id":232,"name":"跑步"},
     * {"id":233,"name":"航海"},{"id":234,"name":"探洞"},{"id":235,"name":"滑翔伞"}]
     */

    @SerializedName("sievenlist")
    public List<SievenlistEntity> sievenlist;

    public static class SievenlistEntity {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        /**
         * id : 202
         * name : 徒步
         */

        @SerializedName("lists")
        public List<ListsEntity> lists;

        public static class ListsEntity {
            @SerializedName("id")
            public int id;
            @SerializedName("name")
            public String name;
//            0 未选中， 1 选中
            public int isSelect;
        }
    }
}
