package org.mdground.api.dto;

/**
 * 服务器端返回，医生预约统计实体类
 * @author Vincent
 *
 */
public class DoctorAppointmentCount {
	private int ClinicID;
	private int DoctorID;
	private int FinishedCount;
	private int MissedCount;
	private int TotalCount;
	private int WaitingCount;

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public int getDoctorID() {
		return DoctorID;
	}

	public void setDoctorID(int doctorID) {
		DoctorID = doctorID;
	}

	public int getFinishedCount() {
		return FinishedCount;
	}

	public void setFinishedCount(int finishedCount) {
		FinishedCount = finishedCount;
	}

	public int getMissedCount() {
		return MissedCount;
	}

	public void setMissedCount(int missedCount) {
		MissedCount = missedCount;
	}

	public int getTotalCount() {
		return TotalCount;
	}

	public void setTotalCount(int totalCount) {
		TotalCount = totalCount;
	}

	public int getWaitingCount() {
		return WaitingCount;
	}

	public void setWaitingCount(int waitingCount) {
		WaitingCount = waitingCount;
	}

}
