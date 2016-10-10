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
package com.lis99.mobile.kf.easemob.helpdesk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.kf.easemob.KFCommon;


public class HelpDeskPreferenceUtils {

	/**
	 * 保存Preference的name
	 */
	public static final String PREFERENCE_NAME = "appkeyInfo";

	private static SharedPreferences mSharedPreferences;
	private static HelpDeskPreferenceUtils mPreferenceUtils;
	private static SharedPreferences.Editor editor;

	private String SHARED_KEY_SETTING_CUSTOMER_APPKEY = "shared_key_setting_customer_appkey";
	private String SHARED_KEY_SETTING_CUSTOMER_ACCOUNT = "shared_key_setting_customer_account";
	
	private String SHARED_KEY_SETTING_CURRENT_NICK = "shared_key_setting_current_nick";
	private String SHARED_KEY_SETTING_TENANT_ID = "shared_key_setting_tenant_id";
	private String SHARED_KEY_SETTING_PROJECT_ID = "shared_key_setting_project_id";
//	保存的帖子ID
	private String TOPIC_ID = "topic_id";


	private HelpDeskPreferenceUtils(Context cxt) {
		mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}

	public static HelpDeskPreferenceUtils getInstance(Context cxt) {
		if (mPreferenceUtils == null) {
			mPreferenceUtils = new HelpDeskPreferenceUtils(cxt);
		}
		return mPreferenceUtils;
	}

	public void setSettingCustomerAppkey(String appkey) {
		editor.putString(SHARED_KEY_SETTING_CUSTOMER_APPKEY, appkey);
		editor.commit();
	}

	public String getSettingCustomerAppkey() {
//		return mSharedPreferences.getString(SHARED_KEY_SETTING_CUSTOMER_APPKEY, Constant.DEFAULT_COSTOMER_APPKEY);
		return KFCommon.APPKEY;
	}

	public void setSettingCustomerAccount(String account) {
		editor.putString(SHARED_KEY_SETTING_CUSTOMER_ACCOUNT, account);
		editor.commit();
	}

	public String getSettingCustomerAccount() {
//		return mSharedPreferences.getString(SHARED_KEY_SETTING_CUSTOMER_ACCOUNT, Constant.DEFAULT_COSTOMER_ACCOUNT);
		return KFCommon.imCode;
	}

	public void setSettingCurrentNick(String nick){
		editor.putString(SHARED_KEY_SETTING_CURRENT_NICK, nick);
		editor.commit();
	}
	
	public String getSettingCurrentNick(){
//		return mSharedPreferences.getString(SHARED_KEY_SETTING_CURRENT_NICK, "");
		String name = DataManager.getInstance().getUser().getNickname();
		if (TextUtils.isEmpty(name))
		{
			name = "消息"+ (int)(Math.random() * 10);
		}
		return name;
	}
	
	public void setSettingProjectId(long projectId){
		editor.putLong(SHARED_KEY_SETTING_PROJECT_ID, projectId);
		editor.commit();
	}

	public void setSettingTenantId(long tenantId){
		editor.putLong(SHARED_KEY_SETTING_TENANT_ID, tenantId);
		editor.commit();
	}

	public long getSettingTenantId(){
//		return mSharedPreferences.getLong(SHARED_KEY_SETTING_TENANT_ID, Constant.DEFAULT_TENANT_ID);
		return KFCommon.TenantId;
	}

	public long getSettingProjectId(){
//		return mSharedPreferences.getLong(SHARED_KEY_SETTING_PROJECT_ID, Constant.DEFAULT_PROJECT_ID);
		return KFCommon.ProjectId;
	}

}
