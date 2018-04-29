package com.mdground.screen.activity;

import com.mdground.screen.utils.AppManager;

import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

	protected void onCreate(android.os.Bundle arg0) {
		super.onCreate(arg0);
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

}
