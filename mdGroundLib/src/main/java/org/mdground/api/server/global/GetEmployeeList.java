package org.mdground.api.server.global;

import org.mdground.api.base.GlobalRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

public class GetEmployeeList extends GlobalRequest {
	private static final String FUNCTION_NAME = "GetEmployeeList";

	public GetEmployeeList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getEmployeeList(RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);
		pocess();
	}

}
