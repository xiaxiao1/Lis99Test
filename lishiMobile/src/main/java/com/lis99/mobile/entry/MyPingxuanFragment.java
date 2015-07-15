package com.lis99.mobile.entry;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lis99.mobile.myactivty.ActivityItem;

import java.util.List;


public class MyPingxuanFragment extends Fragment {
	
	private static List<ActivityItem> infos;

	public static MyPingxuanFragment newInstance(List<ActivityItem> infos){
		MyPingxuanFragment newFragment = new MyPingxuanFragment();
		MyPingxuanFragment.infos=infos;
		
		return newFragment;
	}
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	    }
//	
//	   private RefreshListView orderList;
//	    private MyOrderAdapter orderAdapter;
//	
	
	
	
	
	
	
	
	
	
}
