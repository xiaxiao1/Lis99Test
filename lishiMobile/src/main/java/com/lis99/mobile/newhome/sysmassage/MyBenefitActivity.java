package com.lis99.mobile.newhome.sysmassage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.Page;

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

            }
        });


    }

    private void getList() {

    }

    private void cleanList() {
        page = new Page();
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

        public MyBenefitAdapter(Context c, List listItem) {
            super(c, listItem);
        }

        @Override
        public View setView(int i, View view, ViewGroup viewGroup) {

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

            if ( true )
            {
                holder.view_bg.setBackgroundResource(R.drawable.my_benefit_item_bg);
            }
            else
            {
                holder.view_bg.setBackgroundResource(R.drawable.my_benefit_item_bg_pass);
            }

            switch ( 0 )
            {
                case 1:
                    holder.btn_ok.setBackgroundResource(R.drawable.btn_benefit_have);
                    break;
                case 2:
                    holder.btn_ok.setBackgroundResource(R.drawable.btn_benefit_had);
                    break;
                case 3:
                    holder.btn_ok.setBackgroundResource(R.drawable.btn_benefit_pass);
                    break;
                default:
                    holder.btn_ok.setBackgroundResource(R.drawable.btn_benefit_have);
                    break;
            }

            switch ( 0 )
            {
                case 1:
                    holder.iv_icon.setImageResource(R.drawable.icon_benefit_integral);
                    break;
                case 2:
                    holder.iv_icon.setImageResource(R.drawable.icon_benefit_coupon_out);
                    break;
                case 3:
                    holder.iv_icon.setImageResource(R.drawable.icon_benefit_coupon_my);
                    break;
                case 4:
//                    ImageLoader.getInstance().displayImage();
                    break;
                default:
                    holder.btn_ok.setBackgroundResource(R.drawable.btn_benefit_have);
                    break;
            }





            holder.btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, MyBenefitAddAddress.class);
                    startActivity(intent);
                }
            });

            return view;
        }

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
