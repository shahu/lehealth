package com.lehealth.bean;

import net.sf.json.JSONObject;

public class MedicineConfig {

	private String userid="";
	private int medicineid=0;
	private String medicinename="";
	private float amount=0;
	private float frequency=0;
	private int timing=0;
	private long datefrom=0;
	private long dateto=0;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getMedicineid() {
		return medicineid;
	}
	public void setMedicineid(int medicineid) {
		this.medicineid = medicineid;
	}
	public long getDatefrom() {
		return datefrom;
	}
	public void setDatefrom(long datefrom) {
		this.datefrom = datefrom;
	}
	public long getDateto() {
		return dateto;
	}
	public void setDateto(long dateto) {
		this.dateto = dateto;
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
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("medicineid", medicineid);
		obj.accumulate("medicinename", medicinename);
		obj.accumulate("amount", amount);
		obj.accumulate("frequency", frequency);
		obj.accumulate("timing", timing);
		obj.accumulate("datefrom", datefrom);
		obj.accumulate("dateto", dateto);
		return obj;
	}
}
