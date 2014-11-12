package com.lehealth.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MedicineInfo {
	
	private String userid="";
	private int medicineid=0;
	private String medicinename="";
	private long date=0;
	//计划
	private Map<String,Float> configs=new HashMap<String, Float>();
	//实际
	private Map<String,Float> situations=new HashMap<String, Float>();
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getMedicineid() {
		return medicineid;
	}
	public void setMedicineid(int medicineid) {
		this.medicineid = medicineid;
	}
	public String getMedicinename() {
		return medicinename;
	}
	public void setMedicinename(String medicinename) {
		this.medicinename = medicinename;
	}
	public Map<String, Float> getConfigs() {
		return configs;
	}
	public void setConfigs(Map<String, Float> configs) {
		this.configs = configs;
	}
	public void addConfig(String time,Float dosage) {
		this.configs.put(time,dosage);
	}
	public Map<String, Float> getSituations() {
		return situations;
	}
	public void setSituations(Map<String, Float> situations) {
		this.situations = situations;
	}
	public void addSituation(String time,Float dosage) {
		this.configs.put(time,dosage);
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	//实际用药信息
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		if(configs!=null&&!configs.isEmpty()){
			obj.accumulate("medicineid", medicineid);
			obj.accumulate("medicinename", medicinename);
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
}
