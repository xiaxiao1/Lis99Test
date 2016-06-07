package com.lis99.mobile.club.model;

/**
 * Created by yy on 15/10/14.
 */
public interface ShareInterface {

/** 是否显示管理菜单 置顶， 删除*/
//    public boolean visibleManager ();

/**  是否是置顶 2置顶， 其他 解除置顶  */
    public String getStick ();
    /**  是否是置顶 2置顶， 其他 解除置顶  */
    public void setStick (String s);

/**  是否显示报名管理， 1线下活动， 2 线上活动，999 系列活动 ， 显示管理， 其他不显示*/
    public String getCategory ();
/**    创始人标记， 是否显示删除 1 创始人（显示删除）*/
    public String getIsJoin ();

    /**
     *   是否为新版的线下活动贴, null 否， 不为空 是新版活动帖
      * @return
     */
    public String getNewActive();

    /**
     *      俱乐部Id
     * @return
     */
    public String getClubId ();

    /**
     *      帖子Id
     * @return
     */
    public String getTopicId ();

    /**
     *      图片Url 地址
     * @return
     */
    public String getImageUrl ();

    /**
     *     分享显示标题
     * @return
     */
    public String getTitle ();

    /**
     *      分享内容
     * @return
     */
    public String getShareTxt ();

    /**
     *      分享连接地址
     * @return
     */
    public String getShareUrl ();


}
