package com.mdground.screen.view;

import java.util.Timer;
import java.util.TimerTask;

import com.mdground.screen.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class FlickerTextView extends AppCompatTextView {
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

	@SuppressLint("HandlerLeak")
	public void startFlicker(final String message) {
		setVisibility(View.VISIBLE);

		handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				if (change) {
					change = false;
					setTextColor(getResources().getColor(R.color.white));
					setText(message);
				} else {
					change = true;
					setTextColor(getResources().getColor(R.color.font_blue));
					setText(message);
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
		setTextColor(getResources().getColor(R.color.white));
	}
}