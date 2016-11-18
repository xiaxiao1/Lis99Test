package com.lis99.mobile.club.newtopic.series;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.activityinfo.PayModel;
import com.lis99.mobile.club.model.ApplyContactsListModel;
import com.lis99.mobile.club.model.ClubTopicGetApplyList;
import com.lis99.mobile.club.model.NewApplyUpData;
import com.lis99.mobile.club.widget.applywidget.MyApplyItem;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ContactsUtil;
import com.lis99.mobile.util.MyRequestManager;

import java.util.ArrayList;

/**
 * Created by yy on 15/11/18.
 */
public class LSApplySeriesNew extends LSBaseActivity {

    private ListView list;

    private Button layout_btn_ok, layout_btn_add;

    private MyApplyItem adapter;

    private int topicID, clubID, batchID;
// 上传列表
    public ArrayList<NewApplyUpData> updata;

    //  选择联系人
    public final static int ADDCONTACTS = 998;

    private TextView tv_warning;

    private int joinPeople = 5;

    private PayModel payModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lsclub_apply_contcts_main);

        initViews();

        setTitle("填写报名信息");

        payModel = (PayModel) getIntent().getSerializableExtra("PAYMODEL");

        topicID = payModel.topicId;//getIntent().getIntExtra("topicID", 0);
        clubID = payModel.clubId;//getIntent().getIntExtra("clubID", 0);
        batchID = payModel.batchId;//getIntent().getIntExtra("batchID", 0);
        joinPeople = payModel.selectNum;//getIntent().getIntExtra("SELECTNUM", 1);

        tv_warning.setText("应填"+joinPeople+"人，请依据活动要求填写报名信息");

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
//        getContacts();

    }

        @Override
    protected void initViews() {
        super.initViews();

            tv_warning = (TextView) findViewById(R.id.tv_warning);

        list = (ListView) findViewById(R.id.list);

            layout_btn_add = (Button) findViewById(R.id.btn_add);

            layout_btn_ok = (Button) findViewById(R.id.btn_ok);

            layout_btn_ok.setOnClickListener(this);

            layout_btn_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);

        switch (arg0.getId())
        {
            case R.id.btn_ok:

                if ( !isOk() )
                {
//                    Common.toast("还有一些信息未填写");
                    return;
                }
                payModel.updata = updata;
                Intent intent = new Intent(this, LSApplySeriesEnterActivity.class);
//                intent.putExtra("clubID", clubID);
//                intent.putExtra("topicID", topicID);
//                intent.putExtra("batchID",batchID );
//                intent.putExtra("SELECTNUM", getIntent().getIntExtra("SELECTNUM", -1));
//                intent.putExtra("PRICE", getIntent().getLongExtra("PRICE", -1));
                intent.putExtra("PAYMODEL", payModel);
                startActivityForResult(intent, 999);
                break;
            case R.id.btn_add:

                if ( !isOk() )
                {
//                    Common.toast("还有一些信息未填写");
                    return;
                }

                if ( updata.size() >= joinPeople )
                {
                    Common.toast("您好，报名人员数量不能多与规格数量");
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

//  获取常用联系人
    private void getContacts ()
    {
        ContactsUtil.getInstance().getContactsList(new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                ApplyContactsListModel model = (ApplyContactsListModel) mTask.getResultModel();
                if ( model == null || model.user_list == null || model.user_list.size() == 0 )
                {

                }
                else {
//                    NewApplyUpData info = model.user_list.get(0);
                    NewApplyUpData info = setInfo(listmodel.items, model.user_list.get(0));
                    if ( updata.size() == 0 )
                    {
                        updata.add(info);
                    }
                    else
                    {
                        updata.set(0, info);
                    }
                    if ( adapter != null ) adapter.setList(updata);
                }
//                getApplyList();
            }

            @Override
            public void handlerError(MyTask mTask) {
//                getApplyList();
            }
        });
    }

    private void getApplyList()
    {

        listmodel = new ClubTopicGetApplyList();

        String url = C.ACTIVE_SERIES_APPLY_LIST;



        MyRequestManager.getInstance().requestGet(
                url + topicID, listmodel, new CallBack() {

                    @Override
                    public void handlerError(MyTask mTask) {
                        super.handlerError(mTask);
                        getContacts();
                    }

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

                        getContacts();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        updata.clear();
    }

    private void cleanList()
    {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        updata.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//      退出
        if ( requestCode == 999 && resultCode == RESULT_OK )
        {
            setResult(RESULT_OK);
            finish();
//            updata.clear();
        }
//        选择报名人
        else if ( requestCode == ADDCONTACTS && resultCode == RESULT_OK )
        {
            NewApplyUpData info = (NewApplyUpData) data.getSerializableExtra("INFO");

            int position = data.getIntExtra("POSITION", -1);
            updata.set(position, setInfo(listmodel.items, info));

            if ( adapter != null )
            {
                adapter.setList(updata);
            }
        }
    }

    /**
     *       从常用联系人里选择， 判断哪些信息需要赋值
     * @param item
     * @param info
     * @return
     */
    private NewApplyUpData setInfo ( ArrayList<String> item, NewApplyUpData info )
    {
        if ( item == null ) return info;
        NewApplyUpData newInfo = new NewApplyUpData();

        //控制显示哪个选项      姓名
        if ("0".equals(item.get(0)))
        {
        } else
        {
            newInfo.name = info.name;
        }
        //        身份证号
        if ("0".equals(item.get(1)))
        {

        } else
        {
            newInfo.credentials = info.credentials;
        }
//        性别
        if ("0".equals(item.get(2)))
        {
        } else
        {
            newInfo.sex = info.sex;
        }
//        手机号
        if ("0".equals(item.get(3)))
        {
        } else
        {
            newInfo.mobile = info.mobile;
        }
        return newInfo;
    }


}
