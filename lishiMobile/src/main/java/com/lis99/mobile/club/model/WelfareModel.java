package com.lis99.mobile.club.model;

import java.util.List;

/**
 * Created by xiaxiao on 2016/9/18.
 * 福利社页面的返回数据model
 */
public class WelfareModel extends BaseModel {

    /**
     * totalNum : 3
     * totPage : 1
     * freegoods : [{"id":"9431","title":"北斗--北斗盒子","content":"免费申请丨夜空中最亮的星","integral":"0",
     * "market_price":"3499","topicid":"1592156","category":"2","images":"http://i3.lis99
     * .com/upload/images/8/0/0/8019de75697e87236e1aaa5cd1819a80.jpg","height":200,"width":199},
     * {"id":"9459","title":"纳趣--软壳冲锋衣","content":"免费申请丨国货当自强！","integral":"0",
     * "market_price":"472","topicid":"1597275","category":"2","images":"http://i3.lis99
     * .com/upload/images/f/c/f/fca081128daf6488eae07a3a177e32cf.jpg","height":1280,
     * "width":1280},{"id":"9458","title":"纳趣--三合一冲锋衣","content":"免费申请丨国货当自强！","integral":"0",
     * "market_price":"1120","topicid":"1597275","category":"2","images":"http://i3.lis99
     * .com/upload/images/6/2/9/623e16e021af783177bb8f0fec519069.jpg","height":1280,"width":1280}]
     * jfgoods : [{"id":"9444","title":"砾石防晒头巾百变魔术头巾",
     * "content":"一件头巾多种用法，头带、围脖、帽子、面罩、头套随心随时变换，根据不同环境需求更换。超轻设计佩戴无负担，轻盈便于携带。\n零售价：39元",
     * "integral":"750","market_price":"39","topicid":"0","category":"0","images":"http://i3
     * .lis99.com/upload/images/4/8/2/48cdb68023e3474cff4b95907f80e4e2.jpg","height":200,
     * "width":200},{"id":"9443","title":"砾石保温杯500ml",
     * "content":"保温效力：6小时内（约）72℃、12小时（约）66℃\n容量：500ml\n材质：采用304＃食用不锈钢基材，健康水质，清新自然。\n零售价：149元",
     * "integral":"5215","market_price":"149","topicid":"0","category":"0","images":"http://i3
     * .lis99.com/upload/images/2/7/0/271fb4b575429e6d418b29d9b1e34b60.png","height":709,
     * "width":709}]
     */

    private int totalNum;
    private int totPage;
    /**
     * id : 9431
     * title : 北斗--北斗盒子
     * content : 免费申请丨夜空中最亮的星
     * integral : 0
     * market_price : 3499
     * topicid : 1592156
     * category : 2
     * images : http://i3.lis99.com/upload/images/8/0/0/8019de75697e87236e1aaa5cd1819a80.jpg
     * height : 200
     * width : 199
     */

    private List<FreegoodsBean> freegoods;
    /**
     * id : 9444
     * title : 砾石防晒头巾百变魔术头巾
     * content : 一件头巾多种用法，头带、围脖、帽子、面罩、头套随心随时变换，根据不同环境需求更换。超轻设计佩戴无负担，轻盈便于携带。
     零售价：39元
     * integral : 750
     * market_price : 39
     * topicid : 0
     * category : 0
     * images : http://i3.lis99.com/upload/images/4/8/2/48cdb68023e3474cff4b95907f80e4e2.jpg
     * height : 200
     * width : 200
     */

    private List<JfgoodsBean> jfgoods;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotPage() {
        return totPage;
    }

    public void setTotPage(int totPage) {
        this.totPage = totPage;
    }

    public List<FreegoodsBean> getFreegoods() {
        return freegoods;
    }

    public void setFreegoods(List<FreegoodsBean> freegoods) {
        this.freegoods = freegoods;
    }

    public List<JfgoodsBean> getJfgoods() {
        return jfgoods;
    }

    public void setJfgoods(List<JfgoodsBean> jfgoods) {
        this.jfgoods = jfgoods;
    }

    public static class FreegoodsBean {
        private String id;
        private String title;
        private String content;
        private String integral;
        private String market_price;
        private String topicid;
        private String category;
        private String images;
        private int height;
        private int width;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getTopicid() {
            return topicid;
        }

        public void setTopicid(String topicid) {
            this.topicid = topicid;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    public static class JfgoodsBean {
        private String id;
        private String title;
        private String content;
        private String integral;
        private String market_price;
        private String topicid;
        private String category;
        private String images;
        private int height;
        private int width;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getTopicid() {
            return topicid;
        }

        public void setTopicid(String topicid) {
            this.topicid = topicid;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}
