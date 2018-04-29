package org.mdground.api.server.global;

import org.mdground.api.base.GlobalRequest;

import android.content.Context;

public class SaveEmployeePhoto extends GlobalRequest {
	private static final String FUNCTION_NAME = "SaveEmployeePhoto";

	public SaveEmployeePhoto(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

}
