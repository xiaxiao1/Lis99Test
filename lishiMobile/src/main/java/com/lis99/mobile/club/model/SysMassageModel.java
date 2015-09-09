package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/9/9.
 *
 * totPage	int	总页数
 totNum	int	总条数
 lists	-------------------------	-------------------
 nickname	string	昵称
 headicon	string	头像
 topicid	int	帖子ID
 title	string	标题
 content	string	内容
 createtime	string	创建时间
 url	str	跳转链接

 *
 */
public class SysMassageModel extends BaseModel {

    public int totPage;
    public int totNum;

    public ArrayList<Lists> lists;

    public class Lists
    {
        public String nickname;
        public String headicon;
        public String title;
        public int topicid;
        public String content;
        public String createtime;
        public String url;
        //默认没有展开
        public int state = 0;
    }

}
