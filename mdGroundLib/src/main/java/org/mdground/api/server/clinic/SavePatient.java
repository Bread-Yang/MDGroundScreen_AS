package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.bean.Patient;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SavePatient extends ClinicRequest {
	private static final String FUNCTION_NAME = "SavePatient";

	public SavePatient(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void savePatient(Patient patientDetail, RequestCallBack requestCallBack) {
		if (patientDetail == null) {
			return;
		}
		
		setRequestCallBack(requestCallBack);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		getData().setQueryData(gson.toJson(patientDetail));
		pocess();
	}

}
