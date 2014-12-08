package com.lehealth.sync.entity;

import java.util.ArrayList;
import java.util.List;

public class YundfUser {

	private String accId="";
	private String phone="";
	private String userId="";
	private int lastRid=0;
	private List<YundfRecord> records=new ArrayList<YundfRecord>();
//	private String markname="";
//	private String access="";
//	private String atype="";
	
//	private String nickname="";
//	private String realname="";
//	private String gender="";
//	private String birthday="";
//	private String province="";
//	private String city="";
//	private String tall="";
//	private String weight="";
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getLastRid() {
		return lastRid;
	}
	public void setLastRid(int lastRid) {
		this.lastRid = lastRid;
	}
	public List<YundfRecord> getRecords() {
		return records;
	}
	public void setRecords(List<YundfRecord> records) {
		this.records = records;
	}
	public void addRecord(YundfRecord record) {
		this.records.add(record);
	}
}
