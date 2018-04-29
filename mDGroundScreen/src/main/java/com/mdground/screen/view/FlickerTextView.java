package com.mdground.screen.view;

import java.util.Timer;
import java.util.TimerTask;

import com.mdground.screen.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class FlickerTextView extends TextView {
	private static final String LOG_TAG = "FlickerTextView";
	boolean change = false;
	private Handler handler = null;
	private Timer timer;

	public FlickerTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlickerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlickerTextView(Context context) {
		super(context);
	}

	public void startFlicker(String message) {
		setVisibility(View.VISIBLE);
		setText(message);
		handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				if (change) {
					change = false;
					setTextColor(getResources().getColor(R.color.font_dark));
				} else {
					change = true;
					setTextColor(getResources().getColor(R.color.font_blue));
				}
			}
		};

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		};
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(task, 1, 300); // 参数分别是delay（多长时间后执行），duration（执行间隔）
	}

	public void stopFlicker() {
		if (timer != null) {
			timer.cancel();
		}
		setTextColor(getResources().getColor(R.color.font_dark));
	}
}