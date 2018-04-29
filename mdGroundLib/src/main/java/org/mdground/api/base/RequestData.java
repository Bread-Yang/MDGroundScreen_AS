package org.mdground.api.base;


public class RequestData {
	// / 当前使用的通信协议包的版本
	private String Version = "1.0";

	// / 文化/语言信息
	private String Culture;

	// / 业务码
	private int BusinessCode;

	// / 功能码
	private String FunctionName;

	// / 诊所ID
	private int ClinicID;

	// / 用户ID
	private int EmployeeID;

	// / 设备ID
	private int DeviceID;

	// / 发起请求平台
	private int Platform;

	// / 通讯Token
	private String ServiceToken = "";

	// / 请求时间（现在与1970.1.1的时间差，单位秒）
	private int ActionTimeSpan;

	// / 请求参数
	private String QueryData;

	// / 请求签名
	private String Sign;

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getCulture() {
		return Culture;
	}

	public void setCulture(String culture) {
		Culture = culture;
	}

	public int getBusinessCode() {
		return BusinessCode;
	}

	public void setBusinessCode(int businessCode) {
		BusinessCode = businessCode;
	}

	public String getFunctionName() {
		return FunctionName;
	}

	public void setFunctionName(String functionName) {
		FunctionName = functionName;
	}

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public int getEmployeeID() {
		return EmployeeID;
	}

	public void setEmployeeID(int employeeID) {
		EmployeeID = employeeID;
	}

	public int getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(int deviceID) {
		DeviceID = deviceID;
	}

	public int getPlatform() {
		return Platform;
	}

	public void setPlatform(int platform) {
		Platform = platform;
	}

	public String getServiceToken() {
		return ServiceToken;
	}

	public void setServiceToken(String serviceToken) {
		ServiceToken = serviceToken;
	}

	public int getActionTimeSpan() {
		return ActionTimeSpan;
	}

	public void setActionTimeSpan(int actionTimeSpan) {
		ActionTimeSpan = actionTimeSpan;
	}

	public String getQueryData() {
		return QueryData;
	}

	public void setQueryData(String queryData) {
		QueryData = queryData;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	@Override
	public String toString() {
		return "RequestData [Version=" + Version + ", Culture=" + Culture + ", BusinessCode=" + BusinessCode + ", FunctionName=" + FunctionName
				+ ", ClinicID=" + ClinicID + ", EmployeeID=" + EmployeeID + ", DeviceID=" + DeviceID + ", Platform=" + Platform + ", ServiceToken="
				+ ServiceToken + ", ActionTimeSpan=" + ActionTimeSpan + ", QueryData=" + QueryData + ", Sign=" + Sign + "]";
	}

}
