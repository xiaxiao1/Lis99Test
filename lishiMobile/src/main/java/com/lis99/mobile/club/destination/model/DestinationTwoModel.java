package com.lis99.mobile.club.destination.model;

import com.google.gson.annotations.SerializedName;
import com.lis99.mobile.club.model.BaseModel;

import java.util.List;

/**
 * Created by yy on 16/7/12.
 */
public class DestinationTwoModel extends BaseModel {


    @SerializedName("total")
    public int total;
    @SerializedName("pageTotal")
    public int pageTotal;


    @SerializedName("destlist")
    public List<DestlistEntity> destlist;

    public static class DestlistEntity {
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;

        @SerializedName("destlists")
        public List<DestlistsEntity> destlists;

        public static class DestlistsEntity {
            @SerializedName("dest_id")
            public int destId;
            @SerializedName("tag_id")
            public int tagId;
            @SerializedName("name")
            public String name;
            @SerializedName("images")
            public String images;
            @SerializedName("image_width")
            public int imageWidth;
            @SerializedName("image_height")
            public int imageHeight;
        }
    }
}
