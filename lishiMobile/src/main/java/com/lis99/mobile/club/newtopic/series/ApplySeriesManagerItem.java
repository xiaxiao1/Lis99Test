package com.lis99.mobile.club.newtopic.series;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.apply.MyJoinActiveInfoActivity;
import com.lis99.mobile.club.newtopic.series.model.ApplyManagerSeriesModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/11/19.
 */
public class ApplySeriesManagerItem extends MyBaseAdapter {
//  0:已确认， 1：已拒绝， 2：待确认
    private int type, topicId;//, clubId;
    private ApplyManagerSeries main;

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


    public ApplySeriesManagerItem(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    public void setType ( int t, int tId, ApplyManagerSeries mMain )
    {
        type = t;
//        clubId = cId;
        topicId = tId;
        main = mMain;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.apply_manager_list_item, null);
            holder = new Holder();

            holder.roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_data = (TextView) view.findViewById(R.id.tv_data);
//            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.vipStar = (ImageView) view.findViewById(R.id.vipStar);
            holder.list = (ListView) view.findViewById(R.id.list);
            holder.bottombar = view.findViewById(R.id.bottombar);
            holder.btn_ok = (Button) view.findViewById(R.id.btn_ok);
            holder.btn_out = (Button) view.findViewById(R.id.btn_out);
            holder.iv_pay_state = (ImageView) view.findViewById(R.id.iv_pay_state);
            holder.tv_pay_state = (TextView) view.findViewById(R.id.tv_pay_state);
            holder.view_wait = view.findViewById(R.id.view_wait);


            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        final ApplyManagerSeriesModel.ApplylistEntity item = (ApplyManagerSeriesModel.ApplylistEntity) getItem(i);

        if (item == null) return view;

        setIv_pay_state(item.payType, item.payStatus, holder.iv_pay_state, holder.tv_pay_state);

//        holder.tv_title.setText("报名信息" + (i + 1));
        ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, ImageUtil.getclub_topic_headImageOptions());

        holder.tv_name.setText(item.nickname);

        holder.tv_data.setText("报名时间：" + item.createdate);

        if ( item.isVip == 1 )
        {
            holder.vipStar.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.vipStar.setVisibility(View.GONE);
        }

        ArrayList<Object> list = new ArrayList<Object>();

        for ( int n = 0; n < item.applyinfoList.size(); n++ )
        {

            list.add("报名信息" + (n + 1));
            HashMap<String, String> map = item.applyinfoList.get(n);

            ArrayList<HashMap<String, String>> ilist = new ArrayList<HashMap<String, String>>();

            for ( int m = 0; m < namesCode.length; m++ )
            {
                String nameC = namesCode[m];

                if ( map.containsKey(nameC))
                {
                    String nameS = names[m];
                    if ( m == 1 && ilist.size() > 0 )
                    {
                        String str = ilist.get(0).get("value");
                        str = str + "（"+map.get(nameC)+"）";
                        ilist.get(0).put("value", str);
                        continue;
                    }
                    HashMap<String, String> hmap = new HashMap<String, String>();
                    hmap.put("name", nameS);
                    hmap.put("value", map.get(nameC));
                    ilist.add(hmap);
                }
            }

            list.add(ilist);
        }

        ApplySeriesManagerItem2 adapter = new ApplySeriesManagerItem2(mContext, list);

        holder.list.setAdapter(adapter);

        holder.view_wait.setVisibility(View.GONE);

        if (type == 0)
        {
            holder.btn_ok.setVisibility(View.GONE);
            holder.btn_out.setVisibility(View.VISIBLE);

        }
        else if ( type == 1 )
        {
            holder.btn_ok.setVisibility(View.GONE);
            holder.btn_out.setVisibility(View.GONE);
        }
        else
        {
            holder.btn_ok.setVisibility(View.VISIBLE);
            holder.btn_out.setVisibility(View.VISIBLE);
        }

        if ( item.payStatus == 0 )
        {
            holder.btn_ok.setVisibility(View.GONE);
            holder.btn_out.setVisibility(View.GONE);
            holder.view_wait.setVisibility(View.VISIBLE);
        }

        holder.btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LSRequestManager.getInstance().managerApplaySeriesPass(topicId, new CallBack() {
                    @Override
                    public void handler(MyTask mTask) {
                        main.onHeaderRefresh(null);
                    }
                });
            }
        });

        holder.btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(mContext).setTitle("据绝此报名")
                        .setMessage("确认拒绝此报名？")
                        .setNegativeButton("返回", null)
                        .setPositiveButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                LSRequestManager.getInstance().managerApplaySeriesRefuse(topicId, new CallBack() {
                                    @Override
                                    public void handler(MyTask mTask) {
                                        main.onHeaderRefresh(null);
                                    }
                                });
                            }
                        })
                        .create().show();

            }
        });


        return view;
    }


    public void setIv_pay_state ( int type, int status, ImageView iv_pay_state, TextView tv_pay_state )
    {
        switch ( type )
        {
            case 0:
                iv_pay_state.setImageResource(R.drawable.pay_free);
                break;
            case 1:
                iv_pay_state.setImageResource(R.drawable.pay_off_line);
                break;
            case 2:
                iv_pay_state.setImageResource(R.drawable.pay_weixin);
                break;
            case 3:
                iv_pay_state.setImageResource(R.drawable.pay_zhifubao);
                break;
        }



        tv_pay_state.setText((status >= 0 && status < MyJoinActiveInfoActivity.PAY_TYPE.length) ? MyJoinActiveInfoActivity.PAY_TYPE[status] : "");

    }


    class Holder
    {
        RoundedImageView roundedImageView1;
        TextView tv_name, tv_data;
        ImageView vipStar;
        TextView tv_title;
        ListView list;
        View bottombar;
        Button btn_out, btn_ok;

        ImageView iv_pay_state;
        TextView tv_pay_state;

        View view_wait;

    }

}
