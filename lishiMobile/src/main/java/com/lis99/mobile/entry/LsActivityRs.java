package com.lis99.mobile.entry;

import android.os.Bundle;

import com.lis99.mobile.R;
import com.lis99.mobile.util.StatusUtil;

public class LsActivityRs extends ActivityPattern {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.xxl_activity_result);

		StatusUtil.setStatusBar(this);
		
	}
}
