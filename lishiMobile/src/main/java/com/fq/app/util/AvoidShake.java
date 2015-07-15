package com.fq.app.util;

public class AvoidShake {
	private static long lastTime;

	public static boolean isFastshake() {
		long time = System.currentTimeMillis();
		long timeD = time - lastTime;
		if (0 < timeD && timeD < 2000) {
			return true;
		}
		lastTime = time;
		return false;
	}
}
