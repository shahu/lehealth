package com.lehealth.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MedicineInfo {
	private int status=0;
	//药品id-服用时间
	private Map<Integer,ArrayList<Date>> medicineTimes=new HashMap<Integer, ArrayList<Date>>();
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<Integer, ArrayList<Date>> getMedicineTimes() {
		return medicineTimes;
	}
	public void setMedicineTimes(Map<Integer, ArrayList<Date>> medicineTimes) {
		this.medicineTimes = medicineTimes;
	}
}
