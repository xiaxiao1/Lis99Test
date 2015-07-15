package com.lis99.mobile.dilog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.R.color;
import com.lis99.mobile.ShopDetailActivity;
import com.lis99.mobile.engine.base.ShowUtil;
import com.lis99.mobile.entity.bean.SearchBean;
import com.lis99.mobile.entity.item.Shop;
import com.lis99.mobile.entry.adapter.LsBuyAdapter;
import com.lis99.mobile.json.Util;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.constens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author Administrator 需要在Manifest的Activity里面添加
 *         android:windowSoftInputMode="stateAlwaysVisible|adjustResize"
 */
public class ShopSearchDialog extends Dialog {

	private EditText searchText;
	private TextView buttonSearch;
	private ListView listView1;
	private ArrayList<Shop> shops;
	private ArrayList<Shop> arrayList;
	private LsBuyAdapter<Shop> lsBuyAdapter;
	private TextView text;
	private RelativeLayout relativeLayout;
	private Activity activity;
	private String search_text;
	private LinearLayout ll_ll;
	InputMethodManager imm;
	private TextView text1;
	private String Latitude1;
	private String Longtitude1;
	private int offset;
	private String ct;
	private OnSearchClickLinsener clickLinsener;

	public interface OnSearchClickLinsener {
		public void toSearch(String text);
	}

	public ShopSearchDialog(Activity activity, ArrayList<Shop> shops,
			String Latitude1, String Longtitude1, int offset, String ct) {
		super(activity, R.style.server_dialog);
		this.activity = activity;
		this.shops = shops;
		this.Latitude1 = Latitude1;
		this.Longtitude1 = Longtitude1;
		this.offset = offset;
		this.ct = ct;
	}
    public ShopSearchDialog(Activity activity,ArrayList<Shop> shops,OnSearchClickLinsener clickLinsener) {
    	super(activity, R.style.server_dialog);
    	this.clickLinsener=clickLinsener;
    	this.activity = activity;
    	this.shops = shops;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_dialog);
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				imm = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT);

			}
		}, 300);
		if (arrayList == null) {
			arrayList = new ArrayList<Shop>();
		} else {
			arrayList.clear();
		}
		searchText = (EditText) findViewById(R.id.searchText);
		buttonSearch = (TextView) findViewById(R.id.buttonSearch);
		listView1 = (ListView) findViewById(R.id.listView1);
		searchText.addTextChangedListener(watcher);
		relativeLayout = (RelativeLayout) activity.findViewById(R.id.rl_title);
		text = (TextView) findViewById(R.id.text);
		text1 = (TextView) findViewById(R.id.text1);
		ll_ll=(LinearLayout)findViewById(R.id.ll_ll);
		lsBuyAdapter = new LsBuyAdapter<Shop>(activity, null);
		listView1.setAdapter(lsBuyAdapter);
		buttonSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				dismiss();
				relativeLayout.setVisibility(View.VISIBLE);
				int i = 0;
				if (i == 0) {
					imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
					i++;
				}
			}
		});
		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Shop shop = (Shop) lsBuyAdapter.getItem(arg2);
				Intent intent = new Intent(activity, ShopDetailActivity.class);
				String oid = shop.getOid();
				intent.putExtra(constens.OID, oid);
				intent.putExtra("fav", "ls");
				activity.startActivity(intent);

			}
		});
		
		searchText.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg1 == KeyEvent.KEYCODE_ENTER) {
					imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
					if (search_text != null) {
						if (search_text.length() > 0) {
							setABC();

						} else {
							ShowUtil.showToast(activity, "没有关键词");
							dismiss();
						}
					}
				}
				return false;
			}
		});
	}

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			search_text = arg0.toString().trim();
			if(searchText.getText().toString().equals("")){
				listView1.setVisibility(View.GONE);
				text.setVisibility(View.GONE);
				text1.setVisibility(View.VISIBLE);
				text1.setBackgroundColor(color.bantouming);
				text1.setText("");
			}else 
			if (shops != null) {
				
					listView1.setVisibility(View.VISIBLE);
					text1.setVisibility(View.GONE);
					text.setVisibility(View.GONE);
					lsBuyAdapter.clearData();
					arrayList.clear();
					for (Shop shop : shops) {
						boolean boo = ifHasThisText(shop.getTitle(), search_text);
						if (boo) {
							arrayList.add(shop);
						}
					}
					if (arrayList.size() > 0) {
						lsBuyAdapter.setData(arrayList);
					} else {
						listView1.setVisibility(View.GONE);
						text.setVisibility(View.VISIBLE);
					}
				
				
			} else {
				listView1.setVisibility(View.GONE);
				text.setVisibility(View.VISIBLE);
				text1.setVisibility(View.GONE);
				text.setText("");
			}

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			search_text = arg0.toString().trim();
			
			if (shops != null) {
				
				listView1.setVisibility(View.VISIBLE);
				text.setVisibility(View.GONE);
				text1.setVisibility(View.GONE);
				lsBuyAdapter.clearData();
				arrayList.clear();
				for (Shop shop : shops) {
					boolean boo = ifHasThisText(shop.getTitle(), search_text);
					if (boo) {
						arrayList.add(shop);
					}
				}
				if (arrayList.size() > 0) {
					lsBuyAdapter.setData(arrayList);
				} else {
					listView1.setVisibility(View.GONE);
					text.setVisibility(View.VISIBLE);
				}
			} else {
				listView1.setVisibility(View.GONE);
				text.setVisibility(View.VISIBLE);
				text1.setVisibility(View.GONE);
				text.setText("");
			}
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			search_text = arg0.toString().trim();
			
			if (shops != null) {
				
				listView1.setVisibility(View.VISIBLE);
				text.setVisibility(View.GONE);
				text1.setVisibility(View.GONE);
				lsBuyAdapter.clearData();
				arrayList.clear();
				for (Shop shop : shops) {
					boolean boo = ifHasThisText(shop.getTitle(), search_text);
					if (boo) {
						arrayList.add(shop);
					}
				}
				if (arrayList.size() > 0) {
					lsBuyAdapter.setData(arrayList);
				} else {
					listView1.setVisibility(View.GONE);
					text.setVisibility(View.VISIBLE);
				}
			} else {
				listView1.setVisibility(View.GONE);
				text.setVisibility(View.VISIBLE);
				text1.setVisibility(View.GONE);
				text.setText("");
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

	private void setABC() {
		new OptData<SearchBean>(activity).readData(new QueryData<SearchBean>() {

			@Override
			public String callData() {
				HttpNetRequest httpNetRequest = new HttpNetRequest();
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("word", searchText.getText());
				hashMap.put("latitude", Latitude1);
				hashMap.put("longitude", Longtitude1);
				hashMap.put("offset", offset);
				hashMap.put("limit", 20);
				hashMap.put("cityid", ct);
				return httpNetRequest.connect(constens.URLSC, hashMap);
			}

			@Override
			public void showData(SearchBean t) {

				// TODO Auto-generated method stub
				if (t != null) {

					if (t.getData() != null && t.getData().size() > 0) {
						listView1.setVisibility(View.VISIBLE);
						lsBuyAdapter.clearData();
						lsBuyAdapter.setData(t.getData());

					}
				} else {
					if(Util.isNet(activity)){
						text.setVisibility(View.VISIBLE);
						listView1.setVisibility(View.GONE);
						text.setText("无结果");
						text1.setVisibility(View.GONE);
					}else{
					ShowUtil.showToast(activity,
							activity.getString(R.string.lj_false));
					}
				}
			}
		}, SearchBean.class);
	}
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}

}