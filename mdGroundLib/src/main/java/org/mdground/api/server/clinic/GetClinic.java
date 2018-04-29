package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;

/**
 * 获取诊所信息
 *
 * @author yoghourt
 *
 */
public class GetClinic extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetClinic";

	public GetClinic(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getClinic(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		RequestData data = getData();

		pocess();
	}
}
