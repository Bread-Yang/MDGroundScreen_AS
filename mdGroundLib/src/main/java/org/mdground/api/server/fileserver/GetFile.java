package org.mdground.api.server.fileserver;

import org.mdground.api.base.FileServerRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;
import org.mdground.api.base.ResponseData;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

public class GetFile extends FileServerRequest {
	public static final String FUNCTION_NAME = "GetFile";

	public GetFile(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	// 同步调用
	public ResponseData getFile(int clinicID, long fileID) {
		GetFileQuery query = new GetFileQuery();
		query.setClinicID(clinicID);
		query.setFileID(fileID);

		Gson gons = new Gson();
		String queryJson = gons.toJson(query);

		RequestData data = getData();
		data.setQueryData(queryJson);
		
		return pocessSync(false);
	}

	// 异步调用
	public void getFile(int clinicID, long fileID, RequestCallBack callBack) {

		setRequestCallBack(callBack);

		GetFileQuery query = new GetFileQuery();
		query.setClinicID(clinicID);
		query.setFileID(fileID);

		Gson gons = new Gson();
		String queryJson = gons.toJson(query);

		RequestData data = getData();
		data.setQueryData(queryJson);
		
		pocess(false);
	}

	class GetFileQuery {
		private int ClinicID;
		private long FileID;

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

	}

}
