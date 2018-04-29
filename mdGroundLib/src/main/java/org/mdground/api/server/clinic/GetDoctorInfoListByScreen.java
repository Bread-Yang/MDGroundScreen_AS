package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;
import android.util.Log;

public class GetDoctorInfoListByScreen extends ClinicRequest {

	private static final String FUNCTION_NAME = "GetDoctorInfoListByScreen";

	public GetDoctorInfoListByScreen(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getDoctorInfoListByScreen(int employeeID, RequestCallBack listener) {
		
		setRequestCallBack(listener);

		RequestData data = getData();
		data.setQueryData(String.valueOf(employeeID));

		pocess();
	}

}
