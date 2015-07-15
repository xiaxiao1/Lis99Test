package com.lis99.mobile.entry.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.lis99.mobile.application.cache.ImageCacheManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.IEventHandler;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.engine.io.IOManager;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.L;
import com.lis99.mobile.util.StringUtil;

/*******************************************
 * @ClassName: AsyncLoadImageView 
 * @Description: 自定义ImageView控件,实现图片异步加载 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2014-1-3 下午4:27:16 
 *  
 ******************************************/
public class AsyncLoadImageView extends ImageView implements IEventHandler {

	private static final String TAG = "AsyncLoadImageView";

	protected Context context;
	private String url;
	private Bitmap loadFailResource;
	private Bitmap loadingResource;
	private Bitmap bitmap;
	public final static int IMAGE_NORMAL = 0;
	public final static int IMAGE_RCB = 1;
	private int imageType = 0;
	private int rcb_num = 10;
	private float resizeWidth = 0;
	private int borderWidth = 0;
	private int borderColor = -1;
	private boolean resize_flag = false;

	public int getRcb_num() {
		return rcb_num;
	}

	public void setRcb_num(int rcb_num) {
		this.rcb_num = rcb_num;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				L.i(TAG, "图片加载成功,来自网络");
				//setBackground(new BitmapDrawable(ImageCacheManager.getInstance().getBitmapFromCache(url)));
				setImageBitmap(ImageCacheManager.getInstance().getBitmapFromCache(url));
				//listener.getImageSuccess(ImageCacheManager.getInstance().getBitmapFromCache(url));
				break;
			case 1:
				L.i(TAG, "图片加载成功,来自缓存");
				//setBackground(new BitmapDrawable(bitmap));
				setImageBitmap(bitmap);
				
				//listener.getImageSuccess(bitmap);
				break;
			case -1:
				L.i(TAG, "图片加载失败");
				if (loadFailResource != null) {
					setImageBitmap(loadFailResource);
					//listener.getImageSuccess(loadFailResource);
				}
				break;
			}
		}
	};

	public AsyncLoadImageView(Context context) {
		super(context);
		this.context = context;
	}

	public AsyncLoadImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public AsyncLoadImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void setImage(final String url, final int type, final Bitmap loadFailResource, final Bitmap loadingResource){
		imageType = type;
		setImage(url,loadFailResource,loadingResource);
	}
	public void setImage(final String url,final Bitmap loadFailResource, final Bitmap loadingResource, String string) {
		pageType = string;
		setImage(url,loadFailResource,loadingResource);
	}
	
	String pageType;
	
	public void setImage(final String url, final int type,final int width, final Bitmap loadFailResource, final Bitmap loadingResource){
		resizeWidth = width;
		imageType = type;
		setImage(url,loadFailResource,loadingResource);
	}
	public void setImage(final String url, final boolean flag,final int width, final Bitmap loadFailResource, final Bitmap loadingResource){
		resizeWidth = width;
		resize_flag = flag;
		setImage(url,loadFailResource,loadingResource);
	}
	public void setBorder(int borderWidth,int borderColor){
		this.borderWidth = borderWidth;
		this.borderColor = borderColor;
	}
	OnGetImageListener listener;
	public void setImage(final String url, final Bitmap loadFailResource, final Bitmap loadingResource) {
		this.url = url;

		// 先到缓存中取图片资源
		bitmap = ImageCacheManager.getInstance().getBitmapFromCache(url);

		if (bitmap != null) {
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			return;
		}
		this.loadFailResource = loadFailResource;
		this.loadingResource = loadingResource;

		// 设置正在加载图片时显示的图片资源
		if (loadingResource != null) {
			setImageBitmap(loadingResource);
		}
       // SystemClock.sleep(3000);
		// 缓存没有取到数据开启任务去去网络中取资源，并且保存到缓存和磁盘中，如果获取失败将fallbackResource设置为背景
		Task task = new Task(null, url, C.HTTP_GET, null, this);
		IOManager ioManager = IOManager.getInstance();
		ioManager.addTask(task);
	}
	public interface OnGetImageListener{
		void getImageSuccess(Bitmap b);
	}
	public void setImage(final String url, final Bitmap loadFailResource, final Bitmap loadingResource,boolean l) {
		this.url = url;
		this.height_flag = l;
		// 先到缓存中取图片资源
		bitmap = ImageCacheManager.getInstance().getBitmapFromCache(url);

		if (bitmap != null) {
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			return;
		}
		this.loadFailResource = loadFailResource;
		this.loadingResource = loadingResource;

		// 设置正在加载图片时显示的图片资源
		if (loadingResource != null) {
			setImageBitmap(loadingResource);
		}
       // SystemClock.sleep(3000);
		// 缓存没有取到数据开启任务去去网络中取资源，并且保存到缓存和磁盘中，如果获取失败将fallbackResource设置为背景
		Task task = new Task(null, url, C.HTTP_GET, null, this);
		IOManager ioManager = IOManager.getInstance();
		ioManager.addTask(task);
	}
	
	
	public void setBackground(Drawable background) {
		super.setBackgroundDrawable(background);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if(bm !=null){
			//设置图片边框
			if(borderWidth != 0){
				if(resizeWidth!=0){
					bm = BitmapUtil.resizeBitmap(bm, resizeWidth, ((float)bm.getHeight())/((float)bm.getWidth())*resizeWidth);
				}
				bm = BitmapUtil.toBorder(bm, borderWidth, borderColor);
			}
			//设置图片圆角
			if (bm != null && imageType == IMAGE_RCB) {
				if(resizeWidth!=0){
					bm = BitmapUtil.resizeBitmap(bm, resizeWidth, ((float)bm.getHeight())/((float)bm.getWidth())*resizeWidth);
				}
				bm = BitmapUtil.getRCB(bm, rcb_num);
			}
			if(resize_flag){
				if(resizeWidth!=0){
					bm = BitmapUtil.resizeBitmap(bm, resizeWidth, ((float)bm.getHeight())/((float)bm.getWidth())*resizeWidth);
				}
			}
			if(height_flag){
				LinearLayout.LayoutParams ll = (LayoutParams) AsyncLoadImageView.this.getLayoutParams();
				ll.width = StringUtil.getXY((Activity)context)[0] / 2 - StringUtil.dip2px(context, 20);
				ll.height = ll.width * bm.getHeight() / bm.getWidth();
				AsyncLoadImageView.this.setLayoutParams(ll);
			}
			if(pageType!=null){
				if("zhuangbei_detail".equals(pageType)){
					bm = BitmapUtil.getRCB(bm, bm.getWidth() / 2);
				}
			}
		}
		super.setImageBitmap(bm);
	}
	boolean height_flag = false;

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		Message msg = new Message();
		msg.what = -1;
		if (task == null) {
			L.i(TAG, "任务不存在");
			mHandler.sendMessage(msg);
			return;
		}
		if (task.isFailed()) {
			L.i(TAG, "任务失败了");
			mHandler.sendMessage(msg);
			return;
		}
		if (task.isCanceled()) {
			L.i(TAG, "任务被取消了");
			mHandler.sendMessage(msg);
			return;
		}
		switch (initiator) {
		case IEvent.IO:
			L.i(TAG, "来自IO模块的任务,可取出数据结果去解析");
			Object data = task.getData();
			if (data == null) {
				L.i(TAG, "未获取数据");
			} else if (data instanceof byte[] || data instanceof Bitmap) {
				msg.what = 0;
				msg.obj = data;
				mHandler.sendMessage(msg);
				L.i(TAG, "数据结果--" + ((byte[]) data).length);
			} else {
				L.i(TAG, "未知数据结果--" + data);
			}
			break;
		default:
			break;
		}
	}

	
}