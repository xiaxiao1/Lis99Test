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

    @Column(name = "item")
    public ArrayList<StringImageChildModel> item;

    public List<StringImageChildModel> getChildern (DbManager db ) throws DbException
    {
        return db.selector(StringImageChildModel.class).where("parentId", "=", this.id).findAll();
    }

    public List<StringImageChildModel> getChildernWithTopicId (DbManager db ) throws DbException
    {
        return db.selector(StringImageChildModel.class).where("topicId", "=", this.topicId).findAll();
    }


}
