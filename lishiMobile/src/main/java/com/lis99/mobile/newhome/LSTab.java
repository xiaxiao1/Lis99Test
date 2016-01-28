package com.lis99.mobile.newhome;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.ScrollTopUtil;

/**
 * tab 切换
 * 
 * @author zhangjie
 * 
 */
public class LSTab extends FrameLayout implements OnClickListener {

	private final static int TabCount = 5;
	private int mTabWidth;
	public static int sTabHeight;
	private View mTabView;

//	private View selectView;
//	private ImageView selectImg;

	private View equiView;
	private ImageView equiImg;

	private View shopView;
	private ImageView shopImg;

//	private View saleView;
//	private ImageView saleImg;

	private View eventView;
	private ImageView eventImg;

	private ImageView mImageview;
//	private List<ImageView> mImageViews;
	
	private RelativeLayout choiceness;
	private ImageView iv_choiceness;
	private TextView tv_choiceness;

	private Handler mHandler;

	public static final int SELECT = 0;
	public static final int EQUI = 1;
	public static final int CLUB = 2;
	public static final int CHOICENESS = 3;
	public static final int EVENT = 4;

	public int mTabCur = SELECT; // 当前选项
	private int mTabOld = 0;
	private TextView tv_mine, tv_club, tv_equip;
	
	private ImageView tab_reddot;
	
	public LSTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTabWidth = context.getResources().getDisplayMetrics().widthPixels
				/ TabCount;
		sTabHeight = (int) context.getResources().getDimension(
				R.dimen.bottom_bar_height);

		mImageview = new ImageView(context);
		FrameLayout.LayoutParams lp = new LayoutParams(mTabWidth, sTabHeight);
		// mImageview.setBackgroundResource(R.color.Home_tab);
		mImageview.setLayoutParams(lp);
		addView(mImageview);

		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mTabView = li.inflate(R.layout.home_bottom_bar, this);
		initView();

	}

	private void initView() {
//		selectView = mTabView.findViewById(R.id.home);
//		selectImg = (ImageView) mTabView.findViewById(R.id.homeimg);

		equiView = mTabView.findViewById(R.id.dynamic);
		equiImg = (ImageView) mTabView.findViewById(R.id.dynamicimg);

		shopView = mTabView.findViewById(R.id.takephoto);
		shopImg = (ImageView) mTabView.findViewById(R.id.takephotoimg);

//		saleView = mTabView.findViewById(R.id.myboard);
//		saleImg = (ImageView) mTabView.findViewById(R.id.myboardimg);

		eventView = mTabView.findViewById(R.id.setting);
		eventImg = (ImageView) mTabView.findViewById(R.id.settingimg);
		
		tv_equip = (TextView) mTabView.findViewById(R.id.tv_equip);
		tv_club = (TextView) mTabView.findViewById(R.id.tv_club);
		tv_mine = (TextView) mTabView.findViewById(R.id.tv_mine);
		
		choiceness = (RelativeLayout) mTabView.findViewById(R.id.choiceness);
		iv_choiceness = (ImageView) mTabView.findViewById(R.id.iv_choiceness);
		tv_choiceness = (TextView) mTabView.findViewById(R.id.tv_choiceness);

//		selectView.setOnClickListener(this);
		equiView.setOnClickListener(this);
		shopView.setOnClickListener(this);
//		saleView.setOnClickListener(this);
		eventView.setOnClickListener(this);
		choiceness.setOnClickListener(this);
		
//		mImageViews = new ArrayList<ImageView>();
////		mImageViews.add(selectImg);
//		mImageViews.add(equiImg);
//		mImageViews.add(eventImg);
////		mImageViews.add(saleImg);
//		mImageViews.add(shopImg);
//		mImageViews.add(iv_choiceness);
		
		tab_reddot = (ImageView) findViewById(R.id.tab_reddot);
		
	}
	//红点
	public void visibleRedDot ( boolean visible)
	{
		if ( visible )
		{
			tab_reddot.setVisibility(View.VISIBLE);
		}
		else
		{
			tab_reddot.setVisibility(View.GONE);
		}
	}
	
	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

	public void startAnimation(int taget) {
		Animation animation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, mTabCur * mTabWidth,
				TranslateAnimation.ABSOLUTE, taget * mTabWidth,
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, 0);
		animation.setDuration(200);
		animation.setFillAfter(true);
		mImageview.startAnimation(animation);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		sTabHeight = MeasureSpec.getSize(heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private void sendMessage(int what) {
		if (mHandler != null) {
			mHandler.sendEmptyMessage(what);
		}
	}

	public void selectTab(int tab) {
		switchDrawable(mTabCur, tab);
		mTabCur = mTabOld;
		// startAnimation(tab);
	}

	public void backOldSelect() {
		switchDrawable(mTabCur, mTabOld);
		mTabCur = mTabOld;
		// startAnimation(mTabOld);
	}

	public void switchDrawable(int oldcur, int newcur) {
		if (oldcur == newcur) {
			//第第次点击相同的， 则执行滚动到顶部
			ScrollTopUtil.getInstance().scrollToTop();
			return;
		}

		switch (newcur) {
//		case SELECT:
//			selectImg.setImageResource(R.drawable.icon_tab_select_selected);
//			setSelectBackground(selectView);
//			break;
//		case SALE:
//			saleImg.setImageResource(R.drawable.icon_tab_sale_selected);
//			setSelectBackground(shopView);
//			break;
		case SELECT:
			equiImg.setImageResource(R.drawable.icon_tab_equi_selected);
			tv_equip.setTextColor(getResources().getColor(R.color.text_color_green));
			break;
		case EVENT:
			eventImg.setImageResource(R.drawable.tab_icon_mine_press);
			tv_mine.setTextColor(getResources().getColor(R.color.text_color_green));
			break;
		case CLUB:
			shopImg.setImageResource(R.drawable.tab_icon_club_press);
			tv_club.setTextColor(getResources().getColor(R.color.text_color_green));
			break;
		case CHOICENESS:
			iv_choiceness.setImageResource(R.drawable.icon_tab_select_selected);
			tv_choiceness.setTextColor(getResources().getColor(R.color.text_color_green));
			break;
		default:
			break;
		}
		switch (oldcur) {
//		case SELECT:
//			selectImg.setImageResource(R.drawable.icon_tab_select);
//			setUnSelectBackground(selectView);
//			break;
//		case SALE:
//			saleImg.setImageResource(R.drawable.icon_tab_sale);
//			setUnSelectBackground(shopView);
//			break;
		case SELECT:
			equiImg.setImageResource(R.drawable.icon_tab_equi);
			tv_equip.setTextColor(getResources().getColor(R.color.color_tab_unselect));
			break;
		case EVENT:
			eventImg.setImageResource(R.drawable.tab_icon_mine);
			tv_mine.setTextColor(getResources().getColor(R.color.color_tab_unselect));
			break;
		case CLUB:
			shopImg.setImageResource(R.drawable.tab_icon_club);
			tv_club.setTextColor(getResources().getColor(R.color.color_tab_unselect));
			break;
		case CHOICENESS:
			iv_choiceness.setImageResource(R.drawable.icon_tab_select);
			tv_choiceness.setTextColor(getResources().getColor(R.color.color_tab_unselect));
			break;
		default:
			break;
		}
	}

	private void setSelectBackground(View aView) {
		// setViewBackground(aView, R.drawable.bar_select);
	}

	private void setUnSelectBackground(View aView) {
		// setViewBackground(aView,R.drawable.bar_default);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dynamic:
			onTabClick(SELECT);
			break;
		case R.id.setting:
			onTabClick(EVENT);
			break;
		case R.id.takephoto:
			onTabClick(CLUB);
			break;
		case R.id.choiceness:
			onTabClick(CHOICENESS);
			break;
		default:
			break;
		}
	}

	public void onTabClick(int tag) {
		// startAnimation(tag);
		switchDrawable(mTabCur, tag);
		mTabOld = mTabCur;
		mTabCur = tag;
		sendMessage(tag);
	}

	public void onTabChange(int tag) {
		// startAnimation(tag);
		switchDrawable(mTabCur, tag);
		mTabOld = mTabCur;
		mTabCur = tag;
	}

	public int getCurrentTab() {
		return mTabCur;
	}
}
