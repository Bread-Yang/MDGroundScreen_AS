package com.mdground.screen.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

import com.mdground.screen.MedicalAppliction;
import com.mdground.screen.constant.MemberConstant;
import com.mdground.screen.utils.L;
import com.mdground.screen.utils.PreferenceUtils;

import android.content.Context;
import android.content.Intent;

public class Client extends Thread {

	private static final int BUFFER_SIZE = 4 * 1024;

	private Socket socket;
	private Context mContext;
	byte[] buffer;

	private BufferedInputStream mBufferInputStream = null;
	private BufferedOutputStream mBufferedOutputStream = null;

	public Client(Socket so, Context context) {
		this.socket = so;
		this.mContext = context;
		buffer = new byte[BUFFER_SIZE];
	}

	public void run() {
		try {
			if (mBufferInputStream == null) {
				mBufferInputStream = new BufferedInputStream(
						socket.getInputStream());
			}

			if (mBufferedOutputStream == null) {
				mBufferedOutputStream = new BufferedOutputStream(
						socket.getOutputStream());
			}

			while (mBufferInputStream.read(buffer) != -1) {
				sendMessage(new String(buffer, "utf-8"));

				JSONObject json = new JSONObject(new String(buffer, "utf-8"));

				int action = json.getInt("Action");

				if (action == 3) {
					int clinicID = json.getInt("ClinicID");

					boolean isRightClinic = MedicalAppliction.employee
							.getClinicID() == clinicID;
					
					// 没有登录的时候返回false
					int loginStatus = PreferenceUtils.getPrefInt(mContext, MemberConstant.LOGIN_STATUS, MemberConstant.LOGIN_OUT);
					
					if (loginStatus == MemberConstant.LOGIN_OUT) {
						isRightClinic = false;
					}

					L.e(Client.this, "json : clinicID" + clinicID);
					L.e(Client.this, "get clinicID"
							+ MedicalAppliction.employee.getClinicID());

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("Action", 3);
					jsonObject.put("Valid", isRightClinic);

					L.e(Client.this, "jsonString" + jsonObject.toString());

					mBufferedOutputStream.write(jsonObject.toString().getBytes());
					mBufferedOutputStream.flush();
				} else  {
					mBufferedOutputStream.write(buffer);
					mBufferedOutputStream.flush();
				}
			}

		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			try {
				if (mBufferInputStream != null) {
					mBufferInputStream.close();
				}

				if (mBufferedOutputStream != null) {
					mBufferedOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendMessage(String message) {
		Intent intent = new Intent("com.mdground.message");
		intent.putExtra("message", message);
		mContext.sendBroadcast(intent);
	}
}