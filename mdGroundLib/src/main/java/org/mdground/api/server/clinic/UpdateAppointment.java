package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

public class UpdateAppointment extends ClinicRequest {
	private static final String FUNCTION_NAME = "UpdateAppointment";

	public UpdateAppointment(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateAppointment(int oPid, int statusFinish, RequestCallBack requestCallBack) {

		setRequestCallBack(requestCallBack);

		String query = new QueryDataBuilder().putData("OPID", String.valueOf(oPid)).putData("OPStatus", String.valueOf(statusFinish)).create();
		getData().setQueryData(query);
		pocess();
	}

}
