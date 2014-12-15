package com.lehealth.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MedicineConfig {

	private String userId="";
	private int medicineId=0;
	private String medicineName="";
	private Map<String,Float> configs=new HashMap<String, Float>();
	private long dateFrom=0;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getMedicineId() {
		return medicineId;
	}
	public void setMedicineId(int medicineId) {
		this.medicineId = medicineId;
	}
	public String getMedicineName() {
		return medicineName;
	}
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}
	public long getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(long dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Map<String, Float> getConfigs() {
		return configs;
	}
	public void setConfigs(Map<String, Float> configs) {
		this.configs = configs;
	}
	public void addConfig(String time,float dosage) {
		this.configs.put(time, dosage);
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		if(configs!=null&&!configs.isEmpty()){
			obj.accumulate("medicineid", medicineId);
			obj.accumulate("medicinename", medicineName);
			obj.accumulate("datefrom", dateFrom);
			JSONArray arr=new JSONArray();
			for(Entry<String, Float> e : configs.entrySet()){
				JSONObject config=new JSONObject();
				config.accumulate("time", e.getKey());
				config.accumulate("dosage", e.getValue());
				arr.add(config);
			}
			obj.accumulate("configs", arr);
		}
		return obj;
	}
}
