package com.lis99.mobile.club.topicstrimg;

import com.lis99.mobile.club.model.BaseModel;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Created by yy on 16/4/1.
 */
@Table(name = "StringImageChild")
public class StringImageChildModel extends BaseModel {


    //        获取父类
    public StringImageModel getParent(DbManager db ) throws DbException
    {
        return db.findById(StringImageModel.class, parentId);
    }

    @Column(name = "parentId")
    public int parentId;

    @Column(name = "id", isId = true)
    public int id;

    /**
     *      图 片地址
     */
    @Column(name = "img")
    public String img;
    /**
     *      内容
     */
    @Column(name = "content")
    public String content;

    /**
     *      是否可编辑, 0 可以， 1， 不可编辑
     */
    @Column(name = "isEditing")
    public int isEditing;

}
