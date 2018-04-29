package org.mdground.api.server.global;

import org.mdground.api.base.GlobalRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;
import android.util.Log;

public class CheckEmployeePassword extends GlobalRequest {

	private static final String FUNCTION_NAME = "CheckEmployeePassword";

	public CheckEmployeePassword(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void checkEmployeePwd(String password, RequestCallBack listener) {
		if (password == null || password.equals("")) {
			Log.e(TAG, "password is null");
			return;
		}
		setRequestCallBack(listener);

		RequestData data = getData();
		data.setQueryData(password);

		pocess();
	}

}
