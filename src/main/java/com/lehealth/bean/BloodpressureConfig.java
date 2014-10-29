package com.lehealth.bean;

import net.sf.json.JSONObject;

public class BloodpressureConfig {

	private String userid="";
	private int dbp1=0;
	private int dbp2=0;
	private int sbp1=0;
	private int sbp2=0;
	private int heartrate1=0;
	private int heartrate2=0;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getDbp1() {
		return dbp1;
	}
	public void setDbp1(int dbp1) {
		this.dbp1 = dbp1;
	}
	public int getDbp2() {
		return dbp2;
	}
	public void setDbp2(int dbp2) {
		this.dbp2 = dbp2;
	}
	public int getSbp1() {
		return sbp1;
	}
	public void setSbp1(int sbp1) {
		this.sbp1 = sbp1;
	}
	public int getSbp2() {
		return sbp2;
	}
	public void setSbp2(int sbp2) {
		this.sbp2 = sbp2;
	}
	public int getHeartrate1() {
		return heartrate1;
	}
	public void setHeartrate1(int heartrate1) {
		this.heartrate1 = heartrate1;
	}
	public int getHeartrate2() {
		return heartrate2;
	}
	public void setHeartrate2(int heartrate2) {
		this.heartrate2 = heartrate2;
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("dbp1", dbp1);
		obj.accumulate("dbp2", dbp2);
		obj.accumulate("sbp1", sbp1);
		obj.accumulate("sbp2", sbp2);
		obj.accumulate("heartrate1", heartrate1);
		obj.accumulate("heartrate2", heartrate2);
		return obj;
	}
}
