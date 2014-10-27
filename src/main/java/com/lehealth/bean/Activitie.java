package com.lehealth.bean;

import net.sf.json.JSONObject;

public class Activitie {
	
	private int id=0;
	private String name="";
	private long starttime=0;
	private long endtime=0;
	//TODO 医院id
	private String location="";
	private String desc="";
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
	public long getStarttime() {
		return starttime;
	}
	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}
	public long getEndtime() {
		return endtime;
	}
	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}
	public String getExternalurl() {
		return externalurl;
	}
	public void setExternalurl(String externalurl) {
		this.externalurl = externalurl;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("id", id);
		obj.accumulate("name", name);
		obj.accumulate("starttime", starttime);
		obj.accumulate("endtime", endtime);
		obj.accumulate("location", location);
		obj.accumulate("desc", desc);
		obj.accumulate("externalurl", externalurl);
		return obj;
	}
}
