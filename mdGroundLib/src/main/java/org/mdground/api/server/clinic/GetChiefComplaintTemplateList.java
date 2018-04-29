package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

public class GetChiefComplaintTemplateList extends ClinicRequest {
	private static final String FUNCTION_NAME = "GetChiefComplaintTemplateList";

	public GetChiefComplaintTemplateList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void getChiefComplaintTemplateList(RequestCallBack listener) {
		setRequestCallBack(listener);
		getData().setQueryData("{}");
		pocess();
	}

}
