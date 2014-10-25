package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import com.lehealth.util.JacksonGlobalMappers;

public class BloodpressureResult {
	
	private String status="1";
	private int score=0;
	private List<BloodpressureInfo> records=new ArrayList<BloodpressureInfo>();
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public List<BloodpressureInfo> getRecords() {
		return records;
	}
	public void setRecords(List<BloodpressureInfo> records) {
		this.records = records;
	}
}
