package com.lis99.mobile.equip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.ShopDetailActivity;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entity.item.Shop;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.adapter.LsBuyAdapter;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.constens;

import java.util.HashMap;
import java.util.List;

public class LSRelatedShopActivity extends LSBaseActivity implements View.OnClickListener,PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private PullToRefreshView refreshView;
    private ListView listView;

    private LsBuyAdapter<Shop> lsBuyAdapter;

    private Page page;

    private String Latitude1; // 纬度
    private String Longtitude1; // 经度

    private int brandID;

    private MyReciever myReciever;


    @Override
    protected void onDestroy() {
        stopService(new Intent("com.lis99.mobile.service.LocService"));
        unregisterReceiver(myReciever);
        super.onDestroy();
    }

    class MyReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String Latitude = (String) intent.getStringExtra("X");
            String Longtitude = (String) intent.getStringExtra("Y");

            stopService(new Intent("com.lis99.mobile.service.LocService"));

            if (Latitude != null && !"".equals(Latitude)) {
                Latitude1 = Latitude;
                Longtitude1 = Longtitude;
            }
            getList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brandID = getIntent().getIntExtra("brandID", 0);
        setContentView(R.layout.activity_lsrelated_shop);
        initViews();
        setTitle("相关店铺");

        startService(new Intent("com.lis99.mobile.service.LocService"));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lis99.mobile.loc");
        myReciever = new MyReciever();
        registerReceiver(myReciever, intentFilter);
    }

    @Override
    protected void initViews() {
        super.initViews();

        page = new Page();

        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);

        listView = (ListView) findViewById(R.id.listView);

        lsBuyAdapter = new LsBuyAdapter<Shop>(this, null);
        listView.setAdapter(lsBuyAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Shop shop = (Shop) lsBuyAdapter.getItem(arg2);
                Intent intent = new Intent(LSRelatedShopActivity.this,
                        ShopDetailActivity.class);
                String oid = shop.getOid();
                intent.putExtra(constens.OID, oid);
                intent.putExtra("fav", "ls");
                intent.putExtra("dis", shop.getDistance());
                startActivity(intent);
            }
        });

    }

    private void cleanList () {
        page = new Page();
        if ( lsBuyAdapter != null )
        {
            lsBuyAdapter.clearData();
        }
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


    private void parseShopList(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            if (!"OK".equals(errCode)) {
                errCode = "没有内容";
                postMessage(ActivityPattern1.POPUP_TOAST, errCode);
                return;
            }

            JsonNode data = root.get("data");
            page.setCount(data.get("totalpage").asInt());
            page.setPageNo(page.getPageNo()+1);
            final List<Shop> temp = LSFragment.mapper.readValue(data.get("shoplist").traverse(), new TypeReference<List<Shop>>() {
            });
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (lsBuyAdapter == null) {
                        lsBuyAdapter = new LsBuyAdapter<Shop>(LSRelatedShopActivity.this, temp);
                        listView.setAdapter(lsBuyAdapter);
                    } else {
                        lsBuyAdapter.addAll(temp);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Common.toast("没有内容");
        } finally {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }
    }


    @Override
    public void handleTask(int initiator, Task task, int operation) {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]) {
                    result = new String((byte[]) task.getData());
                    if (((String) task.getParameter()).equals(C.BRAND_SHOP)) {
                        parseShopList(result);
                    }
                }
                break;
            default:
                break;
        }
        postMessage(DISMISS_PROGRESS);
    }


    private void getList () {
        if ( page.isLastPage() )
        {
            Common.toast("没有内容了");
            return;
        }
        String url = C.BRAND_SHOP + page.pageNo;

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("brand_id", brandID);
        if (Latitude1 != null && !"".equals(Latitude1)) {
            params.put("latitude", Latitude1);
            params.put("longitude", Longtitude1);
        }

        Task task = new Task(null, url, C.HTTP_POST, C.BRAND_SHOP,
                this);
        task.setPostData(RequestParamUtil.getInstance(this)
                .getRequestParams(params).getBytes());
        publishTask(task, IEvent.IO);

    }


}
