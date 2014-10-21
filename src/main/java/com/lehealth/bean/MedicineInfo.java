package com.lehealth.bean;

import java.util.Date;

public class MedicineInfo {
	
	private Date date=new Date();
	//TODO 用id好点
	private String medicinename="";
	private int amount=0;
	private int frequency=0;
	
	public long getDate() {
		if(date==null){
			return 0;
		}
		return date.getTime();
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMedicinename() {
		return medicinename;
	}
	public void setMedicinename(String medicinename) {
		this.medicinename = medicinename;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
}
