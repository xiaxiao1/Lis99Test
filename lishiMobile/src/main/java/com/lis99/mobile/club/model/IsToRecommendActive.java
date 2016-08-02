package com.lis99.mobile.club.model;

/**
 * Created by xiaxiao on 2016/8/1.
 * 用来存放话题帖底部去往推荐活动页入口的信息
 */
public class IsToRecommendActive {


    /**
     * 是否显示推荐活动接口，vaule=标签id
     * >0 :有推荐的活动
     * <0 :没有
     */
    int is_tagid;
    //推荐活动的原因
    String reason;



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


    @Override
    public String toString() {
        return "IsToRecommendActive{" +
                "is_tagid=" + is_tagid +
                ", reason='" + reason + '\'' +
                '}';
    }
}
