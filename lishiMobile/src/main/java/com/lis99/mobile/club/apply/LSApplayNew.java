package com.lis99.mobile.club.apply;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ClubTopicGetApplyList;
import com.lis99.mobile.club.model.NewApplyUpData;
import com.lis99.mobile.club.widget.applywidget.MyApplyItem;
import com.lis99.mobile.util.Common;

import java.util.ArrayList;

/**
 * Created by yy on 15/11/18.
 */
public class LSApplayNew extends LSBaseActivity {

    private ListView list;

    private Button btn_ok, btn_add;

    private MyApplyItem adapter;

    private int topicID, clubID;
// 上传列表
    public static ArrayList<NewApplyUpData> updata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lsclub_new_apply_main);

        initViews();

        setTitle("报名");

        topicID = getIntent().getIntExtra("topicID", 0);
        clubID = getIntent().getIntExtra("clubID", 0);

        updata = new ArrayList<NewApplyUpData>();

        NewApplyUpData item = new NewApplyUpData();

        updata.add(item);

//        item = new NewApplyUpData();
//
//        item.address = "456";
//
//        updata.add(item);
//
//        String ss = ParserUtil.getGsonString(updata);
//
//        Common.log("ss="+ss);
//
//        ss = ParserUtil.getJsonArrayWithName("adfdf", ss);
//
//        Common.log("ss="+ss);




        getApplyList();

    }

        @Override
    protected void initViews() {
        super.initViews();

        list = (ListView) findViewById(R.id.list);

        btn_add = (Button) findViewById(R.id.btn_add);

        btn_ok = (Button) findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(this);

        btn_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);

        switch (arg0.getId())
        {
            case R.id.btn_ok:

                if ( !isOk() )
                {
                    Common.toast("还有一些信息未填写");
                    return;
                }
                Intent intent = new Intent(this, LSApplyEnterActivity.class);
                intent.putExtra("clubID", clubID);
                intent.putExtra("topicID", topicID);
                startActivity(intent);
                break;
            case R.id.btn_add:

                if ( !isOk() )
                {
                    Common.toast("还有一些信息未填写");
                    return;
                }

                if ( adapter == null ) return;

                NewApplyUpData item = new NewApplyUpData();

                updata.add(item);

                if ( adapter != null ) adapter.notifyDataSetChanged();

                break;
            default:
                break;
        }

    }


    /**
     *      填写信息是否填写完毕
     * @return
     */
    private boolean isOk ()
    {
        boolean ok = true;

        if ( adapter == null )
        {
            return ok;
        }

        for ( int i = 0; i < updata.size(); i++ )
        {
            NewApplyUpData item = updata.get(i);
            if ( listmodel.items.get(0).equals("1") && TextUtils.isEmpty(item.name))
            {
                ok = false;
                break;
            }
            else if ( listmodel.items.get(1).equals("1") && TextUtils.isEmpty(item.credentials) )
            {
                ok = false;
                break;
            }
            else if ( listmodel.items.get(2).equals("1") && TextUtils.isEmpty(item.sex) )
            {
                ok = false;
                break;
            }
            else if ( listmodel.items.get(3).equals("1") && TextUtils.isEmpty(item.mobile) )
            {
                ok = false;
                break;
            }
            else if ( listmodel.items.get(4).equals("1") && TextUtils.isEmpty(item.phone) )
            {
                ok = false;
                break;
            }
            else if ( listmodel.items.get(5).equals("1") && TextUtils.isEmpty(item.qq) )
            {
                ok = false;
                break;
            }
            else if ( listmodel.items.get(6).equals("1") && TextUtils.isEmpty(item.postaladdress) )
            {
                ok = false;
                break;
            }
            else if ( listmodel.items.get(7).equals("1") && TextUtils.isEmpty(item.address) )
            {
                ok = false;
                break;
            }

        }

        return ok;
    }

    private void goPayList ()
    {

    }

    private ClubTopicGetApplyList listmodel;


    private void getApplyList()
    {
        listmodel = new ClubTopicGetApplyList();

        listmodel.items = new ArrayList<String>();
        listmodel.items.add("1");
        listmodel.items.add("1");
        listmodel.items.add("1");
        listmodel.items.add("1");
        listmodel.items.add("0");
        listmodel.items.add("0");
        listmodel.items.add("1");
        listmodel.items.add("0");
        listmodel.items.add("0");

        adapter = new MyApplyItem(activity, updata);
//        设置显示属性
        adapter.setVisibleItem(listmodel.items);

        list.setAdapter(adapter);

//        MyRequestManager.getInstance().requestGet(
//                C.CLUB_TOPIC_APPLY_LIST + topicID, listmodel, new CallBack() {
//
//                    @Override
//                    public void handler(MyTask mTask) {
//                        // TODO Auto-generated method stub
//                        listmodel = (ClubTopicGetApplyList) mTask
//                                .getResultModel();
//                        if (listmodel.items == null) {
//                            listmodel.items = new ArrayList<String>();
//                            listmodel.items.add("1");
//                            listmodel.items.add("1");
//                            listmodel.items.add("0");
//                            listmodel.items.add("1");
//                            listmodel.items.add("0");
//                            listmodel.items.add("0");
//                            listmodel.items.add("1");
//                            listmodel.items.add("0");
//                            listmodel.items.add("0");
//                            // return;
//                        }
//
//
//                        adapter = new MyApplyItem(activity, updata);
////        设置显示属性
//                        adapter.setVisibleItem(listmodel.items);
//
//                        list.setAdapter(adapter);
//
//
//                    }
//                });

    }


    private void cleanList()
    {

    }



}
