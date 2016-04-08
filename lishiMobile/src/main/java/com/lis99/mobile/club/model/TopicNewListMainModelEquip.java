package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;
import com.lis99.mobile.util.Common;

import java.util.ArrayList;

/**
 * Created by yy on 16/3/8.
 */
public class TopicNewListMainModelEquip extends TopicNewListMainModelTitle implements LikeInterface {


    @SerializedName("zhuangbei_id")
    public int zhuangbei_id;
    @SerializedName("zhuangbei_image")
    public String zhuangbei_image;
    @SerializedName("zhuangbei_title")
    public String zhuangbei_title;
    @SerializedName("zhuangbei_star")
    public int zhuangbei_star;
    @SerializedName("zhuangbei_price")
    public int zhuangbei_price;
    @SerializedName("likeNum")
    public int likeNum;
    @SerializedName("myLikeStatus")
    public int myLikeStatus;
    @SerializedName("likeuserlist")
    public ArrayList<Likeuserlist> likeuserlist;
    @SerializedName("taglist")
    public ArrayList<Taglist> taglist;

    public static class Taglist
    {
        @SerializedName("tagid")
        public int tagid;
        @SerializedName("tagname")
        public String tagname;
    }

    public static class Likeuserlist extends LikeListModel
    {
        @SerializedName("id")
        public int id;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("headicon")
        public String headicon;

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getHeadIcon() {
            return headicon;
        }

        @Override
        public int getIsVip() {
            return 0;
        }
    }

    @Override
    public String getTopic_id() {
        return topicsId;
    }

    @Override
    public String getLikeNum() {
        return ""+likeNum;
    }

    @Override
    public String getLikeStatus() {
        return ""+myLikeStatus;
    }

    @Override
    public void setLikeStatus(String status) {
        myLikeStatus = Common.string2int(status);
    }

    @Override
    public void setLikeNum(String likeNum) {
        this.likeNum = Common.string2int(likeNum);
    }

    @Override
    public ArrayList<LikeListModel> getList() {
        ArrayList<LikeListModel> llm = new ArrayList<LikeListModel>(likeuserlist);
        return llm;
    }

}
