package com.lis99.mobile.application.data;

import com.lis99.mobile.myactivty.AddLikeBean;
import com.lis99.mobile.myactivty.AlbumidBean;
import com.lis99.mobile.myactivty.BaseInfoBean;
import com.lis99.mobile.myactivty.Comment;
import com.lis99.mobile.myactivty.CommentLajis;
import com.lis99.mobile.myactivty.Comments;
import com.lis99.mobile.myactivty.Equipcate;
import com.lis99.mobile.myactivty.ItemInfoBean;
import com.lis99.mobile.myactivty.LineInfoBean;
import com.lis99.mobile.myactivty.LingduiInfoBean;
import com.lis99.mobile.myactivty.LingduiLineBean;
import com.lis99.mobile.myactivty.PhaseItem;
import com.lis99.mobile.myactivty.ShaituBean;
import com.lis99.mobile.myactivty.ShowPic;
import com.lis99.mobile.myactivty.UserInfo;
import com.lis99.mobile.util.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * 
 * @ClassName: DataManager
 * @Description: 数据管理类
 * @author xingyong cosmos250.xy@gmail.com
 * @date 2013-12-30 上午10:41:30
 * 
 *       1,保存所有业务逻辑数据,并对数据进行有效的利用 2,保证能对任意数据的 添加，设置，删除及释放 3,根据数据的状态分配并创建数据结构
 * 
 ****************************************** 
 */
public class DataManager {

	private static final String TAG = "DataManager";

	public static final int TYPE_MAIN_BANNER = 100;
	public static final int TYPE_ZHUANGBEI_LIST = 101;
	public static final int TYPE_ZHUANJI_LIST = 102;
	public static final int TYPE_ZHUANGBEI_DETAIL = 103;
	public static final int TYPE_ZHUANJI_DETAIL = 104;
	public static final int TYPE_SIGNUP = 105;
	public static final int TYPE_SHAITU_LIST = 106;
	public static final int TYPE_MAIN_COMMENTLIST = 107;
	public static final int TYPE_MAIN_COMMENT = 108;
	public static final int TYPE_MAIN_ADDLIKE = 109;
	public static final int TYPE_WENDA_LATEST = 110;
	public static final int TYPE_DAYI_LIST = 111;
	public static final int TYPE_DAREN_LISTS = 112;
	public static final int TYPE_UPLOAD_PIC = 113;
	public static final int TYPE_WENDA_CATES_LIST = 114;
	public static final int TYPE_WENDA_QUESTION = 115;
	public static final int TYPE_WENDA_ANSWER = 116;
	public static final int TYPE_SHAITU_CREATE = 117;
	public static final int TYPE_SHAITU_DETAIL = 118;
	public static final int TYPE_USER_SHAITU_LIST = 119;
	public static final int TYPE_USER_ANSWER = 120;
	public static final int TYPE_USER_NOTICE = 121;
	public static final int TYPE_USER_INFO = 122;
	public static final int TYPE_MAIN_CHECKVERSION = 123;
	public static final int TYPE_USER_DAREN = 124;
	public static final int TYPE_WENDA_UPLOAD = 125;
	public static final int TYPE_DAREN_DETAIL = 126;
	public static final int TYPE_DAREN_SHAITU_LIST = 127;
	public static final int TYPE_USER_WENDA_NOTICE = 128;
	public static final int TYPE_DAREN_HUIDA = 129;
	public static final int TYPE_SINA_USER = 130;
	public static final int TYPE_THIRDLOGIN = 131;
	public static final int TYPE_BASE_INFO = 132;
	public static final int TYPE_ITEM_INFO = 133;
	public static final int TYPE_LINE_INFO = 134;
	public static final int TYPE_LINGDUI_INFO = 135;
	public static final int TYPE_LDLINE_INFO = 136;
	public static final int TYPE_SHAITU = 137;
	public static final int TYPE_ZAN_INFO = 138;
	public static final int TYPE_LAJI_INFO = 139;
	public static final int TYPE_SHAITUQIAN_INFO = 140;
	public static final int TYPE_ZANS_INFO = 141;
	public static final int TYPE_PHASE_INFO = 142;

	/**
	 * 领队活动
	 */
	public static final int TYPE_ACTIVTIY = 143;

	private static DataManager mSingleton = null;

	private UserBean user = null;
	private boolean login_flag = false;

	public void setLogin_flag(boolean login_flag) {
		this.login_flag = login_flag;
	}

	private int zhuangbei_comment_total;
	List<UserDraftBean> drafts = new ArrayList<UserDraftBean>();

	public List<UserDraftBean> getDrafts() {
		return drafts;
	}

	public void setDrafts(List<UserDraftBean> drafts) {
		this.drafts = drafts;
	}

	public int getZhuangbei_comment_total() {
		return zhuangbei_comment_total;
	}

	public boolean isLogin_flag() {
		return login_flag;
	}

	public UserBean getUser() {
		if (user == null)
			user = new UserBean();
		return user;
	}

	public void setUser(UserBean u) {
		this.user = u;
	}

	private DataManager() {

	}

	/**
	 * 初始化数据管理器
	 */
	synchronized public static boolean init() {
		boolean result = true;
		L.enable(TAG, L.DEBUG_LEVEL);
		if (mSingleton != null) {
			return true;
		}
		try {
			// 创建DataManager
			mSingleton = new DataManager();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 退出数据管理器
	 */
	public static void exit() {
		if (mSingleton == null) {
			return;
		}
		// 退出时,是不要要做数据的保存操作,是时清空所有变量
	}

	/**
	 * 多线程安全: 获取DataManager句柄
	 */
	synchronized public static final DataManager getInstance() {
		if (mSingleton == null) {
			if (!init()) {
				if (L.ERROR)
					L.e(TAG, "Failed to init itself");
			}
		}
		return mSingleton;
	}

	/**
	 * 网络响应数据校验
	 * 
	 * @param params
	 * @return
	 */
	public String validateResult(String params) {
		// L.i(TAG, params);
		System.out.println(params + "");
		String status = "";
		try {
			JSONObject json = new JSONObject(params);
			status = json.getString("status");
			if (!"OK".equalsIgnoreCase(status)) {
				return json.optString("data");
			}
		} catch (Exception e) {
			status = "";
		}
		return status;
	}
	
	public String validateResultss(String params) {
		System.out.println(params + "");
		String status = "";
		try {
			JSONObject json = new JSONObject(params);
			status = json.getString("status");
		} catch (Exception e) {
			status = "";
		}
		return status;
	}
	
	/**
	 * 解析data
	 * @param params
	 * @return
	 */
	public String validateResults(String params) {
		System.out.println(params + "");
		String data = "";
		try {
			JSONObject json = new JSONObject(params);
			data = json.getString("data");
		} catch (Exception e) {
			data = "";
		}
		return data;
	}
	

	/**
	 * json数据解析
	 * 
	 * @param params
	 * @param type
	 * @return
	 */
	public Object jsonParse(String params, int type) {
		boolean result = false;
		try {
			JSONObject json = new JSONObject(params);
			switch (type) {
			case TYPE_MAIN_BANNER:
				JSONArray urls = json.optJSONArray("data");
				List<BannerBean> list = new ArrayList<BannerBean>();
				if (urls != null) {
					for (int i = 0; i < urls.length(); i++) {
						JSONObject urlobj = urls.optJSONObject(i);
						BannerBean bb = new BannerBean();
						bb.setId(urlobj.optString("id"));
						bb.setPosition(urlobj.optString("position"));
						bb.setType(urlobj.optString("type"));
						bb.setImageUrl(urlobj.optString("image_url"));
						list.add(bb);
					}
					return list;
				}
				return null;
			case TYPE_SINA_USER:
				System.out.println("TYPE_SINA_USER=====" + json.toString());
				break;
			case TYPE_WENDA_UPLOAD:
				ImageBean ib = new ImageBean();
				JSONObject ibjob = json.optJSONObject("data");
				ib.setImgurl(ibjob.optString("imgurl"));
				ib.setShowimg(ibjob.optString("showimg"));
				return ib;
			case TYPE_ZAN_INFO:
				// TODO
				AddLikeBean alb = new AddLikeBean();
				JSONObject zanInfoData = new JSONObject(params);
				alb.setData(zanInfoData.getString("status"));
				alb.setData(zanInfoData.getString("data"));
				break;
			case TYPE_SHAITUQIAN_INFO:
				JSONArray shaiTuQianData = json.optJSONArray("data");
				List<AlbumidBean> listttt = new ArrayList<AlbumidBean>();
				if (shaiTuQianData !=null) {
					for (int i = 0; i < shaiTuQianData.length(); i++) {
						JSONObject jjjjj = shaiTuQianData.optJSONObject(i);
						AlbumidBean db = new AlbumidBean();
						db.setAlbumid(jjjjj.optInt("albumid"));
						listttt.add(db);
					}
					return listttt ;
				}
				return null ;
			case TYPE_USER_INFO:
				JSONObject infojob1 = json.optJSONObject("data");
				JSONObject userObj = infojob1.optJSONObject("user");
				if(user == null)
					user = new UserBean();
				user.setUser_id(userObj.optString("user_id"));
				user.setEmail(userObj.optString("email"));
				user.setNickname(userObj.optString("nickname"));
				user.setHeadicon(userObj.optString("headicon"));

				user.setSex(userObj.optString("sex"));
				user.setProvince_id(userObj.optString("province_id"));
				user.setCity_id(userObj.optString("city_id"));
				user.setProvince(userObj.optString("province"));
				user.setCity(userObj.optString("city"));
				user.setIs_vip(userObj.optString("is_vip"));
				user.setPoint(userObj.optString("point"));
				user.setNote(userObj.optString("note"));
				user.setVtitle(userObj.optString("vtitle"));
				break;
			case TYPE_DAREN_HUIDA:
				DarenHuidaBean dhb = new DarenHuidaBean();
				JSONObject dhbobj = json.optJSONObject("data");
				JSONObject useroob = dhbobj.optJSONObject("user");
				DarenBean user1 = new DarenBean();
				user1.setUser_id(useroob.optString("user_id"));
				user1.setHeadicon(useroob.optString("headicon"));
				user1.setNickname(useroob.optString("nickname"));
				user1.setBirth(useroob.optString("birth"));
				user1.setSex(useroob.optString("sex"));
				user1.setPoint(useroob.optString("point"));
				user1.setCity(useroob.optString("city"));
				user1.setCreate_time(useroob.optString("create_time"));
				user1.setIs_online(useroob.optString("is_online"));
				user1.setProvince(useroob.optString("province"));
				user1.setNote(useroob.optString("note"));
				user1.setTags(useroob.optString("tags"));
				user1.setTitle(useroob.optString("title"));
				dhb.setUser(user1);
				List<HuidaBean> huis1 = new ArrayList<HuidaBean>();
				JSONArray huios = dhbobj.optJSONArray("lists");
				if (huios != null) {
					for (int i = 0; i < huios.length(); i++) {
						JSONObject yearobj = huios.optJSONObject(i);
						HuidaBean syb = new HuidaBean();
						syb.setId(yearobj.optString("id"));
						syb.setContent(yearobj.optString("content"));
						syb.setCommentnum(yearobj.optString("commentnum"));
						syb.setLikenum(yearobj.optString("likenum"));
						syb.setFlag(yearobj.optString("flag"));
						syb.setWenda_id(yearobj.optString("wenda_id"));
						syb.setAnswer_id(yearobj.optString("answer_id"));
						syb.setCreate_time(yearobj.optString("create_time"));
						syb.setUser_id(yearobj.optString("user_id"));
						syb.setText(yearobj.optString("text"));
						syb.setTitle(yearobj.optString("title"));
						huis1.add(syb);
					}
					dhb.setLists(huis1);
				}
				return dhb;
			case TYPE_LINGDUI_INFO:
				List<LingduiInfoBean> listlb = new ArrayList<LingduiInfoBean>();
				JSONArray jsonListlb = json.optJSONArray("data");
				if (jsonListlb != null) {
					for (int i = 0; i < jsonListlb.length(); i++) {
						JSONObject objlb = jsonListlb.optJSONObject(i);
						LingduiInfoBean ldib = new LingduiInfoBean();
						ldib.setCity(objlb.optString("city"));
						ldib.setNickname(objlb.optString("nickname"));
						ldib.setHeadicon(objlb.optString("headicon"));
						ldib.setProvince(objlb.optString("province"));
						ldib.setNote(objlb.optString("note"));
						ldib.setVtitle(objlb.optString("vtitle"));
						ldib.setIs_vip(objlb.optInt("is_vip"));
						ldib.setUser_id(objlb.optInt("user_id"));
						listlb.add(ldib);
					}
					return listlb;
				}
				return null;
			case TYPE_LDLINE_INFO:
				JSONArray ld = json.optJSONArray("data");
				List<LingduiLineBean> llllll = new ArrayList<LingduiLineBean>();
				if (ld != null) {
					for (int i = 0; i < ld.length(); i++) {
						JSONObject jsobbb = ld.optJSONObject(i);
						LingduiLineBean ldlbean = new LingduiLineBean();
						ldlbean.setId(jsobbb.optInt("id"));
						ldlbean.setThumb(jsobbb.optString("thumb"));
						ldlbean.setTitle(jsobbb.optString("title"));
						llllll.add(ldlbean);
					}
					return llllll;
				}
				return null;
			case TYPE_THIRDLOGIN:
				JSONObject datajson2 = json.optJSONObject("data");
				UserBean u = new UserBean();
				u.setUser_id(datajson2.optString("user_id"));
				u.setEmail(datajson2.optString("email"));
				u.setSn(datajson2.optString("sn"));
				u.setNickname(datajson2.optString("nickname"));
				u.setHeadicon(datajson2.optString("headicon"));
				u.setSex(datajson2.optString("sex"));
				u.setPoint(datajson2.optString("point"));
				login_flag = true;
				setUser(u);
				break;
			case TYPE_DAREN_DETAIL:
				DarenDetailBean ddb = new DarenDetailBean();
				JSONObject ddbobj = json.optJSONObject("data");
				ddb.setFollowers_num(ddbobj.optString("followers_num"));
				ddb.setFollowing_num(ddbobj.optString("following_num"));
				ddb.setHuida_num(ddbobj.optString("huida_num"));
				ddb.setShaitu_num(ddbobj.optString("shaitu_num"));
				ddb.setRelation(ddbobj.optString("relation"));
				JSONObject userobj = ddbobj.optJSONObject("user");
				DarenBean user = new DarenBean();
				user.setUser_id(userobj.optString("user_id"));
				user.setHeadicon(userobj.optString("headicon"));
				user.setNickname(userobj.optString("nickname"));
				user.setBirth(userobj.optString("birth"));
				user.setSex(userobj.optString("sex"));
				user.setPoint(userobj.optString("point"));
				user.setCity(userobj.optString("city"));
				user.setCreate_time(userobj.optString("create_time"));
				user.setIs_online(userobj.optString("is_online"));
				user.setProvince(userobj.optString("province"));
				user.setNote(userobj.optString("note"));
				user.setTags(userobj.optString("tags"));
				user.setTitle(userobj.optString("title"));
				ddb.setUser(user);
				List<ShaiYearBean> shaiys = new ArrayList<ShaiYearBean>();
				JSONArray yers = ddbobj.optJSONArray("shaitu_list");
				if (yers != null) {
					for (int i = 0; i < yers.length(); i++) {
						JSONObject yearobj = yers.optJSONObject(i);
						ShaiYearBean syb = new ShaiYearBean();
						syb.setYear(yearobj.optString("year"));
						List<ShaiDateBean> shaidate = new ArrayList<ShaiDateBean>();
						JSONArray datesls = yearobj.optJSONArray("yearlist");
						if (datesls != null) {
							for (int j = 0; j < datesls.length(); j++) {
								JSONObject dateobj = datesls.optJSONObject(j);
								ShaiDateBean sdb = new ShaiDateBean();
								JSONArray dtls = dateobj
										.optJSONArray("datelist");
								List<ShaituDateBean> shaitudate = new ArrayList<ShaituDateBean>();
								if (dtls != null) {
									for (int k = 0; k < dtls.length(); k++) {
										JSONObject dtobj = dtls
												.optJSONObject(k);
										ShaituDateBean stdb = new ShaituDateBean();
										stdb.setModifytime(dtobj
												.optString("modifytime"));
										stdb.setCreatetime(dtobj
												.optString("createtime"));
										stdb.setId(dtobj.optString("id"));
										stdb.setRecommendtime(dtobj
												.optString("recommendtime"));
										stdb.setFlag(dtobj.optString("flag"));
										stdb.setClicktimes(dtobj
												.optString("clicktimes"));
										stdb.setImage_url(dtobj
												.optString("image_url"));
										stdb.setTag(dtobj.optString("tag"));
										stdb.setDiet_desc(dtobj
												.optString("diet_desc"));
										stdb.setUser_id(dtobj
												.optString("user_id"));
										stdb.setIs_idx(dtobj
												.optString("is_idx"));
										stdb.setZhuangbei_id(dtobj
												.optString("zhuangbei_id"));
										shaitudate.add(stdb);
									}
									sdb.setDateBean(shaitudate);
								}
								sdb.setDate(dateobj.optString("date"));
								shaidate.add(sdb);
							}
							syb.setDateBean(shaidate);
						}
						shaiys.add(syb);
					}
					ddb.setShaiyears(shaiys);
				}
				List<AnswerBean> ans = new ArrayList<AnswerBean>();
				JSONArray huis = ddbobj.optJSONArray("huida_list");
				if (huis != null) {
					for (int i = 0; i < huis.length(); i++) {
						JSONObject obj = huis.optJSONObject(i);
						AnswerBean abb = new AnswerBean();
						abb.setId(obj.optString("id"));
						abb.setContent(obj.optString("content"));
						abb.setCommentnum(obj.optString("commentnum"));
						abb.setHeadicon(obj.optString("headicon"));
						abb.setLikenum(obj.optString("likenum"));
						abb.setFlag(obj.optString("flag"));
						abb.setWenda_id(obj.optString("wenda_id"));
						abb.setIs_vip(obj.optString("is_vip"));
						abb.setVtitle(obj.optString("vtitle"));
						abb.setNickname(obj.optString("nickname"));
						abb.setCreate_time(obj.optString("create_time"));
						abb.setUser_id(obj.optString("user_id"));
						abb.setText(obj.optString("text"));
						abb.setTitle(obj.optString("title"));
						ans.add(abb);
					}
					ddb.setAnswers(ans);
				}
				return ddb;
			case TYPE_DAREN_SHAITU_LIST:
				List<ShaiYearBean> shaiyears1 = new ArrayList<ShaiYearBean>();
				JSONObject ssssobj = json.optJSONObject("data");
				JSONArray years1 = ssssobj.optJSONArray("lists");
				if (years1 != null) {
					for (int i = 0; i < years1.length(); i++) {
						JSONObject yearobj = years1.optJSONObject(i);
						ShaiYearBean syb = new ShaiYearBean();
						syb.setYear(yearobj.optString("year"));
						List<ShaiDateBean> shaidate = new ArrayList<ShaiDateBean>();
						JSONArray datesls = yearobj.optJSONArray("yearlist");
						if (datesls != null) {
							for (int j = 0; j < datesls.length(); j++) {
								JSONObject dateobj = datesls.optJSONObject(j);
								ShaiDateBean sdb = new ShaiDateBean();
								JSONArray dtls = dateobj
										.optJSONArray("datelist");
								List<ShaituDateBean> shaitudate = new ArrayList<ShaituDateBean>();
								if (dtls != null) {
									for (int k = 0; k < dtls.length(); k++) {
										JSONObject dtobj = dtls
												.optJSONObject(k);
										ShaituDateBean stdb = new ShaituDateBean();
										stdb.setModifytime(dtobj
												.optString("modifytime"));
										stdb.setCreatetime(dtobj
												.optString("createtime"));
										stdb.setId(dtobj.optString("id"));
										stdb.setRecommendtime(dtobj
												.optString("recommendtime"));
										stdb.setFlag(dtobj.optString("flag"));
										stdb.setClicktimes(dtobj
												.optString("clicktimes"));
										stdb.setImage_url(dtobj
												.optString("image_url"));
										stdb.setTag(dtobj.optString("tag"));
										stdb.setDiet_desc(dtobj
												.optString("diet_desc"));
										stdb.setUser_id(dtobj
												.optString("user_id"));
										stdb.setIs_idx(dtobj
												.optString("is_idx"));
										stdb.setZhuangbei_id(dtobj
												.optString("zhuangbei_id"));
										shaitudate.add(stdb);
									}
									sdb.setDateBean(shaitudate);
								}
								sdb.setDate(dateobj.optString("date"));
								shaidate.add(sdb);
							}
							syb.setDateBean(shaidate);
						}
						shaiyears1.add(syb);
					}
				}
				return shaiyears1;
			case TYPE_USER_SHAITU_LIST:
				List<ShaiYearBean> shaiyears = new ArrayList<ShaiYearBean>();
				JSONArray years = json.optJSONArray("data");
				if (years != null) {
					for (int i = 0; i < years.length(); i++) {
						JSONObject yearobj = years.optJSONObject(i);
						ShaiYearBean syb = new ShaiYearBean();
						syb.setYear(yearobj.optString("year"));
						List<ShaiDateBean> shaidate = new ArrayList<ShaiDateBean>();
						JSONArray datesls = yearobj.optJSONArray("yearlist");
						if (datesls != null) {
							for (int j = 0; j < datesls.length(); j++) {
								JSONObject dateobj = datesls.optJSONObject(j);
								ShaiDateBean sdb = new ShaiDateBean();
								JSONArray dtls = dateobj
										.optJSONArray("datelist");
								List<ShaituDateBean> shaitudate = new ArrayList<ShaituDateBean>();
								if (dtls != null) {
									for (int k = 0; k < dtls.length(); k++) {
										JSONObject dtobj = dtls
												.optJSONObject(k);
										ShaituDateBean stdb = new ShaituDateBean();
										stdb.setModifytime(dtobj
												.optString("modifytime"));
										stdb.setCreatetime(dtobj
												.optString("createtime"));
										stdb.setId(dtobj.optString("id"));
										stdb.setRecommendtime(dtobj
												.optString("recommendtime"));
										stdb.setFlag(dtobj.optString("flag"));
										stdb.setClicktimes(dtobj
												.optString("clicktimes"));
										stdb.setImage_url(dtobj
												.optString("image_url"));
										stdb.setTag(dtobj.optString("tag"));
										stdb.setDiet_desc(dtobj
												.optString("diet_desc"));
										stdb.setUser_id(dtobj
												.optString("user_id"));
										stdb.setIs_idx(dtobj
												.optString("is_idx"));
										stdb.setZhuangbei_id(dtobj
												.optString("zhuangbei_id"));
										shaitudate.add(stdb);
									}
									sdb.setDateBean(shaitudate);
								}
								sdb.setDate(dateobj.optString("date"));
								shaidate.add(sdb);
							}
							syb.setDateBean(shaidate);
						}
						shaiyears.add(syb);
					}
				}
				return shaiyears;
			case TYPE_LINE_INFO:
				List<LineInfoBean> lib = new ArrayList<LineInfoBean>();
				JSONArray infolib = json.optJSONArray("data");
				if (infolib != null) {
					for (int i = 0; i < infolib.length(); i++) {
						LineInfoBean libbean = new LineInfoBean();
						JSONObject js_obj = infolib.optJSONObject(i);
						libbean.setActiveCost(js_obj.optInt("activeCost"));
						libbean.setAddress(js_obj.optString("address"));
						libbean.setActiveDiff(js_obj.optInt("activeDiff"));
						libbean.setActiveIntro(js_obj.optString("activeIntro"));
						libbean.setActiveType(js_obj.optInt("activeType"));
						libbean.setAreaid(js_obj.optInt("ateaid"));
						libbean.setCatename(js_obj.optString("catename"));
						libbean.setCityid(js_obj.optInt("cityid"));
						libbean.setCityname(js_obj.optString("cityname"));
						libbean.setCostFee(js_obj.optInt("costfree"));
						libbean.setEnd_date(js_obj.optString("end_date"));
						libbean.setEndTimes(js_obj.optString("endTimes"));
						libbean.setEquipDiscuss(js_obj.optString("equipDiscuss"));
						libbean.setId(js_obj.optInt("id"));
						libbean.setMan(js_obj.optInt("man"));
						libbean.setNote(js_obj.optString("note"));
						libbean.setPlaceIntro(js_obj.optString("placeIntro"));
						libbean.setProvinceid(js_obj.optInt("provinceid"));
						libbean.setRenqizhi(js_obj.optInt("renqizhi"));
						libbean.setStart_date(js_obj.optString("start_date"));
						libbean.setSelection_end_date(js_obj
								.optString("selection_end_date"));
						libbean.setStartTimes(js_obj.optString("startTimes"));
						libbean.setTitle(js_obj.optString("title"));
						libbean.setThumb(js_obj.optString("thumb"));
						libbean.setTotal_showPic(js_obj.optInt("total_showPic"));
						libbean.setTravePlan(js_obj.optString("travePlan"));
						libbean.setUid(js_obj.optInt("uid"));
						JSONObject bbb = js_obj.optJSONObject("userInfo");
						UserInfo userInfo = new UserInfo();
						userInfo.setHeadicon(bbb.optString("headicon"));
						userInfo.setNickname(bbb.optString("nickname"));
						userInfo.setNote(bbb.optString("note"));
						userInfo.setVip(bbb.optString("vip"));
						libbean.setUserInfo(userInfo);

						JSONArray ccc = js_obj.optJSONArray("equipcate");
						List<Equipcate> equipcate = new ArrayList<Equipcate>();
						if (ccc != null) {
							for (int j = 0; j < ccc.length(); j++) {
								Equipcate e = new Equipcate();
								JSONObject hhh = ccc.optJSONObject(j);
								e.setCatname(hhh.optString("catname"));
								e.setThumb(hhh.optString("thumb"));
								equipcate.add(e);
							}
						}
						libbean.setEquipcate(equipcate);

						JSONArray commentJsonData = js_obj.optJSONArray("comment");
						List<Comment> commentList = new ArrayList<Comment>();
						if (commentJsonData != null) {
							for (int j = 0; j < commentJsonData.length(); j++) {
								Comment comment = new Comment();
								JSONObject eee = commentJsonData.optJSONObject(j);
								comment.setComment(eee.optString("comment"));
								comment.setCreatetime(eee
										.optString("createtime"));
								comment.setHeadicon(eee.optString("headicon"));
								comment.setNickname(eee.optString("nickname"));
								commentList.add(comment);
							}
						}
						libbean.setComment(commentList);

						JSONArray fff = js_obj.optJSONArray("showPic");
						ArrayList<ShowPic> picList = new ArrayList<ShowPic>();
						if (fff != null) {
							for (int k = 0; k < fff.length(); k++) {
								ShowPic showPic = new ShowPic();
								JSONObject hhh = fff.optJSONObject(k);
								showPic.setImage_url(hhh.optString("image_url"));
								picList.add(showPic);
							}
						}
						libbean.setShowPic(picList);
						lib.add(libbean);
					}
					return lib;
				}

				return null;
			case TYPE_USER_WENDA_NOTICE:
				JSONArray notices1 = json.optJSONArray("data");
				List<UserMsgBean> msgs1 = new ArrayList<UserMsgBean>();
				if (notices1 != null) {
					for (int i = 0; i < notices1.length(); i++) {
						JSONObject mso = notices1.optJSONObject(i);
						UserMsgBean umb = new UserMsgBean();
						umb.setObject_id(mso.optString("object_id"));
						umb.setHeadicon(mso.optString("headicon"));
						umb.setImage_url(mso.optString("image_url"));
						umb.setNickname(mso.optString("nickname"));
						umb.setCreate_time(mso.optString("create_time"));
						umb.setUser_id(mso.optString("user_id"));
						umb.setType_id(mso.optString("type_id"));
						umb.setContent(mso.optString("content"));
						umb.setWenda_id(mso.optString("wenda_id"));
						msgs1.add(umb);
					}
					return msgs1;
				}
				return null;
			case TYPE_USER_NOTICE:
				JSONArray notices = json.optJSONArray("data");
				List<UserMsgBean> msgs = new ArrayList<UserMsgBean>();
				if (notices != null) {
					for (int i = 0; i < notices.length(); i++) {
						JSONObject mso = notices.optJSONObject(i);
						UserMsgBean umb = new UserMsgBean();
						umb.setObject_id(mso.optString("object_id"));
						umb.setHeadicon(mso.optString("headicon"));
						umb.setImage_url(mso.optString("image_url"));
						umb.setNickname(mso.optString("nickname"));
						umb.setCreate_time(mso.optString("create_time"));
						umb.setUser_id(mso.optString("user_id"));
						umb.setType_id(mso.optString("type_id"));
						umb.setContent(mso.optString("content"));
						msgs.add(umb);
					}
					return msgs;
				}
				return null;
			case TYPE_MAIN_CHECKVERSION:
				VersionBean vb = new VersionBean();
				JSONObject vbobj = json.optJSONObject("data");
				vb.setChangelog(vbobj.optString("changelog"));
				vb.setUrl(vbobj.optString("url"));
				vb.setVersion(vbobj.optString("version"));
				return vb;
			case TYPE_USER_ANSWER:
				List<UserAnswerBean> asblist = new ArrayList<UserAnswerBean>();
				JSONArray asbs = json.optJSONArray("data");
				if (asbs != null) {
					for (int i = 0; i < asbs.length(); i++) {
						JSONObject obj = asbs.optJSONObject(i);
						UserAnswerBean uab = new UserAnswerBean();
						uab.setAnswer_id(obj.optString("answer_id"));
						uab.setContent(obj.optString("content"));
						uab.setCreate_time(obj.optString("create_time"));
						uab.setLikenum(obj.optString("likenum"));
						uab.setText(obj.optString("text"));
						uab.setTitle(obj.optString("title"));
						uab.setWenda_id(obj.optString("wenda_id"));
						asblist.add(uab);
					}
				}
				return asblist;
			case TYPE_ZHUANGBEI_LIST:
				JSONArray jsonArray = json.optJSONArray("data");
				List<ZhuangbeiBean> zhuangbei_list = new ArrayList<ZhuangbeiBean>();
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject obj = jsonArray.optJSONObject(i);
						ZhuangbeiBean zb = new ZhuangbeiBean();
						zb.setId(obj.optString("id"));
						zb.setTitle(obj.optString("title"));
						zb.setThumb(obj.optString("thumb"));
						zb.setStar(obj.optString("star"));
						zb.setScore(obj.optString("score"));
						zhuangbei_list.add(zb);
					}
					return zhuangbei_list;
				}
				return null;
			case TYPE_SHAITU:
				JSONArray ldal = json.optJSONArray("data");
				List<ShaituBean> ll = new ArrayList<ShaituBean>();
				if (ldal != null) {
					for (int i = 0; i < ldal.length(); i++) {
						JSONObject jssss = ldal.optJSONObject(i);
						ShaituBean sb = new ShaituBean();
						sb.setId(jssss.optInt("id"));
						sb.setImage_url(jssss.optString("image_url"));
						ll.add(sb);
					}
					return ll;
				}
				return null;
			case TYPE_SHAITU_DETAIL:
				ShaituDetailBean sdb = new ShaituDetailBean();
				JSONObject sdbjob = json.optJSONObject("data");
				JSONObject infojob = sdbjob.optJSONObject("info");
				JSONObject authorjob = sdbjob.optJSONObject("author");
				sdb.setId(infojob.optString("id"));
				sdb.setIs_idx(infojob.optString("is_idx"));
				sdb.setUser_id(infojob.optString("user_id"));
				sdb.setTag(infojob.optString("tag"));
				sdb.setDiet_desc(infojob.optString("diet_desc"));
				sdb.setCreatetime(infojob.optString("createtime"));
				sdb.setModifytime(infojob.optString("modifytime"));
				sdb.setZhuangbei_id(infojob.optString("zhuangbei_id"));
				sdb.setFlag(infojob.optString("flag"));
				sdb.setComments_num(infojob.optString("comments_num"));
				sdb.setClicktimes(infojob.optString("clicktimes"));
				sdb.setRecommendtime(infojob.optString("recommendtime"));
				sdb.setHeadicon(authorjob.optString("headicon"));
				sdb.setNickname(authorjob.optString("nickname"));
				sdb.setSex(authorjob.optString("sex"));
				sdb.setBirth(authorjob.optString("birth"));
				sdb.setProvince(authorjob.optString("province"));
				sdb.setCity(authorjob.optString("city"));
				sdb.setNote(authorjob.optString("note"));
				sdb.setPoint(authorjob.optString("point"));
				sdb.setIs_vip(authorjob.optString("is_vip"));
				sdb.setTags(authorjob.optString("tags"));
				sdb.setCreate_time(authorjob.optString("create_time"));
				sdb.setImage_num(sdbjob.optString("image_num"));
				sdb.setShare_url(sdbjob.optString("share_url"));
				List<ShaituImageBean> sib = new ArrayList<ShaituImageBean>();
				JSONArray sdbArray = sdbjob.optJSONArray("images");
				if (sdbArray != null) {
					for (int i = 0; i < sdbArray.length(); i++) {
						ShaituImageBean sibBean = new ShaituImageBean();
						JSONObject sibObject = sdbArray.optJSONObject(i);
						sibBean.setCreatetime(sibObject.optString("createtime"));
						sibBean.setFlag(sibObject.optString("flag"));
						sibBean.setId(sibObject.optString("id"));
						sibBean.setImage_url(sibObject.optString("image_url"));
						sibBean.setItem_title(sibObject.optString("item_title"));
						sibBean.setSzb_id(sibObject.optString("szb_id"));
						sibBean.setUser_id(sibObject.optString("user_id"));
						sibBean.setZhuangbei_id(sibObject
								.optString("zhuangbei_id"));
						sib.add(sibBean);
					}
					sdb.setImages(sib);
				}
				return sdb;
			case TYPE_WENDA_ANSWER:
				AnswerDetailBean adb = new AnswerDetailBean();
				JSONObject adbjob = json.optJSONObject("data");
				adb.setId(adbjob.optString("id"));
				adb.setAnswernum(adbjob.optString("answernum"));
				adb.setLikenum(adbjob.optString("likenum"));
				adb.setTitle(adbjob.optString("title"));
				adb.setDesc(adbjob.optString("desc"));
				adb.setShare_url(adbjob.optString("share_url"));
				adb.setDate(adbjob.optString("date"));
				adb.setAnswer_like_status(adbjob
						.optString("answer_like_status"));
				adb.setQuestion_like_status(adbjob
						.optString("question_like_status"));
				JSONObject ansjob = adbjob.optJSONObject("answer");
				AnswerBean abbb = new AnswerBean();
				abbb.setId(ansjob.optString("id"));
				abbb.setContent(ansjob.optString("content"));
				abbb.setCommentnum(ansjob.optString("commentnum"));
				abbb.setHeadicon(ansjob.optString("headicon"));
				abbb.setLikenum(ansjob.optString("likenum"));
				abbb.setFlag(ansjob.optString("flag"));
				abbb.setWenda_id(ansjob.optString("wenda_id"));
				abbb.setIs_vip(ansjob.optString("is_vip"));
				abbb.setVtitle(ansjob.optString("vtitle"));
				abbb.setNickname(ansjob.optString("nickname"));
				abbb.setCreate_time(ansjob.optString("create_time"));
				abbb.setUser_id(ansjob.optString("user_id"));
				adb.setAnswer(abbb);
				return adb;
			case TYPE_WENDA_QUESTION:
				QuestionBean qb = new QuestionBean();
				JSONObject qbjob = json.optJSONObject("data");
				qb.setAnswernum(qbjob.optString("answernum"));
				JSONArray optJSONArray1 = qbjob.optJSONArray("images");
				List<String> images = new ArrayList<String>();
				if (optJSONArray1 != null) {
					for (int j = 0; j < optJSONArray1.length(); j++) {
						images.add((String) optJSONArray1.get(j));
					}
					qb.setImages(images);
				}
				qb.setDate(qbjob.optString("data"));
				qb.setDesc(qbjob.optString("desc"));
				qb.setId(qbjob.optString("id"));
				qb.setLike_status(qbjob.optString("like_status"));
				qb.setLikenum(qbjob.optString("likenum"));
				qb.setShare_url(qbjob.optString("share_url"));
				qb.setTitle(qbjob.optString("title"));
				JSONObject aujob = qbjob.optJSONObject("author");
				AuthorBean ab = new AuthorBean();
				ab.setTags(aujob.optString("tags"));
				ab.setPoint(aujob.optString("point"));
				ab.setBirth(aujob.optString("birth"));
				ab.setSex(aujob.optString("sex"));
				ab.setHeadicon(aujob.optString("headicon"));
				ab.setFlag(aujob.optString("flag"));
				ab.setIs_vip(aujob.optString("is_vip"));
				ab.setNickname(aujob.optString("nickname"));
				ab.setCreate_time(aujob.optString("create_time"));
				ab.setProvince(aujob.optString("province"));
				ab.setUser_id(aujob.optString("user_id"));
				ab.setNote(aujob.optString("note"));
				ab.setCity(aujob.optString("city"));
				qb.setAuthor(ab);
				JSONArray anArray = qbjob.optJSONArray("answers");
				List<AnswerBean> ansList = new ArrayList<AnswerBean>();
				if (anArray != null) {
					for (int i = 0; i < anArray.length(); i++) {
						JSONObject obj = anArray.optJSONObject(i);
						AnswerBean abb = new AnswerBean();
						abb.setId(obj.optString("id"));
						abb.setContent(obj.optString("content"));
						abb.setCommentnum(obj.optString("commentnum"));
						abb.setHeadicon(obj.optString("headicon"));
						abb.setLikenum(obj.optString("likenum"));
						abb.setFlag(obj.optString("flag"));
						abb.setWenda_id(obj.optString("wenda_id"));
						abb.setIs_vip(obj.optString("is_vip"));
						abb.setVtitle(obj.optString("vtitle"));
						abb.setNickname(obj.optString("nickname"));
						abb.setCreate_time(obj.optString("create_time"));
						abb.setUser_id(obj.optString("user_id"));
						ansList.add(abb);
					}
					qb.setAnswers(ansList);
				}
				JSONObject darenjob = qbjob.optJSONObject("daren");
				DarenBean db = new DarenBean();
				if (darenjob != null) {

				}
				return qb;
			case TYPE_SHAITU_CREATE:
				System.out.println("TYPE_SHAITU_CREATE=====" + json.toString());
				break;
			case TYPE_UPLOAD_PIC:
				System.out.println("TYPE_UPLOAD_PIC=====" + json.toString());
				break;
			case TYPE_DAYI_LIST:
				JSONArray dayiArray = json.optJSONArray("data");
				List<WenDayiBean> dayiList = new ArrayList<WenDayiBean>();
				if (dayiArray != null) {
					for (int i = 0; i < dayiArray.length(); i++) {
						JSONObject obj = dayiArray.optJSONObject(i);
						WenDayiBean wdb = new WenDayiBean();
						wdb.setId(obj.optString("id"));
						wdb.setHeadicon(obj.optString("headicon"));
						wdb.setIs_vip(obj.optString("is_vip"));
						wdb.setAnswernum(obj.optString("answernum"));
						wdb.setCommentnum(obj.optString("commentnum"));
						wdb.setContent(obj.optString("content"));
						wdb.setCreate_time(obj.optString("create_time"));
						wdb.setDate(obj.optString("date"));
						wdb.setFlag(obj.optString("flag"));
						wdb.setLikenum(obj.optString("likenum"));
						wdb.setNickname(obj.optString("nickname"));
						wdb.setTitle(obj.optString("title"));
						wdb.setUser_id(obj.optString("user_id"));
						wdb.setVtitle(obj.optString("vtitle"));
						wdb.setWenda_id(obj.optString("wenda_id"));
						dayiList.add(wdb);
					}
					return dayiList;
				}
				return null;
				/*
				 * List<V> if(jsonArray!=null){ for(int
				 * i=0;i<jsonArray.length();i++){ JSONObject obj =
				 * jsonArray.optJSONObject(i); ZhuangbeiBean zb = new
				 * ZhuangbeiBean(); zb.setId(obj.optString("id"));
				 * zb.setTitle(obj.optString("title"));
				 * zb.setThumb(obj.optString("thumb"));
				 * zb.setStar(obj.optString("star"));
				 * zb.setScore(obj.optString("score")); zhuangbei_list.add(zb);
				 * } return zhuangbei_list; }
				 */
			case TYPE_MAIN_ADDLIKE:
				System.out.println("TYPE_MAIN_ADDLIKE==" + json.toString());
				break;
			case TYPE_USER_DAREN:
				List<DarenBean> listDaren1 = new ArrayList<DarenBean>();
				JSONArray jsonDaren1 = json.optJSONArray("data");
				if (jsonDaren1 != null) {
					for (int i = 0; i < jsonDaren1.length(); i++) {
						JSONObject obj = jsonDaren1.optJSONObject(i);
						DarenBean zb = new DarenBean();
						zb.setUser_id(obj.optString("user_id"));
						zb.setHeadicon(obj.optString("headicon"));
						zb.setNickname(obj.optString("nickname"));
						zb.setBirth(obj.optString("birth"));
						zb.setSex(obj.optString("sex"));
						zb.setPoint(obj.optString("point"));
						zb.setCity(obj.optString("city"));
						zb.setCreate_time(obj.optString("create_time"));
						zb.setIs_online(obj.optString("is_online"));
						zb.setProvince(obj.optString("province"));
						zb.setNote(obj.optString("note"));
						zb.setTags(obj.optString("tags"));
						zb.setTitle(obj.optString("title"));
						listDaren1.add(zb);
					}
					return listDaren1;
				}
				return null;
			case TYPE_LAJI_INFO:
				JSONObject jsssss = json.optJSONObject("data");
				CommentLajis ccccccc = new CommentLajis();
				ccccccc.setComment_total(jsssss.optString("comment_total"));
				JSONArray jdddd = jsssss.optJSONArray("comment");
				List<Comments> laaa = new ArrayList<Comments>();
				if (jdddd != null) {
					for (int i = 0; i < jdddd.length(); i++) {
						Comments c = new Comments();
						JSONObject hhhhh = jdddd.optJSONObject(i);
						c.setComment(hhhhh.optString("comment"));
						c.setCommentid(hhhhh.optString("commentid"));
						c.setCreatetime(hhhhh.optString("createtime"));
						c.setHeadicon(hhhhh.optString("headicon"));
						c.setNickname(hhhhh.optString("nickname"));
						laaa.add(c);
					}
					ccccccc.setComments(laaa);
					
					return ccccccc ;
				}
				return null;
			case TYPE_DAREN_LISTS:
				List<DarenBean> listDaren = new ArrayList<DarenBean>();
				JSONArray jsonDaren = json.optJSONArray("data");
				if (jsonDaren != null) {
					for (int i = 0; i < jsonDaren.length(); i++) {
						JSONObject obj = jsonDaren.optJSONObject(i);
						DarenBean zb = new DarenBean();
						zb.setUser_id(obj.optString("user_id"));
						zb.setHeadicon(obj.optString("headicon"));
						zb.setNickname(obj.optString("nickname"));
						zb.setBirth(obj.optString("birth"));
						zb.setSex(obj.optString("sex"));
						zb.setPoint(obj.optString("point"));
						zb.setCity(obj.optString("city"));
						zb.setCreate_time(obj.optString("create_time"));
						zb.setIs_online(obj.optString("is_online"));
						zb.setProvince(obj.optString("province"));
						zb.setNote(obj.optString("note"));
						zb.setTags(obj.optString("tags"));
						zb.setTitle(obj.optString("title"));
						listDaren.add(zb);
					}
					return listDaren;
				}
				return null;
			case TYPE_WENDA_CATES_LIST:
				List<WendaNewBean> listNew1 = new ArrayList<WendaNewBean>();
				JSONArray jsonNewA1 = json.optJSONArray("data");
				if (jsonNewA1 != null) {
					for (int i = 0; i < jsonNewA1.length(); i++) {
						JSONObject obj = jsonNewA1.optJSONObject(i);
						WendaNewBean zb = new WendaNewBean();
						zb.setId(obj.optString("id"));
						zb.setTitle(obj.optString("title"));
						zb.setAnswernum(obj.optString("answernum"));
						zb.setCate(obj.optString("cate"));
						zb.setDate(obj.optString("date"));
						zb.setUser_id(obj.optString("user_id"));
						/*
						 * JSONArray optJSONArray = obj.optJSONArray("images");
						 * List<String> images = new ArrayList<String>();
						 * if(optJSONArray!=null){ for(int
						 * j=0;j<optJSONArray.length();j++){ JSONObject obj1 =
						 * optJSONArray.optJSONObject(j);
						 * images.add(obj1.optString(name)) } }
						 */
						listNew1.add(zb);
					}
					return listNew1;
				}
				return null;
			case TYPE_WENDA_LATEST:
				List<WendaNewBean> listNew = new ArrayList<WendaNewBean>();
				JSONArray jsonNewA = json.optJSONArray("data");
				if (jsonNewA != null) {
					for (int i = 0; i < jsonNewA.length(); i++) {
						JSONObject obj = jsonNewA.optJSONObject(i);
						WendaNewBean zb = new WendaNewBean();
						zb.setId(obj.optString("id"));
						zb.setTitle(obj.optString("title"));
						zb.setAnswernum(obj.optString("answernum"));
						zb.setCate(obj.optString("cate"));
						zb.setCate_id(obj.optString("cate_id"));
						zb.setCate_title(obj.optString("cate_title"));
						zb.setDate(obj.optString("date"));
						zb.setUser_id(obj.optString("user_id"));

						JSONArray optJSONArray = obj.optJSONArray("images");
						List<String> images1 = new ArrayList<String>();
						if (optJSONArray != null) {
							for (int j = 0; j < optJSONArray.length(); j++) {
								images1.add((String) optJSONArray.get(j));
							}
							zb.setImages(images1);
						}

						listNew.add(zb);
					}
					return listNew;
				}
				return null;
			case TYPE_MAIN_COMMENT:
				System.out.println("TYPE_MAIN_COMMENT==" + json.toString());
				break;
			case TYPE_ZHUANJI_LIST:
				JSONArray zhuanjiArray = json.optJSONArray("data");
				List<ZhuanjiBean> zhuanji_list = new ArrayList<ZhuanjiBean>();
				if (zhuanjiArray != null) {
					for (int i = 0; i < zhuanjiArray.length(); i++) {
						JSONObject obj = zhuanjiArray.optJSONObject(i);
						ZhuanjiBean zj = new ZhuanjiBean();
						zj.setId(obj.optString("id"));
						zj.setTitle(obj.optString("title"));
						zj.setDesc(obj.optString("desc"));
						zj.setImage(obj.optString("image"));
						zhuanji_list.add(zj);
					}
					return zhuanji_list;
				}
				return null;
			case TYPE_ZHUANGBEI_DETAIL:
				ZhuangbeiBean zb1 = new ZhuangbeiBean();
				JSONObject job = json.optJSONObject("data");
				zb1.setTitle(job.optString("title"));
				zb1.setImage(job.optString("image"));
				zb1.setContent(job.optString("content"));
				zb1.setScore(job.optString("score"));
				zb1.setStar(job.optString("star"));
				zb1.setLike(job.optString("like"));
				zb1.setShare_url(job.optString("share_url"));
				zb1.setWap_url(job.optString("wap_url"));
				return zb1;
			case TYPE_ITEM_INFO:
				List<ItemInfoBean> itembean = new ArrayList<ItemInfoBean>();
				JSONArray itemay = json.optJSONArray("data");
				if (itemay != null) {
					for (int i = 0; i < itemay.length(); i++) {
						ItemInfoBean itemb = new ItemInfoBean();
						JSONObject aaa = itemay.optJSONObject(i);
						itemb.setId(aaa.optInt("id"));
						itemb.setRenqizhi(aaa.optInt("renqizhi"));
						itemb.setImageUrl(aaa.optString("image_url"));
						itemb.setTitle(aaa.optString("title"));
						itemb.setUid(aaa.optInt("uid"));
						itemb.setStartDate(aaa.optString("start_date"));
						itemb.setEndDate(aaa.optString("end_date"));
						JSONObject bbb = aaa.optJSONObject("userInfo");
						UserInfo userInfo = new UserInfo();
						userInfo.setHeadicon(bbb.optString("headicon"));
						userInfo.setNickname(bbb.optString("nickname"));
						itemb.setUserinfo(userInfo);
						itembean.add(itemb);
					}
					return itembean;
				}
				return null;
			case TYPE_PHASE_INFO:
				List<PhaseItem> phaseItems = new ArrayList<PhaseItem>();
				JSONArray phaseArray = json.optJSONArray("data");
				if (phaseArray != null) {
					for (int i = 0; i < phaseArray.length(); i++) {
						JSONObject aaa = phaseArray.optJSONObject(i);
						PhaseItem item = new PhaseItem(aaa.optString("stages",""), aaa.optInt("offset",0));
						phaseItems.add(item);
					}
					return phaseItems;
				}
				return null;
			case TYPE_ZHUANJI_DETAIL:
				ZhuanjiBean zj = new ZhuanjiBean();
				JSONObject zjjob = json.optJSONObject("data");
				JSONObject album_info = zjjob.optJSONObject("album_info");
				zj.setShareUrl(zjjob.optString("share_url"));
				zj.setId(album_info.optString("id"));
				zj.setImage(album_info.optString("image"));
				zj.setTitle(album_info.optString("title"));
				zj.setDesc(album_info.optString("desc"));
				zj.setThumb(album_info.optString("thumb"));
				zj.setWap_url(album_info.optString("wap_url"));
				JSONArray zjArray = zjjob.optJSONArray("lists");
				List<ZhuanjiCate> zjcates = new ArrayList<ZhuanjiCate>();
				if (zjArray != null) {
					for (int i = 0; i < zjArray.length(); i++) {
						ZhuanjiCate cate = new ZhuanjiCate();
						JSONObject jObject = zjArray.optJSONObject(i);
						if (jObject == null)
							continue;
						JSONObject j2Object = jObject.optJSONObject("catename");
						if (j2Object == null) {
							JSONArray cateArray = jObject.optJSONArray("list");
							List<ZhuanjiCateChild> catechilds = new ArrayList<ZhuanjiCateChild>();
							if (cateArray != null) {
								for (int j = 0; j < cateArray.length(); j++) {
									ZhuanjiCateChild child = new ZhuanjiCateChild();
									JSONObject cateObject = cateArray
											.getJSONObject(j);
									child.setBanner(cateObject
											.optString("banner"));
									child.setBrand_id(cateObject
											.optString("brand_id"));
									child.setCate_id(cateObject
											.optString("cate_id"));
									child.setClick_times(cateObject
											.optString("click_times"));
									child.setContent(cateObject
											.optString("content"));
									child.setCountry_id(cateObject
											.optString("country_id"));
									child.setCreate_time(cateObject
											.optString("create_time"));
									child.setDescription(cateObject
											.optString("description"));
									child.setFlag(cateObject.optString("flag"));
									child.setId(cateObject.optString("id"));
									child.setLikenum(cateObject
											.optString("likenum"));
									child.setMarket_price(cateObject
											.optString("market_price"));
									child.setMarket_time(cateObject
											.optString("market_time"));
									child.setMaterial(cateObject
											.optString("material"));
									child.setModel(cateObject
											.optString("model"));
									child.setOrgin(cateObject
											.optString("orgin"));
									child.setPublish_date(cateObject
											.optString("publish_date"));
									child.setRecommend(cateObject
											.optString("recommend"));
									child.setRecommend_score(cateObject
											.optString("recommend_score"));
									child.setRecommend_star(cateObject
											.optString("recommend_star"));
									child.setRelation_id(cateObject
											.optString("relation_id"));
									child.setSports_id(cateObject
											.optString("sports_id"));
									child.setThumb(cateObject
											.optString("thumb"));
									child.setTitle(cateObject
											.optString("title"));
									child.setWeight(cateObject
											.optString("weight"));
									catechilds.add(child);
								}
								cate.setChilds(catechilds);
							}
							zjcates.add(cate);
						} else {
							JSONObject jsonObject = j2Object
									.optJSONObject("info");
							if (jsonObject == null) {

							} else {
								cate.setId(jsonObject.optString("id"));
								cate.setTitle(jsonObject.optString("title"));
								cate.setDesc(jsonObject
										.optString("description"));
								cate.setAlbumId(jsonObject
										.optString("album_id"));
								cate.setCreateTime(jsonObject
										.optString("create_time"));
								cate.setSortNum(jsonObject
										.optString("sort_num"));
								JSONArray cateArray1 = zjArray.getJSONObject(i)
										.getJSONObject("catename")
										.optJSONArray("list");
								List<ZhuanjiCateChild> catechilds1 = new ArrayList<ZhuanjiCateChild>();
								if (cateArray1 != null) {
									for (int j = 0; j < cateArray1.length(); j++) {
										ZhuanjiCateChild child = new ZhuanjiCateChild();
										JSONObject cateObject = cateArray1
												.getJSONObject(j);
										child.setBanner(cateObject
												.optString("banner"));
										child.setBrand_id(cateObject
												.optString("brand_id"));
										child.setCate_id(cateObject
												.optString("cate_id"));
										child.setClick_times(cateObject
												.optString("click_times"));
										child.setContent(cateObject
												.optString("content"));
										child.setCountry_id(cateObject
												.optString("country_id"));
										child.setCreate_time(cateObject
												.optString("create_time"));
										child.setDescription(cateObject
												.optString("description"));
										child.setFlag(cateObject
												.optString("flag"));
										child.setId(cateObject.optString("id"));
										child.setLikenum(cateObject
												.optString("likenum"));
										child.setMarket_price(cateObject
												.optString("market_price"));
										child.setMarket_time(cateObject
												.optString("market_time"));
										child.setMaterial(cateObject
												.optString("material"));
										child.setModel(cateObject
												.optString("model"));
										child.setOrgin(cateObject
												.optString("orgin"));
										child.setPublish_date(cateObject
												.optString("publish_date"));
										child.setRecommend(cateObject
												.optString("recommend"));
										child.setRecommend_score(cateObject
												.optString("recommend_score"));
										child.setRecommend_star(cateObject
												.optString("recommend_star"));
										child.setRelation_id(cateObject
												.optString("relation_id"));
										child.setSports_id(cateObject
												.optString("sports_id"));
										child.setThumb(cateObject
												.optString("thumb"));
										child.setTitle(cateObject
												.optString("title"));
										child.setWeight(cateObject
												.optString("weight"));
										catechilds1.add(child);
									}
									cate.setChilds(catechilds1);
								}
								zjcates.add(cate);
							}

						}

					}
					zj.setListCates(zjcates);
				}
				return zj;
			case TYPE_MAIN_COMMENTLIST:
				List<CommentBean> list_cb = new ArrayList<CommentBean>();
				JSONObject jb = json.optJSONObject("data");
				JSONArray cbArray = jb.optJSONArray("lists");
				zhuangbei_comment_total = Integer.parseInt(jb
						.optString("total"));
				if (cbArray != null) {
					for (int i = 0; i < cbArray.length(); i++) {
						JSONObject obj = cbArray.optJSONObject(i);
						CommentBean cb = new CommentBean();
						cb.setCommentId(obj.optString("commentid"));
						cb.setHeadicon(obj.optString("headicon"));
						cb.setUser_id(obj.optString("user_id"));
						cb.setFlag(obj.optString("flag"));
						cb.setIp(obj.optString("ip"));
						cb.setIs_vip(obj.optString("is_vip"));
						cb.setReplyid(obj.optString("replyid"));
						cb.setUser_id(obj.optString("user_id"));
						cb.setComment(obj.optString("comment"));
						cb.setCreatedate(obj.optString("createdate"));
						cb.setObject_id(obj.optString("object_id"));
						cb.setParentid(obj.optString("parentid"));
						cb.setVtitle(obj.optString("vtitle"));
						cb.setNickname(obj.optString("nickname"));
						list_cb.add(cb);
					}
					return list_cb;
				}
				return null;

			case TYPE_BASE_INFO:
				List<BaseInfoBean> lists = new ArrayList<BaseInfoBean>();
				JSONArray jsonListA = json.optJSONArray("data");
				if (jsonListA != null) {
					for (int i = 0; i < jsonListA.length(); i++) {
						JSONObject obj = jsonListA.optJSONObject(i);
						BaseInfoBean bb = new BaseInfoBean();
						bb.setBase_info(obj.optString("base_info"));
						bb.setFull_info(obj.optString("full_info"));
						bb.setId(Integer.valueOf(obj.optString("id")));
						bb.setImage_url(obj.optString("image_url"));
						bb.setLocation(obj.optString("location"));
						bb.setTitle(obj.optString("title"));
						lists.add(bb);
					}
					return lists;
				}
				break;
			case TYPE_ACTIVTIY:
				List<ActivityLeaderPicBean> leaderPicBeans = new ArrayList<ActivityLeaderPicBean>();
				JSONArray jsonListL = json.optJSONArray("data");
				if (jsonListL != null) {
					for (int i = 0; i < jsonListL.length(); i++) {
						JSONObject obj = jsonListL.optJSONObject(i);
						ActivityLeaderPicBean bb = new ActivityLeaderPicBean();
						bb.setHuodong_url(obj.optString("huodong_url"));
						bb.setStartDate(obj.optString("start_date"));
						bb.setId(Integer.valueOf(obj.optString("id")));
						bb.setImage_url(obj.optString("image_url"));
						bb.setEndDate(obj.optString("end_date"));
						bb.setTitle(obj.optString("title"));
						leaderPicBeans.add(bb);
					}
					return leaderPicBeans;
				}
				break;
			case TYPE_SIGNUP:
				JSONObject datajson1 = json.optJSONObject("data");
				JSONObject userjson = datajson1.optJSONObject("user");
				UserBean u1 = new UserBean();
				u1.setUser_id(userjson.optString("user_id"));
				u1.setEmail(userjson.optString("email"));
				u1.setNickname(userjson.optString("nickname"));
				u1.setHeadicon(userjson.optString("headicon"));
				u1.setSex(userjson.optString("sex"));
				u1.setPoint(userjson.optString("point"));
				setUser(u1);
				login_flag = true;
				break;
			case TYPE_SHAITU_LIST:
				List<ShaiTuBean> sblists = new ArrayList<ShaiTuBean>();
				JSONObject data1 = json.optJSONObject("data");
				JSONArray aaa1 = data1.optJSONArray("lists");
				List<String> strs = new ArrayList<String>();
				if (aaa1 != null) {
					for (int i = 0; i < aaa1.length(); i++) {
						JSONObject obj = aaa1.optJSONObject(i);
						ShaiTuBean sb = new ShaiTuBean();
						sb.setId(obj.optString("id"));
						sb.setImage_url(obj.optString("image_url"));
						sb.setNickname(obj.optString("nickname"));
						sb.setHeadicon(obj.optString("headicon"));
						sb.setUser_id(obj.optString("user_id"));
						sb.setIs_vip(obj.optString("is_vip"));
						sb.setTitle(obj.optString("title"));
						sb.setDesc(obj.optString("desc"));
						if (obj.optString("image_url") == null
								|| "".equals(obj.optString("image_url"))) {
						} else {
							sblists.add(sb);
						}

					}
				}
				return sblists;
			}

		} catch (Exception e) {
			return null;
		}
		return null;
	}
}
