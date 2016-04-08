package com.lis99.mobile.mine;

/**
 * Created by zhangjie on 8/19/15.
 */
public class LSBaseTopicModel {


    String topic_id;
    String topic_title;
    String createdate;
    String club_title;
    String club_id;
    String image;
    /**
     *      帖子类型：0话题贴，1线路活动帖 ，2线上活动帖 3 新版活动帖子 4 新版话题帖
     */
    int category;


    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopic_title() {
        return topic_title;
    }

    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getClub_title() {
        return club_title;
    }

    public void setClub_title(String club_title) {
        this.club_title = club_title;
    }

    public String getClub_id() {
        return club_id;
    }

    public void setClub_id(String club_id) {
        this.club_id = club_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
