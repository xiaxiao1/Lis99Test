package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/7.
 */
public class SearchInfoTopicModel extends BaseModel{


    public int totpage;

    public int clubtopictot;

    public ArrayList<Clubtopiclist> clubtopiclist;

    public class Clubtopiclist
    {
        public int id;
        public String title;
        public String createdate;
        public int club_id;
        public String clubname;
        public int user_id;
        public String nickname;

        /**
         * 帖子类型：0旧版话题贴，1旧版线路活动帖, 2旧版线上活动，3新版话题贴，4新版活动贴
         */
        @SerializedName("category")
        public int category;
    }





}
