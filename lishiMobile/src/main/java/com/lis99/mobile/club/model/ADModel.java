package com.lis99.mobile.club.model;

/**
 * Created by yy on 15/9/1.
 */
public class ADModel extends BaseModel {

//    public ArrayList<Lists> lists;

    public Lists lists;

    public class Lists
    {
//        标志位（0：没有广告 1：没有新广告 2：新广告）
        public int flag;

        public String version;

        public String images;
    }

}
