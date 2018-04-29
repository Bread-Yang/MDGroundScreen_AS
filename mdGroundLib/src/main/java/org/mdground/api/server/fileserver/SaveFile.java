package org.mdground.api.server.fileserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.mdground.api.base.FileServerRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

public class SaveFile extends FileServerRequest {
	public static final String FUNCTION_NAME = "SaveFile";

	public SaveFile(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveFile(int clinicID, int employeeID, int employeeRole, File file, long fileID, RequestCallBack callBack) {
		setRequestCallBack(callBack);
		if (file == null) {
			return;
		}

		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			Log.e(TAG, "file too big...");
			return;
		}

		byte[] buffer = null;
		FileInputStream in = null;
		try {
			// 一次读多个字节
			in = new FileInputStream(file);
			buffer = new byte[(int) fileSize];
			int offset = 0;
			int numRead = 0;

			while (offset < buffer.length && (numRead = in.read(buffer, offset, buffer.length - offset)) >= 0) {
				offset += numRead;
			}
			// 确保所有数据均被读取
			if (offset != buffer.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (buffer == null || buffer.length == 0) {
			Log.e(TAG, "read file failed");
			return;
		}

		String fileName = file.getName();
		String fileExt = fileName.substring(fileName.lastIndexOf(".", fileName.length()));

		String dataStr = new String(Base64.encode(buffer, Base64.DEFAULT));
		try {
			dataStr = URLEncoder.encode(dataStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		SaveFileQuery query = new SaveFileQuery();
		query.setClinicID(clinicID);
		query.setCreatedBy(employeeID);
		query.setCreatedRole(employeeRole);
		query.setData(dataStr);
		query.setFileExt(fileExt);
		query.setFileID(fileID);
		query.setFileName(fileName);

		Gson gson = new Gson();
		String queryJson = gson.toJson(query);
		RequestData data = getData();
		data.setQueryData(queryJson);

		pocess(false);
	}

	protected class SaveFileQuery {
		private int ClinicID;
		private long FileID;
		private String FileName;
		private String FileExt;
		private int CreatedBy;
		private int CreatedRole;
		private String Data;

		public int getClinicID() {
			return ClinicID;
		}

		public void setClinicID(int clinicID) {
			ClinicID = clinicID;
		}

		public long getFileID() {
			return FileID;
		}

		public void setFileID(long fileID) {
			FileID = fileID;
		}

		public String getFileName() {
			return FileName;
		}

		public void setFileName(String fileName) {
			FileName = fileName;
		}

		public String getFileExt() {
			return FileExt;
		}

		public void setFileExt(String fileExt) {
			FileExt = fileExt;
		}

		public int getCreatedBy() {
			return CreatedBy;
		}

		public void setCreatedBy(int createdBy) {
			CreatedBy = createdBy;
		}

		public int getCreatedRole() {
			return CreatedRole;
		}

		public void setCreatedRole(int createdRole) {
			CreatedRole = createdRole;
		}

		public String getData() {
			return Data;
		}

		public void setData(String data) {
			Data = data;
		}

	}

}
