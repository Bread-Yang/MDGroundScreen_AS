package com.mdground.screen.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.mdground.api.base.ResponseCode;
import org.mdground.api.base.ResponseData;
import org.mdground.api.server.fileserver.GetFile;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

/**
 * 图片下载类
 * 
 * @author Vincent
 * 
 */
public class MedicalImageDownload extends BaseImageDownloader {

	private static final String TAG = "MedicalImageDownload";
	private static final String uriPrefix = "image://";

	public MedicalImageDownload(Context context, int connectTimeout, int readTimeout) {
		super(context, connectTimeout, readTimeout);
	}

	public MedicalImageDownload(Context context) {
		super(context);
	}

	@Override
	protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
		if (imageUri.startsWith(uriPrefix)) {
			return getStreamFromSoap(imageUri, extra);
		}

		return super.getStreamFromOtherSource(imageUri, extra);
	}

	private InputStream getStreamFromSoap(String imageUri, Object extra) {
		String url = imageUri.substring(uriPrefix.length());
		if (url == null || url.equals("")) {
			Log.e(TAG, "imageUri is error ! url = " + imageUri);
			return null;
		}

		String data[] = url.split("\\.");
		if (data.length < 2) {
			Log.e(TAG, "imageUri is error ! url = " + imageUri);
			return null;
		}

		int clinicID = Integer.parseInt(data[0]);
		int fileID = Integer.parseInt(data[1]);

		if (fileID <= 0) {
			Log.e(TAG, "fileId can't be small than 0");
			return null;
		}

		GetFile getFile = new GetFile(context);
		ResponseData responseData = getFile.getFile(clinicID, fileID);
		if (responseData == null) {
			Log.e(TAG, "request failed");
			return null;
		}

		if (responseData.getCode() == ResponseCode.Normal.getValue()) {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(responseData.getContent(), Base64.DEFAULT);
			return new ByteArrayInputStream(bitmapArray);
		} else {
			Log.e(TAG,
					"Download failed : url = " + imageUri + " responseCode = " + responseData.getCode() + " message = " + responseData.getMessage());
			return null;
		}

	}
}
