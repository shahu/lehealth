package com.lehealth.bean;

import net.sf.json.JSONObject;

public class MedicineInfo {
	
	private String userid="";
	private long date=0;
	private int medicineid=0;
	private String medicinename="";
	//用药设置信息
	private float schedulamount=0;
	private float schedulfrequency=0;
	private int schedultiming=0;
	//实际用药信息
	private float amount=0;
	private float frequency=0;
	private int timing=0;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getMedicineid() {
		return medicineid;
	}
	public void setMedicineid(int medicineid) {
		this.medicineid = medicineid;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getFrequency() {
		return frequency;
	}
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
	public int getTiming() {
		return timing;
	}
	public void setTiming(int timing) {
		this.timing = timing;
	}
	public String getMedicinename() {
		return medicinename;
	}
	public void setMedicinename(String medicinename) {
		this.medicinename = medicinename;
	}
	public float getSchedulamount() {
		return schedulamount;
	}
	public void setSchedulamount(float schedulamount) {
		this.schedulamount = schedulamount;
	}
	public float getSchedulfrequency() {
		return schedulfrequency;
	}
	
	public void setSchedulfrequency(float schedulfrequency) {
		this.schedulfrequency = schedulfrequency;
	}
	public int getSchedultiming() {
		return schedultiming;
	}
	public void setSchedultiming(int schedultiming) {
		this.schedultiming = schedultiming;
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("date", date);
		obj.accumulate("medicinename", medicinename);
		obj.accumulate("amount", amount);
		obj.accumulate("frequency", frequency);
		return obj;
	}
}
