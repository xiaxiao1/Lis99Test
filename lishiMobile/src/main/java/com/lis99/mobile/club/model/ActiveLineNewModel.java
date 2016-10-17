package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yy on 16/1/21.
 */
public class ActiveLineNewModel extends BaseModel {


    public int getDefault_data() {
        return default_data;
    }

    public void setDefault_data(int default_data) {
        this.default_data = default_data;
    }

    /**
     * 是否为容错数据，1为容错数据，0为正常数据
     * totalpage : 1
     * total : 5
     * activitylist : [{"id":"5","title":"有村架纯","startdate":1454169600,"harddesc":"日本新生代","images":"http://i3.lis99.com/upload/club/7/7/2/773e2c215a606b21af0731e648aefac2.jpg!middle","height":600,"width":425},{"id":"4","title":"新版活动贴测试","startdate":1455033600,"harddesc":"出来浪一浪！","images":"http://i3.lis99.com/upload/club/6/f/7/6f5435fe5f24e87962c5e4cab4504e57.jpg!middle","height":1024,"width":1020},{"id":"3","title":"测试新帖子信息","startdate":1453305600,"harddesc":"很轻松","images":"http://i3.lis99.com/upload/club/3/b/7/3b252e7c8a7eff71636f42a58edc4f37.jpg!middle","height":768,"width":1024},{"id":"2","title":"坝上徒步","startdate":1454083200,"harddesc":"活动短描述","images":"http://i3.lis99.com/upload/club/1/d/4/1d6d7089fb2f1dad87d7f6da6a234924.jpg!middle","height":594,"width":950},{"id":"1","title":"2016年1月20日砾石滑雪","startdate":1453219200,"harddesc":"难度一般","images":"http://i3.lis99.com/upload/club/0/e/e/0eb72d329480056744914cf4cf9b11ce.jpg!middle","height":900,"width":1600}]
     * areaweblist : [{"images":"http://i3.lis99.com/upload/mstaticon/5/a/4/5a0c562b01a4cdf592b26c5e03dc73c4.jpg!middle","height":270,"width":338,"tagid":"17","tagname":"五台山"},{"images":"http://i3.lis99.com/upload/mstaticon/4/5/f/457de256cb7edbae3009b49699db356f.jpg!middle","height":270,"width":338,"tagid":"20","tagname":"灵山"},{"images":"http://i3.lis99.com/upload/mstaticon/6/3/7/636242c29d4ea609d249efc8e88e8d27.jpg!middle","height":270,"width":338,"tagid":"59","tagname":"牛背山"},{"images":"http://i3.lis99.com/upload/mstaticon/b/2/3/b23a3606f64b1a9e9dfda2b132e58413.jpg!middle","height":271,"width":339,"tagid":"84","tagname":"阿尔山"},{"images":"http://i3.lis99.com/upload/mstaticon/3/e/b/3e38940008f5910260043f26ff4296ab.jpg!middle","height":271,"width":339,"tagid":"79","tagname":"王莽岭"},{"images":"http://i3.lis99.com/upload/mstaticon/1/8/4/184b1d1a364cfe247debe756b1e0fe64.jpg!middle","height":270,"width":338,"tagid":"18","tagname":"长白山"},{"images":"http://i3.lis99.com/upload/mstaticon/d/9/0/d9d044e682f23b20999b953c19c0c290.jpg!middle","height":270,"width":338,"tagid":"14","tagname":"鳌太"},{"images":"http://i3.lis99.com/upload/mstaticon/8/d/4/8d866dc6f99b6908f707f7ace6a0f9f4.jpg!middle","height":270,"width":339,"tagid":"95","tagname":"扎尕那"}]
     */
    public int default_data;
    private int totalpage;
    private int total;
//    城市ID
    public int city_id;
//    当前城市名称
    public String city_name;
    /**
     * id : 5
     * title : 有村架纯
     * startdate : 1454169600
     * harddesc : 日本新生代
     * images : http://i3.lis99.com/upload/club/7/7/2/773e2c215a606b21af0731e648aefac2.jpg!middle
     * height : 600
     * width : 425
     */

    private List<ActivitylistEntity> activitylist;
    /**
     * images : http://i3.lis99.com/upload/mstaticon/5/a/4/5a0c562b01a4cdf592b26c5e03dc73c4.jpg!middle
     * height : 270
     * width : 338
     * tagid : 17
     * tagname : 五台山
     */

    private List<AreaweblistEntity> areaweblist;

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setActivitylist(List<ActivitylistEntity> activitylist) {
        this.activitylist = activitylist;
    }

    public void setAreaweblist(List<AreaweblistEntity> areaweblist) {
        this.areaweblist = areaweblist;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public int getTotal() {
        return total;
    }

    public List<ActivitylistEntity> getActivitylist() {
        return activitylist;
    }

    public List<AreaweblistEntity> getAreaweblist() {
        return areaweblist;
    }

    public static class ActivitylistEntity implements Serializable{
        private String id;
        private String title;
        private String startdate;
        private String harddesc;
        private String images;
        private int height;
        private int width;
//        出发城市
        public String setcityname;
//        批次总数
        public String batch_count;
//        批次里价格最小
        public String min_price;
//          活动批次区间
        public String starttime_intval;
//          行成天数
        public String trip_days;

        public String getCatename() {
            return catename;
        }

        public void setCatename(String catename) {
            this.catename = catename;
        }

        private String catename;

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public void setHarddesc(String harddesc) {
            this.harddesc = harddesc;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getStartdate() {
            return startdate;
        }

        public String getHarddesc() {
            return harddesc;
        }

        public String getImages() {
            return images;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    public static class AreaweblistEntity implements Serializable {
        private String images;
        private int height;
        private int width;
        private String tagid;
        private String tagname;

        public void setImages(String images) {
            this.images = images;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setTagid(String tagid) {
            this.tagid = tagid;
        }

        public void setTagname(String tagname) {
            this.tagname = tagname;
        }

        public String getImages() {
            return images;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public String getTagid() {
            return tagid;
        }

        public String getTagname() {
            return tagname;
        }
    }

    public List<Adlist> adlist;

    public class Adlist implements Serializable {

        public String title;
        public int type;
        public String url;
        public String platform;
        public String images;

    }


}
