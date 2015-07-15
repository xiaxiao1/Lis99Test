package com.lis99.mobile.entry;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ZhuangbeiBean;
import com.lis99.mobile.application.data.ZhuanjiBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.AutoResizeListView;
import com.lis99.mobile.entry.view.FlowTag;
import com.lis99.mobile.entry.view.LazyScrollView;
import com.lis99.mobile.entry.view.MyLinerLayout;
import com.lis99.mobile.entry.view.ZhuangbeiView;
import com.lis99.mobile.entry.view.ZhuanjiView;
import com.lis99.mobile.entry.view.scroll.PullToRefreshBase.OnRefreshListener;
import com.lis99.mobile.entry.view.scroll.PullToRefreshScrollView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LsXuanActivity1 extends ActivityPattern  implements OnRefreshListener{

	ImageView iv_home;
	Button bt_dangjiliuxing,bt_remenzhuanji;
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
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	private static final int SHOW_ZHUANJI_LIST = 201;
	private static final int SHOW_ZHUANGBEI_CONTENT = 202;
	
	
	//private Button ls_more;
	
	private PullToRefreshScrollView refreshContainer;
	private ScrollView refreshScrollView;
	private AutoResizeListView ls_remen_lv;
	
	private com.huewu.pla.lib.MultiColumnListView mAdapterView = null;
	ZhuangbeiAdapter adapter;
	ZhuanjiAdapter zhuanji_adapter;
	MyLinerLayout ls_dangji_layout;
	
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

	private Context context;

	int scroll_height;
	int scroll_height1;

	private final int REFRESH = 100;
	
	private int item_width;
	private int item_width1;
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
	private void showPic1() {
		display = this.getWindowManager().getDefaultDisplay();
		item_width1 = display.getWidth() / column_count1;// 根据屏幕大小计算每列大小

		column_height1 = new int[column_count1];
		context = this;
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
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				offset = offset + limits;
				getZhuangbeiList(offset,limits);
				++current_page;
			}

			@Override
			public void onAutoScroll(int l, int t, int oldl, int oldt) {

				// Log.d("MainActivity",
				// String.format("%d  %d  %d  %d", l, t, oldl, oldt));

				// Log.d("MainActivity", "range:" + range);
				// Log.d("MainActivity", "range-t:" + (range - t));
				scroll_height = waterfall_scroll.getMeasuredHeight();
				Log.d("MainActivity", "scroll_height:" + scroll_height);
				try{
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
				}catch(Exception e){
					
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
	private void InitLayout1() {
		waterfall_scroll1 = (LazyScrollView) findViewById(R.id.waterfall_scroll1);
		waterfall_scroll1.getView();
		waterfall_scroll1.setOnScrollListener(new com.lis99.mobile.entry.view.LazyScrollView.OnScrollListener() {

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
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				zhuanji_offset = zhuanji_offset + zhuanji_limits;
				getZhuanjiList(zhuanji_offset,zhuanji_limits);
				++current_page1;
			}

			@Override
			public void onAutoScroll(int l, int t, int oldl, int oldt) {

				// Log.d("MainActivity",
				// String.format("%d  %d  %d  %d", l, t, oldl, oldt));

				// Log.d("MainActivity", "range:" + range);
				// Log.d("MainActivity", "range-t:" + (range - t));
				scroll_height1 = waterfall_scroll1.getMeasuredHeight();
				Log.d("MainActivity", "scroll_height:" + scroll_height1);
				try{
				if (t > oldt) {// 向下滚动
					if (t > 2 * scroll_height1) {// 超过两屏幕后

						for (int k = 0; k < column_count1; k++) {

							LinearLayout localLinearLayout = waterfall_items1
									.get(k);

							if (pin_mark1[k].get(Math.min(bottomIndex1[k] + 1,
									lineIndex1[k])) <= t + 3 * scroll_height1) {// 最底部的图片位置小于当前t+3*屏幕高度
								ZhuanjiView vv = ((ZhuanjiView) waterfall_items1.get(k).getChildAt(
										Math.min(1 + bottomIndex1[k],
												lineIndex1[k])));
								if(vv !=null)
									
									vv.Reload();

									bottomIndex1[k] = Math.min(1 + bottomIndex1[k],
											lineIndex1[k]);
								

							}
							Log.d("MainActivity",
									"headIndex:" + topIndex1[k] + "  footIndex:"
											+ bottomIndex1[k] + "  headHeight:"
											+ pin_mark1[k].get(topIndex1[k]));
							if (pin_mark1[k].get(topIndex1[k]) < t - 2
									* scroll_height1) {// 未回收图片的最高位置<t-两倍屏幕高度

								int i1 = topIndex1[k];
								topIndex1[k]++;
								/*ZhuangbeiView vv1 = ((ZhuangbeiView) localLinearLayout.getChildAt(i1));
								if(vv1 !=null){
									//deleteItems(vv1);
									//vv1.recycle();
								}*/
								//if(((FlowView) localLinearLayout.getChildAt(i1))!=null)
								//deleteItems(((FlowView) localLinearLayout.getChildAt(i1)));
								Log.d("MainActivity", "recycle,k:" + k
										+ " headindex:" + topIndex1[k]);
								

							}
						}

					}
				} else {// 向上滚动
					if (t > 2 * scroll_height1) {// 超过两屏幕后
						for (int k = 0; k < column_count1; k++) {
							LinearLayout localLinearLayout = waterfall_items1
									.get(k);
							if (pin_mark1[k].get(bottomIndex1[k]) > t + 3
									* scroll_height1) {
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
								

									bottomIndex1[k]--;
								
							}

							if (pin_mark1[k].get(Math.max(topIndex1[k] - 1, 0)) >= t
									- 2 * scroll_height1) {
								ZhuanjiView v2 = ((ZhuanjiView) localLinearLayout.getChildAt(Math
										.max(-1 + topIndex1[k], 0)));
								if(v2 !=null)
									v2.Reload();
									topIndex1[k] = Math.max(topIndex1[k] - 1, 0);
								
								
								
							}
						}
					}

				}
				}catch(Exception e){
					
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

				// super.handleMessage(msg);

				switch (msg.what) {
				case 1:

					//FlowView v = (FlowView) msg.obj;
					ZhuanjiView v = (ZhuanjiView) msg.obj;
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
						int columnIndex = GetMinValue(column_height1);
	
						v.setColumnIndex(columnIndex);
	
						column_height1[columnIndex] += h;
	
						pins1.put(v.getId(), f);
						iviews1.put(v.getId(), v);
						
						waterfall_items1.get(columnIndex).addView(v);
						//if(parent == null)
	
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
		//waterfall_container.removeAllViews();
		for (int i = 0; i < column_count1; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width1, LayoutParams.WRAP_CONTENT);
			//itemLayout.setPadding(StringUtil.dip2px(LsXuanActivity1.this, 8), StringUtil.dip2px(LsXuanActivity1.this, 8), 0, StringUtil.dip2px(LsXuanActivity1.this, 8));
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			waterfall_items1.add(itemLayout);
			waterfall_container1.addView(itemLayout);
		}

		// 加载所有图片路径

		/*try {
			image_filenames = Arrays.asList(asset_manager.list(image_path));

		} catch (IOException e) {
			e.printStackTrace();
		}*/
		// 第一次加载
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
                if(((ZhuangbeiView) this.waterfall_items.get(columnIndex).getChildAt(i)) != null)
                ((ZhuangbeiView) this.waterfall_items.get(columnIndex).getChildAt(i))
                                .setRowIndex(i);
        }

       lineIndex[columnIndex]--;
        column_height[columnIndex] -= height;
        if (this.bottomIndex[columnIndex] > this.lineIndex[columnIndex]) {
                bottomIndex[columnIndex]--;
        }
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
	private void AddItemToContainer1(int pageindex, int pagecount) {
		int currentIndex = pageindex * pagecount;

		int imagecount = zhuanji_lists.size();// image_filenames.size();
		for (int i = currentIndex; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			loaded_count1++;
			Random rand = new Random();
			//int r = rand.nextInt(lists.size());
			
			AddImage1(zhuanji_lists.get(i),
					(int) Math.ceil(loaded_count1 / (double) column_count1),
					loaded_count1);
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
			ImageView iv = new ImageView(LsXuanActivity1.this);
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
private void AddImage1(ZhuanjiBean picture, int rowIndex, int id) {
		
		
	ZhuanjiView item = new ZhuanjiView(this);
		//FlowView item = new FlowView(context);
		// item.setColumnIndex(columnIndex);

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_xuan1);
		StatusUtil.setStatusBar(this);
		inflater = LayoutInflater.from(LsXuanActivity1.this);
		waterfall_scroll1 = (LazyScrollView) findViewById(R.id.waterfall_scroll1);
		waterfall_scroll = (LazyScrollView) findViewById(R.id.waterfall_scroll);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getZhuangbeiList(offset,limits);
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
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("ZHUANGBEI_LIST_URL")) {
					parserZhuangbeiList(result);
				}else if(((String) task.getParameter()).equals("ZHUANGBEI_ALBUMS_URL")){
					parserZhuanjiList(result);
				}else if(((String) task.getParameter()).equals("ZHUANGBEI_DETAIL_URL")){
					parserZhuangbeiDetailList(result);
				}
			}
			break;
		default:
			break;
		}
		
		//refreshContainer.onRefreshComplete();
	}
	String content ;
	private void parserZhuangbeiDetailList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				content = (String) DataManager.getInstance().jsonParse(params, DataManager.TYPE_ZHUANGBEI_DETAIL);
				postMessage(SHOW_ZHUANGBEI_CONTENT);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}		
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
	List<ZhuanjiBean> zhuanjilist;
	private void parserZhuanjiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				zhuanjilist = (List<ZhuanjiBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_ZHUANJI_LIST);
				postMessage(SHOW_ZHUANJI_LIST);
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
			/*refreshContainer.onRefreshComplete();
			//List<ZhuangbeiBean> list = new ArrayList<ZhuangbeiBean>();
			dangji_lists.addAll(getlist);
			//initAdapter();
			if(adapter==null){
				adapter = new ZhuangbeiAdapter();
				mAdapterView.setAdapter(adapter);
			}else{
				adapter.notifyDataSetChanged();
			}*/
			dangji_lists.addAll(getlist);
			if(current_page==0){
				showPic();
			}else{
				AddItemToContainer(current_page, page_count);
				//more.setText("上拉获取更多");
			}
			postMessage(DISMISS_PROGRESS);
			//setAdapterHeightBasedOnChildren(mAdapterView);
			
			//refreshScrollView.addView(view);
			break;
		case SHOW_ZHUANJI_LIST:
			//refreshContainer.onRefreshComplete();
			zhuanji_lists.addAll(zhuanjilist);
			if(current_page1==0){
				showPic1();
			}else{
				AddItemToContainer(current_page1, page_count1);
				//more.setText("上拉获取更多");
			}
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_ZHUANGBEI_CONTENT:
			
			break;
		}
		return true;
	}
	private Random mRand = new Random();
	private void initAdapter() {
		/*mAdapter = new MySimpleAdapter(this, R.layout.sample_item);

		for( int i = 0; i < 30; ++i){
			//generate 30 random items.

			StringBuilder builder = new StringBuilder();
			builder.append("Hello!![");
			builder.append(i);
			builder.append("] ");

			char[] chars = new char[mRand.nextInt(500)];
			Arrays.fill(chars, '1');
			builder.append(chars);
			mAdapter.add(builder.toString());
		}*/
		
	}
	View view;
	private void setView() {
		iv_home = (ImageView) findViewById(R.id.iv_home);
		bt_dangjiliuxing = (Button) findViewById(R.id.bt_dangjiliuxing);
		bt_remenzhuanji = (Button) findViewById(R.id.bt_remenzhuanji);
		filterButton = findViewById(R.id.filter);
		/*refreshContainer = (PullToRefreshScrollView) this.findViewById(R.id.refreshContainer);
		refreshContainer.setMinimumHeight(StringUtil.getXY(this)[1]);
		refreshScrollView = refreshContainer.getRefreshableView();
		refreshContainer.setBackgroundColor(0xffdedede);
		refreshScrollView.setBackgroundColor(0xffdedede);
		view = LayoutInflater.from(this).inflate(R.layout.ls_xuan_dangjiliuxing, null);
		ls_remen_lv = (AutoResizeListView) view.findViewById(R.id.ls_remen_lv);
		ls_dangji_layout = (MyLinerLayout) view.findViewById(R.id.ls_dangji_layout);
		//ls_more = (Button) view.findViewById(R.id.ls_more);
		mAdapterView = (MultiColumnListView) view.findViewById(R.id.list);
		mAdapterView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(PLA_AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		refreshScrollView.addView(view);*/
	}
	
	private void setListener() {
		iv_home.setOnClickListener(this);
		bt_dangjiliuxing.setOnClickListener(this);
		bt_remenzhuanji.setOnClickListener(this);
		filterButton.setOnClickListener(this);
		/*refreshContainer.setOnRefreshListener(this);
		mAdapterView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LsXuanActivity1.this,LsZhuangbeiDetail.class);
				intent.putExtra("id", dangji_lists.get(position).getId());
				startActivity(intent);
			}
			
		});
		ls_remen_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				Intent intent = new Intent(LsXuanActivity1.this,LsZhuanjiDetail.class);
				intent.putExtra("id", zhuanji_lists.get(position).getId());
				startActivity(intent);
			}
			
		});*/
		//ls_more.setOnClickListener(this);
	}
	protected void getZhuangbeiDetail(String id) {
		// TODO Auto-generated method stub
		String url = C.ZHUANGBEI_ALBUM_DETAIL_URL + id;
		Task task = new Task(null, url, null, "ZHUANGBEI_DETAIL_URL", this);
		publishTask(task, IEvent.IO);
		
	}
	boolean remen_flag = false;
	boolean danji_flag = false;
	
	String type = "1";
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_home.getId()){
			finish();
		}/*else if(v.getId() == ls_more.getId()){
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			if("1".equals(type)){
				offset = offset + limits;
				getZhuangbeiList(offset,limits);
			}else if("2".equals(type)){
				zhuanji_offset = zhuanji_offset + zhuanji_limits;
				getZhuanjiList(zhuanji_offset,zhuanji_limits);
			}
		}*/else if(v.getId() == bt_dangjiliuxing.getId()){
			type = "1";
			//ls_remen_lv.setVisibility(View.GONE);
			//mAdapterView.setVisibility(View.VISIBLE);
			/*offset = 0;
			limits = 40;
			dangji_lists.clear();
			if(!danji_flag){
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				getZhuangbeiList(offset,limits);
				danji_flag = true;
			}*/
			waterfall_scroll1.setVisibility(View.GONE);
			waterfall_scroll.setVisibility(View.VISIBLE);
			/*for(int i=0;i<zhuanji_lists.size();i++){
				ImageCacheManager.getInstance().removeBitmapFromCache(zhuanji_lists.get(i).getThumb());
			}*/
			bt_dangjiliuxing.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_left_selected);
			bt_dangjiliuxing.setTextColor(0xffffffff);
			bt_remenzhuanji.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_right_unselected);
			bt_remenzhuanji.setTextColor(0xff2acbc2);
		}else if(v.getId() == bt_remenzhuanji.getId()){
			type = "2";
			if(!remen_flag){
				zhuanji_offset = 0;
				zhuanji_limits = 10;
				zhuanji_lists.clear();
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				getZhuanjiList(zhuanji_offset,zhuanji_limits);
				remen_flag = true;
			}
			waterfall_scroll.setVisibility(View.GONE);
			waterfall_scroll1.setVisibility(View.VISIBLE);
			/*for(int i=0;i<dangji_lists.size();i++){
				ImageCacheManager.getInstance().removeBitmapFromCache(dangji_lists.get(i).getThumb());
			}*/
			//ls_remen_lv.setVisibility(View.VISIBLE);
			//mAdapterView.setVisibility(View.GONE);
			bt_dangjiliuxing.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_left_unselected);
			bt_dangjiliuxing.setTextColor(0xff2acbc2);
			bt_remenzhuanji.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_right_selected);
			bt_remenzhuanji.setTextColor(0xffffffff);
		}else if(v.getId() == R.id.filter){
			Intent intent = new Intent(this,LsEquiFilterActivity.class);
			startActivity(intent);
		}
	}
	
	private static class ViewHolder{
		AsyncLoadImageView ls_zhuangbei_item_pic;
		TextView ls_zhuangbei_item_title,ls_zhuangbei_item_score;
		LinearLayout ls_zhuangbei_item_star;
	}
	
	private class ZhuangbeiAdapter extends ArrayAdapter {
		
		LayoutInflater inflater;

		public ZhuangbeiAdapter() {
			super(LsXuanActivity1.this, R.layout.ls_xuan_zhuangbei_item, dangji_lists);
			inflater = LayoutInflater.from(LsXuanActivity1.this);
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
				convertView = inflater.inflate(R.layout.ls_xuan_zhuangbei_item, null);
				holder = new ViewHolder();
				holder.ls_zhuangbei_item_pic = (AsyncLoadImageView) convertView.findViewById(R.id.ls_zhuangbei_item_pic);
				holder.ls_zhuangbei_item_title = (TextView) convertView.findViewById(R.id.ls_zhuangbei_item_title);
				holder.ls_zhuangbei_item_score = (TextView) convertView.findViewById(R.id.ls_zhuangbei_item_score);
				holder.ls_zhuangbei_item_star = (LinearLayout) convertView.findViewById(R.id.ls_zhuangbei_item_star);
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) holder.ls_zhuangbei_item_pic.getLayoutParams();
				ll.width = StringUtil.getXY(LsXuanActivity1.this)[0] / 2 - StringUtil.dip2px(LsXuanActivity1.this, 20);
				ll.height = ll.width ;
				holder.ls_zhuangbei_item_pic.setLayoutParams(ll);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.ls_zhuangbei_item_pic.setImage(zb.getThumb(), null, null,true);
			holder.ls_zhuangbei_item_title.setText(zb.getTitle());
			holder.ls_zhuangbei_item_score.setText(zb.getScore());
			holder.ls_zhuangbei_item_star.removeAllViews();
			int score = (int)(Float.parseFloat(zb.getScore()));
			for(int i=0;i<score;i++){
				ImageView iv = new ImageView(LsXuanActivity1.this);
				iv.setImageResource(R.drawable.ls_zhuangbei_star_icon);
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				iv.setLayoutParams(ll);
				holder.ls_zhuangbei_item_star.addView(iv);
			}
			return convertView;
		}
		
	}
	
	private static class ZhuanjiViewHolder{
		AsyncLoadImageView ls_zhuanji_item_pic;
		TextView ls_zhuanji_item_title;
	}
	
	private class ZhuanjiAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public ZhuanjiAdapter() {
			inflater = LayoutInflater.from(LsXuanActivity1.this);
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
				convertView = inflater.inflate(R.layout.ls_xuan_zhuanji_item, null);
				holder = new ZhuanjiViewHolder();
				holder.ls_zhuanji_item_pic = (AsyncLoadImageView) convertView.findViewById(R.id.ls_zhuanji_item_pic);
				holder.ls_zhuanji_item_title = (TextView) convertView.findViewById(R.id.ls_zhuanji_item_title);
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) holder.ls_zhuanji_item_pic.getLayoutParams();
				ll.width = StringUtil.getXY(LsXuanActivity1.this)[0];
				ll.height = ll.width / 2 ;
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
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		if("1".equals(type)){
			offset = 0;
			limits = 20;
			dangji_lists.clear();
			getZhuangbeiList(offset,limits);
		}else if("2".equals(type)){
			zhuanji_offset = 0;
			zhuanji_limits = 10;
			zhuanji_lists.clear();
			getZhuanjiList(zhuanji_offset,zhuanji_limits);
		}
		
	}

}
