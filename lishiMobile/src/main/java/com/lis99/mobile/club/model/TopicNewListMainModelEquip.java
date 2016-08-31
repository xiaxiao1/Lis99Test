package com.lis99.mobile.club.model;

import com.google.gson.annotations.SerializedName;
import com.lis99.mobile.util.Common;

import java.util.ArrayList;

/**
 * Created by yy on 16/3/8.
 */
public class TopicNewListMainModelEquip extends TopicNewListMainModelTitle implements LikeInterface {


    /**
     * 为了修改推荐活动入口位置添加的字段，只在话题帖底部的关联推荐活动时使用
     * is_tagid
     * reason
     * title
     */
    public int is_tagid;
    public String reason;
    public String tag_name;
    public String tag_image;

    public String getTag_image() {
        return tag_image;
    }

    public void setTag_image(String tag_image) {
        this.tag_image = tag_image;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getIs_tagid() {
        return is_tagid;
    }

    public void setIs_tagid(int is_tagid) {
        this.is_tagid = is_tagid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

//*****************************************************************************************//
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
