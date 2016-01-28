package com.lis99.mobile.club.apply;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ClubTopicGetApplyList;
import com.lis99.mobile.club.model.NewApplyUpData;
import com.lis99.mobile.club.widget.applywidget.MyApplyItem;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;

import java.util.ArrayList;

/**
 * Created by yy on 15/11/18.
 */
public class LSApplayNew extends LSBaseActivity {

    private ListView list;

    private RelativeLayout layout_btn_ok, layout_btn_add;

    private MyApplyItem adapter;

    private int topicID, clubID;
// 上传列表
    public static ArrayList<NewApplyUpData> updata;
//    从哪里来
    private String TYPE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lsclub_new_apply_main);

        initViews();

        setTitle("报名");

        topicID = getIntent().getIntExtra("topicID", 0);
        clubID = getIntent().getIntExtra("clubID", 0);

        TYPE = getIntent().getStringExtra("TYPE");

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

            layout_btn_add = (RelativeLayout) findViewById(R.id.layout_btn_add);

            layout_btn_ok = (RelativeLayout) findViewById(R.id.layout_btn_ok);

            layout_btn_ok.setOnClickListener(this);

            layout_btn_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);

        switch (arg0.getId())
        {
            case R.id.layout_btn_ok:

                if ( !isOk() )
                {
//                    Common.toast("还有一些信息未填写");
                    return;
                }
                Intent intent = new Intent(this, LSApplyEnterActivity.class);
                intent.putExtra("clubID", clubID);
                intent.putExtra("topicID", topicID);
                intent.putExtra("TYPE", TYPE);
                startActivityForResult(intent, 999);
                break;
            case R.id.layout_btn_add:

                if ( !isOk() )
                {
//                    Common.toast("还有一些信息未填写");
                    return;
                }

                if ( updata.size() >= 5 )
                {
                    Common.toast("您好，一次最多报名五人,如有特殊需求请联系领队");
                    return;
                }

                if ( adapter == null ) return;

                NewApplyUpData item = new NewApplyUpData();

                updata.add(item);

                if ( adapter != null ) adapter.notifyDataSetChanged();

                list.setSelection(updata.size() - 1);

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
            return false;
        }

        for ( int i = 0; i < updata.size(); i++ )
        {
            NewApplyUpData item = updata.get(i);
            if ( listmodel.items.get(0).equals("1") && TextUtils.isEmpty(item.name))
            {
                ok = false;
                Common.toast("姓名不能为空");
                return false;
            }
            else if ( listmodel.items.get(1).equals("1") && TextUtils.isEmpty(item.credentials) )
            {
                ok = false;
                Common.toast("身份证号码不能为空");
                return false;
            }
            else if ( listmodel.items.get(2).equals("1") && TextUtils.isEmpty(item.sex) )
            {
                ok = false;
//                Common.toast("身份证号码不能为空");
                return false;
            }
            else if ( listmodel.items.get(3).equals("1") && TextUtils.isEmpty(item.mobile) )
            {
                ok = false;
                Common.toast("手机号不能为空");
                return false;
            }
            else if ( listmodel.items.get(4).equals("1") && TextUtils.isEmpty(item.phone) )
            {
                ok = false;
                Common.toast("紧急联系电话不能为空");
                return false;
            }
            else if ( listmodel.items.get(5).equals("1") && TextUtils.isEmpty(item.qq) )
            {
                ok = false;
                Common.toast("QQ不能为空");
                return false;
            }
            else if ( listmodel.items.get(7).equals("1") && TextUtils.isEmpty(item.postaladdress) )
            {
                ok = false;
                Common.toast("邮寄地址不能为空");
                return false;
            }
            else if ( listmodel.items.get(8).equals("1") && TextUtils.isEmpty(item.address) )
            {
                ok = false;
                Common.toast("地址不能为空");
                return false;
            }
            else if ( listmodel.items.get(3).equals("1") && item.mobile.length() != 11 )
            {
                ok = false;
                Common.toast("手机号码格式错误");
                return false;
            }
            else if (listmodel.items.get(1).equals("1") && !TextUtils.isEmpty(item.credentials) )
            {
                if (item.credentials.length() != 18)
                {
                    ok = false;
                    Common.toast("身份证号位数不正确");
                    return false;
                }

                if (!item.credentials.matches("[0-9]{18}") && !item.credentials.matches("[0-9]{17}(X|x)")
                        && listmodel.items.get(1).equals("1"))
                {
                    ok = false;
                    Common.toast("身份证号格式不正确");
                    return false;
                }
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
//        listmodel = new ClubTopicGetApplyList();
//
//        listmodel.items = new ArrayList<String>();
//        listmodel.items.add("1");
//        listmodel.items.add("1");
//        listmodel.items.add("1");
//        listmodel.items.add("1");
//        listmodel.items.add("0");
//        listmodel.items.add("0");
//        listmodel.items.add("1");
//        listmodel.items.add("0");
//        listmodel.items.add("0");
//
//        adapter = new MyApplyItem(activity, updata);
////        设置显示属性
//        adapter.setVisibleItem(listmodel.items);
//
//        list.setAdapter(adapter);

        listmodel = new ClubTopicGetApplyList();

        MyRequestManager.getInstance().requestGet(
                C.CLUB_TOPIC_APPLY_LIST + topicID, listmodel, new CallBack() {

                    @Override
                    public void handler(MyTask mTask) {
                        // TODO Auto-generated method stub
                        listmodel = (ClubTopicGetApplyList) mTask
                                .getResultModel();
                        if (listmodel.items == null) {
                            listmodel.items = new ArrayList<String>();
//                            姓名
                            listmodel.items.add("1");
//                            身份证号
                            listmodel.items.add("0");
//                            性别
                            listmodel.items.add("0");
//                            手机号
                            listmodel.items.add("0");
//                            紧急电话
                            listmodel.items.add("0");
//                            QQ
                            listmodel.items.add("0");
//                              没用
                            listmodel.items.add("0");

                            //                            邮寄地址
                            listmodel.items.add("0");
                            //                            居住城市
                            listmodel.items.add("0");
                            // return;
                        }


                        adapter = new MyApplyItem(activity, updata);
//        设置显示属性
                        adapter.setVisibleItem(listmodel.items);

                        list.setAdapter(adapter);


                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updata.clear();
    }

    private void cleanList()
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == 999 && resultCode == RESULT_OK )
        {
            setResult(RESULT_OK);
            finish();
        }
    }
}
