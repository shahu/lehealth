package com.lehealth.bean;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.lehealth.util.Constant;

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
	public String getDatefrom() {
		if(startTime==null){
			return "";
		}
		return DateFormatUtils.format(startTime, Constant.dateFormat_yyyy_mm_dd);
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getDateto() {
		if(endTime==null){
			return "";
		}
		return DateFormatUtils.format(endTime, Constant.dateFormat_yyyy_mm_dd);
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
