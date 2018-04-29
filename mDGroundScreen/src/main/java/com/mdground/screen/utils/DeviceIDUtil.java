package com.mdground.screen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.os.Environment;

public class DeviceIDUtil {

	private static String fileName = "YiDeGuanScreen.txt";
	private static String xgpushTokenFileName = "YiDeGuanScreenToken.txt";

	public int getDeviceID() {
		File file = new File(Environment.getExternalStorageDirectory(),
				fileName);
		if (!file.exists()) {
			L.e(DeviceIDUtil.this, "文件不存在");
			return -1;
		} else {
			try {

				FileInputStream is = new FileInputStream(file);
				byte[] b = new byte[is.available()];
				is.read(b);
				String result = new String(b);
				L.e(DeviceIDUtil.this, "读取dviceID成功 : deviceID是 : " + result);
				return Integer.valueOf(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;
		}
	}

	public void saveDeviceIDToSDCard(int deviceID) {
		File file = new File(Environment.getExternalStorageDirectory(),
				fileName);
		try {
			OutputStream out;
			out = new FileOutputStream(file, false); 
			String content = String.valueOf(deviceID);
			out.write(content.getBytes());
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void saveXgPushTokenToSDCard(String token) {
		File file = new File(Environment.getExternalStorageDirectory(),
				xgpushTokenFileName);
		try {
			OutputStream out;
			out = new FileOutputStream(file, false); 
			out.write(token.getBytes());
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
