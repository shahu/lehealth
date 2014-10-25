package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import com.lehealth.util.JacksonGlobalMappers;

public class MedicineResult {
	
	private int status=1;
	private int score=0;
	private List<MedicineInfo> medicineTimes=new ArrayList<MedicineInfo>();
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<MedicineInfo> getMedicineTimes() {
		return medicineTimes;
	}
	public void setMedicineTimes(List<MedicineInfo> medicineTimes) {
		this.medicineTimes = medicineTimes;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public String toJson(){
		return JacksonGlobalMappers.getNoNullJsonStr(this);
	}
}
