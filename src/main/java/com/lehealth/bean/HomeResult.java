package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HomeResult{
	private List<BloodpressureRecord> records=new ArrayList<BloodpressureRecord>();
	private BloodpressureConfig config=new BloodpressureConfig();
	private List<MedicineRecord> medicineecords=new ArrayList<MedicineRecord>();
	
	public List<MedicineRecord> getMedicineecords() {
		return medicineecords;
	}
	public void setMedicineecords(List<MedicineRecord> medicineecords) {
		this.medicineecords = medicineecords;
	}
	public void setConfig(BloodpressureConfig config) {
		this.config = config;
	}
	public void setRecords(List<BloodpressureRecord> records) {
		this.records = records;
	}
	
	public int getStatus() {
		for(BloodpressureRecord record:records){
			if(record.getDbp()>=config.getDbp2()
				||record.getSbp()>=config.getSbp2()
				||record.getHeartrate()>=config.getHeartrate2()){
				return 3;
			}else if(record.getDbp()<=config.getDbp1()
				||record.getSbp()<=config.getSbp1()
				||record.getHeartrate()<=config.getHeartrate1()){
				return 1;
			}
		}
		return 2;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		if(records!=null&&!records.isEmpty()){
			obj.accumulate("status", getStatus());
			JSONArray arr=new JSONArray();
			for(BloodpressureRecord record:records){
				arr.add(record.toJsonObj());
			}
			obj.accumulate("records", arr);
		}
		if(medicineecords!=null&&!medicineecords.isEmpty()){
			JSONArray arr=new JSONArray();
			for(MedicineRecord record:medicineecords){
				arr.add(record.toHomeJsonObj());
			}
			obj.accumulate("history", arr);
		}
		return obj;
	}
}
