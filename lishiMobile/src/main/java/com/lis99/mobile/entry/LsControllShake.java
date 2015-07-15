package com.lis99.mobile.entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

@SuppressLint("Instantiatable")
public class LsControllShake implements SensorEventListener {

	private static final int FORCE_THRESHOLD = 888;
	private static final int TIME_THRESHOLD = 123;
	private static final int SHAKE_TIMEOUT = 499;
	private static final int SHAKE_DURATION = 1000;
	private static final int SHAKE_COUNT = 1;

	private SensorManager mSensorMgr;
	private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
	private long mLastTime;
	private OnShakeListener mShakeListener;
	private Context mContext;
	private int mShakeCount = 0;
	private long mLastShake;
	private long mLastForce;

	public interface OnShakeListener {
		public void onShake();
		// public void onShakeHorizontal();
		// public void onShakeVertical();
	}

	public LsControllShake(Context context) {
		mContext = context;
		resume();
	}

	public void setOnShakeListener(OnShakeListener listener) {
		mShakeListener = listener;
	}

	public void resume() {
		mSensorMgr = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorMgr == null) {
			throw new UnsupportedOperationException("Sensors not supported");
		}

		boolean supported = mSensorMgr.registerListener(this,
				mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		if (!supported) {
			mSensorMgr.unregisterListener(this);
			throw new UnsupportedOperationException(
					"Accelerometer not supported");
		}
	}

	public void pause() {
		if (mSensorMgr != null) {
			mSensorMgr.unregisterListener(this);
			mSensorMgr = null;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
			return;
		}

		long now = System.currentTimeMillis();

		if ((now - mLastForce) > SHAKE_TIMEOUT) {
			mShakeCount = 0;
		}

		if ((now - mLastTime) > TIME_THRESHOLD) {
			long diff = now - mLastTime;
			float speed = Math.abs(event.values[SensorManager.DATA_X]
					+ event.values[SensorManager.DATA_Y]
					+ event.values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ)
					/ diff * 10000;
			if (speed > FORCE_THRESHOLD) {
				if ((++mShakeCount >= SHAKE_COUNT)
						&& (now - mLastShake > SHAKE_DURATION)) {
					mLastShake = now;
					mShakeCount = 0;
					if (mShakeListener != null) {
						mShakeListener.onShake();
					}
				}
				mLastForce = now;
			}
			mLastTime = now;
			mLastX = event.values[SensorManager.DATA_X];
			mLastY = event.values[SensorManager.DATA_Y];
			mLastZ = event.values[SensorManager.DATA_Z];
		}
	}
}
// int sensorType = event.sensor.getType();
// // values[0]:X轴，values[1]：Y轴，values[2]：Z轴
// float[] values = event.values;
//
// float x = values[0];
// float y = values[1];
// float z = values[2];
//
// if (sensorType == Sensor.TYPE_ACCELEROMETER) {
// int value = 15;// 摇一摇阀值,不同手机能达到的最大值不同,如某品牌手机只能达到20
// if (x >= value || x <= -value || y >= value || y <= -value
// || z >= value || z <= -value) {
//
// if (condition) {
//
// mShakeListener.onShake();
// }
//
//
//
// }
// }

