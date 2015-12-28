package com.lis99.mobile.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.widget.ioscitychoose.AbstractWheelTextAdapter;
import com.lis99.mobile.club.widget.ioscitychoose.AddressData;
import com.lis99.mobile.club.widget.ioscitychoose.ArrayWheelAdapter;
import com.lis99.mobile.club.widget.ioscitychoose.MyAlertDialog;
import com.lis99.mobile.club.widget.ioscitychoose.OnWheelChangedListener;
import com.lis99.mobile.club.widget.ioscitychoose.WheelView;
import com.lis99.mobile.entry.view.CustomProgressDialog;

/**
 * 				提示框类
 * @author Administrator
 *
 */
public class DialogManager {

	private static CustomProgressDialog customProgressDialog;
//
	private static Dialog dlWait;
	
	private static DialogManager Instance;
	
	private String cityTxt;
	
	public static DialogManager getInstance ()
	{
		if ( null == Instance ) Instance = new DialogManager();
		return Instance;
	}
	
	public void alert(Activity a, String message) {
		alert(a, "提示", message);
	}

	public void alert (Activity a, String title, String message)
	{
		AlertDialog.Builder bld = new AlertDialog.Builder(a);
		bld.setTitle(title);
		bld.setMessage(message);
		bld.setNeutralButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		AlertDialog create = bld.create();
		create.setCanceledOnTouchOutside(false);
		create.setCancelable(false);
		create.show();
	}
	
	/**
	 * 启动等待对话框
	 * 
	 * @param title
	 * @param content
	 */
	synchronized public void startWaiting(Activity a, String title, String content) {
//		if (customProgressDialog == null) {
//			customProgressDialog = CustomProgressDialog.getInstance(a);
//		}
//		if (customProgressDialog != null
//				&& customProgressDialog.isShow() == false) {
//			customProgressDialog.popup(this, a, title, content);
//		}

		showWaiting(a);


	}

	/**
	 * 结束等待对话框
	 */
	public synchronized void stopWaitting() {

//		if (customProgressDialog != null && customProgressDialog.isShow()) {
//			customProgressDialog.close();
//		}

		stopWaitingDialog();

	}
	/**
	 * 				启动提示对话框
	 * @param a					activity
	 * @param title				
	 * @param content
	 * @param leftBtn			是否有左按钮
	 * @param leftStr			按钮文字
	 * @param leftBtnListener
	 * @param rightBtn			是否有右按钮
	 * @param rightStr			按钮文字
	 * @param rightBtnListener
	 */
	synchronized public void startAlert(Activity a, String title, String content,
			boolean leftBtn, String leftStr,
			DialogInterface.OnClickListener leftBtnListener, boolean rightBtn,
			String rightStr, DialogInterface.OnClickListener rightBtnListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setTitle(title);
		builder.setMessage(content);
		if (leftBtn)
			builder.setPositiveButton(leftStr, leftBtnListener);
		if (rightBtn)
			builder.setNegativeButton(rightStr, rightBtnListener);
		builder.create().show();
	}

	
	/**
	 * 开启列表对话框
	 * 
	 * @param title
	 * @param itemsId
	 * @param listener
	 */
	synchronized public void showDialogList(Activity a, String title, int itemsId,
			DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setTitle(title);
		builder.setItems(itemsId, listener).setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.create().show();
	}
	//GPS定位失败后弹出提示
	public void showDialogGpsError (final Activity a)
	{
		startAlert(a, "请在设置中启用定位服务", "砾石客户端将仅使用定位服务帮助您选择附近的聚乐部和户外店", true, "设置", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			    a.startActivityForResult(intent,0);
			}
		}, true, "取消", null);
	}
	/**
	 * 			报名， 城市选择
	 */
	public void showCityChooseDialog ( final Activity a, final callBack click )
	{
		View view = dialogm(a);
		final MyAlertDialog dialog1 = new MyAlertDialog(a)
				.builder()
				.setTitle("城市选择")
				.setView(view)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
		dialog1.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(a, cityTxt, 1).show();
				if ( click != null )
					click.onCallBack(cityTxt);
			}
		});
		dialog1.show();
	}
	
	
	private CountryAdapter counrtyA;
	private ArrayWheelAdapter<String> cityadapter;
	private ArrayWheelAdapter<String> ccityadapter;
	
	private View dialogm(final Activity a) {
		View contentView = LayoutInflater.from(a).inflate(
				R.layout.wheelcity_cities_layout, null);
		final WheelView country = (WheelView) contentView
				.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		counrtyA = new CountryAdapter(a);
		country.setViewAdapter(counrtyA);

		final String cities[][] = AddressData.CITIES;
		final String ccities[][][] = AddressData.COUNTIES;
		final WheelView city = (WheelView) contentView
				.findViewById(R.id.wheelcity_city);
		city.setVisibleItems(0);

		// 地区选择
		final WheelView ccity = (WheelView) contentView
				.findViewById(R.id.wheelcity_ccity);
		ccity.setVisibleItems(0);// 不限城市

		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(a, city, cities, newValue);
				
				cityTxt = getCity((String)counrtyA.getItemText(country.getCurrentItem()), (String)cityadapter.getItemText(city.getCurrentItem()), (String)ccityadapter.getItemText(ccity.getCurrentItem()));
			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(a, ccity, ccities, country.getCurrentItem(),
						newValue);
				cityTxt = getCity((String)counrtyA.getItemText(country.getCurrentItem()), (String)cityadapter.getItemText(city.getCurrentItem()), (String)ccityadapter.getItemText(ccity.getCurrentItem()));
			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				cityTxt = getCity((String)counrtyA.getItemText(country.getCurrentItem()), (String)cityadapter.getItemText(city.getCurrentItem()), (String)ccityadapter.getItemText(ccity.getCurrentItem()));
			}
		});

		updateCities(a,city, cities, 0);
		updatecCities(a,ccity, ccities, 0, 0);
		country.setCurrentItem(0);// 设置北京
		city.setCurrentItem(0);
		ccity.setCurrentItem(0);
		return contentView;
	}
	
	/**
	 * Updates the city wheel
	 */
	private void updateCities(Activity a, WheelView city, String cities[][], int index) {
		cityadapter = new ArrayWheelAdapter<String>(a,
				cities[index]);
		cityadapter.setTextSize(18);
		cityadapter.setTextColor(a.getResources().getColor(R.color.pull_text_color));
		city.setViewAdapter(cityadapter);
		city.setCurrentItem(0);
	}
	
	/**
	 * Updates the ccity wheel
	 */
	private void updatecCities(Activity a, WheelView city, String ccities[][][], int index,
			int index2) {
		String[] str = null;
		if ( ccities.length <= index || ccities[index].length <= index2 )
		{
			str = AddressData.NO_LIMITS;
		}
		else
		{
			str = ccities[index][index2];
		}
		ccityadapter = new ArrayWheelAdapter<String>(a,
				str);
		ccityadapter.setTextSize(18);
		ccityadapter.setTextColor(a.getResources().getColor(R.color.pull_text_color));
		city.setViewAdapter(ccityadapter);
		city.setCurrentItem(0);
	}
	
	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
		private String countries[] = AddressData.PROVINCES;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}
	
	public interface callBack{
		public void onCallBack(Object o);
	}
	
	public String getCity ( String c, String cc, String ccc )
	{
		String str = "";
		if ( !"未设定".equals(c))
		{
			str += c;
		}
		if ( !"未设定".equals(cc))
		{
			str += " "+cc;
		}
		if ( !"未设定".equals(ccc))
		{
			str += " "+ccc;
		}
		return str;
	}
//  应用更新提醒框
	public void showUpdataDialog (String title, String content, String url, String type )
	{
		final String Url = url;

		final Dialog dialog = new Dialog(LSBaseActivity.activity, R.style.CustomDialog);

		dialog.show();

		dialog.setContentView(R.layout.new_dialog_view);

		TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);

		TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);

		Button cancel = (Button) dialog.findViewById(R.id.cancel);

		Button ok = (Button) dialog.findViewById(R.id.ok);

		if ( !TextUtils.isEmpty(title))
		{
			tv_title.setText(title);
		}

		tv_content.setText(content);


		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (dialog != null) dialog.dismiss();
			}
		});

		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (dialog != null) dialog.dismiss();
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(Url);
				intent.setData(content_url);
				LSBaseActivity.activity.startActivity(intent);
			}
		});
	}

	synchronized private static void showWaiting ( Activity a )
	{
		if ( dlWait == null )
		{
			dlWait = new Dialog(a == null ? LSBaseActivity.activity : a, R.style.waitingDialog);

			dlWait.setContentView(R.layout.waiting_dialog);

			dlWait.setCanceledOnTouchOutside(false);

		}


		if ( dlWait != null && !dlWait.isShowing() )
		{
			dlWait.show();
		}
	}

	synchronized private static void stopWaitingDialog ()
	{
		if ( dlWait != null && dlWait.isShowing() )
		{
			dlWait.dismiss();
		}
	}


}
