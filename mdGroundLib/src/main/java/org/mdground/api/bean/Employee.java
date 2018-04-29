package org.mdground.api.bean;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;

public class Employee {
	public static final int DOCTOR = 1;
	public static final int NURSE = 2;
	public static final int Patient = 3;
	// / 客服人员
	public static final int Support = 4;
	// / 排班员
	public static final int Scheduling = 8;

	@Id
	public int EmployeeID;
	public int ClinicID;
	public String EmployeeCode;
	public String EmployeeName;
	public String LoginID;
	public String LoginPwd;

	public int EmployeeRole;

	public long PhotoID;

	public long PhotoSID;

	public int Gender;

	public Date DOB;

	public String IDCard;

	public String QRCode;

	public String EmployeeTitle;

	public String EMRType;

	public String GraduateSchool;

	public String SpecialtyCode;

	public String SpecialtyName;

	public boolean HasCertification;

	public String ZiGeNo;

	public String ZhiYeNo;

	public String ZhiYeType;

	public String ZhiYeRange;

	public Date ZhiYeIssuedTime;

	public int CountryID;

	public int ProvinceID;

	public int CityID;

	public int DistrictID;

	public String Street;

	public String Address;

	public String WorkPhone;

	public String WorkEmail;

	public String WorkWX;

	public String Resume;

	public float PrintPosTop;

	public float PrintPosLeft;

	public boolean NeedCosign;

	public boolean NeedResetPwd;

	public boolean NeedUpdateData;

	public int LoginFailNum;

	public Date UpdatedPwdTime;

	public Date LoginTime;

	public Date ExpiredTime;

	public boolean SystemLock;

	public int SystemTimeout;

	public int Status;

	public int UpdatedBy;

	public Date UpdatedTime;

	public int CreatedBy;

	public Date CreatedTime;

	public int DeviceID;

	public String ServiceToken;

	public String URL;

	public String ClinicName;

	public String SupportName;

	public String SupportPhone;

	public int getEmployeeID() {
		return EmployeeID;
	}

	public void setEmployeeID(int employeeID) {
		EmployeeID = employeeID;
	}

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public String getEmployeeCode() {
		return EmployeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		EmployeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return EmployeeName;
	}

	public void setEmployeeName(String employeeName) {
		EmployeeName = employeeName;
	}

	public String getLoginID() {
		return LoginID;
	}

	public void setLoginID(String loginID) {
		LoginID = loginID;
	}

	public String getLoginPwd() {
		return LoginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		LoginPwd = loginPwd;
	}

	public int getEmployeeRole() {
		return EmployeeRole;
	}

	public void setEmployeeRole(int employeeRole) {
		EmployeeRole = employeeRole;
	}

	public long getPhotoID() {
		return PhotoID;
	}

	public void setPhotoID(long photoID) {
		PhotoID = photoID;
	}

	public long getPhotoSID() {
		return PhotoSID;
	}

	public void setPhotoSID(long photoSID) {
		PhotoSID = photoSID;
	}

	public int getGender() {
		return Gender;
	}

	public void setGender(int gender) {
		Gender = gender;
	}

	public Date getDOB() {
		return DOB;
	}

	public void setDOB(Date dOB) {
		DOB = dOB;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public String getQRCode() {
		return QRCode;
	}

	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}

	public String getEmployeeTitle() {
		return EmployeeTitle;
	}

	public void setEmployeeTitle(String employeeTitle) {
		EmployeeTitle = employeeTitle;
	}

	public String getEMRType() {
		return EMRType;
	}

	public void setEMRType(String eMRType) {
		EMRType = eMRType;
	}

	public String getGraduateSchool() {
		return GraduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		GraduateSchool = graduateSchool;
	}

	public String getSpecialtyCode() {
		return SpecialtyCode;
	}

	public void setSpecialtyCode(String specialtyCode) {
		SpecialtyCode = specialtyCode;
	}

	public String getSpecialtyName() {
		return SpecialtyName;
	}

	public void setSpecialtyName(String specialtyName) {
		SpecialtyName = specialtyName;
	}

	public boolean isHasCertification() {
		return HasCertification;
	}

	public void setHasCertification(boolean hasCertification) {
		HasCertification = hasCertification;
	}

	public String getZiGeNo() {
		return ZiGeNo;
	}

	public void setZiGeNo(String ziGeNo) {
		ZiGeNo = ziGeNo;
	}

	public String getZhiYeNo() {
		return ZhiYeNo;
	}

	public void setZhiYeNo(String zhiYeNo) {
		ZhiYeNo = zhiYeNo;
	}

	public String getZhiYeType() {
		return ZhiYeType;
	}

	public void setZhiYeType(String zhiYeType) {
		ZhiYeType = zhiYeType;
	}

	public String getZhiYeRange() {
		return ZhiYeRange;
	}

	public void setZhiYeRange(String zhiYeRange) {
		ZhiYeRange = zhiYeRange;
	}

	public Date getZhiYeIssuedTime() {
		return ZhiYeIssuedTime;
	}

	public void setZhiYeIssuedTime(Date zhiYeIssuedTime) {
		ZhiYeIssuedTime = zhiYeIssuedTime;
	}

	public int getCountryID() {
		return CountryID;
	}

	public void setCountryID(int countryID) {
		CountryID = countryID;
	}

	public int getProvinceID() {
		return ProvinceID;
	}

	public void setProvinceID(int provinceID) {
		ProvinceID = provinceID;
	}

	public int getCityID() {
		return CityID;
	}

	public void setCityID(int cityID) {
		CityID = cityID;
	}

	public int getDistrictID() {
		return DistrictID;
	}

	public void setDistrictID(int districtID) {
		DistrictID = districtID;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getWorkPhone() {
		return WorkPhone;
	}

	public void setWorkPhone(String workPhone) {
		WorkPhone = workPhone;
	}

	public String getWorkEmail() {
		return WorkEmail;
	}

	public void setWorkEmail(String workEmail) {
		WorkEmail = workEmail;
	}

	public String getWorkWX() {
		return WorkWX;
	}

	public void setWorkWX(String workWX) {
		WorkWX = workWX;
	}

	public String getResume() {
		return Resume;
	}

	public void setResume(String resume) {
		Resume = resume;
	}

	public float getPrintPosTop() {
		return PrintPosTop;
	}

	public void setPrintPosTop(float printPosTop) {
		PrintPosTop = printPosTop;
	}

	public float getPrintPosLeft() {
		return PrintPosLeft;
	}

	public void setPrintPosLeft(float printPosLeft) {
		PrintPosLeft = printPosLeft;
	}

	public boolean isNeedCosign() {
		return NeedCosign;
	}

	public void setNeedCosign(boolean needCosign) {
		NeedCosign = needCosign;
	}

	public boolean isNeedResetPwd() {
		return NeedResetPwd;
	}

	public void setNeedResetPwd(boolean needResetPwd) {
		NeedResetPwd = needResetPwd;
	}

	public boolean isNeedUpdateData() {
		return NeedUpdateData;
	}

	public void setNeedUpdateData(boolean needUpdateData) {
		NeedUpdateData = needUpdateData;
	}

	public int getLoginFailNum() {
		return LoginFailNum;
	}

	public void setLoginFailNum(int loginFailNum) {
		LoginFailNum = loginFailNum;
	}

	public Date getUpdatedPwdTime() {
		return UpdatedPwdTime;
	}

	public void setUpdatedPwdTime(Date updatedPwdTime) {
		UpdatedPwdTime = updatedPwdTime;
	}

	public Date getLoginTime() {
		return LoginTime;
	}

	public void setLoginTime(Date loginTime) {
		LoginTime = loginTime;
	}

	public Date getExpiredTime() {
		return ExpiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		ExpiredTime = expiredTime;
	}

	public boolean isSystemLock() {
		return SystemLock;
	}

	public void setSystemLock(boolean systemLock) {
		SystemLock = systemLock;
	}

	public int getSystemTimeout() {
		return SystemTimeout;
	}

	public void setSystemTimeout(int systemTimeout) {
		SystemTimeout = systemTimeout;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getUpdatedBy() {
		return UpdatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		UpdatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return UpdatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		UpdatedTime = updatedTime;
	}

	public int getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(int createdBy) {
		CreatedBy = createdBy;
	}

	public Date getCreatedTime() {
		return CreatedTime;
	}

	public void setCreatedTime(Date createdTime) {
		CreatedTime = createdTime;
	}

	public int getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(int deviceID) {
		DeviceID = deviceID;
	}

	public String getServiceToken() {
		return ServiceToken;
	}

	public void setServiceToken(String serviceToken) {
		ServiceToken = serviceToken;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getClinicName() {
		return ClinicName;
	}

	public void setClinicName(String clinicName) {
		ClinicName = clinicName;
	}

	public String getSupportName() {
		return SupportName;
	}

	public void setSupportName(String supportName) {
		SupportName = supportName;
	}

	public String getSupportPhone() {
		return SupportPhone;
	}

	public void setSupportPhone(String supportPhone) {
		SupportPhone = supportPhone;
	}

	public String getGenderStr() {
		if (Gender == 1) {
			return "男";
		} else {
			return "女";
		}
	}

	public String getPhotoUrl() {
		return "https://file.yideguan.com/View/GetFile.aspx?FileID=" + PhotoSID
				+ "&ClinicID=" + ClinicID;
		// return "image://" + ClinicID + "." + PhotoSID;
	}

}
