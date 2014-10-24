package com.lehealth.bean;

public class MedicineConfig {

	private String userId="";
	private int medicineId=0;
	private float amount=0;
	private float frequency=0;
	private int timing=0;
	private long datefromStamp=0;
	private long datetoStamp=0;
	
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getDatefromStamp() {
		return datefromStamp;
	}
	public void setDatefromStamp(long datefromStamp) {
		this.datefromStamp = datefromStamp;
	}
	public long getDatetoStamp() {
		return datetoStamp;
	}
	public void setDatetoStamp(long datetoStamp) {
		this.datetoStamp = datetoStamp;
	}
	
}
