package org.mdground.api.base;

import android.content.Context;

import org.mdground.api.BuildConfig;


public abstract class FileServerRequest extends BaseRequest {

	public FileServerRequest(Context context) {
		super(context);
	}

	@Override
	protected String getUrl() { 
		return BuildConfig.FILE_HOST;
	}

	@Override
	protected int getBusinessCode() {
		return BusinessType.FILE.getType(); 
	}

}
