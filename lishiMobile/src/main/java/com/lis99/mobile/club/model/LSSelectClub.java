package com.lis99.mobile.club.model;

import java.util.List;

/**
 * Created by zhangjie on 1/28/16.
 */
public class LSSelectClub {

    int club_id;
    String club_title;
    String image;
    int category;
    int members;
    int total;


    public int getClub_id() {
        return club_id;
    }

    public void setClub_id(int club_id) {
        this.club_id = club_id;
    }

    public String getClub_title() {
        return club_title;
    }

    public void setClub_title(String club_title) {
        this.club_title = club_title;
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

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
