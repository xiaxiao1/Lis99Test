package com.lis99.mobile.club.newtopic.series;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * Created by yy on 15/11/19.
 */
public class ApplySeriesManagerItem extends MyBaseAdapter {
//  0:已确认， 1：已拒绝， 2：待确认
    private int type, topicId;//, clubId;
    private ApplyManagerSeries main;
/*

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

*/

    public ApplySeriesManagerItem(Activity c, ArrayList listItem) {
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
            view = View.inflate(mContext, R.layout.apply_manager_list_item_series_new, null);
            holder = new Holder(view);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        final ApplyManagerSeriesModel.ApplylistEntity item = (ApplyManagerSeriesModel.ApplylistEntity) getItem(i);

        if (item == null) return view;

        setIv_pay_state(item.payType, item.payStatus, holder.iv_pay_state, holder.tv_pay_state);

        ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, ImageUtil.getclub_topic_headImageOptions());

        holder.tv_name.setText(item.nickname);



        /*备注*/
        if ( !TextUtils.isEmpty(item.remark))
        {
            holder.note_tv.setText(item.remark);
        }
        else
        {
            holder.note_tv.setText("无");
        }
        /*取消理由*/
        if (!TextUtils.isEmpty(item.reason)) {
            holder.cancelReason_tv.setText(item.reason);
            holder.cancelReason_ll.setVisibility(View.VISIBLE);
        } else {
            holder.cancelReason_ll.setVisibility(View.GONE);

        }


        if ( item.isVip == 1 )
        {
            holder.vipStar.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.vipStar.setVisibility(View.GONE);
        }

        /*订单的报名人信息*/
        ApplySeriesManagerItemAdapter adapter = new ApplySeriesManagerItemAdapter(mContext, item.applyinfoList);

        holder.list.setAdapter(adapter);

        holder.tv_wait.setVisibility(View.GONE);
        holder.bottombar.setVisibility(View.GONE);
        if (type == 0)
        {
            holder.bottombar.setVisibility(View.VISIBLE);
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
            holder.bottombar.setVisibility(View.VISIBLE);
            holder.btn_ok.setVisibility(View.VISIBLE);
            holder.btn_out.setVisibility(View.VISIBLE);
        }

        if ( item.payStatus == 0 )
        {
            holder.bottombar.setVisibility(View.VISIBLE);
            holder.btn_ok.setVisibility(View.GONE);
            holder.btn_out.setVisibility(View.GONE);
            holder.tv_wait.setVisibility(View.VISIBLE);
        }

        holder.btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LSRequestManager.getInstance().managerApplaySeriesPass(item.orderid, new CallBack() {
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

                                LSRequestManager.getInstance().managerApplaySeriesRefuse(item.orderid, new CallBack() {
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

        //出行时间
        holder.goTime_tv.setText(item.starttime);
        //报名时间
        holder.applyTime_tv.setText(item.createdate);
        //订单编号
        holder.orderNumber_tv.setText(item.ordercode);

        /*
        规格部分
        */
        //得到规格数量
        int guigeSize=item.orderbglist.size();
        StringBuffer guigeInfos = new StringBuffer();
        if (guigeSize == 0) {//没有规格则不显示展示区域
            holder.adultInfo_ll.setVisibility(View.GONE);
        } else {
            //遍历，组装规格信息，并展示
            holder.adultInfo_ll.setVisibility(View.VISIBLE);
            for (int gi=0;gi<guigeSize;gi++) {
                guigeInfos.append(item.orderbglist.get(gi)[0]);
                guigeInfos.append(" : ");
                guigeInfos.append(item.orderbglist.get(gi)[1]);
                guigeInfos.append(" × ");
                guigeInfos.append(item.orderbglist.get(gi)[2]);
                guigeInfos.append("\n");
            }
            guigeInfos.delete(guigeInfos.length() - 1, guigeInfos.length());
            holder.guige1_label_tv.setText(guigeInfos.toString());
        }
        /*if (guigeSize==1) {
            holder.guige1_label_tv.setText(item.orderbglist.get(0)[0]+" : ");
            holder.adult_tv.setText(item.orderbglist.get(0)[1]+" × "+item.orderbglist.get(0)[2]);

            holder.adultInfo_ll.setVisibility(View.VISIBLE);
            holder.childInfo_ll.setVisibility(View.GONE);
        } else if (guigeSize == 2) {
            holder.guige1_label_tv.setText(item.orderbglist.get(0)[0]+" : ");
            holder.adult_tv.setText(item.orderbglist.get(0)[1] + " × " + item.orderbglist.get(0)
                    [2]);

            holder.guige2_label_tv.setText(item.orderbglist.get(1)[0]+" : ");
            holder.child_tv.setText(item.orderbglist.get(1)[1] + " × " + item.orderbglist.get(1)
                    [2]);

            holder.adultInfo_ll.setVisibility(View.VISIBLE);
            holder.childInfo_ll.setVisibility(View.VISIBLE);
        } else {
            holder.adultInfo_ll.setVisibility(View.GONE);
            holder.childInfo_ll.setVisibility(View.GONE);
        }*/
        //总价
        holder.totalPrice_tv.setText(item.totprice);


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
        TextView tv_name;
        ImageView vipStar;
        ListView list;
        View bottombar;
        Button btn_out, btn_ok;

        ImageView iv_pay_state;
        TextView tv_pay_state;

        //支付中提示，
        TextView tv_wait;

        /*订单头部的信息*/
        TextView goTime_tv;
        TextView applyTime_tv;
        TextView orderNumber_tv;
        TextView guige1_label_tv;
        TextView guige2_label_tv;
        TextView adult_tv;
        TextView child_tv;
        TextView totalPrice_tv;
        TextView note_tv;
        TextView cancelReason_tv;
        LinearLayout cancelReason_ll;
        LinearLayout adultInfo_ll;
        LinearLayout childInfo_ll;

        public Holder(View view) {
             roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
             tv_name = (TextView) view.findViewById(R.id.tv_name);
             vipStar = (ImageView) view.findViewById(R.id.vipStar);
             list = (ListView) view.findViewById(R.id.list);
             bottombar = view.findViewById(R.id.bottombar);
             btn_ok = (Button) view.findViewById(R.id.btn_ok);
             btn_out = (Button) view.findViewById(R.id.btn_out);
             iv_pay_state = (ImageView) view.findViewById(R.id.iv_pay_state);
             tv_pay_state = (TextView) view.findViewById(R.id.tv_pay_state);

             tv_wait = (TextView) view.findViewById(R.id.tv_wait);

            goTime_tv = (TextView) view.findViewById(R.id.order_info_gotime_tv);
            applyTime_tv = (TextView) view.findViewById(R.id.order_info_applytime_tv);
            orderNumber_tv = (TextView) view.findViewById(R.id.order_info_ordernumber_tv);
//            adult_tv = (TextView) view.findViewById(R.id.order_info_adult_tv);
            child_tv = (TextView) view.findViewById(R.id.order_info_child_tv);
            guige1_label_tv = (TextView) view.findViewById(R.id.order_info_guige1_label_tv);
            guige2_label_tv = (TextView) view.findViewById(R.id.order_info_guige2_label_tv);
            totalPrice_tv = (TextView) view.findViewById(R.id.order_info_totalprice_tv);
            note_tv = (TextView) view.findViewById(R.id.order_info_note_tv);
            cancelReason_tv = (TextView) view.findViewById(R.id.order_info_cancelreason_tv);
            cancelReason_ll = (LinearLayout) view.findViewById(R.id.order_info_cancelreason_ll);
            adultInfo_ll = (LinearLayout) view.findViewById(R.id.order_info_adult_ll);
            childInfo_ll = (LinearLayout) view.findViewById(R.id.order_info_child_ll);
        }

    }

}
