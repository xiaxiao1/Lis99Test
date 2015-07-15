package com.lis99.mobile.entry.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lis99.mobile.R;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ListViewAdp extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private String[] cityName;
	static int i;
	static String[] first;

	public ListViewAdp(Context mContext, String[] parentData) {
		this.mContext = mContext;
		this.cityName = parentData;
	}

	@Override
	public int getCount() {
		return cityName.length;
	}

	@Override
	public Object getItem(int position) {
		return cityName[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String nickName = cityName[position];
		i = position;

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.contact_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvCatalog = (TextView) convertView
					.findViewById(R.id.contactitem_catalog);
			viewHolder.tvNick = (TextView) convertView
					.findViewById(R.id.contactitem_nick);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String catalog = converterToFirstSpell(nickName).substring(0, 1);
		viewHolder.tvCatalog.setText(catalog);
		if (position <=11&&position==0) {
			viewHolder.tvCatalog.setVisibility(View.VISIBLE);
			viewHolder.tvCatalog.setText("热门城市");
		}else if(position <=11&&position!=0){
			viewHolder.tvCatalog.setVisibility(View.GONE);
		}else if(position==12){
			viewHolder.tvCatalog.setVisibility(View.VISIBLE);
			viewHolder.tvCatalog.setText(catalog);
		} else {
			String lastCatalog = converterToFirstSpell(cityName[position - 1])
					.substring(0, 1);
			if (catalog.equals(lastCatalog)) {
				viewHolder.tvCatalog.setVisibility(View.GONE);
			} else {
				viewHolder.tvCatalog.setVisibility(View.VISIBLE);
				viewHolder.tvCatalog.setText(catalog);
			}
		}

		// viewHolder.ivAvatar.setImageResource(R.drawable.default_avatar);
		viewHolder.tvNick.setText(cityName[position]);
		return convertView;
	}

	static class ViewHolder {
		TextView tvCatalog;
		// ImageView ivAvatar;
		TextView tvNick;
	}

	@Override
	public int getPositionForSection(int section) {
		if(section==48){
			return 0;
		}
		for (int i = 12; i < cityName.length; i++) {
			String l = converterToFirstSpell(cityName[i]).substring(0, 1);
			char firstChar = l.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public static String converterToFirstSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}
}
