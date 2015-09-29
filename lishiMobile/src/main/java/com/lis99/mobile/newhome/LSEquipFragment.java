package com.lis99.mobile.newhome;


import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.LsBuyActivity;
import com.lis99.mobile.LsEquiCateActivity;
import com.lis99.mobile.R;
import com.lis99.mobile.club.ClubSpecialListActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.LsEquiFilterActivity;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LSEquipFragment extends LSFragment implements View.OnClickListener,PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {


    private static final int SHOW_CONTENT_LIST = 1001;
    private static final int SHOW_QUICK_ENTER = 1002;
    LSEquipAdapter adapter;
    private PullToRefreshView refreshView;
    private ListView listView;
    private ListView listView2;
    private View headerView;
    List<LSEquipContent> loadedContents = new ArrayList<LSEquipContent>();

    static HashMap<String,Integer[]> categories;

    static {
        categories = new HashMap<String, Integer[]>();
        categories.put("冲锋衣", new Integer[]{6, 1});
        categories.put("冲锋裤", new Integer[]{7, 1});
        categories.put("速干⾐", new Integer[]{12, 1});
        categories.put("速干裤", new Integer[]{13, 1});

        categories.put("抓绒衣", new Integer[]{10, 1});
        categories.put("软壳⾐", new Integer[]{8, 1});
        categories.put("高山鞋", new Integer[]{26, 1});
        categories.put("徒步鞋", new Integer[]{28, 1});

        categories.put("越野跑鞋", new Integer[]{137, 1});
        categories.put("攀岩鞋", new Integer[]{32, 1});
        categories.put("中型背包", new Integer[]{39, 1});
        categories.put("三季帐", new Integer[]{48, 1});

        categories.put("登⼭", new Integer[]{27, 0});
        categories.put("徒步", new Integer[]{18, 0});
        categories.put("越野跑", new Integer[]{24, 0});
        categories.put("⽇常", new Integer[]{31, 0});


        categories.put("更多装备", new Integer[]{0, 0});

    }

    public LSEquipFragment() {
        // Required empty public constructor
    }


    private View.OnClickListener categoryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
//            int id = 0;
            Integer[] id = new Integer[2];
            String name = textView.getText().toString();
            if (categories.containsKey(name)) {
                id = categories.get(name);
                if (id[0] == 0) {
                    Intent intent = new Intent(getActivity(), LsEquiFilterActivity.class);
//                    Intent intent = new Intent(getActivity(), LsXuanActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(),LsEquiCateActivity.class);
                    if(id[1] == 1 ){
                        intent.putExtra("cate", id[0]);
                    }else{
                        intent.putExtra("sport", id[0]);
                    }
                    intent.putExtra("title", name);
                    startActivity(intent);
                }

            }
        }
    };


    @Override
    protected void initViews(ViewGroup container) {
        super.initViews(container);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        body = inflater.inflate(R.layout.fragment_lsequip, container, false);
        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);

        listView = (ListView) findViewById(R.id.listView);


        headerView = inflater.inflate(R.layout.ls_equip_fragment_header, null);
        listView.addHeaderView(headerView);

        ViewGroup morePanel = (ViewGroup) headerView.findViewById(R.id.morePanel);
        addCategoryListenter(morePanel);

        View v = headerView.findViewById(R.id.welfarePanel);
        v.setOnClickListener(this);
        v = headerView.findViewById(R.id.evalPanel);
        v.setOnClickListener(this);
        v = headerView.findViewById(R.id.worthPanel);
        v.setOnClickListener(this);
        v = headerView.findViewById(R.id.outdoorPanel);
        v.setOnClickListener(this);

        /*
        adapter = new LSEquipAdapter(getActivity(), loadedContents);
        listView.setAdapter(adapter);
        */


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                LSEquipContent content = loadedContents.get(position);
                if (content.getType() == LSEquipContent.CHANGE_FOOTER) {
                    Intent intent = new Intent(getActivity(),LSWebActivity.class);
                    intent.putExtra("url", "http://m.lis99.com/club/integralshop/goodList");
                    startActivity(intent);
                }


            }
        });

        getEquipContents();
    }

    private void addCategoryListenter (ViewGroup rootView){
        int count = rootView.getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = rootView.getChildAt(i);
            if (v instanceof TextView) {
                v.setOnClickListener(categoryListener);
            } else if (v instanceof ViewGroup) {
                addCategoryListenter((ViewGroup)v);
            }
        }
    }

    public void getEquipContents() {
        Task task = new Task(null, C.EQUIP_CONTENTS, null,
                C.EQUIP_CONTENTS, this);
        publishTask(task, IEvent.IO);
    }

    @Override
    public void handleTask(int initiator, Task task, int operation)
    {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator)
        {
            case IEvent.IO:
                if (task.getData() instanceof byte[])
                {
                    result = new String((byte[]) task.getData());
                    if (((String) task.getParameter()).equals(C.EQUIP_CONTENTS))
                    {
                        parserContents(result);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void parserContents(String result)
    {
        try
        {
            Common.log("result =" + result);
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            if (!"OK".equals(errCode))
            {
                String msg = root.get("data").asText();
                postMessage(ActivityPattern1.POPUP_TOAST, msg);
                return;
            }
            JsonNode data = root.get("data");

            List<LSEquipContent> loadedContents = new ArrayList<LSEquipContent>();

            List<LSEquipContent> temp = LSFragment.mapper.readValue(data.get("freegoods")
                    .traverse(), new TypeReference<List<LSEquipContent>>() {
            });

            LSEquipContent content = new LSEquipContent();
            content.setType(LSEquipContent.FREE_HEADER);
            loadedContents.add(content);

            loadedContents.addAll(temp);

            content = new LSEquipContent();
            content.setType(LSEquipContent.FREE_FOOTER);
            loadedContents.add(content);

            temp = LSFragment.mapper.readValue(data.get("jfgoods")
                    .traverse(), new TypeReference<List<LSEquipContent>>() {
            });

            for (LSEquipContent tempContent : temp) {
                tempContent.setType(LSEquipContent.CHANGE_TYPE);
            }

            content = new LSEquipContent();
            content.setType(LSEquipContent.CHANGE_HEADER);
            loadedContents.add(content);

            loadedContents.addAll(temp);

            content = new LSEquipContent();
            content.setType(LSEquipContent.CHANGE_FOOTER);
            loadedContents.add(content);


            LSEquipFragment.this.loadedContents = loadedContents;

            postMessage(SHOW_CONTENT_LIST);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        if (msg.what == SHOW_CONTENT_LIST)
        {
            adapter = new LSEquipAdapter(getActivity(), loadedContents);
            listView.setAdapter(adapter);
            return true;
        }
        return super.handleMessage(msg);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        view.onFooterRefreshComplete();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        getEquipContents();
        view.onHeaderRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.welfarePanel:
            {
                intent = new Intent(getActivity(), ClubSpecialListActivity.class);
                intent.putExtra("tagid", 6);
                startActivity( intent );
            }
            break;
            case R.id.evalPanel:
            {
                intent = new Intent(getActivity(), ClubSpecialListActivity.class);
                intent.putExtra("tagid", 2);
                startActivity( intent );
            }
            break;
            case R.id.worthPanel:
            {
                intent = new Intent(getActivity(), ClubSpecialListActivity.class);
                intent.putExtra("tagid", 10);
                startActivity( intent );
            }
            break;
            case R.id.outdoorPanel:
            {
                intent = new Intent(getActivity(), LsBuyActivity.class);
                intent.putExtra("shoptype", "0");
                startActivity(intent);
            }
            break;

        }
    }
}
