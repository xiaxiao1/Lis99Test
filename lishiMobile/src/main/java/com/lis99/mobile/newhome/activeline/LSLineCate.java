package com.lis99.mobile.newhome.activeline;

import java.util.List;

/**
 * Created by zhangjie on 3/23/16.
 */
public class LSLineCate {

    List<LSLineCateItem> catelist;

    static class LSLineCateItem {
        public String name;
        public String images;
        public int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}
