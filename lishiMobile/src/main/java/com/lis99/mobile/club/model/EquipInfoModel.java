package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/9/23.
 *
 * totstart	int	总星数
 info	-----------------------------------------------	装备详情信息
 id	int	装备id
 title	string	装备标题
 thumb	string	装备图片
 height	int	图片高
 width	int	图片宽
 market_price	string	市场价格
 model	string	型号
 description	string	描述
 country_id	int	国家id
 country_title	int	国家名称
 brand_id	int	品牌id
 brand_title	int	品牌名
 cate	--------------------------------------------------	类型
 id	int	类型id
 parentid	int	父类型
 catname	string	类型名称


 texture	-----------------------------------------------	面料
 sid	int	面料id
 name	string	面料名称
 describe	string	面料描述
 path	string	面料路径
 weight	int	面料权重


 totcomment	int	评论总数
 commenlist	-------------------------------------------------	评论列表
 commentid	int	评论id
 comment	string	评论内容
 star	int	星数
 user_id	int	用户id
 headicon	string	用户头像
 nickname	int	用户昵称

 */
public class EquipInfoModel extends BaseModel {

    public int totstart;

    public Info info;

    public class Info
    {
        public int id;
        public String title;
        public String thumb;
        public String market_price;
        public String model;
        public String description;
        public int country_id;
        public String country_title;
        public int brand_id;
        public String brand_title;

        public int totcomment;

        public int is_buy;

        public int likestatus;

        public String share_url;


        public Cate cate;

        public ArrayList<Texture> texture;

        public ArrayList<Commenlist> commenlist;

        public ArrayList<Zhuangbeiimg> zhuangbeiimg;

    }

    public class Zhuangbeiimg
    {
        public String images;
    }

    public class Cate
    {
        public int id;
        public int parentid;
        public String catname;
    }

    public class Texture
    {
        public int sid;
        public String name;
        public String describe;
        public String path;
        public int weight;
        public boolean isLast;
    }

    public class Commenlist
    {
        public int commentid;
        public String comment;
        public String star;
        public int user_id;
        public String headicon;
        public String nickname;
    }

}
