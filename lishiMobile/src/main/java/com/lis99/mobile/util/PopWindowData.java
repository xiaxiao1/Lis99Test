package com.lis99.mobile.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 16/1/26.
 */
public class PopWindowData {

    /*活动筛选， 日期数据*/
    public static ArrayList<HashMap<String, String>> getActiveTime ()
    {
        final ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("select", "1");
        map.put("name", "全部开始日期");
        map.put("value", "0");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "1周内开始");
        map.put("value", "1");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "1-2周内开始");
        map.put("value", "2");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "2周-1个月内开始");
        map.put("value", "3");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "1个月以后开始");
        map.put("value", "4");
        alist.add(map);

        return alist;
    }


    /**活动筛选， 城市数据
    */
    public static ArrayList<HashMap<String, String>> getActiveCityList ()
    {
        final ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("select", "1");
        map.put("name", "安徽");
        map.put("value", "3");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "澳门");
        map.put("value", "34");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "北京");
        map.put("value", "2");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "重庆");
        map.put("value", "32");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "福建");
        map.put("value", "4");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "甘肃");
        map.put("value", "5");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "广东");
        map.put("value", "6");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "广西");
        map.put("value", "7");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "贵州");
        map.put("value", "8");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "海南");
        map.put("value", "9");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "河北");
        map.put("value", "10");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "河南");
        map.put("value", "11");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "黑龙江");
        map.put("value", "12");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "湖北");
        map.put("value", "13");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "湖南");
        map.put("value", "14");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "吉林");
        map.put("value", "15");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "江苏");
        map.put("value", "16");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "江西");
        map.put("value", "17");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "辽宁");
        map.put("value", "18");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "内蒙古");
        map.put("value", "19");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "宁夏");
        map.put("value", "20");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "青海");
        map.put("value", "21");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "山东");
        map.put("value", "22");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "山西");
        map.put("value", "23");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "陕西");
        map.put("value", "24");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "上海");
        map.put("value", "25");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "四川");
        map.put("value", "26");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "台湾");
        map.put("value", "35");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "天津");
        map.put("value", "27");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "西藏");
        map.put("value", "28");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "香港");
        map.put("value", "33");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "新疆");
        map.put("value", "29");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "云南");
        map.put("value", "30");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "浙江");
        map.put("value", "31");
        alist.add(map);

        return alist;
    }

}
