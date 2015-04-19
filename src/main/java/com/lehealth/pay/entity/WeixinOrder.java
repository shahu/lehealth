package com.lehealth.pay.entity;

import net.sf.json.JSONObject;

public class WeixinOrder {
	
	private int id=0;
	private String name="";
	private long startTime=0;
	private long endTime=0;
	private String location="";
	private String desc="";
	private String externalUrl="";
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDesc() {
		return this.desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public long getStartTime() {
		return this.startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return this.endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getExternalUrl() {
		return this.externalUrl;
	}
	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("id", this.id);
		obj.accumulate("name", this.name);
		obj.accumulate("starttime", this.startTime);
		obj.accumulate("endtime", this.endTime);
		obj.accumulate("location", this.location);
		obj.accumulate("desc", this.desc);
		obj.accumulate("externalurl", this.externalUrl);
		return obj;
	}
}
