package com.lis99.mobile.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 捕获异常,完善逻辑 2012年4月17日进行修改
 * 
 * @author zhaoyabin@cy2009.com
 */
public class ImageUtil
{
	public static final String TAG = "ImageUtil";
	// 头像保存路径
	public static final String SDCARD_CACHE_IMG_HEADPATH = Environment
			.getExternalStorageDirectory().getPath() + "/tata/head/";
	// 用户保存路径
	public static final String SDCARD_CACHE_IMG_USERSAVES = Environment
			.getExternalStorageDirectory().getPath() + "/tata/usersaves/";
	// 表情路径
	public static final String SDCARD_CACHE_IMG_EXPRESSION = Environment
			.getExternalStorageDirectory().getPath() + "/tata/expression/";
	// 照相保存路径
	public static final String SDCARD_CACHE_IMG_PHOTO = Environment
			.getExternalStorageDirectory().getPath() + "/tata/photo/";
	// 默认路径
	public static final String SDCARD_CACHE_IMG_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/tata/default/";
	// 撰写日记页的图片保存路径
	public static String SDCARD_CACHE_IMG_NOTE_TEMP = null;

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels)
	{
		if (bitmap == null)
			return null;
		if (pixels <= 0)
			return bitmap;
		Bitmap ouput = null;
		try
		{
			ouput = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas(ouput);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (Exception e)
		{
			ouput = bitmap;
		} catch (OutOfMemoryError errory)
		{
			ouput = bitmap;
		}
		return ouput;
	}

	/**
	 * 图片加边框
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param borderWidth
	 *            边框尺寸
	 * @param borderColor
	 *            边框颜色
	 * @return 返回修改后的图片
	 */
	public static Bitmap toBorder(Bitmap bmpOriginal, int borderWidth,
			int borderColor)
	{
		if (bmpOriginal == null)
			return null;
		if (borderWidth < 0)
			return bmpOriginal;
		Bitmap ouput = null;
		try
		{
			ouput = Bitmap.createBitmap(bmpOriginal.getWidth(),
					bmpOriginal.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(ouput);
			// 画边框
			Rect rec = canvas.getClipBounds();
			rec.bottom--;
			rec.right--;
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			// 设置边框颜色
			paint.setColor(borderColor);
			paint.setStyle(Paint.Style.STROKE);
			// 设置边框宽度
			paint.setStrokeWidth(borderWidth);
			canvas.drawBitmap(bmpOriginal, 0, 0, paint);
			canvas.drawRect(rec, paint);
		} catch (Exception e)
		{
			ouput = bmpOriginal;
		} catch (OutOfMemoryError e)
		{
			ouput = bmpOriginal;
		}

		return ouput;
	}

	/**
	 * 按给定宽高等比例缩放
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight)
	{
		if (bm == null)
			return null;
		if (newWidth <= 0 || newHeight <= 0)
			return bm;
		Bitmap newbm = null;

		try
		{

			// 获得图片的宽高
			int width = bm.getWidth();
			int height = bm.getHeight();
			if (width <= newWidth)
			{
				return bm;
			}
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		} catch (Exception e)
		{
			newbm = bm;
		} catch (OutOfMemoryError e)
		{
			newbm = bm;
		}
		return newbm;
	}

	/**
	 * 按给定宽等比例缩放
	 * 
	 * @param bm
	 * @param newWidth
	 * @return
	 */
	public static Bitmap zoomImgWith(Bitmap bm, int newWidth)
	{
		if (bm == null)
			return null;
		if (newWidth <= 0)
			return bm;
		Bitmap newbm = null;
		try
		{
			// 获得图片的宽高
			int width = bm.getWidth();
			int height = bm.getHeight();
			if (width <= newWidth)
			{
				return bm;
			}
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleWidth);
			// 得到新的图片
			newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		} catch (Exception e)
		{
			newbm = bm;
		} catch (OutOfMemoryError e)
		{
			newbm = bm;
		}
		return newbm;
	}

	/**
	 * 根据指定的宽和高进行缩放
	 * 
	 * @param bitmap
	 *            原图
	 * @param w
	 *            待缩放的宽度
	 * @param h
	 *            待缩放的高度
	 * @return 返回缩放后的图像或是原图
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, float w, float h)
	{
		if (bitmap == null)
			return null;
		Bitmap output = null;
		Matrix matrix = null;
		try
		{
			// 图像宽和高,不符合条件时返回原图
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if (width <= 0 || height <= 0)
				return bitmap;

			float scaleWidth = w / width;
			float scaleHeight = h / height;
			matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			output = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
		} catch (Exception e)
		{
			output = bitmap;
		} catch (OutOfMemoryError errory)
		{
			output = bitmap;
		} finally
		{
			matrix = null;
		}
		return output;
	}

	/**
	 * 根据指定的宽和高进行缩放
	 * 
	 * @param bitmap
	 *            原图
	 * @param w
	 *            待缩放的宽度
	 * @param h
	 *            待缩放的高度
	 * @return 返回缩放后的图像或是原图
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h)
	{
		return resizeBitmap(bitmap, (float) w, (float) h);
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
	public static Bitmap scalePic(String path, int newWidth)
	{
		Bitmap resizedBitmap = null;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap bmp = BitmapFactory.decodeFile(path, bfo);// 此时返回bmp为空
		bfo.inJustDecodeBounds = false;
		int scalePer = 1;
		try
		{
			// 根据文件大小计算缩放比例
			File file = new File(path);
			long size = file.length();
			while (true)
			{
				if (size > (20 * 1024))
				{
					size /= 2;
					scalePer += 1;
					continue;
				} else
				{
					break;
				}
			}
		} catch (Exception e)
		{
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
		if (bmp != null)
		{
			bmp.recycle();
		}
		return resizedBitmap;
	}

	// TODO 暂时 未用到此方法
	// 将图片压缩后加载到内存 图片占用内存较大，真正图片相关项目当中图片都是被压缩过的
	public static Bitmap scalePic(String path, int widthToScale,
			int heightToScale)
	{
		int height = heightToScale;
		int width = widthToScale;

		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = false;
		int scalePer = 1;
		try
		{
			// 根据文件大小计算缩放比例
			File file = new File(path);
			long size = file.length();
			while (true)
			{
				if (size > (1.0 * 1024 * 1024))
				{
					size /= 2;
					scalePer += 1;
					continue;
				} else
				{
					break;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// 设置给bfo一个缩放的比例
		bfo.inSampleSize = scalePer;

		Bitmap bmp = BitmapFactory.decodeFile(path, bfo);

		int photoWidth = bmp.getWidth();
		int photoHeight = bmp.getHeight();
		// System.out.println(photoWidth + " h " + photoHeight);
		float scaleW = 0f;
		float scaleH = 0f;

		scaleW = ((float) width) / photoWidth;
		scaleH = ((float) height) / photoHeight;

		if ((photoWidth > width) || (photoHeight > height))
		{

			if (scaleW < scaleH)
			{
				scaleW = scaleH;
			} else
			{
				scaleH = scaleW;
			}
		}

		if ((photoWidth < width) && (photoHeight < height))
		{
			if (scaleW > scaleH)
			{
				scaleH = scaleW;
			} else
			{
				scaleW = scaleH;
			}
		}

		Matrix scaleM = new Matrix();
		scaleM.postScale(scaleW, scaleH);

		Bitmap scaledBmp = Bitmap.createBitmap(bmp, 0, 0, photoWidth,
				photoHeight, scaleM, true);
		bmp.recycle();
		return scaledBmp;
	}

	/**
	 * 根据指定的宽进行缩放
	 * 
	 * @param bitmap
	 *            原图
	 * @param newWidth
	 *            待缩放的宽度
	 * @return 返回压缩并缩放后的图像
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth)
	{
		if (bitmap == null || newWidth <= 0)
		{
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// 原始图的高宽比
		float temp = ((float) height) / ((float) width);

		int newHeight = (int) ((newWidth) * temp);

		float scaleWidth = 0f;
		float scaleHeight = 0f;

		if (newWidth > width)
		{
			scaleWidth = newWidth;
			scaleHeight = newHeight;
		} else
		{
			scaleWidth = ((float) newWidth) / width;
			scaleHeight = ((float) newHeight) / height;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		if (bitmap != null)
		{
			bitmap.recycle();
		}
		return resizedBitmap;
	}

	/**
	 * 将指定图片存储到指定路径中
	 * 
	 * @param savePath
	 *            待存储路径
	 * @param bit
	 *            源图数据
	 */
	public static void savePic(String savePath, Bitmap bit, int quality)
	{
		if (savePath == null)
			return;
		File file1 = new File(savePath);

		if (!file1.getParentFile().exists())
			file1.getParentFile().mkdirs();

		FileOutputStream bitmapWriter = null;
		try
		{
			bitmapWriter = new FileOutputStream(file1);
			if (bit.compress(Bitmap.CompressFormat.JPEG, quality, bitmapWriter))
			{
				bitmapWriter.flush();
			}
		} catch (Exception e)
		{
		} catch (OutOfMemoryError error)
		{
		} finally
		{
			try
			{
				if (bitmapWriter != null)
				{
					bitmapWriter.close();
					bitmapWriter = null;
				}
			} catch (Exception e)
			{
			}
		}
	}

	/**
	 * 保存图片到SD卡
	 * 
	 * @param imagePath
	 *            待存储路径
	 * @param buffer
	 *            图像数据
	 */
	public static void saveImage(String imagePath, byte[] buffer)
	{
		saveImage(imagePath, buffer, false);
	}

	/**
	 * 保存图片到SD卡是否强制保存图片
	 * 
	 * @param imagePath
	 *            待存储路径
	 * @param buffer
	 *            图像数据
	 */
	public static boolean saveImage(String imagePath, byte[] buffer,
			boolean isDelet)
	{
		if (imagePath == null || buffer == null)
			return false;
		// imagePath = imagePath;
		FileOutputStream fos = null;
		File file2 = null;
		File f = null;
		boolean isSaveSuccess = false;
		try
		{
			String filePath = imagePath
					.substring(0, imagePath.lastIndexOf("/"));
			file2 = new File(filePath);

			if (!file2.exists())
			{
				file2.mkdirs();
			}
			f = new File(filePath
					+ imagePath.substring(imagePath.lastIndexOf("/"))
							.replaceAll("\\*", "x").replaceAll("\\?", "x"));
			if (f.exists() && !isDelet)
			{
				return false;
			}
			if (!f.createNewFile() && !isDelet)
			{
				return false;
			} else
			{
				fos = new FileOutputStream(f);
				fos.write(buffer);
				fos.flush();
				isSaveSuccess = true;
			}
		} catch (Exception e)
		{
		} catch (OutOfMemoryError error)
		{
		} finally
		{
			try
			{
				if (fos != null)
				{
					fos.close();
					fos = null;
				}
			} catch (Exception e)
			{
			}
		}
		return isSaveSuccess;
	}

	/**
	 * 从SD卡加载图片
	 * 
	 * @param imagePath
	 *            相对路径
	 * @return
	 */
	public static Bitmap getImageFromLocal(String imagePath)
	{
		if (imagePath == null)
			return null;
		Bitmap bitmap = null;
		try
		{
			File file = new File(imagePath);
			if (file.exists())
			{
				bitmap = BitmapFactory.decodeFile(imagePath);
				file.setLastModified(System.currentTimeMillis());
			}
		} catch (Exception e)
		{
		} catch (OutOfMemoryError error)
		{
		}
		return bitmap;
	}

	/**
	 * 从指定url直接创建图片,若益处,调用GC,并二次创建,再失败就忽略
	 * 
	 * @param imageUrl
	 * @return
	 */
	static public Drawable loadImageFromUrl(String imageUrl)
	{
		// 异常保护
		if (imageUrl == null)
			return null;

		InputStream is = null;
		Drawable drawable = null;
		URL imgUrl = null;
		try
		{
			if (imgUrl == null)
			{
				imgUrl = new URL(imageUrl);
			}
			if (is == null)
			{
				is = imgUrl.openStream();
			}
			if (is != null)
			{
				drawable = Drawable.createFromStream(is, "src");
			}
		} catch (Exception e)
		{
		} catch (OutOfMemoryError error)
		{
			System.gc();
			try
			{
				if (imgUrl == null)
				{
					imgUrl = new URL(imageUrl);
				}
				if (is == null)
				{
					is = imgUrl.openStream();
				}
				if (is != null)
				{
					drawable = Drawable.createFromStream(is, "src");
				}
			} catch (Exception e)
			{
			} catch (OutOfMemoryError error2)
			{
			}
		} finally
		{
			try
			{
				if (is != null)
				{
					is.close();
					is = null;
				}
			} catch (Exception e)
			{
			}
		}
		return drawable;
	}

	/**
	 * 通过URL获取图片Drawable对象 避免数据获取异常问题
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static Drawable loadDrawableFromUrl(String imageUrl)
	{
		if (imageUrl == null || "".equals(imageUrl))
			return null;
		HttpURLConnection conn = null;
		Bitmap bitmap = null;
		InputStream in = null;
		ByteArrayOutputStream bos = null;
		URL url = null;
		byte[] buffer = null;
		byte[] dataImage = null;
		Drawable drawable = null;
		try
		{
			url = new URL(imageUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			conn.connect();
			in = conn.getInputStream();
			buffer = new byte[1024];
			int len = 0;
			bos = new ByteArrayOutputStream();
			while ((len = in.read(buffer)) != -1)
			{
				bos.write(buffer, 0, len);
			}
			dataImage = bos.toByteArray();
			if (dataImage != null && dataImage.length > 0)
				bitmap = BitmapFactory.decodeByteArray(dataImage, 0,
						dataImage.length);
			if (bitmap != null)
				drawable = new BitmapDrawable(bitmap);

		} catch (Exception e)
		{
		} catch (OutOfMemoryError error2)
		{

		} finally
		{
			try
			{
				if (bos != null)
					bos.close();
				if (in != null)
					in.close();
				if (conn != null)
					conn.disconnect();
			} catch (Exception e)
			{
			}

		}
		return drawable;

	}

	/**
	 * 保存数据到指SD卡中,以指定的名字
	 * 
	 * @param bmp
	 *            源图
	 * @param imagename
	 *            图片名称
	 */
	public static void saveBitmapToSDCard(Bitmap bmp, String imagename)
	{

		if (bmp == null || "".equals(imagename))
			return;
		FileOutputStream fos = null;
		byte[] buffer = null;
		try
		{
			File path = new File(SDCARD_CACHE_IMG_PATH);
			if (!path.exists())
			{
				path.mkdirs();
			}

			File file = new File(SDCARD_CACHE_IMG_PATH + imagename);
			if (file.exists())
			{
				return;
			}
			fos = new FileOutputStream(file);
			buffer = Bitmap2Bytes(bmp);
			fos.write(buffer);
			fos.flush();
		} catch (Exception e)
		{
		} finally
		{
			try
			{
				if (fos != null)
				{
					fos.close();
					fos = null;
				}
			} catch (Exception e)
			{
			}
			buffer = null;
		}
	}

	/**
	 * 获取指定Bitmap的字节数组
	 * 
	 * @param bm
	 *            源图
	 * @return 返回图像的字节数组
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm)
	{
		byte[] bytes = null;
		ByteArrayOutputStream baos = null;
		try
		{
			baos = new ByteArrayOutputStream();
			// 将Bitmap压缩成PNG编码，质量为100%存储
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			bytes = baos.toByteArray();
		} catch (Exception e)
		{
		} finally
		{
			try
			{
				if (baos != null)
				{
					baos.close();
					baos = null;
				}
			} catch (Exception e)
			{
			}
		}
		return bytes;
	}

	/**
	 * 将字节数组转化为一张Bitmap图像
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b)
	{
		if (b != null && b.length != 0)
		{
			try
			{
				return BitmapFactory.decodeByteArray(b, 0, b.length);
			} catch (Exception e)
			{
				return null;
			}
		} else
		{
			return null;
		}
	}

	/**
	 * 从SD卡中获取指定文件名的图像
	 * 
	 * @param imagename
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitMapFromSDCard(String imagename)
	{
		FileInputStream inputStream = null;
		ByteArrayOutputStream out = null;
		byte[] data = null;
		Bitmap bitmap = null;
		try
		{
			File file = new File(SDCARD_CACHE_IMG_PATH + imagename);
			inputStream = new FileInputStream(file);
			out = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = inputStream.read(buffer)) != -1)
			{
				out.write(buffer, 0, len);
			}
			out.flush();

		} catch (Exception e)
		{
		} finally
		{
			try
			{
				if (out != null)
				{
					data = out.toByteArray();
					out.close();
					out = null;
				}
				if (inputStream != null)
				{
					inputStream.close();
					inputStream = null;
				}
				bitmap = Bytes2Bimap(data);
			} catch (Exception e)
			{
			}
		}
		return bitmap;
	}

	// =============以下每个方法,没有做过异常判断===========
	/**
	 * 图片风格
	 */
	public final static int STYLE_DEFAULT = 0;// 默认类型
	public final static int STYLE_NOSTALGIA = 1;// 怀旧类型
	public final static int STYLE_BLACKANDWIHTIE = 2;// 黑白世界
	public final static int STYLE_GOTHIC = 3;// 哥特
	public final static int STYLE_LOMO = 4;// Lomo
	public final static int STYLE_BLUE = 7;// 蓝色
	public final static int STYLE_NIGHT = 6;// 烂漫夜色
	public final static int STYLE_ELEGANTANDQUIET = 8;// 淡雅
	public final static int STYLE_SHARP = 9;// 锐化效果
	public final static int STYLE_PINK = 5;// 红粉世界

	public static Bitmap getStyleBitmap(Bitmap bitmap, float[] array)
	{
		Bitmap output = null;
		try
		{

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);

			Canvas canvas = new Canvas(output);

			paint.setColorFilter(null);

			ColorMatrix cm = new ColorMatrix();
			// 设置颜色矩阵
			cm.set(array);
			// 颜色滤镜，将颜色矩阵应用于图片
			paint.setColorFilter(new ColorMatrixColorFilter(cm));
			// 绘图
			canvas.drawBitmap(bitmap, 0, 0, paint);
		} catch (Exception e)
		{
			// TODO: handle exception
		} catch (OutOfMemoryError e)
		{
		}
		return output;

	}

	/** 获取百叶窗效果图片 */
	public static Bitmap getShutterBitmap(Bitmap bitmap)
	{
		if (bitmap == null)
			return null;
		Bitmap output = null;
		// 图片的宽度和高度
		try
		{

			int imgW = bitmap.getWidth();
			int imgH = bitmap.getHeight();

			// 先将默认图绘制到新的ouput上
			output = Bitmap.createBitmap(imgW, imgH, Config.ARGB_8888);
			Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			Canvas canvas = new Canvas(output);
			canvas.drawBitmap(bitmap, 0, 0, mPaint);

			// 画笔白色,透明度60
			mPaint.setColor(0xffffffff);
			mPaint.setAlpha(60);

			// 绘制的行高
			int drawHeight = 2;
			// 绘制的行数,中间空行较大
			int drawLines = imgH / drawHeight;

			for (int i = 1; i <= drawLines; i++)
			{
				canvas.save();
				canvas.drawRect(0, (i << 1) * drawHeight, imgW, ((i << 1) + 1)
						* drawHeight, mPaint);
				canvas.restore();
			}
		} catch (Exception e)
		{
			output = bitmap;
		} catch (OutOfMemoryError e)
		{
			output = bitmap;
		}
		return output;
	}

	// drawable转换为Bitmap
	public static Bitmap drawableToBitmap(Drawable drawable)
	{
		if (drawable == null)
			return null;
		try
		{
			return ((BitmapDrawable) drawable).getBitmap();
		} catch (Exception e)
		{
		} catch (OutOfMemoryError e)
		{
		}
		return null;
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts)
	{
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception
	{
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	public static Bitmap getBitmapFromUri(Uri uri, Context c)
	{
		try
		{
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					c.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e)
		{
			Log.e("[Android]", e.getMessage());
			Log.e("[Android]", "目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}

	// 根据资源id 获取图片bitmap
	public static Bitmap getBitMapFromID(Resources resources, int id)
	{
		try
		{
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			// 获取资源图片
			InputStream is = resources.openRawResource(id);
			return BitmapFactory.decodeStream(is, null, opt);
		} catch (Exception e)
		{
			// TODO: handle exception
		} catch (OutOfMemoryError e)
		{
		}
		return null;

	}

	/*
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * 
	 * @param resId
	 * 
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId)
	{
		if (context == null || resId < 0)
			return null;
		try
		{

			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			opt.inJustDecodeBounds = false;
			// 获取资源图片
			InputStream is = context.getResources().openRawResource(resId);
			return BitmapFactory.decodeStream(is, null, opt);

		} catch (Exception e)
		{

		} catch (OutOfMemoryError e)
		{
		}
		return null;
	}

	public static Map<Integer, Bitmap> recycleImage(Map<Integer, Bitmap> map,
			int start, int end)
	{
		// TODO Auto-generated method stub
		if (map == null)
			return null;
		for (java.util.Map.Entry<Integer, Bitmap> entry : map.entrySet())
		{
			int positionLocal = entry.getKey();
			if (positionLocal < start || positionLocal > end)
			{
				boolean isneedrecycle = true;
				for (int i = start; i <= end; i++)
				{
					if (entry.getValue() == null || map.get(i) == null)
					{
						continue;
					}
					if (map.get(i) == entry.getValue())
					{
						isneedrecycle = false;
					}
				}
				if (isneedrecycle)
				{
					if (entry.getValue() != null
							&& entry.getValue().isRecycled() == false)
					{
						System.out
								.println("recycle---------->" + positionLocal);
						entry.getValue().recycle();
						entry.setValue(null);
					}
				}
			}
		}
		return map;
	}

	public static void recoverBitmap(ListView lv_msg, int position,
			Map<Integer, Bitmap> maplist)
	{
		int num = lv_msg.getChildCount();
		if (num < 3)
			num += 5;
		Log.i(TAG, "listview.getchildnum--------->" + num);
		List<Integer> list = new ArrayList<Integer>();
		for (Entry<Integer, Bitmap> entry : maplist.entrySet())
		{
			int locationPosition = entry.getKey();
			if ((locationPosition < position && position - locationPosition > num)
					|| (locationPosition > position && locationPosition
							- position > num))
			{
				boolean isneedrecycle = true;
				for (int i = position - 5; i <= position + 5; i++)
				{
					if (entry.getValue() != null
							&& entry.getValue().isRecycled() == false
							&& maplist.get(i) != null)
					{
						if (maplist.get(i) == entry.getValue())
						{
							isneedrecycle = false;
							break;
						}
					}
				}
				if (!isneedrecycle)
				{
					continue;
				}
				Bitmap temp = entry.getValue();
				if (temp != null && temp.isRecycled() == false)
				{
					Log.i(TAG, "recycle  maplocation:------>"
							+ locationPosition);
					Log.i(TAG, "recycle  location:------>" + position);
					temp.recycle();
					list.add(locationPosition);
				}
			}
		}
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				maplist.remove(list.get(i));
			}
		}
	}

	// 俱乐部广告图片加载
	public static DisplayImageOptions getImageOptionsClubAD()
	{
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_topic_default)
				.showImageForEmptyUri(R.drawable.club_topic_default)
				.showImageOnFail(R.drawable.club_topic_default)
				.cacheInMemory(true)
				.considerExifParams(true)
				.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//				.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	// 个人页， 动态列表默认图标
	public static DisplayImageOptions getImageOptionsMyPageItem()
	{
		return new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.my_page_item_img_none)
				.showImageOnFail(R.drawable.my_page_item_img_none)
				.cacheInMemory(true)
				.considerExifParams(true)
				.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//				.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	/**
	 * 俱乐部Icon图标
	 * 
	 * @return
	 */
	public static DisplayImageOptions getImageOptionClubIcon()
	{
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_icon_header_default)
				.showImageForEmptyUri(R.drawable.club_icon_header_default)
				.showImageOnFail(R.drawable.club_icon_header_default)
				.cacheInMemory(true)
				.considerExifParams(true)
				.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//				.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	/**
	 * 		获取大图动态loading效果
	 * @return
	 */
	public static ImageLoadingListener getImageLoading(ImageView loader, ImageView imgRes )
	{
		return new ImageLoadingListen(loader, imgRes);
	}

	/**
	 * 帖子详情大图, 白色占位图
	 * 
	 * @return
	 */
	public static DisplayImageOptions getclub_topic_imageOptions()
	{
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_topic_default)
				.showImageForEmptyUri(R.drawable.club_topic_default)
				.showImageOnFail(R.drawable.club_topic_default)
				.considerExifParams(true)// 图片旋转
				.cacheInMemory(true)
				.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//				.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	/**
	 * 		默认options 什么参数都没有
	 * @return
	 */
	public static DisplayImageOptions getDefultImageOptions()
	{
		return new DisplayImageOptions.Builder().considerExifParams(true)// 图片旋转
				.cacheInMemory(true)
				.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//				.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	/**
	 * 		新版活动贴， 默认图片
	 * @return
	 */
	public static DisplayImageOptions getDefultTravelImageOptions()
	{
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.topic_travel_default_bg)
				.showImageForEmptyUri(R.drawable.topic_travel_default_bg)
				.showImageOnFail(R.drawable.topic_travel_default_bg)
				.considerExifParams(true)// 图片旋转
				.cacheInMemory(true)
				.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//				.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	public static DisplayImageOptions getDynamicImageOptions ()
	{
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_topic_default)
				.showImageForEmptyUri(R.drawable.dymamic_img_none)
				.showImageOnFail(R.drawable.dymamic_img_none)
				.considerExifParams(true)// 图片旋转
				.cacheInMemory(true)
				.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//				.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	/**
	 * 帖子详情， 用户头像
	 * 
	 * @return
	 */
	public static DisplayImageOptions getclub_topic_headImageOptions()
	{
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ls_nologin_header_icon)
				.showImageForEmptyUri(R.drawable.ls_nologin_header_icon)
				.showImageOnFail(R.drawable.ls_nologin_header_icon)
				.cacheInMemory(true)
				.considerExifParams(true)
				.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//				.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	/**
	 * 通过ExifInterface类读取图片文件的被旋转角度
	 * 
	 * @param path
	 *            ： 图片文件的路径
	 * @return 图片文件的被旋转角度
	 */
	public static int readPicDegree(String path)
	{
		int degree = 0;

		// 读取图片文件信息的类ExifInterface
		ExifInterface exif = null;
		try
		{
			exif = new ExifInterface(path);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (exif != null)
		{
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation)
			{
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		}

		return degree;
	}

	/**
	 * 将图片纠正到正确方向
	 * 
	 * @param degree
	 *            ： 图片被系统旋转的角度
	 * @param bitmap
	 *            ： 需纠正方向的图片
	 * @return 纠向后的图片
	 */
	public static Bitmap rotateBitmap(int degree, Bitmap bitmap)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);

		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bm;
	}
	
	/**
	 * 			根据图片的宽度， 设置图片的高度
	 * @param v
	 * @param url
	 * @param options
	 */
	public static void setImageWidthAndHeight ( final ImageView v, final String url, final DisplayImageOptions options )
	{
		ViewTreeObserver vto = v.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				final int ImageWidth = v.getWidth();
				ViewTreeObserver obs = v.getViewTreeObserver();
				obs.removeOnGlobalLayoutListener(this);

				ImageLoader.getInstance().displayImage(
						url,
						v, options, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri, View view) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String imageUri, View view,
														FailReason failReason) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								int w = loadedImage.getWidth();
								int h = loadedImage.getHeight();
								int imgh = ImageWidth * h / w;
								android.view.ViewGroup.LayoutParams l = v.getLayoutParams();
								l.height = imgh;
							}

							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub

							}
						});

			}
		});
	}
	/**
	 * 	保存图片， 回复图片file
	 * */
	public static File saveImageInSdCard ( Bitmap b )
	{
		if ( !Common.hasSDCard() ) return null;
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "lsShare.png");
		ImageUtil.savePic(file.getAbsolutePath(), b, 80);
		return file;
	}


	public static class ImageLoadingListen implements com.nostra13.universalimageloader.core.listener.ImageLoadingListener {
	//3个点， 背影（展示的图片）
		private ImageView imageView, imgRes;
		private AnimationDrawable animationDrawable;

		public ImageLoadingListen(ImageView imageView, ImageView imgRes)
		{
			this.imageView = imageView;
			this.imgRes = imgRes;
		}

		@Override
		public void onLoadingStarted(String s, View view) {
			this.imgRes.setImageBitmap(null);
			this.imageView.setVisibility(View.VISIBLE);
			animationDrawable = (AnimationDrawable) imageView.getDrawable();
			animationDrawable.start();
		}

		@Override
		public void onLoadingFailed(String s, View view, FailReason failReason) {
		}

		@Override
		public void onLoadingComplete(String s, View view, Bitmap bitmap) {
			this.imageView.setVisibility(View.GONE);
			if ( animationDrawable != null )
			{
				animationDrawable.stop();
				animationDrawable = null;
			}
		}

		@Override
		public void onLoadingCancelled(String s, View view) {
		}



	}

	/**
	 * 		保存图片到本地
	 * @param name
	 */
	public static void mySaveBitmap2SD ( final Context c, final String name, String imgUrl )
	{
//		if ( !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//		{
//			return;
//		}
		ImageLoader.getInstance().loadImage(imgUrl, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String s, View view) {

			}

			@Override
			public void onLoadingFailed(String s, View view, FailReason failReason) {

			}

			@Override
			public void onLoadingComplete(String s, View view, Bitmap bitmap) {



				try {
					File f = getImageFileNative(c, name);
					if ( f == null ) return;
					FileOutputStream foutp = new FileOutputStream(f);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foutp);
					foutp.flush();
					foutp.close();
					bitmap = null;

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onLoadingCancelled(String s, View view) {

			}
		});
	}
//获取对应地址的FILE
	private static File getImageFile ( String name )
	{
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lis99/";
		File f = new File(path);
		if ( !f.exists() )
		{
			f.mkdirs();
		}
		File ff = new File(f, name);
		if ( !ff.exists())
		{
			try {
				ff.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return ff;
	}

	//获取对应地址的FILE
	private static File getImageFileNative ( Context c, String name )
	{
//		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lis99/";
		String path = c.getFilesDir() + "/lis99/";
		File f = new File(path);
		if ( !f.exists() )
		{
			f.mkdirs();
		}
		File ff = new File(f, name);
		if ( !ff.exists())
		{
			try {
				ff.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return ff;
	}

	public static Bitmap myGetBitmap2SD ( Context c, String name )
	{
		Bitmap b = null;
		File f = getImageFileNative(c, name);
		if ( f != null )
		{
			try {
				InputStream in = new FileInputStream(f);
				b = BitmapFactory.decodeStream(in);
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	/**
	 * 		根据文件名删除图片
	 * @param name
     * @return
     */
	public static boolean deleteNativeImg ( String name ) {
		if ( name.startsWith("file://"))
		{
			name = name.replace("file://", "");
		}
		File f = new File (name);
		if (f != null && f.exists())
		{
			return f.delete();
		}

		return false;
	}



	/**
	 * 		保存广告
	 * @param imgUrl
	 */
	public static void saveAD ( Context c, String imgUrl )
	{
		mySaveBitmap2SD(c, "LIS99.AD", imgUrl);
	}

	/**
	 * 		读取广告
	 * @return
	 */
	public static Bitmap getAD (Context c )
	{
		return myGetBitmap2SD(c, "LIS99.AD");
	}

	public static Bitmap getUpdataBitmap ( String imgPath ) {

		try {

			//		float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
			float ww = 800f;// 设置宽度为120f，可以明显看到图片缩小了

			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;
			newOpts.inPreferredConfig = Config.RGB_565;
			// Get bitmap info, but notice that bitmap is null now
			Bitmap bitmap = BitmapFactory.decodeFile(imgPath,newOpts);

			newOpts.inJustDecodeBounds = false;

			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			// 想要缩放的目标尺寸

			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;//be=1表示不缩放
			if (w > ww) {//如果宽度大的话根据宽度固定大小缩放
				be = (int) (newOpts.outWidth / ww);
			}
			if (be <= 0) be = 1;
			newOpts.inSampleSize = be;//设置缩放比例
			// 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
			bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
			// 压缩好比例大小后再进行质量压缩
//        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除


			int width = bitmap.getWidth();
			if ( width > ww )
			{
				bitmap = getMatrixBitmap(bitmap);


			}


			return bitmap;

		} catch (Exception e )
		{
//			Common.log("相片格式错误");
//			Common.toast("相片格式错误");
		}

		return null;


	}

	public static Bitmap getMatrixBitmap ( Bitmap b )
	{
		float ww = 800f;
		Bitmap bitmap = null;

		int width = b.getWidth();
		int height = b.getHeight();
		if ( width > ww )
		{
			float scaleWidth = ww / width;

			Matrix m = new Matrix();
			m.postScale(scaleWidth, scaleWidth);
			bitmap = Bitmap.createBitmap(b, 0, 0, width, height, m, true);

		}
		return bitmap;
	}

	/**
	 * 		保存Bitmap 到指定file
	 * @param file
	 * @param bitmap
     * @return
     */
	public static boolean saveImageNative( File file, Bitmap bitmap )
	{
		if ( file == null || bitmap == null ) return false;
		try {
			FileOutputStream foutp = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foutp);
			foutp.flush();
			foutp.close();
			bitmap = null;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 		保存图片从选择的位置，保存到指定位置
	 * @param url
	 * @return
     */
	public static String saveTopicImg ( Context c, String url )
	{
		Bitmap b = getUpdataBitmap(url);

		int start = url.lastIndexOf("/");
		int end = url.lastIndexOf(".");
		String name = url.substring(start + 1, end);

		File fileNative = getImageFileNative(c, name);

		if ( saveImageNative(fileNative, b))
		{
			return fileNative.getAbsolutePath();
		}
		return url;
	}




}
