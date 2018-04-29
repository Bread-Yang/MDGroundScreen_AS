package org.mdground.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;

public class GetAppointmentInfoListByDoctor extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetAppointmentInfoListByDoctor";

	public GetAppointmentInfoListByDoctor(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getAppointmentInfoListByDoctor(int status, int doctorId, RequestCallBack listener) {
		if (listener == null) {
			throw new RuntimeException("please input listener");
		}
		setRequestCallBack(listener);

		JSONObject obj = new JSONObject();
		try {
			obj.put("Status", String.valueOf(status));
			obj.put("DoctorID", String.valueOf(doctorId));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}

}
