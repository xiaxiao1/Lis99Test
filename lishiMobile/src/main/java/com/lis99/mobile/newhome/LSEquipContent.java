package com.lis99.mobile.newhome;

/**
 * Created by zhangjie on 9/19/15.
 */
public class LSEquipContent {


    public static final int FREE_TYPE = 0;
    public static final int CHANGE_TYPE = 1;

    public static final int FREE_HEADER = 2;
    public static final int FREE_FOOTER = 3;
    public static final int CHANGE_HEADER = 4;
    public static final int CHANGE_FOOTER = 5;

    int id;
    String title;
    String content;
    int integral;
    String images;
    int height;
    int width;

    int type;


    public int getMarket_price() {
        return market_price;
    }

    public void setMarket_price(int market_price) {
        this.market_price = market_price;
    }

    public int market_price;

    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

    public int topicid;




    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
