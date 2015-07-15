package com.lis99.mobile.club;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.LSClubFragmentAdapter;
import com.lis99.mobile.club.model.ClubLevelModel;
import com.lis99.mobile.club.model.LeaderLevelModel;
import com.lis99.mobile.club.widget.FragmentLevel;
import com.lis99.mobile.club.widget.LevelTabs;
import com.lis99.mobile.club.widget.LevelTabs.onClickBtn;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.StatusUtil;

import java.util.ArrayList;
/**
 * 				排行榜
 * @author Administrator
 *
 */
public class LSClubLevelActivity extends ActivityPattern1{

	
	private ViewPager viewPager;
	private LevelTabs lTabs;
	private FragmentLevel fLevelClub, fLevelLeader;
	private ArrayList<Fragment> fList = new ArrayList<Fragment>();
	private LSClubFragmentAdapter adapter;
	public static final int CLUB_LEVEL = 0;
	public static final int LEADER_LEVEL = 1;
	private int state;
	private ImageView titleLeftImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_club_level);

		StatusUtil.setStatusBar(this);
		
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		lTabs = (LevelTabs) findViewById(R.id.lTabs);
		
		viewPager.setOffscreenPageLimit(1);
		
		fLevelClub = new FragmentLevel(0);
		fLevelLeader = new FragmentLevel(1);
		fList.add(fLevelClub);
		fList.add(fLevelLeader);
		
		 adapter = new LSClubFragmentAdapter(getSupportFragmentManager(), fList);
		 
		 viewPager.setAdapter(adapter);
		 
		 viewPager.setOnPageChangeListener( new MyPagerListener());
		
		 lTabs.setonClick( new onClickBtn() {
			
			@Override
			public void selectLeader() {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(1, true);
			}
			
			@Override
			public void selectClub() {
				// TODO Auto-generated method stub
				viewPager.setCurrentItem(0, true);
			}
		});
		 
		 
		 mRequest = MyRequestManager.getInstance();
		 
		 clubModel = new ClubLevelModel();
		 
		 leaderModel = new LeaderLevelModel();
		 
		 state = getIntent().getIntExtra("CLUB_LEVEL", 0); 
		 
		 viewPager.setCurrentItem(state);
		 if ( state == 0 )
		 {
			 requestClub();
		 }
		 
		 titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
		 titleLeftImage.setOnClickListener( new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		 
	}
	
	
	
	
	public class MyPagerListener implements OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if ( arg0 == 0 )
			{
				lTabs.selectClub();
				requestClub();
			}
			else
			{
				lTabs.selectLeader();
				requestLeader();
			}
		}
	}
	
//	private MyTask myTask_Club, myTask_leader;
//	private MyRequest myRequest;
	private ClubLevelModel clubModel;
	private LeaderLevelModel leaderModel;
	private MyRequestManager mRequest;
	
	public CallBack call_Club = new CallBack() {
		
		@Override
		public void handler(MyTask mTask) {
			// TODO Auto-generated method stub
			clubModel = (ClubLevelModel) mTask.getResultModel();
			fLevelClub.init(clubModel);
		}
	};
	
	public CallBack call_leader = new CallBack() {
		
		@Override
		public void handler(MyTask mTask) {
			// TODO Auto-generated method stub
			leaderModel = (LeaderLevelModel) mTask.getResultModel();
			fLevelLeader.init(leaderModel);
		}
	};
	
	public void requestClub ()
	{
		if ( clubModel.hotclublist != null ) return;
		mRequest.requestGet(C.CLUB_LEVEL+"/0", clubModel, call_Club);
	}
	
	public void requestLeader ()
	{
		if ( leaderModel.leaderlist != null ) return;
		mRequest.requestGet(C.CLUB_LEVEL+"/1", leaderModel, call_leader);
	}
	
}
