package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

public class UpdateAppointmentByAssign extends ClinicRequest {
	private static final String FUNCTION_NAME = "UpdateAppointmentByAssign";

	public UpdateAppointmentByAssign(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateAppointmentByAssign(int opid, int doctorId, RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);

		String query = new QueryDataBuilder().putData("OPID", String.valueOf(opid)).putData("DoctorID", String.valueOf(doctorId)).create();
		getData().setQueryData(query);
		pocess();
	}

}
