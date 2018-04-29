package com.mdground.screen.bean;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class DocotorBean implements Parcelable{
	private String name;
	private int drawableId;
	private String currentNum;
	private List<String>nums;
	public DocotorBean(){
		
	}
	public DocotorBean(String name,int drawableId,String currentNum,List<String>nums){
	    this.name=name;
	    this.drawableId=drawableId;
	    this.currentNum=currentNum;
	    this.nums=nums;
	}
	
	public int getDrawableId() {
		return drawableId;
	}
	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrentNum() {
		return currentNum;
	}
	public void setCurrentNum(String currentNum) {
		this.currentNum = currentNum;
	}
	public List<String> getNums() {
		return nums;
	}
	public void setNums(List<String> nums) {
		this.nums = nums;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(currentNum);
		dest.writeStringList(nums);	
		dest.writeInt(drawableId);
	}
	public static final Parcelable.Creator<DocotorBean> CREATOR = new Creator<DocotorBean>() {  
		@Override  
		public DocotorBean createFromParcel(Parcel source) {  
		DocotorBean docotor = new DocotorBean();  
		docotor.name = source.readString();  
		docotor.currentNum = source.readString();  
		docotor.nums=source.createStringArrayList();
		docotor.drawableId=source.readInt();
		return docotor;  
		}  
		@Override  
		public DocotorBean[] newArray(int size) {  
		// TODO Auto-generated method stub  
		return new DocotorBean[size];  
		}  
	};
	

}
