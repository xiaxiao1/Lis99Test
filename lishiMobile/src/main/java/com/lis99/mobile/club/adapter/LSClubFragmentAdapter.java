package com.lis99.mobile.club.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class LSClubFragmentAdapter extends FragmentPagerAdapter{

	private ArrayList<Fragment> list;
	
	public LSClubFragmentAdapter(FragmentManager fm, ArrayList<Fragment> mList ) {
		super(fm);
		// TODO Auto-generated constructor stub
		list = mList;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
