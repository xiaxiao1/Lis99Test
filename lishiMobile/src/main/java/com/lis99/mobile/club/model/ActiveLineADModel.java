package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by yy on 2016/11/14.
 */

public class ActiveLineADModel extends BaseModel {


    public LinkedHashMap<String, List<Adlist>> adlist;

    public static class Adlist implements Serializable {

        public List<Adlist> lists;

        public String title;
//        0 话题贴， 1线下活动贴， 2 线上活动贴， 3URL， 4 俱乐部， 5 新线下活动贴
        public String type;
        public String url;
        public String platform;
        public String images;

        public String weight;


    }


}
