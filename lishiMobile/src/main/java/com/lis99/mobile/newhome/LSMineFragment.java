package com.lis99.mobile.newhome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSClubMyTopicActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.LSCollectionActivity;
import com.lis99.mobile.entry.LsSettingActivity;
import com.lis99.mobile.entry.LsUserLikeActivity;
import com.lis99.mobile.mine.ActivityReplyMine;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.mine.LSMineApplyActivity;
import com.lis99.mobile.mine.LSMineApplyManageActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class LSMineFragment extends LSFragment implements OnClickListener
{

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

	private final static int SHOW_USER_INFO = 2001;
	private final static int SHOW_NOTICE = 2002;

	// ========

	LSTab tab;

//	=====3.6.0======

	private RelativeLayout layout_user;
	private RelativeLayout layout_friends;
	private ImageView titleRightImage, titleLeftImage;
	private boolean isAttention;

	private View v_friend_arrow, v_applyinfo_arrow, v_reply_arrow, v_applymanager_arrow, iv_friendDot;

	public void setTab(LSTab tab)
	{
		this.tab = tab;
	}

	private void buildOptions()
	{
		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ls_nologin_header_icon)
				.showImageForEmptyUri(R.drawable.ls_nologin_header_icon)
				.showImageOnFail(R.drawable.ls_nologin_header_icon)
				.cacheInMemory(true).cacheOnDisk(true).build();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		buildOptions();
	}

	public void refreshUser()
	{
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id()))
		{
			String headicon = DataManager.getInstance().getUser().getHeadicon();
			ImageLoader.getInstance().displayImage(headicon, header, options);
			nameView.setText(DataManager.getInstance().getUser().getNickname());
			getUserInfoTask();
		} else
		{
			header.setImageResource(R.drawable.ls_nologin_header_icon);
			vipView.setVisibility(View.GONE);
			nameView.setText("登录");
//			tags.clear();
//			setTags();

			haveApplyInfo = false;
			haveApply = false;

			haveReply = false;

			isFounder = false;
			isAdministrator = false;

			isAttention = false;

			showOrHideViews();
		}
		getNoticeDot();
	}

	@Override
	protected void initViews(ViewGroup container)
	{
		super.initViews(container);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		body = inflater.inflate(R.layout.fragment_mine, container, false);

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

		iv_friendDot = findViewById(R.id.iv_friendDot);

		v_friend_arrow = findViewById(R.id.v_friend_arrow);
		v_applyinfo_arrow = findViewById(R.id.v_applyinfo_arrow);
		v_applymanager_arrow = findViewById(R.id.v_applymanager_arrow);
		v_reply_arrow = findViewById(R.id.v_reply_arrow);

	}

	private void getUserInfoTask()
	{
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id()))
		{
			Task task = new Task(null, C.USER_INFO_URL
					+ DataManager.getInstance().getUser().getUser_id(), null,
					C.USER_INFO_URL, this);
			publishTask(task, IEvent.IO);

			// getNoticeDot();

		}
	}

	/** 红点请求 */
	public void getNoticeDot()
	{
		String userId = DataManager.getInstance().getUser().getUser_id();
		if (TextUtils.isEmpty(userId))
		{
			return;
		}
		Task task = new Task(null, C.USER_NOTICE_TIPS_URL
				+ DataManager.getInstance().getUser().getUser_id(), null,
				C.USER_NOTICE_TIPS_URL, this);
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
					if (((String) task.getParameter()).equals(C.USER_INFO_URL))
					{
						parserUserInfo(result);
					} else if (((String) task.getParameter())
							.equals(C.USER_NOTICE_TIPS_URL))
					{
						parseNoticeTips(result);
					}
				}
				break;
			default:
				break;
		}
	}

	private void parseNoticeTips(String result)
	{
		try
		{
			Common.log("result ="+result);
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode))
			{
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
			postMessage(SHOW_NOTICE);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}

	private void parserUserInfo(String result)
	{
		try
		{
			Common.log("result=" + result);
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode))
			{
				String msg = root.get("data").asText();
				postMessage(ActivityPattern1.POPUP_TOAST, msg);
				return;
			}
			JsonNode data = root.get("data").get("user");
			String vip = data.get("is_vip").asText();
			if ("1".equals(vip))
			{
				isVip = true;
			} else
			{
				isVip = false;
			}
			// isVip = data.get("is_vip").asBoolean();
			if (data.has("tag"))
			{
				List<String> temp = LSFragment.mapper.readValue(data.get("tag")
						.traverse(), new TypeReference<List<String>>()
				{
				});
				tags.clear();
				tags.addAll(temp);
			}
			postMessage(SHOW_USER_INFO);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
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

	void showOrHideViews()
	{
		//箭头
		v_friend_arrow.setVisibility(isAttention ? View.GONE : View.VISIBLE);
		v_reply_arrow.setVisibility(haveReply ? View.GONE : View.VISIBLE);
		v_applyinfo_arrow.setVisibility(haveApplyInfo ? View.GONE : View.VISIBLE);
		v_applymanager_arrow.setVisibility(haveApply ? View.GONE : View.VISIBLE);

		//红点
		iv_friendDot.setVisibility(isAttention ? View.VISIBLE : View.GONE);
		applyInfoDot.setVisibility(haveApplyInfo ? View.VISIBLE : View.GONE);
		applyManageDot.setVisibility(haveApply ? View.VISIBLE : View.GONE);
		replyDot.setVisibility(haveReply ? View.VISIBLE : View.GONE);
		managePanel.setVisibility((isFounder || isAdministrator) ? View.VISIBLE
				: View.GONE);
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		if (msg.what == SHOW_USER_INFO)
		{
			if (isVip)
			{
				vipView.setVisibility(View.VISIBLE);
			} else
			{
				vipView.setVisibility(View.GONE);
			}

//			setTags();

			return true;
		} else if (msg.what == SHOW_NOTICE)
		{
			showOrHideViews();
			return true;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v)
	{
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id()))
		{
			if (v.getId() == R.id.likePanel)
			{
				Intent intent = new Intent(getActivity(),
						LsUserLikeActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.collectionPanel)
			{
				Intent intent = new Intent(getActivity(),
						LSCollectionActivity.class);
				startActivity(intent);
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
			else if (v.getId() == R.id.titleRightImage  )
			{
				Intent intent = new Intent(getActivity(),
						LsSettingActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.topicPanel)
			{
				Intent intent = new Intent(getActivity(),
						LSClubMyTopicActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.replyPanel)
			{
				// replyDot.setVisibility(View.GONE);
				Intent intent = new Intent(getActivity(),
						ActivityReplyMine.class);
				startActivity(intent);
			} else if (v.getId() == R.id.applyInfoPanel)
			{
				// applyInfoDot.setVisibility(View.GONE);
				Intent intent = new Intent(getActivity(),
						LSMineApplyActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.applyManagePanel)
			{
				// applyManageDot.setVisibility(View.GONE);
				Intent intent = new Intent(getActivity(),
						LSMineApplyManageActivity.class);
				startActivity(intent);
			}
			else if (v.getId() == R.id.layout_friends )
			{
				Intent intent = new Intent(getActivity(),
						MyFriendsActivity.class);
				startActivity(intent);
			}
		}
		else
		{
			if (v.getId() == R.id.layout_user )
			{
				Intent intent = new Intent(getActivity(), LSLoginActivity.class);
				startActivity(intent);
			}
			else if ( v.getId() == R.id.titleRightImage )
			{
				Intent intent = new Intent(getActivity(),
						LsSettingActivity.class);
				startActivity(intent);
			}

		}
	}

}
