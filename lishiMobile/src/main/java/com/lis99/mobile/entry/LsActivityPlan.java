package com.lis99.mobile.entry;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.StatusUtil;

public class LsActivityPlan extends ActivityPattern {

	private TextView detail;
	private ImageView back;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xxl_activity_plan);

		StatusUtil.setStatusBar(this);

		setview();
		setlisten();
		initdata();
	}

	private void initdata() {
		bundle = this.getIntent().getExtras();
		String plan = bundle.getString("plan");
		detail.setText("     " +plan);
	}

	private void setlisten() {
		back.setOnClickListener(this);
	}

	private void setview() {
		detail = (TextView) findViewById(R.id.detail);
		back = (ImageView) findViewById(R.id.iv_back);
	}

	@Override
	public void onClick(View arg0) {
		finish();
	}

}
