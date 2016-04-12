package com.lis99.mobile.util.dbhelp;

import android.os.Build;

import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.Common;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by yy on 16/4/1.
 */
public class DataHelp {

    static private DataHelp instance;

    private DbManager.DaoConfig daoConfig;

    private DbManager db;

    public DataHelp() {

        daoConfig = new DbManager.DaoConfig();
        daoConfig.setDbName("mytest.db");
        daoConfig.setDbDir(StorageUtils.getOwnCacheDirectory(
                LSBaseActivity.activity.getApplicationContext(), "lishi99/cache"));
        daoConfig.setDbVersion(1);
        daoConfig.setDbOpenListener(new DbManager.DbOpenListener() {
            @Override
            public void onDbOpened(DbManager db) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    db.getDatabase().enableWriteAheadLogging();
                }
            }
        });
        daoConfig.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

//                Common.log("newVersion == " + newVersion + "\noldVersion == " + oldVersion);

//                if ( newVersion >= 2 && oldVersion < 2 )
//                {
//                    try {
//                        db.addColumn(StringImageChildModel.class, "img");
//                        db.addColumn(StringImageChildModel.class, "content");
//                        db.addColumn(StringImageChildModel.class, "isEditing");
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                        Common.log("db Updata Error = " + e.toString());
//                    }
//                }
//
//                if ( newVersion >= 5 && oldVersion < 5 )
//                {
//                    try {
//                        db.addColumn(StringImageModel.class, "isPass");
//                        Common.log("db Updata OK ====  5 ");
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                        Common.log("db Updata Error = " + e.toString());
//                    }
//                }

            }
        });

        db = x.getDb(daoConfig);

    }

    public static DataHelp getInstance() {
        if (instance == null) instance = new DataHelp();
        return instance;
    }


    public void search() {


        try {
            StringImageModel parent = db.selector(StringImageModel.class).where("id", "in", new int[]{5, 6}).findFirst();

            if (parent != null) {
                Common.log("parent.title == " + parent.title + "\nparent.id == " + parent.id);
            } else {
                Common.log("parent search id in 1, 3, is null");
            }

        } catch (DbException e) {
            e.printStackTrace();
        }


    }


    public void add() {
        StringImageModel parent = new StringImageModel();
        parent.title = "title" + System.currentTimeMillis();

        try {
            db.save(parent);
        } catch (DbException e) {
            e.printStackTrace();

        }

    }

    public boolean remove() {
        try {
            StringImageModel parent = db.selector(StringImageModel.class).where("id", "in", new
                    int[]{3, 4, 5, 6, 7}).findFirst();
            if (parent != null) {
                db.delete(parent);
            }
        } catch (DbException e) {
            e.printStackTrace();
            Common.log("remove Error == " + e.toString());
            return false;
        }
        return true;
    }

    public boolean cheange() {
        try {
            StringImageModel parent = db.selector(StringImageModel.class).findFirst();

            if ( parent != null )
            {

                Common.log("parent.title == "+parent.title);

                parent.title = "is cheange";

                db.saveOrUpdate(parent);

//                db.saveBindingId()


                StringImageModel first = db.selector(StringImageModel.class).findFirst();

                if ( first != null )
                {
                    Common.log("parent.title == "+parent.title);
                }



            }
        } catch (DbException e) {
            e.printStackTrace();
            Common.log("change Error == " + e.toString());
            return false;
        }
        return true;

    }


    public boolean removeTab()
    {
        try {
            List<StringImageModel> parent = db.selector(StringImageModel.class).findAll();

            if ( parent != null )
            {

                db.dropTable(StringImageModel.class);

                db.save(parent);

            }
        } catch (DbException e) {
            e.printStackTrace();
            Common.log("change Error == " + e.toString());
            return false;
        }
        return true;
    }


    public void addChild ()
    {


        try {
            StringImageModel parent = db.selector(StringImageModel.class).findFirst();

            if ( parent != null )
            {
                Common.log("parent.title == "+parent.title+"\nparent.id == "+parent.id);

                StringImageChildModel child = new StringImageChildModel();
                child.parentId = parent.id;
                child.content = "Content"+System.currentTimeMillis();

//                db.save(child);
                db.saveBindingId(child);

                Common.log("child is added");

            }
            else
            {
                Common.log("parent search id in 1, 3, is null");
            }





        } catch (DbException e) {
            e.printStackTrace();
        }


    }


    public void search2 ()
    {


        try {
            List<StringImageModel> parent = db.selector(StringImageModel.class).findAll();

            if ( parent != null && parent.size() != 0 )
            {
                for ( StringImageModel item : parent )
                {
                    Common.log("parent.title == "+item.title+"\nparent.id == "+item.id + "\nparent.isPass=="+item.isPass);

//                    List<StringImageChildModel> child = item.getChildern(db);

//                    if ( child != null && child.size() != 0 )
//                    {
//                        for ( StringImageChildModel c : child )
//                        {
//                            Common.log("child.content == "+c.content+"\nchild.id == "+c.id+"\nchild.parent == "+c.parentId);
//                        }
//                    }
//                    else
//                    {
//                        Common.log("parent.title == "+item.title+"\nparent.id == "+item.id +"   child is null");
//                    }


                }
            }
            else
            {
                Common.log("parent search id in 1, 3, is null");
            }





        } catch (DbException e) {
            e.printStackTrace();
        }


    }


}
