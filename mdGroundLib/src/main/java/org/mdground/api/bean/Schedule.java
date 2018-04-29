package org.mdground.api.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

import org.mdground.api.utils.DateUtils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 值班表数据
 * 
 * @author Vincent
 * 
 */
@Table(name = "schedule")
public class Schedule implements Parcelable {

	@Expose
	@SerializedName("ESID")
	@Id(column = "id")
	private long id;

	// 工作状态
	@Expose
	@SerializedName("WorkStatus")
	private long status;

	@Expose
	@SerializedName("BeginHour")
	private int beginHour = -1;

	@Expose
	@SerializedName("EndHour")
	private int endHour = -1;

	@Expose
	@SerializedName("UpdatedTime")
	private Date updateTime;

	@Expose
	@SerializedName("ClinicID")
	private long clinicId;

	@Expose
	@SerializedName("WorkDate")
	private Date workDate;
	
	@Expose
	@SerializedName("EmployeeID")
	private long employeeID;

	@Transient
	@Expose
	private transient boolean isVacation = false;

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public int getBeginHour() {
		return beginHour;
	}

	public void setBeginHour(int beginHour) {
		this.beginHour = beginHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public long getClinicId() {
		return clinicId;
	}

	public void setClinicId(long clinicId) {
		this.clinicId = clinicId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public String getWorkDateStr() {
		if (this.workDate != null) {
			return DateUtils.getDateString(workDate, new SimpleDateFormat("MM月dd日", Locale.CHINA));
		}
		return "";
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(long employeeID) {
		this.employeeID = employeeID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Schedule [id=" + id + ", status=" + status + ", beginHour=" + beginHour + ", endHour=" + endHour + ", updateTime=" + updateTime
				+ ", clinicId=" + clinicId + ", workDate=" + workDate + ", employeeID=" + employeeID + "]";
	}

	public Schedule(Parcel source) {
	}

	public Schedule() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}

	public boolean isVacation() {
		return isVacation;
	}

	public void setVacation(boolean isVacation) {
		this.isVacation = isVacation;
	}

	public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {

		@Override
		public Schedule createFromParcel(Parcel source) {
			return new Schedule(source);
		}

		@Override
		public Schedule[] newArray(int size) {
			return new Schedule[size];
		}
	};

}
