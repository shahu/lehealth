package com.lehealth.bean;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.lehealth.util.Constant;

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
	public String getStartTime() {
		if(startTime==null){
			return "";
		}
		return DateFormatUtils.format(startTime, Constant.dateFormat_yyyy_mm_dd_hh_mm_ss);
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getEndtime() {
		if(endtime==null){
			return "";
		}
		return DateFormatUtils.format(endtime, Constant.dateFormat_yyyy_mm_dd_hh_mm_ss);
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
