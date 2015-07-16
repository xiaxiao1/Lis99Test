package com.lis99.mobile.util;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.BaseModel;
import com.lis99.mobile.club.model.ClubTopicInfoLike;
import com.lis99.mobile.club.model.EquipAppraiseModel;
import com.lis99.mobile.club.model.EquipRecommendModel;
import com.lis99.mobile.club.model.EquipTypeModel;
import com.lis99.mobile.club.model.NearbyModel;
import com.lis99.mobile.club.model.RedDotModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.mobel.LSRegistModel;
import com.lis99.mobile.newhome.LSTab;

import java.util.HashMap;

/**
 * 			网络请求公共方法
 * 			作用： 多处请求相同借口
 * @author yy
 *
 */
public class LSRequestManager
{
	private static LSRequestManager instance;
	
	public static LSRequestManager getInstance ()
	{
		if ( instance == null ) instance = new LSRequestManager();
		return instance;
	}
	/***
	 * 			删除回复
	 * @param clubId
	 * @param topicId
	 */
	public void mClubTopicReplyDelete ( final String clubId,final String topicId, final CallBack call )
	{
		DialogManager.getInstance().startAlert(LSBaseActivity.activity, "提示", "确定要删除吗？", true, "确定", new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				BaseModel model = new BaseModel();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("topicid", topicId);
				map.put("club_id", clubId);
				map.put("user_id", DataManager.getInstance().getUser().getUser_id());
				map.put("token", SharedPreferencesHelper.getLSToken());
				MyRequestManager.getInstance().requestPost(C.CLUB_TOPIC_REPLY_DELETE, map, model, call);
			}
		}, true, "取消", null);
		
	}
	/**
	 * 赞
	 * @param topicId		楼层ID
	 * @param call
	 */
	public void mClubTopicInfoLike ( String topicId, CallBack call )
	{
		ClubTopicInfoLike model = new ClubTopicInfoLike();
		if (!Common.isLogin(LSBaseActivity.activity))
		{
			return;
		}
		String userid = DataManager.getInstance().getUser().getUser_id();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userid);
		String url = C.CLUB_TOPIC_INFO_LIKE + topicId;
		MyRequestManager.getInstance().requestPost(url, map, model, call );
	}
	
	/**上传用户信息*/
	public void upDataInfo ()
	{
		String Token = SharedPreferencesHelper.getPushToken();
		if ( TextUtils.isEmpty(Token)) return;
		String userid = DataManager.getInstance().getUser().getUser_id();
		if ( TextUtils.isEmpty(userid))
		{
			return;
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		BaseModel model = new BaseModel();
		map.put("os_version", DeviceInfo.SDKVERSIONCODE);
		map.put("os_name", "Android");
		map.put("channel_number", DeviceInfo.CHANNELVERSION);
		map.put("client_version", DeviceInfo.CLIENTVERSIONCODE);
		map.put("device_token", Token);
		map.put("imei", DeviceInfo.IMEI);
		map.put("userid", TextUtils.isEmpty(userid) ? "0" : userid);
//		Common.toast("Login userId=" + userid + "\n token=" + Token);
//		Common.log("Login userId=" + userid + "\n token=" + Token);
//		Toast.makeText(DemoApplication.getInstance().getApplicationContext(), "Login userId=" + userid + "\n token=" + Token, Toast.LENGTH_SHORT).show();
		String url = C.UPDATA_DEVICE_INFO;
		MyRequestManager.getInstance().requestPostNoDialog(url, map, model, null);
	}
	/**
	 * 			登陆
	 * @param email
	 * @param pass
	 */
	public void Login ( final String email, final String pass, final CallBack call )
	{
		LSRegistModel model = new LSRegistModel();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("pwd", pass);
		MyRequestManager.getInstance().requestPost(C.LS_SIGNIN, map, model,
				new CallBack()
				{

					@Override
					public void handler(MyTask mTask)
					{
						// TODO Auto-generated method stub
						LSRegistModel model = (LSRegistModel) mTask.getResultModel();
						if (model == null)
							return;
						SharedPreferencesHelper.saveLSToken(model.token);
						SharedPreferencesHelper.saveUserName(email);
						SharedPreferencesHelper.saveUserPass(pass);
						SharedPreferencesHelper.saveIsVip(model.is_vip);
						UserBean u1 = new UserBean();
						u1.setUser_id(model.user_id);
						u1.setEmail(email);
						u1.setNickname(model.nickname);
						u1.setHeadicon(model.headicon);
						u1.setSex("");
						u1.setPoint("");
						DataManager.getInstance().setUser(u1);
						DataManager.getInstance().setLogin_flag(true);
						//上传设备信息
						LSRequestManager.getInstance().upDataInfo();
						if ( call != null )
						{
							call.handler(mTask);
						}
					}
				});
		
	}
	
	/**
	 * 			注册
	 * @param email
	 * @param password
	 * @param nickname
	 * @param call
	 */
	public void getRegist ( final String email, final String password, String nickname, final CallBack call )
	{
		LSRegistModel model = new LSRegistModel();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("pwd", password);
		map.put("nickname", nickname);
		MyRequestManager.getInstance().requestPost(C.LS_REGIST, map, model, new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				LSRegistModel model = (LSRegistModel) mTask.getResultModel();
				if ( model == null ) return;
				SharedPreferencesHelper.saveUserName(email);
				SharedPreferencesHelper.saveUserPass(password);
				SharedPreferencesHelper.saveLSToken(model.token);
				SharedPreferencesHelper.saveIsVip(model.is_vip);
				
				UserBean u1 = new UserBean();
				u1.setUser_id(model.user_id);
				u1.setEmail(email);
				u1.setNickname(model.nickname);
				u1.setHeadicon(model.headicon);
				
				u1.setSex("");
				u1.setPoint("");
				DataManager.getInstance().setUser(u1);
				DataManager.getInstance().setLogin_flag(true);
				//上传设备信息
				LSRequestManager.getInstance().upDataInfo();
				if ( call != null )
				{
					call.handler(mTask);
				}
			}
		});
	}
	/**
	 * 		退出登陆调用
	 */
	public void Logout ()
	{
		// TODO Auto-generated method stub
		BaseModel model = new BaseModel();
		String token = SharedPreferencesHelper.getPushToken();
		String UserId = DataManager.getInstance().getUser().getUser_id();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userid", UserId);
		map.put("device_token", token);
//		Common.toast("Logout userId=" + UserId + "\n token=" + token);
		MyRequestManager.getInstance().requestPostNoDialog(C.LOGOUT, map, model, null);
	}
	
	public void getRedDot ( final LSTab tab )
	{
		String url = C.USER_NOTICE_TIPS_URL
		+ DataManager.getInstance().getUser().getUser_id();
		
		RedDotModel model = new RedDotModel();
		
		MyRequestManager.getInstance().requestGetNoDialog(url, model, new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				RedDotModel model = (RedDotModel) mTask.getResultModel();
				int num = model.is_baoming + model.is_reply + model.manage_baoming;
				Common.log("b================"+num);
				Common.log("model.is_reply"+model.is_reply);
				
				// Tab红点
				if ( tab.mTabCur != LSTab.EVENT && num > 0 )
				{
					Common.log("Visible tab");
					tab.visibleRedDot(true);
				}
				else
				{
					Common.log("gone tab");
					tab.visibleRedDot(false);
				}
				
			}
		});
		
	}
	/**户外店*/
	public void getEquipType (CallBack callBack)
	{
		EquipTypeModel model = new EquipTypeModel(  );
		String url = C.EQUIPTYPE;
		MyRequestManager.getInstance().requestGet(url, model, callBack);
	}
	/**
	 * 		热门装备
	 * @param callBack
	 */
	public void getEquipRecommend ( CallBack callBack )
	{
		EquipRecommendModel model = new EquipRecommendModel();
		String url = C.EQUIPRECOMMEND;
		MyRequestManager.getInstance().requestGet(url, model, callBack);
	}
	/**
	 * 			附近店铺
	 * 			http://api.lis99.com/v2/zhuangbei/nearShop?%20Latitude=%20111&%20longitude=1111
	 * @param latitude
	 * @param longitude
	 * @param callBack
	 */
	public void getNearby ( double latitude, double longitude, CallBack callBack )
	{
		NearbyModel model = new NearbyModel();
		String url = C.EQUIPNEARBY + "?latitude=" + latitude + "&longitude="+longitude;
		MyRequestManager.getInstance().requestGet(url, model, callBack);
	}
	/**
	 * 		装备评测
	 * @param callBack
	 */
	public void getEquipAppraise ( CallBack callBack )
	{
		EquipAppraiseModel model = new EquipAppraiseModel();
		String url = C.EQUIPAPPRAISE;
		MyRequestManager.getInstance().requestGet(url, model, callBack);
	}
	
	
	
}
