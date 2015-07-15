package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.myactivty.CommentLajis;
import com.lis99.mobile.myactivty.Comments;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

public class LsActivityComment extends ActivityPattern1 {

	ImageView iv_back;
	ListView lv_comment_list;
	EditText et_comment;
	Button bt_comment;
	String comment, type;
	private static final int SHOW_COMMENTLIST = 203;
	private static final int REFRESH_COMMENT = 204;
	private Bundle bundle;
	int id;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_activity_comment);

		StatusUtil.setStatusBar(this);

		bundle = this.getIntent().getExtras();
		id = bundle.getInt("id");

		setView();
		setListener();
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		getCommentList(id);
	}

	private void getCommentList(int id) {
		String url = C.ACTIVITY_USER_COMMENTLIST_URL + id;
		Task task = new Task(null, url, null, "ACTIVITY_USER_COMMENTLIST_URL",
				this);
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
				if (((String) task.getParameter())
						.equals("ACTIVITY_USER_COMMENTLIST_URL")) {
					Log.i("aa", "走到这一步了没,这是判断啊");
					parserCommentList(result);
				} else if (((String) task.getParameter())
						.equals("ACTIVITY_LAJI_COMMENTLIST_URL")) {
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
			if ("OK".equals(result)) {
				DataManager.getInstance().jsonParse(params,
						DataManager.TYPE_MAIN_COMMENT);
				postMessage(REFRESH_COMMENT);
			} else {
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}
	}

	List<Comments> list_lj;
	CommentLajis cccccc = new CommentLajis();

	private void parserCommentList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				cccccc = (CommentLajis) DataManager.getInstance().jsonParse(
						params, DataManager.TYPE_LAJI_INFO);

				list_lj = cccccc.getComments();

				Log.i("aa", cccccc + "hehefuhehe");
				postMessage(SHOW_COMMENTLIST);
			} else {
				postMessage(DISMISS_PROGRESS);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}
	}

	ListLajiAdapter adapter;

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_COMMENTLIST:
			Log.i("aa", "走到这一步了没");
			if (list_lj != null && list_lj.size() > 0) {
				adapter = new ListLajiAdapter();
				lv_comment_list.setAdapter(adapter);
			}
			postMessage(DISMISS_PROGRESS);
			break;
		case REFRESH_COMMENT:
			postMessage(POPUP_TOAST, "评论成功");
			et_comment.setText("");
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.hideSoftInputFromWindow(getWindow().getDecorView()
						.getWindowToken(), 0);
			}
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
		if (v.getId() == iv_back.getId()) {
			finish();
		} else if (v.getId() == bt_comment.getId()) {
			if (!(DataManager.getInstance().getUser().getUser_id() != null && !""
					.equals(DataManager.getInstance().getUser().getUser_id()))) {
				Toast.makeText(this, "请先登录，再进行该操作。", 0).show();
				Intent intent = new Intent(this, LSLoginActivity.class);
				startActivity(intent);
				return;
			}
			comment = et_comment.getText().toString();
			if (comment == null || "".equals(comment)) {
				Toast.makeText(this, "评论内容不能为空", 0).show();
			} else {
				postMessage(POPUP_PROGRESS, getString(R.string.sending));
				sendCommentTask();
			}
		}
	}

	private void sendCommentTask() {
		String url = C.ACTIVITY_LAJI_COMMENTLIST_URL + id;
		Task task = new Task(null, url, null, "ACTIVITY_LAJI_COMMENTLIST_URL",
				this);
		task.setPostData(creatCommentParam().getBytes());
		publishTask(task, IEvent.IO);
	}

	private String creatCommentParam() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		params.put("comment", comment);
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}

	private static class ViewHolder {
		ImageView item_comment_heads;
		TextView item_comment_nickname, item_comment_date, item_comment;
	}

	private class ListLajiAdapter extends BaseAdapter {
		LayoutInflater inflater;

		public ListLajiAdapter() {
			inflater = LayoutInflater.from(LsActivityComment.this);
		}

		@Override
		public int getCount() {
			return list_lj.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_lj.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final int pos = position;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.ls_activity_comment_items, null);
				holder = new ViewHolder();
				holder.item_comment_heads = (ImageView) convertView
						.findViewById(R.id.item_comment_heads);
				holder.item_comment_nickname = (TextView) convertView
						.findViewById(R.id.item_comment_nickname);
				holder.item_comment_date = (TextView) convertView
						.findViewById(R.id.item_comment_date);
				holder.item_comment = (TextView) convertView
						.findViewById(R.id.item_comment);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			imageLoader.displayImage(list_lj.get(position).getHeadicon(),
					holder.item_comment_heads, options);
			holder.item_comment_nickname.setText(list_lj.get(position)
					.getNickname());
			holder.item_comment_date.setText(list_lj.get(position)
					.getCreatetime());
			holder.item_comment.setText(list_lj.get(position).getComment());
			return convertView;
		}
	}
}
