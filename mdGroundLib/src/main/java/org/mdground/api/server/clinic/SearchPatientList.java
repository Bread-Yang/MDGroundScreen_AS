package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

public class SearchPatientList extends ClinicRequest {
	private static final String FUNCTION_NAME = "SearchPatientList";

	public SearchPatientList(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void searchPatient(String keyword, RequestCallBack requestCallBack) {
		if (keyword == null) {
			return;
		}
		
		setRequestCallBack(requestCallBack);
		String query = new QueryDataBuilder().putData("Keyword", keyword).create();
		getData().setQueryData(query);
		pocess();
	}

}
