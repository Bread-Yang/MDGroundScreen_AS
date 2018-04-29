package org.mdground.api.utils;

import java.util.Locale;

import org.mdground.api.base.PlatformType;
import org.mdground.api.bean.Device;


import android.content.Context;
import android.content.res.Configuration;

public class DeviceUtils {

	// 判断是平板还是手机
	public static boolean isPad(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static String getLanguage(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		return locale.getLanguage();
	}

	public static Device getDeviceInfo(Context context) {
		Device device = new Device();
		boolean isPad = DeviceUtils.isPad(context);
		if (isPad) {
			device.setPlatform(PlatformType.ANDROID_PAD.platform());
		} else {
			device.setPlatform(PlatformType.ANDROID_PHONE.platform());
		}
		// 设置android版本号
		device.setPlatformVersion(android.os.Build.VERSION.RELEASE);
		// 型号
		device.setDeviceModel(android.os.Build.MODEL);

		return device;
	}
}
