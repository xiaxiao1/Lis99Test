/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lis99.mobile.kf.easemob.helpdesk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;

import com.easemob.easeui.ui.EaseBaseActivity;
import com.lis99.mobile.club.LSBaseActivity;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends EaseBaseActivity {
	
	@Override
	protected void onCreate(Bundle arg0) {
		LSBaseActivity.activity = this;
		super.onCreate(arg0);

	}

	@Override
	protected void onResume() {
		super.onResume();
		//umeng
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//umeng
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LSBaseActivity.activity = this;
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 通过xml查找相应的ID，通用方法
	 * @param id
	 * @param <T>
	 * @return
	 */
	protected <T extends View> T $(@IdRes int id) {
		return (T) findViewById(id);
	}
}
