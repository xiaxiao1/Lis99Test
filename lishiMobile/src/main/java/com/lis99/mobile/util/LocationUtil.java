package com.lis99.mobile.util;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lis99.mobile.entry.application.DemoApplication;

/***
 * 定位功能
 * 
 * @author yy
 * 
 */
public class LocationUtil
{
	private static LocationUtil instance;

	public static LocationUtil getinstance()
	{
		if (instance == null)
			instance = new LocationUtil();
		return instance;
	}

	// 定位相关
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();

	public void getLocation()
	{
		// 定位初始化
		mLocClient = new LocationClient(DemoApplication.getInstance());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(900);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener
	{

		@Override
		public void onReceiveLocation(BDLocation location)
		{
			// map view 销毁后不在处理新接收的位置
			if (location == null){
				Common.toast("定位失败");
				stopLocation();

				if ( glocation != null )
				{
					glocation.Location(0, 0);
				}

				return;
			}
//			Common.log("getLatitude()=="+location.getLatitude()+"=location.getLongitude()=="+location.getLongitude());
			if ( glocation != null )
			{
				glocation.Location(location.getLatitude(), location.getLongitude());
			}
			stopLocation();
		}

		public void onReceivePoi(BDLocation poiLocation)
		{
		}
	}

	public void stopLocation()
	{
		if ( mLocClient != null && mLocClient.isStarted() )
		{
			mLocClient.stop();
		}
		setGlocation(null);
	}
	
	private getLocation glocation;
	
	

	public getLocation getGlocation()
	{
		return glocation;
	}



	public void setGlocation(getLocation glocation)
	{
		this.glocation = glocation;
	}

	public interface getLocation
	{
		public void Location ( double latitude, double longitude );
	}
	
}
