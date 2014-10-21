package com.lehealth.bean;

import java.util.Date;

public class MedicineConfig {

	private int medicineId=0;
	private int amount=0;
	private int frequency=0;
	private int timing=0;
	private Date startTime=new Date();
	private Date endTime=new Date();
	
	public int getMedicineId() {
		return medicineId;
	}
	public void setMedicineId(int medicineId) {
		this.medicineId = medicineId;
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
	public int getTiming() {
		return timing;
	}
	public void setTiming(int timing) {
		this.timing = timing;
	}
	public long getDatefrom() {
		if(startTime==null){
			return 0;
		}
		return startTime.getTime();
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public long getDateto() {
		if(endTime==null){
			return 0;
		}
		return endTime.getTime();
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
