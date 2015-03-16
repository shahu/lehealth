package com.lehealth.data.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MedicineResult {
	
	private int status=1;
	private List<MedicineRecord> records=new ArrayList<MedicineRecord>();
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<MedicineRecord> getRecords() {
		return records;
	}
	public void setRecords(List<MedicineRecord> records) {
		this.records = records;
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("status", status);
		JSONArray arr=new JSONArray();
		for(MedicineRecord record:records){
			arr.add(record.toJsonObj());
		}
		obj.accumulate("records", arr);
		return obj;
	}
}
