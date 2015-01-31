package com.lehealth.bean;

import net.sf.json.JSONObject;

public class BloodpressureRecord {
	
	private String userId="";
	private long date=0;
	private int dbp=0;
	private int sbp=0;
	private int heartrate=0;
	private int dosed=-1;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
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
	public int getDosed() {
		return dosed;
	}
	public void setDosed(int dosed) {
		this.dosed = dosed;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("date", date);
		obj.accumulate("dbp", dbp);
		obj.accumulate("sbp", sbp);
		obj.accumulate("heartrate", heartrate);
		obj.accumulate("dosed", dosed);
		return obj;
	}
}
