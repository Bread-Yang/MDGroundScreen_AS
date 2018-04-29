package org.mdground.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;

public class GetAppointmentCountForWaiting extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetAppointmentCountForWaiting";

	public GetAppointmentCountForWaiting(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getAppointmentCountForWaiting(String doctorList, RequestCallBack listener) {
		if (doctorList == null || doctorList.equals("")) {
			return;
		}
		setRequestCallBack(listener);

		JSONObject obj = new JSONObject();
		try {
			obj.put("DoctorIDList", doctorList);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}

}
