package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.ClubSpecialListActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.model.ChoicenessModel;
import com.lis99.mobile.club.model.ChoicenessModel.Omnibuslist;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.newhome.DynamicActivity;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.search.SearchActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.CardsAnimationAdapter;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

public class FragmentChoiceness extends LSFragment implements
OnHeaderRefreshListener, OnFooterRefreshListener, OnClickListener
{
	private ChoicenessAdapter adapter;
	private ListView list;
	private PullToRefreshView pull_refresh_view;
	private Page page;
	private ChoicenessModel model;

	private LinearLayout layout_search;

	private View headView;

	private View iv_subject, iv_active, iv_dynamic;

	@Override
	protected void initViews(ViewGroup container)
	{
		// TODO Auto-generated method stub
		super.initViews(container);
		body = View.inflate(getActivity(), R.layout.choiceness, null);
		setTitle("精选");
		page = new Page();
		
		list = (ListView) findViewById(R.id.list);
		pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		
		pull_refresh_view.setOnHeaderRefreshListener(this);
		pull_refresh_view.setOnFooterRefreshListener(this);
		
//		adapter = new ChoicenessAdapter(getActivity());
//		list.setAdapter(adapter);
		
		list.setOnItemClickListener( new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
				if ( adapter == null ) return;
				Omnibuslist item = (Omnibuslist) adapter.getItem(arg2 - 1);
//				Omnibuslist item = (Omnibuslist) adapter.getItem(arg2);
				if ( item == null ) return;
				Intent intent = null;
				if ( item.type == 1 || item.type == 2 || item.type == 5 || item.type == 6 )
				{
					intent = new Intent(FragmentChoiceness.this.getActivity(), LSClubTopicActivity.class);
					intent.putExtra("topicID", item.topic_id);
					startActivity(intent);

				}
				else if ( item.type == 3 )
				{
					/**Intent intent = new Intent(getActivity(),
								LsActivityDetail.class);
						Bundle bundle = new Bundle();
						bundle.putInt("id", content.eventID);
						bundle.putString("url", "http://www.lis99.com/huodong/detail/"+content.eventID);
						bundle.putString("title", "活动详情");
						intent.putExtras(bundle);
						startActivity(intent);*/
					intent = new Intent(FragmentChoiceness.this.getActivity(), MyActivityWebView.class);
					Bundle bundle = new Bundle();
					bundle.putString("URL", item.url);
					bundle.putString("TITLE", item.title);
					bundle.putString("IMAGE_URL", item.image);
					bundle.putInt("TOPIC_ID", item.topic_id);
					intent.putExtras(bundle);
					startActivity(intent);

				}
				else if ( item.type == 4 )
				{
					intent = new Intent(FragmentChoiceness.this.getActivity(), SubjectActivity.class);
					intent.putExtra("TITLE", item.title);
					intent.putExtra("ID", item.id);
					Common.log("item.id="+item.id);
					startActivity(intent);
				}
				else if ( item.type == 7 )
				{
					intent = new Intent(getActivity(), ClubSpecialListActivity.class);
					intent.putExtra("tagid", item.tag_id);
					startActivity( intent );
				}
				
			}
		});

		layout_search = (LinearLayout) findViewById(R.id.layout_search);

		layout_search.setOnClickListener(this);

		//==3.4====

		headView = View.inflate(getActivity(), R.layout.choiceness_head, null);

		iv_subject = headView.findViewById(R.id.iv_subject);
		iv_active = headView.findViewById(R.id.iv_active);
		iv_dynamic = headView.findViewById(R.id.iv_dynamic);

		iv_subject.setOnClickListener(this);
		iv_active.setOnClickListener(this);
		iv_dynamic.setOnClickListener(this);

		list.addHeaderView(headView);
	}
	
	private void cleanList ()
	{
		list.setAdapter(null);
		adapter = null;
		page = new Page();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getList();
	}

	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		switch ( arg0.getId() )
		{
			case R.id.iv_dynamic:
				startActivity(new Intent(getActivity(), DynamicActivity.class));
				break;
			case R.id.layout_search:
				startActivity(new Intent(getActivity(), SearchActivity.class));
				break;
			case R.id.iv_subject:
				startActivity(new Intent (getActivity(), ChoicenessAllActivity.class));
				break;
			case R.id.iv_active:
				startActivity(new Intent (getActivity(), ActiveAllActivity.class));
				break;
		}
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
	
	private void getList ()
	{
		if ( page.isLastPage() )
		{
			Common.toast("没有内容了");
			return;
		}
		String url = C.CHOICENESS + page.pageNo;
		model = new ChoicenessModel();
		MyRequestManager.getInstance().requestGet(url, model, new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				model = (ChoicenessModel) mTask.getResultModel();
				page.pageNo += 1;
				if ( adapter == null )
				{
					page.setPageSize(model.totalpage);
					adapter = new ChoicenessAdapter(getActivity(), model.omnibuslist);

					AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
					animationAdapter.setAbsListView(list);
					list.setAdapter(animationAdapter);

//					list.setAdapter(adapter);
				}
				else
				{
					adapter.setList(model.omnibuslist);
				}
			}
		});
	}
	
}
