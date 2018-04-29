package org.mdground.api.server.global;

import org.mdground.api.base.GlobalRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

public class GetDoctorList extends GlobalRequest {
	private static final String FUNCTION_NAME = "GetDoctorList";

	public GetDoctorList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}


	public void getDoctorList(RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);
		
		pocess();
	}

}
