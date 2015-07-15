package com.lis99.mobile.entry;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.CommentBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import java.util.HashMap;
import java.util.List;

public class LsShaiCommentActivity extends ActivityPattern {

	ImageView iv_back;
	ListView lv_comment_list;
	EditText et_comment;
	Button bt_comment;
	String id,comment,type;
	private static final int SHOW_COMMENTLIST = 203;
	private static final int REFRESH_COMMENT = 204;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_shai_comment);
		StatusUtil.setStatusBar(this);
		id = getIntent().getStringExtra("id");
		type = getIntent().getStringExtra("type");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getCommentList(id);
	}
	private void getCommentList(String id) {
		String url = C.MAIN_COMMENTLIST_URL + type + "/" +id+ "/" +"0" +"/"+10+"/" + "desc";
		Task task = new Task(null, url, null, "MAIN_COMMENTLIST_URL", this);
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
				if(((String) task.getParameter()).equals("MAIN_COMMENTLIST_URL")){
					parserCommentList(result);
				}else if(((String) task.getParameter()).equals("MAIN_COMMENT_URL")){
					parserComment(result);
				}
			}
			break;
		default:
			break;
		}
		
	}
	private void parserComment(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				DataManager.getInstance().jsonParse(params, DataManager.TYPE_MAIN_COMMENT);
				postMessage(REFRESH_COMMENT);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}else{
			postMessage(DISMISS_PROGRESS);
		}
	}
	List<CommentBean> list_cb;
	private void parserCommentList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				list_cb = (List<CommentBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_MAIN_COMMENTLIST);
				postMessage(SHOW_COMMENTLIST);
			}else{
				postMessage(DISMISS_PROGRESS);
			}
		}else{
			postMessage(DISMISS_PROGRESS);
		}
	}
	CommentListAdapter adapter;
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_COMMENTLIST:
			if(list_cb !=null && list_cb.size()>0){
				adapter = new CommentListAdapter();
				lv_comment_list.setAdapter(adapter);
			}else{
				
			}
			postMessage(DISMISS_PROGRESS);
			break;
		case REFRESH_COMMENT:
			postMessage(POPUP_TOAST, "评论成功");
			getCommentList(id);
			break;
		}
		return true;
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv_comment_list = (ListView) findViewById(R.id.lv_comment_list);
		et_comment = (EditText) findViewById(R.id.et_comment);
		bt_comment = (Button) findViewById(R.id.bt_comment);
	}

	private void setListener() {
		iv_back.setOnClickListener(this);
		bt_comment.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == bt_comment.getId()){
			comment = et_comment.getText().toString();
			if(comment==null || "".equals(comment)){
				Toast.makeText(this, "评论内容不能为空", 0).show();
			}else{
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				sendCommentTask();
			}
		}
	}
	
	private void sendCommentTask() {
		String url = C.MAIN_COMMENT_URL;
		Task task = new Task(null, url, null, "MAIN_COMMENT_URL", this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);
	}
	private String getLoginParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pid", "0");
		params.put("rid", "0");
		params.put("type", type);
		params.put("comment", comment);
		params.put("id", id);
		params.put("touser", "0");
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}
	private static class ViewHolder{
		AsyncLoadImageView item_comment_head;
		TextView item_comment_nickname,item_comment_date,item_comment;
	}
	private class CommentListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public CommentListAdapter() {
			inflater = LayoutInflater.from(LsShaiCommentActivity.this);
		}

		@Override
		public int getCount() {
			return list_cb.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_cb.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			CommentBean cb = list_cb.get(position);
			final int pos = position;
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_zhuangbei_comment_item, null);
					holder=new ViewHolder();
					holder.item_comment_head = (AsyncLoadImageView) convertView.findViewById(R.id.item_comment_head);
					holder.item_comment_nickname = (TextView) convertView.findViewById(R.id.item_comment_nickname);
					holder.item_comment_date = (TextView) convertView.findViewById(R.id.item_comment_date);
					holder.item_comment = (TextView) convertView.findViewById(R.id.item_comment);
					convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.item_comment_head.setImage(cb.getHeadicon(), null, null,"zhuangbei_detail");
			holder.item_comment_nickname.setText(cb.getNickname());
			holder.item_comment_date.setText(cb.getCreatedate());
			holder.item_comment.setText(cb.getComment());
			return convertView;
		}
		
	}
}
