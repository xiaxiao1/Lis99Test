package com.lis99.mobile.entry.cptslide;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lis99.mobile.application.data.ShaiTuBean;

import java.util.ArrayList;

/**
 * 
 * @author gluoyer@gmail.com
 *
 */
public class ChapterAdapter extends FragmentPagerAdapter{
	public final static int CHAPTER_PAGE_NUM = 9;
	private ArrayList<Fragment> mFragments;
	
	public ChapterAdapter(FragmentManager fm, ArrayList<ArrayList<ShaiTuBean>> arrayLists) {
		super(fm);
		mFragments = new ArrayList<Fragment>();
		int startPos = 0; // count the click offset
		for(ArrayList<ShaiTuBean>list : arrayLists) {
			mFragments.add(ChapterFragment.getNewInstance(startPos * CHAPTER_PAGE_NUM, list));
			startPos ++;
		}
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

}
