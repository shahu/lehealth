package com.lehealth.bean;

import net.sf.json.JSONObject;

public class UserGuardianInfo {
	
	private String userId="";
	private String guardianName="";
	private String guardianNumber="";
	private int needNotice=0;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGuardianName() {
		return guardianName;
	}
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	public String getGuardianNumber() {
		return guardianNumber;
	}
	public void setGuardianNumber(String guardianNumber) {
		this.guardianNumber = guardianNumber;
	}
	public int getNeedNotice() {
		return needNotice;
	}
	public void setNeedNotice(int needNotice) {
		this.needNotice = needNotice;
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("userid", userId);
		obj.accumulate("guardiannumber", guardianNumber);
		obj.accumulate("guardianname", guardianName);
		obj.accumulate("neednotice", needNotice);
		return obj;
	}
	
}
