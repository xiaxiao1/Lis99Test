package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.City;

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends BaseListAdapter<City> {

	private static final int TITLE = 0;
	private static final int CITY = 1;

	private Filter mFilter;

	private boolean mFilted;
	private List<City> originData;

	public CityListAdapter(Context context, List<City> cities) {
		super(context, cities);
		originData = cities;
		mFilter = new CityFilter();
	}

	@Override
	public int getItemViewType(int position) {
		if (getItem(position).getId() < 0) {
			return TITLE;
		} else {
			return CITY;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == TITLE) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.citylist_title_item,
						parent, false);
			}

			TextView text = (TextView) convertView.findViewById(R.id.titleView);
			text.setText(getItem(position).getName());
		} else {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.citylist_item, parent,
						false);
			}
			TextView textView = (TextView) convertView
					.findViewById(R.id.nameView);
			textView.setText(getItem(position).getName());
		}
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	public Filter getFilter() {
		return mFilter;
	}

	private class CityFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String prefix = constraint.toString();
			List<City> list;

			if (TextUtils.isEmpty(prefix)) {
				mFilted = false;
				list = originData;
			} else {
				mFilted = true;
				list = new ArrayList<City>();
				boolean have = false;
				for (City city : originData) {
					if (city.getId() > 0
							&& (city.getName().startsWith(prefix) || city
									.getPinyin().startsWith(
											prefix.toLowerCase()))) {
						have = true;
						list.add(city);
					}
				}
				if ( !have )
				{
					City c = new City();
					c.setName("抱歉，没有找到相关城市");
					list.add(c);
				}
			}
			
			FilterResults results = new FilterResults();
			results.values = list;
			results.count = list.size();
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mData = (List<City>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

}
