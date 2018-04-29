package org.mdground.api.bean;

import java.util.Date;

import org.mdground.api.utils.DateUtils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 预约信息 /// <summary> /// 等待支付 /// </summary> NotPaid = 1,
 * 
 * /// <summary> /// 成功预约 /// </summary> HasPaid = 2,
 * 
 * /// <summary> /// 正在候诊（医生端/护士端挂号则直接进入此阶段） /// </summary> Waiting = 4,
 * 
 * /// <summary> /// 就诊中 /// </summary> Working = 8,
 * 
 * /// <summary> /// 完成预约 /// </summary> Finished = 16,
 * 
 * /// <summary> /// 取消预约 /// </summary> Cancel = 32,
 * 
 * /// <summary> /// 预约过号（支持重新排队） /// </summary> Missed = 64,
 * 
 * /// <summary> /// 预约过期（不支持重新排队） /// </summary> Expired = 128,
 * 
 * /// <summary> /// 重新排队 /// </summary> ReWaiting = 256
 * 
 * @author Vincent
 * 
 */
public class Appointment implements Parcelable, Comparable<Appointment> {

	/* 等待支付 */
	public static final int STATUS_NOT_PAID = 1;
	// / 成功预约
	public static final int STATUS_HAS_PAID = 2;
	/* 候诊 */
	public static final int STATUS_WATTING = 4;
	/*
	 * 就诊中 请求的数据为，已支付就诊中的状态
	 */
	public static final int STATUS_DIAGNOSING = 8;
	/* 已诊断 */
	public static final int STATUS_FINISH = 16;
	// / 取消预约
	public static final int STATUS_CANCEL = 32;
	// / 预约过号（支持重新排队）
	public static final int STATUS_PASSED = 64;
	// / 预约过期（不支持重新排队）
	public static final int STATUS_INVALID = 128;
	// / 重新排队
	public static final int STATUS_REAPPOINT = 256;

	@SerializedName("OPNo")
	@Expose
	private int OPNo;

	@SerializedName("OPStatus")
	@Expose
	private int OPStatus;
	
	@SerializedName("OPType")
	@Expose
	private int OPType;

	@SerializedName("ClinicID")
	@Expose
	private int ClinicID;

	@SerializedName("DoctorID")
	@Expose
	private int DoctorID;
	
	@SerializedName("Emergency")
	@Expose
	private boolean Emergency;

	@SerializedName("OPID")
	@Expose
	private int OPID;//

	@SerializedName("PatientDOB")
	@Expose
	private Date PatientDOB;// 病人生日

	@SerializedName("PatientGender")
	@Expose
	private int PatientGender = 1;// 默认为男

	@SerializedName("PatientID")
	@Expose
	private int PatientID;

	@SerializedName("PatientName")
	@Expose
	private String PatientName;

	@SerializedName("Remark")
	@Expose
	private String Remark = "";// 症状类型

	@SerializedName("WaitingCount")
	@Expose
	private long WaitingCount;

	@SerializedName("OPDate")
	@Expose
	private String OPDate;

	@SerializedName("CreateTime")
	@Expose
	private String CreateTime;

	@SerializedName("DoctorName")
	@Expose
	private String DoctorName;

	@SerializedName("OPDatePeriod")
	@Expose
	private int OPDatePeriod;

	// 预约医生的电子专业
	private String OPEMR;

	public Appointment() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 根据病人出生计算
	 * 
	 * @return
	 */
	public String getPatientAge() {
		int day = DateUtils.daysBetween(PatientDOB);
		if (day == 0) {
			return "今天";
		} else {
			if ((day / 365) != 0) {
				return DateUtils.calcAgeByBrithday(PatientDOB) + "岁";
			} else {
				return Math.abs(day % 365) + "天";
			}
		}
	}

	public String getPatientGenderStr() {
		if (PatientGender == 1) {
			return "男";
		} else if (PatientGender == 2) {
			return "女";
		}

		return "";
	}

	public String getTaStr() {
		if (PatientGender == 1) {
			return "他";
		} else if (PatientGender == 2) {
			return "她";
		}

		return "未知";
	}

	public Integer getOPNo() {
		return OPNo;
	}

	public void setOPNo(int oPNo) {
		OPNo = oPNo;
	}

	public int getOPStatus() {
		return OPStatus;
	}

	public void setOPStatus(int oPStatus) {
		OPStatus = oPStatus;
	}

	public int getOPType() {
		return OPType;
	}

	public void setOPType(int oPType) {
		OPType = oPType;
	}

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
	
	public boolean isEmergency() {
		return Emergency;
	}

	public void setEmergency(boolean emergency) {
		Emergency = emergency;
	}

	public int getOPID() {
		return OPID;
	}

	public void setOPID(int oPID) {
		OPID = oPID;
	}

	public Date getPatientDOB() {
		return PatientDOB;
	}

	public void setPatientDOB(Date patientDOB) {
		PatientDOB = patientDOB;
	}

	public int getPatientGender() {
		return PatientGender;
	}

	public void setPatientGender(int patientGender) {
		PatientGender = patientGender;
	}

	public int getPatientID() {
		return PatientID;
	}

	public void setPatientID(int patientID) {
		PatientID = patientID;
	}

	public String getPatientName() {
		return PatientName;
	}

	public void setPatientName(String patientName) {
		PatientName = patientName;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public long getWaitingCount() {
		return WaitingCount;
	}

	public void setWaitingCount(long waitingCount) {
		WaitingCount = waitingCount;
	}

	public String getOPDate() {
		return OPDate;
	}

	public void setOPDate(String oPDate) {
		OPDate = oPDate;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getDoctorName() {
		return DoctorName;
	}

	public void setDoctorName(String doctorName) {
		DoctorName = doctorName;
	}

	public int getOPDatePeriod() {
		return OPDatePeriod;
	}

	public void setOPDatePeriod(int oPDatePeriod) {
		OPDatePeriod = oPDatePeriod;
	}

	public String getOPEMR() {
		return OPEMR;
	}

	public void setOPEMR(String oPEMR) {
		OPEMR = oPEMR;
	}

	@Override
	public String toString() {
		return "AppointmentInfo [AppointmentNo=" + OPNo
				+ ", AppointmentStatus=" + OPStatus + ", ClinicID=" + ClinicID
				+ ", DoctorID=" + DoctorID + ", OPID=" + OPID + ", PatientDOB="
				+ PatientDOB + ", PatientGender=" + PatientGender
				+ ", PatientID=" + PatientID + ", PatientName=" + PatientName
				+ ", Remark=" + Remark + ", WaitingCount=" + WaitingCount
				+ ", AppointmentDate=" + OPDate + ", CreateTime=" + CreateTime
				+ ", DoctorName=" + DoctorName + "]";
	}

	public Appointment(Parcel source) {
		this.OPNo = source.readInt();
		this.OPStatus = source.readInt();
		this.OPType = source.readInt();
		this.ClinicID = source.readInt();
		this.DoctorID = source.readInt();
		this.Emergency = (source.readInt() == 0) ? false : true;
		this.OPID = source.readInt();
		long tmpDOB = source.readLong();
		this.PatientDOB = tmpDOB == -1 ? null : new Date(tmpDOB);
		this.PatientGender = source.readInt();
		this.PatientID = source.readInt();
		this.PatientName = source.readString();
		this.Remark = source.readString();
		this.WaitingCount = source.readLong();
		this.OPDate = source.readString();
		this.CreateTime = source.readString();
		this.DoctorName = source.readString();
		this.OPEMR = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(OPNo);
		dest.writeInt(OPStatus);
		dest.writeInt(OPType);
		dest.writeInt(ClinicID);
		dest.writeInt(DoctorID);
		dest.writeInt(Emergency ? 1 : 0);
		dest.writeInt(OPID);
		if (PatientDOB == null) {
			dest.writeLong(0);
		} else {
			dest.writeLong(PatientDOB.getTime());
		}
		dest.writeInt(PatientGender);
		dest.writeInt(PatientID);
		dest.writeString(PatientName);
		dest.writeString(Remark);
		dest.writeLong(WaitingCount);
		dest.writeString(OPDate);
		dest.writeString(CreateTime);
		dest.writeString(DoctorName);
		dest.writeString(OPEMR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Appointment> CREATOR = new Parcelable.Creator<Appointment>() {

		@Override
		public Appointment createFromParcel(Parcel source) {
			return new Appointment(source);
		}

		@Override
		public Appointment[] newArray(int size) {
			return new Appointment[size];
		}
	};

	@Override
	public int compareTo(Appointment object) {
		if (object != null) {
			if (this.getOPNo() > object.getOPNo()) {
				return 1;
			} else if (this.getOPNo() == object.getOPNo()) {
				return 0;
			}
		}
		return -1;
	}
}
