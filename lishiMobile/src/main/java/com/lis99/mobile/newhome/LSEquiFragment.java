package com.lis99.mobile.newhome;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ZhuangbeiBean;
import com.lis99.mobile.application.data.ZhuanjiBean;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.LsEquiFilterActivity;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.FlowTag;
import com.lis99.mobile.entry.view.LazyScrollView;
import com.lis99.mobile.entry.view.ZhuangbeiView;
import com.lis99.mobile.entry.view.ZhuanjiView;
import com.lis99.mobile.entry.view.scroll.PullToRefreshBase.OnRefreshListener;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LSEquiFragment extends LSBaseActivity implements OnRefreshListener,
		OnClickListener {

	View iv_home;
	int offset = 0;
	int limits = 20;
	int zhuanji_offset = 0;
	int zhuanji_limits = 10;
	private Handler handler1;
	private Handler handler2;
	List<ZhuangbeiBean> getlist;
	List<ZhuangbeiBean> dangji_lists = new ArrayList<ZhuangbeiBean>();
	List<ZhuanjiBean> zhuanji_lists = new ArrayList<ZhuanjiBean>();
	private LazyScrollView waterfall_scroll;
	private LinearLayout waterfall_container;
	private LazyScrollView waterfall_scroll1;
	private LinearLayout waterfall_container1;
	private ArrayList<LinearLayout> waterfall_items;
	private ArrayList<LinearLayout> waterfall_items1;
	private static final int SHOW_ZHUANGBEI_LIST = 2001;
	private static final int SHOW_ZHUANJI_LIST = 2002;
	private static final int SHOW_ZHUANGBEI_CONTENT = 2003;


//	private PullToRefreshScrollView refreshContainer;
//	private ScrollView refreshScrollView;
//	private AutoResizeListView ls_remen_lv;

//	private com.huewu.pla.lib.MultiColumnListView mAdapterView = null;
//	ZhuangbeiAdapter adapter;
//	ZhuanjiAdapter zhuanji_adapter;
//	MyLinerLayout ls_dangji_layout;

	View filterButton;

	private Display display;

	private int column_count = 2;// 显示列数
	private int page_count = 20;// 每次加载30张图片
	private int column_count1 = 1;// 显示列数
	private int page_count1 = 10;// 每次加载30张图片

	private int current_page = 0;// 当前页数
	private int current_page1 = 0;// 当前页数

	private int[] topIndex;
	private int[] bottomIndex;
	private int[] lineIndex;
	private int[] column_height;// 每列的高度
	private int[] topIndex1;
	private int[] bottomIndex1;
	private int[] lineIndex1;
	private int[] column_height1;// 每列的高度
	private HashMap<Integer, String> pins;
	private HashMap<Integer, String> pins1;
	private int loaded_count = 0;// 已加载数量
	private int loaded_count1 = 0;// 已加载数量
	private HashMap<Integer, ZhuangbeiView> iviews;
	private HashMap<Integer, ZhuanjiView> iviews1;

	private HashMap<Integer, Integer>[] pin_mark = null;
	private HashMap<Integer, Integer>[] pin_mark1 = null;

	int scroll_height;
	int scroll_height1;

	private final int REFRESH = 100;

	private int item_width;
	private int item_width1;
	
	View allPanel;
	TextView allView;
	View allLine;

	View eventPanel;
	TextView eventView;
	View eventLine;

	private void showPic() {
		display = getWindowManager().getDefaultDisplay();
		item_width = display.getWidth() / column_count;// 根据屏幕大小计算每列大小

		column_height = new int[column_count];
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

	private void showPic1() {
		display = getWindowManager().getDefaultDisplay();
		item_width1 = display.getWidth() / column_count1;// 根据屏幕大小计算每列大小

		column_height1 = new int[column_count1];
		iviews1 = new HashMap<Integer, ZhuanjiView>();
		pins1 = new HashMap<Integer, String>();
		pin_mark1 = new HashMap[column_count1];

		this.lineIndex1 = new int[column_count1];
		this.bottomIndex1 = new int[column_count1];
		this.topIndex1 = new int[column_count1];

		for (int i = 0; i < column_count1; i++) {
			lineIndex1[i] = -1;
			bottomIndex1[i] = -1;
			pin_mark1[i] = new HashMap();
		}

		InitLayout1();

	}

	private void InitLayout() {
		waterfall_scroll = (LazyScrollView) findViewById(R.id.waterfall_scroll);
		waterfall_scroll.getView();
		waterfall_scroll
				.setOnScrollListener(new com.lis99.mobile.entry.view.LazyScrollView.OnScrollListener() {

					@Override
					public void onTop() {
					}

					@Override
					public void onScroll() {
					}

					@Override
					public void onBottom() {
						postMessage(ActivityPattern1.POPUP_PROGRESS,
								getString(R.string.sending));
						offset = offset + limits;
						getZhuangbeiList(offset, limits);
						++current_page;
					}

					@Override
					public void onAutoScroll(int l, int t, int oldl, int oldt) {
						scroll_height = waterfall_scroll.getMeasuredHeight();
						Log.d("MainActivity", "scroll_height:" + scroll_height);
						try {
							if (t > oldt) {// 向下滚动
								if (t > 2 * scroll_height) {// 超过两屏幕后

									for (int k = 0; k < column_count; k++) {

										LinearLayout localLinearLayout = waterfall_items
												.get(k);

										if (pin_mark[k].get(Math.min(
												bottomIndex[k] + 1,
												lineIndex[k])) <= t + 3
												* scroll_height) {// 最底部的图片位置小于当前t+3*屏幕高度
											ZhuangbeiView vv = ((ZhuangbeiView) waterfall_items
													.get(k)
													.getChildAt(
															Math.min(
																	1 + bottomIndex[k],
																	lineIndex[k])));
											if (vv != null)

												vv.Reload();

											bottomIndex[k] = Math.min(
													1 + bottomIndex[k],
													lineIndex[k]);

										}
										Log.d("MainActivity",
												"headIndex:"
														+ topIndex[k]
														+ "  footIndex:"
														+ bottomIndex[k]
														+ "  headHeight:"
														+ pin_mark[k]
																.get(topIndex[k]));
										if (pin_mark[k].get(topIndex[k]) < t
												- 2 * scroll_height) {// 未回收图片的最高位置<t-两倍屏幕高度

											int i1 = topIndex[k];
											topIndex[k]++;
										}
									}

								}
							} else {// 向上滚动
								if (t > 2 * scroll_height) {// 超过两屏幕后
									for (int k = 0; k < column_count; k++) {
										LinearLayout localLinearLayout = waterfall_items
												.get(k);
										if (pin_mark[k].get(bottomIndex[k]) > t
												+ 3 * scroll_height) {
											bottomIndex[k]--;

										}

										if (pin_mark[k].get(Math.max(
												topIndex[k] - 1, 0)) >= t - 2
												* scroll_height) {
											ZhuangbeiView v2 = ((ZhuangbeiView) localLinearLayout
													.getChildAt(Math.max(-1
															+ topIndex[k], 0)));
											if (v2 != null)
												v2.Reload();
											topIndex[k] = Math.max(
													topIndex[k] - 1, 0);

										}
									}
								}

							}
						} catch (Exception e) {

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

				switch (msg.what) {
				case 1:

					ZhuangbeiView v = (ZhuangbeiView) msg.obj;
					LinearLayout ll = (LinearLayout) v.getParent();
					if (ll == null) {
						int w = msg.arg1;
						int h = msg.arg2;
						String f = v.getFlowTag().getFileName();

						// 此处计算列值
						int columnIndex = GetMinValue(column_height);

						v.setColumnIndex(columnIndex);

						column_height[columnIndex] += h;

						pins.put(v.getId(), f);
						iviews.put(v.getId(), v);

						waterfall_items.get(columnIndex).addView(v);

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
		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width, LayoutParams.WRAP_CONTENT);
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			waterfall_items.add(itemLayout);
			waterfall_container.addView(itemLayout);
		}

		AddItemToContainer(current_page, page_count);
	}

	private void InitLayout1() {
		waterfall_scroll1 = (LazyScrollView) findViewById(R.id.waterfall_scroll1);
		waterfall_scroll1.getView();
		waterfall_scroll1
				.setOnScrollListener(new com.lis99.mobile.entry.view.LazyScrollView.OnScrollListener() {

					@Override
					public void onTop() {
					}

					@Override
					public void onScroll() {

					}

					@Override
					public void onBottom() {
						postMessage(ActivityPattern1.POPUP_PROGRESS,
								getString(R.string.sending));
						zhuanji_offset = zhuanji_offset + zhuanji_limits;
						getZhuanjiList(zhuanji_offset, zhuanji_limits);
						++current_page1;
					}

					@Override
					public void onAutoScroll(int l, int t, int oldl, int oldt) {
						scroll_height1 = waterfall_scroll1.getMeasuredHeight();
						Log.d("MainActivity", "scroll_height:" + scroll_height1);
						try {
							if (t > oldt) {// 向下滚动
								if (t > 2 * scroll_height1) {// 超过两屏幕后

									for (int k = 0; k < column_count1; k++) {

										LinearLayout localLinearLayout = waterfall_items1
												.get(k);

										if (pin_mark1[k].get(Math.min(
												bottomIndex1[k] + 1,
												lineIndex1[k])) <= t + 3
												* scroll_height1) {// 最底部的图片位置小于当前t+3*屏幕高度
											ZhuanjiView vv = ((ZhuanjiView) waterfall_items1
													.get(k)
													.getChildAt(
															Math.min(
																	1 + bottomIndex1[k],
																	lineIndex1[k])));
											if (vv != null)

												vv.Reload();

											bottomIndex1[k] = Math.min(
													1 + bottomIndex1[k],
													lineIndex1[k]);

										}
										Log.d("MainActivity",
												"headIndex:"
														+ topIndex1[k]
														+ "  footIndex:"
														+ bottomIndex1[k]
														+ "  headHeight:"
														+ pin_mark1[k]
																.get(topIndex1[k]));
										if (pin_mark1[k].get(topIndex1[k]) < t
												- 2 * scroll_height1) {// 未回收图片的最高位置<t-两倍屏幕高度

											int i1 = topIndex1[k];
											topIndex1[k]++;
										}
									}

								}
							} else {// 向上滚动
								if (t > 2 * scroll_height1) {// 超过两屏幕后
									for (int k = 0; k < column_count1; k++) {
										LinearLayout localLinearLayout = waterfall_items1
												.get(k);
										if (pin_mark1[k].get(bottomIndex1[k]) > t
												+ 3 * scroll_height1) {
											bottomIndex1[k]--;

										}

										if (pin_mark1[k].get(Math.max(
												topIndex1[k] - 1, 0)) >= t - 2
												* scroll_height1) {
											ZhuanjiView v2 = ((ZhuanjiView) localLinearLayout
													.getChildAt(Math.max(-1
															+ topIndex1[k], 0)));
											if (v2 != null)
												v2.Reload();
											topIndex1[k] = Math.max(
													topIndex1[k] - 1, 0);

										}
									}
								}

							}
						} catch (Exception e) {

						}
					}
				});

		waterfall_container1 = (LinearLayout) this
				.findViewById(R.id.waterfall_container1);

		handler2 = new Handler() {

			@Override
			public void dispatchMessage(Message msg) {

				super.dispatchMessage(msg);
			}

			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 1:

					ZhuanjiView v = (ZhuanjiView) msg.obj;
					LinearLayout ll = (LinearLayout) v.getParent();
					if (ll == null) {
						int w = msg.arg1;
						int h = msg.arg2;
						String f = v.getFlowTag().getFileName();

						// 此处计算列值
						int columnIndex = GetMinValue(column_height1);

						v.setColumnIndex(columnIndex);

						column_height1[columnIndex] += h;

						pins1.put(v.getId(), f);
						iviews1.put(v.getId(), v);

						waterfall_items1.get(columnIndex).addView(v);

						lineIndex1[columnIndex]++;

						pin_mark1[columnIndex].put(lineIndex1[columnIndex],
								column_height1[columnIndex]);
						bottomIndex1[columnIndex] = lineIndex1[columnIndex];
					}

					break;
				}

			}

			@Override
			public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
				return super.sendMessageAtTime(msg, uptimeMillis);
			}
		};

		waterfall_items1 = new ArrayList<LinearLayout>();
		for (int i = 0; i < column_count1; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width1, LayoutParams.WRAP_CONTENT);
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			waterfall_items1.add(itemLayout);
			waterfall_container1.addView(itemLayout);
		}

		AddItemToContainer1(current_page1, page_count1);
	}

	public void deleteItems(ZhuangbeiView v) {
		int rowIndex = v.getRowIndex();
		int columnIndex = v.getColumnIndex();

		int height = v.getHeight();
		waterfall_items.get(columnIndex).removeView(v);
		this.pin_mark[columnIndex].remove(rowIndex);
		for (int i = rowIndex; i < pin_mark[columnIndex].size(); i++) {
			this.pin_mark[columnIndex].put(i,
					this.pin_mark[columnIndex].get(i + 1) - height);
			this.pin_mark[columnIndex].remove(i + 1);
			if (((ZhuangbeiView) this.waterfall_items.get(columnIndex)
					.getChildAt(i)) != null)
				((ZhuangbeiView) this.waterfall_items.get(columnIndex)
						.getChildAt(i)).setRowIndex(i);
		}

		lineIndex[columnIndex]--;
		column_height[columnIndex] -= height;
		if (this.bottomIndex[columnIndex] > this.lineIndex[columnIndex]) {
			bottomIndex[columnIndex]--;
		}
	}

	private void AddItemToContainer(int pageindex, int pagecount) {
		int currentIndex = pageindex * pagecount;

		int imagecount = dangji_lists.size();
		for (int i = currentIndex; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			loaded_count++;
			AddImage(dangji_lists.get(i),
					(int) Math.ceil(loaded_count / (double) column_count),
					loaded_count);
		}

	}

	private void AddItemToContainer1(int pageindex, int pagecount) {
		int currentIndex = pageindex * pagecount;

		int imagecount = zhuanji_lists.size();
		for (int i = currentIndex; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			loaded_count1++;
			AddImage1(zhuanji_lists.get(i),
					(int) Math.ceil(loaded_count1 / (double) column_count1),
					loaded_count1);
		}

	}

	private void AddImage(ZhuangbeiBean picture, int rowIndex, int id) {

		ZhuangbeiView item = new ZhuangbeiView(this);
		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(this.handler1);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		ll.width = StringUtil.getXY(this)[0] / 2;
		item.setLayoutParams(ll);

		item.getLs_zhuangbei_item_title().setText(picture.getTitle());
		item.getLs_zhuangbei_item_score().setText(picture.getScore());
		item.getLs_zhuangbei_item_star().removeAllViews();
		int score = (int) (Float.parseFloat(picture.getScore()));
		for (int i = 0; i < score; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(R.drawable.ls_zhuangbei_star_icon);
			LinearLayout.LayoutParams ll1 = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
		item.LoadImage();

	}

	private void AddImage1(ZhuanjiBean picture, int rowIndex, int id) {

		ZhuanjiView item = new ZhuanjiView(this);

		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(this.handler2);

		item.getLs_zhuanji_item_title().setText(picture.getTitle());

		// 多线程参数
		FlowTag param = new FlowTag();
		param.setFlowId(Integer.parseInt(picture.getId()));
		param.setAssetManager(asset_manager);
		param.setFileName(picture.getImage());
		param.setItemWidth(item_width1);

		item.setFlowTag(param);
		item.LoadImage();

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_xuan1);
		initViews();
		
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		getZhuangbeiList(offset, limits);
	}

	@Override
	protected void initViews() {
		super.initViews();

		waterfall_scroll1 = (LazyScrollView) findViewById(R.id.waterfall_scroll1);
		waterfall_scroll = (LazyScrollView) findViewById(R.id.waterfall_scroll);
		
		
		allPanel = findViewById(R.id.allPanel);
		allView = (TextView) findViewById(R.id.allView);
		allLine = findViewById(R.id.allLine);
		
		eventPanel = findViewById(R.id.eventPanel);
		eventView = (TextView) findViewById(R.id.eventView);
		eventLine = findViewById(R.id.eventLine);
		
		allPanel.setOnClickListener(this);
		eventPanel.setOnClickListener(this);
		
		setView();
		setListener();
		
		
	}

	private void getZhuangbeiList(int offset2, int limits2) {
		String url = C.ZHUANGBEI_ITEM_URL + offset2 + "/" + limits2;
		Task task = new Task(null, url, null, "ZHUANGBEI_LIST_URL", this);
		publishTask(task, IEvent.IO);
	}

	private void getZhuanjiList(int offset2, int limits2) {
		String url = C.ZHUANGBEI_ALBUMS_URL + offset2 + "/" + limits2;
		Task task = new Task(null, url, null, "ZHUANGBEI_ALBUMS_URL", this);
		publishTask(task, IEvent.IO);
	}

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		//super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("ZHUANGBEI_LIST_URL")) {
					parserZhuangbeiList(result);
				} else if (((String) task.getParameter())
						.equals("ZHUANGBEI_ALBUMS_URL")) {
					parserZhuanjiList(result);
				} else if (((String) task.getParameter())
						.equals("ZHUANGBEI_DETAIL_URL")) {
					parserZhuangbeiDetailList(result);
				}
			}
			break;
		default:
			break;
		}
	}

	String content;

	private void parserZhuangbeiDetailList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				content = (String) DataManager.getInstance().jsonParse(params,
						DataManager.TYPE_ZHUANGBEI_DETAIL);
				postMessage(SHOW_ZHUANGBEI_CONTENT);
			} else {
				postMessage(ActivityPattern1.POPUP_TOAST, result);
				postMessage(ActivityPattern1.DISMISS_PROGRESS);
			}
		}
	}

	private void parserZhuangbeiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				getlist = (List<ZhuangbeiBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_ZHUANGBEI_LIST);
				postMessage(SHOW_ZHUANGBEI_LIST);
			} else {
				postMessage(ActivityPattern1.POPUP_TOAST, result);
				postMessage(ActivityPattern1.DISMISS_PROGRESS);
			}
		}

	}

	List<ZhuanjiBean> zhuanjilist;

	private void parserZhuanjiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				zhuanjilist = (List<ZhuanjiBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_ZHUANJI_LIST);
				postMessage(SHOW_ZHUANJI_LIST);
			} else {
				postMessage(ActivityPattern1.POPUP_TOAST, result);
				postMessage(ActivityPattern1.DISMISS_PROGRESS);
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
			if (current_page == 0) {
				showPic();
			} else {
				AddItemToContainer(current_page, page_count);
			}
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
			return true;
		case SHOW_ZHUANJI_LIST:
			zhuanji_lists.addAll(zhuanjilist);
			if (current_page1 == 0) {
				showPic1();
			} else {
				AddItemToContainer(current_page1, page_count1);
			}
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
			return true;
		case SHOW_ZHUANGBEI_CONTENT:

			break;
		}
		return false;
	}

	private Random mRand = new Random();

	private void initAdapter() {
	}

	View view;

	private void setView() {
		iv_home = findViewById(R.id.iv_home);
		filterButton = findViewById(R.id.filter);
	}

	private void setListener() {
		iv_home.setOnClickListener(this);
		filterButton.setOnClickListener(this);
	}

	protected void getZhuangbeiDetail(String id) {
		String url = C.ZHUANGBEI_ALBUM_DETAIL_URL + id;
		Task task = new Task(null, url, null, "ZHUANGBEI_DETAIL_URL", this);
		publishTask(task, IEvent.IO);

	}

	boolean remen_flag = false;
	boolean danji_flag = false;

	String type = "1";

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_home.getId()) {
			finish();
//			getLSActivity().toggleMenu();
		} else if (v.getId() == allPanel.getId()) {
			if ("1".equals(type)) {
				return;
			}
			type = "1";
			waterfall_scroll1.setVisibility(View.GONE);
			waterfall_scroll.setVisibility(View.VISIBLE);
			allView.setTextColor(Color.rgb(0x2a, 0xcb, 0xc2));
			allLine.setVisibility(View.VISIBLE);
			eventLine.setVisibility(View.GONE);
			eventView.setTextColor(Color.rgb(0x66, 0x66, 0x66));
		} else if (v.getId() == eventPanel.getId()) {
			if ("2".equals(type)) {
				return;
			}
			type = "2";
			if (!remen_flag) {
				zhuanji_offset = 0;
				zhuanji_limits = 10;
				zhuanji_lists.clear();
				postMessage(ActivityPattern1.POPUP_PROGRESS,
						getString(R.string.sending));
				getZhuanjiList(zhuanji_offset, zhuanji_limits);
				remen_flag = true;
			}
			waterfall_scroll.setVisibility(View.GONE);
			waterfall_scroll1.setVisibility(View.VISIBLE);
			eventView.setTextColor(Color.rgb(0x2a, 0xcb, 0xc2));
			eventLine.setVisibility(View.VISIBLE);
			allLine.setVisibility(View.GONE);
			allView.setTextColor(Color.rgb(0x66, 0x66, 0x66));
		} else if (v.getId() == R.id.filter) {
			Intent intent = new Intent(this,
					LsEquiFilterActivity.class);
			startActivity(intent);
		}
	}

	private static class ViewHolder {
		AsyncLoadImageView ls_zhuangbei_item_pic;
		TextView ls_zhuangbei_item_title, ls_zhuangbei_item_score;
		LinearLayout ls_zhuangbei_item_star;
	}

	private class ZhuangbeiAdapter extends ArrayAdapter {

		LayoutInflater inflater;

		public ZhuangbeiAdapter() {
			super(LSEquiFragment.this, R.layout.ls_xuan_zhuangbei_item, dangji_lists);
			inflater = LayoutInflater.from(LSEquiFragment.this);
		}

		@Override
		public int getCount() {
			return dangji_lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dangji_lists.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final int pos = position;
			ZhuangbeiBean zb = dangji_lists.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_xuan_zhuangbei_item,
						null);
				holder = new ViewHolder();
				holder.ls_zhuangbei_item_pic = (AsyncLoadImageView) convertView
						.findViewById(R.id.ls_zhuangbei_item_pic);
				holder.ls_zhuangbei_item_title = (TextView) convertView
						.findViewById(R.id.ls_zhuangbei_item_title);
				holder.ls_zhuangbei_item_score = (TextView) convertView
						.findViewById(R.id.ls_zhuangbei_item_score);
				holder.ls_zhuangbei_item_star = (LinearLayout) convertView
						.findViewById(R.id.ls_zhuangbei_item_star);
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) holder.ls_zhuangbei_item_pic
						.getLayoutParams();
				ll.width = StringUtil.getXY(LSEquiFragment.this)[0] / 2
						- StringUtil.dip2px(LSEquiFragment.this, 20);
				ll.height = ll.width;
				holder.ls_zhuangbei_item_pic.setLayoutParams(ll);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.ls_zhuangbei_item_pic.setImage(zb.getThumb(), null, null,
					true);
			holder.ls_zhuangbei_item_title.setText(zb.getTitle());
			holder.ls_zhuangbei_item_score.setText(zb.getScore());
			holder.ls_zhuangbei_item_star.removeAllViews();
			int score = (int) (Float.parseFloat(zb.getScore()));
			for (int i = 0; i < score; i++) {
				ImageView iv = new ImageView(LSEquiFragment.this);
				iv.setImageResource(R.drawable.ls_zhuangbei_star_icon);
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				iv.setLayoutParams(ll);
				holder.ls_zhuangbei_item_star.addView(iv);
			}
			return convertView;
		}

	}

	private static class ZhuanjiViewHolder {
		AsyncLoadImageView ls_zhuanji_item_pic;
		TextView ls_zhuanji_item_title;
	}

	private class ZhuanjiAdapter extends BaseAdapter {

		LayoutInflater inflater;

		public ZhuanjiAdapter() {
			inflater = LayoutInflater.from(LSEquiFragment.this);
		}

		@Override
		public int getCount() {
			return zhuanji_lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return zhuanji_lists.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ZhuanjiViewHolder holder;
			final int pos = position;
			ZhuanjiBean zj = zhuanji_lists.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_xuan_zhuanji_item,
						null);
				holder = new ZhuanjiViewHolder();
				holder.ls_zhuanji_item_pic = (AsyncLoadImageView) convertView
						.findViewById(R.id.ls_zhuanji_item_pic);
				holder.ls_zhuanji_item_title = (TextView) convertView
						.findViewById(R.id.ls_zhuanji_item_title);
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) holder.ls_zhuanji_item_pic
						.getLayoutParams();
				ll.width = StringUtil.getXY(LSEquiFragment.this)[0];
				ll.height = ll.width / 2;
				holder.ls_zhuanji_item_pic.setLayoutParams(ll);
				convertView.setTag(holder);
			} else {
				holder = (ZhuanjiViewHolder) convertView.getTag();
			}
			holder.ls_zhuanji_item_pic.setImage(zj.getImage(), null, null);
			holder.ls_zhuanji_item_title.setText(zj.getTitle());
			return convertView;
		}

	}

	@Override
	public void onRefresh() {
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		if ("1".equals(type)) {
			offset = 0;
			limits = 20;
			dangji_lists.clear();
			getZhuangbeiList(offset, limits);
		} else if ("2".equals(type)) {
			zhuanji_offset = 0;
			zhuanji_limits = 10;
			zhuanji_lists.clear();
			getZhuanjiList(zhuanji_offset, zhuanji_limits);
		}

	}

}