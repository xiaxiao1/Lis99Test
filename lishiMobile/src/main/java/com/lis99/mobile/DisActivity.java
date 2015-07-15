package com.lis99.mobile;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.engine.ShowUtil;
import com.lis99.mobile.engine.base.BaseActivity;
import com.lis99.mobile.entity.bean.ComBean;
import com.lis99.mobile.entity.bean.CountBean;
import com.lis99.mobile.entity.item.ComItem;
import com.lis99.mobile.entry.adapter.DisAdapter;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.constens;

import java.util.ArrayList;

public class DisActivity extends BaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private ListView listView1;
	private TextView title;
	private HttpNetRequest httpNetRequest;
	private DisAdapter<ComItem> disAdapter;
	private ImageView iv_home;
	private String id = "";
	private PullToRefreshView refreshView;
	private int page;
	private Button button1;
	private int count;
	private ComBean comBean;

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_dis;
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		listView1 = (ListView) findViewById(R.id.listView1);
		iv_home = (ImageView) findViewById(R.id.iv_home);
		button1 = (Button) findViewById(R.id.button1);
		title = (TextView) findViewById(R.id.textView2);
		title.setText(getIntent().getStringExtra("title"));
		
		page = 0;
		refreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
		id = getIntent().getStringExtra(constens.ID);
		disAdapter = new DisAdapter<ComItem>(DisActivity.this, null);
		listView1.setAdapter(disAdapter);
		viewdp();
		getcount();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DisActivity.this, PJActivity.class);
				intent.putExtra(constens.ID, id);
				startActivity(intent);
				finish();
			}
		});
		iv_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DisActivity.this,
						ShopDetailActivity.class);
				intent.putExtra(constens.OID, id);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected View getView() {
		// TODO Auto-generated method stub
		return null;
	}

	public void viewdp() {
		showWaiting(DisActivity.this);
		new OptData<ComBean>(DisActivity.this).readData(
				new QueryData<ComBean>() {

					@Override
					public String callData() {
						String data = "?" + constens.ID + "=" + id + "&"
								+ constens.OFFSET + "=" + page;
						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(constens.URLcom,
								data);
						return str;
					}

					@Override
					public void showData(ComBean t) {
						hideDialog();
						// TODO Auto-generated method stub
						if (t != null) {
							if (page == 0) {
								disAdapter.clearData();
							}
							ArrayList<ComItem> arrayList = t.getData();
							if (arrayList != null) {
								comBean = t;
								disAdapter.setData(arrayList);
							}
						}
					}
				}, ComBean.class);
	}

	public void getcount() {

		new OptData<CountBean>(DisActivity.this).readData(
				new QueryData<CountBean>() {

					@Override
					public String callData() {
						String data = "?" + constens.ID + "=" + id;

						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(constens.urlpage,
								data);
						return str;
					}

					@Override
					public void showData(CountBean t) {

						// TODO Auto-generated method stub
						if (t != null) {
							count = t.getData();

						}
					}
				}, CountBean.class);
	}

	public void onFooterRefresh(PullToRefreshView view) {
		int i;
		if (count >= 1) {
			i = (int) ((count - 1) / 15);
		} else {
			i = (int) (count / 15);
		}

		if (comBean != null && page < i) {
			page += 1;
			viewdp();

		} else {
			ShowUtil.showToast(DisActivity.this, R.string.isLast);
		}
		refreshView.onFooterRefreshComplete();

	}

	public void onHeaderRefresh(PullToRefreshView view) {

		page = 0;
		viewdp();
		refreshView.onHeaderRefreshComplete();

	}
}
