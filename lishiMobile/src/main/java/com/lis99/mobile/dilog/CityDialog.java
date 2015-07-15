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
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.LsBuyActivity;
import com.lis99.mobile.R;
import com.lis99.mobile.engine.base.ShowUtil;
import com.lis99.mobile.entity.item.CityList;
import com.lis99.mobile.entry.adapter.CityListAdapter2;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author Administrator 需要在Manifest的Activity里面添加
 *         android:windowSoftInputMode="stateAlwaysVisible|adjustResize"
 */
public class CityDialog extends Dialog {

	private EditText searchText;
	private TextView buttonSearch;
	private ListView listView1;
	private ArrayList<CityList> shops;
	private ArrayList<CityList> arrayList;
	private CityListAdapter2<CityList> lsBuyAdapter;
	private TextView text;
	private Activity activity;
	private String search_text;
	private OnSearchClickLinsener clickLinsener;
	InputMethodManager imm;
	public interface OnSearchClickLinsener {
		public void toSearch(String text);
	}

	public CityDialog(Activity activity, ArrayList<CityList> shops,OnSearchClickLinsener clickLinsener) {
		super(activity, R.style.server_dialog);
		this.activity = activity;
		this.clickLinsener=clickLinsener;
		this.shops = shops;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_dialog);
		Timer timer = new Timer();  
		  
		timer.schedule(new TimerTask() {   
		public void run() {   
			
			imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT);  
			
		}   
		}, 1000);  
		if (arrayList == null) {
			arrayList = new ArrayList<CityList>();
		} else {
			arrayList.clear();
		}
		searchText = (EditText) findViewById(R.id.searchText);
		buttonSearch = (TextView) findViewById(R.id.buttonSearch);
		listView1 = (ListView) findViewById(R.id.listView1);
		searchText.addTextChangedListener(watcher);
		text = (TextView) findViewById(R.id.text);
		lsBuyAdapter = new CityListAdapter2<CityList>(activity, null);
		listView1.setAdapter(lsBuyAdapter);
		buttonSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				if (search_text.length() > 0) {
//					clickLinsener.toSearch(search_text);
//					dismiss();
//				} else {
//					dismiss();
//				}
				dismiss();
				imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
			}
		});
		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CityList cityList = (CityList) lsBuyAdapter.getItem(arg2);
				Intent intent = new Intent(activity, LsBuyActivity.class);
				String id=cityList.getId();
				String name=cityList.getName();
				intent.putExtra("id", id);
				intent.putExtra("name", name);
				activity.startActivity(intent);

			}
		});
		searchText.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if(arg1==KeyEvent.KEYCODE_ENTER){
					imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
					if (search_text.length() > 0) {
						clickLinsener.toSearch(search_text);
						
					} else {
						ShowUtil.showToast(activity, "没有关键词");
						dismiss();
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
			if (shops != null) {
				listView1.setVisibility(View.VISIBLE);
				text.setVisibility(View.GONE);
				lsBuyAdapter.clearData();
				arrayList.clear();
				for (CityList cityList : shops) {
					boolean boo = ifHasThisText(cityList.getName(), search_text);
					if (boo) {
						arrayList.add(cityList);
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
			}

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			search_text = arg0.toString().trim();
			if (shops != null) {
				listView1.setVisibility(View.VISIBLE);
				text.setVisibility(View.GONE);
				lsBuyAdapter.clearData();
				arrayList.clear();
				for (CityList cityList : shops) {
					boolean boo = ifHasThisText(cityList.getName(), search_text);
					if (boo) {
						arrayList.add(cityList);
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
			}
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			search_text = arg0.toString().trim();
			if (shops != null) {
				listView1.setVisibility(View.VISIBLE);
				text.setVisibility(View.GONE);
				lsBuyAdapter.clearData();
				arrayList.clear();
				for (CityList cityList : shops) {
					boolean boo = ifHasThisText(cityList.getName(), search_text);
					if (boo) {
						arrayList.add(cityList);
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
			}
		}

	};

	/**
	 * 
	 * @param str1从外面取的字符串
	 * @param str2用户输入的字符串
	 * @return
	 */
	public boolean ifHasThisText(final String str1,final String str2) {
		boolean boos=false;
		if (str1 != null && str2 != null) {
			try {
				boos=	str1.indexOf(str2+"") != -1;
					return boos;
				
			} catch (Exception e) {
				e.fillInStackTrace();
				
			}finally{
				return boos;
			}
		} else {
			return false;
		}
	}
}
