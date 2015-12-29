package com.lis99.mobile.newhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.lis99.mobile.LsBuyActivity;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.VersionBean;
import com.lis99.mobile.choiceness.FragmentChoicenessNewMain;
import com.lis99.mobile.club.LSClubFragment;
import com.lis99.mobile.club.LSClubFragmentNew;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.model.PushModel;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.LsSettingActivity;
import com.lis99.mobile.entry.LsUserDraftActivity;
import com.lis99.mobile.entry.LsUserLikeActivity;
import com.lis99.mobile.entry.LsUserMsgActivity;
import com.lis99.mobile.entry.LsUserOrderActivity;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.SlidingMenuView;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.PushManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class NewHomeActivity extends ActivityPattern1 implements OnItemClickListener {

    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                postMessage(POPUP_TOAST, "百度地图 key 验证出错! ");
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                postMessage(POPUP_TOAST, "网络出错 ");
            }
        }
    }

    private SDKReceiver mReceiver;

    private static final int HAVE_NEW_VERSION = 201;

    private VersionBean vb;

    private SlidingMenuView menu_view;
    private View layout_left;
    private View layout_right;
    private ListView lv_set;
    LSTab tab;

    private int MAX_WIDTH = 0;
    private final static int SPEED = 15;
    private int window_width;
    private boolean hasMeasured = false;
    private final static int sleep_time = 5;

    private TextView ls_login_text, ls_user_nickname, ls_user_point;// 立即登录
    private LinearLayout ls_login_ll;
    private ImageView iv_set, ls_nologin_header;// 图片
    AsyncLoadImageView ls_header;
    private LinearLayout ll_point;
    Bitmap bmp;

    String login = "";

    private LSFragment content = new LSFragment();
    private LSFragment selectFragment;
    private LSFragment equiFragment;
    private LSClubFragment clubFragment;
    private LSShakeFragment saleFragment;
    private LSMineFragment mineFragment;
//    private FragmentChoiceness choicenessFragment;
    private FragmentChoicenessNewMain choicenessNewMain;
    private LSClubFragmentNew clubFragmentNew;

    public static boolean CLOSEAPPLICATION = false;


    public void gotoShop(String shopType) {
        Intent intent = new Intent(this, LsBuyActivity.class);
        intent.putExtra("shoptype", shopType);
        startActivity(intent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LSTab.SELECT:
                    if (equiFragment == null) {
                        equiFragment = new LSEquipFragment();
                    }
                    switchContent(equiFragment);
                    break;
                case LSTab.EQUI:
                    if (equiFragment == null)
//					equiFragment = new LSEquiFragment();
//                        switchContent(equiFragment);
                    break;
                case LSTab.CLUB:
//                    if (clubFragment == null) {
//                        clubFragment = new LSClubFragment();
//                    }
//                    switchContent(clubFragment);
                    if ( clubFragmentNew == null )
                    {
                        clubFragmentNew = new LSClubFragmentNew();
                    }
                    clubFragmentNew.handler();
                    switchContent(clubFragmentNew);
                    break;
                case LSTab.CHOICENESS:
                    if ( choicenessNewMain == null )
                    {
                        choicenessNewMain = new FragmentChoicenessNewMain();
                    }
                    choicenessNewMain.handler();
                    switchContent(choicenessNewMain);
                    break;
                case LSTab.EVENT:
                    if (mineFragment == null) {
                        mineFragment = new LSMineFragment();
                    } else {
                        //每次都获取用户信息
                        mineFragment.refreshUser();
                    }
                    //获取红点
//                    mineFragment.getNoticeDot();
                    tab.visibleRedDot(false);
                    switchContent(mineFragment);
                    break;
                default:
                    break;
            }
        }
    };

    public void switchContent(LSFragment to) {
        if (content != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(content).add(R.id.content_frame, to).commit();
            } else {
                transaction.hide(content).show(to).commit();
            }
            content = to;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (content == saleFragment) {
            if (saleFragment.onKeyDown(keyCode, event)) {
                return true;
            }
        }

        //点击返回
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!Common.exitApp()) {
                Toast.makeText(NewHomeActivity.this, "再次点击退出", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                NewHomeActivity.this.finish();
                System.exit(0);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        StatusUtil.setStatusBar(this);

        login = getIntent().getStringExtra("login");

        initViews();
        initListeners();
        if ( !Common.isBDUpdata() )
        {
//        检测更新
            getCheckTask();
        }

        // 注册 百度地图SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);


        bmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.ls_nologin_header_icon);

        if (PushManager.isPush) {
            //开关推送
            PushManager.getInstance().initViews(this);
        }
        //上传设备信息
        LSRequestManager.getInstance().upDataInfo();

        PushModel model = PushManager.getInstance().getPushModel(getIntent());
        if (model == null) {
//			tab.onTabClick(tab.SELECT);
			tab.onTabClick(tab.CHOICENESS);
		}
		else
		{
			sendPush(model);
		}
		Common.mainIsStart = true;
	}

	private void initViews() {
		layout_left = findViewById(R.id.layout_left);
		layout_right = findViewById(R.id.layout_right);
		
		menu_view = (SlidingMenuView) findViewById(R.id.ls_slidingmenu_view);
		lv_set = (ListView) findViewById(R.id.lv_set);
		
		adapter = new MenuAdapter();
		lv_set.setAdapter(adapter);
		lv_set.setOnItemClickListener(this);
		
		ls_user_point = (TextView) findViewById(R.id.ls_user_point);
		ll_point = (LinearLayout) findViewById(R.id.ll_point);
		ls_nologin_header = (ImageView) findViewById(R.id.ls_nologin_header);
		ls_login_text = (TextView) findViewById(R.id.ls_login_text);
		ls_login_ll = (LinearLayout) findViewById(R.id.ls_login_ll);
		ls_user_nickname = (TextView) findViewById(R.id.ls_user_nickname);
		ls_header = (AsyncLoadImageView) findViewById(R.id.ls_header);
		
		tab = (LSTab) findViewById(R.id.lstab);
		tab.setHandler(handler);
		
		getMAX_WIDTH();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ls_login_text:
		{
			Intent intent = new Intent(this, LSLoginActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.ls_header:
		{
			if (DataManager.getInstance().getUser().getUser_id() != null
					&& !"".equals(DataManager.getInstance().getUser()
							.getUser_id())) {
				setUserHead();
			} else {
				Intent intent = new Intent(this, LSLoginActivity.class);
				startActivity(intent);
			}
		}
		break;
		case R.id.ls_nologin_header:
		{
			if (DataManager.getInstance().getUser().getUser_id() != null
					&& !"".equals(DataManager.getInstance().getUser()
							.getUser_id())) {
				setUserHead();
			} else {
				Intent intent = new Intent(this, LSLoginActivity.class);
				startActivity(intent);
			}
		}
		break;

            default:
                break;
        }
    }

    private void setUserHead() {
        postMessage(POPUP_DIALOG_LIST, "选择图片", R.array.select_head_items,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 拍摄
                                BitmapUtil.doTakePhoto(NewHomeActivity.this);
                                break;
                            case 1:
                                // 相册
                                BitmapUtil
                                        .doPickPhotoFromGallery(NewHomeActivity.this);
                                break;
                        }
                    }
                });
    }

    public void toggleMenu() {
//		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
//				.getLayoutParams();
//		layout_left.setVisibility(View.VISIBLE);
//		// 缩回去
//		if (layoutParams.rightMargin < -window_width / 2) {
//			new AsynMove().execute(-SPEED);
//		} else {
//			new AsynMove().execute(SPEED);
//		}
    }

    void getMAX_WIDTH() {
        ViewTreeObserver viewTreeObserver = layout_right.getViewTreeObserver();
        // 获取控件宽度
        viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    window_width = getWindowManager().getDefaultDisplay()
                            .getWidth();
                    MAX_WIDTH = layout_left.getWidth()
                            - (StringUtil.getXY(NewHomeActivity.this)[0] / 8);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
                            .getLayoutParams();
                    ViewGroup.LayoutParams layoutParams_2 = menu_view
                            .getLayoutParams();
                    layoutParams.width = window_width;
                    layout_right.setLayoutParams(layoutParams);

                    // 设置layout_left的初始位置.
                    if ("loginin".equals(login) || "loginup".equals(login)) {
                        layoutParams.rightMargin = -MAX_WIDTH;
                        layout_right.setLayoutParams(layoutParams);
                        layout_left.setVisibility(View.VISIBLE);
                    } else {
                        layout_left.setVisibility(View.INVISIBLE);
                    }

                    layoutParams_2.width = MAX_WIDTH;
                    menu_view.setLayoutParams(layoutParams_2);

                    hasMeasured = true;

                }
                return true;
            }
        });

    }

    class AsynMove extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int times = 0;
            if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除
                times = MAX_WIDTH / Math.abs(params[0]);
            else
                times = MAX_WIDTH / Math.abs(params[0]) + 1;// 有余数

            for (int i = 0; i < times; i++) {
                publishProgress(params[0]);
                try {
                    Thread.sleep(sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        /**
         * update UI
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
                    .getLayoutParams();
            // 右移动
            if (values[0] > 0) {
                layoutParams.rightMargin = Math.max(layoutParams.rightMargin
                        - values[0], -MAX_WIDTH);
            } else {
                // 左移动
                layoutParams.rightMargin = Math.min(layoutParams.rightMargin
                        - values[0], 0);
                if (layoutParams.rightMargin == 0) {
                    layout_left.setVisibility(View.INVISIBLE);
                }
            }
            layout_right.setLayoutParams(layoutParams);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        Common.mainIsStart = false;
        Common.log("NewHomeActivity.isStart set ===  false");
    }

    private void getCheckTask() {
        Task task = new Task(null, C.MAIN_CHECKVERSION_URL, null,
                "USER_INFO_URL", this);
        task.setPostData(getCheckVersionParams().getBytes());
        publishTask(task, IEvent.IO);
    }

    private String getCheckVersionParams() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String version = C.VERSION.replaceAll("\\.", "");
        params.put("version", version);
        return RequestParamUtil.getInstance(this).getRequestParams(params);
    }


    @Override
    public void handleTask(int initiator, Task task, int operation) {
        /*
		if(content != null){
			content.handleTask(initiator, task, operation);
		}
		*/

        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]
                        || task.getData() instanceof Bitmap) {
                    result = new String((byte[]) task.getData());
                    if (((String) task.getParameter())
                            .equals("USER_INFO_URL")) {
                        parserVersionInfo(result);
                    }
                }
                break;
            default:
                break;
        }
        postMessage(DISMISS_PROGRESS);
    }

    private void parserVersionInfo(String params) {
        String result = DataManager.getInstance().validateResult(params);
        if (result != null) {
            if ("OK".equals(result)) {
                vb = (VersionBean) DataManager.getInstance().jsonParse(params,
                        DataManager.TYPE_MAIN_CHECKVERSION);
                postMessage(HAVE_NEW_VERSION);
            }
        }
    }


    private void showNewVersionDialog() {

        DialogManager.getInstance().showUpdataDialog(null, vb.getChangelog(), vb.getUrl(), null);
//        AlertDialog.Builder builder = new Builder(this);
//        builder.setMessage(vb.getChangelog());
//        builder.setTitle("新版本提示");
//        builder.setPositiveButton("去更新", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(vb.getUrl());
//                intent.setData(content_url);
//                startActivity(intent);
//            }
//        });
//        builder.setNegativeButton("暂不", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                // 敢点暂不，就退出程序
////				Intent intent = new Intent(Intent.ACTION_MAIN);
////				intent.addCategory(Intent.CATEGORY_HOME);
////				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////				startActivity(intent);
////				android.os.Process.killProcess(Process.myPid());
//            }
//        });
//        builder.create().show();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (content != null && content.handleMessage(msg))
            return true;
        if (super.handleMessage(msg))
            return true;
        switch (msg.what) {
            case HAVE_NEW_VERSION:
                showNewVersionDialog();
                break;
        }
        return true;
    }


    private void initListeners() {
        ls_login_text.setOnClickListener(this);
        ls_header.setOnClickListener(this);
        ls_nologin_header.setOnClickListener(this);
    }

    MenuAdapter adapter;

    protected void onResume() {
        super.onResume();
        if (content == mineFragment) {
            mineFragment.refreshUser();
        } else if (content == clubFragment) {
            clubFragment.getLocation();//loadClubHomePageInfo();
        }
        else if ( content == clubFragmentNew )
        {
            clubFragmentNew.onResume();
        }
        if (content != mineFragment) {
            //获取红点
//			mineFragment.getNoticeDot();
            LSRequestManager.getInstance().getRedDot(tab);
        }
		/*
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id())) {
			ls_login_text.setVisibility(View.GONE);
			ls_login_ll.setVisibility(View.VISIBLE);
			ls_user_nickname.setText(DataManager.getInstance().getUser()
					.getNickname());
			ls_user_point.setText(DataManager.getInstance().getUser()
					.getPoint());
			ll_point.setVisibility(View.GONE);
			ls_nologin_header.setVisibility(View.GONE);
			ls_header.setVisibility(View.VISIBLE);
			String headicon = DataManager.getInstance().getUser().getHeadicon();
			if (!TextUtils.isEmpty(headicon)) {
				ls_header.setImage(headicon, bmp, bmp, "zhuangbei_detail");
			}
		} else {
			ls_login_text.setVisibility(View.VISIBLE);
			ls_login_ll.setVisibility(View.GONE);
			ls_nologin_header.setVisibility(View.VISIBLE);
			ll_point.setVisibility(View.GONE);
			ls_header.setVisibility(View.GONE);
		}
		*/

    }



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id())) {
			if (position == 0) {
				Intent intent = new Intent(this, LsUserLikeActivity.class);
				startActivity(intent);
			}  else if (position == 1) {
				Intent intent = new Intent(this, LsUserOrderActivity.class);
				startActivity(intent);
			} else if (position == 2) {
				Intent intent = new Intent(this, LsUserMsgActivity.class);
				startActivity(intent);
			} else if (position == 3) {
				Intent intent = new Intent(this, LsUserDraftActivity.class);
				startActivity(intent);
			}
		} else {
			Intent intent = new Intent(this, LSLoginActivity.class);
			startActivity(intent);
		}
	}


    private static class ViewHolder {
        ImageView iv_menu_item;
        TextView tv_menu_item;
        Button ls_setting_bt;
        LinearLayout ls_menu_item;
    }

    private class MenuAdapter extends BaseAdapter {

        public MenuAdapter() {
            inflater = LayoutInflater.from(NewHomeActivity.this);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        LayoutInflater inflater;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.ls_main_item, null);
                holder = new ViewHolder();
                holder.iv_menu_item = (ImageView) convertView
                        .findViewById(R.id.ls_menu_item_iv);
                holder.tv_menu_item = (TextView) convertView
                        .findViewById(R.id.ls_menu_item_tv);
                holder.ls_setting_bt = (Button) convertView
                        .findViewById(R.id.ls_setting_bt);
                holder.ls_menu_item = (LinearLayout) convertView
                        .findViewById(R.id.ls_menu_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 4) {
                holder.ls_menu_item.setVisibility(View.GONE);
                holder.ls_setting_bt.setVisibility(View.VISIBLE);
                holder.ls_setting_bt.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewHomeActivity.this,
                                LsSettingActivity.class);
                        startActivity(intent);
                    }
                });

            } else {
                holder.ls_menu_item.setVisibility(View.VISIBLE);
                holder.ls_setting_bt.setVisibility(View.GONE);
            }
            if (position == 0) {
                holder.iv_menu_item
                        .setBackgroundResource(R.drawable.ls_xihuan_icon);
                holder.tv_menu_item.setText("喜欢的装备");
            } else if (position == 1) {
                holder.iv_menu_item
                        .setBackgroundResource(R.drawable.ls_order_icon);
                holder.tv_menu_item.setText("我的订单");
            } else if (position == 2) {
                holder.iv_menu_item
                        .setBackgroundResource(R.drawable.ls_xiaoxi_icon);
                holder.tv_menu_item.setText("消息提醒");
            } else if (position == 3) {
                holder.iv_menu_item
                        .setBackgroundResource(R.drawable.ls_caogao_icon);
                holder.tv_menu_item.setText("草稿箱");
            }
            return convertView;
        }

    }


    Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case C.PHOTO_PICKED_WITH_DATA:
                    Uri photo_uri = data.getData();
                    bitmap = BitmapUtil.getThumbnail(photo_uri, this);
                    postMessage(POPUP_PROGRESS, getString(R.string.sending));
                    new GetDataTask(bitmap).execute();
                    // ls_nologin_header.setImageBitmap(BitmapUtil.getRCB(bitmap,
                    // bitmap.getWidth() / 2));
                    // postMessage(POPUP_PROGRESS,getString(R.string.sending));
                    // new GetDataTask().execute();
                    // new GetDataTask().execute();
                    // uploadFile();
                    // uploadPicTask(bitmap);
                    // BitmapUtil.doCropPhoto(bitmap, MainActivity.this);
                    break;
                case C.PICKED_WITH_DATA:
				/*
				 * Bitmap bt = data.getParcelableExtra("data");
				 * postMessage(POPUP_PROGRESS,getString(R.string.sending)); new
				 * GetDataTask(bt).execute();
				 */
				/*
				 * postMessage(POPUP_PROGRESS,
				 * getString(ResourceUtil.getStringId(this,"sending")));
				 * uploadHeadTask(bt);
				 */
                    break;
                case C.CAMERA_WITH_DATA:
                    File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");
                    bitmap = BitmapUtil.getThumbnail(file, this);
                    postMessage(POPUP_PROGRESS, getString(R.string.sending));
                    new GetDataTask(bitmap).execute();
                    // ls_nologin_header.setImageBitmap(BitmapUtil.getRCB(bitmap,
                    // bitmap.getWidth() / 2));
                    // postMessage(POPUP_PROGRESS,getString(R.string.sending));
                    // uploadFile();
                    // uploadPicTask(bitmap);
                    // BitmapUtil.doCropPhoto(bitmap, MainActivity.this);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);


        if (CLOSEAPPLICATION)
        {
            CLOSEAPPLICATION = false;
            finish();
        }
        PushModel model = PushManager.getInstance().getPushModel(intent);
        if (model == null) {
            Common.log("push model NewHome == null");
            return;
        }
        sendPush(model);
    }

    private void sendPush(PushModel model) {
        //打开帖子
        if (model.type == 2) {
            tab.onTabClick(tab.CHOICENESS);
            Intent i = new Intent(this, LSClubTopicActivity.class);
            i.putExtra("topicID", model.topic_id);
            startActivity(i);
        }
//				个人中心报名通知
        else if (model.type == 3) {
            if (tab != null) {
                tab.onTabClick(tab.EVENT);
            } else {
                Common.log("push error tab == null");
            }
        }
//				个人中心 我发布的活动
        else if (model.type == 4) {
            if (tab != null) {
                tab.onTabClick(tab.EVENT);
            } else {
                Common.log("push error tab == null");
            }
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        Bitmap btt;

        public GetDataTask(Bitmap bt) {
            btt = bt;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            String[] netResult = new String[10];
            // photo_data2 = ImageUtil.resizeBitmap(bitmap, 100, 100);
            // String url = FConstant.TASKID_WORK_ADDBOLG_URL;
            // String img_source = String.valueOf(photo_source);
            // String savePath2 = Environment.getExternalStorageDirectory()
            // + "/tmp_icon.jpg";
            String savePath = Environment.getExternalStorageDirectory()
                    + "/temp.jpg";
            ImageUtil.savePic(savePath, btt, 100);
            PostMethod filePost = new PostMethod(
                    "http://api.lis99.com/user/savePhoto/");
            try {
                // 组拼post数据的实体
                // image参数
                File file = new File(savePath);
                FilePart f_part = new FilePart("photo", file);
                f_part.setCharSet("utf-8");
                f_part.setContentType("image/jpeg");

                // user_id参数
                StringPart s_part = new StringPart("user_id", DataManager
                        .getInstance().getUser().getUser_id(), "utf-8");

                Part[] parts = {s_part, f_part};

                filePost.setRequestEntity(new MultipartRequestEntity(parts,
                        filePost.getParams()));
                HttpClient client = new HttpClient();
                client.getHttpConnectionManager().getParams()
                        .setConnectionTimeout(60000);

                // 完成文件上的post请求
                client.executeMethod(filePost);
                String receiveMsg = filePost.getResponseBodyAsString();
                netResult[0] = receiveMsg;
                System.out.println("服务器端返回结果:" + receiveMsg);
            } catch (Exception e) {
            } finally {
                filePost.releaseConnection();
            }

            return netResult;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // mWaittingDialog.dismiss();

            String result1 = DataManager.getInstance()
                    .validateResult(result[0]);
            String img;
            if (result1 != null) {
                if ("OK".equals(result1)) {
                    try {
                        JSONObject json = new JSONObject(result[0]);
                        JSONObject ibjob = json.optJSONObject("data");
                        img = ibjob.optString("img");
                        ls_nologin_header.setVisibility(View.GONE);
                        ls_header.setVisibility(View.VISIBLE);
                        // DataManager.getInstance().getUser().setHeadicon(img);
                        String headicon = DataManager.getInstance().getUser()
                                .getHeadicon();
                        if (!TextUtils.isEmpty(img)) {
                            DataManager.getInstance().getUser().setHeadicon(img);
                        } else {
                            img = headicon;
                        }
                        ls_header.setImage(img, null, null, "zhuangbei_detail");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    postMessage(POPUP_TOAST, result);
                }
            }
            postMessage(DISMISS_PROGRESS);
        }
    }


}