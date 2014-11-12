package com.lehealth.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MedicineConfig {

	private String userid="";
	private int medicineid=0;
	private String medicinename="";
	private Map<String,Float> configs=new HashMap<String, Float>();
	private long datefrom=0;
	private long dateto=0;
	
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
	public long getDatefrom() {
		return datefrom;
	}
	public void setDatefrom(long datefrom) {
		this.datefrom = datefrom;
	}
	public long getDateto() {
		return dateto;
	}
	public void setDateto(long dateto) {
		this.dateto = dateto;
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
	public void addConfig(String time,float dosage) {
		this.configs.put(time, dosage);
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		if(configs!=null&&!configs.isEmpty()){
			obj.accumulate("medicineid", medicineid);
			obj.accumulate("medicinename", medicinename);
			obj.accumulate("datefrom", datefrom);
			obj.accumulate("dateto", dateto);
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
