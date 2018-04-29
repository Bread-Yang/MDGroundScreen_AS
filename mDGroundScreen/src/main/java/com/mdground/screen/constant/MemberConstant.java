package com.mdground.screen.constant;

/**
 * 成员常量，类中的常量。
 * 
 * @author Administrator
 * 
 */
public class MemberConstant {
	public static final int BASIC_INFO_CODE = 0;
	public static final int RECORD_INFO_CODE = 1;
	public static final int Medicine_INFO_CODE = 2;

	/** 控制滑动事件分发使用的常量 **/
	public static final String SHOW_FRAGMENT_PAGE = "showFragment";
	public static final String SHOW_LIST_COUNT = "showCount";

	/** 打开左侧菜单 **/
	public static final int OPEN_LEFT_MENU = 1001;

	public static final String ROLE_TYPE = "roleType";

	/** 医生角色 **/
	public static final String DOCTOR = "doctor";
	public static final String DOCTOR_ID = "doctorId";
	public static final String DOCTOR_ROLE = "doctorRole";
	public static final String DOCTOR_Name = "doctorName";
	public static final String DOCTOR_EMR = "doctorEmr";
	public static final String DOCTOR_LIST = "doctorList";
	/** 症状 */
	public static final String SYMPTOM_LIST = "symptomList";
	public static final String SYMPTOM_REMARK = "symptomRemark";

	/** 护士角色 **/
	public static final String NURSE_ROLE = "nurse";

	public static final String PATIENT = "patient";
	public static final String PATIENT_ID = "patient_id";
	public static final String PATIENT_NAME = "patientName";

	/**
	 * 找回密码，startActivityForResult中使用的参数
	 */
	public static final String PHONE = "phone";

	// 用户二维码
	public static final String EMPLOYEE_QRCODE = "employee_qrcode";
	public static final String EMPLOYEE_PHOTO_SID = "employee_photo_sid";
	public static final String EMPLOYEE_NAME = "employee_name";
	public static final String EMPLOYEE_CLINIC_NAME = "employee_clinic_name";
	public static final String EMPLOYEE_GENDER = "employee_gender";
	public static final String EMPLOYEE_UPDATE = "updateCode";
	public static final String EMPLOYEE_ID = "employee_id";
	public static final String EMPLOYEE_ROLE = "employee_role";
	
	// 医生候诊大厅

	public static final int APPIONTMENT_RESOULT_PASS = 2;// 过号

	public static final int APPIONTMENT_RESOULT_ASSIGN = 4;// 分配

	public static final String APPOINTMENT_STATUS = "appointment_status";
	public static final String APPOINTMENT_NO = "appointment_no";
	public static final String APPOINTMENT_ID = "appointment_id";
	public static final String APPOINTMENT = "appointment";

	// 登陆状态
	public static final String LOGIN_STATUS = "loginStatus";
	public static final int LOGIN_IN = 1;
	public static final int LOGIN_OUT = 2;
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	public static final int UPDATE_NAME = 1;
	public static final int UPDATE_ADDRESS = 2;
	public static final int UPDATE_SPECIALTY_NAME = 3;
	public static final int UPDATE_GRADUATE_SCHOOL = 4;
	public static final int UPDATE_RESUME = 5;
	public static final int UPDATE_BIRTHDAY = 6;
	public static final int UPDATE_SEX = 7;

	/** requestCode = 1 跳转到PersonEditInfoActivity **/
	public static final int JUMP_TO_PERSON = 20;

	/** requestCode = 2 跳转到PatientDetailInfoActivity **/
	public static final int JUMP_TO_PATIENT = 10;

	public static final String LOCATION_CONTURY_ID = "location_contury_id";
	public static final String LOCATION_PROVINCE_ID = "location_province_id";
	public static final String LOCATION_CITY_ID = "location_city_id";
	public static final String LOCATION_DISTRICT_ID = "location_district_id";
	public static final String LOCATION_ADDRESS = "location_address";
	public static final String LOCATION_STREET = "location_street";

	// 界面之间请求启动activity的请求code
	public static final int APPIONTMENT_REQUEST_CODE = 1;// 挂号请求
	public static final int APPIONTMENT_NEXT_REQUEST_CODE = 2;// 挂号请求
	public static final int APPIONTMENT_ASSIGN_REQUEST_CODE = 3;// 重新分配
	public static final int DOCTOR_REQUEST_CODE = 4;// 请求医生，在选择医生界面
	public static final int SYMPTOM_REQUEST_CODE = 5;// 请求症状，在症状界面
	public static final int PASSWORD_REQUEST_CODE = 6;// 请求修改密码界面
	public static final int PATIENT_REQUEST_CODE = 7;
	public static final int LOCATION_REQUEST_CODE = 8;
	public static final int EMPLOYEE_GENDER_REQUEST = 9;
	
	// 请求对应的结果
	public static final int APPIONTMENT_RESOULT_CODE = 1;// 预约成
	public static final int APPIONTMENT_NEXT_RESOULT_CODE = 2;// 下一个预约
	public static final int DOCTOR_RESOULT_CODE = 3;// 请求医生，在选择医生界面
	public static final int SYMPTOM_RESOULT_CODE = 4;// 请求症状，在症状界面
	public static final int PASSWORD_RESOULT_CODE = 5;// 请求修改密码界面
	public static final int PATIENT_RESOULT_CODE = 6;
	public static final int PATIENT_ADDRESS_RESOULT_CODE = 7;
	public static final int LOCATION_RESOULT_CODE = 8;
	
	
	public static final String PHOTO_ID = "photo_id";
	public static final String INCOME_TYPE = "income_type";
	
	public static final int INCOME_TODAY = 0;
	public static final int INCOME_TOTAL = 1;
	public static final int INCOME_WEEK = 2;
	public static final int INCOME_MONTH = 3;
	public static final int INCOME_TURN_OUT = 4;
	public static final int INCOME_BALANCE = 5;
	
	public static final String SCHEDULE_DATE = "schedule_date";
	
	//登录相关常量
	public static final String LOGIN_EMPLOYEE = "login_employee";
	public static final String DEVICE_ID = "device_id";
	
	
	

}
