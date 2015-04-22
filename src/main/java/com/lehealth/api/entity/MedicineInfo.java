package com.lehealth.api.entity;

import net.sf.json.JSONObject;

public class MedicineInfo {
	
	private int id=0;
	private String name="";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("id", id);
		obj.accumulate("name", name);
		return obj;
	}
}
