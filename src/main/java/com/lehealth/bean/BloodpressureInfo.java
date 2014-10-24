package com.lehealth.bean;

public class BloodpressureInfo {
	
	private String userId="";
	//TODO date不止到天
	private long dateStamp=0;
	private int dbp=0;
	private int sbp=0;
	private int heartrate=0;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getDateStamp() {
		return dateStamp;
	}
	public void setDateStamp(long dateStamp) {
		this.dateStamp = dateStamp;
	}
	public int getDbp() {
		return dbp;
	}
	public void setDbp(int dbp) {
		this.dbp = dbp;
	}
	public int getSbp() {
		return sbp;
	}
	public void setSbp(int sbp) {
		this.sbp = sbp;
	}
	public int getHeartrate() {
		return heartrate;
	}
	public void setHeartrate(int heartrate) {
		this.heartrate = heartrate;
	}
	
}
