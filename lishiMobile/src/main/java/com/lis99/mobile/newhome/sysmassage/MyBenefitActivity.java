package com.lis99.mobile.newhome.sysmassage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.BenefitListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yy on 16/5/18.
 * 我的福利
 */
public class MyBenefitActivity extends LSBaseActivity implements PullToRefreshView
        .OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private PullToRefreshView refreshView;
    private ListView listView;
    private Page page;

    private MyBenefitAdapter adapter;

    private BenefitListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_benefit);

        initViews();

        setTitle("我的福利");

        onHeaderRefresh(refreshView);


    }

    @Override
    protected void initViews() {
        super.initViews();

        listView = (ListView) findViewById(R.id.listView);

        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ( adapter == null ) return;
                BenefitListModel.BenefitItem item = (BenefitListModel.BenefitItem) adapter.getItem(position);
                if ( item == null ) return;

                Intent intent = null;
//                积分
                if ( item.type == 2 )
                {
                    intent = new Intent(activity, MyBenefitInfoActivity.class);
                    intent.putExtra("OBJECT", item);
                    startActivityForResult(intent, 999);
                }
//                装备
                else if ( item.type == 1 )
                {
                    intent = new Intent(activity, MyActivityWebView.class);

                    intent.putExtra("URL", C.MY_BENEFIT_INFO_M + item.id + "/" + item.welfare_id + "/" + Common.getUserId());

                    intent.putExtra("TITLE", "我的福利");

                    intent.putExtra("OBJECT", item);

//                    startActivity(intent);
                    startActivityForResult(intent, 999);
                }



            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode != RESULT_OK ) return;
        //刷新列表
        if ( requestCode == 999 )
        {
            onHeaderRefresh(refreshView);
        }

    }

    private void getList() {

        if ( page.isLastPage() )
        {
            return;
        }

        String userId = Common.getUserId();

        if (TextUtils.isEmpty(userId))
        {
            return;
        }

        String url = C.MY_BENEFIT_LIST + page.getPageNo();

        HashMap<String, Object> map = new HashMap<>();

        map.put("user_id", userId);

        model = new BenefitListModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {

                if ( mTask == null ) return;
                model = (BenefitListModel) mTask.getResultModel();
                if ( model == null ) return;

                page.nextPage();

                if ( adapter == null )
                {
                    page.setPageSize(model.welfarePage);
                    adapter = new MyBenefitAdapter(activity, model.lists);
                    listView.setAdapter(adapter);
                }
                else
                {
                    adapter.addList(model.lists);
                }



            }
        });




    }

    private void cleanList() {
        page = new Page();
        listView.setAdapter(null);
        if ( adapter != null )
            adapter.clean();
        adapter = null;
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshView.onFooterRefreshComplete();
        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();
        cleanList();
        getList();
    }

    class MyBenefitAdapter extends MyBaseAdapter {

        private BenefitListModel.BenefitItem currentItem;

        public MyBenefitAdapter(Activity c, List listItem) {
            super(c, listItem);
        }

        @Override
        public View setView(final int i, View view, ViewGroup viewGroup) {

            ViewHolder holder = null;
            if (view == null) {
                view = View.inflate(activity, R.layout.my_benefit_adapter, null);

                holder = new ViewHolder(view);

                view.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) view.getTag();
            }

            final BenefitListModel.BenefitItem item = (BenefitListModel.BenefitItem) getItem(i);

            if ( item == null ) return view;

            holder.tv_title.setText(item.title);
            holder.tv_content.setText(item.enddate);


            switch (item.flag )
            {
                case 1:
                    holder.view_bg.setBackgroundResource(R.drawable.my_benefit_item_bg_pass);
                    holder.btn_ok.setBackgroundResource(R.drawable.btn_benefit_had);
                    break;
                case 2:
                    holder.view_bg.setBackgroundResource(R.drawable.my_benefit_item_bg_pass);
                    holder.btn_ok.setBackgroundResource(R.drawable.btn_benefit_pass);
                    break;
                case 3:
                    holder.view_bg.setBackgroundResource(R.drawable.my_benefit_item_bg);
                    holder.btn_ok.setBackgroundResource(R.drawable.btn_benefit_have);
                    break;
            }

            switch ( item.type )
            {
                case 1:
                    ImageLoader.getInstance().displayImage(item.thumb, holder.iv_icon, ImageUtil.getDefultImageOptions());
                    break;
                case 2:
                    holder.iv_icon.setImageResource(R.drawable.icon_benefit_integral);
                    break;
                case 3:
                    holder.iv_icon.setImageResource(R.drawable.icon_benefit_coupon_out);
                    break;
                case 4:
                    holder.iv_icon.setImageResource(R.drawable.icon_benefit_coupon_my);
                    break;
                default:

                    break;
            }





            holder.btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ( item.flag != 3 ) return;

                    if ( item.type == 2  )
                    {
                        currentItem = item;

                        HashMap<String, Object> map = new HashMap<>();

                        String userId = Common.getUserId();

                        map.put("user_id", userId);
                        map.put("welfare_id", item.welfare_id);
                        map.put("type", item.type);
                        map.put("platfrom", "Android");

                        LSRequestManager.getInstance().getIntegral(map, callBack);
                    }
                    else if ( item.type == 1 )
                    {
                        Intent intent = new Intent(activity, MyBenefitAddAddress.class);
                        intent.putExtra("OBJECT", item);
                        startActivityForResult(intent, 999);
                    }



                }
            });

            return view;
        }


        CallBack callBack = new CallBack() {
            @Override
            public void handler(MyTask mTask) {

                if ( mTask  == null || mTask.getResultModel() == null )
                {
                    Common.toast("抱歉，领取积分失败，请稍后重试");
                    return;
                }

                String content = String.format("您已成功领取%s积分。", currentItem.content);
                DialogManager.getInstance().startAlert(activity, "领取积分", content, true, "确定", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentItem.flag = 1;
                        MyBenefitAdapter.this.notifyDataSetChanged();
                    }
                }, false, "", null);


            }
        };


        public class ViewHolder {
            public View rootView;
            public ImageView iv_icon;
            public TextView tv_title;
            public TextView tv_content;
            public Button btn_ok;
            public RelativeLayout view_bg;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.iv_icon = (ImageView) rootView.findViewById(R.id.iv_icon);
                this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
                this.tv_content = (TextView) rootView.findViewById(R.id.tv_content);
                this.btn_ok = (Button) rootView.findViewById(R.id.btn_ok);
                this.view_bg = (RelativeLayout) rootView.findViewById(R.id.view_bg);
            }


        }
    }


}
