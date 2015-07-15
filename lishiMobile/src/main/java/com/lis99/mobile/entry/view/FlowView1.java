package com.lis99.mobile.entry.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.lis99.mobile.application.cache.ImageCacheManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.IEventHandler;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.engine.io.IOManager;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StringUtil;

public class FlowView1 extends ImageView implements View.OnClickListener,
		View.OnLongClickListener,IEventHandler {

	private AnimationDrawable loadingAnimation;
	private FlowTag flowTag;
	private Context context;
	public Bitmap bitmap;
	// private ImageLoaderTask task;
	private int columnIndex;// 图片属于第几列
	private int rowIndex;// 图片属于第几行
	private Handler viewHandler;
	
	

	public void setContext(Context context) {
		this.context = context;
	}

	public FlowView1(Context c, AttributeSet attrs, int defStyle) {
		super(c, attrs, defStyle);
		this.context = c;
		Init();
	}

	public FlowView1(Context c, AttributeSet attrs) {
		super(c, attrs);
		this.context = c;
		Init();
	}

	public FlowView1(Context c) {
		super(c);
		this.context = c;
		Init();
	}

	private void Init() {

		setOnClickListener(this);
		this.setOnLongClickListener(this);
		setAdjustViewBounds(true);

	}

	@Override
	public boolean onLongClick(View v) {
		Log.d("FlowView", "LongClick");
		Toast.makeText(context, "长按：" + this.flowTag.getFlowId(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void onClick(View v) {
		Log.d("FlowView", "Click");
		view.onClick(null);
		//ActivityManager.getInstance().changeStateTo(ActivityManager.STATE_PIC_DETAIL, String.valueOf(this.flowTag.getFlowId()));
	}

	/**
	 * 加载图片
	 */
	public void LoadImage() {
		if (getFlowTag() != null) {
			

			//setImageBitmap(bitmap);
			setImage(flowTag.getFileName(), null, null);
			
			//new LoadImageThread().start();
		}
	}

	/**
	 * 重新加载图片
	 */
	public void Reload() {
		//if (this.bitmap == null && getFlowTag() != null) {
			setImage(flowTag.getFileName(), null, null);
			//new ReloadImageThread().start();
		//}
	}

	/**
	 * 回收内存
	 */
	public void recycle() {
		setImageBitmap(null);
		if ((this.bitmap == null) || (this.bitmap.isRecycled()))
			return;
		this.bitmap.recycle();
		this.bitmap = null;
	}

	public FlowTag getFlowTag() {
		return flowTag;
	}

	public void setFlowTag(FlowTag flowTag) {
		this.flowTag = flowTag;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Handler getViewHandler() {
		return viewHandler;
	}

	public FlowView1 setViewHandler(Handler viewHandler) {
		this.viewHandler = viewHandler;
		return this;
	}

	class ReloadImageThread extends Thread {

		@Override
		public void run() {
			if (flowTag != null) {

				/*BufferedInputStream buf;
				try {
					buf = new BufferedInputStream(flowTag.getAssetManager()
							.open(flowTag.getFileName()));
					bitmap = BitmapFactory.decodeStream(buf);

				} catch (IOException e) {

					e.printStackTrace();
				}*/

				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {// 此处在线程过多时可能为null
							setImage(flowTag.getFileName(), null, null);
							System.out.println(flowTag.getFileName());
						}
					}
				});
			}

		}
	}

	class LoadImageThread extends Thread {
		LoadImageThread() {
		}

		public void run() {

			if (flowTag != null) {

				/*BufferedInputStream buf;
				try {
					buf = new BufferedInputStream(flowTag.getAssetManager()
							.open(flowTag.getFileName()));
					bitmap = BitmapFactory.decodeStream(buf);

				} catch (IOException e) {

					e.printStackTrace();
				}*/
				// if (bitmap != null) {

				// 此处不能直接更新UI，否则会发生异常：
				// CalledFromWrongThreadException: Only the original thread that
				// created a view hierarchy can touch its views.
				// 也可以使用Handler或者Looper发送Message解决这个问题

				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {// 此处在线程过多时可能为null
							
						}
					}
				});

				// }

			}

		}
	}
	
	private static final String TAG = "AsyncLoadImageView";

	private String url;
	private Bitmap loadFailResource;
	private Bitmap loadingResource;
	private Bitmap bitm;
	public final static int IMAGE_NORMAL = 0;
	public final static int IMAGE_RCB = 1;
	private int imageType = 0;
	private float resizeWidth = 0;
	private int borderWidth = 0;
	private int borderColor = -1;
	private boolean resize_flag = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				setImageBitmap(ImageCacheManager.getInstance().getBitmapFromCache(url));
				break;
			case 1:
				setImageBitmap(bitm);
				break;
			case -1:
				if (loadFailResource != null) {
					setImageBitmap(loadFailResource);
				}
				break;
			}
		}
	};

	public void setImage(final String url, final int type, final Bitmap loadFailResource, final Bitmap loadingResource){
		imageType = type;
		setImage(url,loadFailResource,loadingResource);
	}
	
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
	
	public void setImage(final String url, final Bitmap loadFailResource, final Bitmap loadingResource) {
		this.url = url;

		// 先到缓存中取图片资源
		bitm = ImageCacheManager.getInstance().getBitmapFromCache(url);

		if (bitm != null) {
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

		// 缓存没有取到数据开启任务去去网络中取资源，并且保存到缓存和磁盘中，如果获取失败将fallbackResource设置为背景
		Task task = new Task(null, url, C.HTTP_GET, null, this);
		IOManager ioManager = IOManager.getInstance();
		ioManager.addTask(task);
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		if(bm !=null){
			int width = bm.getWidth();// 获取真实宽高
			int height = bm.getHeight();
	
			LayoutParams lp = getLayoutParams();
	
			int layoutHeight = (height * StringUtil.getXY((Activity)context)[0])
					/ width;// 调整高度
			if (lp == null) {
				lp = new LayoutParams(flowTag.getItemWidth(),
						layoutHeight);
			}
			setLayoutParams(lp);
			super.setImageBitmap(bm);
			Handler h = getViewHandler();
			Message m = h.obtainMessage(flowTag.what, width,
					layoutHeight, view);
			h.sendMessage(m);
		}else{
			super.setImageBitmap(bm);
		}
		
		
	}


	@Override
	public void handleTask(int initiator, Task task, int operation) {
		Message msg = new Message();
		msg.what = -1;
		if (task == null) {
			mHandler.sendMessage(msg);
			return;
		}
		if (task.isFailed()) {
			mHandler.sendMessage(msg);
			return;
		}
		if (task.isCanceled()) {
			mHandler.sendMessage(msg);
			return;
		}
		switch (initiator) {
		case IEvent.IO:
			Object data = task.getData();
			if (data == null) {
			} else if (data instanceof byte[] || data instanceof Bitmap) {
				msg.what = 0;
				msg.obj = data;
				mHandler.sendMessage(msg);
			} else {
			}
			break;
		default:
			break;
		}		
	}
	
	
	ZhuanjiView view;
	public void setZhuanjiView(ZhuanjiView zhuanjiView) {
		view = zhuanjiView;
	}

}
