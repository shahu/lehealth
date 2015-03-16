package com.lehealth.data.bean;

import net.sf.json.JSONObject;

public class PanientGuardianInfo {
	
	private String userId="";
	private String guardianName="";
	private String guardianNumber="";
	
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
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("userid", userId);
		obj.accumulate("guardiannumber", guardianNumber);
		obj.accumulate("guardianname", guardianName);
		return obj;
	}
	
}
