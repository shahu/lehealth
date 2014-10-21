package com.lehealth.bean;

import java.util.Date;

public class BloodpressureInfo {
	
	//TODO date不止到天
	private Date date=new Date();
	private int dbp=0;
	private int sbp=0;
	private int heartrate=0;
	
	public long getDate() {
		if(date==null){
			return 0;
		}
		return date.getTime();
	}
	public void setDate(Date date) {
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
	
}
