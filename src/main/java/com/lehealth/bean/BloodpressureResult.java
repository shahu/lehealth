package com.lehealth.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BloodpressureResult {
	
	private String status="1";
	private int score=0;
	private List<BloodpressureInfo> records=new ArrayList<BloodpressureInfo>();
	
	public void setStatus(String status) {
		this.status = status;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void setRecords(List<BloodpressureInfo> records) {
		this.records = records;
	}
	public JSONObject toJsonObj(){
		JSONObject obj=new JSONObject();
		obj.accumulate("status", status);
		obj.accumulate("score", score);
		JSONArray arr=new JSONArray();
		for(BloodpressureInfo record:records){
			arr.add(record.toJsonObj());
		}
		obj.accumulate("records", arr);
		return obj;
	}
}
