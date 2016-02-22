package com.lis99.mobile.club.model;

import java.io.Serializable;

/**
 * Created by yy on 16/2/22.
 *  分享类
 *
 */
public class ShareModel implements ShareInterface, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String stick, category, isJoin, newActive, clubId, topicId, imageUrl, title, shareTxt, shareUrl;


    /**
     * 是否是置顶 2置顶， 其他 解除置顶
     */
    @Override
    public String getStick() {
        return stick;
    }

    /**
     * 是否是置顶 2置顶， 其他 解除置顶
     *
     * @param s
     */
    @Override
    public void setStick(String s) {
        stick = s;
    }

    /**
     * 是否显示报名管理， 1线下活动， 2 线上活动， 显示管理， 其他不显示
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * 创始人标记， 是否显示删除 1 创始人（显示删除）
     */
    @Override
    public String getIsJoin() {
        return isJoin;
    }

    /**
     * 是否为新版的线下活动贴, null 否， 不为空 是新版活动帖
     *
     * @return
     */
    @Override
    public String getNewActive() {
        return newActive;
    }

    /**
     * 俱乐部Id
     *
     * @return
     */
    @Override
    public String getClubId() {
        return clubId;
    }

    /**
     * 帖子Id
     *
     * @return
     */
    @Override
    public String getTopicId() {
        return topicId;
    }

    /**
     * 图片Url 地址
     *
     * @return
     */
    @Override
    public String getImageUrl() {
        return imageUrl;
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
        return shareTxt;
    }

    /**
     * 分享连接地址
     *
     * @return
     */
    @Override
    public String getShareUrl() {
        return shareUrl;
    }
}
