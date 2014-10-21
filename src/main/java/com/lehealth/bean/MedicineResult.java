package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

public class MedicineResult {
	
	private int status=0;
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
}
