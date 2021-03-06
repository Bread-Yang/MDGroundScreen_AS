package org.mdground.api.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.mdground.api.utils.GsonUtils;

public class ResponseData {
	public  static String CurrentPostString;
	public  static String CurrentEncryptPostString;

	// / 响应编码
	public int Code;

	// / 响应信息
	public String Message;

	// / 响应结果
	public String Content;

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public <T> T getContent(Class<? extends T> clazz) {
		Gson gson = GsonUtils.getGson();
		if (Content == null) {
			return null;
		} else {
			return gson.fromJson(Content, clazz);
		}
	}

	public <T> T getContent(TypeToken<T> type) {
		if (type == null) {
			return null;
		}
		Gson gson = GsonUtils.getGson();
		if (Content == null) {
			return null;
		} else {
			return gson.fromJson(Content, type.getType());
		}
	}
}
