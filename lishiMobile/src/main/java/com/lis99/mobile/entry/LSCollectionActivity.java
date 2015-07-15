package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.ShopDetailActivity;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entity.bean.LSCollection;
import com.lis99.mobile.entry.adapter.CollectionAdapter;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.constens;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LSCollectionActivity extends LSBaseActivity {
	
	CollectionAdapter adapter;
	ListView listView;
	List<LSCollection> collections = new ArrayList<LSCollection>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lscollection);
		initViews();
		setTitle("收藏的店铺");
		loadCollection();
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LSCollection conllection = collections.get(position);
				Intent intent = new Intent(LSCollectionActivity.this,
						ShopDetailActivity.class);
				int oid = conllection.getShop_info().getShop_id();
				intent.putExtra(constens.OID, ""+oid);
				intent.putExtra("fav", "ls");
				intent.putExtra("dis", "");
				startActivity(intent);
				
			}
		});
	}
	
	private void loadCollection() {
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (StringUtils.isEmpty(userID)) {
			return;
		}
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.COLLECTION_GET_LIST + userID;
		Task task = new Task(null, url, null, C.COLLECTION_GET_LIST, this);
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
				parserCollestions(result);
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserCollestions(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				errCode = "没有内容";
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			JsonNode data = root.get("data");
			List<LSCollection> temp = LSFragment.mapper.readValue(data.traverse(), new TypeReference<List<LSCollection>>() {
			});
			collections.clear();
			collections.addAll(temp);
			postMessage(SHOW_UI);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == SHOW_UI) {
			if (adapter == null) {
				adapter = new CollectionAdapter(this, collections);
				View footer = LayoutInflater.from(this).inflate(R.layout.collection_list_footer, null);
				listView.addFooterView(footer);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
			return true;
		}
		return super.handleMessage(msg);
	}
	
	
}
