package com.lis99.mobile.entry.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.BannerBean;
import com.lis99.mobile.entry.LsZhuanjiDetail;
import com.lis99.mobile.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SildeShowWidget extends ViewFlipper {

	private Context mContext;
	private boolean directionFlag = false;// �����־�� trueΪ��������flaseΪ��������
	private int imageCount = 0;
	private int moveFlag;// moveDown��λ��X���
	private boolean startFlag = true;// ��ʼ��ֹͣ ���

	private ArrayList<Bitmap> newsForBit = null;
	private ArrayList<Integer> newsForRes = null;
	private List<BannerBean> image_urls = null;
	private long showtime=5000;

	private Timer timer;
	private TimerTask ttask;
	public SildeShowWidget(Context context ,List<BannerBean> newsForBit) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.image_urls = newsForBit;
		imageCount = image_urls.size();
		addViewToFlipper();
	}
	


	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				showNext();
				break;
			case 101:
				showPrevious();
				break;
			}
			super.handleMessage(msg);
		}
	};

	Thread mSildeShowThread = new Thread(new Runnable() {
		public void run() {
			while (startFlag) {
				try {
					Thread.sleep(showtime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				if (directionFlag) {
					msg.what = 101;
				} else {
					msg.what = 100;
				}
				mHandler.sendMessage(msg);
			}
		}
	});
	

	
	public void addViewToFlipper() {
		for (int i = 0; i < imageCount; i++) {
			addView(addMyView(i), i);
		}
		// mViewFlipper.set
		setInAnimation(mContext, R.anim.push_left_in);
		setOutAnimation(mContext, R.anim.push_left_out);
		setPersistentDrawingCache(ViewGroup.PERSISTENT_ALL_CACHES);
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int x = (int) event.getRawX();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					moveFlag = x;
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					int m = x - moveFlag;
					if (m > 10) {
						// ��
						directionFlag = true;
						setInAnimation(mContext, R.anim.push_right_in);
						setOutAnimation(mContext, R.anim.push_right_out);
						showPrevious();
					} else if (m < -10) {
						// �һ�
						directionFlag = false;
						setInAnimation(mContext, R.anim.push_left_in);
						setOutAnimation(mContext, R.anim.push_left_out);
						showNext();
					}

					break;
				}
				return true;
			}
		});
	}

	public View addMyView(final int index) {

		RelativeLayout r = (RelativeLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.silde_new_layout, null);
		AsyncLoadImageView imageView = (AsyncLoadImageView) r.findViewById(R.id.imageView1);
		RelativeLayout.LayoutParams imageView_params = (RelativeLayout.LayoutParams) imageView
		.getLayoutParams();
		imageView_params.width = StringUtil.getXY((Activity)mContext)[0]-2*StringUtil.dip2px(mContext, 10);
		imageView_params.height = (int)(((float)(StringUtil.getXY((Activity)mContext)[0]-StringUtil.dip2px(mContext, 10)))*3/7);
		imageView.setLayoutParams(imageView_params);
		/*if (newsForBit==null && newsForRes==null) {

		} else {
			if (newsForBit!=null) {
				imageView.setImageBitmap(newsForBit.get(index));
			} else {
				Drawable draw;
				draw=getResources().getDrawable(newsForRes.get(index));
				imageView.setBackgroundDrawable(draw);
			}
		}*/
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,LsZhuanjiDetail.class);
				intent.putExtra("id", image_urls.get(index).getId());
				mContext.startActivity(intent);
			}
		});
		if(image_urls!=null){
			imageView.setImage(image_urls.get(index).getImageUrl(), null, null);
		}

		return r;
	}

	/**
	 * ����ͼƬ��,��bitmap����
	 * 
	 * @param newsPhoto
	 */
	public void setNewsPhotoForBitmap(ArrayList<Bitmap> newsForBit) {
		this.newsForBit = newsForBit;
		imageCount = newsForBit.size();
	}


	/**
	 * ��������ͼƬ������res��Դ��
	 * 
	 * @param resource
	 *            ͼƬres id
	 */
	public void setNewPhotoForResourceId(ArrayList<Integer> newsForRes) {
		this.newsForRes = newsForRes;
		imageCount = newsForRes.size();
	}

	/**
	 * ���ö���Ч��
	 * 
	 * @param inAnimation
	 *            ���붯��
	 * @param outAnimation
	 *            ��ȥ����
	 */
	public void setAnimation(Animation inAnimation, Animation outAnimation) {
		setInAnimation(inAnimation);
		setOutAnimation(outAnimation);
	}

	/**
	 * ���ò��ż��ʱ��
	 */
	public void setShowTime(long time){
		this.showtime=time;
	}
	
	/**
	 * ��ʼ����
	 */
	public void startSildeShow() {
//		mSildeShowThread.start();
		timer=new Timer();
		 ttask=new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message msg = new Message();
					if (directionFlag) {
						msg.what = 101;
					} else {
						msg.what = 100;
					}
					mHandler.sendMessage(msg);
				}
			};
		timer.schedule(ttask, 1000, 3000);
		
	}

	/**
	 * ֹͣ����
	 */
	public void stopSildeShow() {
		//startFlag = false;
		if(timer!=null)
			timer.cancel();
		if(ttask!=null)
			ttask.cancel();
		timer=null;
		ttask=null;
	}

	/**
	 * �����
	 */
	public void restartSildeShow() {
		startFlag = true;
	}
	
	

	class NewsEntityForBit {
		Bitmap photo;
		String title;
	}

	class NewsEntityForRes {
		int resid;
		String title;
	}

}
