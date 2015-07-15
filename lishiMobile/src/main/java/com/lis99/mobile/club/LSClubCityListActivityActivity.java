package com.lis99.mobile.club;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.BasicGridLayoutAdapter;
import com.lis99.mobile.club.adapter.CityListAdapter;
import com.lis99.mobile.club.model.City;
import com.lis99.mobile.club.widget.LSEditTextWithClearButton;
import com.lis99.mobile.club.widget.LSGridLayout;
import com.lis99.mobile.club.widget.QuickAlphabeticBar;
import com.lis99.mobile.club.widget.QuickAlphabeticBar.OnTouchingLetterChangedListener;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.constens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LSClubCityListActivityActivity extends LSBaseActivity implements
		OnTouchingLetterChangedListener, OnFocusChangeListener,
		OnEditorActionListener, TextWatcher {

	List<City> hotCities = new ArrayList<City>();
	List<City> allCities = new ArrayList<City>();
	private List<Integer> sectionPositions = new ArrayList<Integer>();
	int position;
	ListView listView;
	CityListAdapter adapter;
	private List<String> sectionNames = new ArrayList<String>();
	QuickAlphabeticBar alphaBar;
	LinearLayout mHotHeader;
	private LinearLayout mLocationHeader; 
	LSEditTextWithClearButton mSearchEditText;
	boolean mInSearchMode;
	
	int locCityID = 0;
	String locCityName;
	boolean mHiddenHeader;
	City locCity;
	
	 private int locationHeaderHeight;
	    private int hotHeaderHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		locCityID = getIntent().getIntExtra("cityID", 0);
		locCityName = getIntent().getStringExtra("cityName");
		if (locCityID > 0) {
			locCity = new City(locCityID, locCityName, "");
		}
		
		setContentView(R.layout.activity_lsclub_city_list_activity);
		initViews();
		setTitle("城市列表");
		loadCityList();
	}

	@Override
	protected void initViews() {
		super.initViews();
		listView = (ListView) findViewById(R.id.listView);
		alphaBar = (QuickAlphabeticBar) findViewById(R.id.citylist_alpha_bar);

		alphaBar.setOnTouchingLetterChangedListener(this);

		LayoutInflater inflater = LayoutInflater.from(this);

		mHotHeader = new LinearLayout(this);
		mHotHeader.setOrientation(LinearLayout.VERTICAL);
		mHotHeader.setBackgroundColor(Color.WHITE);
		LinearLayout hotContainer = (LinearLayout) inflater.inflate(
				R.layout.citylist_title_item, null);
		TextView hotTitle = (TextView) hotContainer
				.findViewById(R.id.titleView);
		hotTitle.setText("热门城市");
		mHotHeader.addView(hotContainer);
		
		mLocationHeader = new LinearLayout(this);
		mLocationHeader.setOrientation(LinearLayout.VERTICAL);
		LinearLayout locationTitle = (LinearLayout) inflater.inflate(
				R.layout.citylist_title_item, null);
		TextView locTitle = (TextView) locationTitle
				.findViewById(R.id.titleView);
		locTitle.setText("定位城市");
		mLocationHeader.addView(locationTitle);
		LinearLayout locCityView = (LinearLayout) inflater.inflate(
				R.layout.citylist_item, null);
		locCityView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (locCity != null) {
					selectCity(locCity);
				}
			}
		});
		TextView locCityNameView = (TextView) locCityView
				.findViewById(R.id.nameView);
		if (locCity != null){
			locCityNameView.setText(locCity.getName());
		} else {
			locCityNameView.setText("无法获取定位城市");
		}
		
		mLocationHeader.addView(locCityView);
		
//		sectionNames.add("定位");
//		sectionPositions.add(position++);
		position++;

		mSearchEditText = (LSEditTextWithClearButton) findViewById(R.id.citylist_search);
		mSearchEditText.setLSOnFocusListener(this);
		mSearchEditText.clearFocus();
		mSearchEditText.addTextChangedListener(this);
		mSearchEditText.setOnEditorActionListener(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 final int headerViewsCount = listView.getHeaderViewsCount();
		            int realPosition = position - headerViewsCount;
		            if (adapter != null && realPosition < adapter.getCount()) {
		            	City city = adapter.getItem(realPosition);
		                if (city.getId() > 0) {
		                	selectCity(city);
		                }
		            }
			}
		});
		
		setHeaderLayoutListener();
	}
	
	private void addLocationHeader() {
//		if (locCityID > 0) {
			listView.addHeaderView(mLocationHeader, null, false);
//		}
	}

	private void addHotHeader() {
		if (hotCities.size() > 0) {
			mHotHeader.addView(getGridLayout(hotCities));
			View v = new View(this);
			v.setBackgroundColor(Color.rgb(0xe8, 0xe8, 0xe8));
			v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
			mHotHeader.addView(v);
			listView.addHeaderView(mHotHeader, null, false);
		}
	}

	private void loadCityList() {
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.CLUB_GET_AREA;

		Task task = new Task(null, url, null, C.CLUB_GET_AREA, this);
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
				if (((String) task.getParameter()).equals(C.CLUB_GET_AREA)) {
					parserCityInfo(result);
				}
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserCityInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			JsonNode data = root.get("data");
			List<City> temp = LSFragment.mapper.readValue(data.get("hotcity")
					.traverse(), new TypeReference<List<City>>() {
			});
			hotCities.addAll(temp);

			
			if (hotCities.size() > 0) {
				sectionNames.add("热门");
				sectionPositions.add(position++);
			}

			temp = LSFragment.mapper.readValue(data.get("citylist").traverse(),
					new TypeReference<List<City>>() {
					});
			bindAllCities(temp);
			postMessage(SHOW_UI);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}

	private void bindAllCities(List<City> cities) {
		sortCityByLetter(cities);
		char firstChar = ' ';
		int j = 0;
		for (int i = 0, n = cities.size(); i < n; i++) {
			String pinyin = cities.get(i).getPinyin();
			char ch = pinyin == null ? ' ' : pinyin.toUpperCase().charAt(0);
			if (ch != ' ' && ch != firstChar) {
				firstChar = ch;
				String charStr = String.valueOf(firstChar);
				allCities.add(new City(-1, charStr, charStr)); // 增加首个字母
				sectionNames.add(charStr);
				sectionPositions.add(position + i + j);
				j++;
			}
			allCities.add(cities.get(i));
		}
	}

	private void sortCityByLetter(List<City> mCities) {
		Collections.sort(mCities, new Comparator<City>() {

			@Override
			public int compare(City c1, City c2) {
				String p1 = c1.getPinyin().toUpperCase();
				String p2 = c2.getPinyin().toUpperCase();
				return p1.compareTo(p2);
			}
		});
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == SHOW_UI) {
			addLocationHeader();
			addHotHeader();
			adapter = new CityListAdapter(this, allCities);
			listView.setAdapter(adapter);
			alphaBar.setAlphas(sectionNames.toArray(new String[sectionNames
					.size()]));
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onTouchingLetterChanged(int section) {
		hideSoftInput();
		listView.setSelection(sectionPositions.get(section));
	}

	private void hideSoftInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			inputMethodManager.hideSoftInputFromWindow(
					mSearchEditText.getWindowToken(), 0);
		}
	}

	@Override
	public void onActionUp() {

	}

	private LSGridLayout getGridLayout(final List<City> cities) {
		LSGridLayout layout = new LSGridLayout(this);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// final int marginBottom = (int) (BaseConfig.density * 12);
		int leftMargin = (int) (BaseConfig.density * 6);
		int rightMargin = (int) (BaseConfig.density * 30);
		int topMargin = (int) (BaseConfig.density * 6);
		int bottomMargin = topMargin;
		layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
		layout.setLayoutParams(layoutParams);
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.setColumnCount(4);
		layout.setColumnSpace(10);
		layout.setRowSpace(5);
		layout.setBackgroundColor(Color.WHITE);
		layout.setOnItemClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getTag() instanceof Integer) {
					City city = cities.get((Integer) v.getTag());
					selectCity(city);
				}
			}
		});
		layout.setAdapter(new CityGridLayoutAdapter(this, cities));

		layout.setClickable(true);

		return layout;
	}

	protected void selectCity(City city) {
		SharedPreferences.Editor editor = getSharedPreferences(constens.CITYINFO, Context.MODE_PRIVATE).edit();
		editor.putString("clubCityid", city.getId() + "");
		editor.putString("clubCity", city.getName());
		editor.commit();
		Intent intent = new Intent("com.lis99.mobile.loc");
		sendBroadcast(intent);
		finish();
	}

	private static class CityGridLayoutAdapter extends
			BasicGridLayoutAdapter<City> {
		private LayoutInflater inflater;

		public CityGridLayoutAdapter(Context context, List<City> data) {
			super(context);
			resource = data;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position) {
			TextView textView = (TextView) inflater.inflate(
					R.layout.layout_search_hot_item, null);
			textView.setText(this.resource.get(position).getName());
			return textView;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s == null || s.length() == 0) {
			hideHeader(false);
        } else {
        	hideHeader(true);
        }
	}	
	
	@SuppressLint("NewApi")
	private void setHeaderLayoutListener() {
        mLocationHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mLocationHeader.getHeight() > 0) {
                    locationHeaderHeight = mLocationHeader.getHeight();
                    mLocationHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        mHotHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mHotHeader.getHeight() > 0) {
                    hotHeaderHeight = mHotHeader.getHeight();
                    mHotHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }


	 private void hideHeader(boolean hide) {
	        hideHeader(hide, mLocationHeader, locationHeaderHeight);
	        hideHeader(hide, mHotHeader, hotHeaderHeight);
	        mHiddenHeader = hide;
	    }

	    private void hideHeader(boolean hide, View layout, int height) {
	        // 要考虑之前隐藏的情况
	        if (!hide) {
	            layout.setPadding(0, 0, 0, 0);
	        } else if (!mHiddenHeader) {
	            layout.setPadding(0, 0, 0, -height);
	        }
	    }

	@Override
	public void afterTextChanged(Editable s) {
		if (s == null || s.length() == 0) {
            mInSearchMode = false;
            alphaBar.setVisibility(View.VISIBLE);
        } else {
            mInSearchMode = true;
            alphaBar.setVisibility(View.GONE);
        }

            adapter.getFilter().filter(s);
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onInvalidated() {
                    super.onInvalidated();
//                    viewNoCity.setVisibility(View.VISIBLE);
                }

                @Override
                public void onChanged() {
                    super.onChanged();
//                    viewNoCity.setVisibility(View.GONE);
                }
            });
	}

}
