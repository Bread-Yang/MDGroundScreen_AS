package org.mdground.api.server.clinic;

import java.util.List;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;
import org.mdground.api.bean.Schedule;

import android.content.Context;

public class SaveDoctorEmergencyLeave extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveDoctorEmergencyLeave";

	public SaveDoctorEmergencyLeave(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveDoctorEmergencyLeave(List<Schedule> schedules, RequestCallBack callBack) {
		if (schedules == null || schedules.size() == 0) {
			return;
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < schedules.size(); i++) {
			Schedule s = schedules.get(i);
			sb.append(s.getId());
			sb.append(",");
		}
		String esids = sb.substring(0, sb.length() - 1);

		saveDoctorEmergencyLeave(esids, callBack);
	}

	public void saveDoctorEmergencyLeave(String esids, RequestCallBack callBack) {
		setRequestCallBack(callBack);
		RequestData data = getData();
		data.setQueryData(esids);

		pocess();
	}
}
