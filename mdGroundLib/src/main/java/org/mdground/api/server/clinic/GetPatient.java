package org.mdground.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;

public class GetPatient extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetPatient";

	public GetPatient(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getPatient(int patientId, RequestCallBack callBack) {
		setRequestCallBack(callBack);
		JSONObject obj = new JSONObject();
		try {
			obj.put("PatientID", String.valueOf(patientId));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
