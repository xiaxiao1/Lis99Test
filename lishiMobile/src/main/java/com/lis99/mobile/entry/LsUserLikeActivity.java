package com.lis99.mobile.entry;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ZhuangbeiBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.FlowTag;
import com.lis99.mobile.entry.view.LazyScrollView;
import com.lis99.mobile.entry.view.ZhuangbeiView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LsUserLikeActivity extends ActivityPattern {

	ImageView iv_back;
	private Handler handler1;
	List<ZhuangbeiBean> getlist;
	List<ZhuangbeiBean> dangji_lists = new ArrayList<ZhuangbeiBean>();
	private LazyScrollView waterfall_scroll;
	private LinearLayout waterfall_container;
	private ArrayList<LinearLayout> waterfall_items;
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	private Display display;
	
	private int column_count = 2;// 显示列数
	private int page_count = 40;// 每次加载30张图片

	private int current_page = 0;// 当前页数

	private int[] topIndex;
	private int[] bottomIndex;
	private int[] lineIndex;
	private int[] column_height;// 每列的高度
	private HashMap<Integer, String> pins;
	private int loaded_count = 0;// 已加载数量
	private HashMap<Integer, ZhuangbeiView> iviews;
	private HashMap<Integer, Integer>[] pin_mark = null;
	private Context context;

	int scroll_height;
	private final int REFRESH = 100;
	
	private int item_width;
	private void showPic() {
		display = this.getWindowManager().getDefaultDisplay();
		item_width = display.getWidth() / column_count;// 根据屏幕大小计算每列大小

		column_height = new int[column_count];
		context = this;
		iviews = new HashMap<Integer, ZhuangbeiView>();
		pins = new HashMap<Integer, String>();
		pin_mark = new HashMap[column_count];

		this.lineIndex = new int[column_count];
		this.bottomIndex = new int[column_count];
		this.topIndex = new int[column_count];

		for (int i = 0; i < column_count; i++) {
			lineIndex[i] = -1;
			bottomIndex[i] = -1;
			pin_mark[i] = new HashMap();
		}

		InitLayout();
		
	}
	
	private void InitLayout() {
		waterfall_scroll = (LazyScrollView) findViewById(R.id.waterfall_scroll);
		waterfall_scroll.getView();
		waterfall_scroll.setOnScrollListener(new com.lis99.mobile.entry.view.LazyScrollView.OnScrollListener() {

			@Override
			public void onTop() {
				// 滚动到最顶端
				Log.d("LazyScroll", "Scroll to top");
			}

			@Override
			public void onScroll() {

			}

			@Override
			public void onBottom() {
				// 滚动到最低端
				/*start_pid = Integer.parseInt(lists.get(lists.size()-1).getPic_id());
				socketInit();
				//postMessage(POPUP_PROGRESS, "加载中");
				//more.setText("正在加载更新");
				socketManager.sendData(getSendMsg(start_pid,count));*/
				/*postMessage(POPUP_PROGRESS,getString(R.string.sending));
				offset = offset + limits;
				getZhuangbeiList(offset,limits);
				++current_page;*/
			}

			@Override
			public void onAutoScroll(int l, int t, int oldl, int oldt) {

				// Log.d("MainActivity",
				// String.format("%d  %d  %d  %d", l, t, oldl, oldt));

				// Log.d("MainActivity", "range:" + range);
				// Log.d("MainActivity", "range-t:" + (range - t));
				scroll_height = waterfall_scroll.getMeasuredHeight();
				Log.d("MainActivity", "scroll_height:" + scroll_height);

				if (t > oldt) {// 向下滚动
					if (t > 2 * scroll_height) {// 超过两屏幕后

						for (int k = 0; k < column_count; k++) {

							LinearLayout localLinearLayout = waterfall_items
									.get(k);

							if (pin_mark[k].get(Math.min(bottomIndex[k] + 1,
									lineIndex[k])) <= t + 3 * scroll_height) {// 最底部的图片位置小于当前t+3*屏幕高度
								ZhuangbeiView vv = ((ZhuangbeiView) waterfall_items.get(k).getChildAt(
										Math.min(1 + bottomIndex[k],
												lineIndex[k])));
								if(vv !=null)
									
									vv.Reload();

									bottomIndex[k] = Math.min(1 + bottomIndex[k],
											lineIndex[k]);
								

							}
							Log.d("MainActivity",
									"headIndex:" + topIndex[k] + "  footIndex:"
											+ bottomIndex[k] + "  headHeight:"
											+ pin_mark[k].get(topIndex[k]));
							if (pin_mark[k].get(topIndex[k]) < t - 2
									* scroll_height) {// 未回收图片的最高位置<t-两倍屏幕高度

								int i1 = topIndex[k];
								topIndex[k]++;
								/*ZhuangbeiView vv1 = ((ZhuangbeiView) localLinearLayout.getChildAt(i1));
								if(vv1 !=null){
									//deleteItems(vv1);
									//vv1.recycle();
								}*/
								//if(((FlowView) localLinearLayout.getChildAt(i1))!=null)
								//deleteItems(((FlowView) localLinearLayout.getChildAt(i1)));
								Log.d("MainActivity", "recycle,k:" + k
										+ " headindex:" + topIndex[k]);
								

							}
						}

					}
				} else {// 向上滚动
					if (t > 2 * scroll_height) {// 超过两屏幕后
						for (int k = 0; k < column_count; k++) {
							LinearLayout localLinearLayout = waterfall_items
									.get(k);
							if (pin_mark[k].get(bottomIndex[k]) > t + 3
									* scroll_height) {
								//if((FlowView) localLinearLayout
								//		.getChildAt(bottomIndex[k])!=null)
								//deleteItems((FlowView) localLinearLayout
								//		.getChildAt(bottomIndex[k]));
								
								/*ZhuangbeiView vv2 = ((ZhuangbeiView) localLinearLayout
										.getChildAt(bottomIndex[k]));
								if(vv2 !=null){
									//deleteItems(vv2);
									//vv2.recycle();
								}*/
								

									bottomIndex[k]--;
								
							}

							if (pin_mark[k].get(Math.max(topIndex[k] - 1, 0)) >= t
									- 2 * scroll_height) {
								ZhuangbeiView v2 = ((ZhuangbeiView) localLinearLayout.getChildAt(Math
										.max(-1 + topIndex[k], 0)));
								if(v2 !=null)
									v2.Reload();
									topIndex[k] = Math.max(topIndex[k] - 1, 0);
								
								
								
							}
						}
					}

				}

			}
		});

		waterfall_container = (LinearLayout) this
				.findViewById(R.id.waterfall_container);
		


		handler1 = new Handler() {

			@Override
			public void dispatchMessage(Message msg) {

				super.dispatchMessage(msg);
			}

			@Override
			public void handleMessage(Message msg) {

				// super.handleMessage(msg);

				switch (msg.what) {
				case 1:

					//FlowView v = (FlowView) msg.obj;
					ZhuangbeiView v = (ZhuangbeiView) msg.obj;
					LinearLayout ll = (LinearLayout) v.getParent();
					if(ll==null){
					//deleteItems(v);
						int w = msg.arg1;
						int h = msg.arg2;
						// Log.d("MainActivity",
						// String.format(
						// "获取实际View高度:%d,ID：%d,columnIndex:%d,rowIndex:%d,filename:%s",
						// v.getHeight(), v.getId(), v
						// .getColumnIndex(), v.getRowIndex(),
						// v.getFlowTag().getFileName()));
						String f = v.getFlowTag().getFileName();
	
						// 此处计算列值
						int columnIndex = GetMinValue(column_height);
	
						v.setColumnIndex(columnIndex);
	
						column_height[columnIndex] += h;
	
						pins.put(v.getId(), f);
						iviews.put(v.getId(), v);
						
						waterfall_items.get(columnIndex).addView(v);
						//if(parent == null)
	
						lineIndex[columnIndex]++;
	
						pin_mark[columnIndex].put(lineIndex[columnIndex],
								column_height[columnIndex]);
						bottomIndex[columnIndex] = lineIndex[columnIndex];
					}
						
					break;
				}

			}

			@Override
			public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
				return super.sendMessageAtTime(msg, uptimeMillis);
			}
		};

		waterfall_items = new ArrayList<LinearLayout>();
		//waterfall_container.removeAllViews();
		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width, LayoutParams.WRAP_CONTENT);
			//itemLayout.setPadding(StringUtil.dip2px(LsXuanActivity1.this, 8), StringUtil.dip2px(LsXuanActivity1.this, 8), 0, StringUtil.dip2px(LsXuanActivity1.this, 8));
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			waterfall_items.add(itemLayout);
			waterfall_container.addView(itemLayout);
		}

		// 加载所有图片路径

		/*try {
			image_filenames = Arrays.asList(asset_manager.list(image_path));

		} catch (IOException e) {
			e.printStackTrace();
		}*/
		// 第一次加载
		AddItemToContainer(current_page, page_count);
	}
	private void AddItemToContainer(int pageindex, int pagecount) {
		int currentIndex = pageindex * pagecount;

		int imagecount = dangji_lists.size();// image_filenames.size();
		for (int i = currentIndex; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			loaded_count++;
			Random rand = new Random();
			//int r = rand.nextInt(lists.size());
			
			AddImage(dangji_lists.get(i),
					(int) Math.ceil(loaded_count / (double) column_count),
					loaded_count);
		}
		
	}
	
	private void AddImage(ZhuangbeiBean picture, int rowIndex, int id) {
		
		
		ZhuangbeiView item = new ZhuangbeiView(this);
		//FlowView item = new FlowView(context);
		// item.setColumnIndex(columnIndex);

		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(this.handler1);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		ll.width = StringUtil.getXY(this)[0]/2;
		item.setLayoutParams(ll);
		
		item.getLs_zhuangbei_item_title().setText(picture.getTitle());
		item.getLs_zhuangbei_item_score().setText(picture.getScore());
		item.getLs_zhuangbei_item_star().removeAllViews();
		int score = (int)(Float.parseFloat(picture.getScore()));
		for(int i=0;i<score;i++){
			ImageView iv = new ImageView(LsUserLikeActivity.this);
			iv.setImageResource(R.drawable.ls_zhuangbei_star_icon);
			LinearLayout.LayoutParams ll1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			iv.setLayoutParams(ll1);
			item.getLs_zhuangbei_item_star().addView(iv);
		}
		
		// 多线程参数
		FlowTag param = new FlowTag();
		param.setFlowId(Integer.parseInt(picture.getId()));
		param.setAssetManager(asset_manager);
		param.setFileName(picture.getThumb());
		param.setItemWidth(item_width);

		item.setFlowTag(param);
		//item.setImage(filename, null, null);
		item.LoadImage();
		// waterfall_items.get(columnIndex).addView(item);

	}
	private AssetManager asset_manager;
	private List<String> image_filenames;
	private final String image_path = "images";
	private int GetMinValue(int[] array) {
		int m = 0;
		int length = array.length;
		for (int i = 0; i < length; ++i) {

			if (array[i] < array[m]) {
				m = i;
			}
		}
		return m;
	}
	LayoutInflater inflater;
	int offset = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_user_like);
		StatusUtil.setStatusBar(this);
		inflater = LayoutInflater.from(LsUserLikeActivity.this);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getZhuangbeiList(offset);
	}
	private void getZhuangbeiList(int offset2) {
		String url = C.USER_ZHUANGBEI_URL + offset2;
		//String url = C.ZHUANGBEI_ITEM_URL + offset2 + "/" + 40;
		Task task = new Task(null, url, null, "ZHUANGBEI_LIST_URL", this);
		task.setPostData(getSendParams().getBytes());
		publishTask(task, IEvent.IO);
	}
	private String getSendParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}
	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("ZHUANGBEI_LIST_URL")) {
					parserZhuangbeiList(result);
				}
			}
			break;
		default:
			break;
		}
		
		//refreshContainer.onRefreshComplete();
	}
	private void parserZhuangbeiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				getlist = (List<ZhuangbeiBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_ZHUANGBEI_LIST);
				postMessage(SHOW_ZHUANGBEI_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}
		
	}
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ZHUANGBEI_LIST:
			dangji_lists.addAll(getlist);
			if(current_page==0){
				showPic();
			}else{
				AddItemToContainer(current_page, page_count);
			}
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}

	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}

	private void setListener() {
		iv_back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}
	}
}
