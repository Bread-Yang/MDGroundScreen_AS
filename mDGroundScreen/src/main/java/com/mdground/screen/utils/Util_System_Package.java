package com.mdground.screen.utils;

import java.io.File;

import com.mdground.screen.utils.ShellUtils.CommandResult;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

public class Util_System_Package {
	
	public static final int APP_INSTALL_AUTO = 0;
	public static final int APP_INSTALL_INTERNAL = 1;
	public static final int APP_INSTALL_EXTERNAL = 2;
	
	
	public static boolean isPakcageInstall(Context context, String packageName) {
		try {
			if (packageName == null) {
				return false;
			}

			PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
			if (pi != null) {
				return true;
			}

		} catch (Throwable e) {
			
		}
		return false;

	}
	
	public static boolean isSystemApplication(Context context) {
		if (context == null) {
			return false;
		}
		return isSystemApplication(context.getPackageManager(), context.getPackageName());
	}
	
	public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
		if (packageManager == null || packageName == null || packageName.length() == 0) {
			return false;
		}

		try {
			ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
			return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Intent getInstallApkIntentByApkFilePath(Context context, String filePath) {
		try {
			if (filePath == null) {
				return null;
			}
			File file = new File(filePath);
			if (!file.exists()) {
				return null;
			}

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			return intent;
		} catch (Throwable e) {
			
		}
		return null;
	}
	
	public static void InstallApkByFilePath(Context context, String filePath) {
		if (filePath == null) {
			return;
		}
		Log.e("bao", "111111" );
		try {
			boolean installSlient = installSlient(context, filePath);
			if (!installSlient) {
				Log.e("noSilent", "noroot");
				Intent intent = getInstallApkIntentByApkFilePath(context, filePath);
				if (intent != null) {
					context.startActivity(intent);
				}
			} else {
				Log.e("Silent", "root");
			}
		} catch (Throwable e) {
		
		}

	}
	
	
	
	
	/**
	 * 静默安装，root或者系统软件时选择使用静默安装
	 * 
	 * @param context
	 * @param filePath
	 * @return 0 表示正常, 1 表示, 2 means other exception error
	 */
	public static boolean installSlient(Context context, String apkPath) {
		Log.e("bao", "222222" );
		try {
			String pmParams = " -r " + getInstallLocationParams();
			Log.e("bao", "000000" );
			StringBuilder command = new StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install ")
					.append(pmParams == null ? "" : pmParams).append(" ").append(apkPath.replace(" ", "\\ "));
			boolean isSysApp = isSystemApplication(context);
			Log.e("bao", "23232323" );
			CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSysApp, true);
			Log.e("bao", "333" );
			
			Log.e("bao", "是否是系统应用 : "+ isSysApp);
			Log.e("bao", "是否root : "+  ShellUtils.checkRootPermission());
			
			
			if (commandResult.successMsg != null
					&& (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
				Log.e("bao", "4444" );
				return true;
			} else {
				boolean isRoot = ShellUtils.checkRootPermission(); 
				if (isSysApp && isRoot) {
					Log.e("bao", "5555" );
					commandResult = ShellUtils.execCommand(command.toString(), true, true);
					if (commandResult.successMsg != null
							&& (commandResult.successMsg.contains("Success") || commandResult.successMsg
									.contains("success"))) {
						return true;
					}
				}
				Log.e("bao", "66666" );
				return false;
			}  
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}
	

	public static int getInstallLocation() {
		CommandResult commandResult = ShellUtils.execCommand(
				"LD_LIBRARY_PATH=/vendor/lib:/system/lib pm get-install-location", false, true);
		if (commandResult.result == 0 && commandResult.successMsg != null && commandResult.successMsg.length() > 0) {
			try {
				int location = Integer.parseInt(commandResult.successMsg.substring(0, 1));
				switch (location) {
				case APP_INSTALL_INTERNAL:
					return APP_INSTALL_INTERNAL;
				case APP_INSTALL_EXTERNAL:
					return APP_INSTALL_EXTERNAL;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				Log.e("youmi", "pm get-install-location error");
			}
		}
		return APP_INSTALL_AUTO;
	}
	
	private static String getInstallLocationParams() {
		int location = getInstallLocation();
		switch (location) {
		case APP_INSTALL_INTERNAL:
			return "-f";
		case APP_INSTALL_EXTERNAL:
			return "-s";
		}
		return "";
	}

}
