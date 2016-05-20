package com.lis99.mobile.newhome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSClubMyTopicActivity;
import com.lis99.mobile.club.MyJoinClubActivity;
import com.lis99.mobile.club.apply.MyJoinActiveActivity;
import com.lis99.mobile.club.topicstrimg.DraftsListActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.LSCollectionActivity;
import com.lis99.mobile.entry.LsSettingActivity;
import com.lis99.mobile.entry.LsUserLikeActivity;
import com.lis99.mobile.mine.ActivityReplyMine;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.mine.LSMineApplyActivity;
import com.lis99.mobile.mine.LSMineApplyManageActivity;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.newhome.sysmassage.LSReceiveMassageActivity;
import com.lis99.mobile.newhome.sysmassage.SysMassageActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSScoreManager;
import com.lis99.mobile.util.RedDotUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class LSMineFragment extends LSFragment implements OnClickListener {

    DisplayImageOptions options;

    ImageView header;
    TextView nameView;
    boolean isVip;
    List<String> tags = new ArrayList<String>();
    List<TextView> tagViews = new ArrayList<TextView>();
    View vipView;

    View applyInfoDot;
    View replyDot;
    View applyManageDot;

    View managePanel;

    boolean haveApplyInfo;
    boolean haveApply;

    boolean haveReply;

    boolean isFounder;
    boolean isAdministrator;
    //系统消息
    boolean isSysMassage;
    //	我赞过的
    boolean likeStatus;

    private final static int SHOW_USER_INFO = 2001;
    private final static int SHOW_NOTICE = 2002;

    // ========

    LSTab tab;

//	=====3.6.0======

    private RelativeLayout layout_user;
    private RelativeLayout layout_friends;
    private ImageView titleRightImage, titleLeftImage;
    private boolean isAttention;

    private View v_friend_arrow, v_applyinfo_arrow, v_reply_arrow, v_applymanager_arrow, iv_friendDot,
//            我的福利
            v_benefit_arrow, iv_benefitDot,
//              我的优惠券
            v_coupon_arrow, iv_couponDot;

    private boolean isBenefit, isCoupon;

    //=====3.5.2=======
//	签到
    private Button btn_check_in;
    private View layout_market, layout_sys;
    private View iv_sysDot, v_sys_arrow, v_market_arrow;
    //商城积分
    private TextView tv_score;

    //	收到的赞
    private ImageView likeDot, v_like_arrow;

    private final int[] level_icon = new int[]{
            R.drawable.level_1, R.drawable.level_2, R.drawable.level_3, R.drawable.level_4, R.drawable.level_5,
            R.drawable.level_6, R.drawable.level_7, R.drawable.level_8, R.drawable.level_9, R.drawable.level_10,
            R.drawable.level_11, R.drawable.level_12, R.drawable.level_13, R.drawable.level_14, R.drawable.level_15,
            R.drawable.level_16, R.drawable.level_17, R.drawable.level_18, R.drawable.level_19, R.drawable.level_20,
            R.drawable.level_21, R.drawable.level_22, R.drawable.level_23, R.drawable.level_24, R.drawable.level_25,
            R.drawable.level_26, R.drawable.level_27, R.drawable.level_28, R.drawable.level_29, R.drawable.level_30,
    };

    private ImageView user_level;

    /**
     * points 积分
     * rank 等级
     *
     * @param tab
     */
    private int points, rank;

    private View layout_my_join, v_my_join_arrow, iv_joinDot;


    public void setTab(LSTab tab) {
        this.tab = tab;
    }

    private void buildOptions() {
        options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ls_nologin_header_icon)
                .showImageForEmptyUri(R.drawable.ls_nologin_header_icon)
                .showImageOnFail(R.drawable.ls_nologin_header_icon)
                .cacheInMemory(true).cacheOnDisk(true).build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildOptions();
    }

    public void refreshUser() {
        if (DataManager.getInstance().getUser().getUser_id() != null
                && !"".equals(DataManager.getInstance().getUser().getUser_id())) {
            String headicon = DataManager.getInstance().getUser().getHeadicon();
            ImageLoader.getInstance().displayImage(headicon, header, options);
            nameView.setText(DataManager.getInstance().getUser().getNickname());
            getUserInfoTask();
            //只有登陆后， 再获取红点。
            getNoticeDot();
        }
            else
        {
//            关闭Loading
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
            header.setImageResource(R.drawable.ls_nologin_header_icon);
            vipView.setVisibility(View.GONE);
            nameView.setText("登录");
//			tags.clear();
//			setTags();

            user_level.setVisibility(View.GONE);
            tv_score.setText("");

            haveApplyInfo = false;
            haveApply = false;

            haveReply = false;

            isFounder = false;
            isAdministrator = false;

            isAttention = false;

            isSysMassage = false;
//赞
            likeStatus = false;

            isBenefit = false;

            isCoupon = false;

            showOrHideViews();

        }
    }

    @Override
    protected void initViews(ViewGroup container) {
        super.initViews(container);


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        body = inflater.inflate(R.layout.fragment_mine, container, false);

        setTitle("我");

        View v = null;

        v = findViewById(R.id.likePanel);
        v.setOnClickListener(this);

        v = findViewById(R.id.collectionPanel);
        v.setOnClickListener(this);

        v = findViewById(R.id.topicPanel);
        v.setOnClickListener(this);

        v = findViewById(R.id.applyInfoPanel);
        v.setOnClickListener(this);

        v = findViewById(R.id.replyPanel);
        v.setOnClickListener(this);

        v = findViewById(R.id.applyManagePanel);
        v.setOnClickListener(this);
//		收到的赞
        v = findViewById(R.id.receivelike);
        v.setOnClickListener(this);
//		我加入的俱乐部
        v = findViewById(R.id.layout_join_club);
        v.setOnClickListener(this);
//      草稿箱
        v = findViewById(R.id.layout_drafts);
        v.setOnClickListener(this);
//      福利
        v = findViewById(R.id.layout_benefit);
        v.setOnClickListener(this);


//        优惠券
        v = findViewById(R.id.layout_coupon);
        v.setOnClickListener(this);


        managePanel = findViewById(R.id.managePanel);
        managePanel.setVisibility(View.GONE);

        header = (ImageView) findViewById(R.id.roundedImageView1);
        nameView = (TextView) findViewById(R.id.nameView);

        vipView = findViewById(R.id.vipStar);

        applyInfoDot = findViewById(R.id.applyInfoDot);
        applyManageDot = findViewById(R.id.applyManageDot);
        replyDot = findViewById(R.id.replyDot);

//		TextView tagView = (TextView) findViewById(R.id.tagTextView1);
//		tagViews.add(tagView);
//		tagView = (TextView) findViewById(R.id.tagTextView2);
//		tagViews.add(tagView);
//		tagView = (TextView) findViewById(R.id.tagTextView3);
//		tagViews.add(tagView);
//		tagView = (TextView) findViewById(R.id.tagTextView4);
//		tagViews.add(tagView);
//		tagView = (TextView) findViewById(R.id.tagTextView5);
//		tagViews.add(tagView);
//		tagView = (TextView) findViewById(R.id.tagTextView6);
//		tagViews.add(tagView);


//		=====3.6.0====

        layout_user = (RelativeLayout) findViewById(R.id.layout_user);
        layout_user.setOnClickListener(this);

        layout_friends = (RelativeLayout) findViewById(R.id.layout_friends);
        layout_friends.setOnClickListener(this);

        //设置按钮
        titleRightImage = (ImageView) findViewById(R.id.titleRightImage);
        titleRightImage.setImageResource(R.drawable.mine_icon_setting);
        titleRightImage.setOnClickListener(this);


        v_benefit_arrow = findViewById(R.id.v_benefit_arrow);
        iv_benefitDot = findViewById(R.id.iv_benefitDot);

        v_coupon_arrow = findViewById(R.id.v_coupon_arrow);
        iv_couponDot = findViewById(R.id.iv_couponDot);




        iv_friendDot = findViewById(R.id.iv_friendDot);

        v_friend_arrow = findViewById(R.id.v_friend_arrow);
        v_applyinfo_arrow = findViewById(R.id.v_applyinfo_arrow);
        v_applymanager_arrow = findViewById(R.id.v_applymanager_arrow);
        v_reply_arrow = findViewById(R.id.v_reply_arrow);

        //===3.5.3===
        btn_check_in = (Button) findViewById(R.id.btn_check_in);
        btn_check_in.setOnClickListener(this);

        layout_market = findViewById(R.id.layout_market);
        layout_sys = findViewById(R.id.layout_sys);
        iv_sysDot = findViewById(R.id.iv_sysDot);
        v_sys_arrow = findViewById(R.id.v_sys_arrow);
        v_market_arrow = findViewById(R.id.v_market_arrow);

//		收到的赞
        likeDot = (ImageView) findViewById(R.id.likeDot);
        v_like_arrow = (ImageView) findViewById(R.id.v_like_arrow);

        tv_score = (TextView) findViewById(R.id.tv_score);
        user_level = (ImageView) findViewById(R.id.user_level);

        layout_market.setOnClickListener(this);
        layout_sys.setOnClickListener(this);

        layout_my_join = findViewById(R.id.layout_my_join);
        layout_my_join.setOnClickListener(this);

        iv_joinDot = findViewById(R.id.iv_joinDot);
        v_my_join_arrow = findViewById(R.id.v_my_join_arrow);

        RedDotUtil.getInstance().setRedText(v_sys_arrow);

    }

    private void getUserInfoTask() {

//		postMessage(ActivityPattern1.POPUP_PROGRESS,
//				getString(R.string.sending));


        if (DataManager.getInstance().getUser().getUser_id() != null
                && !"".equals(DataManager.getInstance().getUser().getUser_id())) {
            Task task = new Task(null, C.USER_INFO_URL
                    + DataManager.getInstance().getUser().getUser_id(), null,
                    C.USER_INFO_URL, this);
            publishTask(task, IEvent.IO);

            // getNoticeDot();

        }
    }

    /**
     * 红点请求
     */
    public void getNoticeDot() {
        String userId = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        Task task = new Task(null, C.USER_NOTICE_TIPS_URL
                + DataManager.getInstance().getUser().getUser_id(), null,
                C.USER_NOTICE_TIPS_URL, this);
        publishTask(task, IEvent.IO);
    }

    @Override
    public void handleTask(int initiator, Task task, int operation) {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]) {
                    result = new String((byte[]) task.getData());
                    if (((String) task.getParameter()).equals(C.USER_INFO_URL)) {
                        parserUserInfo(result);
                    } else if (((String) task.getParameter())
                            .equals(C.USER_NOTICE_TIPS_URL)) {
                        parseNoticeTips(result);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void parseNoticeTips(String result) {
        try {
            Common.log("result =" + result);
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            if (!"OK".equals(errCode)) {
                String msg = root.get("data").asText();
                postMessage(ActivityPattern1.POPUP_TOAST, msg);
                return;
            }
            JsonNode data = root.get("data");
            haveApplyInfo = data.get("is_baoming").asBoolean();
            haveApply = data.get("manage_baoming").asBoolean();
            haveReply = data.get("is_reply").asBoolean();
            isFounder = data.get("is_creater").asBoolean();
            //关注
            isAttention = data.get("is_follow").asBoolean();
            isAdministrator = data.get("is_admin").asBoolean();
//系统消息
            isSysMassage = data.get("notice").asBoolean();
//我赞过的
            likeStatus = data.get("likeStatus").asBoolean();

            postMessage(SHOW_NOTICE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }
    }

    private void parserUserInfo(String result) {
        try {
            Common.log("result=" + result);
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            if (!"OK".equals(errCode)) {
                String msg = root.get("data").asText();
                postMessage(ActivityPattern1.POPUP_TOAST, msg);
                return;
            }
            JsonNode data = root.get("data").get("user");
            String vip = data.get("is_vip").asText();
            //保存用户VIP状态
            DataManager.getInstance().getUser().setIs_vip(vip);
            SharedPreferencesHelper.saveIsVip(vip);

            points = data.get("points").asInt();
            rank = data.get("rank").asInt();

            if ("1".equals(vip)) {
                isVip = true;
            } else {
                isVip = false;
            }
            // isVip = data.get("is_vip").asBoolean();
            if (data.has("tag")) {
                List<String> temp = LSFragment.mapper.readValue(data.get("tag")
                        .traverse(), new TypeReference<List<String>>() {
                });
                tags.clear();
                tags.addAll(temp);
            }
            postMessage(SHOW_USER_INFO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        postMessage(ActivityPattern1.POPUP_PROGRESS,
//                getString(R.string.sending));

        refreshUser();
    }

//	private void setTags()
//	{
//		for (int i = 0; i < tagViews.size(); ++i)
//		{
//			TextView tagView = tagViews.get(i);
//			if (tags.size() > i)
//			{
//				tagView.setVisibility(View.VISIBLE);
//				tagView.setText(tags.get(i));
//			} else
//			{
//				tagView.setVisibility(View.GONE);
//			}
//		}
//	}

    void showOrHideViews() {
        //箭头
        v_friend_arrow.setVisibility(isAttention ? View.GONE : View.VISIBLE);
        v_reply_arrow.setVisibility(haveReply ? View.GONE : View.VISIBLE);
        v_applyinfo_arrow.setVisibility(haveApplyInfo ? View.GONE : View.VISIBLE);
        v_applymanager_arrow.setVisibility(haveApply ? View.GONE : View.VISIBLE);
        v_sys_arrow.setVisibility(isSysMassage ? View.GONE : View.VISIBLE);
        v_like_arrow.setVisibility(likeStatus ? View.GONE : View.VISIBLE);
        v_my_join_arrow.setVisibility(haveApplyInfo ? View.GONE : View.VISIBLE);

        v_benefit_arrow.setVisibility( isBenefit ? View.GONE : View.VISIBLE);
        v_coupon_arrow.setVisibility( isCoupon ? View.GONE : View.VISIBLE);



        //红点
        iv_friendDot.setVisibility(isAttention ? View.VISIBLE : View.GONE);
        applyInfoDot.setVisibility(haveApplyInfo ? View.VISIBLE : View.GONE);
        applyManageDot.setVisibility(haveApply ? View.VISIBLE : View.GONE);
        replyDot.setVisibility(haveReply ? View.VISIBLE : View.GONE);
        managePanel.setVisibility((isFounder || isAdministrator) ? View.VISIBLE
                : View.GONE);
        iv_sysDot.setVisibility(isSysMassage ? View.VISIBLE : View.GONE);
        likeDot.setVisibility(likeStatus ? View.VISIBLE : View.GONE);
        iv_joinDot.setVisibility(haveApplyInfo ? View.VISIBLE : View.GONE);

        iv_benefitDot.setVisibility(isBenefit ? View.VISIBLE : View.GONE);
        iv_couponDot.setVisibility( isCoupon ? View.VISIBLE : View.GONE);

    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == SHOW_USER_INFO) {
            //
            int num = rank - 1;
            user_level.setVisibility(View.VISIBLE);
            user_level.setImageResource((num < 0 || num > 29) ? level_icon[0] : level_icon[num]);
            tv_score.setText(points + "积分");
            if (isVip) {
                vipView.setVisibility(View.VISIBLE);
            } else {
                vipView.setVisibility(View.GONE);
            }

//			setTags();

            return true;
        } else if (msg.what == SHOW_NOTICE) {
            showOrHideViews();
            return true;
        }
        return super.handleMessage(msg);
    }

    @Override
    public void onClick(View v) {
        String UserId = DataManager.getInstance().getUser().getUser_id();
        if (UserId != null
                && !"".equals(UserId)) {
            if (v.getId() == R.id.likePanel) {
                Intent intent = new Intent(getActivity(),
                        LsUserLikeActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.layout_user) {
                Common.goUserHomeActivit(getActivity(), UserId);
            } else if (v.getId() == R.id.collectionPanel) {
                Intent intent = new Intent(getActivity(),
                        LSCollectionActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.btn_check_in) {
                //				签到
                LSScoreManager instance = LSScoreManager.getInstance();
                instance.setCallBack(new CallBack() {
                    @Override
                    public void handler(MyTask mTask) {
                        //签到成功后的操作
                        Common.toast("今日签到成功");
                    }
                });

                instance.sendScore(LSScoreManager.sign, "0");

            } else if (v.getId() == R.id.layout_market) {
                int id = 0;
                String Userid = DataManager.getInstance().getUser().getUser_id();
                if (!TextUtils.isEmpty(Userid)) {
                    id = Integer.parseInt(Userid);
                }
//				商城
                Intent intent = new Intent(getActivity(), MyActivityWebView.class);
                Bundle bundle = new Bundle();
                bundle.putString("URL", "http://m.lis99.com/club/integralshop/goodList/" + id);
                bundle.putString("TITLE", "积分商城");
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (v.getId() == R.id.layout_sys) {
//				系统消息
                startActivity(new Intent(getActivity(), SysMassageActivity.class));
            }
//			else if (v.getId() == R.id.orderPanel)
//			{
//				Intent intent = new Intent(getActivity(),
//						LsUserOrderActivity.class);
//				startActivity(intent);
//			} else if (v.getId() == R.id.messagePanel)
//			{
//				Intent intent = new Intent(getActivity(),
//						LsUserMsgActivity.class);
//				startActivity(intent);
//			} else if (v.getId() == R.id.draftPanel)
//			{
//				Intent intent = new Intent(getActivity(),
//						LsUserDraftActivity.class);
//				startActivity(intent);
//				//设置
//			}
            else if (v.getId() == R.id.titleRightImage) {
                Intent intent = new Intent(getActivity(),
                        LsSettingActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.topicPanel) {
                Intent intent = new Intent(getActivity(),
                        LSClubMyTopicActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.replyPanel) {
                // replyDot.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(),
                        ActivityReplyMine.class);
                startActivity(intent);
            } else if (v.getId() == R.id.applyInfoPanel) {
                // applyInfoDot.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(),
                        LSMineApplyActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.applyManagePanel) {
                // applyManageDot.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(),
                        LSMineApplyManageActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.roundedImageView1) {
                Intent intent = new Intent(getActivity(), LSUserHomeActivity.class);
                intent.putExtra("userID", DataManager.getInstance().getUser().getUser_id());
                startActivity(intent);
            } else if (v.getId() == R.id.layout_friends) {
                Intent intent = new Intent(getActivity(),
                        MyFriendsActivity.class);
                startActivity(intent);
            }
            //收到的赞
            else if (v.getId() == R.id.receivelike) {
                Intent intent = new Intent(getActivity(),
                        LSReceiveMassageActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.layout_my_join) {
                Intent intent = new Intent(getActivity(),
                        MyJoinActiveActivity.class);
                startActivity(intent);
            }
//			我加入的俱乐部
            else if (v.getId() == R.id.layout_join_club) {
                startActivity(new Intent(getActivity(), MyJoinClubActivity.class));
            }
//            草稿箱
            else if ( v.getId() == R.id.layout_drafts )
            {
                startActivity(new Intent(getActivity(), DraftsListActivity.class));
            }
//            我的福利
            else if ( v.getId() == R.id.layout_benefit )
            {

            }
//            我的优惠券
            else if ( v.getId() == R.id.layout_coupon )
            {

            }
        }
//		UserId == null
        else {
//			if (v.getId() == R.id.layout_user )
//			{
//				Intent intent = new Intent(getActivity(), LSLoginActivity.class);
//				startActivity(intent);
//			}
//			else
            if (v.getId() == R.id.titleRightImage) {
                Intent intent = new Intent(getActivity(),
                        LsSettingActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.layout_market) {
//				商城
                int id = 0;
                String Userid = DataManager.getInstance().getUser().getUser_id();
                if (!TextUtils.isEmpty(Userid)) {
                    id = Integer.parseInt(Userid);
                }
//				商城
                Intent intent = new Intent(getActivity(), MyActivityWebView.class);
                Bundle bundle = new Bundle();
                bundle.putString("URL", "http://m.lis99.com/club/integralshop/goodList/" + id);
                bundle.putString("TITLE", "积分商城");
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), LSLoginActivity.class);
                startActivity(intent);
            }

        }
    }

}
