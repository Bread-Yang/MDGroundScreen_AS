package com.mdground.screen.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.mdground.screen.socket.Client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 接收数据服务
 * 
 * @author qinglong.huang
 * 
 */
public class DataService extends Service {
	private final static String TAG = "DataService";

	private ServerSocket serverSocket;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");

		new Thread() {

			public void run() {
				try {
					initSocket();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	private void initSocket() throws IOException {
		Log.i(TAG, "init Socket");
		if (serverSocket == null) {
			serverSocket = new ServerSocket(1212);
		}
		// ServerSocket ss = new ServerSocket(5555);
		System.out.println("Server已经启动。。。");
		while (true) {
			Socket so = serverSocket.accept();// 等待客户连接
			Log.i(TAG, "client connected : "+so.getRemoteSocketAddress());
			new Client(so, this).start();
		}
	}
}
