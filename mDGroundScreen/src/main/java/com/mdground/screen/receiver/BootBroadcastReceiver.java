package com.mdground.screen.receiver;

import com.mdground.screen.activity.LoginActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	static final String action_boot = "android.intent.action.BOOT_COMPLETED";

	// 重写onReceive方法
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(action_boot)) {
			Intent ootStartIntent = new Intent(context, LoginActivity.class);
			ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(ootStartIntent);
		}
	}

}
