package com.lis99.mobile.club.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lis99.mobile.R;
/**
 * 				排行榜TABS
 * @author Administrator
 *
 */
public class LevelTabs extends LinearLayout{

	private LayoutInflater li;
	private Button btn_level_club, btn_level_leader;
	private View v;
	private onClickBtn clickBtn;
	private boolean isLeader;
	
	public LevelTabs(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		li = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		v = li.inflate(R.layout.club_level_tabs, null);
		addView(v);
		init ();
	}
	
	private void init ()
	{
		btn_level_club = (Button) v.findViewById(R.id.btn_level_club);
		btn_level_leader = (Button) v.findViewById(R.id.btn_level_leader);
		
		btn_level_club.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( !isLeader ) return;
				selectClub();
				if ( clickBtn != null )
					clickBtn.selectClub();
			}
		});
		btn_level_leader.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( isLeader ) return;
				selectLeader();
				if ( clickBtn != null )
					clickBtn.selectLeader();
			}
		});
	}
	
	public void selectClub ()
	{
		isLeader = false;
		btn_level_club.setBackgroundResource(R.drawable.club_level_tab1_left);
		btn_level_leader.setBackgroundResource(R.drawable.club_level_tab2_right);
		btn_level_club.setTextColor(getResources().getColor(R.color.text_color_blue));
		btn_level_leader.setTextColor(getResources().getColor(R.color.white));
	}
	
	public void selectLeader ()
	{
		isLeader = true;
		btn_level_club.setBackgroundResource(R.drawable.club_level_tab2_left);
		btn_level_leader.setBackgroundResource(R.drawable.club_level_tab1_right);
		btn_level_leader.setTextColor(getResources().getColor(R.color.text_color_blue));
		btn_level_club.setTextColor(getResources().getColor(R.color.white));
	}
	
	public void setonClick ( onClickBtn onclick )
	{
		this.clickBtn = onclick;
	}
	
	public interface onClickBtn
	{
		public void selectClub ();
		public void selectLeader ();
	}

}
