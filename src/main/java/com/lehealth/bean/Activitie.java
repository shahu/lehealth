package com.lehealth.bean;

public class Activitie {
	
	private int id=0;
	private String name="";
	private long startTimeStamp=0;
	private long endtimeStamp=0;
	//TODO 医院id
	private String location="";
	private String desc="";
	//TODO 报名状态
	private String externalurl="";
	
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
	public long getStartTimeStamp() {
		return startTimeStamp;
	}
	public void setStartTimeStamp(long startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}
	public long getEndtimeStamp() {
		return endtimeStamp;
	}
	public void setEndtimeStamp(long endtimeStamp) {
		this.endtimeStamp = endtimeStamp;
	}
	public String getExternalurl() {
		return externalurl;
	}
	public void setExternalurl(String externalurl) {
		this.externalurl = externalurl;
	}
}
