package com.lis99.mobile.newhome;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.IEventHandler;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.util.PushManager;
import com.lis99.mobile.util.StatusUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;


public class LSFragment extends Fragment implements IEventHandler{
	
	protected final static int default_requestCode = 1024;
	protected int checked_paddingLeft;
	
	protected Handler handler;
	
	protected View body;
	protected DisplayImageOptions options;
	
	public void setListHeightBasedOnChildren(ListView listView) {

		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
	
	public void setGridHeightBasedOnChildren(GridView listView) {

		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			if (i % 4 == 0) {
				totalHeight += listItem.getMeasuredHeight();
			}
			// 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight;
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
	
	public final static ObjectMapper mapper = new ObjectMapper();
	
	static{
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	private void buildOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.image_empty)
				.showImageForEmptyUri(R.drawable.image_empty) 
				.showImageOnFail(R.drawable.image_empty) 
				.cacheInMemory(false) 
				.cacheOnDisk(true) 
				.build();
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LSBaseActivity.activity = getActivity();
		handler = new Handler();
		buildOptions();
		
		PushManager.getInstance().onAppStart(getActivity());

		StatusUtil.setStatusBar(getActivity());

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initViews(container);
		
		View titleLeft = findViewById(R.id.titleLeft);
		if(titleLeft != null){
			titleLeft.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					leftAction();
				}
			});
		}
		
		return body;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected void initViews(ViewGroup container){
		
	}

	
	protected void leftAction(){
//		if(getLSActivity() == null)
//			return;
//		getLSActivity().toggleMenu();
	}
	
	protected void rightAction(){
	}
	
	
	
	protected View findViewById(int id){
		if(body == null)
			return null;
		return body.findViewById(id);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}

	protected void setTitle(String title){
		TextView titleView = (TextView) body.findViewById(R.id.title);
		if (titleView != null) {
			titleView.setText(title);
		}
	}
	
    @Override
    public void onResume() {
    	LSBaseActivity.activity = getActivity();
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    public NewHomeActivity getLSActivity(){
    	if(getActivity() == null)
    		return null;
    	return (NewHomeActivity)getActivity();
    }
    
    public void postMessage(int operation, Object params) {
    	if(getLSActivity() == null)
    		return;
    	getLSActivity().postMessage(operation, params);
	}
    
    public boolean publishTask(Task task, int taskType){
    	if(getLSActivity() == null)
    		return false;
    	return getLSActivity().publishTask(task, taskType);
    }
    
    @Override
	public void handleTask(int initiator, Task task, int operation) {
		if (task == null) {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
			return;
		}
		if (task.isFailed()) {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
			return;
		}
		if (task.isCanceled()) {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
			return;
		}
		
		if (initiator == IEvent.IO && task.getData() instanceof byte[]) {
			String result = new String((byte[]) task.getData());
			String param = (String) task.getParameter();
			
			
			try {
				JsonNode root = mapper.readTree(result);
				String errCode = root.get("status").asText("");
				if (!"OK".equals(errCode)) {
					handleHttpRequestError(root.get("msg").asText("网络请求失败"), param);
					return;
				}
				JsonNode data = root.get("data");
				handleHttpResponseData(data, param);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				postMessage(ActivityPattern1.DISMISS_PROGRESS);
			}
		}
	}
	
	public void handleHttpRequestError(String errMsg, String param) {
		postMessage(ActivityPattern1.POPUP_TOAST, errMsg);
	}
	
	public void handleHttpResponseData(JsonNode dataNode, String param){
		
	}
    
    public void cancelIOTask(Object owner) {
    	if(getLSActivity() == null)
    		return;
    	getLSActivity().cancelIOTask(owner);
    }
    
    public void postMessage(int operation) {
		postMessage(operation, null);
	}
    
    public boolean handleMessage(Message msg) {
    	return false;
    }
    
    public void postMessage(int operation, String title, int itemsId,
			DialogInterface.OnClickListener listener){
    	if(getLSActivity() == null)
    		return;
    	getLSActivity().postMessage(operation, title, itemsId, listener);
    }
    
    public void registerReceiver(BroadcastReceiver receiver, IntentFilter intentFilter) {
    	if(getActivity() != null)
    		getActivity().registerReceiver(receiver, intentFilter);
    }
    
    public void unregisterReceiver(BroadcastReceiver receiver) {
    	if(getActivity() != null)
    		getActivity().unregisterReceiver(receiver);
    }

}
