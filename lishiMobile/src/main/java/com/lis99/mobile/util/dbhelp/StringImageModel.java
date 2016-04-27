package com.lis99.mobile.util.dbhelp;

import com.lis99.mobile.club.model.BaseModel;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 16/4/1.
 */
@Table(name = "StringImage")
public class StringImageModel extends BaseModel {

    @Column(name = "id", isId = true)
    public int id;

    @Column(name = "title")
    public String title;

    /**
     *      内容
     */
    @Column(name = "content")
    public String content;

    /**
     *      是否是过期了， 0 没有， 1过期了
     */
    @Column(name = "isPass")
    public int isPass;

    @Column(name = "istest")
    public int istest;

    @Column(name = "topicId")
    public String topicId;

    @Column(name = "clubId")
    public String clubId;

    @Column(name = "clubName")
    public String clubName;

    /**
     *      编辑时间
     */
    @Column(name = "editTime")
    public String editTime;

    @Column(name = "item")
    public ArrayList<StringImageChildModel> item;
    /**
     *      是否为追加内容
     */
    @Column(name = "isAdd")
    public boolean isAdd;

    public List<StringImageChildModel> getChildern (DbManager db ) throws DbException
    {
        return db.selector(StringImageChildModel.class).where("parentId", "=", this.id).findAll();
    }

//    public List<StringImageChildModel> getChildernWithTopicId (DbManager db ) throws DbException
//    {
//        return db.selector(StringImageChildModel.class).where("topicId", "=", this.topicId).findAll();
//    }


}
