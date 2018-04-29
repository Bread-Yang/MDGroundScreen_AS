package org.mdground.api.server.global;

import org.mdground.api.base.GlobalRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

public class LogoutEmployee extends GlobalRequest {
	private static final String FUNCTION_NAME = "LogoutEmployee";

	public LogoutEmployee(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void logoutEmployee(RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);
		getData().setQueryData("");
		pocess();
	}

}
