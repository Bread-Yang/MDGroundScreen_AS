package org.mdground.api.server.clinic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;
import org.mdground.api.bean.Schedule;
import org.mdground.api.utils.DateUtils;

import android.content.Context;

public class SaveEmployeeScheduleList extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveEmployeeScheduleList";

	public SaveEmployeeScheduleList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveEmployeeScheduleList(Date workDate, List<Schedule> schedules, RequestCallBack callBack) {
		if (workDate == null || schedules == null || schedules.size() == 0) {
			return;
		}

		JSONObject obj = new JSONObject();
		try {
			obj.put("DSDate", DateUtils.getDateString(workDate, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));
			obj.put("DSList", schedules);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());

		pocess();
	}
}
