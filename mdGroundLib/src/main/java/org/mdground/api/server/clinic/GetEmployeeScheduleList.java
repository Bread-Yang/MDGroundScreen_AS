package org.mdground.api.server.clinic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;
import org.mdground.api.utils.DateUtils;

import android.content.Context;

public class GetEmployeeScheduleList extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetEmployeeScheduleList";

	public GetEmployeeScheduleList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getEmployeeScheduleList(Date beginDate, Date endDate, int employeeId, int clinicId, RequestCallBack callBack) {
		if (beginDate == null || endDate == null) {
			return;
		}
		setRequestCallBack(callBack);

		JSONObject obj = new JSONObject();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ", Locale.CHINA);
			obj.put("BeginDate", DateUtils.getDateString(beginDate, sdf) + "00:00:00");
			obj.put("EndDate", DateUtils.getDateString(endDate, sdf) + "23:59:59");
			obj.put("EmployeeID", String.valueOf(employeeId));
			obj.put("ClinicID", String.valueOf(clinicId));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());
		pocess();

	}
}
