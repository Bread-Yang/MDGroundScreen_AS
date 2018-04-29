package org.mdground.api.server.global;

import org.mdground.api.base.GlobalRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;

public class UpdateDeviceToken extends GlobalRequest {
	private static final String FUNCTION_NAME = "UpdateDeviceToken";

	public UpdateDeviceToken(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateDeviceToken(String token, RequestCallBack callBack) {
		setRequestCallBack(callBack);
		
		RequestData data = getData();
		data.setQueryData(token);
		pocess(true);
	}

}
