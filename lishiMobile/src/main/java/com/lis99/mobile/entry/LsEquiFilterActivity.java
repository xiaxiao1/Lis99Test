package com.lis99.mobile.entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.LsEquiCateActivity;
import com.lis99.mobile.R;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entity.bean.LSCate;
import com.lis99.mobile.entry.adapter.LSEquipFilterAdapter;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LsEquiFilterActivity extends ActivityPattern1 {
	

	List<LSCate> typeCates = new ArrayList<LSCate>();
	List<LSCate> sportCates = new ArrayList<LSCate>();
	List<LSCate> currentCates = typeCates;
	
	ListView listView ;


	TextView categoryView;
	TextView sportView;

	View categoryLine;
	View sportLine;

	LSEquipFilterAdapter adapter;


	TextView selectView;
	
	private final static int SHOW_CATES = 111;
	
	LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ls_equi_filter);

		StatusUtil.setStatusBar(this);
		
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		categoryView = (TextView) findViewById(R.id.categoryView);
		sportView = (TextView) findViewById(R.id.sportView);
		categoryLine = findViewById(R.id.categoryLine);
		sportLine = findViewById(R.id.sportLine);

		selectView = categoryView;


		
		View view = findViewById(R.id.iv_back);
		view.setOnClickListener(this);

		view = findViewById(R.id.categoryPanel);
		view.setOnClickListener(this);

		view = findViewById(R.id.sportPanel);
		view.setOnClickListener(this);
		
		listView = (ListView) findViewById(R.id.listView);
		adapter = new LSEquipFilterAdapter(this, currentCates);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LSCate cate = adapter.getItem(position);
				if (cate.getType() == LSCate.LSCATE_VIRTUAL) {
					return;
				}
				Intent intent = new Intent(LsEquiFilterActivity.this,LsEquiCateActivity.class);
				if(currentCates == typeCates){
					intent.putExtra("cate", cate.getID());
				}else{
					intent.putExtra("sport", cate.getID());
				}
				intent.putExtra("title", cate.getName());
				startActivity(intent);
			}
		});
		

		getLsCate();
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.categoryPanel) {
			if (selectView == categoryView)
				return;
			selectView = categoryView;
			categoryView.setTextColor(getResources().getColor(R.color.text_color_blue));
			categoryLine.setVisibility(View.VISIBLE);

			sportLine.setVisibility(View.GONE);
			sportView.setTextColor(Color.rgb(0x66, 0x66, 0x66));

			adapter.setData(typeCates);

//			if (clubs.size() == 0) {
//				loadClubList();
//			} else {
//				postMessage(SHOW_CLUB);
//			}
			return;
		} else if (view.getId() == R.id.sportPanel) {
			if (selectView == sportView)
				return;

			selectView = sportView;
			sportView.setTextColor(getResources().getColor(R.color.text_color_blue));
			categoryLine.setVisibility(View.GONE);
			sportLine.setVisibility(View.VISIBLE);
			categoryView.setTextColor(Color.rgb(0x66, 0x66, 0x66));

			currentCates = sportCates;

			adapter.setData(sportCates);


//			if (allClubs.size() == 0) {
//				loadLishiClubList();
//			} else {
//				postMessage(SHOW_LISH_CLUB);
//			}
			return;
		}


		switch (view.getId()) {
		case R.id.iv_back:
			finish();
			break;


		default:
			break;
		}
	}
	
	
	
	private void getLsCate() {
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		String url = C.ZHUANGBEI_CATE;
		Task task = new Task(null, url, null, "ZHUANGBEI_CATE", this);
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
				String param = (String) task.getParameter();
				if ("ZHUANGBEI_CATE".equals(param)) {
					parserLsCateList(result);
				} 
			}
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserLsCateList(String result) {
		try {
			JSONObject info = new JSONObject(result);
			JSONArray typeArray = info.getJSONObject("data").getJSONArray("cateList");
			for (int i = 0; i < typeArray.length(); i++) {
				JSONObject obj = typeArray.getJSONObject(i);
				LSCate cate = new LSCate();
				cate.setName(obj.optString("catname",""));
				cate.setID(obj.optInt("id", 0));
				cate.setType(LSCate.LSCATE_VIRTUAL);
				cate.setParentID(obj.optInt("parentid", 0));
				typeCates.add(cate);
				JSONArray subArray = obj.optJSONArray("list");
				if(subArray != null){
					for (int j = 0; j < subArray.length(); j++) {
						JSONObject subObj = subArray.getJSONObject(j);
						LSCate subCate = new LSCate();
						subCate.setName(subObj.optString("catname", ""));
						subCate.setID(subObj.optInt("id", 0));
						subCate.setParentID(subObj.optInt("parentid", 0));
						typeCates.add(subCate);
//						cate.addSubCate(subCate);
					}
				}
//				typeCates.add(cate);
			}
			
			JSONArray sportArray = info.getJSONObject("data").getJSONArray("sports_cates");
			for (int i = 0; i < sportArray.length(); i++) {
				JSONObject obj = sportArray.getJSONObject(i);
				LSCate cate = new LSCate();
				cate.setName(obj.optString("title",""));
				cate.setID(obj.optInt("id", 0));
				cate.setParentID(obj.optInt("parentid", 0));
				cate.setType(LSCate.LSCATE_VIRTUAL);
				sportCates.add(cate);
				JSONArray subArray = obj.optJSONArray("list");
				if(subArray != null){
					for (int j = 0; j < subArray.length(); j++) {
						JSONObject subObj = subArray.getJSONObject(j);
						LSCate subCate = new LSCate();
						subCate.setName(subObj.optString("title",""));
						subCate.setID(subObj.optInt("id", 0));
						subCate.setParentID(subObj.optInt("parentid", 0));
						sportCates.add(subCate);
//						cate.addSubCate(subCate);
					}
				}
//				sportCates.add(cate);
			}
			postMessage(SHOW_CATES);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_CATES:
			adapter.setData(currentCates);
//			adapter.notifyDataSetChanged();
			break;
		}
		return true;
	}
	
	

	
}
