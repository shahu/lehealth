package com.lehealth.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.lehealth.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HomeResult{
	private List<BloodpressureRecord> bpRecords=new ArrayList<BloodpressureRecord>();
	private BloodpressureConfig bpConfig=new BloodpressureConfig();
	private List<MedicineRecord> medicineRecords=new ArrayList<MedicineRecord>();
	private int days=7;
	
	public void setBpRecords(List<BloodpressureRecord> bpRecords) {
		this.bpRecords = bpRecords;
	}
	public void setBpConfig(BloodpressureConfig bpConfig) {
		this.bpConfig = bpConfig;
	}
	public void setMedicineRecords(List<MedicineRecord> medicineRecords) {
		this.medicineRecords = medicineRecords;
	}
	public void setDays(int days) {
		this.days = days;
	}

	public HomeResult(int days){
		super();
		this.days=days;
	}
	
	public int getStatus() {
		for(BloodpressureRecord record:bpRecords){
			if(record.getDbp()>=bpConfig.getDbp2()
				||record.getSbp()>=bpConfig.getSbp2()
				||record.getHeartrate()>=bpConfig.getHeartrate2()){
				return 3;
			}else if(record.getDbp()<=bpConfig.getDbp1()
				||record.getSbp()<=bpConfig.getSbp1()
				||record.getHeartrate()<=bpConfig.getHeartrate1()){
				return 1;
			}
		}
		return 2;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		
		Map<String,BloodpressureRecord> bpTemp=new HashMap<String, BloodpressureRecord>();
		for(BloodpressureRecord record:bpRecords){
			bpTemp.put(DateFormatUtils.format(new Date(record.getDate()), Constant.dateFormat_yyyy_mm_dd),record);
		}
		
		Date today=new Date();
		Date tempDate=DateUtils.addDays(today, -days);
		
		JSONArray bpArr=new JSONArray();
		while(!tempDate.after(today)){
			String temp=DateFormatUtils.format(tempDate, Constant.dateFormat_yyyy_mm_dd);
			if(bpTemp.containsKey(temp)){
				bpArr.add(bpTemp.get(temp).toJsonObj());
			}
			tempDate=DateUtils.addDays(tempDate, 1);
		}
		
		JSONArray mArr=new JSONArray();
		for(MedicineRecord record:medicineRecords){
			mArr.add(record.toHomeJsonObj());
		}
		if(bpArr.size()>0){
			obj.accumulate("status", getStatus());
			obj.accumulate("records", bpArr);
		}
		if(mArr.size()>0){
			obj.accumulate("history", mArr);
		}
		return obj;
	}
}
