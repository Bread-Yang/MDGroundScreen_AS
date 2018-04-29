package org.mdground.api.base;

import android.content.Context;


public abstract class GlobalRequest extends BaseRequest {

	private static final String GLOBAL_SERVER = "https://g.yideguan.com/api/RpcService.ashx";
	// 修改版本号 // 修改版本号 // 修改版本号 // 修改版本号 // 修改版本号 // 修改版本号 // 修改版本号 // 修改版本号 // 修改版本号
//	private static final String GLOBAL_SERVER = "https://guat.yideguan.com/api/RpcService.ashx";

//	private static final String GLOBAL_SERVER = "http://192.168.0.107:8000/api/RpcService.ashx";

	public GlobalRequest(Context context) {
		super(context);
	}

	@Override
	protected String getUrl() {
		return GLOBAL_SERVER;
	}

	@Override
	protected int getBusinessCode() {
		return BusinessType.Global.getType();
	}

}
