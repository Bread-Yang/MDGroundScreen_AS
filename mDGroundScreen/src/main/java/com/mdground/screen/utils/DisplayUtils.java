package com.mdground.screen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.mdground.screen.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public class DisplayUtils {

	
	public static int getScreenHeightDip(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();
		int screenHeight = metric.densityDpi;
		return screenHeight;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();
		int screenHeight = metric.heightPixels;
		return screenHeight;
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();
		int screenWidth = metric.widthPixels;
		return screenWidth;
	}

	public static int getScreenWidthInDip(Context context)
	{
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);

		int SCREEN_WIDTH_PIXELS;
		int SCREEN_WIDTH_DP;
		//这里我们只关心竖屏状态下的值
		SCREEN_WIDTH_PIXELS = dm.widthPixels;
		SCREEN_WIDTH_DP = (int) (SCREEN_WIDTH_PIXELS / dm.density);

		return SCREEN_WIDTH_DP;
	}

	public static int getScreenHeightInDip(Context context)
	{
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);

		int SCREEN_HEIGHT_PIXELS;
		int SCREEN_HEIGHT_DP;
		//这里我们只关心竖屏状态下的值
		SCREEN_HEIGHT_PIXELS = dm.heightPixels;
		SCREEN_HEIGHT_DP = (int) (SCREEN_HEIGHT_PIXELS / dm.density);

		return SCREEN_HEIGHT_DP;
	}


	public static int dp2Px(Context context, int dp){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
		return px;
	}

	public static int sp2Px(Context context, int sp){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, dm);
		return px;
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变（有精度损失）
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变（无精度损失）
	 */
	public static float px2dipByFloat(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxValue / scale);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变（有精度损失），类似Context.getDimensionPixelSize方法（四舍五入）
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变（无精度损失），类似Context.getDimension方法
	 */
	public static float dip2pxByFloat(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dipValue * scale);
	}

	/**
	 * 设置状态栏全屏透明（状态栏字体颜色为默认）
	 * */
	public static void setStatusBarFullTranslucent(Activity act) {
		//设置全屏透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			act.getWindow().setStatusBarColor(Color.TRANSPARENT);
		}else{
			setStatusBarColor(act,R.color.background_color);
		}
	}

	/**
	 * 设置状态栏颜色
	 * */
	public static void setStatusBarColor(Activity activity, int colorResId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(activity.getResources().getColor(colorResId));
		}

	}



	/**
	 * 将px值转换为sp值，保证文字大小不变
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 屏幕宽度
	 */
	public static int getScreenWidthPixels(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 屏幕高度
	 */
	public static int getScreenHeightPixels(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static String getDisplayInfo(Context context) {
		String infoFormat = "宽:%s,高:%s 宽Dip:%s,高Dip:%s\ndpi:%s,1dip=%sPixels";
		int screenWidthPixels = DisplayUtils.getScreenWidthPixels(context);
		int screenHeightPixels = DisplayUtils.getScreenHeightPixels(context);
		float density = context.getResources().getDisplayMetrics().density;
		Object[] infoFormatValue = {
				screenWidthPixels, screenHeightPixels,
				((int) (screenWidthPixels / density)), ((int) (screenHeightPixels / density)),
				context.getResources().getDisplayMetrics().densityDpi, density
		};
		return String.format(Locale.getDefault(), infoFormat, infoFormatValue);
	}





	/**
	 * 获取状态栏的高度
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context){
		return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
	}
	/**
	 * 获取ActionBar的高度
	 * @param context
	 * @return
	 */
	public static int getActionBarHeight(Context context){
		TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] {
				android.R.attr.actionBarSize
		});

		return (int) actionbarSizeTypedArray.getDimension(0, 0);
	}

	public static void HiddenStateBar(Activity activity, boolean hasFocus) {
		//判断是否有焦点
		if(hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			View decorView = activity.getWindow().getDecorView();
			decorView.setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
			);

		}
	}
}
