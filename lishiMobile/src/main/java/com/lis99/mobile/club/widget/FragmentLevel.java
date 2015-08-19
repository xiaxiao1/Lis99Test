package com.lis99.mobile.club.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.adapter.LSClubLevelAdapter;
import com.lis99.mobile.club.adapter.LSClubLevelLeaderAdapter;
import com.lis99.mobile.club.model.ClubLevelModel;
import com.lis99.mobile.club.model.ClubLevelModel.Hotclublist;
import com.lis99.mobile.club.model.LeaderLevelModel;
import com.lis99.mobile.club.model.LeaderLevelModel.Leaderlist;
import com.lis99.mobile.mine.LSUserHomeActivity;

public class FragmentLevel extends Fragment{

	private View v;
	private ListView list;
	private LSClubLevelAdapter levelAdapter;
	private LSClubLevelLeaderAdapter leaderAdapter;
	
	public final int LEVEL = 0;
	public final int LEADER = 1;
	private int state;

	public FragmentLevel(){}

	@SuppressLint("ValidFragment")
	public FragmentLevel (int num )
	{
		state = num;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.club_level_list, null);
		list = (ListView) v.findViewById(R.id.list);
		list.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int clubId = 0;
				Intent intent = null;
				if ( state == LEVEL )
				{
					if ( levelAdapter == null ) return;
					Hotclublist item = (Hotclublist) levelAdapter.getItem(arg2);
					clubId = Integer.parseInt(item.clubId);
					intent = new Intent(getActivity(), LSClubDetailActivity.class);
					intent.putExtra("clubID", clubId);
					startActivity(intent);
					
				}
				else
				{
					if ( leaderAdapter == null ) return;
					Leaderlist item = (Leaderlist) leaderAdapter.getItem(arg2);
					clubId = Integer.parseInt(item.club_id);




					intent = new Intent(getActivity(), LSUserHomeActivity.class);
					intent.putExtra("userID", item.user_id);
					startActivity(intent);
					
				}
			}
		});
		
//		init(state);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	/**
	 * 		现实哪个排行榜
	 * @param num
	 */
	public void init ( Object o )
	{
		if ( state == LEVEL )
		{
			levelAdapter = new LSClubLevelAdapter(getActivity(), (ClubLevelModel) o);
			list.setAdapter(levelAdapter);
		}
		else
		{
			leaderAdapter = new LSClubLevelLeaderAdapter(getActivity(), (LeaderLevelModel) o);
			list.setAdapter(leaderAdapter);
		}
	}
	
}
