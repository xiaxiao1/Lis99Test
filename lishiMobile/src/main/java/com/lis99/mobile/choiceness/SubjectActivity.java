package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.model.SubjectModel;
import com.lis99.mobile.club.model.SubjectModel.Topiclist;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.CardsAnimationAdapter;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 专题页
 * 
 * @author yy
 * 
 */
public class SubjectActivity extends LSBaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener, OnClickListener
{

	private ListView list;
	private PullToRefreshView pull_refresh_view;
	private View head;

	private SubjectAdapter adapter;

	private Page page;

	private SubjectModel model;

	private String title, ID;

	// ===

	private TextView tv_title, tv_info, tv_describe;
	private RoundedImageView iv_bg;

	private View view_reference;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choiceness_subject_info);
		initViews();
		title = getIntent().getStringExtra("TITLE");
		ID = getIntent().getStringExtra("ID");

		view_reference = findViewById(R.id.view_reference);

		list = (ListView) findViewById(R.id.list);
		pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

		pull_refresh_view.setOnHeaderRefreshListener(this);
		pull_refresh_view.setOnFooterRefreshListener(this);
		setTitle(title);
		
		setTitleAlpha(0);
		head = View.inflate(this, R.layout.choiceness_subject_head, null);
		tv_title = (TextView) head.findViewById(R.id.tv_title);
		tv_info = (TextView) head.findViewById(R.id.tv_info);
		tv_describe = (TextView) head.findViewById(R.id.tv_describe);
		iv_bg = (RoundedImageView) head.findViewById(R.id.iv_bg);

		list.addHeaderView(head);

		// adapter = new SubjectAdapter(this);
		// list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				if (adapter == null || arg2 == 0)
					return;
				Topiclist item = (Topiclist) adapter.getItem(arg2 - 1);
				Intent intent = new Intent(activity, LSClubTopicActivity.class);
				intent.putExtra("topicID", item.topic_id);
				startActivity(intent);

			}
		});

		list.setOnScrollListener(new OnScrollListener()
		{

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{
				// TODO Auto-generated method stub
				View v = list.getChildAt(0);
				if (v == null)
					return;
				float alpha = v.getTop();
				if (firstVisibleItem > 0)
				{
					setTitleAlpha(headHeight);
				} else
				{
					setTitleAlpha(-alpha);
				}
			}
		});

		page = new Page();
		getList();
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view)
	{
		// TODO Auto-generated method stub
		pull_refresh_view.onFooterRefreshComplete();
		getList();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view)
	{
		// TODO Auto-generated method stub
		pull_refresh_view.onHeaderRefreshComplete();
		cleanList();
		getList();
	}

	private float headHeight = Common.dip2px(160);

	/**
	 * 设置标题栏透明度
	 * 
	 * @param num
	 */
	private void setTitleAlpha(float num)
	{
		Common.log("alpha="+num);
		if (num > headHeight)
		{
			num = headHeight;
		} else if (num < 0)
		{
			num = 0;
		}
		if (num < headHeight)
		{
			setBack(true);
		} else
		{
			setBack(false);
		}
		float alpha = num / headHeight;
		setTitleBarAlpha(alpha);
	}

	// 设置返回按钮
	private void setBack(boolean isBg)
	{
		if (isBg)
		{
			view_reference.setVisibility(View.GONE);
			setLeftView(R.drawable.ls_page_back_icon_bg);
		} else
		{
			view_reference.setVisibility(View.VISIBLE);
			setLeftView(R.drawable.ls_page_back_icon);
		}
	}

	private void getList()
	{
		if (page.isLastPage())
		{
			Common.toast("没有内容了");
			return;
		}
		model = new SubjectModel();
		String url = C.CHOIECNESS_SUBJECT + ID + "/" + page.pageNo;
		MyRequestManager.getInstance().requestGet(url, model, new CallBack()
		{

			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				model = (SubjectModel) mTask.getResultModel();
				page.pageNo += 1;
				if (adapter == null)
				{
					page.setPageSize(model.totalpage);
					if (model.specialinfo != null)
					{
						tv_title.setText(model.specialinfo.title);
						tv_info.setText(model.specialinfo.subhead);
						tv_describe.setText(model.specialinfo.describe);
						ImageLoader.getInstance().displayImage(
								model.specialinfo.image, iv_bg,
								ImageUtil.getclub_topic_imageOptions());
					}

					if (model.topiclist != null)
					{
						adapter = new SubjectAdapter(activity, model.topiclist);
//						list.setAdapter(adapter);

						AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
						animationAdapter.setAbsListView(list);
						list.setAdapter(animationAdapter);

					}
				} else
				{
					adapter.setList(model.topiclist);
				}
			}
		});
	}

	private void cleanList()
	{
		list.setAdapter(null);
		adapter = null;
		page = new Page();
	}
}
