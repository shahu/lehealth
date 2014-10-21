package com.lehealth.bean;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.lehealth.util.Constant;

public class BloodpressureInfo {
	private Date date=new Date();
	private int dbp=0;
	private int sbp=0;
	private int heartrate=0;
	public String getDate() {
		if(date==null){
			return "";
		}
		return DateFormatUtils.format(date, Constant.dateFormat_yyyy_mm_dd);
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
