package com.lis99.mobile.club.model;

import java.util.List;

/**
 * Created by xiaxiao on 2016.10.17.
 * 活动主页下部的砾石推荐活动列表
 */
public class LiShiRecommendActiveModel extends BaseModel {


    /**
     * data : {"error":"","totalpage":1,"total":3,"lists":[{"activity_id":6078,
     * "activity_code":"hGQ5VWoZ","title":"稻城亚丁、海螺沟、新都桥行摄净心之旅","images":"http: //i3.lis99
     * .com/upload/club/2/0/8/203b20bf2f6959dafc9a2a6fa831f048.jpg","width":800,"height":564,
     * "cityname":"成都出发","lowprice":3480,"tripdays":8,"totbatchs":3,"month_day":"2016年10月~2016年11月",
     * "catename":"亲子户外"},{"activity_id":6071,"activity_code":"Tw7GMWoZ","title":"探洞，玩一场地下城与勇士",
     * "images":"http: //i3.lis99.com/upload/club/c/5/6/c59aeb16e9bb9a83da5699e9214ffc66.jpg",
     * "width":500,"height":334,"cityname":"北京出发","lowprice":198,"tripdays":1,"totbatchs":1,
     * "month_day":"2016年10月","catename":""},{"activity_id":6077,"activity_code":"GiBVEWoZ",
     * "title":"膜拜蜀山之王贡嘎神山","images":"http: //i3.lis99
     * .com/upload/club/b/c/d/bcbf3364607a147678038dcb2c2f84bd.jpg","width":800,"height":534,
     * "cityname":"成都出发","lowprice":2180,"tripdays":5,"totbatchs":1,"month_day":"2016年10月",
     * "catename":""}]}
     */




        /**
         * error :
         * totalpage : 1
         * total : 3
         * lists : [{"activity_id":6078,"activity_code":"hGQ5VWoZ","title":"稻城亚丁、海螺沟、新都桥行摄净心之旅",
         * "images":"http: //i3.lis99.com/upload/club/2/0/8/203b20bf2f6959dafc9a2a6fa831f048
         * .jpg","width":800,"height":564,"cityname":"成都出发","lowprice":3480,"tripdays":8,
         * "totbatchs":3,"month_day":"2016年10月~2016年11月","catename":"亲子户外"},{"activity_id":6071,
         * "activity_code":"Tw7GMWoZ","title":"探洞，玩一场地下城与勇士","images":"http: //i3.lis99
         * .com/upload/club/c/5/6/c59aeb16e9bb9a83da5699e9214ffc66.jpg","width":500,"height":334,
         * "cityname":"北京出发","lowprice":198,"tripdays":1,"totbatchs":1,"month_day":"2016年10月",
         * "catename":""},{"activity_id":6077,"activity_code":"GiBVEWoZ","title":"膜拜蜀山之王贡嘎神山",
         * "images":"http: //i3.lis99.com/upload/club/b/c/d/bcbf3364607a147678038dcb2c2f84bd
         * .jpg","width":800,"height":534,"cityname":"成都出发","lowprice":2180,"tripdays":5,
         * "totbatchs":1,"month_day":"2016年10月","catename":""}]
         */

        private int totalpage;
        private int total;
        private List<ActiveEntity> lists;


        public void setTotalpage(int totalpage) {
            this.totalpage = totalpage;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setLists(List<ActiveEntity> lists) {
            this.lists = lists;
        }


        public int getTotalpage() {
            return totalpage;
        }

        public int getTotal() {
            return total;
        }

        public List<ActiveEntity> getLists() {
            return lists;
        }

        public static class ActiveEntity {
            /**
             * activity_id : 6078
             * activity_code : hGQ5VWoZ
             * title : 稻城亚丁、海螺沟、新都桥行摄净心之旅
             * images : http: //i3.lis99.com/upload/club/2/0/8/203b20bf2f6959dafc9a2a6fa831f048.jpg
             * width : 800
             * height : 564
             * cityname : 成都出发
             * lowprice : 3480
             * tripdays : 8
             * totbatchs : 3
             * month_day : 2016年10月~2016年11月
             * catename : 亲子户外
             */

            private int activity_id;
            private String activity_code;
            private String title;
            private String images;
            private int width;
            private int height;
            private String cityname;
            private int lowprice;
            private int tripdays;
            private int totbatchs;
            private String month_day;
            private String catename;

            public void setActivity_id(int activity_id) {
                this.activity_id = activity_id;
            }

            public void setActivity_code(String activity_code) {
                this.activity_code = activity_code;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public void setCityname(String cityname) {
                this.cityname = cityname;
            }

            public void setLowprice(int lowprice) {
                this.lowprice = lowprice;
            }

            public void setTripdays(int tripdays) {
                this.tripdays = tripdays;
            }

            public void setTotbatchs(int totbatchs) {
                this.totbatchs = totbatchs;
            }

            public void setMonth_day(String month_day) {
                this.month_day = month_day;
            }

            public void setCatename(String catename) {
                this.catename = catename;
            }

            public int getActivity_id() {
                return activity_id;
            }

            public String getActivity_code() {
                return activity_code;
            }

            public String getTitle() {
                return title;
            }

            public String getImages() {
                return images;
            }

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public String getCityname() {
                return cityname;
            }

            public int getLowprice() {
                return lowprice;
            }

            public int getTripdays() {
                return tripdays;
            }

            public int getTotbatchs() {
                return totbatchs;
            }

            public String getMonth_day() {
                return month_day;
            }

            public String getCatename() {
                return catename;
            }
        }

}
