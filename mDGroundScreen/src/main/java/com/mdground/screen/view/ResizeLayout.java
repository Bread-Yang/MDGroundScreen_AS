package com.mdground.screen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 软键盘弹出增加size回调
 * 注:需要设置android:windowSoftInputMode="adjustResize"
 * @author luojianhui
 *
 */
public class ResizeLayout extends RelativeLayout {

	private OnResizeListener mListener;

	public interface OnResizeListener {
		void OnResize(int w, int h, int oldw, int oldh);
	}

	public void setOnResizeListener(OnResizeListener listener) {
		mListener = listener;
	}

	public ResizeLayout(Context context) {
		super(context);
	}

	public ResizeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ResizeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
	}

}
