package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

public class SaveLog extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveLog";

	public SaveLog(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveLog(int platform, String platformVersion, String message, String stackTrace, String source,RequestCallBack callBack){
		setRequestCallBack(callBack);
		
		String query = new QueryDataBuilder()
		.putData("Platform", String.valueOf(platform))
		.putData("PlatformVersion", platformVersion)
		.putData("Message", message)
		.putData("StackTrace", stackTrace)
		.putData("Source", source)
		.create();
		
		getData().setQueryData(query);
		pocess();
	}
}
