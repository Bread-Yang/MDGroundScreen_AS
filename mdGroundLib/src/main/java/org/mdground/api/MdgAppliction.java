package org.mdground.api;


import org.mdground.api.base.PlatformType;
import org.mdground.api.bean.Employee;

import android.app.Application;
import android.content.Context;

public abstract class MdgAppliction extends Application {
	
	
	/**对外提供整个应用生命周期的Context**/
	private static Context instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	
	/**

	 * 对外提供Application Context

	 * @return

	 */
	public static Context gainContext() {
		return instance;
	}
	
	
	public abstract Employee getLoginEmployee();
	
	public abstract int getDeviceId();

	/**
	 * 平台类型  参见 {@link PlatformType}
	 * @return
	 */
	public abstract int getPlatform();
}
