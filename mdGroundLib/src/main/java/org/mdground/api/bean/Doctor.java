package org.mdground.api.bean;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 医生实体
 * 
 * @author Vincent
 * 
 */
public class Doctor implements Parcelable {

	@SerializedName("ClinicID")
	@Expose
	private long clinicID;

	@SerializedName("EmployeeID")
	@Expose
	private long EmployeeID;

	@SerializedName("EmployeeName")
	@Expose
	private String EmployeeName;
	
	@SerializedName("EmployeeNickName")
	private String EmployeeNickName;

	@SerializedName("Gender")
	@Expose
	private int Gender;

	@SerializedName("PhotoSID")
	@Expose
	private long PhotoSID;

	@SerializedName("PhotoID")
	@Expose
	private long PhotoID;

	@SerializedName("PhotoURL")
	@Expose
	private String PhotoURL;

	@SerializedName("UpdatedTime")
	@Expose
	private Date UpdatedTime;

	@SerializedName("WorkStatus")
	@Expose
	private long WorkStatus;

	@SerializedName("DoctorID")
	@Expose
	private long DoctorID;

	@SerializedName("DoctorName")
	@Expose
	private String DoctorName;

	@SerializedName("EMRType")
	@Expose
	private String EMRType;

	private int waittingCount;

	private boolean isSelected = false;

	public Doctor() {
		// TODO Auto-generated constructor stub
	}

	public long getClinicID() {
		return clinicID;
	}

	public void setClinicID(long clinicID) {
		this.clinicID = clinicID;
	}

	public long getEmployeeID() {
		return EmployeeID;
	}

	public void setEmployeeID(long employeeID) {
		EmployeeID = employeeID;
	}

	public String getEmployeeName() {
		return EmployeeName;
	}

	public void setEmployeeName(String employeeName) {
		EmployeeName = employeeName;
	}

	public String getEmployeeNickName() {
		return EmployeeNickName;
	}

	public void setEmployeeNickName(String employeeNickName) {
		EmployeeNickName = employeeNickName;
	}

	public int getGender() {
		return Gender;
	}

	public void setGender(int gender) {
		Gender = gender;
	}

	public long getPhotoSID() {
		return PhotoSID;
	}

	public void setPhotoSID(long photoSID) {
		PhotoSID = photoSID;
	}

	public long getPhotoID() {
		return PhotoID;
	}

	public void setPhotoID(long photoID) {
		PhotoID = photoID;
	}

	public String getPhotoURL() {
		// return PhotoURL;
		return "https://file.yideguan.com/View/GetFile.aspx?FileID=" + PhotoSID
				+ "&ClinicID=" + clinicID;
	}

	public void setPhotoURL(String photoURL) {
		PhotoURL = photoURL;
	}

	public Date getUpdatedTime() {
		return UpdatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		UpdatedTime = updatedTime;
	}

	public long getWorkStatus() {
		return WorkStatus;
	}

	public void setWorkStatus(long workStatus) {
		WorkStatus = workStatus;
	}

	public long getDoctorID() {
		return DoctorID;
	}

	public void setDoctorID(long doctorID) {
		DoctorID = doctorID;
	}

	public String getDoctorName() {
		return DoctorName;
	}

	public void setDoctorName(String doctorName) {
		DoctorName = doctorName;
	}

	public String getEMRType() {
		return EMRType;
	}

	public void setEMRType(String eMRType) {
		EMRType = eMRType;
	}

	public Doctor(Parcel source) {
		this.EmployeeID = source.readLong();
		this.EmployeeName = source.readString();
		this.EmployeeNickName = source.readString();
		this.clinicID = source.readLong();
		this.DoctorID = source.readLong();
		this.DoctorName = source.readString();
		this.EMRType = source.readString();
		this.Gender = source.readInt();
		this.PhotoSID = source.readLong();
		this.PhotoID = source.readLong();
		this.PhotoURL = source.readString();
		long tmpDOB = source.readLong();
		this.UpdatedTime = tmpDOB == -1 ? null : new Date(tmpDOB);
		this.WorkStatus = source.readLong();
		this.waittingCount = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.EmployeeID);
		dest.writeString(this.EmployeeName);
		dest.writeString(this.EmployeeNickName);
		dest.writeLong(clinicID);
		dest.writeLong(DoctorID);
		dest.writeString(DoctorName);
		dest.writeString(EMRType);
		dest.writeInt(Gender);
		dest.writeLong(PhotoSID);
		dest.writeLong(PhotoID);
		dest.writeString(PhotoURL);
		dest.writeLong(UpdatedTime != null ? UpdatedTime.getTime() : -1);
		dest.writeLong(WorkStatus);
		dest.writeInt(waittingCount);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getWaittingCount() {
		return waittingCount;
	}

	public void setWaittingCount(int waittingCount) {
		this.waittingCount = waittingCount;
	}

	public static final Parcelable.Creator<Doctor> CREATOR = new Parcelable.Creator<Doctor>() {

		@Override
		public Doctor createFromParcel(Parcel source) {
			return new Doctor(source);
		}

		@Override
		public Doctor[] newArray(int size) {
			return new Doctor[size];
		}
	};

}
