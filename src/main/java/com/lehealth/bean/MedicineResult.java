package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MedicineResult {
	
	private int status=1;
	private List<MedicineInfo> records=new ArrayList<MedicineInfo>();
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<MedicineInfo> getRecords() {
		return records;
	}
	public void setRecords(List<MedicineInfo> records) {
		this.records = records;
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("status", status);
		JSONArray arr=new JSONArray();
		for(MedicineInfo record:records){
			arr.add(record.toJsonObj());
		}
		obj.accumulate("records", arr);
		return obj;
	}
}
