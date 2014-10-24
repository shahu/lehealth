package com.lehealth.bean;

public class MedicineInfo {
	
	private String userId="";
	private long dateStamp=0;
	private int medicineId=0;
	private float amount=0;
	private float frequency=0;
	private int timing=0;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getDateStamp() {
		return dateStamp;
	}
	public void setDateStamp(long dateStamp) {
		this.dateStamp = dateStamp;
	}
	public int getMedicineId() {
		return medicineId;
	}
	public void setMedicineId(int medicineId) {
		this.medicineId = medicineId;
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
	
}
