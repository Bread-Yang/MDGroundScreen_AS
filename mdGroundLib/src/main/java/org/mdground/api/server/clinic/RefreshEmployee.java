package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

public class RefreshEmployee extends ClinicRequest {
	private static final String FUNCTION_NAME = "RefreshEmployee";

	public RefreshEmployee(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void refreshEmployee(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		pocess();
	}
}
