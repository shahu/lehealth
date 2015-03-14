package com.lehealth.data.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MedicineRecord{
	
	private String userId="";
	private int medicineId=0;
	private String medicineName="";
	private long date=0;
	private Map<String,Float> configs=new HashMap<String, Float>();
	//实际
	private Map<String,Float> situations=new HashMap<String, Float>();
	private String time="";
	private float dosage=0;
	
	public Map<String, Float> getSituations() {
		return situations;
	}
	public void setSituations(Map<String, Float> situations) {
		this.situations = situations;
	}
	public void addSituation(String time,Float dosage) {
		this.situations.put(time,dosage);
		this.time=time;
		this.dosage=dosage;
	}
	public String getTime() {
		return time;
	}
	public float getDosage() {
		return dosage;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
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
	public void setTime(String time) {
		this.time = time;
	}
	public void setDosage(float dosage) {
		this.dosage = dosage;
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
	
	//实际用药信息
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		if(configs!=null&&!configs.isEmpty()){
			obj.accumulate("medicineid", medicineId);
			obj.accumulate("medicinename", medicineName);
			obj.accumulate("date", date);
			JSONArray arr=new JSONArray();
			for(Entry<String,Float> e:configs.entrySet()){
				JSONObject configObj=new JSONObject();
				configObj.accumulate("time", e.getKey());
				configObj.accumulate("dosage", e.getValue());
				if(situations.containsKey(e.getKey())){
					configObj.accumulate("status", 1);
				}else{
					configObj.accumulate("status", 0);
				}
				arr.add(configObj);
			}
			obj.accumulate("results", arr);
		}
		return obj;
	}
	
	public JSONObject toHomeJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("medicinename", medicineName);
		obj.accumulate("date", date);
		return obj;
	}
}
