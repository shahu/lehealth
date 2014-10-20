package com.lehealth.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BpInfo {
	private int status=1;
	
	//时刻-血压值
	private Map<Date,Integer> dbp=new HashMap<Date, Integer>();
	private Map<Date,Integer> sbp=new HashMap<Date, Integer>();
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<Date, Integer> getDbp() {
		return dbp;
	}
	public void setDbp(Map<Date, Integer> dbp) {
		this.dbp = dbp;
	}
	public Map<Date, Integer> getSbp() {
		return sbp;
	}
	public void setSbp(Map<Date, Integer> sbp) {
		this.sbp = sbp;
	}
	
}
