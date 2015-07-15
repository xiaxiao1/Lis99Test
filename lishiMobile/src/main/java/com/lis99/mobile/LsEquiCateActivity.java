package com.lis99.mobile;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ZhuangbeiBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.FlowTag;
import com.lis99.mobile.entry.view.LazyScrollView;
import com.lis99.mobile.entry.view.ZhuangbeiView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LsEquiCateActivity extends ActivityPattern1 {

	private LinearLayout waterfall_container;
	LazyScrollView waterfall_scroll;
	int offset = 0;
	int limits = 20;
	int sport_id = -1;
	int cate_id = -1;
	List<ZhuangbeiBean> getlist;
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	List<ZhuangbeiBean> dangji_lists = new ArrayList<ZhuangbeiBean>();
	private int page_count = 20;// 每次加载20张图片

	private int column_count = 2;// 显示列数
	private int current_page = 0;// 当前页数
	private Display display;

	private int[] topIndex;
	private int[] bottomIndex;
	private int[] lineIndex;
	private int[] column_height;// 每列的高度
	private HashMap<Integer, String> pins;
	private int loaded_count = 0;// 已加载数量
	private HashMap<Integer, ZhuangbeiView> iviews;
	private ArrayList<LinearLayout> waterfall_items;

	int scroll_height;

	private int item_width;

	private HashMap<Integer, Integer>[] pin_mark = null;

	private Context context;
	private Handler handler1;
	private AssetManager asset_manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ls_equi_cate);

		StatusUtil.setStatusBar(this);

		sport_id = getIntent().getIntExtra("sport", -1);
		cate_id = getIntent().getIntExtra("cate", -1);
		
		String title = getIntent().getStringExtra("title");
		if(title == null || "".equals(title)){
			title = "装备";
		}
		
		TextView tv = (TextView) findViewById(R.id.tv_title);
		tv.setText(title);
		

		waterfall_scroll = (LazyScrollView) findViewById(R.id.waterfall_scroll);

		View backView = findViewById(R.id.iv_back);
		backView.setOnClickListener(this);

		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		getZhuangbeiList(offset, limits);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_back) {
			finish();
			return;
		}
		super.onClick(v);
	}

	private void getZhuangbeiList(int offset, int limits) {
		String url = C.ZHUANGBEI_ITEM_URL + offset + "/" + limits;
		if (sport_id != -1) {
			url += "?sport_id=" + sport_id;
		} else if (cate_id != -1) {
			url += "?cate_id=" + cate_id;
		}
		Task task = new Task(null, url, null, "ZHUANGBEI_LIST_URL", this);
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
				if (((String) task.getParameter()).equals("ZHUANGBEI_LIST_URL")) {
					parserZhuangbeiList(result);
				}
			}
			break;
		default:
			break;
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
			if (current_page == 0) {
				showPic();
			} else {
				addItemToContainer(current_page, page_count);
			}
			postMessage(DISMISS_PROGRESS);
			break;

		}
		return true;
	}

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

		initLayout();

	}

	private void addItemToContainer(int pageindex, int pagecount) {
		int currentIndex = pageindex * pagecount;

		int imagecount = dangji_lists.size();// image_filenames.size();
		for (int i = currentIndex; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			loaded_count++;
			Random rand = new Random();
			// int r = rand.nextInt(lists.size());

			addImage(dangji_lists.get(i),
					(int) Math.ceil(loaded_count / (double) column_count),
					loaded_count);
		}
	}

	private void addImage(ZhuangbeiBean picture, int rowIndex, int id) {

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
			ImageView iv = new ImageView(LsEquiCateActivity.this);
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
		// item.setImage(filename, null, null);
		item.LoadImage();
		// waterfall_items.get(columnIndex).addView(item);

	}

	private void initLayout() {
		waterfall_scroll.getView();
		waterfall_scroll
				.setOnScrollListener(new com.lis99.mobile.entry.view.LazyScrollView.OnScrollListener() {

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
						postMessage(POPUP_PROGRESS, getString(R.string.sending));
						offset = offset + limits;
						getZhuangbeiList(offset, limits);
						++current_page;
					}

					@Override
					public void onAutoScroll(int l, int t, int oldl, int oldt) {
						scroll_height = waterfall_scroll.getMeasuredHeight();
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
											Log.d("MainActivity", "recycle,k:"
													+ k + " headindex:"
													+ topIndex[k]);

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
						// deleteItems(v);
						int w = msg.arg1;
						int h = msg.arg2;
						String f = v.getFlowTag().getFileName();

						// 此处计算列值
						int columnIndex = getMinValue(column_height);

						v.setColumnIndex(columnIndex);

						column_height[columnIndex] += h;

						pins.put(v.getId(), f);
						iviews.put(v.getId(), v);

						waterfall_items.get(columnIndex).addView(v);
						// if(parent == null)

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
		// waterfall_container.removeAllViews();
		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width, LayoutParams.WRAP_CONTENT);
			// itemLayout.setPadding(StringUtil.dip2px(LsXuanActivity1.this, 8),
			// StringUtil.dip2px(LsXuanActivity1.this, 8), 0,
			// StringUtil.dip2px(LsXuanActivity1.this, 8));
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			waterfall_items.add(itemLayout);
			waterfall_container.addView(itemLayout);
		}

		addItemToContainer(current_page, page_count);
	}

	private int getMinValue(int[] array) {
		int m = 0;
		int length = array.length;
		for (int i = 0; i < length; ++i) {

			if (array[i] < array[m]) {
				m = i;
			}
		}
		return m;
	}
}
