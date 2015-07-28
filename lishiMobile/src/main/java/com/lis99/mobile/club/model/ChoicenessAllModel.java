package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/24.
 * <p/>
 * totalpage	int	总页数
 * omnibustotal	int	总条数
 * omnibuslist	------------------------------------------	列表信息
 * id	int	列表id
 * title	string	专题标题
 * subhead	string	副标题
 * images	string	图片连接
 * describe	string	专题标记
 */
public class ChoicenessAllModel extends BaseModel {

    public int totalpage;
    public int omnibustotal;

    public ArrayList<Omnibuslist> omnibuslist;

    public class Omnibuslist {
        public String id;
        public String title;
        public String subhead;
        public String images;
        public String describe;
    }

}
