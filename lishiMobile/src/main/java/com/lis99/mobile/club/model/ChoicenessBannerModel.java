package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/10/19.
 *
 *  version	int	App版本号
     channel	int	渠道号
     image	string	广告banner
     height	int	图片高度
     width	int	图片宽度
     type	int	跳转类型 0：话题帖 1:线下活动帖 2：线上活动帖 3：URL 4：聚乐部
     id	int	跳转至相应详情页面的id(type为0、1、2、4时出现id字段)
     link		跳转链接（只用type为3的时候才会出现link字段）
 *
 */
public class ChoicenessBannerModel extends BaseModel {

    public int total;

    public ArrayList<Lists> lists;

    public class Lists
    {
        public int version;
        public int channel;
        public String image;
        public int type;
        public int id;
        public String link;
    }

}
