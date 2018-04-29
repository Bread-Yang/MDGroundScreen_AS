package com.mdground.screen.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferUtils {

	private static final String SHARED_PREFERENCES_KEY = "YiDeGuan";

	SharedPreferences share;

	public enum ShareKey {
		ClinicID, UserNickname, UserId, UserPsw, DEVICE_ID, XGPush_Token;
	}

	public SharedPreferUtils(Context context) {
		share = context.getSharedPreferences(SHARED_PREFERENCES_KEY,
				Context.MODE_PRIVATE);
	}

	public void put(ShareKey key, String value) {
		share.edit().putString(key.name(), value).commit();
	}

	public String get(ShareKey key, String defaultValue) {
		return share.getString(key.name(), defaultValue);
	}

	public void put(ShareKey key, Boolean value) {
		share.edit().putBoolean(key.name(), value).commit();
	}

	public Boolean get(ShareKey key, Boolean defaultValue) {
		return share.getBoolean(key.name(), defaultValue);
	}
	
	public void put(ShareKey key, int value) {
		share.edit().putInt(key.name(), value).commit();
	}

	public int get(ShareKey key, int defaultValue) {
		return share.getInt(key.name(), defaultValue);
	}

	/**
	 * 清空
	 * 
	 * @param context
	 */
	public void clear() {
		share.edit().clear().commit();
	}
}
