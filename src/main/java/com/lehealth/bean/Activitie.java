package com.lehealth.bean;

import java.util.Date;

public class Activitie {
	
	private int id=0;
	private String name="";
	private Date startTime=new Date();
	private Date endtime=new Date();
	//TODO 医院id
	private String location="";
	private String desc="";
	//TODO 报名状态
	
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
	public long getStartTime() {
		if(startTime==null){
			return 0;
		}
		return startTime.getTime();
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public long getEndtime() {
		if(endtime==null){
			return 0;
		}
		return endtime.getTime();
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
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
	
}
