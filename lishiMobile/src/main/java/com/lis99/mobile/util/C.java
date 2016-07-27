package com.lis99.mobile.util;

import android.os.Environment;

import com.lis99.mobile.entry.application.DemoApplication;

/*******************************************
 * @ClassName: C 
 * @Description: 常量类 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 上午11:23:19 
 *  
 ******************************************/
public class C {
//	测试地址开关
	public static boolean TESTHOST = false;

	/** 客户端版本 */
	public static String VERSION = DemoApplication.getInstance().versionName;
	
	/** 支持的协议类型 */
	public static final String HTTP = "http://";
	public static final String SOCKET = "socket://";
	public static final String RESOURCE = "res://";
	
	/** 网络请求方式 */
	public static final String HTTP_GET = "GET";
	public static final String HTTP_POST = "POST";
	
	/** 上传文件用常量 */
	public static final String BOUNDARY = "*****";
	public static final String PREFIX = "--";
	public static final String LINEND = "\r\n";
	public static final String MULTIPART_FROM_DATA = "multipart/form-data";
	public static final String CHARSET = "UTF-8";
	
	/** 操作返回结果 */
	public static final int OK = 0;
	public static final int INVALID = -3;
	public static final int FAIL = -1;
	public static final int CANCEL = -2;
	
	public static final String CONFIG_FILENAME = "config";
	public static final String TENCENT_OPEN_ID = "tencent_openid";
	public static final String TENCENT_ACCESS_TOKEN = "tencent_access_token";
	public static final String TENCENT_EXPIRES_IN = "tencent_expires_in";
	
	/** 用来标识请求照相功能的activity */
	public static final int CAMERA_WITH_DATA = 3023;
	/** 用来标识请求gallery的activity */
	public static final int PHOTO_PICKED_WITH_DATA = 3021;
	/** 用来标识裁剪的activity */
	public static final int PICKED_WITH_DATA = 3020;
	/** 头像存放目录 */
	public static final String HEAD_IMAGE_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/gameplatform/head/";
	/** 头像文件后缀 */
	public static final String JPG = "jpg";
	public static final String PNG = "png";
	
	public static final String MODULE_TYPE_ZHUANGBEI = "zhuangbei";
	public static final String MODULE_TYPE_ARITCLE = "aritcle";
	public static final String MODULE_TYPE_WENDA = "wenda";
	public static final String MODULE_TYPE_HUIDA = "huida";
	public static final String MODULE_TYPE_HUODONG = "huodong";
	
	public static final String TYPE_SHARE_GUANZHU = "guanzhu";
	public static final String TYPE_SHARE_TIWEN = "tiwen";
	public static final String TYPE_SHARE_LIKE = "like";
	public static final String TYPE_SHARE_TENCENT = "tencent";
	public static final String TYPE_SHARE_QQ = "qq";
	public static final String TYPE_SHARE_SINA = "sina";
	
	public static final String TYPE_NOTICE_SYS = "sys";
	public static final String TYPE_NOTICE_TIWEN = "tiwen";
	public static final String TYPE_NOTICE_PINGLUN = "pinglun";
	
	
	public static final String TENCENT_APP_ID = "100390791";
	public static final String TENCENT_APP_KEY = "92956fe533a07e4754c44cb3bf24b507";
	
	public static final String TENCENTWEIBO_APP_KEY = "801302591";
	public static final String TENCENTWEIBO_APP_SERCET = "1511da6cd40afbf7bb6fb6a03a116896";
	
//	public static final String WEIXIN_APP_ID = "wxd20a2898e337e6db";//
//	public static final String WEIXIN_APP_KEY = "e8a85b901a0a36905cc057d56bbcf94b";//
	
//	AppID：wx95ad7d760771c3f4			wx7076726e66e9ad2c(公众平台)		wx7076726e66e9ad2c
	public static final String WEIXIN_APP_ID = "wx95ad7d760771c3f4";//
	public static final String WEIXIN_APP_KEY = "f607ce67e7f1e7d9e52865d6b49729af";//
	
	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static final String SINA_APP_KEY      = "4130270425";
    public static final String SINA_APP_SERCET      = "45956933e5d6c88a6f254918ee707da2";
    public static final String ACCOUNT      = "account";
    public static final String PASSWORD      = "password";
    public static final String TOKEN      = "token";
    public static final String SINA_CODE      = "SINA_CODE";
    public static final String TOKEN_ACCOUNT     = "tokenaccount";
    public static final String TOKEN_PASSWORD     = "tokenpassword";

    /** 
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * 
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SINA_SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
	
	/** 网络下载图片的缓存目录 */
	public static final String CACHE_IMAGE_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/lis99/cache/";

	public static final String ACCOUNT_PHONE      = "account_phone";


	public static final String WEIXIN_CODE      = "weixin_code";
	public static final String WEIXIN_TOKEN      = "weixin_token";
	public static final String WEIXIN_OPENID      = "weixin_openid";


	/** 服务器地址 */
	private static String DOMAIN;// = "http://api.lis99.com";
//	HTTPS 地址
	private static String DOMAINS;// = "https://apis.lis99.com";

	public static String getDOMAIN() {
		return DOMAIN;
	}

	public static void setDOMAIN(String DOMAIN) {
		C.DOMAIN = DOMAIN;
	}

	public static String getDOMAINS() {
		return DOMAINS;
	}

	public static void setDOMAINS(String DOMAINS) {
		C.DOMAINS = DOMAINS;
	}

	static {
		Common.log("===HOST====");
		Common.setHost();
	}

	/** 首屏-焦点图 */
	public static final String MAIN_BANNER_URL = DOMAIN + "/main/banner/";
	/** 装备-热门装备 */
	public static final String ZHUANGBEI_ITEM_URL = DOMAIN + "/zhuangbei/items/";
	/** 装备-热门专辑 */
	public static final String ZHUANGBEI_ALBUMS_URL = DOMAIN + "/zhuangbei/albums/";
	/** 装备-专辑详情 */
	public static final String ZHUANGBEI_ALBUM_DETAIL_URL = DOMAIN + "/zhuangbei/album_detail/";
	/** 装备-装备详情 */
	public static final String ZHUANGBEI_DETAIL_URL = DOMAIN + "/zhuangbei/detail/";
	/** 问答-最新 */
	public static final String WENDA_LATEST_URL = DOMAIN + "/wenda/latest/";
	/** 问答-最热 */
	public static final String WENDA_HOTEST_URL = DOMAIN + "/wenda/hotest/";
	/** 问答-达人答疑 */
	public static final String WENDA_DARENDAYI_URL = DOMAIN + "/wenda/darendayi/";
	/** 问答-分类列表 */
	public static final String WENDA_CATES_URL = DOMAIN + "/wenda/cates/";
	/** 问答-分类列表 */
	public static final String WENDA_CATES_LIST_URL = DOMAIN + "/wenda/lists/";
	/** 问答-发布问题 */
	public static final String WENDA_ASK_URL = DOMAIN + "/wenda/ask/";
	/** 问答-回答的图片上传 */
	public static final String WENDA_UPLOAD_URL = DOMAIN + "/wenda/upload/";
	/** 问答-问题详情 */
	public static final String WENDA_QUESTION_URL = DOMAIN + "/wenda/question/";
	/** 问答-答案详情 */
	public static final String WENDA_ANSWER_URL = DOMAIN + "/wenda/answer/";
	/** 问答-回答问题 */
	public static final String WENDA_REPLY_URL = DOMAIN + "/wenda/reply/";
	/** 发表评论 */
	public static final String MAIN_FEEDBACK_URL = DOMAIN + "/main/feedback/";
	/** 发表评论 */
	public static final String MAIN_COMMENT_URL = DOMAIN + "/main/comment/";
	/** 支持/喜欢/关注 */
	public static final String MAIN_ADDLIKE_URL = DOMAIN + "/main/addLike/";
	public static final String MAIN_CHECKVERSION_URL = DOMAIN + "/main/checkVersionForAndroid/";
	
	/** 晒图-列表 */
	public static final String SHAITU_LISTS_URL = DOMAIN + "/shaitu/lists/";
	/** 晒图-创建 */
	public static final String SHAITU_CREATE_URL = DOMAIN + "/shaitu/create/";
	/** 晒图-传图 */
	public static final String SHAITU_UPLOAD_URL = DOMAIN + "/shaitu/upload/";
	/** 晒图-详情 */
	public static final String SHAITU_DETAIL_URL = DOMAIN + "/shaitu/detail/";
	/** 用户-注册 */
	public static final String USER_SIGNUP_URL = DOMAIN + "/user/signup/";
	/** 用户-登录 */
	public static final String USER_SIGNIN_URL = DOMAIN + "/user/signin/";
	/** 用户-修改资料 */
	public static final String USER_SAVEPROFILE_URL = DOMAIN + "/user/saveProfile/";
	public static final String USER_SAVE_NICKNAME_URL = DOMAIN + "/v2/user/updNickname";
	/** 用户-修改头像 */
	public static final String USER_SAVEPHOTO_URL = DOMAIN + "/user/savePhoto/";
	/** 用户-所在地 */
	public static final String USER_GETCITYS_URL = DOMAIN + "/user/getCitys/";
	/** 用户-喜欢的装备 */
	public static final String USER_ZHUANGBEI_URL = DOMAIN + "/user/zhuangbei/";
	/** 用户-喜欢的装备 */
	public static final String USER_SHAITU_URL = DOMAIN + "/user/shaitu/";
	/** 用户-喜欢的装备 */
	public static final String USER_QUESTION_URL = DOMAIN + "/user/question/";
	public static final String USER_ANSWER_URL = DOMAIN + "/user/answer/";
	public static final String USER_INFO_URL = DOMAIN + "/user/info/";
	
	public static final String USER_NOTICE_TIPS_URL = DOMAIN + "/v3/user/noticetips/";
	public static final String EQUIP_CONTENTS = DOMAIN + "/v3/zhuangbei";

	/** 用户-问答提醒 */
	public static final String USER_COMMENTS_URL = DOMAIN + "/user/comments/";
	/** 用户-评论/回复提醒 */
	public static final String USER_NOTICE_URL = DOMAIN + "/user/notice/";
	/** 用户-评论/回复提醒 */
	public static final String MAIN_COMMENTLIST_URL = DOMAIN + "/main/commentlist/";
	/** 达人-店长列表 */
	public static final String DAREN_DIANZHANG_URL = DOMAIN + "/daren/dianzhang/";
	/** 达人-详情 */
	public static final String DAREN_DETAIL_URL = DOMAIN + "/daren/detail/";
	/** 达人-回答 */
	public static final String DAREN_HUIDA_URL = DOMAIN + "/daren/huida/";
	/** 达人-列表 */
	public static final String DAREN_LISTS_URL = DOMAIN + "/daren/lists/";
	/** 达人-晒图 */
	public static final String DAREN_SHAITU_URL = DOMAIN + "/daren/shaitu/";
	public static final String USER_DAREN_URL = DOMAIN + "/user/daren/";
	public static final String USER_WENDA_NOTICE_URL = DOMAIN + "/user/wendanotice/";
	public static final String MAIN_FRIEND_URL = DOMAIN + "/main/friend/";
	public static final String USER_THIRDSIGN_URL = DOMAIN + "/user/thirdsign/";
	public static final String USER_BIND_URL = DOMAIN + "/user/bindthird/";
	
	
//	public static final String SELECT_CONTENT = DOMAIN + "/content/getContentInfo/";
	public static final String SELECT_CONTENT = DOMAIN + "/v2/content/getContentInfo/";
	public static final String SKIINGPARK_LIST = DOMAIN + "/xuechang/getListInfo/";
//	public static final String QUICK_ENTER = DOMAIN + "/main/getSpeedAccessInfo/";
	public static final String QUICK_ENTER = DOMAIN + "/v2/common/getClientbanner";
	
	public static final String CLUB_HOMEPAGE = DOMAIN + "/club/clubpage/";
	public static final String CLUB_JOIN = DOMAIN + "/club/joinClub/";
	public static final String CLUB_QUIT = DOMAIN + "/club/quitClub/";
	public static final String CLUB_EVENT_APPLY = DOMAIN + "/club/addClubActivityApply/";
	public static final String CLUB_EVENT_APPLYLIST = DOMAIN + "/club/clubActivityApplyList/";
//	public static final String CLUB_ADD_TOPIC = DOMAIN + "/club/clubActivityTopic/";
	public static final String CLUB_ADD_TOPIC = DOMAIN + "/v4/club/clubActivityTopic";
//	新版话题， 回复http://api.lis99.com/v5/topics/topicsReply
	public static final String CLUB_ADD_TOPIC_REPLY = DOMAIN + "/v5/topics/topicsReply/";
	public static final String CLUB_GET_INFO = DOMAIN + "/club/getclubinfo/";
	public static final String CLUB_TOPIC_GET_INFO = DOMAIN + "/club/getTopicInfo/";
	public static final String CLUB_TOPIC_GET_INFO2 = DOMAIN + "/club/getClubTopicInfo/";
	public static final String CLUB_GET_ONE_INFO = DOMAIN + "/club/getOneClubInfo/";
	public static final String CLUB_DEL = DOMAIN + "/club/delClub/";
	public static final String CLUB_TOPTOPIC = DOMAIN + "/club/topTopic/";
	public static final String CLUB_DELTOPIC = DOMAIN + "/club/delTopic/";
	public static final String CLUB_GET_LIST = DOMAIN + "/club/getClubList/";
	public static final String CLUB_SEL_LIST = DOMAIN + "/club/selClubInfo/";
	public static final String CLUB_GET_AREA = DOMAIN + "/club/getAreaListInfo/";
	public static final String CLUB_ALL_LIST = DOMAIN + "/v5/club/allclublist/";
	
	public static final String LS_MINE_APPLY_INFO = DOMAIN + "/v2/user/applynotice/";
	public static final String LS_MINE_APPLY_MANAGE = DOMAIN + "/v2/user/myTopic/";
	public static final String LS_MINE_APPLY_INFO_CLEAR = DOMAIN + "/v2/user/clearnotice/";
//	人个主页
//	public static final String LS_USER_HOME_PAGE  =      DOMAIN + "/v3/user/uinfo/";
public static final String LS_USER_HOME_PAGE  =      DOMAIN + "/v5/user/uinfo/";
	public static final String LS_USER_ADD_FOLLOW  =     DOMAIN + "/v3/user/addFollow/";
	public static final String LS_USER_CANCEL_FOLLOW =      DOMAIN +  "/v3/user/cancelFollow/";

	
	
	public static final String COLLECTION_GET_LIST = DOMAIN + "/shop/shopDynamicInfo/";
//	我发的帖   /v5/user/myPubTopics/[page]
//	public static final String USER_GET_MYTOPICS = DOMAIN + "/user/getMyTopicList/";
public static final String USER_GET_MYTOPICS = DOMAIN + "/v5/user/myPubTopics/";

	/**
	 * 活动 - 领队
	 */
	public static final String ACTIVITY_LEADER_URL = DOMAIN + "/activity/bannerList/";
	public static final String ACTIVITY_XXL_URL = DOMAIN + "/activity/getBaseInfo/";
	public static final String ACTIVITY_ITEM_URL = DOMAIN + "/activity/getAreaActivity";
	public static final String ACTIVITY_RESULT_URL = DOMAIN + "/activity/getActivityFinishResult";
	public static final String ACTIVITY_PHASE_URL = DOMAIN + "/activity/getActivityResultList/";
	public static final String ACTIVITY_LINE_URL = DOMAIN + "/activity/getActivityInfo/";
	public static final String ACTIVITY_LINGDUI_URL = DOMAIN + "/user/infoForLingdui/";
	public static final String ACTIVITY_LDLINE_URL = DOMAIN + "/activity/getXianlu/";
	public static final String ACTIVITY_LAJI_COMMENTLIST_URL = DOMAIN + "/activity/addComment/";
	public static final String ACTIVITY_USER_COMMENTLIST_URL = DOMAIN + "/activity/getCommentList/";
	public static final String ACTIVITY_SHAITU_URL = DOMAIN + "/activity/getAllShaitu/";
	public static final String ACTIVITY_ZAN_URL = DOMAIN + "/activity/addLike/";
	public static final String ACTIVITY_SHAITUQIAN_URL = DOMAIN + "/activity/addZhuanji/";
	public static final String ACTIVITY_SHAITUS_URL = DOMAIN + "/activity/addImage/";
	public static final String ACTIVITY_ZANS_URL = DOMAIN + "/activity/getLikeStatus/";
	public static final String ACTIVITY_ZANSS_URL = DOMAIN + "/activity/delLike/";
	
	
	public static final String ZHUANGBEI_CATE = DOMAIN + "/zhuangbei/fenlei/";
	
	//=======3.0===============
	/**俱乐部排行榜*/
	public static final String CLUB_LEVEL = DOMAIN + "/v2/common/rank";
	/**俱乐部详情*/
	public static final String CLUB_DETAIL_HEAD = DOMAIN + "/v4/club/detail/";
	/**俱乐部详情列表 http://api.lis99.com/v2/club/topiclist/170/0?page=0 */
//	public static final String CLUB_DETAIL_LIST = DOMAIN + "/v4/club/topiclist/";
//	public static final String CLUB_DETAIL_LIST = DOMAIN + "/v5/club/topiclist/";
	public static final String CLUB_DETAIL_LIST = DOMAIN + "/v5/club/topiclists/";
	/**帖子详情（HEAD）*/
	public static final String CLUB_TOPIC_DETAIL_HEAD = DOMAIN + "/v2/club/topic/";
//＝＝＝＝帖子详情＝＝＝＝＝＝
//	public static final String CLUB_TOPIC_DETAIL_HEAD_3 = DOMAIN + "/v3/club/topic/";
	public static final String CLUB_TOPIC_DETAIL_HEAD_3 = DOMAIN + "/v3/club/topics/";
	/**帖子详情回复列表*/
	public static final String CLUB_T0PIC_DETAIL_REPLY = DOMAIN + "/v2/club/replylist/";
	/**帖子详情其他信息*/
	public static final String CLUBPTOPIC_DETAIL_INFO_OTHER = DOMAIN + "/v2/club/topicother/";
	/**注册*/
	public static final String LS_REGIST = DOMAIN + "/v2/user/signup"; 
	/**登陆*/
	public static final String LS_SIGNIN = DOMAIN + "/v2/user/signin";
	/**点赞*/
//	public static final String CLUBTIPIC_LIKE = DOMAIN + "/v2/club/addLike/";
	/**获取报名填写选项*/
	public static final String CLUB_TOPIC_APPLY_LIST = DOMAIN + "/v2/apply/applyEnroll/";
	/**获取报名填写选项
	 *		http://api.lis99.com/v5/club/applyEnroll/[topic_id]
	 * */
	public static final String CLUB_TOPIC_APPLY_LIST_NEW = DOMAIN + "/v5/club/applyEnroll/";

	/**报名信息上传*/
	public static final String CLUB_TOPIC_APPLY_INFO = DOMAIN + "/v2/apply/enroll/";
	/**报名列表管理 http://api.lis99.com/v2/apply/enrollList/[topicid]/[offset]*/
	public static final String CLUB_TOPIC_MANAGER_APPLY_LIST = DOMAIN + "/v2/apply/enrollList/";
	/**报名通过*/
	public static final String CLUB_TOPIC_MANAGER_APPLY_PASS = DOMAIN + "/v2/apply/pass";
	/**报名拒绝*/
	public static final String CLUB_TOPIC_MANAGER_APPLY_REJECT = DOMAIN + "/v2/apply/refuse";
	/**置顶*/
	public static final String CLUB_TOPIC_INFO_TOP = DOMAIN + "/v2/club/topTopic"; 
	/**取消置顶*/
	public static final String CLUB_TOPIC_INFO_CANCELTOP = DOMAIN + "/v2/club/cancelTopic";
	/**删除帖子*/
	public static final String CLUB_TOPIC_REPLY_DELETE = DOMAIN + "/v2/club/delTopic";
	/**赞 http://api.lis99.com/v2/club/addLike/*/
	public static final String CLUB_TOPIC_INFO_LIKE = DOMAIN + "/v2/club/addLike/";
	
	/**回复我的*/
	public static final String MINE_REPLY_LIST = DOMAIN + "/v5/user/replyme/";
	/**上传设备信息*/
	public static final String UPDATA_DEVICE_INFO = DOMAIN + "/v2/user/appsInfo/";
	/**退出登陆，调用*/
	public static final String LOGOUT = DOMAIN + "/v2/user/exitappsinfo";
	
	//====3.1======
	/**精选*/
	public static final String CHOICENESS = DOMAIN + "/v2/club/omnibusList/";
	public static final String BRAND_SHOP = DOMAIN + "/v3/zhuangbei/brandshop/";
	public static final String ADD_COMMENT = DOMAIN + "/v3/zhuangbei/addComment/";
	/**精选-专题*/
	public static final String CHOIECNESS_SUBJECT = DOMAIN + "/v2/club/topicSpecialList/";
	/**专题 V2*/
	public static final String CHOIECNESS_SUBJECT12 = DOMAIN + "/v4/club/topicSpecialList/";
	
	//========装备＝＝＝＝
	//户外店
	public static final String EQUIPTYPE = DOMAIN + "/v2/zhuangbei/quickAccess";
	//热门装备
	public static final String EQUIPRECOMMEND = DOMAIN + "/v2/zhuangbei/zhuangbeiCategory";
	//附近户外店
	public static final String EQUIPNEARBY = DOMAIN + "/v2/zhuangbei/nearShop/";
	//装备评测
	public static final String EQUIPAPPRAISE = DOMAIN + "/v2/zhuangbei/profile";

	//===3.2====
	//搜索主接口
//	public static final String SEARCH_MAIN = DOMAIN + "/v2/search/page/";
	public static final String SEARCH_MAIN = DOMAIN + "/v5/search/page/";
//搜索俱乐部
	public static final String SEARCH_CLUB = DOMAIN + "/v5/search/club/";
//搜索话题， 活动
	public static final String SEARCH_TOPIC = DOMAIN + "/v5/search/topics/";




	public static final String GET_AUTHCODE = DOMAIN + "/v2/user/sendCode/";
	public static final String PHONE_LOGIN = DOMAIN + "/v2/user/loginMobile/";
	public static final String PHONE_REGISTER = DOMAIN + "/v2/user/regMobile/";
	public static final String WEIXIN_LOGIN = DOMAIN + "/v2/user/weixinapi/";

//QQ登录
	public static final String QQLOGINURL = DOMAIN + "/v2/user/qqapi";
//	SINA登录
	public static final String SINALOGINURL = DOMAIN + "/v2/user/weiboapi";
	//=====3.4=======
//	全部精选
	public static final String CHOICENESSALL = DOMAIN + "/v3/club/zhuanti/";
/**线路活动
 * http://api.lis99.com/v5/club/alllists
 * */
//	public static final String ACTIVE_ALL = DOMAIN + "/v3/club/clubtopics/";
	public static final String ACTIVE_ALL = DOMAIN + "/v5/club/alllists/";
	/**线路活动， 城市选择*/
	public static final String ACTIVE_ALL_CITY = DOMAIN + "/v3/club/getAreaListInfo";

	//=====3.5======
	/**俱乐部首页*/
	public static final String CLUB_MAIN_INFO = DOMAIN + "/v3/club/index";
/**砾石专栏*/
	public static final String CLBU_MIAN_SPECIAL = DOMAIN + "/v3/club/taglist/";
//		/v5/club/tagTopic/[page]
	public static final String CLBU_SPECIAL_LIST_INFO = DOMAIN + "/v5/club/tagTopic/";

	public static final String CLUB_HOT_TOPIC = DOMAIN + "/v3/club/hottopics/";

	//=======3.6.0============
//	动态
	public static final String DYNAMIC_LIST = DOMAIN + "/v3/user/dynamic/";
//关注， 推荐
	public static final String MYFRIENDS_RECOMMED = DOMAIN + "/v3/user/recommendUser/";
//关注我的粉丝
	public static final String MYFRIENDS_FANS = DOMAIN + "/v3/user/myFans/";
//关注
	public static final String MYFRIENDS_ATTENTION = DOMAIN + "/v3/user/myFollow/";
//取消关注
	public static final String CANCEL_ATTENTION = DOMAIN + "/v3/user/cancelFollow";
//添加关注
	public static final String ADD_ATTENTION = DOMAIN + "/v3/user/addFollow";
//  系统消息
	public static final String SYS_MASSAGE_LIST = DOMAIN + "/v3/user/stationMessage/";
//清空系统消息
	public static final String SYS_MASSAGE_CLEANALL = DOMAIN + "/v3/user/clearNotice";
//	新的点赞
	public static final String CLUB_TOPIC_LIKE = DOMAIN + "/v3/topics/addLike/";
//	新新的点赞
	public static final String CLUB_TOPIC_LIKE_NEW = DOMAIN + "/v5/topics/addLike/";
//  收到的赞
	public static final String SYS_LIKE_LIST = DOMAIN + "/v5/user/likeTopic/";
//  装备详情
	public static final String EQUIP_INFO_LIST = DOMAIN + "/v3/zhuangbei/detail/";
//  装备评论列表
	public static final String EQUIP_REPLY_LIST = DOMAIN + "/v3/zhuangbei/comment/";
//  新版活动贴
	public static final String CLUB_TOPIC_NEW_ACTIVE = DOMAIN + "/v3/club/lineactivedetail";
//	精选广告
	public static final String CHOICENESS_AD_BANNER = DOMAIN + "/v2/advertise/getOmnibusAdvertise";
///v4/club/omnibusList/[page]/[user_id] 新的精选
	public static final String CHOICENESS_NEW_LIST = DOMAIN + "/v4/club/omnibusList/";
///v4/user/myJoinClub/[page]			加入的俱乐部
	public static final String MY_JOIN_CLUB_LIST = DOMAIN + "/v4/user/myJoinClub/";

	public static final String MY_ACTIVITY_LIST = DOMAIN + "/v4/user/myApplyActivities/";
	public static final String MY_ACTIVITY_WAITCOMMENT_LIST = DOMAIN + "/v5/user/waitComment/";
	public static final String MY_ACTIVITY_WAITPAY_LIST = DOMAIN + "/v5/user/waitPay/";
	public static final String MY_ORDER_DETAIL = DOMAIN + "/v4/apply/orderDetail/";
	public static final String MY_ACTIVITY_ADD_COMMENT = DOMAIN + "/v5/topics/leaderComment/";

//	http://api.lis99.com/v4/club/recommendClubList  推荐俱乐部
	public static final String RECOMMEND_CLUB_LIST = DOMAIN + "/v4/club/recommendClubList";
//	获取订单信息， 支付方式等
	public static final String GET_ORDER_INFO = DOMAIN + "/v4/apply/applyInfoDetail";
//	//	获取订单信息， 支付方式等新版  http://api.lis99.com/v5/club/applyInfoDetail
//	public static final String GET_ORDER_INFO_NEW = DOMAIN + "/v5/club/applyInfoDetail";
//	提交报名订单
	public static final String SUBMIT_ORDER_INFO = DOMAIN + "/v4/apply/enroll";
//	提交报名订单 （新版线下活动帖） http://api.lis99.com/v5/club/enroll
	public static final String SUBMIT_ORDER_INFO_NEW = DOMAIN + "/v5/club/enroll/";
//	管理报名		http://api.lis99.com/v4/apply/enrollList/[topicid]/[page]
	public static final String MANAGER_JON_ACTIVE_LIST = DOMAIN + "/v4/apply/enrollList/";
//	管理报名		http://api.lis99.com/v5/club/enrollList/[topicid]/[page]
	public static final String MANAGER_JON_ACTIVE_LIST_NEW = DOMAIN + "/v5/club/enrollList/";
//	http://api.lis99.com/v4/apply/pass
	public static final String MANAGER_APPLY_PASS = DOMAIN + "/v5/club/pass";
//http://api.lis99.com/v4/apply/refuse
	public static final String MANAGER_APPLY_REFUSE = DOMAIN + "/v5/club/refuse";
//	我报名列表详情			http://api.lis99.com/v4/apply/orderDetail
	public static final String MY_APPLY_DETAIL = DOMAIN + "/v4/apply/orderDetail";
//	我报名的活动列表		http://api.lis99.com/v4/user/myApplyActivities/[page]
	public static final String MY_APPLY_LIST = DOMAIN + "/v4/user/myApplyActivities/";
//	http://api.lis99.com/v5/club/activity   新版活动贴
	public static final String CLUB_TOPIC_ACTIVE_LINE_MIAN = DOMAIN + "/v5/club/activitys";
//	新版线路活动    http://api.lis99.com/v5/club/lists
//	public static final String NEW_ACTIVE_LINE_MIAN = DOMAIN + "/v5/club/lists/";
	public static final String NEW_ACTIVE_LINE_MIAN = DOMAIN + "/v5/club/providActivity/";

//	新版话题贴		http://api.lis99.com/v5/topics/detail/[page]
	public static final String CLUB_NEW_TOPIC_LIST_MAIN = DOMAINS + "/topics/detail/";
//	新版话题回复列表 http://api.lis99.com/v5/topics/replylists/[page]
	public static final String CLUB_NEW_TOPIC_LIST_MAIN_REPLY = DOMAIN + "/v5/topics/replylists/";

	public static final String LINE_CATE_INFO = DOMAIN + "/v5/club/cateinfo/";
	public static final String LINE_CATE_PAGE_HOME = DOMAIN + "/v5/club/pageHome/";
//	新版图文混排发帖		http://api.lis99.com/v5/topics/pubTopics
	public static final String REPLY_NEW_TOPIC_STRING_IMAGE = DOMAIN + "/v5/topics/pubTopics";
//	新版图文混排追加内容		地址：http://api.lis99.com/v5/topics/appendTopics
	public static final String REPLY_NEW_TOPIC_STRING_IMAGE_ADD = DOMAIN + "/v5/topics/appendTopics";
//public static final String REPLY_NEW_TOPIC_STRING_IMAGE = DOMAIN + "/v5/topics/upfiles";

//	我的福利列表 http://api.lis99.com/v5/user/welfareList/[page]
	public static final String MY_BENEFIT_LIST = DOMAIN + "/v5/user/welfareList/";
//	领取积分			http://api.lis99.com/v5/user/getWelfare
	public static final String My_BENEFIT_INTEGRAL = DOMAIN + "/v5/user/getWelfare";
//	福利详情M页				http://m.lis99.com/club/welfare/welfareDetail/[id]/[welfare_id]/[user_id]
	public static final String MY_BENEFIT_INFO_M = "http://m.lis99.com/club/welfare/welfareDetail/";







//	==============HTTPS============
	//	https://apis.lis99.com/activity/detail/新版系列活动贴
	public static final String CLUB_TOPIC_ACTIVE_SERIES_LINE_MIAN = DOMAINS + "/activity/detail/";
//https://apis.lis99.com/activity/batchs/	系列活动报名列表
	public static final String CLUB_TOPIC_ACTIVE_SERIES_LINE_LIST = DOMAINS + "/activity/batchs/";
//	https://apis.lis99.com/activity/applybatchs/
	public static final String MANAGER_SERIES_LINE_JOIN_LIST = DOMAINS + "/activity/applybatchs/";
//	https://apis.lis99.com/activity/enrollList/[page]
	public static final String MANAGER_SERIES_LINE_JOIN = DOMAINS + "/activity/enrollList/";
//	https://apis.lis99.com/activity/enroll	添加活动报名信息接口
	public static final String ADD_ACTIVE_SERIES_LINE = DOMAINS+"/activity/enroll";
//	https://apis.lis99.com/activity/applyEnroll/2631	获取报名填写信息
	public static final String ACTIVE_SERIES_APPLY_LIST = DOMAINS + "/activity/applyEnroll/";
//	https://apis.lis99.com/activity/refuse/		系列活动拒绝
	public static final String ACTIVE_SERIES_APPLY_REFUSE = DOMAINS + "/activity/refuse/";
//	地址：https://apis.lis99.com/activity/pass/		系列活动通过
	public static final String ACTIVE_SERIES_APPLY_PASS = DOMAINS + "/activity/pass/";

	//	获取订单信息， 支付方式等新版  http://api.lis99.com/v5/club/applyInfoDetail
	public static final String GET_ORDER_INFO_NEW = DOMAINS + "/Activity/applyInfoDetail";
//	社区首页			https://apis.lis99.com/main/fpage
	public static final String COMMUNITY_AD_MAIN = DOMAINS + "/main/fpage";
//	社区列表			https://apis.lis99.com/main/omnibusList/[page]/[user_id]
	public static final String COMMUNITY_LIST_MAIN = DOMAINS + "/main/omnibusList/";
//	全部专栏			https://apis.lis99.com/columns/index/[page]
	public static final String COMMUNITY_SPECIAL_MAIN = DOMAINS + "/columns/index/";
//	专栏详情			https://apis.lis99.com/columns/detail/[page]
	public static final String COMMUNITY_SPECIAL_INFO_MAIN = DOMAINS + "/columns/detail/";
//	社区明星详情		https://apis.lis99.com/main/startlist/0
	public static final String COMMUNITY_STAR_INFO_LIST = DOMAINS + "/main/startlist/";
//	活动首页头部信息 	https://apis.lis99.com/activity/fpage
	public static final String ACTIVE_NEW_MAIN_HEAD = DOMAINS + "/activity/fpage";
	//目的地1级列表 	https://apis.lis99.com/destination/oneLevelDestination
	public static final String DESTINATION_LIST_ONE = DOMAINS + "/destination/oneLevelDestination";
//	目的地2级列表 	https://apis.lis99.com/destination/secondLevelDestination
	public static final String DESTINATION_LIST_TWO = DOMAINS + "/destination/secondLevelDestination";
//	附近的活动主列表    https://apis.lis99.com/main/nearby
	public static final String NEARBY_MAIN_LIST = DOMAINS + "/main/nearby/";
//	筛选列表			https://apis.lis99.com/main/filterinfo/[type]
	public static final String NEARBY_FILTER = DOMAINS + "/main/filterinfo/";
//	筛选主列表 			https://apis.lis99.com/main/activityfilter
	public static final String NEARBY_MAIN_LIST_FILTER = DOMAINS + "/main/activityfilter/";

}



