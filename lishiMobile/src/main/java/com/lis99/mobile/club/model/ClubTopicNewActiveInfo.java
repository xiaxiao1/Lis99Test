package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/10/13.
 */
public class ClubTopicNewActiveInfo extends BaseModel implements ShareInterface, LikeInterface{

    public String topic_id;
    public String title;
    public int floor;
    public String stick;
    public String activeNum;
    public String shareTxt;
    public String is_image;

    public ArrayList<Topic_image> topic_image;

    public String club_id;
    public String club_title;
    public String club_images;
    public String is_jion;

    public ArrayList<Taglist> taglist;

    public ArrayList<LikeListModel> lists;

    public int category;
    public String catename;
    public String hardDegree;
    public String times;
    public String activeDesc;
    public String activeFileUrl;

    public String activeAward;
    public String awardFileUrl;

    public String deadline;

    public String days;
    public String replytopic;
    public String likeNum;
    public String LikeStatus;
    public String createdate;

    public int applyStauts;

//装备
    public int zhuangbei_id;
    public String zhuangbei_image;
    public String zhuangbei_title;
    public String zhuangbei_price;
    public int zhuangbei_star;
//  0：为关注 1已关注
    public int attenStatus;
//  发贴人id
    public int id;
//  用户ID
    public String user_id;
//用户名称
    public String nickname;

    public String headicon;

    //	====== 3.9.1===== 版主  版主（1：版主 0：非版主）
    public String moderator;

//4.0.2==
    public String visits;
    public int[] tags;

    @Override
    public String getTopic_id() {
        return this.topic_id;
    }

    @Override
    public String getLikeNum() {
        return this.likeNum;
    }

    @Override
    public String getLikeStatus() {
        return this.LikeStatus;
    }

    @Override
    public void setLikeStatus(String status) {
        this.LikeStatus = status;
    }

    @Override
    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    @Override
    public ArrayList<LikeListModel> getList() {
        return this.lists;
    }


    @Override
    public String getStick() {
        return this.stick;
    }

    @Override
    public void setStick(String s) {
        this.stick = s;
    }

    @Override
    public String getCategory() {
        return "1";
    }

    @Override
    public String getIsJoin() {
        return is_jion;
    }

    @Override
    public String getTopicId() {
        return topic_id;
    }

    /**
     * 图片Url 地址
     *
     * @return
     */
    @Override
    public String getImageUrl() {

        String imgUrl = "";
        if (topic_image != null && topic_image.size() >= 1 )
        {
            imgUrl = topic_image.get(0).image;
        }
        return imgUrl;
    }

    /**
     * 分享显示标题
     *
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * 分享内容
     *
     * @return
     */
    @Override
    public String getShareTxt() {
        return "";
    }

    /**
     * 分享连接地址
     *
     * @return
     */
    @Override
    public String getShareUrl() {
        return "http://club.lis99.com/actives/detail/" + topic_id;
    }

    public String active_code;

    @Override
    public String getNewActive() {
        return active_code;
    }

    /**
     * 俱乐部Id
     *
     * @return
     */
    @Override
    public String getClubId() {
        return club_id;
    }


    public class Taglist
    {
        public int id;
        public String name;
        public String images;

    }

    public class Topic_image
    {
        public String image;
    }


}
