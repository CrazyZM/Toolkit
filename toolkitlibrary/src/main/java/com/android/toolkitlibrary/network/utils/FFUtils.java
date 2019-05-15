package com.android.toolkitlibrary.network.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment.InstantiationException;
import android.telephony.TelephonyManager;
import android.util.TypedValue;

import com.android.toolkitlibrary.network.base.FFApplication;

import java.text.DecimalFormat;
import java.util.List;



public class FFUtils {
	public static TelephonyManager getDivice() {
		return (TelephonyManager) FFApplication.app
				.getSystemService(Activity.TELEPHONY_SERVICE);
	}

	public static float getFloat(String fString, float defaultF) {
		try {
			defaultF = Float.parseFloat(fString);
		} catch (Exception e) {

		}
		return defaultF;
	}

	public static void connectWifi(String ssid, String password) {
		if (isStringEmpty(ssid) || isStringEmpty(password)) {
			return;
		}
		try {
			WifiManager wifiManager = (WifiManager) FFApplication.app
					.getSystemService(Context.WIFI_SERVICE);
			WifiConfiguration config = new WifiConfiguration();
			config.SSID = "\"" + ssid + "\"";
			config.preSharedKey = "\"" + password + "\"";
			config.hiddenSSID = false;
			config.status = WifiConfiguration.Status.ENABLED;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			wifiManager.enableNetwork(wifiManager.addNetwork(config),false);
		} catch (Exception e) {

		}
	}

	public static String getSubFloat(float f) {
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(f);
	}

	public static float spToPx(float size) {
		Resources r = FFApplication.app.getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size,
				r.getDisplayMetrics());
	}

	public static int getContentHight(Activity activity) {
		return getDisHight() - getStatusbarHight(activity);
	}

	static int statusBarHight = 0;

	public static int getStatusbarHight(Activity activity) {
		if (statusBarHight != 0) {
			return statusBarHight;
		}
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (java.lang.InstantiationException e) {
				e.printStackTrace();
			}
		}
		statusBarHight = statusHeight;
		return statusHeight;
	}

	public static boolean checkNet() {
		ConnectivityManager manager = (ConnectivityManager) FFApplication.app
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable()) {
			return false;
		}
		return true;
	}

	public static int getPx(float dp) {
		return (int) (FFApplication.app.getResources().getDisplayMetrics().density * dp);
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param
	 * @return
	 */
	public static int getDisHight() {
		return FFApplication.app.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 *
	 *
	 */
	public static int getDisWidth() {
		return FFApplication.app.getResources().getDisplayMetrics().widthPixels;
	}



	public static boolean isStringEmpty(String string) {
		return string == null || string.length() == 0;
	}

	public static boolean isListEmpty(List<?> list) {
		if (list == null) {
			return true;
		}
		return list.isEmpty();
	}

	public static int getListSize(List<?> list) {
		if (isListEmpty(list)) {
			return 0;
		}
		return list.size();
	}

}
