package org.mdground.api.server.global;

import org.mdground.api.base.GlobalRequest;
import org.mdground.api.base.RequestCallBack;

import android.content.Context;

/**
 * 	基本数据
 * @author Vincent
 *
 */
public class UpdateData extends GlobalRequest {
	private static final String FUNCTION_NAME = "UpdateData";

	public UpdateData(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void updateData(RequestCallBack callBack) {
		setRequestCallBack(callBack);

		pocess();
	}
}
