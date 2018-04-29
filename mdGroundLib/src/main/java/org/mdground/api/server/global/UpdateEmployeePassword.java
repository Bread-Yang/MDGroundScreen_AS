package org.mdground.api.server.global;

import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.GlobalRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;
import android.util.Log;

public class UpdateEmployeePassword extends GlobalRequest {
	private static final String FUNCTION_NAME = "UpdateEmployeePassword";

	public UpdateEmployeePassword(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateEmployeePassword(String phone, String newPwd, RequestCallBack listener) {
		if (phone == null || newPwd == null) {
			Log.e(TAG, "phone or newPwd is null");
			return;
		}

		setRequestCallBack(listener);
		JSONObject obj = new JSONObject();
		try {
			obj.put("Phone", phone);
			obj.put("NewPwd", newPwd);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}

}
