package org.mdground.api.server.global;

import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.GlobalRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;
import android.util.Log;

public class CheckEmployeePhone extends GlobalRequest {
	private static final String FUNCTION_NAME = "CheckEmployeePhone";

	public CheckEmployeePhone(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void checkEmployeePhone(String phone, RequestCallBack listener) {
		if (phone == null || phone.equals("")) {
			Log.e(TAG, "phone is null");
			return;
		}
		setRequestCallBack(listener);

		RequestData data = getData();
		JSONObject obj = new JSONObject();
		try {
			obj.put("Phone", phone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		data.setQueryData(obj.toString());

		pocess();
	}

}
