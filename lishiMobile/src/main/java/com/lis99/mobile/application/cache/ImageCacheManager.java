package com.lis99.mobile.application.cache;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import com.lis99.mobile.util.C;
import com.lis99.mobile.util.L;
import com.lis99.mobile.util.MD5Code;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/*******************************************
 * @ClassName: ImageCacheManager 
 * @Description: 图片缓存管理器 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午3:05:39 
 *  
 ******************************************/
public class ImageCacheManager {

	private static final String TAG = "ImageCacheManager";

	/** ImageCacheManager实例 */
	private static ImageCacheManager mSingleton = null;

	private ConcurrentHashMap<String, SoftReference<Bitmap>> softBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

	/**
	 * 构造方法
	 */
	private ImageCacheManager() {
	}

	/**
	 * 初始化图片缓存管理器
	 */
	synchronized public static boolean init() {
		boolean result = true;
		if (mSingleton != null) {
			return true;
		}
		try {
			// 创建图片缓存管理器
			mSingleton = new ImageCacheManager();
		} catch (Exception e) {
			result = false;
		}

		return result;
	}

	/**
	 * 多线程安全: 获取ImageCacheManager句柄
	 */
	synchronized public static final ImageCacheManager getInstance() {
		if (mSingleton == null) {
			if (!init()) {
				if (L.ERROR)
					L.e(TAG, "Failed to init itself");
			}
		}
		return mSingleton;
	}

	/*
	 * public void addBitmapToCache(String url,Bitmap bitmap) {
	 * hardBitmapCache.put(url, bitmap); }
	 */

	public void addBitmapToCache(String url, byte[] array) {
		boolean success = addBitmapToSDCard(url, array);
		if(!success)
	        Log.w(TAG, "缓存sdk失败"); 
		if(!softBitmapCache.contains(url)){
		    softBitmapCache.put(url, new SoftReference<Bitmap>(getBitmapFromSDCard(url)));
		}
		 //addBitmapToSDCard(url, bt);
		// bt.recycle();
	}

	/*private void addBitmapToSDCard(String url, Bitmap bt) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			File file = new File(C.CACHE_IMAGE_PATH + MD5Code.encode(url) + ".jpg");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream outStream;
			try {
				outStream = new FileOutputStream(file);
				outStream.write(BitmapUtil.bitampToByteArray(bt));
				outStream.close();
			} catch (Exception e) {
			}
		}
	}*/

	/**
	 * 检测缓存是否存在图片资源
	 * 
	 * @param url
	 * @return
	 */
	public boolean check(String url) {
		if (softBitmapCache.containsKey(url))
			return true;
		return false;
	}

	/**
	 * 将一个图片资源从缓存中移除（主要是一些使用频率低且容量大的图片资源）
	 * 
	 * @param url
	 * @return void
	 */
	synchronized public void removeBitmapFromCache(String url) {
		if (softBitmapCache.containsKey(url)) {
			softBitmapCache.remove(url);
		}
	}

	synchronized public void removeAllBitmapCache() {
		if (softBitmapCache == null) {
			return;
		}
		softBitmapCache.clear();
	}

	/**
	 * 从缓存中取图片资源
	 * 
	 * @param url
	 * @return BitMap
	 */
	public Bitmap getBitmapFromCache(Object param) {
		Bitmap bitmap = null;
		if (param == null || !(param instanceof String)) {
			return bitmap;
		}
		String url = (String) param;
		synchronized (softBitmapCache) {
			SoftReference<Bitmap> bitmapReference = softBitmapCache.get(url);
			if (bitmapReference != null) {
				bitmap = bitmapReference.get();
				if (bitmap == null) {
					softBitmapCache.remove(url);
				}
			} else {
				// 去sd卡取图片
				bitmap = getBitmapFromSDCard(url);
				if (bitmap != null) {
					softBitmapCache.put(url, new SoftReference(bitmap));
				}
			}
		}
		return bitmap;
	}
	/**
	 * 将图片压缩后加载到内存 图片占用内存较大，真正图片相关项目当中图片都是被压缩过的
	 * 
	 * @param path
	 *            原图路径
	 * @param newWidth
	 *            缩放新宽度
	 * @return
	 */
	public static Bitmap scalePic(String path, int newWidth) {
		Bitmap resizedBitmap = null;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap bmp = BitmapFactory.decodeFile(path, bfo);// 此时返回bmp为空
		bfo.inJustDecodeBounds = false;
		int scalePer = 1;
		try {
			// 根据文件大小计算缩放比例
			File file = new File(path);
			long size = file.length();
			while (true) {
				if (size > (20 * 1024)) {
					size /= 2;
					scalePer += 1;
					continue;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			resizedBitmap = null;
		}
		// 设置给bfo一个缩放的比例
		bfo.inSampleSize = scalePer;
		bmp = BitmapFactory.decodeFile(path, bfo);

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		float temp = ((float) height) / ((float) width);
		// 计算缩放的新高度
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,
				true);
		if (bmp != null) {
			bmp.recycle();
		}
		return resizedBitmap;
	}

	/**
	 * 从SD卡中取图片资源
	 * 
	 * @param url
	 * @return BitMap
	 */
	public static Bitmap getBitmapFromSDCard(String url) {
		Bitmap bitmap = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			try{
			File file = new File(C.CACHE_IMAGE_PATH + MD5Code.encode(url));
			String path = C.CACHE_IMAGE_PATH + MD5Code.encode(url);
			if (file.exists()) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				// 获取这个图片的宽和高
				bitmap = BitmapFactory.decodeFile(path, options);// 此时返回bm为空
				int widthRatio = (int) Math.ceil(options.outWidth / (float) 400);
				int heightRatio = (int) Math.ceil(options.outHeight / (float) 400);

				if (widthRatio > 1 && heightRatio > 1) {
					if (widthRatio > heightRatio) {
						// 压缩到原来的(1/widthRatios)
						options.inSampleSize = widthRatio;
					} else {
						options.inSampleSize = heightRatio;
					}
				}
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(path, options);
				/*BitmapFactory.Options bfo = new BitmapFactory.Options();
				bfo.inJustDecodeBounds = true;
				// 获取这个图片的宽和高
				//Bitmap bmp = BitmapFactory.decodeFile(path, bfo);// 此时返回bmp为空
				bfo.inJustDecodeBounds = false;
				int scalePer = 1;
				try {
					// 根据文件大小计算缩放比例
					File file1 = new File(path);
					long size = file1.length();
					while (true) {
						if (size > (20 * 1024)) {
							size /= 2;
							scalePer += 1;
							continue;
						} else {
							break;
						}
					}
				} catch (Exception e) {
					//resizedBitmap = null;
				}
				// 设置给bfo一个缩放的比例
				bfo.inSampleSize = scalePer;
				bitmap = BitmapFactory.decodeFile(path, bfo);*/
				//bitmap = scalePic(path,400);
				
			}
		} catch (Exception e) {
				} catch (OutOfMemoryError errory) {
					System.gc(); 
				}
		}
		return bitmap;
	}

	/**
	 * 将图片加入sd卡中
	 * 
	 * @param url
	 * @param array
	 */
	private static boolean addBitmapToSDCard(String url, byte[] array) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			try{
			Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
			File file = new File(C.CACHE_IMAGE_PATH + MD5Code.encode(url));		
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream outStream=null;
			try {
				outStream = new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, 50, outStream);
				//outStream.write(array);
			} catch (Exception e) {
				return false;
			}finally{				
					try {
						if(outStream!=null)
						    outStream.close();
					} catch (IOException e) {
					}
			}
			} catch (Exception e) {
			} catch (OutOfMemoryError errory) {
				System.gc(); 
			}
			return true;
		}
		return false;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
}