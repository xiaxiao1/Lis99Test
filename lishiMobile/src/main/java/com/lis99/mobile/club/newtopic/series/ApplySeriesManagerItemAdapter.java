package com.lis99.mobile.club.newtopic.series;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by xiaxaio on 2016.10.31.
 */
public class ApplySeriesManagerItemAdapter extends MyBaseAdapter {


    private String[] namesCode = new String[]{
            "name",
            "sex",
            "credentials",
            "mobile",
            "phone",
            "qq",
            "const",
            "postaladdress",
    };
    private String[] names = new String[]{
            "真实姓名",
            "性别",
            "身份证号",
            "手机号码",
            "紧急联系",
            "QQ",
            "应付费用",
            "邮寄地址",
            "居住城市",
    };
    public ApplySeriesManagerItemAdapter(Activity c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        HashMap<String, String> infos = (HashMap<String,String>)listItem.get(i);
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.apply_manager_list_item_orderitem, null );
            holder = new Holder(view);
            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        holder.applyInfoTitle_tv.setText("报名信息"+(i+1));
        StringBuffer stringBuffer = new StringBuffer();
        int keyNumber=namesCode.length;
        for (int index=0;index<keyNumber;index++) {
//            Common.Log_i(keyNumber+"  index:"+index);
            if (infos.containsKey(namesCode[index])) {
                stringBuffer.append(names[index]+" : "+infos.get(namesCode[index])+"\n");
            }
        }
        holder.realName_tv.setText(stringBuffer.toString());

        return view;
    }


    class Holder
    {
        TextView applyInfoTitle_tv;
        TextView realName_tv;
        /*
        * 因为返回字段数不明确 所以信息都组装在realName_tv字段中
        * */
        /*
        TextView sex_tv;
        TextView phoneNumber_tv;
        TextView IDNumber_tv;
        */

        public  Holder(View view) {
            applyInfoTitle_tv = (TextView) view.findViewById(R.id.order_info_applynumber_tv);
            realName_tv = (TextView) view.findViewById(R.id.order_info_realname_tv);
            /*
            sex_tv = (TextView) view.findViewById(R.id.order_info_sex_tv);
            phoneNumber_tv = (TextView) view.findViewById(R.id.order_info_phone_tv);
            IDNumber_tv = (TextView) view.findViewById(R.id.order_info_idnumber_tv);
            */
        }
    }

}
