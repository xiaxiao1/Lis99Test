package com.lis99.mobile.club;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.model.ClubTopicGetApplyList;
import com.lis99.mobile.club.widget.Stepper;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.DialogManager.callBack;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LSClubApplyActivity extends LSBaseActivity
{

	int clubID;
	int topicID;

	TextView nameView;
	TextView phoneView;
	TextView idNumView;
	// TextView noteView;
	Stepper stepper;

	// 、、＝＝＝＝2.3=＝＝＝＝＝＝
	private EditText et_telOhter, et_QQ, et_address;

	private Button btn_address;

	private RadioGroup radioGroup;

	private LinearLayout layoutPeple;

	private RelativeLayout titleRight;
	private TextView applyButton;

	String sex = "1";
	String city = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		clubID = getIntent().getIntExtra("clubID", 0);
		topicID = getIntent().getIntExtra("topicID", 0);
		setContentView(R.layout.activity_lsclub_apply);
		initViews();
		setTitle("报名");

		getApplyList();
	}
	protected void rightAction()
	{
		if (listmodel.items == null)
			return;
		String name = nameView.getText().toString();
		String phone = phoneView.getText().toString();
		String idNum = idNumView.getText().toString();
		// 2.3
		String QQ = et_QQ.getText().toString();
		String etAddress = et_address.getText().toString();
		String telOther = et_telOhter.getText().toString();

		if (TextUtils.isEmpty(name) && listmodel.items.get(0).equals("1"))
		{
			postMessage(POPUP_TOAST, "姓名不能为空");
			return;
		}
		if (TextUtils.isEmpty(idNum) && listmodel.items.get(1).equals("1"))
		{
			Common.toast("身份证号码不能为空");
			return;
		}
		if (!TextUtils.isEmpty(idNum) && listmodel.items.get(1).equals("1"))
		{
			if (idNum.length() != 18)
			{
				postMessage(POPUP_TOAST, "身份证号位数不正确");
				return;
			}

			if (!idNum.matches("[0-9]{18}") && !idNum.matches("[0-9]{17}(X|x)")
					&& listmodel.items.get(1).equals("1"))
			{
				postMessage(POPUP_TOAST, "身份证号格式不正确");
				return;
			}
		}

		if (TextUtils.isEmpty(phone) && listmodel.items.get(3).equals("1"))
		{
			postMessage(POPUP_TOAST, "手机号不能为空");
			return;
		}

		if (phone.length() != 11 && listmodel.items.get(3).equals("1"))
		{
			postMessage(POPUP_TOAST, "手机号位数不正确");
			return;
		}

		if (TextUtils.isEmpty(QQ) && listmodel.items.get(5).equals("1"))
		{
			postMessage(POPUP_TOAST, "QQ不能为空");
			return;
		}

		// if (TextUtils.isEmpty(idNum)) {
		// postMessage(POPUP_TOAST, "身份证号不能为空");
		// return;
		// }

		// String note = noteView.getText().toString();

		int parterNum = stepper.getStep();

		String userID = DataManager.getInstance().getUser().getUser_id();

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("topicid", topicID);
		params.put("club_id", clubID);
		params.put("token", SharedPreferencesHelper.getLSToken());
		params.put("sex", "" + sex);
		params.put("qq", QQ);
		params.put("name", name);
		params.put("mobile", phone);
		params.put("credentials", idNum);
		params.put("willnum", parterNum);
		params.put("user_id", userID);
		params.put("address", city);
		params.put("postaladdress", etAddress);
		params.put("phone", telOther);

		// params.put("club_id", clubID);
		// params.put("club_topicid", topicID);
		// params.put("remarks", note);

		upDataApplayInfo(params);

		// Task task = new Task(null, C.CLUB_EVENT_APPLY, C.HTTP_POST,
		// C.CLUB_EVENT_APPLY, this);
		// task.setPostData(RequestParamUtil.getInstance(this).getRequestParams(params).getBytes());
		// publishTask(task, IEvent.IO);
	}

	// @Override
	// public void handleTask(int initiator, Task task, int operation) {
	// super.handleTask(initiator, task, operation);
	// String result;
	// switch (initiator) {
	// case IEvent.IO:
	// if (task.getData() instanceof byte[]) {
	// result = new String((byte[]) task.getData());
	// if (((String) task.getParameter()).equals(C.CLUB_EVENT_APPLY)) {
	// parserResultInfo(result);
	// }
	// }
	// break;
	// default:
	// break;
	// }
	// postMessage(DISMISS_PROGRESS);
	// }
	//
	// private void parserResultInfo(String result) {
	// try {
	// JsonNode root = LSFragment.mapper.readTree(result);
	// String errCode = root.get("status").asText("");
	// if (!"OK".equals(errCode)) {
	// postMessage(ActivityPattern1.POPUP_TOAST, errCode);
	// return;
	// }
	//
	// postMessage(SHOW_UI);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// postMessage(ActivityPattern1.DISMISS_PROGRESS);
	// }
	// }

	// @Override
	// public boolean handleMessage(Message msg) {
	// if (msg.what == SHOW_UI) {
	// postMessage(POPUP_TOAST, "报名成功");
	// finish();
	// return true;
	// }
	// return super.handleMessage(msg);
	// }

	@Override
	protected void initViews()
	{
		super.initViews();
		nameView = (TextView) findViewById(R.id.nameView);
		phoneView = (TextView) findViewById(R.id.phoneView);
		idNumView = (TextView) findViewById(R.id.idNumView);
		stepper = (Stepper) findViewById(R.id.stepper1);

		// ====2.3====
		et_telOhter = (EditText) findViewById(R.id.et_telOhter);
		et_QQ = (EditText) findViewById(R.id.et_QQ);
		et_address = (EditText) findViewById(R.id.et_address);

		btn_address = (Button) findViewById(R.id.btn_address);
		btn_address.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				DialogManager.getInstance().showCityChooseDialog(activity,
						new callBack()
						{

							@Override
							public void onCallBack(Object o)
							{
								// TODO Auto-generated method stub
								city = o.toString();
								btn_address.setText(o.toString());
							}
						});

			}
		});

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				// TODO Auto-generated method stub
				if (checkedId == R.id.radioMan)
				{
					// Common.log("man");
					sex = "1";
				} else
				{
					// Common.log("Woman");
					sex = "0";
				}
			}
		});

		layoutPeple = (LinearLayout) findViewById(R.id.layoutPeple);
		applyButton = (TextView) findViewById(R.id.applyButton);
		titleRight = (RelativeLayout) findViewById(R.id.titleRight);
		titleRight.setOnClickListener(this);
		applyButton.setOnClickListener(this);

	}

	private ClubTopicGetApplyList listmodel;

	private void getApplyList()
	{
		listmodel = new ClubTopicGetApplyList();
		MyRequestManager.getInstance().requestGet(
				C.CLUB_TOPIC_APPLY_LIST + topicID, listmodel, new CallBack()
				{

					@Override
					public void handler(MyTask mTask)
					{
						// TODO Auto-generated method stub
						listmodel = (ClubTopicGetApplyList) mTask
								.getResultModel();
						if (listmodel.items == null)
						{
							listmodel.items = new ArrayList<String>();
							listmodel.items.add("1");
							listmodel.items.add("1");
							listmodel.items.add("0");
							listmodel.items.add("1");
							listmodel.items.add("0");
							listmodel.items.add("0");
							listmodel.items.add("1");
							listmodel.items.add("0");
							listmodel.items.add("0");
							// return;
						}
						if ("0".equals(listmodel.items.get(0)))
						{
							nameView.setVisibility(View.GONE);
						} else
						{
							nameView.setVisibility(View.VISIBLE);
						}
						if ("0".equals(listmodel.items.get(1)))
						{
							idNumView.setVisibility(View.GONE);
						} else
						{
							idNumView.setVisibility(View.VISIBLE);
						}
						if ("0".equals(listmodel.items.get(2)))
						{
							radioGroup.setVisibility(View.GONE);
						} else
						{
							radioGroup.setVisibility(View.VISIBLE);
						}
						if ("0".equals(listmodel.items.get(3)))
						{
							phoneView.setVisibility(View.GONE);
						} else
						{
							phoneView.setVisibility(View.VISIBLE);
						}
						if ("0".equals(listmodel.items.get(4)))
						{
							et_telOhter.setVisibility(View.GONE);
						} else
						{
							et_telOhter.setVisibility(View.VISIBLE);
						}
						if ("0".equals(listmodel.items.get(5)))
						{
							et_QQ.setVisibility(View.GONE);
						} else
						{
							et_QQ.setVisibility(View.VISIBLE);
						}
						if ("0".equals(listmodel.items.get(6)))
						{
							layoutPeple.setVisibility(View.GONE);
						} else
						{
							layoutPeple.setVisibility(View.VISIBLE);
						}
						if ("0".equals(listmodel.items.get(7)))
						{
							et_address.setVisibility(View.GONE);
						} else
						{
							et_address.setVisibility(View.VISIBLE);
						}
						if ("0".equals(listmodel.items.get(8)))
						{
							btn_address.setVisibility(View.GONE);
						} else
						{
							btn_address.setVisibility(View.VISIBLE);
						}

					}
				});

	}

	private ClubTopicGetApplyList modelinfo;

	private void upDataApplayInfo(Map<String, Object> map)
	{
		modelinfo = new ClubTopicGetApplyList();
		MyRequestManager.getInstance().requestPost(C.CLUB_TOPIC_APPLY_INFO,
				map, modelinfo, new CallBack()
				{

					@Override
					public void handler(MyTask mTask)
					{
						// TODO Auto-generated method stub
						setResult();
					}
				});
	}

	private void setResult ()
	{
		Common.toast("报名成功");
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		switch (arg0.getId())
		{
			case R.id.titleRight:
			case R.id.applyButton:
				rightAction();
				break;
		}
		super.onClick(arg0);
	}

}
