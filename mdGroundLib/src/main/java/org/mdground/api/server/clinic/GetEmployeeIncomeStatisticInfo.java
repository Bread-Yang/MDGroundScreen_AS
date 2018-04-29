package org.mdground.api.server.clinic;

import org.json.JSONException;
import org.json.JSONObject;
import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;

public class GetEmployeeIncomeStatisticInfo extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetChiefComplaintTemplateList";

	public GetEmployeeIncomeStatisticInfo(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getEmployeeIncomeStatisticInfo(int employeeID, RequestCallBack callBack) {
		setRequestCallBack(callBack);

		JSONObject obj = new JSONObject();
		try {
			obj.put("EmployeeID", String.valueOf(employeeID));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RequestData data = getData();
		data.setQueryData(obj.toString());
		pocess();
	}

}
