package com.lis99.mobile.util;

import com.lis99.mobile.club.model.ActiveMainCityListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 16/1/26.
 */
public class PopWindowData {

    /*活动筛选， 日期数据
    * 大本营（48）、徒步露营（284）、户外摄影（342）、极限攀登（349）、装备控（285）
    * */
    public static ArrayList<HashMap<String, String>> getTopicClub ()
    {
        final ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("select", "1");
        map.put("name", "户外范");
        map.put("value", "48");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "徒步露营");
        map.put("value", "284");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "户外摄影");
        map.put("value", "342");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "极限攀登");
        map.put("value", "349");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "装备控");
        map.put("value", "285");
        alist.add(map);

        if ( "ttest".equals(DeviceInfo.CHANNELVERSION))
        {
            map = new HashMap<String, String>();

            map.put("select", "0");
            map.put("name", "测试俱乐部");
            map.put("value", "190");
            alist.add(map);
        }

        return alist;
    }

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

//  线路活动筛选
    public static ArrayList<HashMap<String, String>> getActiveCityList ()
    {
        final ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("select", "1");
        map.put("name", "全部归属地");
        map.put("value", "0");
        alist.add(map);

        alist.addAll(getActiveMainCityList());

        map = alist.get(1);

        map.put("select", "0");

        return alist;

    }

    //  线路活动城市
    public static ArrayList<HashMap<String, String>> getActiveCityListMain (String cityName, String cityId)
    {
        final ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("select", "1");
        map.put("name", cityName + "  GPS定位");
        map.put("value", cityId);
        alist.add(map);

        alist.addAll(getActiveMainCityListMain());

        return alist;

    }

    /**活动筛选， 城市数据
    */
    public static ArrayList<HashMap<String, String>> getActiveMainCityList ()
    {
        final ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("select", "1");
        map.put("name", "安徽");
        map.put("value", "3");
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


    private static ActiveMainCityListModel activeMClist;

    public static void getActiveMainCityList ( final String cityName, final String cityId, final CallBack callBack )
    {

        // 如果列表获取回来后
        if ( activeMClist != null && activeMClist.citylist != null && activeMClist.citylist.size() != 0 && callBack != null )
        {
            MyTask myTask = new MyTask();
            myTask.setResultModel(activeMClist);
            callBack.handler(myTask);
            return;
        }

        activeMClist = new ActiveMainCityListModel();

        MyRequestManager.getInstance().requestGet("http://api.lis99.com/v5/club/provinceinfo", activeMClist, new CallBack() {

            @Override
            public void handler(MyTask mTask) {

                if ( mTask != null && callBack != null )
                {
                    activeMClist = (ActiveMainCityListModel) mTask.getResultModel();

                        ActiveMainCityListModel.CitylistEntity item = new ActiveMainCityListModel
                                .CitylistEntity();
                        item.id = cityId;
                        item.name = cityName + "  GPS定位";
                        item.select = "0";
                        activeMClist.citylist.add(0, item);
                    callBack.handler(mTask);
                }


            }
        });

    }

    /**活动筛选， 城市数据
     */
    public static ArrayList<HashMap<String, String>> getActiveMainCityListMain ()
    {
        final ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "北京");
        map.put("value", "2");
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
        map.put("name", "吉林");
        map.put("value", "15");
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
        map.put("name", "天津");
        map.put("value", "27");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "新疆");
        map.put("value", "29");
        alist.add(map);

        return alist;
    }


}
