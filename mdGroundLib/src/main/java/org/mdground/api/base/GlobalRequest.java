package org.mdground.api.base;

import android.content.Context;

import org.mdground.api.BuildConfig;


public abstract class GlobalRequest extends BaseRequest {

	public GlobalRequest(Context context) {
		super(context);
	}

	@Override
	protected String getUrl() {
		return BuildConfig.GLOBAL_HOST;
	}

	@Override
	protected int getBusinessCode() {
		return BusinessType.Global.getType();
	}

}
