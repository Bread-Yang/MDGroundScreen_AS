package com.mdground.screen;

import org.mdground.api.MdgAppliction;
import org.mdground.api.base.PlatformType;
import org.mdground.api.bean.Clinic;
import org.mdground.api.bean.Employee;

import com.baidu.android.common.logging.Log;
import com.mdground.screen.constant.MemberConstant;
import com.mdground.screen.utils.L;
import com.mdground.screen.utils.MedicalImageDownload;
import com.mdground.screen.utils.PreferenceUtils;
import com.mdground.screen.utils.crash.CrashHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import android.content.Context;
import android.content.Intent;

public class MedicalApplication extends MdgAppliction {

	public static Employee employee;// 登陆用户

	private Clinic mClinic; // 诊所信息

	@Override
	public void onCreate() {
		super.onCreate();

        CrashHandler.getInstance().init(this, true);
		Context context = getApplicationContext();

//        // 开启logcat输出，方便debug，发布时请关闭  121212
//		XGPushConfig.enableDebug(this, true);
//		// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(),
//		// XGIOperateCallback)带callback版本
//		// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
//		// 具体可参考详细的开发指南
//		// 传递的参数为ApplicationContext
//
////		在application添加attachBaseContext里StubAppUtils.attachBaseContext(context)；
////		初始化页面添加 XGPushConfig.enableOtherPush(getApplicationContext(), true);
//		XGPushConfig.setMiPushAppId(getApplicationContext(), "2100144128");
//		XGPushConfig.setMiPushAppKey(getApplicationContext(), "AK4493K4JNCD");
////		XGPushConfig.setMzPushAppId(this, "APPID");
////		XGPushConfig.setMzPushAppKey(this, "APPKEY");
//		XGPushManager.registerPush(this, new XGIOperateCallback() {
//			@Override
//			public void onSuccess(Object data, int flag) {
////token在设备卸载重装的时候有可能会变
//				Log.d("TPush", "注册成功，设备token为：" + data);
//			}
//			@Override
//			public void onFail(Object data, int errCode, String msg) {
//				Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
//			}
//		});
//
////		XGPushManager.registerPush(context, new XGIOperateCallback() {
////
////			@Override
////			public void onSuccess(Object arg0, int arg1) {
////				Log.e("哈哈", "信鸽注册成功");
//////				L.e(MedicalAppliction.this, "信鸽注册成功");
////			}
////
////			@Override
////			public void onFail(Object arg0, int arg1, String arg2) {
////				Log.e("哈哈", "信鸽注册失败");
//////				L.e(MedicalAppliction.this, "信鸽注册失败");
////			}
////		});
////		L.e(this, "MedicalAppliction");
////
////		XGPushManager.registerPush(getApplicationContext());
//
//		// 2.36（不包括）之前的版本需要调用以下2行代码
//		Intent service = new Intent(context, XGPushServiceV4.class);
//		context.startService(service);

		// 其它常用的API：
		// 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account,
		// XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
		// 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
		// 反注册（不再接收消息）：unregisterPush(context)
		// 设置标签：setTag(context, tagName)
		// 删除标签：deleteTag(context, tagName)

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.imageDownloader(new MedicalImageDownload(getApplicationContext())).build();
		ImageLoader.getInstance().init(configuration);

		L.e(this, "导诊屏的分辨率 : " + this.getResources().getDisplayMetrics().density);
	}

	public MedicalApplication() {
	}

	public Employee getLoginEmployee() {
		return employee;
	}

	public void setLoginEmployee(Employee employee) {
		this.employee = employee;
	}

	public Clinic getmClinic() {
		return mClinic;
	}

	public void setmClinic(Clinic mClinic) {
		this.mClinic = mClinic;
	}

	@Override
	public int getDeviceId() {
		return PreferenceUtils.getPrefInt(this, MemberConstant.DEVICE_ID, 0);
	}

	@Override
	public int getPlatform() {
		return PlatformType.AndroidScreen.platform();
	}

}
