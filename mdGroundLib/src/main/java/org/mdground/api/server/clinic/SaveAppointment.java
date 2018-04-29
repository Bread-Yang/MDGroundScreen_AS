package org.mdground.api.server.clinic;

import org.mdground.api.base.ClinicRequest;
import org.mdground.api.base.RequestCallBack;
import org.mdground.api.base.RequestData;
import org.mdground.api.bean.Appointment;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveAppointment extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveAppointment";

	public SaveAppointment(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	// 保存预约
	public void saveAppointment(Appointment appointment, RequestCallBack callBack) {
		if (appointment == null) {
			return;
		}

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String query = gson.toJson(appointment);

		RequestData data = getData();
		data.setQueryData(query);

		pocess();
	}

	/*protected class Appointment {
		private int DoctorID;
		private String DoctorName;
		private int PatientID;
		private Date AppointmentDate;
		private int AppointmentStatus;
		private int AppointmentType;
		private String Remark;;
		private String AppointmentEMR;
		private float Lat;
		private float Lng;
		private int ClinicID;
		private int CreatedBy;
		private int CreatedRole;

		public int getDoctorID() {
			return DoctorID;
		}

		public void setDoctorID(int doctorID) {
			DoctorID = doctorID;
		}

		public String getDoctorName() {
			return DoctorName;
		}

		public void setDoctorName(String doctorName) {
			DoctorName = doctorName;
		}

		public int getPatientID() {
			return PatientID;
		}

		public void setPatientID(int patientID) {
			PatientID = patientID;
		}

		public Date getAppointmentDate() {
			return AppointmentDate;
		}

		public void setAppointmentDate(Date appointmentDate) {
			AppointmentDate = appointmentDate;
		}

		public int getAppointmentStatus() {
			return AppointmentStatus;
		}

		public void setAppointmentStatus(int appointmentStatus) {
			AppointmentStatus = appointmentStatus;
		}

		public int getAppointmentType() {
			return AppointmentType;
		}

		public void setAppointmentType(int appointmentType) {
			AppointmentType = appointmentType;
		}

		public String getRemark() {
			return Remark;
		}

		public void setRemark(String remark) {
			Remark = remark;
		}

		public String getAppointmentEMR() {
			return AppointmentEMR;
		}

		public void setAppointmentEMR(String appointmentEMR) {
			AppointmentEMR = appointmentEMR;
		}

		public float getLat() {
			return Lat;
		}

		public void setLat(float lat) {
			Lat = lat;
		}

		public float getLng() {
			return Lng;
		}

		public void setLng(float lng) {
			Lng = lng;
		}

		public int getClinicID() {
			return ClinicID;
		}

		public void setClinicID(int clinicID) {
			ClinicID = clinicID;
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

	}*/
}
