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
import com.lis99.mobile.club.model.LikeModelNew;
import com.lis99.mobile.club.model.MyFriendsRecommendModel;
import com.lis99.mobile.club.model.MyJoinClubModel;
import com.lis99.mobile.club.model.NearbyModel;
import com.lis99.mobile.club.model.QQLoginModel;
import com.lis99.mobile.club.model.RedDotModel;
import com.lis99.mobile.club.model.UpdataInfoModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.application.DemoApplication;
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
		DialogManager.getInstance().startAlert(LSBaseActivity.activity, "提示", "确定要删除吗？", true, "确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
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
		MyRequestManager.getInstance().requestPost(url, map, model, call);
	}
	
	/**上传用户信息*/
	public void upDataInfo ()
	{
		String Token = SharedPreferencesHelper.getJPushToken();//getPushToken();
		if ( TextUtils.isEmpty(Token)) return;
		String userid = DataManager.getInstance().getUser().getUser_id();

		if ( TextUtils.isEmpty(userid))
		{
			return;
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		final UpdataInfoModel model = new UpdataInfoModel();
		map.put("os_version", DeviceInfo.SDKVERSIONCODE);
		map.put("os_name", "Android");
		map.put("channel_number", DeviceInfo.CHANNELVERSION);
		map.put("client_version", DeviceInfo.CLIENTVERSIONCODE);
		map.put("device_token", Token);
		map.put("imei", DeviceInfo.IMEI);
		map.put("userid", TextUtils.isEmpty(userid) ? "0" : userid);
		map.put("mac", DeviceInfo.getMacAddress());
		map.put("equipment_brand", DeviceInfo.MANUFACTURER);
		map.put("equipment_num", DeviceInfo.MODEL);

		Common.log("Post upDataInfo =========");

//		Common.toast("Login userId=" + userid + "\n token=" + Token);
//		Common.log("Login userId=" + userid + "\n token=" + Token);
//		Toast.makeText(DemoApplication.getInstance().getApplicationContext(), "Login userId=" + userid + "\n token=" + Token, Toast.LENGTH_SHORT).show();
		String url = C.UPDATA_DEVICE_INFO;
		MyRequestManager.getInstance().requestPostNoDialog(url, map, model, new CallBack() {
			@Override
			public void handler(MyTask mTask) {
				UpdataInfoModel info = (UpdataInfoModel) mTask.getResultModel();
				DemoApplication.LOGIN_ID = info.login_id;
				Common.log("Login_id======="+info.login_id);
//				Common.toast("Login_id========="+info.login_id);

			}
		});
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
						u1.setIs_vip(model.is_vip);
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
		MyRequestManager.getInstance().requestPost(C.LS_REGIST, map, model, new CallBack() {

			@Override
			public void handler(MyTask mTask) {
				// TODO Auto-generated method stub
				LSRegistModel model = (LSRegistModel) mTask.getResultModel();
				if (model == null) return;
				SharedPreferencesHelper.saveUserName(email);
				SharedPreferencesHelper.saveUserPass(password);
				SharedPreferencesHelper.saveLSToken(model.token);
				SharedPreferencesHelper.saveIsVip(model.is_vip);

				UserBean u1 = new UserBean();
				u1.setUser_id(model.user_id);
				u1.setEmail(email);
				u1.setNickname(model.nickname);
				u1.setHeadicon(model.headicon);
				u1.setIs_vip(model.is_vip);

				u1.setSex("");
				u1.setPoint("");
				DataManager.getInstance().setUser(u1);
				DataManager.getInstance().setLogin_flag(true);
				//上传设备信息
				LSRequestManager.getInstance().upDataInfo();
//注册
				LSScoreManager.getInstance().sendScore(LSScoreManager.register, "0");

				if (call != null) {
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
		String token = SharedPreferencesHelper.getJPushToken();//getPushToken();
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
		
		MyRequestManager.getInstance().requestGetNoDialog(url, model, new CallBack() {

			@Override
			public void handler(MyTask mTask) {
				// TODO Auto-generated method stub
				RedDotModel model = (RedDotModel) mTask.getResultModel();
//				相加为0没有通知
				int num = model.is_baoming + model.is_reply + model.manage_baoming + model
						.is_follow + model.notice + model.likeStatus;
				Common.log("b================" + num);
				Common.log("model.is_reply" + model.is_reply);

				// Tab红点
				if (tab.mTabCur != LSTab.EVENT && num > 0) {
					Common.log("Visible tab");
					tab.visibleRedDot(true);
				} else {
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

	/**
	 *
	 * @param openid
	 * @param nickname
	 * @param gender
	 * @param figureurl
	 * @param callBack
	 * @param showDialog		是否显示Dialog
	 */
	public void QQLogin ( String openid, String nickname, String gender, String figureurl, final CallBack callBack, boolean showDialog )
	{
		CallBack call = new CallBack() {
			@Override
			public void handler(MyTask mTask) {
				QQLoginModel model = (QQLoginModel) mTask.getResultModel();
				UserBean u = new UserBean();
				u.setHeadicon(model.headicon);
				u.setNickname(model.nickname);
				u.setUser_id(model.user_id);
//				u.setIs_vip(model.is_vip);

				DataManager.getInstance().setUser(u);
				DataManager.getInstance().setLogin_flag(true);

				SharedPreferencesHelper.saveheadicon(model.headicon);
				SharedPreferencesHelper.savenickname(model.nickname);
				SharedPreferencesHelper.saveuser_id(model.user_id);

				SharedPreferencesHelper.saveaccounttype(SharedPreferencesHelper.QQLOGIN);
				//QQ注册
				if ( "1".equals(model.is_new))
				{
					LSScoreManager.getInstance().sendScore(LSScoreManager.register, "0");
				}

				if ( callBack != null )
				{
					callBack.handler(null);
				}
			}
		};


		QQLoginModel model = new QQLoginModel();
		String Url = C.QQLOGINURL;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("openid", openid);
		map.put("nickname", nickname);
		map.put("gender", gender);
		map.put("figureurl", figureurl);
		if ( !showDialog )
		{
			MyRequestManager.getInstance().requestPost(Url, map, model, call);
		}
		else
		{
			MyRequestManager.getInstance().requestPostNoDialog(Url, map, model, call);
		}
	}

	public void SinaLogin (String uid, String screen_name, String gender, String avatar_large, final CallBack callBack, boolean showDialog)
	{
		CallBack call = new CallBack() {
			@Override
			public void handler(MyTask mTask)
			{
				QQLoginModel model = (QQLoginModel) mTask.getResultModel();
				UserBean u = new UserBean();
				u.setHeadicon(model.headicon);
				u.setNickname(model.nickname);
				u.setUser_id(model.user_id);
//				u.setIs_vip(model.is_vip);

				DataManager.getInstance().setUser(u);
				DataManager.getInstance().setLogin_flag(true);

				SharedPreferencesHelper.saveheadicon(model.headicon);
				SharedPreferencesHelper.savenickname(model.nickname);
				SharedPreferencesHelper.saveuser_id(model.user_id);

				SharedPreferencesHelper.saveaccounttype(SharedPreferencesHelper.SINALOGIN);
//新浪注册
				if ( "1".equals(model.is_new))
				{
					LSScoreManager.getInstance().sendScore(LSScoreManager.register, "0");
				}

				if ( callBack != null )
				{
					callBack.handler(null);
				}
			}
		};

		QQLoginModel model = new QQLoginModel();
		String url = C.SINALOGINURL;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("screen_name", screen_name);
		map.put("gender", gender);
		map.put("avatar_large", avatar_large);

		if ( showDialog )
		{
			MyRequestManager.getInstance().requestPost(url, map, model, call);
		}
		else
		{
			MyRequestManager.getInstance().requestPostNoDialog(url, map, model, call);
		}

	}
	/**加入俱乐部*/
	public void addClub ( String clubID, CallBack call )
	{
		if ( !Common.isLogin(LSBaseActivity.activity) )
		{
			return;
		}
		String userID = DataManager.getInstance().getUser().getUser_id();

		BaseModel model = new BaseModel();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("club_id", clubID);
		map.put("user_id", userID);

		MyRequestManager.getInstance().requestPost(C.CLUB_JOIN, map, model, call);

	}
	/**关注， 推荐关注， 动态， 我-》好友-》推荐关注*/
	public void getFriendsAttentionRecommend ( int page, CallBack call )
	{
		String userID = DataManager.getInstance().getUser().getUser_id();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uid", userID);

		MyFriendsRecommendModel model = new MyFriendsRecommendModel();

		String url = C.MYFRIENDS_RECOMMED + page;

		MyRequestManager.getInstance().requestPost(url, map, model, call);

	}
	/**取消关注*/
	public void getFriendsCancelAttention ( int AttentionId, CallBack call )
	{
		String userID = DataManager.getInstance().getUser().getUser_id();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("fromuid", userID);
		map.put("touid", AttentionId);

		BaseModel model = new BaseModel();

		MyRequestManager.getInstance().requestPost(C.CANCEL_ATTENTION, map, model, call);

	}
	/**添加关注
	 * 			@param AttentionId 被关注的ID
	 * */
	public void getFriendsAddAttention ( int AttentionId, CallBack call )
	{
		if ( !Common.isLogin(LSBaseActivity.activity))
		{
			return;
		}

		String userID = DataManager.getInstance().getUser().getUser_id();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("fromuid", userID);
		map.put("touid", AttentionId);

		BaseModel model = new BaseModel();

		MyRequestManager.getInstance().requestPost(C.ADD_ATTENTION, map, model, call);

	}
	/**点赞*/
	public void clubTopicLike ( int topicid, CallBack callBack )
	{
		if ( !Common.isLogin(LSBaseActivity.activity))
		{
			return;
		}
		String userID = DataManager.getInstance().getUser().getUser_id();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userID);

		String url = C.CLUB_TOPIC_LIKE + topicid;

		LikeModelNew model = new LikeModelNew();

		MyRequestManager.getInstance().requestPostNoDialog(url, map, model, callBack);

	}

	/**点赞*/
	public void clubTopicLikeNew ( int topicid, CallBack callBack )
	{
		if ( !Common.isLogin(LSBaseActivity.activity))
		{
			return;
		}
		String userID = DataManager.getInstance().getUser().getUser_id();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userID);

		String url = C.CLUB_TOPIC_LIKE_NEW + topicid;

		LikeModelNew model = new LikeModelNew();

		MyRequestManager.getInstance().requestPostNoDialog(url, map, model, callBack);

	}

	/**
	 * 		喜欢的装备
	 * 	@param  id 装备id
	 */
	public void equipLike ( int id, CallBack callBack )
	{
		String userId = DataManager.getInstance().getUser().getUser_id();

		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("id", id);
		map.put("module", "zhuangbei");
		map.put("user_id", userId);

		String url = C.MAIN_ADDLIKE_URL;

		BaseModel model = new BaseModel();

		MyRequestManager.getInstance().requestPost(url, map, model, callBack);



	}

	/**
	 * 		用户加入的俱乐部
	 * @param user_id
	 * @param pageNo
	 * @param callBack
	 */
	public void getMyJoinClub ( int user_id, int pageNo, CallBack callBack )
	{
		String url = C.MY_JOIN_CLUB_LIST + pageNo;

		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("user_id", user_id);

		MyJoinClubModel model = new MyJoinClubModel();

		MyRequestManager.getInstance().requestPost(url, map, model, callBack);
	}
// 	报名管理， 通过
	public void managerApplyPass (int topicId, int clubId, int Id, CallBack callBack)
	{
		String url = C.MANAGER_APPLY_PASS;

		String userId = DataManager.getInstance().getUser().getUser_id();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("topicid", topicId);
		map.put("club_id", clubId);
		map.put("user_id", userId);
		map.put("id", Id);

		BaseModel model = new BaseModel();

		MyRequestManager.getInstance().requestPost(url, map, model, callBack);
	}
//	报名管理，拒绝
	public void managerApplyRefuse (int topicId, int clubId, int Id, CallBack callBack)
	{
		String url = C.MANAGER_APPLY_REFUSE;

		String userId = DataManager.getInstance().getUser().getUser_id();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("topicid", topicId);
		map.put("club_id", clubId);
		map.put("user_id", userId);
		map.put("id", Id);

		BaseModel model = new BaseModel();

		MyRequestManager.getInstance().requestPost(url, map, model, callBack);
	}

	
}
