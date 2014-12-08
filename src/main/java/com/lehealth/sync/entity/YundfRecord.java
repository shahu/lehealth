package com.lehealth.sync.entity;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.lehealth.util.Constant;

public class YundfRecord {
	//舒张压
	private int diastolic=0;
	//心率
	private int pulse=0;
	//收缩压
	private int systolic=0;
	private long rdatetime=0;
	private int rid=0;
//	private String did="";
//	private String arrhythmia="";
//	private String identity="";
//	private String sequence="";
//	private String uid="";
	
	public int getDiastolic() {
		return diastolic;
	}
	public void setDiastolic(int diastolic) {
		this.diastolic = diastolic;
	}
	public int getPulse() {
		return pulse;
	}
	public void setPulse(int pulse) {
		this.pulse = pulse;
	}
	public int getSystolic() {
		return systolic;
	}
	public void setSystolic(int systolic) {
		this.systolic = systolic;
	}
	public long getRdatetime() {
		return rdatetime;
	}
	public void setRdatetime(String rdatetime) {
		try {
			Date date = DateUtils.parseDate(rdatetime, Constant.dateFormat_yyyy_mm_dd_hh_mm_ss);
			this.rdatetime = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
}
