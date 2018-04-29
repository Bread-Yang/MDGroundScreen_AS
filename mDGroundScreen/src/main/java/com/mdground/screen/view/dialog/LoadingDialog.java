/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mdground.screen.view.dialog;

import com.mdground.screen.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class LoadingDialog extends Dialog {

	public static final int STYLE_SPINNER = 0;
	public static final int STYLE_HORIZONTAL = 1;

	private static LoadingDialog instance = null;

	public LoadingDialog(Context context) {
		this(context, R.style.loadingDialog);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	boolean isDissmiss = false;

	@Override
	public void dismiss() {
		try {
			super.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		try {
			super.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	String text = "";

	public static LoadingDialog getInstance(Context context, String text) {
		if (instance == null) {
			instance = new LoadingDialog(context.getApplicationContext());
		}
		instance.text = text;
		return instance;
	}

	@Override
	public void onBackPressed() {
		Log.d("hmz", "onBackPressed");
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LinearLayout.inflate(getContext(), R.layout.dialog_loading, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.loading_gif);
		TextView LoginID = (TextView) view.findViewById(R.id.LoginID);
		if (text.length() != 0) {
			LoginID.setText(text);
		}
		AnimationDrawable animator = (AnimationDrawable) imageView.getBackground();
		imageView.setImageDrawable(null);
		animator.start();
		setContentView(view);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public LoadingDialog initText(String text) {
		this.text = text;
		return this;
	}

}
