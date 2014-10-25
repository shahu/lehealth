package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

public class MedicineResult {
	
	private int status=1;
	private int score=0;
	private List<MedicineInfo> records=new ArrayList<MedicineInfo>();
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<MedicineInfo> getRecords() {
		return records;
	}
	public void setRecords(List<MedicineInfo> records) {
		this.records = records;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
