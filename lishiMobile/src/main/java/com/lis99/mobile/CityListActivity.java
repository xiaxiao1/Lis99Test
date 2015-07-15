package com.lis99.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fq.app.util.Pinyin_Comparator;
import com.lis99.mobile.dilog.SideBar;
import com.lis99.mobile.engine.ShowUtil;
import com.lis99.mobile.engine.base.BaseActivity;
import com.lis99.mobile.entity.bean.CityListBean;
import com.lis99.mobile.entity.item.CityList;
import com.lis99.mobile.entity.item.CityListItem;
import com.lis99.mobile.entity.item.Hotcitys;
import com.lis99.mobile.entry.adapter.CityListAdapter;
import com.lis99.mobile.entry.adapter.CityListAdapter2;
import com.lis99.mobile.entry.adapter.ListViewAdp;
import com.lis99.mobile.newhome.NewHomeActivity;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.constens;

import java.util.ArrayList;
import java.util.Arrays;

public class CityListActivity extends BaseActivity {
	private TextView iv_home;
	private ListView listView1;
	private ListView listView2;
	private TextView gpsname;
	private RelativeLayout rl_title;
	private CityListAdapter<Hotcitys> cityListAdapter;
	private CityListAdapter2<CityList> cityListAdapter2;
	private ListViewAdp listViewAdp;
	private ArrayList<CityList> arrayList;
	private ArrayList<Hotcitys> hotcitys;
	private ArrayList<CityList> cityLists;
	private LinearLayout ll_gps;
	private ListView lv_search;
	private ScrollView sv_city;
	private TextView buttonSearch;
	private EditText searchText;
	private String search_text;
	private String[] cityName;
	private SideBar indexBar;
	private OnSearchClickLinsener clickLinsener;
	InputMethodManager imm;

	public interface OnSearchClickLinsener {
		public void toSearch(String text);
	}

	public void toSearch(String text) {

	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_city_list;
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		if (arrayList == null) {
			arrayList = new ArrayList<CityList>();
		} else {
			arrayList.clear();
		}
		indexBar = (SideBar) findViewById(R.id.sideBar);

		buttonSearch = (TextView) findViewById(R.id.buttonSearch);
		// sv_city =(ScrollView)findViewById(R.id.sv_city);
		lv_search = (ListView) findViewById(R.id.lv_search);
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		searchText = (EditText) findViewById(R.id.searchText);
		searchText.addTextChangedListener(watcher);
		gpsname = (TextView) findViewById(R.id.gpsname);
		ll_gps = (LinearLayout) findViewById(R.id.ll_gps);
		iv_home = (TextView) findViewById(R.id.iv_home);
		// listView1=(ListView)findViewById(R.id.listView1);
		listView2 = (ListView) findViewById(R.id.listView2);
		cityListAdapter = new CityListAdapter<Hotcitys>(CityListActivity.this,
				null);
		cityListAdapter2 = new CityListAdapter2<CityList>(
				CityListActivity.this, null);
		// listView1.setAdapter(cityListAdapter);
		lv_search.setAdapter(cityListAdapter2);
		imm = (InputMethodManager) CityListActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		initData();
		viewdp();
	}

	@Override
	public void initData() {
		final String cityname = this.getIntent().getStringExtra("city");
		gpsname.setText(cityname);
		searchText.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				indexBar.setVisibility(View.INVISIBLE);
				return false;
			}
		});
		buttonSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchText.setText("");
				lv_search.setVisibility(View.GONE);
				listView2.setVisibility(View.VISIBLE);
				indexBar.setVisibility(View.VISIBLE);
				imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
			}
		});
		ll_gps.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences.Editor editor = getSharedPreferences(constens.CITYINFO, Context.MODE_PRIVATE).edit();
				editor.putString("cityid", "");
				editor.putString("city", cityname);
				editor.commit();
				Intent intent = new Intent("com.lis99.mobile.loc");
				sendBroadcast(intent);
				finish();
			}
		});
		// listView1.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		// String id=hotcitys.get(arg2).getId();
		// String name=hotcitys.get(arg2).getName();
		// Intent intent = new
		// Intent(CityListActivity.this,LsBuyActivity.class);
		// intent.putExtra("id", id);
		// intent.putExtra("name", name);
		// startActivity(intent);
		// finish();
		// }
		// });
		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String id = "";
				String name = cityName[arg2];
				if (arg2 >= 12) {
					for (int i = 0; i < cityLists.size(); i++) {
						if (cityLists.get(i).getName().equals(name)) {
							id = cityLists.get(i).getId();

						}
					}

				} else {
					for (int i = 0; i < hotcitys.size(); i++) {
						if (hotcitys.get(i).getName().equals(name)) {
							id = hotcitys.get(i).getId();
						}
					}

				}
				SharedPreferences.Editor editor = getSharedPreferences(constens.CITYINFO, Context.MODE_PRIVATE).edit();
				editor.putString("cityid", id);
				editor.putString("city", name);
				editor.commit();
				Intent intent = new Intent("com.lis99.mobile.loc");
				sendBroadcast(intent);
				finish();
			}
		});

		iv_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CityListActivity.this,
						NewHomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		searchText.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_ENTER) {
					imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
					indexBar.setVisibility(View.VISIBLE);
					if (search_text != null) {
						if (search_text.length() > 0) {

						} else {
							ShowUtil.showToast(CityListActivity.this, "没有关键词");

						}
					}
				}
				return false;
			}
		});
		lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CityList cityList = (CityList) cityListAdapter2.getItem(arg2);
				String id = cityList.getId();
				String name = cityList.getName();
				SharedPreferences.Editor editor = getSharedPreferences(constens.CITYINFO, Context.MODE_PRIVATE).edit();
				editor.putString("cityid", id);
				editor.putString("city", name);
				editor.commit();
				Intent intent = new Intent("com.lis99.mobile.loc");
				sendBroadcast(intent);
				finish();
			}
		});
	}

	@Override
	protected View getView() {
		return null;
	}

	public void viewdp() {
		showWaiting(CityListActivity.this);
		new OptData<CityListBean>(CityListActivity.this).readData(
				new QueryData<CityListBean>() {

					@Override
					public String callData() {
						String data = "";
						HttpNetRequest httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(constens.URLctiy,
								data);
						return str;

					}

					@Override
					public void showData(CityListBean t) {
						hideDialog();
						if (t != null) {
							CityListItem cityListItem = t.getData();
							if (cityListItem != null) {
								hotcitys = cityListItem.getHotcitys();
								cityLists = cityListItem.getCitys();
								cityListAdapter.setData(cityListItem
										.getHotcitys());
								cityListAdapter2.setData(cityListItem
										.getCitys());
								String[] string = new String[cityLists.size()];
								setCityname();
								for (int i = 0; i < cityLists.size(); i++) {

									string[i] = cityLists.get(i).getName();

								}
								Arrays.sort(string, new Pinyin_Comparator());
								for (int i = 0; i < string.length; i++) {
									cityName[i + 12] = string[i];
								}

								listViewAdp = new ListViewAdp(
										CityListActivity.this, cityName);
								listView2.setAdapter(listViewAdp);
								indexBar.setListView(listView2);
							}
						} else {
							ShowUtil.showToast(CityListActivity.this,
									getString(R.string.lj_false));
						}
					}
				}, CityListBean.class);
	}

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			search_text = arg0.toString().trim();
			if (cityLists != null) {
				lv_search.setVisibility(View.VISIBLE);
				listView2.setVisibility(View.GONE);
				cityListAdapter2.clearData();
				arrayList.clear();
				for (CityList cityList : cityLists) {
					boolean boo = ifHasThisText(cityList.getName(), search_text);
					if (boo) {
						arrayList.add(cityList);
					}
				}
				if (arrayList.size() > 0) {
					cityListAdapter2.setData(arrayList);
				} else {
					lv_search.setVisibility(View.GONE);
					listView2.setVisibility(View.VISIBLE);
				}
			} else {
				lv_search.setVisibility(View.GONE);
				listView2.setVisibility(View.VISIBLE);
			}

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			search_text = arg0.toString().trim();
			if (cityLists != null) {
				lv_search.setVisibility(View.VISIBLE);
				listView2.setVisibility(View.GONE);
				cityListAdapter2.clearData();
				arrayList.clear();
				for (CityList cityList : cityLists) {
					boolean boo = ifHasThisText(cityList.getName(), search_text);
					if (boo) {
						arrayList.add(cityList);
					}
				}
				if (arrayList.size() > 0) {
					cityListAdapter2.setData(arrayList);
				} else {
					lv_search.setVisibility(View.GONE);
					listView2.setVisibility(View.VISIBLE);
				}
			} else {
				lv_search.setVisibility(View.GONE);
				listView2.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			search_text = arg0.toString().trim();
			if (cityLists != null) {
				lv_search.setVisibility(View.VISIBLE);
				listView2.setVisibility(View.GONE);
				cityListAdapter2.clearData();
				arrayList.clear();
				for (CityList cityList : cityLists) {
					boolean boo = ifHasThisText(cityList.getName(), search_text);
					if (boo) {
						arrayList.add(cityList);
					}
				}
				if (arrayList.size() > 0) {
					cityListAdapter2.setData(arrayList);
				} else {
					lv_search.setVisibility(View.GONE);
					listView2.setVisibility(View.VISIBLE);
				}
			} else {
				lv_search.setVisibility(View.GONE);
				listView2.setVisibility(View.VISIBLE);
			}
		}

	};

	/**
	 * 
	 * @param str1从外面取的字符串
	 * @param str2用户输入的字符串
	 * @return
	 */
	public boolean ifHasThisText(final String str1, final String str2) {
		boolean boos = false;
		if (str1 != null && str2 != null) {
			try {
				boos = str1.indexOf(str2 + "") != -1;
				return boos;

			} catch (Exception e) {
				e.fillInStackTrace();

			} finally {
				return boos;
			}
		} else {
			return false;
		}
	}

	public void setCityname() {
		cityName = new String[12 + cityLists.size()];
		cityName[0] = "北京";
		cityName[1] = "上海";
		cityName[2] = "广州市";
		cityName[3] = "深圳市";
		cityName[4] = "南京市";
		cityName[5] = "郑州市";
		cityName[6] = "长春市";
		cityName[7] = "沈阳市";
		cityName[8] = "哈尔滨市";
		cityName[9] = "西安市";
		cityName[10] = "成都市";
		cityName[11] = "乌鲁木齐";
	}

}
