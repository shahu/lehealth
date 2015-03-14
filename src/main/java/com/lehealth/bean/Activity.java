package com.lehealth.bean;

import net.sf.json.JSONObject;

public class Activity {
	
	private int id=0;
	private String name="";
	private long startTime=0;
	private long endTime=0;
	private String location="";
	private String desc="";
	private String externalUrl="";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getExternalUrl() {
		return externalUrl;
	}
	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("id", id);
		obj.accumulate("name", name);
		obj.accumulate("starttime", startTime);
		obj.accumulate("endtime", endTime);
		obj.accumulate("location", location);
		obj.accumulate("desc", desc);
		obj.accumulate("externalurl", externalUrl);
		return obj;
	}
}
