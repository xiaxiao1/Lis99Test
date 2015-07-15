package com.lis99.mobile.entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.LsEquiCateActivity;
import com.lis99.mobile.R;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entity.bean.LSCate;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LsEquiFilterActivity extends ActivityPattern1 {
	
	private TextView leftBtn;
	private TextView rightBtn;
	private View selectedBtn;
	
	
	List<LSCate> typeCates = new ArrayList<LSCate>();
	List<LSCate> sportCates = new ArrayList<LSCate>();
	List<LSCate> currentCates = typeCates;
	
	ExpandableListView listView ;
	
	private final static int SHOW_CATES = 111;
	
	LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ls_equi_filter);

		StatusUtil.setStatusBar(this);
		
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		leftBtn = (TextView) findViewById(R.id.lsBtn);
		rightBtn = (TextView) findViewById(R.id.lineBtn);

		selectedBtn = leftBtn;

		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		
		View backBtn = findViewById(R.id.iv_back);
		backBtn.setOnClickListener(this);
		
		listView = (ExpandableListView) findViewById(R.id.expandableListView);
		listView.setAdapter(adapter);
		
		listView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				LSCate cate = (LSCate) adapter.getChild(groupPosition, childPosition);
				Intent intent = new Intent(LsEquiFilterActivity.this,LsEquiCateActivity.class);
				if(currentCates == typeCates){
					intent.putExtra("cate", cate.getID());
				}else{
					intent.putExtra("sport", cate.getID());
				}
				intent.putExtra("title", cate.getName());
				startActivity(intent);
				return false;
			}
		});
		
		listView.setOnGroupClickListener(new OnGroupClickListener() {
		    @Override
		    public boolean onGroupClick(ExpandableListView parent, View clickedView, int groupPosition, long rowId) {
		        ImageView groupIndicator = (ImageView) clickedView.findViewById(R.id.iv_icon);
		        if (parent.isGroupExpanded(groupPosition)) {
		            parent.collapseGroup(groupPosition);
		            groupIndicator.setImageResource(R.drawable.icon_expand);
		        } else {
		            parent.expandGroup(groupPosition);
		            groupIndicator.setImageResource(R.drawable.icon_collapse);
		        }
		        return true;
		    }
		});
		
		getLsCate();
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.lsBtn:
			if (selectedBtn != leftBtn) {
				leftBtn.setBackgroundResource(R.drawable.activity_leftbtn_sel);
				leftBtn.setTextColor(Color.WHITE);
				rightBtn.setBackgroundResource(R.drawable.activity_rightbtn);
				rightBtn.setTextColor(Color.rgb(0x2a, 0xcb, 0xc2));
				selectedBtn = leftBtn;
				currentCates = typeCates;
				
				int groupCount = adapter.getGroupCount();  
				 for (int i=0; i<groupCount; i++){   
					 listView.collapseGroup(i);  
				  }
				 adapter.notifyDataSetChanged();
			}
			break;

		case R.id.lineBtn:
			if (selectedBtn != rightBtn) {
				leftBtn.setBackgroundResource(R.drawable.activity_leftbtn);
				leftBtn.setTextColor(Color.rgb(0x2a, 0xcb, 0xc2));
				rightBtn.setBackgroundResource(R.drawable.activity_rightbtn_sel);
				rightBtn.setTextColor(Color.WHITE);
				selectedBtn = rightBtn;
				currentCates = sportCates;
				adapter.notifyDataSetChanged();
				int groupCount = adapter.getGroupCount();  
				 for (int i=0; i<groupCount; i++){   
					 listView.collapseGroup(i);  
				  }
			}
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
				cate.setParentID(obj.optInt("parentid", 0));
				JSONArray subArray = obj.optJSONArray("list");
				if(subArray != null){
					for (int j = 0; j < subArray.length(); j++) {
						JSONObject subObj = subArray.getJSONObject(j);
						LSCate subCate = new LSCate();
						subCate.setName(subObj.optString("catname",""));
						subCate.setID(subObj.optInt("id", 0));
						subCate.setParentID(subObj.optInt("parentid", 0));
						cate.addSubCate(subCate);
					}
				}
				typeCates.add(cate);
			}
			
			JSONArray sportArray = info.getJSONObject("data").getJSONArray("sports_cates");
			for (int i = 0; i < sportArray.length(); i++) {
				JSONObject obj = sportArray.getJSONObject(i);
				LSCate cate = new LSCate();
				cate.setName(obj.optString("title",""));
				cate.setID(obj.optInt("id", 0));
				cate.setParentID(obj.optInt("parentid", 0));
				JSONArray subArray = obj.optJSONArray("list");
				if(subArray != null){
					for (int j = 0; j < subArray.length(); j++) {
						JSONObject subObj = subArray.getJSONObject(j);
						LSCate subCate = new LSCate();
						subCate.setName(subObj.optString("title",""));
						subCate.setID(subObj.optInt("id", 0));
						subCate.setParentID(subObj.optInt("parentid", 0));
						cate.addSubCate(subCate);
					}
				}
				sportCates.add(cate);
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
			adapter.notifyDataSetChanged();
			break;
		}
		return true;
	}
	
	
	final BaseExpandableListAdapter adapter = new BaseExpandableListAdapter() {
        

        
        //重写ExpandableListAdapter中的各个方法
        @Override
        public int getGroupCount() {
            return currentCates.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return currentCates.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
        	LSCate cate = (LSCate) getGroup(groupPosition);
            return cate.getSubCates() == null ? 0 : cate.getSubCates().size();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
        	LSCate cate = (LSCate) getGroup(groupPosition);
            return cate.getSubCates().get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
            if(convertView == null){
            	//TextView textView = getTextView();
                //convertView = textView;
            	convertView = inflater.inflate(R.layout.cate_group_item, null);
            }
            LSCate cate = (LSCate) getGroup(groupPosition);
            TextView nameView = (TextView) convertView.findViewById(R.id.tv_cateName);
            nameView.setText(cate.getName());
            ImageView iconView = (ImageView) convertView.findViewById(R.id.iv_icon);
            if(isExpanded){
            	iconView.setImageResource(R.drawable.icon_collapse);
            }else{
            	iconView.setImageResource(R.drawable.icon_expand);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
        	 if(convertView == null){
        		 convertView = inflater.inflate(R.layout.cate_item, null);
             }
             LSCate cate = (LSCate) getChild(groupPosition, childPosition);
             ((TextView)convertView).setText(cate.getName());
             return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition,
                int childPosition) {
            return true;
        }

    };

	
}
